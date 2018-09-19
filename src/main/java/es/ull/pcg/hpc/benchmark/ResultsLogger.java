package es.ull.pcg.hpc.benchmark;

/**
 * The common interface for logging benchmark results.
 */
public interface ResultsLogger {
    /**
     * Log benchmark results.
     *
     * @param results Benchmark results to log.
     */
    void log (Results results);
}
