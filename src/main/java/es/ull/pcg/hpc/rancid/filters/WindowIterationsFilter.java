package es.ull.pcg.hpc.rancid.filters;

import es.ull.pcg.hpc.rancid.MetricReduceProcessor;
import es.ull.pcg.hpc.rancid.Results;
import es.ull.pcg.hpc.rancid.results.ListResult;
import es.ull.pcg.hpc.rancid.results.ResultTypes;

/**
 * Results filtering analyzer that removes the first set of results for each run and metric, only keeping the latest
 * iterations that fall within the specified window.
 */
public class WindowIterationsFilter extends MetricReduceProcessor {
    public static final String TITLE = "Window";

    private final int mWindow;

    /**
     * Create a new filter for iterations within a window.
     * @param window Size of the window of iterations to keep.
     */
    public WindowIterationsFilter (int window) {
        super(null);
        this.mWindow = window;
    }

    public static String processedMetricTitle (String metricTitle) {
        return TITLE + " " + metricTitle;
    }

    @Override
    public String processedMetricTitle () {
        return processedMetricTitle(super.processedMetricTitle());
    }

    @Override
    protected Results reduceMetric (ListResult metric) {
        int size = metric.size();
        return new ListResult(mWindow >= size? metric : metric.subList(size - mWindow, size),
                processedMetricTitle(), ResultTypes.Metric);
    }
}
