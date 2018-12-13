package es.ull.pcg.hpc.benchmark.analyzers;

import es.ull.pcg.hpc.benchmark.MetricReduceProcessor;
import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.MathUtils;

/**
 * Benchmark results analyzer that sums the values of a given metric found in the results.
 */
public class SumAnalyzer extends MetricReduceProcessor {
    public static final String NAME = "Total";

    /**
     * Create a new sum analyzer.
     *
     * @param metricName Name of the metric for which to calculate the sum.
     */
    public SumAnalyzer (String metricName) {
        super(metricName);
    }

    public static String processedMetricTitle (String metricTitle) {
        return metricTitle + " " + NAME;
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
