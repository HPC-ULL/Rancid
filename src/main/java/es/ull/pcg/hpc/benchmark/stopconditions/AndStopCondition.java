package es.ull.pcg.hpc.benchmark.stopconditions;

import es.ull.pcg.hpc.benchmark.StopCondition;
import es.ull.pcg.hpc.benchmark.benchmark.BenchmarkManager;

/**
 * Binary stop condition implementing an AND operation. It tells the {@link BenchmarkManager}
 * that the execution of a benchmark should stop being repeated when both of the contained stop conditions say so.
 */
public class AndStopCondition extends BinaryStopCondition {
    /**
     * Create a new stop condition.
     *
     * @param conditionA First contained stop condition.
     * @param conditionB Second contained stop condition.
     */
    public AndStopCondition (StopCondition conditionA, StopCondition conditionB) {
        super(conditionA, conditionB);
    }

    @Override
    public boolean shouldStop (boolean shouldStopA, boolean shouldStopB) {
        return shouldStopA && shouldStopB;
    }
}
