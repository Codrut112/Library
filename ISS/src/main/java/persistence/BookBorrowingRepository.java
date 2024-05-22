package persistence;


import domain.BookBorrowing;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;


public class BookBorrowingRepository implements IRepositoryBookAction<BookBorrowing> {

    private SessionFactory sessionFactory;

    public BookBorrowingRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<BookBorrowing> add(BookBorrowing entity) {
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
    public Optional<BookBorrowing> findOne(String idEntity) {
        try(var session = sessionFactory.openSession()){
            var cititor = session.get(BookBorrowing.class, idEntity);
            return Optional.ofNullable(cititor);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<BookBorrowing> findAll() {
        try(var session = sessionFactory.openSession()){
            return session.createQuery("SELECT C FROM BookBorrowing C", BookBorrowing.class).list();
        }
        catch (Exception e){
            return List.of();
        }
    }
}
