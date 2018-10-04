package es.ull.pcg.hpc.benchmark;

import es.ull.pcg.hpc.benchmark.results.ResultTypes;

/**
 * Common interface for benchmark results.
 */
public interface Results {
    /**
     * Get the name of the results.
     *
     * @return The name.
     */
    String getTitle ();

    /**
     * Get the type or category of the results. These are used to give semantic properties to benchmark results when
     * they are outputted.
     *
     * @return The type.
     */
    ResultTypes getType ();

    /**
     * Accept being processed by a {@link ResultsProcessor}.
     *
     * @param processor Processor that will read or write the results.
     * @param <T> Type of {@link ResultsProcessor}.
     */
    <T extends ResultsProcessor> void accept (T processor);
}
