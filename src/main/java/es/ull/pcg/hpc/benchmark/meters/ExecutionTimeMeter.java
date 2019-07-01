package es.ull.pcg.hpc.benchmark.meters;

import es.ull.pcg.hpc.benchmark.Meter;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Meter that uses {@link System#nanoTime()} in order to measure execution time.
 */
public class ExecutionTimeMeter implements Meter {
    public static final String TITLE = "Execution Time";

    private long mStartTime, mStopTime;

    @Override
    public void start () {
        mStartTime = System.nanoTime();
    }

    @Override
    public void stop () {
        mStopTime = System.nanoTime();
    }

    @Override
    public void stopError () {
        mStopTime = mStartTime;
    }

    @Override
    public void reset () {
        mStartTime = mStopTime = 0;
    }

    @Override
    public String getTitle () {
        return TITLE;
    }

    @Override
    public ValueResult getMeasure () {
        return new ValueResult(getTitle(), mStopTime - mStartTime);
    }
}
