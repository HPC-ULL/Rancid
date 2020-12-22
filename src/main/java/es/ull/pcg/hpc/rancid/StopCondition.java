package es.ull.pcg.hpc.rancid;

import es.ull.pcg.hpc.rancid.results.MapResult;

/**
 * Interface for classes representing stop conditions, or logic for deciding whether to keep running a certain benchmark
 * or stop.
 * <p>
 *     Subclasses can be used in order to reduce the measurement error until a certain level, or provide a
 *     certain limit on the execution time, as well as determine a fixed amount of repetitions per benchmark.
 * </p>
 */
public interface StopCondition {
    /**
     * Tell whether the current benchmark should continue being repeated or not.
     *
     * @return {@code true} if the current benchmark should stop being repeatedly executed.
     */
    boolean shouldStop ();

    /**
     * Provide information after each benchmark run, in order to help decide if it should keep being repeated.
     *
     * @param runResults Results obtained on the latest iteration.
     */
    void update (MapResult runResults);

    /**
     * Put the stop condition in its initial state. Called before running each benchmark.
     *
     * @param paramResults Object that always contains the current results, updated after each run. Each element
     *                     contains the list of results for a metric.
     */
    void reset (MapResult paramResults);
}
