package es.ull.pcg.hpc.benchmark.analyzers;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.ResultsAnalyzer;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Benchmark results analyzer template for adding analysis nodes to the results by processing the list of measurements
 * obtained for a given metric during a set of repetitions.
 */
public abstract class MetricReduceAnalyzer extends ResultsProcessor implements ResultsAnalyzer {
    private final String mMetricName;
    private MapResult mCachedValues;

    /**
     * Initialize the analyzer template.
     *
     * @param metricName Name of the metric to analyze.
     */
    protected MetricReduceAnalyzer (String metricName) {
        this.mMetricName = metricName;
        this.mCachedValues = null;
    }

    @Override
    public void analyze (Results results) {
        process(results);
    }

    @Override
    public String getName () {
        return mMetricName;
    }

    @Override
    public void processMap (MapResult map) {
        MapResult prevCached = mCachedValues;

        mCachedValues = MapResult.createTemp();

        for (Results result: map.values())
            process(result);

        map.putAll(mCachedValues);

        mCachedValues.clear();
        mCachedValues = prevCached;
    }

    @Override
    public void processList (ListResult list) {
        if (ResultTypes.Metric == list.getType() && list.getTitle().equals(mMetricName)) {
            mCachedValues.put(getName(), reduceMetric(list));
        }
        else {
            for (Results result: list)
                process(result);
        }
    }

    @Override
    public void processValue (ValueResult value) {}

    /**
     * Perform the reduction over the list of results obtained for the selected metric and a single set of parameters.
     *
     * @param metric List of {@link ValueResult} obtained for the selected metric.
     * @return The node to add to the results.
     */
    protected abstract Results reduceMetric (ListResult metric);
}
