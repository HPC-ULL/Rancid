package es.ull.pcg.hpc.benchmark;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class able to handle and run several benchmarks, and report aggregated results back.
 */
public class BenchmarkManager {

    private final List<Benchmark> mBenchmarks;
    private final List<Meter> mMeters;
    private final List<ProgressListener> mProgressListeners;
    private final List<ResultsAnalyzer> mGlobalAnalyzers;
    private final List<ResultsAnalyzer> mRunAnalyzers;
    private final List<ResultsLogger> mGlobalLoggers;
    private final List<ProgressiveResultsLogger> mOnlineLoggers;

    private final List<Results> mResults;

    /**
     * Create and initialize a benchmark manager.
     */
    public BenchmarkManager () {
        this.mBenchmarks = new ArrayList<>();
        this.mMeters = new ArrayList<>();
        this.mProgressListeners = new ArrayList<>();
        this.mGlobalAnalyzers = new ArrayList<>();
        this.mRunAnalyzers = new ArrayList<>();
        this.mGlobalLoggers = new ArrayList<>();
        this.mOnlineLoggers = new ArrayList<>();
        this.mResults = new ArrayList<>();
    }

    // TODO Read partial results
    // TODO Resume benchmarks

    /**
     * Run all contained benchmarks, according to the current configuration of the manager.
     * <p>
     *     Every benchmark added with {@link #addBenchmark(Benchmark)} is executed in sequence, and progress listeners
     *     added with {@link #addProgressListener(ProgressListener)} are notified as benchmarking progresses. Results
     *     are processed and logged in real time through the classes specified in
     *     {@link #addRunAnalyzer(ResultsAnalyzer)} and {@link #addOnlineLogger(ProgressiveResultsLogger)}.
     * </p>
     * <p>
     *     Once benchmarks are finished, global analyzers added through calls to
     *     {@link #addGlobalAnalyzer(ResultsAnalyzer)} are used to process the complete list of results, and these are
     *     logged by logger added through {@link #addGlobalLogger(ResultsLogger)}.
     * </p>
     */
    public void runBenchmarks () {
        mResults.clear();

        int totalBenchmarks = mBenchmarks.stream().map(Benchmark::getNumImplementations).reduce(0, Integer::sum);
        mProgressListeners.forEach(progress -> progress.start(totalBenchmarks));

        for (Benchmark benchmark: mBenchmarks) {
            benchmark.reset();
            mMeters.forEach(Meter::reset);

            mOnlineLoggers.forEach(logger -> logger.startProgressiveLog(benchmark.getName()));
            Results results = benchmark.benchmark(mMeters, mProgressListeners, mRunAnalyzers, mOnlineLoggers);
            mOnlineLoggers.forEach(ProgressiveResultsLogger::endProgressiveLog);

            mGlobalAnalyzers.forEach(analyzer -> analyzer.analyze(results));
            mGlobalLoggers.forEach(logger -> logger.log(results));

            mResults.add(results);
        }

        mProgressListeners.forEach(ProgressListener::finish);
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
     * Add a new benchmark to run.
     *
     * @param benchmark The new benchmark.
     */
    public void addBenchmark (Benchmark benchmark) {
        if (benchmark != null)
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
        if (meter != null)
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
     * @param listener The new progress listener.
     */
    public void addProgressListener (ProgressListener listener) {
        mProgressListeners.add(listener);
    }

    /**
     * Obtain the list of analyzers applied to global benchmark results.
     *
     * @return The list of global analyzers.
     */
    public List<ResultsAnalyzer> getGlobalAnalyzers () {
        return mGlobalAnalyzers;
    }

    /**
     * Add a new analyzer to apply to global benchmark results.
     *
     * @param analyzer The new global analyzer.
     */
    public void addGlobalAnalyzer (ResultsAnalyzer analyzer) {
        if (analyzer != null)
            mGlobalAnalyzers.add(analyzer);
    }

    /**
     * Obtain the list of analyzers applied to each intermediate result.
     *
     * @return The list of intermediate analyzers.
     */
    public List<ResultsAnalyzer> getRunAnalyzers () {
        return mRunAnalyzers;
    }

    /**
     * Add a new analyzer to apply to intermediate results.
     *
     * @param analyzer The new intermediate analyzer.
     */
    public void addRunAnalyzer (ResultsAnalyzer analyzer) {
        if (analyzer != null)
            mRunAnalyzers.add(analyzer);
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
        if (logger != null)
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
        if (logger != null)
            mOnlineLoggers.add(logger);
    }

    /**
     * Obtain the results for the last benchmark execution.
     *
     * @return The list of results.
     */
    public List<Results> getResults () {
        return mResults;
    }
}
