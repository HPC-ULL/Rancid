package es.ull.pcg.hpc.benchmark.benchmark.actions;

import es.ull.pcg.hpc.benchmark.utils.FileUtils;
import es.ull.pcg.hpc.benchmark.utils.MathUtils;
import es.ull.pcg.hpc.benchmark.utils.ThreadUtils;
import es.ull.pcg.hpc.benchmark.BenchmarkAction;

/**
 * An action that reduces device temperature by putting the current thread to sleep if a certain threshold
 * is exceeded.
 */
public class CoolDownAction implements BenchmarkAction {
    private static final int WAIT_DURATION_MS = 1000;

    private final int mThreshold;
    private final int mBase;
    private String mThermalPath;

    public CoolDownAction (String thermalPath, int thresholdTemp, int baseTemp) {
        this.mThermalPath = thermalPath;
        this.mThreshold = thresholdTemp;
        this.mBase = baseTemp;
    }

    public void setThermalPath (String path) {
        this.mThermalPath = path;
    }

    @Override
    public void execute () {
        // Read current frequency and abort cool-down if the sensor could not be read
        Number value = FileUtils.readNumberFile(mThermalPath);
        if (Double.isNaN(value.doubleValue()))
            return;

        boolean shouldReduce = MathUtils.compare(value, mThreshold) > 0;

        if (shouldReduce) {
            int compareReset;
            do {
                ThreadUtils.waitMs(WAIT_DURATION_MS);
                value = FileUtils.readNumberFile(mThermalPath);
                compareReset = MathUtils.compare(value, mBase);
            } while (compareReset > 0);
        }
    }
}
