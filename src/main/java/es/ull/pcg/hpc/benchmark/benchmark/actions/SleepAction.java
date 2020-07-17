package es.ull.pcg.hpc.benchmark.benchmark.actions;

import es.ull.pcg.hpc.benchmark.utils.ThreadUtils;
import es.ull.pcg.hpc.benchmark.BenchmarkAction;

/**
 * An action that introduces a pause of a fixed duration when executed.
 */
public class SleepAction implements BenchmarkAction {
    private final long mTimeMs;

    public SleepAction (long ms) {
        this.mTimeMs = ms;
    }

    @Override
    public void execute () {
        ThreadUtils.waitMs(mTimeMs);
    }
}
