package es.ull.pcg.hpc.rancid;

import es.ull.pcg.hpc.rancid.benchmark.ModularBenchmarkRunner;

/**
 * A simple action that can be executed by a {@link ModularBenchmarkRunner}.
 */
public interface BenchmarkAction {
    /**
     * Action implementation.
     */
    void execute ();
}
