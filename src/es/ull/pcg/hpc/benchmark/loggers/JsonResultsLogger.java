package es.ull.pcg.hpc.benchmark.loggers;

import es.ull.pcg.hpc.benchmark.OutputPrinter;
import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.IterUtils;

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
        mOut.println("\"Name\": \"" + map.getTitle() + "\",");
        writeIndentation();
        mOut.println("\"Type\": \"" + map.getType() + "\",");
        writeIndentation();
        mOut.println("\"Values\": [");

        indent();

        Collection<? extends Results> values = map.values();
        final int size = values.size();

        IterUtils.enumerate(values, (i, result) -> {
            process(result);

            if (i < size - 1)
                mOut.println(",");
            else
                mOut.println();
        });

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
        mOut.println("\"Name\": \"" + list.getTitle() + "\",");
        writeIndentation();
        mOut.println("\"Type\": \"" + list.getType() + "\",");
        writeIndentation();
        mOut.println("\"Values\": [");

        indent();

        final int size = list.size();
        IterUtils.enumerate(list, (i, result) -> {
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
        });

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
        mOut.println("\"Name\": \"" + value.getTitle() + "\",");
        writeIndentation();
        mOut.println("\"Type\": \"" + value.getType() + "\",");
        writeIndentation();
        mOut.println("\"Value\": " + value);

        unindent();

        writeIndentation();
        mOut.print("}");
    }
}
