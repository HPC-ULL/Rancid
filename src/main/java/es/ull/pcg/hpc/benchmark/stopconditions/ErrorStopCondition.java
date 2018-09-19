package es.ull.pcg.hpc.benchmark.stopconditions;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;
import es.ull.pcg.hpc.benchmark.StopCondition;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.MathUtils;

/**
 * Error-based stop condition. Benchmarks run until the coefficient of variation is below a threshold, discarding
 * a number of warm-up results.
 */
public class ErrorStopCondition extends ResultsProcessor implements StopCondition {
    private final String mMetricName;
    private final int mWarmup;
    private final double mCvTarget;

    private double mCurrentCv;

    /**
     * Create a new error-based stop condition.
     *
     * @param metricName Name of the metric for which to calculate the error.
     * @param warmup Number of ignored iteration measurements.
     * @param cvTarget Target maximum coefficient of variation.
     */
    public ErrorStopCondition (String metricName, int warmup, double cvTarget) {
        this.mMetricName = metricName;
        this.mWarmup = warmup;
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
        if (map.getType().equals(ResultTypes.ParameterSet.toString())) {
            Results result = map.get(mMetricName);

            if (result != null)
                process(result);
        }
        else {
            for (Results result: map.values())
                process(result);
        }
    }

    @Override
    public void processList (ListResult list) {
        if (list.getType().equals(ResultTypes.Metric.toString()) && list.getTitle().equals(mMetricName) &&
            mWarmup < list.size()) {
            ListResult filtered = new ListResult(list.subList(mWarmup, list.size()), list.getTitle(), list.getType());

            double avg = MathUtils.average(filtered);
            double variance = MathUtils.sampleVariance(avg, filtered);

            mCurrentCv = Math.sqrt(variance) / avg;
        }
    }

    @Override
    public void processValue (ValueResult value) {}
}
