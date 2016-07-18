package utils;

/**
 * Created by noctuam on 17.07.16.
 */
@FunctionalInterface
public interface TriFunction<T, T1, T2, R> {
    R apply(T t,T1 t1, T2 t2);
}
