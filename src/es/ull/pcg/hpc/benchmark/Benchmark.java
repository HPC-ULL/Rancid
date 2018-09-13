package es.ull.pcg.hpc.benchmark;

import java.util.List;

/**
 * A Benchmark contains a piece of code that is repeatedly executed by a {@link BenchmarkManager}, subject to a
 * {@link StopCondition}, instrumented through a set of {@link Meter}, processed by a list of {@link ResultsAnalyzer}
 * and which produces {@link Results} communicated through some {@link ProgressListener}. It can process several sets
 * of {@link Parameters}.
 */
public interface Benchmark {
    /**
     * Run the benchmark. Any setup or cleanup code that should not be measured as part of the benchmark should be
     * implemented instead in the {@link #preBenchmark(Parameters)}, {@link #preRun()}, {@link #postBenchmark()} or
     * {@link #postRun()} methods.
     *
     * @param meters    List of meters used to measure execution metrics. They start in order, so that the last meter
     *                  is not affected by the overhead of running any of the others. The first meter, however, will
     *                  include the overhead of starting and stopping all other metrics.
     * @param progress  Classes used to notify in real time the progress status of the benchmark.
     * @param analyzers List of analyzers that take partial results in order to filter and generate new data.
     * @param loggers   List of loggers that are updated as data is generated.
     *
     * @return The results of measuring the specified metrics when running the benchmark code.
     */
    Results benchmark (List<Meter> meters, List<ProgressListener> progress, List<ResultsAnalyzer> analyzers,
                       List<ProgressiveResultsLogger> loggers);

    /**
     * Handle an exception thrown while running benchmark code.
     *
     * @param exception Exception thrown by the benchmark.
     *
     * @return {@code true} if the benchmark can continue.
     */
    boolean handleException (Exception exception);

    /**
     * Obtain the stop condition used by this benchmark.
     *
     * @return The stop condition.
     */
    StopCondition getStopCondition ();

    /**
     * Set the stop condition used by this benchmark.
     *
     * @param stop The new stop condition.
     */
    void setStopCondition (StopCondition stop);

    /**
     * Setup code executed once before running the benchmark.
     *
     * @param parameters Set of parameters used.
     */
    void preBenchmark (Parameters parameters);

    /**
     * Cleanup code executed once after running the benchmark.
     */
    void postBenchmark ();

    /**
     * Setup code executed before each repetition of the benchmark, using the last set of parameters passed to
     * {@link #preBenchmark(Parameters)}.
     */
    void preRun ();

    /**
     * Cleanup code executed after each repetition of the benchmark.
     */
    void postRun ();

    /**
     * Obtain the name of the benchmark.
     *
     * @return The benchmark name.
     */
    String getName ();

    /**
     * Obtain the number of different implementations for this benchmark.
     *
     * @return The number of implementations.
     *
     * @see es.ull.pcg.hpc.benchmark.benchmarks.SimpleBenchmark
     * @see es.ull.pcg.hpc.benchmark.benchmarks.MultipleBenchmark
     */
    int getNumImplementations ();

    /**
     * Set the benchmark back to its initial state.
     */
    void reset ();
}
