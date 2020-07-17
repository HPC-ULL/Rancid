package es.ull.pcg.hpc.benchmark;

import es.ull.pcg.hpc.benchmark.benchmark.ModularBenchmarkRunner;

/**
 * A simple action that can be executed by a {@link ModularBenchmarkRunner}.
 */
public interface BenchmarkAction {
    /**
     * Action implementation.
     */
    void execute ();
}
