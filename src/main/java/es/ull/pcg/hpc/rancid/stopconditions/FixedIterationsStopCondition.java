package es.ull.pcg.hpc.rancid.stopconditions;

import es.ull.pcg.hpc.rancid.StopCondition;
import es.ull.pcg.hpc.rancid.results.MapResult;

/**
 * Iteration-based stop condition. Benchmarks are repeated a fixed amount of times.
 */
public class FixedIterationsStopCondition implements StopCondition {
    private final int mTotalIterations;
    private int mCurrentIterations;

    /**
     * Create a new iteration-based stop condition.
     *
     * @param iterations Number of times to run each benchmark.
     */
    public FixedIterationsStopCondition (int iterations) {
        this.mTotalIterations = iterations;
    }

    @Override
    public boolean shouldStop () {
        return mCurrentIterations >= mTotalIterations;
    }

    @Override
    public void update (MapResult runResults) {
        ++mCurrentIterations;
    }

    @Override
    public void reset (MapResult paramResults) {
        mCurrentIterations = 0;
    }
}
