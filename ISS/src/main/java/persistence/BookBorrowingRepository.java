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
        System.out.println("am ajuns aici");
        try(var session = sessionFactory.openSession()){
            System.out.println("am ajuns aici2");
            return session.createQuery("SELECT C FROM BookBorrowing C", BookBorrowing.class).list();
        }
        catch (Exception e){
            System.out.println("exceptie");
            return List.of();
        }
    }

    @Override
    public Optional<BookBorrowing> update(BookBorrowing entity) {
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
    public Optional<BookBorrowing> delete(String  idEntity) {
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
}
