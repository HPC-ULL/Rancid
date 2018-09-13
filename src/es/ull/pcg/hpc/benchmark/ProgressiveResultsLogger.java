package es.ull.pcg.hpc.benchmark;

import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Logger interface intended for online logging of partial results.
 */
public interface ProgressiveResultsLogger {
    /**
     * Start logging.
     *
     * @param title Name of the benchmark for which the logging will be done.
     *
     * @see #endProgressiveLog()
     */
    void startProgressiveLog (String title);

    /**
     * End logging.
     *
     * @see #startProgressiveLog(String)
     */
    void endProgressiveLog ();

    /**
     * Start logging results of the type {@link es.ull.pcg.hpc.benchmark.results.MapResult}.
     *
     * @param title Name of the partial results map.
     * @param type Type or category of the results.
     *
     * @see #exitMap()
     */
    void enterMap (String title, String type);

    /**
     * End logging of a partial results map.
     *
     * @see #enterMap(String, String)
     */
    void exitMap ();

    /**
     * Start logging results of the type {@link es.ull.pcg.hpc.benchmark.results.ListResult}.
     *
     * @param title Name of the partial results list.
     * @param type Type or category of the results.
     *
     * @see #exitList()
     */
    void enterList (String title, String type);

    /**
     * End logging of a partial results list.
     *
     * @see #enterList(String, String)
     */
    void exitList ();

    /**
     * Start logging results of the type {@link ValueResult}.
     *
     * @param title Name of the partial result value.
     * @param type Type or category of the result.
     */
    void enterValue (String title, String type);

    /**
     * End logging of a partial result value.
     */
    void exitValue ();

    /**
     * Log a single value.
     *
     * @param value The value to log.
     */
    void logValue (ValueResult value);

    /**
     * Log the results of a list of values.
     *
     * @param values The values to log.
     */
    void logValues (ListResult values);
}
