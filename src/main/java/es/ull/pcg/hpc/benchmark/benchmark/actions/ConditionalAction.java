package es.ull.pcg.hpc.benchmark.benchmark.actions;

import es.ull.pcg.hpc.benchmark.BenchmarkAction;
import es.ull.pcg.hpc.benchmark.utils.ScopeBundle;
import java9.util.function.Predicate;

/**
 * An action that will run depending on a condition that is evaluated at the time of execution.
 *
 * @param <T> The type of the object used as scope.
 *
 * @see ScopeBundle
 */
public class ConditionalAction<T> implements BenchmarkAction {
    protected final BenchmarkAction mAction;
    protected final T mScope;
    protected final Predicate<T> mCondition;

    public ConditionalAction (BenchmarkAction action, T scope, Predicate<T> condition) {
        this.mAction = action;
        this.mScope = scope;
        this.mCondition = condition;
    }

    @Override
    public void execute () {
        if (mCondition.test(mScope))
            mAction.execute();
    }
}
