package es.ull.pcg.hpc.benchmark.printers;

import es.ull.pcg.hpc.benchmark.OutputPrinter;

import java.io.PrintStream;

public class PrintStreamOutputPrinter extends OutputPrinter {
    private final PrintStream mStream;

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
