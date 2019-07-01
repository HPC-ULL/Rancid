package es.ull.pcg.hpc.benchmark.analyzers;

import es.ull.pcg.hpc.benchmark.MetricReduceProcessor;
import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.MathUtils;

/**
 * Benchmark results analyzer that calculates a histogram for a given metric found in the results.
 */
public class HistogramAnalyzer extends MetricReduceProcessor {
    public static final String TITLE = "Histogram";
    public static final String BIN_TITLE = "Bin";

    protected Number mCurrentMax, mCurrentMin;
    private final int mBins;

    /**
     * Create a new histogram analyzer.
     *
     * @param metricTitle Name of the metric for which to calculate the histogram.
     * @param bins Number of bins to use when constructing the histogram.
     */
    public HistogramAnalyzer (String metricTitle, int bins) {
        super(metricTitle);
        this.mBins = bins;
    }

    /**
     * Create a new histogram analyzer, using an automatically-calculated number of bins.
     *
     * @param metricTitle Name of the metric for which to calculate the histogram.
     */
    public HistogramAnalyzer (String metricTitle) {
        this(metricTitle, 0);
    }

    public static String processedMetricTitle (String metricTitle) {
        return metricTitle + " " + TITLE;
    }

    /**
     * Calculate the number of bins for a histogram automatically.
     * <br><br>
     *
     * It is based on the square root of the number of elements by default.
     *
     * @param metric List of values to aggregate in a histogram.
     * @return The number of histogram bins.
     */
    protected int automaticNumberOfBins (ListResult metric) {
        return (int) Math.ceil(Math.sqrt(metric.size()));
    }

    @Override
    public String processedMetricTitle () {
        return processedMetricTitle(super.processedMetricTitle());
    }

    @Override
    protected Results reduceMetric (ListResult metric) {
        this.mCurrentMin = MathUtils.min(metric);
        this.mCurrentMax = MathUtils.max(metric);

        int[] histogram = MathUtils.histogram(metric, mCurrentMin, mCurrentMax,
                                              mBins == 0? automaticNumberOfBins(metric) : mBins);

        ListResult result = new ListResult(processedMetricTitle(), ResultTypes.Analysis);

        for (int bin: histogram)
            result.add(new ValueResult(BIN_TITLE, bin));

        return result;
    }
}
