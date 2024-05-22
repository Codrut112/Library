package service;

import domain.*;
import observer.Observable;
import observer.Observer;
import persistence.IRepositoryBook;
import persistence.IRepositoryBookAction;
import persistence.IRepositoryPerson;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Service implements Observable {
    private IRepositoryPerson<Subscriber> repositoryPerson;
    private IRepositoryBook repositoryBook;
    private IRepositoryBookAction repositoryBookBorrowing;
    private IRepositoryBookAction repositoryBookRate;
    private IRepositoryPerson<Librarian> repositoryLibrarian;

    public Service(IRepositoryPerson repositoryPerson, IRepositoryBook repositoryBook, IRepositoryBookAction repositoryBookBorrowing, IRepositoryBookAction repositoryBookRate, IRepositoryPerson repositoryLibrarian) {
        this.repositoryPerson = repositoryPerson;
        this.repositoryBook = repositoryBook;
        this.repositoryBookBorrowing = repositoryBookBorrowing;
        this.repositoryBookRate = repositoryBookRate;
        this.repositoryLibrarian = repositoryLibrarian;
    }
    public Person verifyUser(String username,String password) {
         var person=repositoryPerson.findByUsernameAndPassword(username,password);
          if(person.isEmpty()){
              var librarian=repositoryLibrarian.findByUsernameAndPassword(username,password);
              if(librarian.isEmpty() || !librarian.get().getPassword().equals(password))throw new RuntimeException("parola gresita");
              else return librarian.get();
          }
          if(person.get().getPassword().equals(password))return person.get();
          else throw new RuntimeException("parola gresita");


    }
    public List<Book> findAvailableBooks() {
        HashSet<Book> books = new HashSet<>();
        for (var book : repositoryBook.findAll()) {
            //System.out.println(book);
            if (book.isAvailable()) {
                books.add(book);
            }

        }
        return List.copyOf(books);
    }


    public void addSubscriber(String username,String password,String nume,String cnp,String phone){
        var subscriber=new Subscriber(username,password,nume,cnp,phone);
        System.out.println("am ajuns aici"+subscriber);
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
        //observers.forEach(observer::update);
    }

    public void reserveBooks(Subscriber user, List<Book> selectedBooks) {
        selectedBooks.forEach(book -> {
            if (!book.isAvailable()) {
                throw new RuntimeException("Book is not available");
            }
            else {
                book.setAvailable(false);
                repositoryBook.update(book);
            }
        });
        repositoryBookBorrowing.add(new BookBorrowing(BorrowStatus.Reserved, LocalDateTime.now(),LocalDateTime.now().plusDays(1),user ,new HashSet<>(selectedBooks)));

    }
}
