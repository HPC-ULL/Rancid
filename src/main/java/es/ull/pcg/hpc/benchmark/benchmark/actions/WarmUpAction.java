package es.ull.pcg.hpc.benchmark.benchmark.actions;

import es.ull.pcg.hpc.benchmark.Parameters;
import es.ull.pcg.hpc.benchmark.utils.FileUtils;
import es.ull.pcg.hpc.benchmark.benchmark.BenchmarkImplementation;
import es.ull.pcg.hpc.benchmark.BenchmarkAction;

/**
 * An action that forces processor frequency up by repeatedly running a compute-intensive kernel, until a target
 * frequency or a time limit is reached.
 */
public class WarmUpAction implements BenchmarkAction {
    private final BenchmarkImplementation mKernel;
    private final Parameters mParameters;
    private String mFrequencyPath;
    private long mFrequencyTarget;
    private long mTimeout;

    public WarmUpAction (BenchmarkImplementation kernel, Parameters parameters, String frequencyPath,
                         long frequencyTarget) {
        this.mKernel = kernel;
        this.mParameters = parameters;
        this.mFrequencyPath = frequencyPath;
        this.mFrequencyTarget = frequencyTarget;
        this.mTimeout = -1;
    }

    public void setFrequencyPath (String path) {
        this.mFrequencyPath = path;
    }

    public void setFrequencyTarget (long target) {
        this.mFrequencyTarget = target;
    }

    public void setTimeout (long timeMs) {
        this.mTimeout = timeMs;
    }

    @Override
    public void execute () {
        long start = System.currentTimeMillis();
        long elapsed = 0;

        // Check first if warm-up is necessary
        long freq = (Long) FileUtils.readNumberFile(mFrequencyPath);
        if (freq >= mFrequencyTarget)
            return;

        // Reset kernel parameters
        mKernel.releaseParameters();
        mKernel.finalizeBenchmark();
        mKernel.setupBenchmark(mParameters);

        try {
            while (freq < mFrequencyTarget && (mTimeout < 0 || elapsed < mTimeout)) {
                mKernel.initParameters();
                mKernel.instrumentedRun();
                mKernel.releaseParameters();
                freq = (Long) FileUtils.readNumberFile(mFrequencyPath);
                elapsed = System.currentTimeMillis() - start;
            }
        }
        finally {
            mKernel.finalizeBenchmark();
        }
    }
}
