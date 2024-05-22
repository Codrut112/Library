package persistence;







import domain.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * CRUD operations repository interface
 *
 * @param <String> - type E must have an attribute of type String
 * @param <E>  -  type of entities saved in repository
 */

/**
 * CRUD operations repository interface

 * @param <E> - type of entities saved in repository
 */
public interface IRepository<E extends Entity> {

    Optional<E> findOne(String id);


    Optional<E> add(E entity);


}
