package persistence.interfaces;

import java.util.Optional;

public interface ICrudRepository<ID, E> {
    Optional<E> findOne(ID id);

    Iterable<E> findAll();

    Optional<E> save(E entity);

    Optional<E> delete(ID id);

    Optional<E> update(E entity);
}
