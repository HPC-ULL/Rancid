package es.ull.pcg.hpc.benchmark.results;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;

import java.util.List;
import java.util.TreeMap;

/**
 * A benchmark result mapping {@link String} values to other results.
 */
public class MapResult extends TreeMap<String, Results> implements Results {
    private final String mTitle, mType;

    /**
     * Create a new map result.
     *
     * @param title Descriptive name for the map.
     * @param type Type or category of this result.
     * @param names Names of the results stored in the map.
     */
    public MapResult (String title, String type, List<String> names) {
        this.mTitle = title;
        this.mType = type;
        names.forEach(name -> this.put(name, null));
    }

    /**
     * Create an empty map with no title or type.
     */
    private MapResult () {
        this.mTitle = this.mType = null;
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
    public String getType () {
        return mType;
    }

    @Override
    public <T extends ResultsProcessor> void accept (T processor) {
        processor.processMap(this);
    }
}
