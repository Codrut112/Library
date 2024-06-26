package domain;


import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Objects;


@javax.persistence.Entity
@Table(name="Subscribers")
public class Subscriber extends Person{
    @Column(name="name")
    private String name;
    @Column(name="cnp")
    private  String cnp;
    @Column(name="phone")
    private String phone;

    public Subscriber(String username, String password, String name, String cnp, String phone) {
        super(username, password);
        this.name = name;
        this.cnp = cnp;
        this.phone = phone;
    }

    public Subscriber() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscriber that = (Subscriber) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
