package es.ull.pcg.hpc.benchmark.loggers;

import es.ull.pcg.hpc.benchmark.*;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.IterUtils;

/**
 * Results logger intended to provide human-readable output.
 * <p>
 *     It provides the ability to log complete results and also online logging.
 * </p>
 */
public class HumanReadableResultsLogger extends IndentedResultsLogger implements ProgressiveResultsLogger {
    /**
     * Create a logger.
     *
     * @param out The underlying printer used for output.
     */
    public HumanReadableResultsLogger (OutputPrinter out) {
        super(out);
    }

    @Override
    public void log (Results results) {
        process(results);
        mOut.flush();
    }

    @Override
    public void processMap (MapResult map) {
        enterMap(map.getTitle(), map.getType());
        map.values().forEach(this::log);
        exitMap();
    }

    @Override
    public void processList (ListResult list) {
        enterList(list.getTitle(), list.getType());

        if (list.get(0) instanceof ValueResult)
            logValues(list);
        else
            list.forEach(this::log);

        exitList();
    }

    @Override
    public void processValue (ValueResult value) {
        enterValue(value.getTitle(), value.getType());
        logValue(value);
        exitValue();
    }

    @Override
    public void startProgressiveLog (String title) {
        resetIndentation();
        enterMap(title, ResultTypes.MultiBenchmark.toString());
    }

    @Override
    public void endProgressiveLog () {
        exitMap();
        mOut.flush();
    }

    @Override
    public void enterMap (String title, String type) {
        writeIndentation();
        mOut.println(type + ": \"" + title + "\"");
        indent();
    }

    @Override
    public void exitMap () {
        unindent();
    }

    @Override
    public void enterList (String title, String type) {
        writeIndentation();
        mOut.println(type + ": \"" + title + "\": [");
        indent();
    }

    @Override
    public void exitList () {
        unindent();
        writeIndentation();
        mOut.println(']');
    }

    @Override
    public void enterValue (String title, String type) {
        writeIndentation();
        mOut.print(type + ": \"" + title + "\": ");
    }

    @Override
    public void exitValue () {
        mOut.println();
    }

    @Override
    public void logValue (ValueResult value) {
        mOut.print(value);
    }

    @Override
    public void logValues (ListResult values) {
        final int size = values.size();

        writeIndentation();
        IterUtils.enumerate(values, (i, result) -> {
            mOut.print(values.get(i));
            if (i < size - 1) {
                mOut.print(", ");

                if ((i+1) % 10 == 0) {
                    mOut.println();
                    writeIndentation();
                }
            }
        });

        mOut.println();
    }
}
