package org.apache.poi.ss.formula.functions;

import java.util.Arrays;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/StatsLib.class */
final class StatsLib {
    private StatsLib() {
    }

    public static double avedev(double[] v) {
        double s = 0.0d;
        for (double d : v) {
            s += d;
        }
        double m = s / v.length;
        double s2 = 0.0d;
        for (double d2 : v) {
            s2 += Math.abs(d2 - m);
        }
        double r = s2 / v.length;
        return r;
    }

    public static double stdev(double[] v) {
        double r = Double.NaN;
        if (v != null && v.length > 1) {
            r = Math.sqrt(devsq(v) / (v.length - 1));
        }
        return r;
    }

    public static double var(double[] v) {
        double r = Double.NaN;
        if (v != null && v.length > 1) {
            r = devsq(v) / (v.length - 1);
        }
        return r;
    }

    public static double varp(double[] v) {
        double r = Double.NaN;
        if (v != null && v.length > 1) {
            r = devsq(v) / v.length;
        }
        return r;
    }

    public static double median(double[] v) {
        double r = Double.NaN;
        if (v != null && v.length >= 1) {
            int n = v.length;
            Arrays.sort(v);
            r = n % 2 == 0 ? (v[n / 2] + v[(n / 2) - 1]) / 2.0d : v[n / 2];
        }
        return r;
    }

    public static double devsq(double[] v) {
        double r = Double.NaN;
        if (v != null && v.length >= 1) {
            double s = 0.0d;
            int n = v.length;
            for (double d : v) {
                s += d;
            }
            double m = s / n;
            double s2 = 0.0d;
            for (int i = 0; i < n; i++) {
                s2 += (v[i] - m) * (v[i] - m);
            }
            r = n == 1 ? 0.0d : s2;
        }
        return r;
    }

    public static double kthLargest(double[] v, int k) {
        double r = Double.NaN;
        int index = k - 1;
        if (v != null && v.length > index && index >= 0) {
            Arrays.sort(v);
            r = v[(v.length - index) - 1];
        }
        return r;
    }

    public static double kthSmallest(double[] v, int k) {
        double r = Double.NaN;
        int index = k - 1;
        if (v != null && v.length > index && index >= 0) {
            Arrays.sort(v);
            r = v[index];
        }
        return r;
    }
}
