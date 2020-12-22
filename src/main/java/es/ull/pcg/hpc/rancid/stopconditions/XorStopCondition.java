package es.ull.pcg.hpc.rancid.stopconditions;

import es.ull.pcg.hpc.rancid.StopCondition;
import es.ull.pcg.hpc.rancid.benchmark.BenchmarkManager;

/**
 * Binary stop condition implementing an XOR operation. It tells the {@link BenchmarkManager}
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
