package es.ull.pcg.hpc.benchmark.utils;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Set of math method utilities.
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
        double value = 0.0;

        if (n > 1) {
            for (Results res: result)
                value += Math.pow(((ValueResult) res).doubleValue() - avg, 2);
        }

        return value / (n - 1);
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

    /**
     * Compare two {@link Number}.
     *
     * @param a First number.
     * @param b Second number.
     * @param <T> Type of the parameters.
     *
     * @return the value {@code 0} if {@code a == b}; a value less than {@code 0} if {@code a < b}; and a
     * value greater than {@code 0} if {@code a > b}
     */
    public static <T extends Number> int compare (T a, T b) {
        if (a instanceof Long || a instanceof Integer)
            return Long.compare(a.longValue(), b.longValue());
        else
            return Double.compare(a.doubleValue(), b.doubleValue());
    }
}
