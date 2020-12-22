package es.ull.pcg.hpc.rancid.printers;

import es.ull.pcg.hpc.rancid.OutputPrinter;

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
