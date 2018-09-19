package es.ull.pcg.hpc.benchmark.loggers;

import es.ull.pcg.hpc.benchmark.OutputPrinter;
import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.MapResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;
import es.ull.pcg.hpc.benchmark.utils.IterUtils;

/**
 * Results logger that outputs in XML format.
 */
public class XmlResultsLogger extends IndentedResultsLogger {
    /**
     * Create a logger.
     *
     * @param out The underlying printer used for output.
     */
    public XmlResultsLogger (OutputPrinter out) {
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
        mOut.println("<node type=\"" + map.getType() + "\">");

        indent();

        writeIndentation();
        mOut.println("<name>" + map.getTitle() + "</name>");
        writeIndentation();
        mOut.println("<values>");

        indent();

        IterUtils.enumerate(map.values(), (i, result) -> {
            if (result instanceof ValueResult) {
                writeIndentation();
                mOut.println("<node type=\"" + result.getType() + "\">");

                indent();

                writeIndentation();
                mOut.println("<name>" + result.getTitle() + "</name>");
                writeIndentation();
                mOut.print("<value>");
                process(result);
                mOut.println("</value>");

                unindent();

                writeIndentation();
                mOut.print("</node>");
            }
            else {
                process(result);
            }

            mOut.println();
        });

        unindent();

        writeIndentation();
        mOut.println("</values>");

        unindent();

        writeIndentation();
        mOut.print("</node>");
    }

    @Override
    public void processList (ListResult list) {
        writeIndentation();
        mOut.println("<node type=\"" + list.getType() + "\">");

        indent();

        writeIndentation();
        mOut.println("<name>" + list.getTitle() + "</name>");
        writeIndentation();
        mOut.println("<values>");

        indent();

        IterUtils.enumerate(list, (i, result) -> {
            writeIndentation();
            mOut.print("<value>");

            indent();

            process(list.get(i));

            unindent();

            mOut.println("</value>");
        });

        unindent();

        writeIndentation();
        mOut.println("</values>");

        unindent();

        writeIndentation();
        mOut.print("</node>");
    }

    @Override
    public void processValue (ValueResult value) {
        mOut.print(value);
    }
}
