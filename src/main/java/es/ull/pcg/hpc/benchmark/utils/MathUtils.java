package es.ull.pcg.hpc.benchmark.utils;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Set of math methods working on {@link ListResult}.
 */
public class MathUtils {
    /**
     * Calculate the minimum value.
     *
     * @param result List of values to process.
     * @return The minimum value.
     */
    public static double min (ListResult result) {
        double value = Double.MAX_VALUE;

        for (Results res: result) {
            double resultValue = ((ValueResult) res).doubleValue();
            if (resultValue < value)
                value = resultValue;
        }

        return value;
    }

    /**
     * Calculate the maximum value.
     *
     * @param result List of values to process.
     * @return The maximum value.
     */
    public static double max (ListResult result) {
        double value = Double.MIN_VALUE;

        for (Results res: result) {
            double resultValue = ((ValueResult) res).doubleValue();
            if (resultValue > value)
                value = resultValue;
        }

        return value;
    }

    /**
     * Calculate the sum of all values.
     *
     * @param result List of values to process.
     * @return The sum of all values.
     */
    public static double sum (ListResult result) {
        double value = 0.0;

        for (Results res: result)
            value += ((ValueResult) res).doubleValue();

        return value;
    }

    /**
     * Calculate the arithmetic average of the values.
     *
     * @param result List of values to process.
     * @return The average.
     */
    public static double arithmeticAvg (ListResult result) {
        final int n = result.size();
        return n > 0? sum(result) / n : 0.0;
    }

    /**
     * Calculate the sample variance of the values.
     *
     * @param result List of values to process.
     * @return The sample variance.
     */
    public static double sampleVariance (ListResult result) {
        final double avg = arithmeticAvg(result);
        return sampleVariance(result, avg);
    }

    /**
     * Calculate the sample variance of the values.
     *
     * @param result List of values to process.
     * @param avg The arithmetic average of the values.
     * @return The sample variance.
     */
    public static double sampleVariance (ListResult result, double avg) {
        final int n = result.size();

        if (n > 1) {
            double value = 0.0;

            for (Results res: result)
                value += Math.pow(((ValueResult) res).doubleValue() - avg, 2);

            return value / (n - 1);
        }

        return 0.0;
    }

    /**
     * Calculate the sample standard deviation of the values.
     *
     * @param result List of values to process.
     * @return The sample standard deviation.
     */
    public static double sampleStdDev (ListResult result) {
        final int n = result.size();
        return n > 1? Math.sqrt(sampleVariance(result)) : 0.0;
    }

    public static int[] histogram (ListResult result, int bins) {
        return histogram(result, min(result), max(result), bins);
    }

    public static int[] histogram (ListResult result, double min, double max, int bins) {
        final int n = result.size();
        final double interval = max - min;

        if (n == 0 || interval <= 0.0 || bins == 0)
            return new int[bins];

        int[] hist = new int[bins];
        double step = interval / bins;

        for (Results res: result) {
            double value = ((ValueResult) res).doubleValue();
            int idx = (int)((value - min) / step);

            if (idx < 0) idx = 0;
            else if (idx >= bins) idx = bins - 1;

            ++hist[idx];
        }

        return hist;
    }
}
