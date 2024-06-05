package domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "BookBorrowings")
public class BookBorrowing extends Entity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BorrowStatus status;

    @Column(name="startDate")
    private LocalDateTime startDate;

    @Column(name="endDate")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name="subscriber_id")
    private Subscriber subscriber;

    @ManyToMany
    @JoinTable(
            name = "book_borrowing_books",
            joinColumns = @JoinColumn(name = "book_borrowing_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books;

    public BookBorrowing(BorrowStatus status, LocalDateTime startDate, LocalDateTime endDate, Subscriber subscriber, Set<Book> books) {
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subscriber = subscriber;
        this.books = books;
    }

    public BookBorrowing() {}

    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookBorrowing that)) return false;
        return Objects.equals(getBooks(), that.getBooks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBooks());
    }
}
