package domain;

import javax.persistence.*;



@AttributeOverrides({
        @AttributeOverride(name="id", column = @Column(name="id"))
})
@MappedSuperclass
public abstract class Person extends Entity {
    @Column(name="password")
    private String password;

    @Column(name="username")
    private String username;
    public Person(String username, String password) {
        this.username=username;
        this.password = password;
    }

    public Person() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

