package es.ull.pcg.hpc.benchmark.meters;

import es.ull.pcg.hpc.benchmark.Meter;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Meter that measures {@code 1} or {@code 0} depending on if a benchmark executed successfully or not.
 */
public class SuccessfulRunsMeter implements Meter {
    public static final String TITLE = "Successful Runs";

    private boolean mRunSuccess;

    @Override
    public void start () {}

    @Override
    public void stop () {
        mRunSuccess = true;
    }

    @Override
    public void stopError () {
        mRunSuccess = false;
    }

    @Override
    public void reset () {}

    @Override
    public String getTitle () {
        return TITLE;
    }

    @Override
    public ValueResult getMeasure () {
        return new ValueResult(getTitle(), mRunSuccess? 1 : 0);
    }
}
