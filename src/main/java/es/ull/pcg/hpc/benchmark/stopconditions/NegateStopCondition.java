package es.ull.pcg.hpc.benchmark.stopconditions;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.StopCondition;

/**
 * Inverts the value of another stop condition, implementing a NOT operation.
 */
public class NegateStopCondition implements StopCondition {
    private final StopCondition mStop;

    /**
     * Creates a new negation stop condition.
     *
     * @param stop The stop condition to invert the value of.
     */
    public NegateStopCondition (StopCondition stop) {
        this.mStop = stop;
    }

    @Override
    public boolean shouldStop () {
        return mStop == null || !mStop.shouldStop();
    }

    @Override
    public void update (Results results) {
        if (mStop != null)
            mStop.update(results);
    }

    @Override
    public void reset () {
        if (mStop != null)
            mStop.reset();
    }
}
