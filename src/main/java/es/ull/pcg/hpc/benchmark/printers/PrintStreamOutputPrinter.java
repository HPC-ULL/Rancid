package es.ull.pcg.hpc.benchmark.printers;

import es.ull.pcg.hpc.benchmark.OutputPrinter;

import java.io.PrintStream;

/**
 * Output printer based on a {@link PrintStream}.
 */
public class PrintStreamOutputPrinter extends OutputPrinter {
    private final PrintStream mStream;

    /**
     * Create a new output printer.
     *
     * @param stream Underlying printing handler.
     */
    public PrintStreamOutputPrinter (PrintStream stream) {
        this.mStream = stream;
    }

    @Override
    public void print (String s) {
        mStream.print(s);
    }

    @Override
    public void flush () {
        mStream.flush();
    }
}
