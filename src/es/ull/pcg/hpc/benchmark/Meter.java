package es.ull.pcg.hpc.benchmark;

import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Measurement class used in order to obtain metrics from running benchmarks.
 */
public interface Meter {
    /**
     * Start measuring. Called right before starting each benchmark.
     */
    void start ();

    /**
     * Stop measuring. Called right after finishing each benchmark.
     */
    void stop ();

    /**
     * Stop measuring after running into an error during benchmark execution.
     */
    void stopError ();

    /**
     * Set the meter to its original status, before {@link #start()} was called for the first time.
     * <p>
     *     It is used to avoid measurement interference between different benchmarks.
     * </p>
     */
    void reset ();

    /**
     * Obtain a descriptive name for the meter.
     *
     * @return The name.
     */
    String getName ();

    /**
     * Obtain the latest measured result, between the latest calls to {@link #start()} and {@link #stop()} or
     * {@link #stopError()}.
     *
     * @return The latest measured result.
     */
    ValueResult getMeasure ();
}
