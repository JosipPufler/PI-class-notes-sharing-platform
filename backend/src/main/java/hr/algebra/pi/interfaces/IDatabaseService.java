package hr.algebra.pi.interfaces;

import java.util.List;

public interface IDatabaseService<T> {
    T findById(Long id);
    void update(T entity);
    void deleteById(Long id);
    List<T> getAll();
    T create(T entity);
}
