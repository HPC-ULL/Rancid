package es.ull.pcg.hpc.benchmark;

import java.util.HashMap;
import java.util.Map;

/**
 * A set of parameters, intended to be used in a benchmark run.
 */
public class Parameters {
    private final String mTitle;
    private final Map<String, Object> mValues;

    /**
     * Create a set of parameters.
     *
     * @param title A name to distinguish this set of parameters with others.
     */
    public Parameters (String title) {
        this.mTitle = title;
        this.mValues = new HashMap<>();
    }

    /**
     * Add a new parameter to the set.
     *
     * @param name Name of the parameter.
     * @param value Value of the parameter.
     */
    public void addParameter (String name, Object value) {
        mValues.put(name, value);
    }

    /**
     * Retrieve a parameter from the set.
     *
     * @param name Name of the parameter.
     * @param <T> Data type of the parameter.
     *
     * @return The desired parameter or {@code null} if it does not exist.
     */
    public <T> T getParameter (String name) {
        Object value = mValues.get(name);
        return (T) value;
    }

    /**
     * Obtain the name of the parameter set.
     *
     * @return The name.
     */
    public String getTitle () {
        return mTitle;
    }
}
