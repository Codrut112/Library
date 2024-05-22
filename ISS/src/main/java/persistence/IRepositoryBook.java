package persistence;


import domain.Book;

import java.util.List;
import java.util.Optional;

public interface IRepositoryBook extends IRepository<Book> {

    List<Book> findAll();

    Optional<Book> update(Book entity);

    Optional<Book> delete(String id);
}
