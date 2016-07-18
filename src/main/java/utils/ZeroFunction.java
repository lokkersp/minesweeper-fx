package utils;

/**
 * Created by noctuam on 18.07.16.
 */

/**
 * This is function which return something things without any input
 * @param <R>
 */
@FunctionalInterface
public interface ZeroFunction<R> {
    R apply();
}
