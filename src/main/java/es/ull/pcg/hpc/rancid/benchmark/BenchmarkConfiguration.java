package es.ull.pcg.hpc.rancid.benchmark;

/**
 * A pair of {@link BenchmarkRunner} and {@link BenchmarkImplementation}. It ties implementations of a benchmark with
 * the way in which their execution should be handled.
 */
public class BenchmarkConfiguration {
    private final BenchmarkRunner mRunner;
    private final BenchmarkImplementation mImplementation;

    /**
     * Create and initialize a benchmark configuration.
     *
     * @param runner The benchmark runner.
     * @param implementation The benchmark implementation.
     */
    public BenchmarkConfiguration (BenchmarkRunner runner, BenchmarkImplementation implementation) {
        this.mRunner = runner;
        this.mImplementation = implementation;
    }

    /**
     * Obtain the benchmark runner.
     *
     * @return The benchmark runner.
     */
    public BenchmarkRunner getRunner () {
        return mRunner;
    }

    /**
     * Obtain the benchmark implementation.
     *
     * @return The benchmark implementation.
     */
    public BenchmarkImplementation getImplementation () {
        return mImplementation;
    }
}
