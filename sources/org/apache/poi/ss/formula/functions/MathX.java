package org.apache.poi.ss.formula.functions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.poi.ss.util.NumberToTextConverter;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/MathX.class */
final class MathX {
    private MathX() {
    }

    public static double round(double n, int p) {
        return round(n, p, RoundingMode.HALF_UP);
    }

    public static double roundUp(double n, int p) {
        return round(n, p, RoundingMode.UP);
    }

    public static double roundDown(double n, int p) {
        return round(n, p, RoundingMode.DOWN);
    }

    private static double round(double n, int p, RoundingMode rounding) {
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            return Double.NaN;
        }
        String excelNumber = NumberToTextConverter.toText(n);
        return new BigDecimal(excelNumber).setScale(p, rounding).doubleValue();
    }

    public static short sign(double d) {
        return (short) (d == 0.0d ? 0 : d < 0.0d ? -1 : 1);
    }

    public static double average(double[] values) {
        double sum = 0.0d;
        for (double d : values) {
            sum += d;
        }
        double ave = sum / values.length;
        return ave;
    }

    public static double sum(double[] values) {
        double sum = 0.0d;
        for (double d : values) {
            sum += d;
        }
        return sum;
    }

    public static double sumsq(double[] values) {
        double sumsq = 0.0d;
        int iSize = values.length;
        for (int i = 0; i < iSize; i++) {
            sumsq += values[i] * values[i];
        }
        return sumsq;
    }

    public static double product(double[] values) {
        double product = 0.0d;
        if (values != null && values.length > 0) {
            product = 1.0d;
            for (double d : values) {
                product *= d;
            }
        }
        return product;
    }

    public static double min(double[] values) {
        double min = Double.POSITIVE_INFINITY;
        for (double d : values) {
            min = Math.min(min, d);
        }
        return min;
    }

    public static double max(double[] values) {
        double max = Double.NEGATIVE_INFINITY;
        for (double d : values) {
            max = Math.max(max, d);
        }
        return max;
    }

    public static double floor(double n, double s) {
        double f;
        if ((n >= 0.0d || s <= 0.0d) && ((n <= 0.0d || s >= 0.0d) && (s != 0.0d || n == 0.0d))) {
            f = (n == 0.0d || s == 0.0d) ? 0.0d : Math.floor(n / s) * s;
        } else {
            f = Double.NaN;
        }
        return f;
    }

    public static double ceiling(double n, double s) {
        double c;
        if ((n >= 0.0d || s <= 0.0d) && (n <= 0.0d || s >= 0.0d)) {
            c = (n == 0.0d || s == 0.0d) ? 0.0d : Math.ceil(n / s) * s;
        } else {
            c = Double.NaN;
        }
        return c;
    }

    public static double factorial(int n) {
        double d = 1.0d;
        if (n >= 0) {
            if (n <= 170) {
                for (int i = 1; i <= n; i++) {
                    d *= i;
                }
            } else {
                d = Double.POSITIVE_INFINITY;
            }
        } else {
            d = Double.NaN;
        }
        return d;
    }

    public static double mod(double n, double d) {
        double result;
        if (d == 0.0d) {
            result = Double.NaN;
        } else if (sign(n) == sign(d)) {
            result = n % d;
        } else {
            result = ((n % d) + d) % d;
        }
        return result;
    }

    public static double acosh(double d) {
        return Math.log(Math.sqrt(Math.pow(d, 2.0d) - 1.0d) + d);
    }

    public static double asinh(double d) {
        return Math.log(Math.sqrt((d * d) + 1.0d) + d);
    }

    public static double atanh(double d) {
        return Math.log((1.0d + d) / (1.0d - d)) / 2.0d;
    }

    public static double cosh(double d) {
        double ePowX = Math.pow(2.718281828459045d, d);
        double ePowNegX = Math.pow(2.718281828459045d, -d);
        return (ePowX + ePowNegX) / 2.0d;
    }

    public static double sinh(double d) {
        double ePowX = Math.pow(2.718281828459045d, d);
        double ePowNegX = Math.pow(2.718281828459045d, -d);
        return (ePowX - ePowNegX) / 2.0d;
    }

    public static double tanh(double d) {
        double ePowX = Math.pow(2.718281828459045d, d);
        double ePowNegX = Math.pow(2.718281828459045d, -d);
        return (ePowX - ePowNegX) / (ePowX + ePowNegX);
    }

    public static double nChooseK(int n, int k) {
        double d;
        double d2 = 1.0d;
        if (n < 0 || k < 0 || n < k) {
            d = Double.NaN;
        } else {
            int minnk = Math.min(n - k, k);
            int maxnk = Math.max(n - k, k);
            for (int i = maxnk; i < n; i++) {
                d2 *= i + 1;
            }
            d = d2 / factorial(minnk);
        }
        return d;
    }
}
