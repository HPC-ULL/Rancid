package es.ull.pcg.hpc.benchmark.utils;

/**
 * Set of utils working with threads.
 */
public class ThreadUtils {
    /**
     * Introduce a timed pause in the current thread, ignoring interruptions.
     *
     * @param ms Amount of time to wait, in milliseconds.
     */
    public static void waitMs (long ms) {
        long t1 = System.currentTimeMillis();
        long t = 0;

        while (t < ms) {
            try {
                Thread.sleep(ms - t);
                t = ms;
            } catch (InterruptedException e) {
                long t2 = System.currentTimeMillis();
                t += t2 - t1;
                t1 = t2;
            }
        }
    }
}
