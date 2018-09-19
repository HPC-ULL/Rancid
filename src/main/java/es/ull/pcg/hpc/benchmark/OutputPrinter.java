package es.ull.pcg.hpc.benchmark;

/**
 * Base class for output printers, used by {@link ResultsLogger} classes.
 */
public abstract class OutputPrinter {
    private static final String ENDL = System.lineSeparator();

    public void print (boolean b) {
        print(b? "true" : "false");
    }

    public void print (char c) {
        print(String.valueOf(c));
    }

    public void print (int i) {
        print(String.valueOf(i));
    }

    public void print (long l) {
        print(String.valueOf(l));
    }

    public void print (float f) {
        print(String.valueOf(f));
    }

    public void print (double d) {
        print(String.valueOf(d));
    }

    public void print (Object obj) {
        print(String.valueOf(obj));
    }

    public void println () {
        print(ENDL);
        flush();
    }

    public void println (char c) {
        print(c);
        println();
    }

    public void println (int i) {
        print(i);
        println();
    }

    public void println (long l) {
        print(l);
        println();
    }

    public void println (float f) {
        print(f);
        println();
    }

    public void println (double d) {
        print(d);
        println();
    }

    public void println (String s) {
        print(s);
        println();
    }

    public void println (Object obj) {
        print(obj);
        println();
    }

    /**
     * Print a string to the output.
     *
     * @param s String to be printed.
     */
    public abstract void print (String s);

    /**
     * Ensure previously printed data is sent to the output device.
     */
    public abstract void flush ();
}
