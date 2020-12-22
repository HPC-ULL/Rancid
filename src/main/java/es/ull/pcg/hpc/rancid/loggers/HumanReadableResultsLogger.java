package es.ull.pcg.hpc.rancid.loggers;

import es.ull.pcg.hpc.rancid.*;
import es.ull.pcg.hpc.rancid.results.ResultTypes;
import es.ull.pcg.hpc.rancid.results.ListResult;
import es.ull.pcg.hpc.rancid.results.MapResult;
import es.ull.pcg.hpc.rancid.results.ValueResult;

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
        super.processMap(map);
        enterMap(map.getTitle(), map.getType());

        for (Results value: map.values())
            log(value);

        exitMap();
    }

    @Override
    public void processList (ListResult list) {
        super.processList(list);
        enterList(list.getTitle(), list.getType());

        if (list.get(0) instanceof ValueResult) {
            logValues(list);
        }
        else {
            for (Results value: list)
                log(value);
        }

        exitList();
    }

    @Override
    public void processValue (ValueResult value) {
        super.processValue(value);
        enterValue(value.getTitle(), value.getType());
        logValue(value);
        exitValue();
    }

    @Override
    public void startProgressiveLog (String title) {
        resetIndentation();
        enterMap(title, ResultTypes.MultiBenchmark);
    }

    @Override
    public void endProgressiveLog () {
        exitMap();
        mOut.flush();
    }

    @Override
    public void enterMap (String title, ResultTypes type) {
        writeIndentation();
        mOut.println(type + ": \"" + title + "\"");
        indent();
    }

    @Override
    public void exitMap () {
        unindent();
    }

    @Override
    public void enterList (String title, ResultTypes type) {
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
    public void enterValue (String title, ResultTypes type) {
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

        int i = 0;
        for (Results result: values) {
            mOut.print(result);
            if (i < size - 1) {
                mOut.print(", ");

                if ((i+1) % 10 == 0) {
                    mOut.println();
                    writeIndentation();
                }
            }
            ++i;
        }

        mOut.println();
    }
}
