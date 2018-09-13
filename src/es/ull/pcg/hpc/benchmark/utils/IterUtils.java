package es.ull.pcg.hpc.benchmark.utils;

import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * Set of iteration methods.
 */
public class IterUtils {
    /**
     * Index a lambda-style foreach loop.
     *
     * @param values List of values to iterate over.
     * @param consumer Function called with the iteration index and value.
     * @param <T> Variable type of the elements in the list of values.
     */
    public static <T> void enumerate (Collection<T> values, BiConsumer<Integer, T> consumer) {
        int i = 0;

        for (T value: values)
            consumer.accept(i++, value);
    }
}
