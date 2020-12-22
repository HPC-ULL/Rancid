package es.ull.pcg.hpc.rancid.removers;

import es.ull.pcg.hpc.rancid.Results;
import es.ull.pcg.hpc.rancid.ResultsProcessor;
import es.ull.pcg.hpc.rancid.results.ListResult;
import es.ull.pcg.hpc.rancid.results.MapResult;
import es.ull.pcg.hpc.rancid.results.ResultTypes;

import java.util.HashSet;
import java.util.Set;

/**
 * Benchmark results analyzer that removes a given metric found in the results.
 */
public class ResultsRemover extends ResultsProcessor {
    private final ResultTypes mType;
    private final String mElementTitle;

    /**
     * Create a new metric filter.
     *
     * @param type Type of elements to remove from the results.
     * @param elementTitle Name of the element to remove from the results.
     */
    public ResultsRemover (ResultTypes type, String elementTitle) {
        this.mType = type;
        this.mElementTitle = elementTitle;
    }

    @Override
    public void processMap (MapResult map) {
        super.processMap(map);
        boolean found = false;

        for (Results result: map.values()) {
            if (result.getType() == mType && result.getTitle().equals(mElementTitle))
                found = true;
            else
                process(result);
        }

        if (found)
            map.remove(mElementTitle);
    }

    @Override
    public void processList (ListResult list) {
        super.processList(list);

        Set<Integer> found = new HashSet<>();
        int i = 0;

        for (Results result: list) {
            if (result.getType() == mType && result.getTitle().equals(mElementTitle))
                found.add(i);
            else
                process(result);

            ++i;
        }

        if (!found.isEmpty()) {
            for (Integer idx: found)
                list.remove(idx.intValue());
        }
    }
}
