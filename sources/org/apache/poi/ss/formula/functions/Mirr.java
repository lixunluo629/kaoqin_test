package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Mirr.class */
public class Mirr extends MultiOperandNumericFunction {
    public Mirr() {
        super(false, false);
    }

    @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
    protected int getMaxNumOperands() {
        return 3;
    }

    @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
    protected double evaluate(double[] values) throws EvaluationException {
        double financeRate = values[values.length - 1];
        double reinvestRate = values[values.length - 2];
        double[] mirrValues = new double[values.length - 2];
        System.arraycopy(values, 0, mirrValues, 0, mirrValues.length);
        boolean mirrValuesAreAllNegatives = true;
        int len$ = mirrValues.length;
        for (int i$ = 0; i$ < len$; i$++) {
            double mirrValue = mirrValues[i$];
            mirrValuesAreAllNegatives &= mirrValue < 0.0d;
        }
        if (mirrValuesAreAllNegatives) {
            return -1.0d;
        }
        boolean mirrValuesAreAllPositives = true;
        int len$2 = mirrValues.length;
        for (int i$2 = 0; i$2 < len$2; i$2++) {
            double mirrValue2 = mirrValues[i$2];
            mirrValuesAreAllPositives &= mirrValue2 > 0.0d;
        }
        if (mirrValuesAreAllPositives) {
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
        return mirr(mirrValues, financeRate, reinvestRate);
    }

    private static double mirr(double[] in, double financeRate, double reinvestRate) {
        double value = 0.0d;
        int numOfYears = in.length - 1;
        double pv = 0.0d;
        double fv = 0.0d;
        int indexN = 0;
        for (double anIn : in) {
            if (anIn < 0.0d) {
                int i = indexN;
                indexN++;
                pv += anIn / Math.pow((1.0d + financeRate) + reinvestRate, i);
            }
        }
        for (double anIn2 : in) {
            if (anIn2 > 0.0d) {
                int i2 = indexN;
                indexN++;
                fv += anIn2 * Math.pow(1.0d + financeRate, numOfYears - i2);
            }
        }
        if (fv != 0.0d && pv != 0.0d) {
            value = Math.pow((-fv) / pv, 1.0d / numOfYears) - 1.0d;
        }
        return value;
    }
}
