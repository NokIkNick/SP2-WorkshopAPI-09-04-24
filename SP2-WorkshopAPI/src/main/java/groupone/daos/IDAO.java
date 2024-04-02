package groupone.daos;

import java.util.List;

public interface IDAO<T, K> {

    List<T> getAll();

    T getById(K id);

    void create(T in);

    T update(T in, K id);

    T delete(K id);


}
