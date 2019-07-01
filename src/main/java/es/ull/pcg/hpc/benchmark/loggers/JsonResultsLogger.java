package es.ull.pcg.hpc.benchmark.loggers;

import es.ull.pcg.hpc.benchmark.OutputPrinter;
import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

import java.util.Collection;

/**
 * Results logger that outputs in JSON format.
 */
public class JsonResultsLogger extends IndentedResultsLogger {
    /**
     * Create a logger.
     *
     * @param out The underlying printer used for output.
     */
    public JsonResultsLogger (OutputPrinter out) {
        super(out);
    }

    @Override
    public void log (Results results) {
        process(results);
        mOut.flush();
    }

    @Override
    public void processMap (MapResult map) {
        writeIndentation();
        mOut.println("{");

        indent();

        writeIndentation();
        mOut.println("\"Title\": \"" + map.getTitle() + "\",");
        writeIndentation();
        mOut.println("\"Type\": \"" + map.getType() + "\",");
        writeIndentation();
        mOut.println("\"Values\": [");

        indent();

        Collection<? extends Results> values = map.values();
        final int size = values.size();

        int i = 0;
        for (Results result: values) {
            process(result);

            if (i < size - 1)
                mOut.println(",");
            else
                mOut.println();

            ++i;
        }

        unindent();

        writeIndentation();
        mOut.println("]");

        unindent();

        writeIndentation();
        mOut.print('}');
    }

    @Override
    public void processList (ListResult list) {
        writeIndentation();
        mOut.println("{");

        indent();

        writeIndentation();
        mOut.println("\"Title\": \"" + list.getTitle() + "\",");
        writeIndentation();
        mOut.println("\"Type\": \"" + list.getType() + "\",");
        writeIndentation();
        mOut.println("\"Values\": [");

        indent();

        final int size = list.size();
        int i = 0;

        for (Results result: list) {
            if (result instanceof ValueResult) {
                writeIndentation();
                mOut.print(result);
            }
            else {
                process(list.get(i));
            }

            if (i < size - 1)
                mOut.println(",");
            else
                mOut.println();

            ++i;
        }

        unindent();

        writeIndentation();
        mOut.println("]");

        unindent();

        writeIndentation();
        mOut.print("}");
    }

    @Override
    public void processValue (ValueResult value) {
        writeIndentation();
        mOut.println("{");

        indent();

        writeIndentation();
        mOut.println("\"Title\": \"" + value.getTitle() + "\",");
        writeIndentation();
        mOut.println("\"Type\": \"" + value.getType() + "\",");
        writeIndentation();
        mOut.println("\"Value\": " + value);

        unindent();

        writeIndentation();
        mOut.print("}");
    }
}
