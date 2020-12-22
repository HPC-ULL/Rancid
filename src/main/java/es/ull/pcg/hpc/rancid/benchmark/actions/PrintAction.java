package es.ull.pcg.hpc.rancid.benchmark.actions;

import es.ull.pcg.hpc.rancid.OutputPrinter;
import es.ull.pcg.hpc.rancid.BenchmarkAction;
import es.ull.pcg.hpc.rancid.utils.ScopeBundle;
import java9.util.function.Function;

/**
 * An action that prints a {@link String} when executed. The output can be produced at runtime.
 *
 * @param <T> The type of the object used as scope.
 *
 * @see ScopeBundle
 */
public class PrintAction<T> implements BenchmarkAction {
    private final OutputPrinter mPrinter;
    private final T mScope;
    private final Function<T, String> mProducer;

    public PrintAction (OutputPrinter printer, T scope, Function<T, String> producer) {
        this.mPrinter = printer;
        this.mScope = scope;
        this.mProducer = producer;
    }

    public PrintAction (OutputPrinter printer, final String message) {
        this(printer, null, new Function<T, String>() {
            @Override
            public String apply (T t) {
                return message;
            }
        });
    }

    @Override
    public void execute () {
        mPrinter.println(mProducer.apply(mScope));
    }
}
