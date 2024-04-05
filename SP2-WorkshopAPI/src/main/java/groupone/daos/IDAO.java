package groupone.daos;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface IDAO<T, K> {

    List<T> getAll();

    T getById(K id);

    T getById(K in, @NotNull Consumer<T> initializer);

    void create(T in);

    T update(T in, K id);

    T delete(K id);


}
