package service;

import domain.*;
import observer.Observable;
import observer.Observer;
import persistence.IRepositoryBook;
import persistence.IRepositoryBookAction;
import persistence.IRepositoryPerson;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class Service implements Observable {
    private IRepositoryPerson<Subscriber> repositoryPerson;
    private IRepositoryBook repositoryBook;
    private IRepositoryBookAction<BookBorrowing> repositoryBookBorrowing;
    private IRepositoryBookAction<BookRate> repositoryBookRate;
    private IRepositoryPerson<Librarian> repositoryLibrarian;

    public Service(IRepositoryPerson repositoryPerson, IRepositoryBook repositoryBook, IRepositoryBookAction repositoryBookBorrowing, IRepositoryBookAction repositoryBookRate, IRepositoryPerson repositoryLibrarian) {
        this.repositoryPerson = repositoryPerson;
        this.repositoryBook = repositoryBook;
        this.repositoryBookBorrowing = repositoryBookBorrowing;
        this.repositoryBookRate = repositoryBookRate;
        this.repositoryLibrarian = repositoryLibrarian;
    }

    public Person verifyUser(String username, String password) {
        var person = repositoryPerson.findByUsernameAndPassword(username, password);
        if (person.isEmpty()) {
            var librarian = repositoryLibrarian.findByUsernameAndPassword(username, password);
            if (librarian.isEmpty() || !librarian.get().getPassword().equals(password))
                throw new RuntimeException("parola gresita");
            else return librarian.get();
        }
        if (person.get().getPassword().equals(password)) return person.get();
        else throw new RuntimeException("parola gresita");


    }

    public List<Book> findAvailableBooks() {

        HashSet<Book> books = new HashSet<>();
        for (var book : repositoryBook.findAll()) {
            System.out.println(book);
            if (book.isAvailable()) {
                books.add(book);
            }

        }
        return List.copyOf(books);
    }


    public void addSubscriber(String username, String password, String nume, String cnp, String phone) {
        var subscriber = new Subscriber(username, password, nume, cnp, phone);
        System.out.println("am ajuns aici" + subscriber);
        repositoryPerson.add(subscriber);
    }


    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyall() {
        observers.forEach(Observer::update);
    }

    public void reserveBooks(Subscriber user, List<Book> selectedBooks) {
        selectedBooks.forEach(book -> {
            if (!book.isAvailable()) {
                throw new RuntimeException("Book is not available");
            } else {
                book.setAvailable(false);
                repositoryBook.update(book);
            }
        });
        repositoryBookBorrowing.add(new BookBorrowing(BorrowStatus.Reserved, LocalDateTime.now(), LocalDateTime.now().plusDays(1), user, new HashSet<>(selectedBooks)));
        notifyall();
    }
    @Transactional
    public List<BookBorrowing> findUserBooks(Person subscriber) {
      HashSet<BookBorrowing> books = new HashSet<>();
        for (BookBorrowing borrow : repositoryBookBorrowing.findAll()) {
            //System.out.println(book);
            if (borrow.getSubscriber().equals(subscriber) && borrow.getStatus()!=BorrowStatus.Reserved) {
                books.add(borrow);
            }

        }
        System.out.println(books.size());
        System.out.println(books.size());
        return List.copyOf(books);
    }
    public List<Book> findAllBooks(){
        return repositoryBook.findAll();
    }

    public void borrowBook(BookBorrowing reservation,Book selectedBook) {
        selectedBook.setStatus(BookStatus.Borrowed);
        repositoryBook.update(selectedBook);
        reservation.setStatus(BorrowStatus.Acticve);
        repositoryBookBorrowing.update(reservation);
    }

    public Person findUserByUsername(String username){
       var person= repositoryPerson.findByUsername(username);
       return person.orElse(null);
}

    public BookBorrowing verifyReservation(Person subscriber, Book book) {
        for (BookBorrowing borrow : repositoryBookBorrowing.findAll()) {
           // System.out.println("imprumut:"+borrow.getSubscriber().equals(subscriber));
          //  System.out.println("dadada:"+borrow.getBooks().contains(book));
            if (borrow.getSubscriber().equals(subscriber) && borrow.getBooks().contains(book)) {
                return borrow;
            }
        }
        return null;
    }

    public void returnBook(Book book) {
       book.setStatus(BookStatus.Available);
         repositoryBook.update(book);
         notifyall();
    }

    public void addBook(String isbn, String author, String title, int publicationYear,BookStatus status, Genre genre) {
        var newBook=new Book(isbn,author,title,publicationYear,status,genre);
        repositoryBook.add(newBook);
        notifyall();
    }

    public void updateBook(Book selectedBook) {
        repositoryBook.update(selectedBook);
        notifyall();
    }

    public void deleteBook(Book selectedBook) {
        repositoryBook.delete(selectedBook.getId());
       // repositoryBookBorrowing.findAll().stream().filter(borrow -> borrow.getBooks().contains(selectedBook)).forEach(borrow -> repositoryBookBorrowing.delete(borrow.getId()));
        notifyall();
    }

    public Book findBookById(String bookId) {
        return repositoryBook.findOne(bookId).orElse(null);
    }

    public void rateBook(BookBorrowing borrowing, Integer value) {
        System.out.println("id:"+borrowing.getId());
        BookRate rate = new BookRate(BorrowStatus.Ended, borrowing.getStartDate(), borrowing.getEndDate(), borrowing.getSubscriber(), borrowing.getBooks(), value);
        repositoryBookBorrowing.delete(borrowing.getId());
        repositoryBookRate.add(rate);

    }

    public void clearOldReservations() {
        repositoryBookBorrowing.findAll().stream().filter(borrow -> borrow.getEndDate().isBefore(LocalDateTime.now().minusDays(1)) && borrow.getStatus()==BorrowStatus.Reserved).forEach(borrow -> {
            borrow.getBooks().forEach(book -> {
                book.setAvailable(true);
                repositoryBook.update(book);
            });
            repositoryBookBorrowing.delete(borrow.getId());
        });
    }
}