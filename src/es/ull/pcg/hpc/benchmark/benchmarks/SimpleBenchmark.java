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
import java.util.stream.Collectors;

/**
 * A parameterized single benchmark. When executed, it repeatedly runs its {@link #benchmark()} method for each set of
 * parameters specified, according to its stop condition.
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
    public Results benchmark (List<Meter> meters, List<ProgressListener> progress, List<ResultsAnalyzer> analyzers,
                              List<ProgressiveResultsLogger> loggers) {
        List<String> meterNames = meters.stream().map(Meter::getName).collect(Collectors.toList());
        List<String> paramNames = mParameters.stream().map(Parameters::getTitle).collect(Collectors.toList());

        MapResult results = new MapResult(this.getName(), ResultTypes.Benchmark.toString(), paramNames);

        progress.forEach(p -> p.startBenchmark(this.getName(), mParameters.size()));
        loggers.forEach(logger -> logger.enterMap(this.getName(), ResultTypes.Benchmark.toString()));

        for (Parameters parameters: mParameters) {
            progress.forEach(p -> p.startParameters(parameters.getTitle()));
            loggers.forEach(logger -> logger.enterMap(parameters.getTitle(), ResultTypes.ParameterSet.toString()));

            MapResult paramResults = new MapResult(parameters.getTitle(), ResultTypes.ParameterSet.toString(),
                                                   meterNames);
            meters.forEach(meter -> paramResults.put(meter.getName(),
                                                     new ListResult(meter.getName(),
                                                                    ResultTypes.Metric.toString())));

            preBenchmark(parameters);

            while (!mStop.shouldStop()) {
                ListIterator<Meter> reverseIterator = meters.listIterator(meters.size());
                preRun();

                try {
                    // Start measuring
                    meters.forEach(Meter::start);

                    benchmark();

                    while (reverseIterator.hasPrevious()) {
                        Meter meter = reverseIterator.previous();
                        meter.stop();
                    }
                    // End measuring
                } catch (Exception exception) {
                    while (reverseIterator.hasPrevious()) {
                        Meter meter = reverseIterator.previous();
                        meter.stopError();
                    }

                    if (!handleException(exception))
                        break;
                }

                meters.forEach(meter -> ((ListResult) paramResults.get(meter.getName())).add(meter.getMeasure()));

                postRun();
                mStop.update(paramResults);
            }

            // Online analysis of results
            analyzers.forEach(analyzer -> analyzer.analyze(paramResults));

            // Logging of results for the current set of parameters
            for (Map.Entry<String, Results> entry: paramResults.entrySet()) {
                Results result = entry.getValue();

                if (result instanceof ListResult) {
                    loggers.forEach(logger -> {
                        logger.enterList(result.getTitle(), result.getType());
                        logger.logValues((ListResult) result);
                        logger.exitList();
                    });
                }
                else {
                    loggers.forEach(logger -> {
                        logger.enterValue(result.getTitle(), result.getType());
                        logger.logValue((ValueResult) result);
                        logger.exitValue();
                    });
                }
            }
            progress.forEach(ProgressListener::finishParameters);
            loggers.forEach(ProgressiveResultsLogger::exitMap);

            // Update global results
            results.put(parameters.getTitle(), paramResults);
            postBenchmark();

        }

        progress.forEach(ProgressListener::finishBenchmark);
        loggers.forEach(ProgressiveResultsLogger::exitMap);

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

    protected abstract void benchmark ();

}
