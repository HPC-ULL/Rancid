package es.ull.pcg.hpc.benchmark.benchmarks;

import es.ull.pcg.hpc.benchmark.Benchmark;
import es.ull.pcg.hpc.benchmark.Parameters;
import es.ull.pcg.hpc.benchmark.StopCondition;

/**
 * A barebones adapter benchmark class that allows subclasses to only implement the main {@link Benchmark} methods.
 */
public abstract class GenericBenchmark implements Benchmark {

    protected final String mName;
    protected StopCondition mStop;

    /**
     * Create a named benchmark with undefined stop condition.
     *
     * @param name Descriptive name of the benchmark.
     */
    protected GenericBenchmark (String name) {
        this(name, null);
    }

    /**
     * Create a named benchmark with the specified stop condition.
     *
     * @param name Descriptive name of the benchmark.
     * @param stop Stop condition for this benchmark.
     */
    protected GenericBenchmark (String name, StopCondition stop) {
        this.mName = name;
        this.mStop = stop;
    }

    @Override
    public boolean handleException (Exception exception) {
        exception.printStackTrace();
        return true;
    }

    @Override
    public StopCondition getStopCondition () {
        return mStop;
    }

    @Override
    public void setStopCondition (StopCondition stop) {
        mStop = stop;
    }

    @Override
    public void preBenchmark (Parameters parameters) {
        mStop.reset();
    }

    @Override
    public void postBenchmark () {}

    @Override
    public void preRun () {}

    @Override
    public void postRun () {}

    @Override
    public String getName () {
        return mName;
    }

    @Override
    public void reset () {}
}
