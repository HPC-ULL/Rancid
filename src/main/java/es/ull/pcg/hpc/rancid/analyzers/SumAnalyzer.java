package es.ull.pcg.hpc.rancid.analyzers;

import es.ull.pcg.hpc.rancid.MetricReduceProcessor;
import es.ull.pcg.hpc.rancid.Results;
import es.ull.pcg.hpc.rancid.results.ListResult;
import es.ull.pcg.hpc.rancid.results.ResultTypes;
import es.ull.pcg.hpc.rancid.results.ValueResult;
import es.ull.pcg.hpc.rancid.utils.MathUtils;

/**
 * Benchmark results analyzer that sums the values of a given metric found in the results.
 */
public class SumAnalyzer extends MetricReduceProcessor {
    public static final String TITLE = "Total";

    /**
     * Create a new sum analyzer.
     *
     * @param metricTitle Name of the metric for which to calculate the sum.
     */
    public SumAnalyzer (String metricTitle) {
        super(metricTitle);
    }

    public static String processedMetricTitle (String metricTitle) {
        return metricTitle + " " + TITLE;
    }

    @Override
    public String processedMetricTitle () {
        return processedMetricTitle(super.processedMetricTitle());
    }

    @Override
    protected Results reduceMetric (ListResult metric) {
        return new ValueResult(processedMetricTitle(), ResultTypes.Analysis, MathUtils.sum(metric));
    }
}
