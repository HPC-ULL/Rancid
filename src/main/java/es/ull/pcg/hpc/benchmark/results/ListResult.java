package es.ull.pcg.hpc.benchmark.results;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A benchmark result composed of a list of results.
 */
public class ListResult extends ArrayList<Results> implements Results {
    private final String mTitle, mType;

    /**
     * Create a new list result.
     *
     * @param title Descriptive name for the list.
     * @param type Type or category of this result.
     */
    public ListResult (String title, String type) {
        this.mTitle = title;
        this.mType = type;
    }

    /**
     * Create a new list result with an initial set of values.
     *
     * @param elements Initial values, copied in this list.
     * @param title Descriptive name for the list.
     * @param type Type or category of this result.
     */
    public ListResult (Collection<Results> elements, String title, String type) {
        super(elements);
        this.mTitle = title;
        this.mType = type;
    }

    @Override
    public String getTitle () {
        return mTitle;
    }

    @Override
    public String getType () {
        return mType;
    }

    @Override
    public <T extends ResultsProcessor> void accept (T processor) {
        processor.processList(this);
    }
}