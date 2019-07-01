package es.ull.pcg.hpc.benchmark.results;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;

import java.util.List;
import java.util.TreeMap;

/**
 * A benchmark result mapping {@link String} values to other results.
 */
public class MapResult extends TreeMap<String, Results> implements Results {
    private final String mTitle;
    private final ResultTypes mType;

    /**
     * Create a new map result.
     *
     * @param title Descriptive name for the map.
     * @param type Type or category of this result.
     * @param titles Names of the results stored in the map.
     */
    public MapResult (String title, ResultTypes type, List<String> titles) {
        this.mTitle = title;
        this.mType = type;

        for (String name: titles)
            put(name, null);
    }

    /**
     * Create an empty map with no title or type.
     */
    private MapResult () {
        this.mTitle = null;
        this.mType = null;
    }

    /**
     * Create a temporary map result that is not intended to be a part of the final result structure.
     *
     * @return A new map result with no title or type.
     */
    public static MapResult createTemp () {
        return new MapResult();
    }

    @Override
    public String getTitle () {
        return mTitle;
    }

    @Override
    public ResultTypes getType () {
        return mType;
    }

    @Override
    public <T extends ResultsProcessor> void accept (T processor) {
        processor.processMap(this);
    }
}
