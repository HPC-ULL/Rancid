package es.ull.pcg.hpc.rancid.benchmark.actions;

import es.ull.pcg.hpc.rancid.utils.ScopeBundle;
import es.ull.pcg.hpc.rancid.utils.FileUtils;
import es.ull.pcg.hpc.rancid.utils.ShellUtils;
import es.ull.pcg.hpc.rancid.BenchmarkAction;
import java9.util.function.Function;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * An action that writes a {@link String} to file when executed. The output can be produced at runtime.
 *
 * @param <T> The type of the object used as scope.
 *
 * @see ScopeBundle
 */
public class FileWriteAction<T> implements BenchmarkAction {
    private final String mPath;
    private final boolean mRoot;
    private final T mScope;
    private final Function<T, String> mProducer;

    public FileWriteAction (String path, boolean root, T scope, Function<T, String> producer) {
        this.mPath = path;
        this.mRoot = root;
        this.mScope = scope;
        this.mProducer = producer;
    }

    public FileWriteAction (String path, boolean root, final String value) {
        // this(path, root, null, v -> value);
        this(path, root, null, new Function<T, String>() {
            @Override
            public String apply (T t) {
                return value;
            }
        });
    }

    @Override
    public void execute () {
        String value = mProducer.apply(mScope);

        try {
            if (mRoot) {
                List<String> cmd = Collections.singletonList("echo '" + value + "' > '" + mPath + "'");
                ShellUtils.rootExec(cmd);
            }
            else {
                FileUtils.writeFile(mPath, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
