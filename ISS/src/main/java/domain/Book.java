package domain;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "Books")
public class Book extends Entity {

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "author")
    private String author;

    @Column(name = "title")
    private String title;

    @Column(name = "publication_year")
    private int publicationYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @ManyToMany(mappedBy = "books")
    private Set<BookBorrowing> borrowings;

    public Book() {
        super();
    }

    public Book(String isbn, String author, String title, int publicationYear, BookStatus status, Genre genre) {
        this.isbn = isbn;
        this.author = author;
        this.title = title;
        this.publicationYear = publicationYear;
        this.status = status;
        this.genre = genre;
    }

    // Getters and setters

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Set<BookBorrowing> getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(Set<BookBorrowing> borrowings) {
        this.borrowings = borrowings;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", publicationYear=" + publicationYear +
                ", status=" + status +
                ", genre=" + genre +
                ", borrowings=" + borrowings +
                '}';
    }

    public boolean isAvailable() {
        return status == BookStatus.Available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return  Objects.equals(isbn, book.isbn) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    public void setAvailable(boolean b) {
        if (b) {
            status = BookStatus.Available;
        } else {
            status = BookStatus.Reserved;
        }
    }
}
