package es.ull.pcg.hpc.benchmark.loggers;

import es.ull.pcg.hpc.benchmark.OutputPrinter;
import es.ull.pcg.hpc.benchmark.ResultsLogger;
import es.ull.pcg.hpc.benchmark.ResultsProcessor;

import java.util.stream.IntStream;

public abstract class IndentedResultsLogger extends ResultsProcessor implements ResultsLogger {
    protected final OutputPrinter mOut;
    private int mIndentLevel;

    protected IndentedResultsLogger (OutputPrinter out) {
        this.mOut = out;
        this.mIndentLevel = 0;
    }

    protected void resetIndentation () {
        mIndentLevel = 0;
    }

    protected void indent () {
        ++mIndentLevel;
    }

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
