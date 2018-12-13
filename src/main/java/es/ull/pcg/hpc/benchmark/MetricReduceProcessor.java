package es.ull.pcg.hpc.benchmark;

import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Benchmark results analyzer template for adding analysis nodes to the results by processing the list of measurements
 * obtained for a given metric during a set of repetitions.
 */
public abstract class MetricReduceProcessor extends ResultsProcessor {
    private final String mMetricName;
    private MapResult mAddedNodes;

    /**
     * Initialize the analyzer template.
     *
     * @param metricName Name of the metric to analyze.
     */
    protected MetricReduceProcessor (String metricName) {
        this.mMetricName = metricName;
        this.mAddedNodes = null;
    }

    @Override
    public void processMap (MapResult map) {
        super.processMap(map);

        MapResult prevNodes = mAddedNodes;
        mAddedNodes = MapResult.createTemp();

        for (Results result: map.values())
            process(result);

        map.putAll(mAddedNodes);
        mAddedNodes.clear();
        mAddedNodes = prevNodes;
    }

    @Override
    public void processList (ListResult list) {
        super.processList(list);

        if (ResultTypes.Metric == list.getType() && (mMetricName == null || list.getTitle().equals(mMetricName))) {
            Results reduced = reduceMetric(list);

            if (reduced != null)
                mAddedNodes.put(processedMetricTitle(), reduced);
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
