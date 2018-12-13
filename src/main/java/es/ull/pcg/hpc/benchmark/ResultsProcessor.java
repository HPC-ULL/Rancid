package es.ull.pcg.hpc.benchmark;

import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Base class for processing benchmark results through a visitor pattern. It contains specific methods for processing
 * each kind of {@link Results}.
 */
public abstract class ResultsProcessor {
    private String mCurrentMetricTitle;

    /**
     * Create a new results processor.
     */
    protected ResultsProcessor () {
        this.mCurrentMetricTitle = null;
    }

    /**
     * Process benchmark results.
     *
     * @param results Results to process.
     */
    public final void process (Results results) {
        if (results != null) {
            String prevTitle = mCurrentMetricTitle;
            results.accept(this);
            this.mCurrentMetricTitle = prevTitle;
        }
    }

    /**
     * The name of a new results node being created as a result of an analysis or filtering operation.
     *
     * @return The name of the processed node, by default the title of the results node currently being processed.
     */
    protected String processedMetricTitle () {
        return mCurrentMetricTitle;
    }

    /**
     * Process a {@link MapResult}.
     *
     * @param map Map to process.
     */
    public void processMap (MapResult map) {
        this.mCurrentMetricTitle = map.getTitle();
    }

    /**
     * Process a {@link ListResult}.
     *
     * @param list List to process.
     */
    public void processList (ListResult list) {
        this.mCurrentMetricTitle = list.getTitle();
    }

    /**
     * Process a {@link ValueResult}.
     *
     * @param value Value to process.
     */
    public void processValue (ValueResult value) {
        this.mCurrentMetricTitle = value.getTitle();
    }
}
