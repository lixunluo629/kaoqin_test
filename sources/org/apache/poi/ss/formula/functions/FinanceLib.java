package org.apache.poi.ss.formula.functions;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/FinanceLib.class */
public final class FinanceLib {
    private FinanceLib() {
    }

    public static double fv(double r, double n, double y, double p, boolean t) {
        double retval;
        if (r == 0.0d) {
            retval = (-1.0d) * (p + (n * y));
        } else {
            double r1 = r + 1.0d;
            retval = ((((1.0d - Math.pow(r1, n)) * (t ? r1 : 1.0d)) * y) / r) - (p * Math.pow(r1, n));
        }
        return retval;
    }

    public static double pv(double r, double n, double y, double f, boolean t) {
        double retval;
        if (r == 0.0d) {
            retval = (-1.0d) * ((n * y) + f);
        } else {
            double r1 = r + 1.0d;
            retval = (((((1.0d - Math.pow(r1, n)) / r) * (t ? r1 : 1.0d)) * y) - f) / Math.pow(r1, n);
        }
        return retval;
    }

    public static double npv(double r, double[] cfs) {
        double npv = 0.0d;
        double r1 = r + 1.0d;
        double trate = r1;
        for (double d : cfs) {
            npv += d / trate;
            trate *= r1;
        }
        return npv;
    }

    public static double pmt(double r, double n, double p, double f, boolean t) {
        double retval;
        if (r == 0.0d) {
            retval = ((-1.0d) * (f + p)) / n;
        } else {
            double r1 = r + 1.0d;
            retval = ((f + (p * Math.pow(r1, n))) * r) / ((t ? r1 : 1.0d) * (1.0d - Math.pow(r1, n)));
        }
        return retval;
    }

    public static double nper(double r, double y, double p, double f, boolean t) {
        double retval;
        if (r == 0.0d) {
            retval = ((-1.0d) * (f + p)) / y;
        } else {
            double r1 = r + 1.0d;
            double ryr = ((t ? r1 : 1.0d) * y) / r;
            double a1 = ryr - f < 0.0d ? Math.log(f - ryr) : Math.log(ryr - f);
            double a2 = ryr - f < 0.0d ? Math.log((-p) - ryr) : Math.log(p + ryr);
            double a3 = Math.log(r1);
            retval = (a1 - a2) / a3;
        }
        return retval;
    }
}
