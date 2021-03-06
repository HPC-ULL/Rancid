package es.ull.pcg.hpc.rancid.analyzers;

import es.ull.pcg.hpc.rancid.Results;
import es.ull.pcg.hpc.rancid.ResultsProcessor;
import es.ull.pcg.hpc.rancid.results.ListResult;
import es.ull.pcg.hpc.rancid.results.MapResult;
import es.ull.pcg.hpc.rancid.results.ValueResult;

/**
 * Benchmark results analyzer that removes a given metric found in the results.
 */
public class MetricFilter extends ResultsProcessor {
    public static final String TITLE = "Filter";

    private final String mMetricTitle;

    /**
     * Create a new metric filter.
     *
     * @param metricTitle Name of the metric to filter out from the results.
     */
    public MetricFilter (String metricTitle) {
        this.mMetricTitle = metricTitle;
    }

    @Override
    public void processMap (MapResult map) {
        for (Results result: map.values())
            process(result);

        map.remove(mMetricTitle);
    }

    @Override
    public void processList (ListResult list) {
        for (Results result: list)
            process(result);
    }

    @Override
    public void processValue (ValueResult value) {}
}
