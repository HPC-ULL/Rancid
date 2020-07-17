package es.ull.pcg.hpc.benchmark.benchmark;

import es.ull.pcg.hpc.benchmark.*;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Runner class that is able of managing the execution of any {@link BenchmarkImplementation} using a certain
 * set of {@link Parameters}. It can be customized to handle external factors that could impact benchmarks.
 */
public class BenchmarkRunner {
    private final StopCondition mStop;
    private Benchmark mParent;

    protected BenchmarkImplementation mCurrentImplementation;
    protected Parameters mCurrentParameters;

    /**
     * Create and initialize a benchmark runner.
     *
     * @param stop The stop condition used to determine when to finish repeating the execution of a single
     *             pair of {@link BenchmarkImplementation} and {@link Parameters}.
     */
    public BenchmarkRunner (StopCondition stop) {
        this.mStop = stop;
        this.mParent = null;
        this.mCurrentImplementation = null;
        this.mCurrentParameters = null;
    }

    /**
     * Setup code executed once before running the benchmark.
     */
    protected void preBenchmark () {
        mCurrentImplementation.setupBenchmark(mCurrentParameters);
    }

    /**
     * Setup code executed before each repetition of the benchmark.
     */
    protected void preRun () {
        mCurrentImplementation.initParameters();
    }

    /**
     * <p>
     *     Benchmark a single pair of {@link BenchmarkImplementation} and {@link Parameters} repeatedly, until
     *     the stop condition is reached.
     * </p>
     * <p>
     *     A single benchmark consists of running {@link #preBenchmark()} and {@link #postBenchmark()} once at the
     *     beginning and end, and repeatedly executing the specified combination of implementation and parameters
     *     while taking measures according to the runner parent's {@link Meter}s. Before and after each of these
     *     repetitions, {@link #preRun()} and {@link #postRun()} are called.
     * </p>
     *
     * @param implementation Code to benchmark.
     * @param parameters Set of parameters to use as input to the code.
     *
     * @return The measured and analyzed results of the benchmark.
     *
     * @see #preBenchmark()
     * @see #preRun()
     * @see #postRun()
     * @see #postBenchmark()
     */
    public Results run (BenchmarkImplementation implementation, Parameters parameters) {
        this.mCurrentImplementation = implementation;
        this.mCurrentParameters = parameters;

        BenchmarkManager mgr = getParent().getParent();
        List<Meter> meters = mgr.getMeters();
        List<ResultsProcessor> processors = mgr.getRunProcessors();
        List<ProgressiveResultsLogger> loggers = mgr.getOnlineLoggers();

        ArrayList<String> meterTitles = new ArrayList<>(meters.size());
        for (Meter meter: meters)
            meterTitles.add(meter.getTitle());

        MapResult paramResults = new MapResult(parameters.getTitle(), ResultTypes.ParameterSet, meterTitles);
        for (Meter meter: meters)
            paramResults.put(meter.getTitle(), new ListResult(meter.getTitle(), ResultTypes.Metric));

        mStop.reset(paramResults);
        preBenchmark();

        while (!mStop.shouldStop()) {
            ListIterator<Meter> reverseIterator = meters.listIterator(meters.size());
            preRun();

            try {
                // Start measurements
                for (Meter meter: meters)
                    meter.start();

                implementation.instrumentedRun();

                while (reverseIterator.hasPrevious()) {
                    Meter meter = reverseIterator.previous();
                    meter.stop();
                }
                // End measurements
            } catch (RuntimeException exception) {
                while (reverseIterator.hasPrevious()) {
                    Meter meter = reverseIterator.previous();
                    meter.stopError();
                }

                if (!implementation.handleException(exception))
                    break;
            }

            MapResult runResults = new MapResult(parameters.getTitle(), ResultTypes.ParameterSet, meterTitles);
            for (Meter meter: meters) {
                runResults.put(meter.getTitle(), meter.getMeasure());
                ((ListResult) paramResults.get(meter.getTitle())).add(meter.getMeasure());
            }

            postRun();
            mStop.update(runResults);
        }

        // Online analysis of results
        for (ResultsProcessor processor: processors)
            processor.process(paramResults);

        for (Map.Entry<String, Results> entry: paramResults.entrySet()) {
            Results result = entry.getValue();

            if (result instanceof ListResult) {
                for (ProgressiveResultsLogger logger: loggers) {
                    logger.enterList(result.getTitle(), result.getType());
                    logger.logValues((ListResult) result);
                    logger.exitList();
                }
            }
            else {
                for (ProgressiveResultsLogger logger: loggers) {
                    logger.enterValue(result.getTitle(), result.getType());
                    logger.logValue((ValueResult) result);
                    logger.exitValue();
                }
            }
        }

        // Update global results
        postBenchmark();
        this.mCurrentImplementation = null;
        this.mCurrentParameters = null;

        return paramResults;
    }

    /**
     * Cleanup code executed after each repetition of the benchmark.
     */
    protected void postRun () {
        mCurrentImplementation.releaseParameters();
    }

    /**
     * Cleanup code executed once after running the benchmark.
     */
    protected void postBenchmark () {
        mCurrentImplementation.finalizeBenchmark();
    }

    /**
     * Obtain the parent {@link Benchmark} to this runner.
     *
     * @return The parent benchmark.
     */
    public Benchmark getParent () {
        return mParent;
    }

    void setParent (Benchmark parent) {
        this.mParent = parent;
    }

    /**
     * Obtain the current {@link BenchmarkImplementation} that is running, about to start or about to stop.
     *
     * @return The current implementation.
     *
     * @see #getCurrentParameters()
     */
    public BenchmarkImplementation getCurrentImplementation () {
        return mCurrentImplementation;
    }

    /**
     * Obtain the set of {@link Parameters} used as input for the current {@link BenchmarkImplementation}.
     *
     * @return The current set of parameters.
     *
     * @see #getCurrentImplementation()
     */
    public Parameters getCurrentParameters () {
        return mCurrentParameters;
    }
}
