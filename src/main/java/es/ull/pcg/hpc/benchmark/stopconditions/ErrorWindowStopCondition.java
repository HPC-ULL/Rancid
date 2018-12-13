package es.ull.pcg.hpc.benchmark.stopconditions;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;
import es.ull.pcg.hpc.benchmark.StopCondition;
import es.ull.pcg.hpc.benchmark.meters.SuccessfulRunsMeter;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.MathUtils;

import java.util.Arrays;

/**
 * Error-based stop condition. Benchmarks run until the coefficient of variation is below a threshold, only considering
 * the last successful values contained within the chosen window size.
 */
public class ErrorWindowStopCondition extends ResultsProcessor implements StopCondition {
    private final String mMetricName;
    private final int mWindow;
    private final double mCvTarget;

    private double mCurrentCv;
    private ListResult mCurrentSuccess;

    /**
     * Create a new error-based stop condition.
     *
     * @param metricName Name of the metric for which to calculate the error.
     * @param window Size of the window.
     * @param cvTarget Target maximum coefficient of variation.
     */
    public ErrorWindowStopCondition(String metricName, int window, double cvTarget) {
        this.mMetricName = metricName;
        this.mWindow = window;
        this.mCvTarget = cvTarget;
    }

    @Override
    public boolean shouldStop () {
        return mCurrentCv <= mCvTarget;
    }

    @Override
    public void update (Results results) {
        process(results);
    }

    @Override
    public void reset () {
        this.mCurrentCv = Double.MAX_VALUE;
    }

    @Override
    public void processMap (MapResult map) {
        super.processMap(map);

        if (map.getType() == ResultTypes.ParameterSet) {
            this.mCurrentSuccess = (ListResult) map.get(SuccessfulRunsMeter.NAME);
            Results result = map.get(mMetricName);

            if (result != null)
                process(result);

            this.mCurrentSuccess = null;
        }
        else {
            for (Results result: map.values())
                process(result);
        }
    }

    @Override
    public void processList (ListResult list) {
        super.processList(list);
        int numElements = list.size();

        if (numElements >= mWindow && list.getType() == ResultTypes.Metric && list.getTitle().equals(mMetricName)) {
            Results[] results = new Results[mWindow];

            // Fill window of results with the latest successful runs
            int validResults = 0;

            for (int i = numElements - 1; i >= 0; --i) {
                if (validResults >= mWindow)
                    break;

                if (mCurrentSuccess == null || ((ValueResult) mCurrentSuccess.get(i)).intValue() == 1)
                    results[mWindow - ++validResults] = list.get(i);
            }

            if (validResults == mWindow) {
                ListResult filtered = new ListResult(Arrays.asList(results), list.getTitle(), list.getType());

                double avg = MathUtils.arithmeticAvg(filtered);
                double variance = MathUtils.sampleVariance(filtered, avg);

                this.mCurrentCv = Math.sqrt(variance) / avg;
            }
        }
    }
}
