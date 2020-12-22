package es.ull.pcg.hpc.rancid;

import es.ull.pcg.hpc.rancid.results.ListResult;
import es.ull.pcg.hpc.rancid.results.MapResult;
import es.ull.pcg.hpc.rancid.results.ResultTypes;
import es.ull.pcg.hpc.rancid.results.ValueResult;

/**
 * Benchmark results analyzer template for adding analysis nodes to the results by processing the list of measurements
 * obtained for a given metric during a set of repetitions.
 */
public abstract class MetricReduceProcessor extends ResultsProcessor {
    private final String mMetricTitle;
    private MapResult mCachedValues;

    /**
     * Initialize the analyzer template.
     *
     * @param metricTitle Name of the metric to analyze.
     */
    protected MetricReduceProcessor (String metricTitle) {
        this.mMetricTitle = metricTitle;
        this.mCachedValues = null;
    }

    @Override
    public void processMap (MapResult map) {
        super.processMap(map);

        MapResult prevNodes = mCachedValues;
        mCachedValues = MapResult.createTemp();

        for (Results result: map.values())
            process(result);

        map.putAll(mCachedValues);
        mCachedValues.clear();
        mCachedValues = prevNodes;
    }

    @Override
    public void processList (ListResult list) {
        super.processList(list);

        if (ResultTypes.Metric == list.getType() && (mMetricTitle == null || list.getTitle().equals(mMetricTitle))) {
            Results reduced = reduceMetric(list);

            if (reduced != null)
                mCachedValues.put(processedMetricTitle(), reduced);
        }
        else {
            for (Results result: list)
                process(result);
        }
    }

    /**
     * Perform the reduction over the list of results obtained for the selected metric and a single set of parameters.
     *
     * @param metric List of {@link ValueResult} obtained for the selected metric.
     * @return The node to add to the results.
     */
    protected abstract Results reduceMetric (ListResult metric);
}
