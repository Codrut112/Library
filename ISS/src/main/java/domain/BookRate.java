package domain;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Table(name="BookRates")
@javax.persistence.Entity
@AttributeOverrides({
        @AttributeOverride(name="id", column = @Column(name="id"))
})
public class BookRate extends BookBorrowing  {
    public BookRate(BorrowStatus status, LocalDateTime startDate, LocalDateTime endDate, Subscriber subscriber, Set<Book> books, int grade) {
        super(status, startDate, endDate, subscriber, books);
        this.grade = grade;
    }



    @Column(name = "grade")
    private int grade;



    public BookRate() {

    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
