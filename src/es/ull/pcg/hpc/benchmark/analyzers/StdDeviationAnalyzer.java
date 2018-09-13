package es.ull.pcg.hpc.benchmark.analyzers;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.MathUtils;

/**
 * Benchmark results analyzer that calculates the sample standard deviation of a given metric found in the results.
 */
public class StdDeviationAnalyzer extends MetricReduceAnalyzer {
    public static final String NAME = "Std Deviation";

    /**
     * Create a new standard deviation analyzer.
     *
     * @param metricName Name of the metric for which to calculate the standard deviation.
     */
    public StdDeviationAnalyzer (String metricName) {
        super(metricName);
    }

    @Override
    public String getName () {
        return super.getName() + " " + NAME;
    }

    @Override
    protected Results reduceMetric (ListResult metric) {
        return new ValueResult(getName(), ResultTypes.Analysis.toString(), MathUtils.sampleStdDev(metric));
    }
}
