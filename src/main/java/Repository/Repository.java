package Repository;

import java.util.List;

public interface Repository<ID, T> {

    void save(T entity);

    T findById(ID id);

    List<T> findAll();

    void update(T entity);

    void deleteById(ID id);

}

