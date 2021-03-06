package es.ull.pcg.hpc.rancid.loggers;

import es.ull.pcg.hpc.rancid.OutputPrinter;
import es.ull.pcg.hpc.rancid.Results;
import es.ull.pcg.hpc.rancid.results.ListResult;
import es.ull.pcg.hpc.rancid.results.MapResult;
import es.ull.pcg.hpc.rancid.results.ValueResult;

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
        writeIndentation();
        mOut.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writeIndentation();
        mOut.println("<rancid>");

        indent();

        process(results);

        unindent();

        mOut.println();
        writeIndentation();
        mOut.println("</rancid>");

        mOut.flush();
    }

    @Override
    public void processMap (MapResult map) {
        super.processMap(map);

        writeIndentation();
        mOut.println("<node type=\"" + map.getType() + "\">");

        indent();

        writeIndentation();
        mOut.println("<title>" + map.getTitle() + "</title>");
        writeIndentation();
        mOut.println("<values>");

        indent();

        for (Results result: map.values()) {
            if (result instanceof ValueResult) {
                writeIndentation();
                mOut.println("<node type=\"" + result.getType() + "\">");

                indent();

                writeIndentation();
                mOut.println("<title>" + result.getTitle() + "</title>");
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
        }

        unindent();

        writeIndentation();
        mOut.println("</values>");

        unindent();

        writeIndentation();
        mOut.print("</node>");
    }

    @Override
    public void processList (ListResult list) {
        super.processList(list);

        writeIndentation();
        mOut.println("<node type=\"" + list.getType() + "\">");

        indent();

        writeIndentation();
        mOut.println("<title>" + list.getTitle() + "</title>");
        writeIndentation();
        mOut.println("<values>");

        indent();

        for (Results result: list) {
            writeIndentation();
            mOut.print("<value>");

            indent();

            process(result);

            unindent();

            mOut.println("</value>");
        }

        unindent();

        writeIndentation();
        mOut.println("</values>");

        unindent();

        writeIndentation();
        mOut.print("</node>");
    }

    @Override
    public void processValue (ValueResult value) {
        super.processValue(value);
        mOut.print(value);
    }
}
