package es.ull.pcg.hpc.benchmark.removers;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;
import es.ull.pcg.hpc.benchmark.meters.SuccessfulRunsMeter;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

import java.util.Iterator;

/**
 * Benchmark results analyzer that filters out invalid results.
 */
public class InvalidRunsRemover extends ResultsProcessor {
    public static final String NAME = "Valid";

    private ListResult mInvalid;

    public InvalidRunsRemover () {
        this.mInvalid = null;
    }

    public static String processedMetricTitle (String metricTitle) {
        return NAME + " " + metricTitle;
    }

    @Override
    public String processedMetricTitle () {
        return processedMetricTitle(super.processedMetricTitle());
    }

    @Override
    public void processMap (MapResult map) {
        super.processMap(map);
        mInvalid = null;

        // Find the invalid values metric
        for (Results result: map.values()) {
            process(result);

            if (mInvalid != null)
                break;
        }

        // Apply filter to all metrics and remove successful runs
        if (mInvalid != null) {
            for (Results value: map.values())
                process(value);
        }
    }

    @Override
    public void processList (ListResult list) {
        super.processList(list);

        if (mInvalid != null && list != mInvalid) {
            Iterator<Results> resultIterator = list.iterator();
            Iterator<Results> invalidIterator = mInvalid.iterator();

            while (resultIterator.hasNext() && invalidIterator.hasNext()) {
                resultIterator.next();
                ValueResult valid = (ValueResult) invalidIterator.next();

                if (valid.intValue() == 0)
                    resultIterator.remove();
            }
        }
        else {
            if (list.getType() == ResultTypes.Metric &&
                list.getTitle().equals(SuccessfulRunsMeter.NAME)) {
                mInvalid = list;
            }
            else {
                for (Results result: list)
                    process(result);
            }
        }
    }
}

