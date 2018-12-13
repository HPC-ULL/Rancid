package es.ull.pcg.hpc.benchmark.analyzers;

import es.ull.pcg.hpc.benchmark.MetricReduceProcessor;
import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.MathUtils;

/**
 * Benchmark results analyzer that calculates the arithmetic average of a given metric found in the results.
 */
public class ArithmeticAverageAnalyzer extends MetricReduceProcessor {
    public static final String NAME = "Arithmetic Avg";

    /**
     * Create a new arithmeticAvg analyzer.
     *
     * @param metricName Name of the metric for which to calculate the arithmeticAvg.
     */
    public ArithmeticAverageAnalyzer (String metricName) {
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
        return new ValueResult(processedMetricTitle(), ResultTypes.Analysis, MathUtils.arithmeticAvg(metric));
    }
}
