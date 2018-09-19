package es.ull.pcg.hpc.benchmark;

import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Base class for processing benchmark results through a visitor pattern. It contains specific methods for processing
 * each kind of {@link Results}.
 */
public abstract class ResultsProcessor {
    /**
     * Process benchmark results.
     *
     * @param results Results to process.
     */
    public final void process (Results results) {
        if (results != null)
            results.accept(this);
    }

    /**
     * Process a {@link MapResult}.
     *
     * @param map Map to process.
     */
    public abstract void processMap (MapResult map);

    /**
     * Process a {@link ListResult}.
     *
     * @param list List to process.
     */
    public abstract void processList (ListResult list);

    /**
     * Process a {@link ValueResult}.
     *
     * @param value Value to process.
     */
    public abstract void processValue (ValueResult value);
}
