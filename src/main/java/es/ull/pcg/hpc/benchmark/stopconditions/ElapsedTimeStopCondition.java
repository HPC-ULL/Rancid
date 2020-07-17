package es.ull.pcg.hpc.benchmark.stopconditions;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.StopCondition;
import es.ull.pcg.hpc.benchmark.results.MapResult;

/**
 * Time-based stop condition. Each benchmark runs until the time budget assigned expires. Execution is not interrupted
 * when the time is exceeded.
 */
public class ElapsedTimeStopCondition implements StopCondition {
    private final long mTotalTime;
    private long mStartTime;

    /**
     * Create a new time-based stop condition.
     *
     * @param totalTimeMs Amount of time (in ms) that benchmarks should be repeated.
     */
    public ElapsedTimeStopCondition (long totalTimeMs) {
        this.mTotalTime = totalTimeMs;
    }

    @Override
    public boolean shouldStop () {
        return System.currentTimeMillis() - mStartTime >= mTotalTime;
    }

    @Override
    public void update (MapResult runResults) {}

    @Override
    public void reset (MapResult paramResults) {
        mStartTime = System.currentTimeMillis();
    }
}
