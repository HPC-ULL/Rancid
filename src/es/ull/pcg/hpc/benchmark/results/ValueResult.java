package es.ull.pcg.hpc.benchmark.results;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;

/**
 * A numeric benchmark result.
 */
public class ValueResult extends Number implements Results {
    private final String mTitle, mType;
    private final Number mValue;

    /**
     * Create a new numeric result.
     *
     * @param title Descriptive name for the value.
     * @param type Type or category of the value.
     * @param value Number stored as a result.
     */
    public ValueResult (String title, String type, Number value) {
        this.mTitle = title;
        this.mType = type;
        this.mValue = value;
    }

    /**
     * Create a new numeric result with a default type.
     *
     * @param title Descriptive name for the value.
     * @param value Number stored as a result.
     */
    public ValueResult (String title, Number value) {
        this(title, ResultTypes.Value.toString(), value);
    }

    @Override
    public int intValue () {
        return mValue.intValue();
    }

    @Override
    public long longValue () {
        return mValue.longValue();
    }

    @Override
    public float floatValue () {
        return mValue.floatValue();
    }

    @Override
    public double doubleValue () {
        return mValue.doubleValue();
    }

    @Override
    public String toString () {
        return mValue.toString();
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
        processor.processValue(this);
    }
}
