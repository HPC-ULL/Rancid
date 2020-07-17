package es.ull.pcg.hpc.benchmark.filters;

import es.ull.pcg.hpc.benchmark.MetricReduceProcessor;
import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;

import java.util.Collections;

/**
 * Results filtering analyzer that removes the first set of results for each run and metric, which correspond to warm-up
 * iterations.
 */
public class WarmUpIterationsFilter extends MetricReduceProcessor {
    public static final String TITLE = "Non-Warmup";

    private final int mWarmup;

    /**
     * Create a new filter for warm-up iterations.
     * @param warmup Number of warm-up iterations to filter out.
     */
    public WarmUpIterationsFilter (int warmup) {
        super(null);
        this.mWarmup = warmup;
    }

    public static String processedMetricTitle (String metricTitle) {
        return TITLE + " " + metricTitle;
    }

    @Override
    public String processedMetricTitle () {
        return processedMetricTitle(super.processedMetricTitle());
    }

    @Override
    protected Results reduceMetric (ListResult metric) {
        int size = metric.size();
        return new ListResult(mWarmup >= size? Collections.EMPTY_LIST : metric.subList(mWarmup, size),
                              processedMetricTitle(), ResultTypes.Metric);
    }
}
