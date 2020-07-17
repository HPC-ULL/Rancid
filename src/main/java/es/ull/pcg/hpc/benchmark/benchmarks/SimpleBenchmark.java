package es.ull.pcg.hpc.benchmark.benchmarks;

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
 * A parameterized single benchmark template. When executed, it repeatedly runs its {@link #benchmark()} method for each
 * set of parameters specified, according to its stop condition.
 */
public abstract class SimpleBenchmark extends GenericBenchmark {
    private final List<Parameters> mParameters;

    /**
     * Create a named benchmark with undefined stop condition.
     *
     * @param name Descriptive name of the benchmark.
     */
    public SimpleBenchmark (String name) {
        this(name, null);
    }

    /**
     * Create a named benchmark with the specified stop condition.
     *
     * @param name Descriptive name of the benchmark.
     * @param stop Stop condition for this benchmark.
     */
    public SimpleBenchmark (String name, StopCondition stop) {
        super(name, stop);
        this.mParameters = new ArrayList<>();
    }

    @Override
    public Results benchmark (List<Meter> meters, List<ProgressListener> progress, List<ResultsProcessor> processors,
                              List<ProgressiveResultsLogger> loggers) {
        // Obtain meter names
        ArrayList<String> meterNames = new ArrayList<>(meters.size());

        for (Meter meter: meters)
            meterNames.add(meter.getName());

        // Obtain parameter names
        ArrayList<String> paramNames = new ArrayList<>(mParameters.size());

        for (Parameters param: mParameters)
            paramNames.add(param.getTitle());

        // Create results object
        MapResult results = new MapResult(this.getName(), ResultTypes.Benchmark, paramNames);

        // Notify benchmark start to progress listeners and online loggers
        for (ProgressListener listener: progress)
            listener.startBenchmark(this.getName(), mParameters.size());

        for (ProgressiveResultsLogger logger: loggers)
            logger.enterMap(this.getName(), ResultTypes.Benchmark.toString());

        // Run benchmarks for each parameter set
        for (Parameters parameters: mParameters) {
            // Notify benchmark start for a set of parameters to progress listeners and online loggers
            for (ProgressListener listener: progress)
                listener.startParameters(parameters.getTitle());

            for (ProgressiveResultsLogger logger: loggers)
                logger.enterMap(parameters.getTitle(), ResultTypes.ParameterSet.toString());

            // Create results object for the current set of parameters
            MapResult paramResults = new MapResult(parameters.getTitle(), ResultTypes.ParameterSet, meterNames);

            for (Meter meter: meters)
                paramResults.put(meter.getName(), new ListResult(meter.getName(), ResultTypes.Metric));

            // Setup to start running benchmarks
            mStop.reset(paramResults);
            preBenchmark(parameters);

            // Keep running the same benchmark until the stop condition is reached
            while (!mStop.shouldStop()) {
                // Setup before each iteration
                ListIterator<Meter> reverseIterator = meters.listIterator(meters.size());
                preRun();

                try {
                    // Start measuring
                    for (Meter meter: meters)
                        meter.start();

                    benchmark();

                    // End measuring
                    while (reverseIterator.hasPrevious()) {
                        Meter meter = reverseIterator.previous();
                        meter.stop();
                    }
                } catch (Exception exception) {
                    while (reverseIterator.hasPrevious()) {
                        Meter meter = reverseIterator.previous();
                        meter.stopError();
                    }

                    if (!handleException(exception))
                        break;
                }

                // Obtain the results of the run
                MapResult runResults = MapResult.createTemp();

                for (Meter meter: meters)
                    runResults.put(meter.getName(), meter.getMeasure());

                paramResults.merge(runResults);

                // Tear-down after each iteration and stop condition update
                postRun();
                mStop.update(runResults);
            }

            // Online analysis of results
            for (ResultsProcessor processor: processors)
                processor.process(paramResults);

            // Notify benchmark finish for a parameter set to progress listeners and online loggers
            for (Map.Entry<String, Results> entry: paramResults.entrySet()) {
                Results result = entry.getValue();

                if (result instanceof ListResult) {
                    for (ProgressiveResultsLogger logger: loggers) {
                        logger.enterList(result.getTitle(), result.getType().toString());
                        logger.logValues((ListResult) result);
                        logger.exitList();
                    }
                }
                else {
                    for (ProgressiveResultsLogger logger: loggers) {
                        logger.enterValue(result.getTitle(), result.getType().toString());
                        logger.logValue((ValueResult) result);
                        logger.exitValue();
                    }
                }
            }

            for (ProgressListener listener: progress)
                listener.finishParameters();

            for (ProgressiveResultsLogger logger: loggers)
                logger.exitMap();

            // Update global results
            results.put(parameters.getTitle(), paramResults);
            postBenchmark();
        }

        // Notify benchmark finish to progress listeners and online loggers
        for (ProgressListener listener: progress)
            listener.finishBenchmark();

        for (ProgressiveResultsLogger logger: loggers)
            logger.exitMap();

        return results;
    }

    @Override
    public int getNumImplementations () {
        return 1;
    }

    public List<Parameters> getParameters () {
        return mParameters;
    }

    public void addParameters (Parameters parameters) {
        mParameters.add(parameters);
    }

    /**
     * Implementation of the benchmark. This is the measured method.
     */
    protected abstract void benchmark ();

}
