package es.ull.pcg.hpc.benchmark.results;

public enum ResultTypes {
    MultiBenchmark("Multiple Benchmark"),
    Benchmark("Benchmark"),
    ParameterSet("Parameter Set"),
    Metric("Metric"),
    Analysis("Analysis"),
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
