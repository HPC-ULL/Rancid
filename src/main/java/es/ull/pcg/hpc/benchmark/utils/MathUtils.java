package es.ull.pcg.hpc.benchmark.utils;

import es.ull.pcg.hpc.benchmark.Results;
import es.ull.pcg.hpc.benchmark.results.ListResult;
import es.ull.pcg.hpc.benchmark.results.ValueResult;

/**
 * Set of math method utilities.
 */
public class MathUtils {
    /**
     * Check if a number is floating point.
     *
     * @param number The number to check.
     * @return Whether if the number is {@code float} or {@code double}.
     */
    public static boolean isFloatNumber (Number number) {
        return number instanceof Float || number instanceof Double;
    }

    /**
     * Compare two numbers.
     *
     * @param a First number.
     * @param b Second number.
     * @return {@code -1} if {@code a < b}, {@code 1} if {@code b < a}, and {@code 0} otherwise.
     */
    public static int compare (Number a, Number b) {
        boolean isFloat = isFloatNumber(a) || isFloatNumber(b);
        return isFloat? Double.compare(a.doubleValue(), b.doubleValue()) : Long.compare(a.longValue(), b.longValue());
    }

    /**
     * Calculate the minimum value.
     *
     * @param result List of values to process.
     * @return The minimum value.
     */
    public static Number min (ListResult result) {
        Number minValue = null;

        for (Results res: result) {
            Number value = ((ValueResult) res);

            if (minValue == null || compare(value, minValue) < 0)
                minValue = value;
        }

        return minValue == null? Long.MAX_VALUE : minValue;
    }

    /**
     * Calculate the maximum value.
     *
     * @param result List of values to process.
     * @return The maximum value.
     */
    public static Number max (ListResult result) {
        Number maxValue = null;

        for (Results res: result) {
            Number value = ((ValueResult) res);

            if (maxValue == null || compare(value, maxValue) > 0)
                maxValue = value;
        }

        return maxValue == null? Long.MIN_VALUE : maxValue;
    }

    /**
     * Calculate the sum of all values.
     *
     * @param result List of values to process.
     * @return The sum of all values.
     */
    public static Number sum (ListResult result) {
        Number sumValue = 0;

        if (!result.isEmpty()) {
            boolean isFloat = isFloatNumber((ValueResult) result.get(0));

            for (Results res : result) {
                Number value = ((ValueResult) res);
                sumValue = isFloat? sumValue.doubleValue() + value.doubleValue() :
                                    sumValue.longValue() + value.longValue();
            }
        }

        return sumValue;
    }

    /**
     * Calculate the arithmetic average of the values.
     *
     * @param result List of values to process.
     * @return The average.
     */
    public static double arithmeticAvg (ListResult result) {
        final int n = result.size();
        return n > 0? sum(result).doubleValue() / n : 0.0;
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

    public static int[] histogram (ListResult result, Number min, Number max, int bins) {
        final int n = result.size();
        final double interval = max.doubleValue() - min.doubleValue();

        if (n == 0 || interval <= 0.0 || bins == 0)
            return new int[bins];

        int[] hist = new int[bins];
        double step = interval / bins;

        for (Results res: result) {
            double value = ((ValueResult) res).doubleValue();
            int idx = (int)((value - min.doubleValue()) / step);

            if (idx < 0) idx = 0;
            else if (idx >= bins) idx = bins - 1;

            ++hist[idx];
        }

        return hist;
    }
}
