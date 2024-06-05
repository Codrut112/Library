package persistence;


import domain.Person;

import java.util.Optional;

public interface IRepositoryPerson<P extends Person> extends IRepository< P> {
 Optional<Person> findByUsernameAndPassword(String username,String password);
 Optional<Person> findByUsername(String username);
}
