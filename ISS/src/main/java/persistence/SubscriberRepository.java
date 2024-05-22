package persistence;


import domain.Person;
import domain.Subscriber;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class SubscriberRepository implements IRepositoryPerson<Subscriber>{
    private SessionFactory sessionFactory;

    public SubscriberRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }




    @Override
    public Optional<Subscriber> add(Subscriber entity) {
        System.out.println("HibernateSubscriberRepository.add");
        Transaction transaction = null;
        try (var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return Optional.of(entity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }

           // e.printStackTrace();
            return Optional.empty();
        }
    }





    @Override
    public Optional<Subscriber> findOne(String idEntity) {
        try (var session = sessionFactory.openSession()) {
            var subscriber = session.get(Subscriber.class, idEntity);
            System.out.println(subscriber);
            return Optional.ofNullable(subscriber);
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception for debugging
            return Optional.empty();
        }
    }


    public Optional<Person> findByUsernameAndPassword(String username, String password) {
        try (var session = sessionFactory.openSession()) {
            String hql = "FROM Subscriber WHERE username = :username AND password = :password";
            var query = session.createQuery(hql, Subscriber.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            var subscriber = query.uniqueResult();
            return Optional.ofNullable(subscriber);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
