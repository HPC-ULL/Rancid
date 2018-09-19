package es.ull.pcg.hpc.benchmark;

/**
 * Analysis interface for benchmark results.
 */
public interface ResultsAnalyzer {
    /**
     * Analyze benchmark results. This can result in the filtering or addition of data.
     *
     * @param results Results to analyze.
     */
    void analyze (Results results);

    /**
     * Obtain the a descriptive name for the analyzer.
     *
     * @return The name.
     */
    String getName ();
}
