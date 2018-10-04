package es.ull.pcg.hpc.benchmark.analyzers;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.ResultsAnalyzer;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Results filtering analyzer that removes the first set of results for each run and metric, which correspond to warm-up
 * iterations.
 */
public class WarmupIterationsFilter extends ResultsProcessor implements ResultsAnalyzer {
    public static final String NAME = "Warmup Filter";

    private final int mWarmup;

    /**
     * Create a new filter for warm-up iterations.
     * @param warmup Number of warm-up iterations to filter out.
     */
    public WarmupIterationsFilter (int warmup) {
        this.mWarmup = warmup;
    }

    @Override
    public void analyze (Results results) {
        process(results);
    }

    @Override
    public String getName () {
        return NAME;
    }

    @Override
    public void processMap (MapResult map) {
        for (Results result: map.values())
            process(result);
    }

    @Override
    public void processList (ListResult list) {
        if (ResultTypes.Metric == list.getType()) {
            list.subList(0, mWarmup).clear();
        }
        else {
            for (Results result: list)
                process(result);
        }
    }

    @Override
    public void processValue (ValueResult value) {}
}
