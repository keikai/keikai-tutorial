package io.keikai.tutorial;

import java.util.function.Consumer;

/**
 * Since a lambda can't throw exceptions so we need to catch it and throw an unchecked exception
 * @author hawk
 *
 */
public interface ExceptionalConsumer<T> extends Consumer<T> {
    default void accept(T v){
        try {
            acceptWithException(v);
        } catch (Exception e) {
            throw new RuntimeException("wrapped", e);
        }
    }

    void acceptWithException(T v) throws Exception;
}