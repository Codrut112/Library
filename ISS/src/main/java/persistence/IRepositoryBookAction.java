package persistence;

import domain.Book;
import domain.BookBorrowing;
import domain.Entity;

import java.util.List;
import java.util.Optional;

public interface IRepositoryBookAction<A extends BookBorrowing> extends IRepository<A>{
    List<A> findAll();
    Optional<A> update(A entity);
    Optional<A> delete(String id);
}
