package es.ull.pcg.hpc.benchmark.stopconditions;

import es.ull.pcg.hpc.benchmark.StopCondition;

/**
 * Binary stop condition implementing an XOR operation. It tells the {@link es.ull.pcg.hpc.benchmark.BenchmarkManager}
 * that the execution of a benchmark should stop being repeated when exactly one of the contained stop conditions say
 * so.
 */
public class XorStopCondition extends BinaryStopCondition {
    /**
     * Create a new stop condition.
     *
     * @param conditionA First contained stop condition.
     * @param conditionB Second contained stop condition.
     */
    public XorStopCondition (StopCondition conditionA, StopCondition conditionB) {
        super(conditionA, conditionB);
    }

    @Override
    protected boolean shouldStop (boolean shouldStopA, boolean shouldStopB) {
        return shouldStopA != shouldStopB;
    }
}
