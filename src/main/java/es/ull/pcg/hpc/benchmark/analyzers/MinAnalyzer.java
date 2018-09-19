package es.ull.pcg.hpc.benchmark.analyzers;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.MathUtils;

/**
 * Benchmark results analyzer that calculates the max of a given metric found in the results.
 */
public class MinAnalyzer extends MetricReduceAnalyzer {
    public static final String NAME = "Min";

    /**
     * Create a new min analyzer.
     *
     * @param metricName Name of the metric for which to calculate the minimum.
     */
    public MinAnalyzer (String metricName) {
        super(metricName);
    }

    @Override
    public String getName () {
        return super.getName() + " " + NAME;
    }

    @Override
    protected Results reduceMetric (ListResult metric) {
        return new ValueResult(getName(), ResultTypes.Analysis.toString(), MathUtils.min(metric));
    }
}
