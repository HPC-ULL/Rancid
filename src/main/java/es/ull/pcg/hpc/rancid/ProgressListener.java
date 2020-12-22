package es.ull.pcg.hpc.rancid;

/**
 * Listener class for obtaining real time updates on the execution of benchmarks.
 */
public interface ProgressListener {
    /**
     * Notifies that a new set of benchmarks has started running.
     *
     * @param numBenchmarks Total number of benchmarks that will be executed.
     * @see #finish()
     */
    void start (int numBenchmarks);

    /**
     * Notifies that benchmarks have finished running.
     *
     * @see #start(int)
     */
    void finish ();

    /**
     * Notifies that a benchmark is about to start running.
     *
     * @param benchmarkTitle Name of the benchmark.
     * @param numParameters Number of different {@link Parameters} with which the benchmark will run.
     *
     * @see #finishBenchmark()
     */
    void startBenchmark (String benchmarkTitle, int numParameters);

    /**
     * Notifies that a benchmark has just finished running.
     *
     * @see #startBenchmark(String, int)
     */
    void finishBenchmark ();

    /**
     * Notifies that a benchmark run with a certain set of parameters is about to start running.
     *
     * @param parametersTitle Name of the parameter set.
     *
     * @see #finishParameters()
     */
    void startParameters (String parametersTitle);

    /**
     * Notifies that a benchmark run has just finished running.
     *
     * @see #startParameters(String)
     */
    void finishParameters ();
}
