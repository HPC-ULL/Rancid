package es.ull.pcg.hpc.benchmark.stopconditions;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.StopCondition;

/**
 * Stop condition composed by two contained stop conditions, whose decisions are merged in order to obtain a single
 * decision as to whether execution of a benchmark should stop.
 */
public abstract class BinaryStopCondition implements StopCondition {
    private final StopCondition mConditionA, mConditionB;

    /**
     * Create a new stop condition.
     *
     * @param conditionA First contained stop condition.
     * @param conditionB Second contained stop condition.
     */
    public BinaryStopCondition (StopCondition conditionA, StopCondition conditionB) {
        this.mConditionA = conditionA;
        this.mConditionB = conditionB;
    }

    /**
     * Merge the decisions obtained by the contained stop conditions in order to provide a single decision.
     *
     * @param shouldStopA Decision obtained for the first stop condition.
     * @param shouldStopB Decision obtained for the second stop condition.
     * @return {@code true} if, after combining decisions, the current benchmark should stop being repeatedly executed.
     */
    protected abstract boolean shouldStop (boolean shouldStopA, boolean shouldStopB);

    @Override
    public boolean shouldStop () {
        if (mConditionA == null)
            return mConditionB == null || mConditionB.shouldStop();

        if (mConditionB == null)
            return mConditionA.shouldStop();

        return shouldStop(mConditionA.shouldStop(), mConditionB.shouldStop());
    }

    @Override
    public void update (Results results) {
        if (mConditionA != null)
            mConditionA.update(results);

        if (mConditionB != null)
            mConditionB.update(results);
    }

    @Override
    public void reset () {
        if (mConditionA != null)
            mConditionA.reset();

        if (mConditionB != null)
            mConditionB.reset();
    }
}
