package io.keikai.tutorial;

import java.util.function.Function;

/**
 * Represents a function that handles checked exception by throwing a runtime exception.
 * @param <T> input
 * @param <R> result
 */
public interface ExceptionableFunction<T, R> extends Function<T, R> {
    default R apply(T val) {
        try {
            return applyWithException(val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    R applyWithException(T val) throws Exception;
}
