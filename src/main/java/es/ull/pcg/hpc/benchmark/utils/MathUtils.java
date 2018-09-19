package es.ull.pcg.hpc.benchmark.utils;

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
        return result.stream().map(results -> ((ValueResult) results).doubleValue())
                     .reduce(Double.MAX_VALUE, Math::min);
    }

    /**
     * Calculate the maximum value.
     *
     * @param result List of values to process.
     * @return The maximum value.
     */
    public static double max (ListResult result) {
        return result.stream().map(results -> ((ValueResult) results).doubleValue())
                     .reduce(Double.MIN_VALUE, Math::max);
    }

    /**
     * Calculate the sum of all values.
     *
     * @param result List of values to process.
     * @return The sum of all values.
     */
    public static double sum (ListResult result) {
        return result.stream()
                     .map(results -> ((ValueResult) results).doubleValue())
                     .reduce(0.0, (x, y) -> x + y);
    }

    /**
     * Calculate the average of the values.
     *
     * @param result List of values to process.
     * @return The average.
     */
    public static double average (ListResult result) {
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
        final int n = result.size();

        if (n <= 1)
            return 0.0;

        final double avg = average(result);
        return sampleVariance(avg, result);
    }

    /**
     * Calculate the sample variance of the values.
     *
     * @param avg The average of the values.
     * @param result List of values to process.
     * @return The sample variance.
     */
    public static double sampleVariance (double avg, ListResult result) {
        final int n = result.size();

        if (n <= 1)
            return 0.0;

        return result.stream()
                     .map(results -> Math.pow(((ValueResult) results).doubleValue() - avg, 2))
                     .reduce(0.0, (x, y) -> x + y) / (n - 1);
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
}
