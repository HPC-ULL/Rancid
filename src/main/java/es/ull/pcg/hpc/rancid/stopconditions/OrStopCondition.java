package es.ull.pcg.hpc.rancid.stopconditions;

import es.ull.pcg.hpc.rancid.StopCondition;
import es.ull.pcg.hpc.rancid.benchmark.BenchmarkManager;

/**
 * Binary stop condition implementing an OR operation. It tells the {@link BenchmarkManager}
 * that the execution of a benchmark should stop being repeated when any of the contained stop conditions say so.
 */
public class OrStopCondition extends BinaryStopCondition {
    /**
     * Create a new stop condition.
     *
     * @param conditionA First contained stop condition.
     * @param conditionB Second contained stop condition.
     */
    public OrStopCondition (StopCondition conditionA, StopCondition conditionB) {
        super(conditionA, conditionB);
    }

    @Override
    public boolean shouldStop (boolean shouldStopA, boolean shouldStopB) {
        return shouldStopA || shouldStopB;
    }
}
