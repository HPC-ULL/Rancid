package es.ull.pcg.hpc.rancid.results;

/**
 * Types or categories in which result nodes can be classified.
 */
public enum ResultTypes {
    /**
     * A set of benchmarks.
     */
    MultiBenchmark("Multiple Benchmark"),
    /**
     * One benchmark with a single implementation.
     */
    Benchmark("Benchmark"),
    /**
     * Results obtained for a benchmark with a single set of parameters.
     */
    ParameterSet("Parameter Set"),
    /**
     * Results for a single metric.
     */
    Metric("Metric"),
    /**
     * Results created by an analysis pass.
     */
    Analysis("Analysis"),
    /**
     * Result composed by a single value.
     */
    Value("Value");

    private final String mName;

    ResultTypes (String name) {
        this.mName = name;
    }

    @Override
    public String toString () {
        return mName;
    }
}
