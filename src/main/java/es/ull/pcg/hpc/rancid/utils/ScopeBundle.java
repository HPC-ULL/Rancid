package es.ull.pcg.hpc.rancid.utils;

import es.ull.pcg.hpc.rancid.BenchmarkAction;

import java.util.HashMap;
import java.util.Map;

/**
 * A mapping of keys to values designed to introduce multiple elements into a scope on {@link BenchmarkAction}s that
 * accept functions to be evaluated during benchmarking.
 */
public class ScopeBundle {
    private final Map<String, Object> mScope;

    public ScopeBundle () {
        this.mScope = new HashMap<>();
    }

    public ScopeBundle (String key, Object value) {
        this();
        add(key, value);
    }

    public ScopeBundle (String key1, Object value1,
                        String key2, Object value2) {
        this();
        add(key1, value1).add(key2, value2);
    }

    public ScopeBundle (String key1, Object value1,
                        String key2, Object value2,
                        String key3, Object value3) {
        this();
        add(key1, value1).add(key2, value2).add(key3, value3);
    }

    public ScopeBundle (String key1, Object value1,
                        String key2, Object value2,
                        String key3, Object value3,
                        String key4, Object value4) {
        this();
        add(key1, value1).add(key2, value2).add(key3, value3).add(key4, value4);
    }

    /**
     * Add a mapping to the scope.
     *
     * @param key The name of the element.
     * @param value The value of the element.
     *
     * @return The scope.
     */
    public ScopeBundle add (String key, Object value) {
        mScope.put(key, value);
        return this;
    }

    /**
     * Retrieve an element from the scope.
     *
     * @param key The name of the element.
     *
     * @return The value assigned to that element or {@code null}.
     */
    public Object get (String key) {
        return mScope.get(key);
    }
}
