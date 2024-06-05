package persistence;


import domain.Librarian;
import domain.Person;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class LibrarianRepository implements IRepositoryPerson<Librarian> {
    private SessionFactory sessionFactory;

    public LibrarianRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }




    @Override
    public Optional<Librarian> add(Librarian entity) {
        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return Optional.of(entity);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }





    @Override
    public Optional<Librarian> findOne(String idEntity) {
        try(var session = sessionFactory.openSession()){
            var cititor = session.get(Librarian.class, idEntity);
            return Optional.ofNullable(cititor);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }


    public Optional<Person> findByUsernameAndPassword(String username, String password) {
        try (var session = sessionFactory.openSession()) {
            String hql = "FROM Librarian WHERE username = :username AND password = :password";
            var query = session.createQuery(hql, Librarian.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            var subscriber = query.uniqueResult();
            return Optional.ofNullable(subscriber);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Person> findByUsername(String username) {
        try (var session = sessionFactory.openSession()) {
            String hql = "FROM Librarian WHERE username = :username";
            var query = session.createQuery(hql, Librarian.class);
            query.setParameter("username", username);
            var subscriber = query.uniqueResult();
            return Optional.ofNullable(subscriber);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}