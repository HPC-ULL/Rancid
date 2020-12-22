package es.ull.pcg.hpc.rancid.removers;

import es.ull.pcg.hpc.rancid.Results;
import es.ull.pcg.hpc.rancid.ResultsProcessor;
import es.ull.pcg.hpc.rancid.meters.SuccessfulRunsMeter;
import es.ull.pcg.hpc.rancid.results.ListResult;
import es.ull.pcg.hpc.rancid.results.MapResult;
import es.ull.pcg.hpc.rancid.results.ResultTypes;
import es.ull.pcg.hpc.rancid.results.ValueResult;

import java.util.Iterator;

/**
 * Benchmark results analyzer that filters out invalid results.
 */
public class InvalidRunsRemover extends ResultsProcessor {
    public static final String TITLE = "Valid";

    private ListResult mInvalid;

    public InvalidRunsRemover () {
        this.mInvalid = null;
    }

    public static String processedMetricTitle (String metricTitle) {
        return TITLE + " " + metricTitle;
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

        // Apply filter to all metrics and remove unsuccessful runs
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
                list.getTitle().equals(SuccessfulRunsMeter.TITLE)) {
                mInvalid = list;
            }
            else {
                for (Results result: list)
                    process(result);
            }
        }
    }
}

