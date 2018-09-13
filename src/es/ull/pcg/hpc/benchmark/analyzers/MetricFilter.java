package es.ull.pcg.hpc.benchmark.analyzers;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.ResultsAnalyzer;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Benchmark results analyzer that removes a given metric found in the results.
 */
public class MetricFilter extends ResultsProcessor implements ResultsAnalyzer {
    public static final String NAME = "Filter";

    private final String mMetricName;

    /**
     * Create a new metric filter.
     *
     * @param metricName Name of the metric to filter out from the results.
     */
    public MetricFilter (String metricName) {
        this.mMetricName = metricName;
    }

    @Override
    public void analyze (Results results) {
        process(results);
    }

    @Override
    public String getName () {
        return mMetricName + " " + NAME;
    }

    @Override
    public void processMap (MapResult map) {
        map.forEach((k, v) -> process(v));
        map.remove(mMetricName);
    }

    @Override
    public void processList (ListResult list) {
        list.forEach(this::process);
    }

    @Override
    public void processValue (ValueResult value) {}
}
