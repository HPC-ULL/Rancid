package es.ull.pcg.hpc.benchmark.stopconditions;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.StopCondition;
import es.ull.pcg.hpc.benchmark.meters.SuccessfulRunsMeter;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.MathUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * Error-based stop condition. Benchmarks run until the coefficient of variation is below a threshold, only considering
 * the last successful values contained within the chosen window size.
 */
public class ErrorWindowStopCondition implements StopCondition {
    private final String mMetricName;
    private final int mWindow;
    private final double mCoVTarget;

    protected MapResult mParamResults;
    protected double mWindowAvg;
    protected double mWindowVariance;
    protected int mSteadyIteration;

    /**
     * Create a new error-based stop condition.
     *
     * @param metricName Name of the metric for which to calculate the error.
     * @param window Size of the window.
     * @param covTarget Target maximum coefficient of variation.
     */
    public ErrorWindowStopCondition (String metricName, int window, double covTarget) {
        this.mMetricName = metricName;
        this.mWindow = window;
        this.mCoVTarget = covTarget;
    }

    @Override
    public boolean shouldStop () {
        return mWindowAvg != 0.0 && Math.sqrt(mWindowVariance) / mWindowAvg <= mCoVTarget;
    }

    @Override
    public void update (MapResult runResults) {
        // Only process if last iteration was successful and there's relevant new data
        ValueResult success = (ValueResult) runResults.get(SuccessfulRunsMeter.NAME);

        if (runResults.get(mMetricName) != null && success == null || success.intValue() == 1) {
            // Obtain complete list of results
            ListResult allValues = (ListResult) mParamResults.get(mMetricName);
            ListResult allSuccess = (ListResult) mParamResults.get(SuccessfulRunsMeter.NAME);

            int numResults = allValues.size();

            if (numResults >= mWindow) {
                // Fill window of results with the latest successful runs
                Results[] results = new Results[mWindow];
                int validResults = 0;

                for (int i = numResults - 1; i >= 0; --i) {
                    if (validResults == mWindow)
                        break;

                    if (allSuccess == null || ((ValueResult) allSuccess.get(i)).intValue() == 1)
                        results[mWindow - ++validResults] = allValues.get(i);
                }

                if (validResults == mWindow) {
                    // Calculate the mean and variance inside the window
                    Collection<Results> filtered = Arrays.asList(results);

                    this.mWindowAvg = MathUtils.arithmeticAvg(filtered);
                    this.mWindowVariance = MathUtils.sampleVariance(filtered, mWindowAvg);

                    // Calculate CoV and record iteration where the steady-state was last reached
                    double cov = Math.sqrt(mWindowVariance) / mWindowAvg;

                    if (mSteadyIteration < 0 && cov <= mCoVTarget)
                        mSteadyIteration = numResults - 1;
                    else if (cov > mCoVTarget)
                        mSteadyIteration = -1;
                }
            }
        }
    }

    @Override
    public void reset (MapResult paramResults) {
        this.mParamResults = paramResults;
        this.mWindowAvg = 0.0;
        this.mWindowVariance = 0.0;
        this.mSteadyIteration = -1;
    }
}
