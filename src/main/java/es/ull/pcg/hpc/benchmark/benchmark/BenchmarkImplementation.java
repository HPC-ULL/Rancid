package es.ull.pcg.hpc.benchmark.benchmark;

import es.ull.pcg.hpc.benchmark.Parameters;

/**
 * The implementation of a benchmark, or the code that is to be benchmarked.
 */
public abstract class BenchmarkImplementation {
    private final String mTitle;

    /**
     * Create and initialize a benchmark implementation.
     *
     * @param title The title of the implementation.
     */
    public BenchmarkImplementation (String title) {
        this.mTitle = title;
    }

    /**
     * The code to be measured. Any extra computation that should be done but not measured should go
     * into the other initialization and finalization methods of the class.
     *
     * @see #setupBenchmark(Parameters)
     * @see #initParameters()
     * @see #releaseParameters()
     * @see #finalizeBenchmark()
     */
    public abstract void instrumentedRun ();

    /**
     * Extract the set of parameters and set up any invariants across different executions.
     *
     * @param parameters The set of parameters with which to run the next set of instrumented runs.
     *
     * @see #instrumentedRun()
     * @see #initParameters()
     */
    public void setupBenchmark (Parameters parameters) {}

    /**
     * Close open files and release memory after finishing all runs.
     *
     * @see #instrumentedRun()
     * @see #releaseParameters()
     */
    public void finalizeBenchmark () {}

    /**
     * Prepare parameters that may have to be initialized before every run of the benchmark.
     *
     * @see #instrumentedRun()
     * @see #setupBenchmark(Parameters)
     */
    public void initParameters () {}

    /**
     * Clean up after each run of the benchmark.
     *
     * @see #instrumentedRun()
     * @see #finalizeBenchmark()
     */
    public void releaseParameters () {}

    /**
     * Handle an exception thrown while running benchmark code.
     *
     * @param e Exception thrown by the benchmark.
     *
     * @return {@code true} if next benchmark runs can continue.
     */
    public boolean handleException (RuntimeException e) {
        e.printStackTrace();
        return true;
    }

    /**
     * Obtain the title of the implementation.
     *
     * @return The title of the implementation.
     */
    public String getTitle () {
        return mTitle;
    }
}
