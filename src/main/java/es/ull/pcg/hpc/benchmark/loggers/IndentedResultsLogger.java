package es.ull.pcg.hpc.benchmark.loggers;

import es.ull.pcg.hpc.benchmark.OutputPrinter;
import es.ull.pcg.hpc.benchmark.ResultsLogger;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;

import java.util.stream.IntStream;

/**
 * Template for results loggers that write results hierarchically using indentation.
 */
public abstract class IndentedResultsLogger extends ResultsProcessor implements ResultsLogger {
    protected final OutputPrinter mOut;
    private int mIndentLevel;

    /**
     * Initialize the template.
     *
     * @param out Output printer.
     */
    protected IndentedResultsLogger (OutputPrinter out) {
        this.mOut = out;
        this.mIndentLevel = 0;
    }

    /**
     * Set the indentation back to the first level.
     */
    protected void resetIndentation () {
        mIndentLevel = 0;
    }

    /**
     * Add an indentation level.
     */
    protected void indent () {
        ++mIndentLevel;
    }

    /**
     * Remove an indentation level.
     */
    protected void unindent () {
        if (mIndentLevel > 0)
            --mIndentLevel;
    }

    /**
     * Write the current level of indentation in the output.
     */
    protected void writeIndentation () {
        IntStream.range(0, mIndentLevel).forEach(i -> mOut.print("    "));
    }
}
