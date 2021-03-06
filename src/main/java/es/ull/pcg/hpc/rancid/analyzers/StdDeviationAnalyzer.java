package es.ull.pcg.hpc.rancid.analyzers;

import es.ull.pcg.hpc.rancid.MetricReduceProcessor;
import es.ull.pcg.hpc.rancid.Results;
import es.ull.pcg.hpc.rancid.results.ListResult;
import es.ull.pcg.hpc.rancid.results.ResultTypes;
import es.ull.pcg.hpc.rancid.results.ValueResult;
import es.ull.pcg.hpc.rancid.utils.MathUtils;

/**
 * Benchmark results analyzer that calculates the sample standard deviation of a given metric found in the results.
 */
public class StdDeviationAnalyzer extends MetricReduceProcessor {
    public static final String TITLE = "Std Dev";

    /**
     * Create a new standard deviation analyzer.
     *
     * @param metricTitle Name of the metric for which to calculate the standard deviation.
     */
    public StdDeviationAnalyzer (String metricTitle) {
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
        return new ValueResult(processedMetricTitle(), ResultTypes.Analysis, MathUtils.sampleStdDev(metric));
    }
}
