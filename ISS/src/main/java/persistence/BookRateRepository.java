package persistence;


import domain.BookRate;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class BookRateRepository implements IRepositoryBookAction<BookRate> {
    public BookRateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private SessionFactory sessionFactory;
    @Override
    public Optional<BookRate> add(BookRate entity) {
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
    public Optional<BookRate> findOne(String idEntity) {
        try(var session = sessionFactory.openSession()){
            var cititor = session.get(BookRate.class, idEntity);
            return Optional.ofNullable(cititor);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<BookRate> findAll() {
        try(var session = sessionFactory.openSession()){
            return session.createQuery("SELECT C FROM BookRate C", BookRate.class).list();
        }
        catch (Exception e){
            return List.of();
        }
    }

    @Override
    public Optional<BookRate> update(BookRate entity) {
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
    public Optional<BookRate> delete(String id) {
        var bookRateOpt = findOne(id);
        if(bookRateOpt.isEmpty()){
            return Optional.empty();
        }
        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            session.delete(bookRateOpt.get());
            transaction.commit();
            return bookRateOpt;
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
}
