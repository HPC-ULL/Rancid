package es.ull.pcg.hpc.rancid.benchmark.actions;

import es.ull.pcg.hpc.rancid.BenchmarkAction;

/**
 * An action that records a timestamp at the time it executes. It can be used by other actions to determine elapsed
 * time.
 */
public class TimestampRecordAction implements BenchmarkAction {
    private long mTimestamp = 0;

    public long getTimestamp () {
        return mTimestamp;
    }

    @Override
    public void execute () {
        this.mTimestamp = System.currentTimeMillis();
    }
}
