package es.ull.pcg.hpc.benchmark;

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
     * @param results Partial results obtained up to the current point.
     */
    void update (Results results);

    /**
     * Put the stop condition in its initial state. Called before running each benchmark.
     */
    void reset ();
}
