package es.ull.pcg.hpc.benchmark.progress;

import es.ull.pcg.hpc.benchmark.ProgressListener;

/**
 * Template class that allows obtaining relative progress (i.e. percentage of the current progress) in real time.
 */
public abstract class RelativeProgressListener implements ProgressListener {
    protected String mBenchmarkName;
    protected String mParametersName;

    private int mTotalBenchmarks, mCurrentBenchmarks;
    private int mTotalParameters, mCurrentParameters;

    /**
     * Use the current gathered data about execution status to obtain the relative progress and update subclasses.
     */
    private void updateProgress () {
        double benchmarksProgress = mCurrentBenchmarks / (double) mTotalBenchmarks;
        double parametersProgress = mCurrentParameters / (double) mTotalParameters;
        double globalProgress = benchmarksProgress + (1 / (double) mTotalBenchmarks) * parametersProgress;
        updateProgress(globalProgress, benchmarksProgress, parametersProgress);
    }

    /**
     * Notifies about the current progress of benchmark execution.
     *
     * @param globalProgress Current global progress.
     * @param benchmarksProgress Current progress on the list of benchmarks.
     * @param parametersProgress Current progress on the list of parameters for the current benchmark.
     */
    public abstract void updateProgress (double globalProgress, double benchmarksProgress, double parametersProgress);

    @Override
    public void start (int numBenchmarks) {
        mBenchmarkName = null;
        mTotalBenchmarks = numBenchmarks;
        mCurrentBenchmarks = mCurrentParameters = 0;
    }

    @Override
    public void finish () {
        mBenchmarkName = null;
        updateProgress();
    }

    @Override
    public void startBenchmark (String benchmarkName, int numParameters) {
        mBenchmarkName = benchmarkName;
        mTotalParameters = numParameters;
        updateProgress();
    }

    @Override
    public void finishBenchmark () {
        mParametersName = null;
        mCurrentParameters = 0;
        ++mCurrentBenchmarks;
    }

    @Override
    public void startParameters (String parametersName) {
        mParametersName = parametersName;
        updateProgress();
    }

    @Override
    public void finishParameters () {
        ++mCurrentParameters;
    }
}