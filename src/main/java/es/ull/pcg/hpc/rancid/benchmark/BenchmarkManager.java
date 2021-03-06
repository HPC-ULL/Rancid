package es.ull.pcg.hpc.rancid.benchmark;

import es.ull.pcg.hpc.rancid.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class able to handle and run several benchmarks, and report aggregated results back.
 */
public class BenchmarkManager {
    private final List<Benchmark> mBenchmarks;
    private final List<Meter> mMeters;
    private final List<ProgressListener> mProgressListeners;
    private final List<ResultsProcessor> mGlobalProcessors;
    private final List<ResultsProcessor> mRunProcessors;
    private final List<ResultsLogger> mGlobalLoggers;
    private final List<ProgressiveResultsLogger> mOnlineLoggers;

    /**
     * Create and initialize a benchmark manager.
     */
    public BenchmarkManager () {
        this.mBenchmarks = new ArrayList<>();
        this.mMeters = new ArrayList<>();
        this.mProgressListeners = new ArrayList<>();
        this.mGlobalProcessors = new ArrayList<>();
        this.mRunProcessors = new ArrayList<>();
        this.mGlobalLoggers = new ArrayList<>();
        this.mOnlineLoggers = new ArrayList<>();
    }

    // TODO Read partial results
    // TODO Pause/Resume benchmarks

    /**
     * Run all contained benchmarks, according to the current configuration of the manager.
     * <p>
     *     Every benchmark added with {@link #addBenchmark(Benchmark)} is executed in sequence, and progress listeners
     *     added with {@link #addProgressListener(ProgressListener)} are notified as benchmarking progresses. Results
     *     are processed and logged in real time through the classes specified in
     *     {@link #addRunProcessor(ResultsProcessor)} and {@link #addOnlineLogger(ProgressiveResultsLogger)}.
     * </p>
     * <p>
     *     Once benchmarks are finished, global processors added through calls to
     *     {@link #addGlobalProcessor(ResultsProcessor)} are used to process the complete list of results, and these are
     *     logged by logger added through {@link #addGlobalLogger(ResultsLogger)}.
     * </p>
     */
    public List<Results> runBenchmarks () {
        List<Results> results = new ArrayList<>();

        int totalBenchmarks = 0;
        for (Benchmark bench: mBenchmarks)
            totalBenchmarks += bench.getNumConfigurations();

        for (ProgressListener listener: mProgressListeners)
            listener.start(totalBenchmarks);

        for (Benchmark benchmark: mBenchmarks) {
            Results benchmarkResults = benchmark.benchmark();

            // Process and log complete results
            for (ResultsProcessor processor: mGlobalProcessors)
                processor.process(benchmarkResults);

            for (ResultsLogger logger: mGlobalLoggers)
                logger.log(benchmarkResults);

            results.add(benchmarkResults);
        }

        for (ProgressListener listener: mProgressListeners)
            listener.finish();

        return results;
    }

    /**
     * Obtain the list of benchmarks.
     *
     * @return The list of benchmarks.
     */
    public List<Benchmark> getBenchmarks () {
        return mBenchmarks;
    }

    /**
     * Add a new benchmark to run and parent it to the manager.
     *
     * @param benchmark The new benchmark.
     */
    public void addBenchmark (Benchmark benchmark) {
        benchmark.setParent(this);
        mBenchmarks.add(benchmark);
    }

    /**
     * Obtain the list of meters.
     *
     * @return The list of meters.
     */
    public List<Meter> getMeters () {
        return mMeters;
    }

    /**
     * Add a new meter to use.
     *
     * @param meter The new meter.
     */
    public void addMeter (Meter meter) {
        mMeters.add(meter);
    }

    /**
     * Obtain the list of progress listeners.
     *
     * @return The list of progress listeners.
     */
    public List<ProgressListener> getProgressListeners () {
        return mProgressListeners;
    }

    /**
     * Add a new progress listener.
     *
     * @param progress The new progress listener.
     */
    public void addProgressListener (ProgressListener progress) {
        mProgressListeners.add(progress);
    }

    /**
     * Obtain the list of processors applied to global benchmark results.
     *
     * @return The list of global processors.
     */
    public List<ResultsProcessor> getGlobalProcessors () {
        return mGlobalProcessors;
    }

    /**
     * Add a new processor to apply to global benchmark results.
     *
     * @param processor The new global processor.
     */
    public void addGlobalProcessor (ResultsProcessor processor) {
        if (processor != null)
            mGlobalProcessors.add(processor);
    }

    /**
     * Obtain the list of processors applied to each intermediate result.
     *
     * @return The list of intermediate processors.
     */
    public List<ResultsProcessor> getRunProcessors () {
        return mRunProcessors;
    }

    /**
     * Add a new processor to apply to intermediate results.
     *
     * @param processor The new intermediate processor.
     */
    public void addRunProcessor (ResultsProcessor processor) {
        if (processor != null)
            mRunProcessors.add(processor);
    }

    /**
     * Obtain the list of loggers used after obtaining the global results of a set of benchmarks.
     *
     * @return The list of global loggers.
     */
    public List<ResultsLogger> getGlobalLoggers () {
        return mGlobalLoggers;
    }

    /**
     * Add a new logger for outputting after obtaining the global results.
     *
     * @param logger The new global logger.
     */
    public void addGlobalLogger (ResultsLogger logger) {
        mGlobalLoggers.add(logger);
    }

    /**
     * Obtain the list of loggers used for outputting partial results as they are generated.
     *
     * @return The list of online loggers.
     */
    public List<ProgressiveResultsLogger> getOnlineLoggers () {
        return mOnlineLoggers;
    }

    /**
     * Add a new logger for outputting partial results.
     *
     * @param logger The new online logger.
     */
    public void addOnlineLogger (ProgressiveResultsLogger logger) {
        mOnlineLoggers.add(logger);
    }
}
