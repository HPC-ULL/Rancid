package es.ull.pcg.hpc.benchmark.benchmark.actions;

import es.ull.pcg.hpc.benchmark.BenchmarkAction;

import java.util.ArrayList;
import java.util.List;

/**
 * An action composed by several other actions to be executed sequentially.
 */
public class CompositeAction implements BenchmarkAction {
    private final List<BenchmarkAction> mActions;

    public CompositeAction () {
        this.mActions = new ArrayList<>();
    }

    public void addAction (BenchmarkAction action) {
        mActions.add(action);
    }

    @Override
    public void execute () {
        for (BenchmarkAction action: mActions)
            action.execute();
    }
}
