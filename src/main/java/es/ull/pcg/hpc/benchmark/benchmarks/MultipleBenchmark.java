package es.ull.pcg.hpc.benchmark.benchmarks;

import es.ull.pcg.hpc.benchmark.*;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A benchmark in which multiple implementations of the same algorithm are compared.
 */
public class MultipleBenchmark extends GenericBenchmark {
    private final List<SimpleBenchmark> mImplementations;

    /**
     * Create a named benchmark with undefined stop condition.
     *
     * @param name Descriptive name of the benchmark.
     */
    public MultipleBenchmark (String name) {
        this(name, null);
    }

    /**
     * Create a named benchmark with the specified stop condition.
     *
     * @param name Descriptive name of the benchmark.
     * @param stop Stop condition for this benchmark.
     */
    public MultipleBenchmark (String name, StopCondition stop) {
        super(name, stop);
        this.mImplementations = new ArrayList<>();
    }

    @Override
    public Results benchmark (List<Meter> meters, List<ProgressListener> progress, List<ResultsAnalyzer> analyzers,
                              List<ProgressiveResultsLogger> loggers) {
        mImplementations.forEach(implem -> implem.setStopCondition(mStop));

        List<String> implementationNames = mImplementations.stream().map(Benchmark::getName)
                                                           .collect(Collectors.toList());

        MapResult results = new MapResult(this.getName(), ResultTypes.MultiBenchmark.toString(), implementationNames);

        mImplementations.forEach(impl -> {
            meters.forEach(Meter::reset);
            results.put(impl.getName(), impl.benchmark(meters, progress, analyzers, loggers));
        });

        return results;
    }

    @Override
    public int getNumImplementations () {
        return mImplementations.size();
    }

    @Override
    public void reset () {
        mImplementations.forEach(SimpleBenchmark::reset);
    }

    /**
     * Obtain the list of {@link SimpleBenchmark} representing each implementation for this benchmark.
     *
     * @return The list of implementations for this benchmark.
     */
    public List<SimpleBenchmark> getImplementations () {
        return mImplementations;
    }

    /**
     * Add a new implementation to this benchmark.
     *
     * @param implementation The new implementation.
     */
    public void addImplementations (SimpleBenchmark implementation) {
        mImplementations.add(implementation);
    }
}
