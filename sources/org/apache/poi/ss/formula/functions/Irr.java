package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.AggregateFunction;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Irr.class */
public final class Irr implements Function {
    @Override // org.apache.poi.ss.formula.functions.Function
    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        double guess;
        if (args.length == 0 || args.length > 2) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            double[] values = AggregateFunction.ValueCollector.collectValues(args[0]);
            if (args.length == 2) {
                guess = NumericFunction.singleOperandEvaluate(args[1], srcRowIndex, srcColumnIndex);
            } else {
                guess = 0.1d;
            }
            double result = irr(values, guess);
            NumericFunction.checkValue(result);
            return new NumberEval(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    public static double irr(double[] income) {
        return irr(income, 0.1d);
    }

    public static double irr(double[] values, double guess) {
        double x0 = guess;
        for (int i = 0; i < 20; i++) {
            double factor = 1.0d + x0;
            int k = 0;
            double fValue = values[0];
            double fDerivative = 0.0d;
            double denominator = factor;
            while (true) {
                k++;
                if (k >= values.length) {
                    break;
                }
                double value = values[k];
                fValue += value / denominator;
                denominator *= factor;
                fDerivative -= (k * value) / denominator;
            }
            double x1 = x0 - (fValue / fDerivative);
            if (Math.abs(x1 - x0) <= 1.0E-7d) {
                return x1;
            }
            x0 = x1;
        }
        return Double.NaN;
    }
}
