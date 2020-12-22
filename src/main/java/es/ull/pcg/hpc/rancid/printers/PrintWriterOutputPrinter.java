package es.ull.pcg.hpc.rancid.printers;

import es.ull.pcg.hpc.rancid.OutputPrinter;

import java.io.PrintWriter;

/**
 * Output printer based on a {@link PrintWriter}.
 */
public class PrintWriterOutputPrinter extends OutputPrinter {
    private final PrintWriter mWriter;

    /**
     * Create a new output printer.
     *
     * @param writer Underlying printing handler.
     */
    public PrintWriterOutputPrinter (PrintWriter writer) {
        this.mWriter = writer;
    }

    @Override
    public void print (String s) {
        mWriter.print(s);
    }

    @Override
    public void flush () {
        mWriter.flush();
    }
}
