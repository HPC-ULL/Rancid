package es.ull.pcg.hpc.rancid.benchmark;

import es.ull.pcg.hpc.rancid.BenchmarkAction;
import es.ull.pcg.hpc.rancid.StopCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link BenchmarkRunner} that is composed of {@link BenchmarkAction}s to be executed at different
 * {@link BenchmarkStage}s. It allows complex composition of functions in a simple way.
 */
public class ModularBenchmarkRunner extends BenchmarkRunner {
    private final Map<BenchmarkStage, List<BenchmarkAction>> mActions;

    /**
     * Create and initialize the benchmark runner, with no actions to execute.
     *
     * @param stop The stop condition.
     */
    public ModularBenchmarkRunner (StopCondition stop) {
        super(stop);

        this.mActions = new HashMap<>();
        mActions.put(BenchmarkStage.PRE_BENCHMARK, new ArrayList<BenchmarkAction>());
        mActions.put(BenchmarkStage.PRE_RUN, new ArrayList<BenchmarkAction>());
        mActions.put(BenchmarkStage.POST_RUN, new ArrayList<BenchmarkAction>());
        mActions.put(BenchmarkStage.POST_BENCHMARK, new ArrayList<BenchmarkAction>());
    }

    /**
     * Add an action to a specific benchmark stage. Many actions can be added to each stage, and they are
     * executed in the order they are added.
     *
     * @param stage The stage in which to add the action.
     * @param action The action to add.
     */
    public void addAction (BenchmarkStage stage, BenchmarkAction action) {
        mActions.get(stage).add(action);
    }

    protected void runActions (BenchmarkStage stage) {
        List<BenchmarkAction> actions = mActions.get(stage);

        for (BenchmarkAction action: actions)
            action.execute();
    }

    @Override
    protected void preBenchmark () {
        super.preBenchmark();
        runActions(BenchmarkStage.PRE_BENCHMARK);
    }

    @Override
    protected void preRun () {
        super.preRun();
        runActions(BenchmarkStage.PRE_RUN);
    }

    @Override
    protected void postRun () {
        runActions(BenchmarkStage.POST_RUN);
        super.postRun();
    }

    @Override
    protected void postBenchmark () {
        runActions(BenchmarkStage.POST_BENCHMARK);
        super.postBenchmark();
    }
}
