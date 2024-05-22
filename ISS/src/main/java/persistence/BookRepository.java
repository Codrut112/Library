package persistence;


import domain.Book;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class BookRepository implements IRepositoryBook {
    private SessionFactory sessionFactory;

    public BookRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



    @Override
    public Optional<Book> add(Book entity) {
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
    public Optional<Book> update(Book entity) {
        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            return Optional.of(entity);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> delete(String idEntity) {
        var biblioOpt = findOne(idEntity);
        if(biblioOpt.isEmpty()){
            return Optional.empty();
        }
        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            session.delete(biblioOpt.get());
            transaction.commit();
            return biblioOpt;
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> findOne(String idEntity) {
        try(var session = sessionFactory.openSession()){
            var cititor = session.get(Book.class, idEntity);
            return Optional.ofNullable(cititor);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        try(var session = sessionFactory.openSession()){
            System.out.println("HibernateBookRepository.findAll");
            return session.createQuery("SELECT C FROM Book C", Book.class).list();
        }
        catch (Exception e){
            return List.of();
        }
    }
}