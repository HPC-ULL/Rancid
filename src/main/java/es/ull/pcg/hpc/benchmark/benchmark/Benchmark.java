package es.ull.pcg.hpc.benchmark.benchmark;

import es.ull.pcg.hpc.benchmark.*;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * A Benchmark contains a set of {@link BenchmarkConfiguration} that are executed by a {@link BenchmarkManager},
 * instrumented through a set of {@link Meter}, processed by a list of {@link ResultsProcessor}, and which produces
 * {@link Results} communicated through some {@link ProgressListener}. It can process several sets of
 * {@link Parameters}.
 */
public class Benchmark {
    private final String mTitle;
    private final List<BenchmarkConfiguration> mConfigurations;
    private final List<Parameters> mParameters;
    private BenchmarkManager mParent;

    private List<Meter> mCurrentMeters;
    private List<ProgressListener> mCurrentProgress;
    private List<ProgressiveResultsLogger> mCurrentLoggers;
    private List<String> mCurrentParamTitles;

    /**
     * Create and initialize a benchmark.
     *
     * @param title The title of the benchmark.
     */
    public Benchmark (String title) {
        this.mTitle = title;
        this.mConfigurations = new ArrayList<>();
        this.mParameters = new ArrayList<>();
        this.mParent = null;

        this.mCurrentMeters = null;
        this.mCurrentProgress = null;
        this.mCurrentLoggers = null;
        this.mCurrentParamTitles = null;
    }

    /**
     * Run the benchmark. Each {@link BenchmarkConfiguration} and {@link Parameters} combination is repeatedly
     * executed according to their respecting {@link StopCondition}.
     *
     * @return The results of measuring the specified metrics when running the different configurations.
     */
    public Results benchmark () {
        // Setup
        List<String> implementationTitles = new ArrayList<>(mConfigurations.size());
        for (BenchmarkConfiguration config: mConfigurations)
            implementationTitles.add(config.getImplementation().getTitle());

        MapResult results = new MapResult(getTitle(),
                                          ResultTypes.MultiBenchmark, implementationTitles);

        for (ProgressiveResultsLogger logger: mCurrentLoggers)
            logger.startProgressiveLog(getTitle());

        // Cache parameter names
        this.mCurrentParamTitles = new ArrayList<>(mParameters.size());
        for (Parameters param: mParameters)
            mCurrentParamTitles.add(param.getTitle());

        // Run all combinations of implementation and parameters
        for (BenchmarkConfiguration config: mConfigurations) {
            Results configResult = benchmarkConfiguration(config);
            results.put(config.getImplementation().getTitle(), configResult);
        }

        // Finalize
        this.mCurrentParamTitles = null;
        for (ProgressiveResultsLogger logger: mCurrentLoggers)
            logger.endProgressiveLog();

        return results;
    }

    private Results benchmarkConfiguration (BenchmarkConfiguration config) {
        String implementationTitle = config.getImplementation().getTitle();
        MapResult results = new MapResult(implementationTitle,
                                          ResultTypes.Benchmark, mCurrentParamTitles);

        // Setup benchmarks
        for (ProgressListener listener: mCurrentProgress)
            listener.startBenchmark(implementationTitle, mParameters.size());

        for (ProgressiveResultsLogger logger: mCurrentLoggers)
            logger.enterMap(implementationTitle, ResultTypes.Benchmark);

        // Run benchmark for each parameter set and retrieve results
        for (Parameters parameterSet: mParameters) {
            Results paramResults = benchmarkParameter(config, parameterSet);
            results.put(parameterSet.getTitle(), paramResults);
        }

        // Finalize benchmarks
        for (ProgressListener listener: mCurrentProgress)
            listener.finishBenchmark();

        for (ProgressiveResultsLogger logger: mCurrentLoggers)
            logger.exitMap();

        return results;
    }

    private Results benchmarkParameter (BenchmarkConfiguration config, Parameters parameterSet) {
        // Setup individual benchmark
        for (Meter meter: mCurrentMeters)
            meter.reset();

        for (ProgressListener listener: mCurrentProgress)
            listener.startParameters(parameterSet.getTitle());

        for (ProgressiveResultsLogger logger: mCurrentLoggers)
            logger.enterMap(parameterSet.getTitle(), ResultTypes.ParameterSet);

        // Execute and measure
        Results results = config.getRunner().run(config.getImplementation(), parameterSet);

        // Finalize individual benchmark
        for (ProgressListener listener: mCurrentProgress)
            listener.finishParameters();

        for (ProgressiveResultsLogger logger: mCurrentLoggers)
            logger.exitMap();

        return results;
    }

    /**
     * Obtain the name of the benchmark.
     *
     * @return The benchmark name.
     */
    public String getTitle () {
        return mTitle;
    }

    /**
     * Obtain the number of different configurations in this benchmark.
     *
     * @return The number of configurations.
     */
    public int getNumConfigurations () {
        return mConfigurations.size();
    }

    /**
     * Add a new benchmark configuration and parent it to the benchmark.
     *
     * @param config The new benchmark configuration.
     */
    public void addConfiguration (BenchmarkConfiguration config) {
        config.getRunner().setParent(this);
        mConfigurations.add(config);
    }

    /**
     * Add one set of parameters to test with each configuration.
     *
     * @param parameters The new set of parameters.
     */
    public void addParameterSet (Parameters parameters) {
        mParameters.add(parameters);
    }

    /**
     * Obtain the parent {@link BenchmarkManager} of this benchmark.
     *
     * @return The parent manager.
     */
    public BenchmarkManager getParent () {
        return mParent;
    }

    void setParent (BenchmarkManager parent) {
        this.mParent = parent;
        this.mCurrentMeters = parent.getMeters();
        this.mCurrentProgress = parent.getProgressListeners();
        this.mCurrentLoggers = parent.getOnlineLoggers();
    }
}
