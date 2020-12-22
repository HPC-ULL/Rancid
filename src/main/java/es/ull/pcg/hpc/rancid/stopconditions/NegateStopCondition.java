package es.ull.pcg.hpc.rancid.stopconditions;

import es.ull.pcg.hpc.rancid.StopCondition;
import es.ull.pcg.hpc.rancid.results.MapResult;

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
    public void update (MapResult runResults) {
        if (mStop != null)
            mStop.update(runResults);
    }

    @Override
    public void reset (MapResult paramResults) {
        if (mStop != null)
            mStop.reset(paramResults);
    }
}
