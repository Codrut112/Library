package domain;


import javax.persistence.*;
import javax.persistence.Entity;

@Table(name="Librarians")
@Entity
public class Librarian extends Person{
    public Librarian(String username, String password) {
        super(username, password);
    }

    public Librarian() {
        super();
    }

}
