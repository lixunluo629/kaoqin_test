package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Rate.class */
public class Rate implements Function {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) Rate.class);

    @Override // org.apache.poi.ss.formula.functions.Function
    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length < 3) {
            return ErrorEval.VALUE_INVALID;
        }
        double future_val = 0.0d;
        double type = 0.0d;
        double estimate = 0.1d;
        try {
            ValueEval v1 = OperandResolver.getSingleValue(args[0], srcRowIndex, srcColumnIndex);
            ValueEval v2 = OperandResolver.getSingleValue(args[1], srcRowIndex, srcColumnIndex);
            ValueEval v3 = OperandResolver.getSingleValue(args[2], srcRowIndex, srcColumnIndex);
            ValueEval v4 = null;
            if (args.length >= 4) {
                v4 = OperandResolver.getSingleValue(args[3], srcRowIndex, srcColumnIndex);
            }
            ValueEval v5 = null;
            if (args.length >= 5) {
                v5 = OperandResolver.getSingleValue(args[4], srcRowIndex, srcColumnIndex);
            }
            ValueEval v6 = null;
            if (args.length >= 6) {
                v6 = OperandResolver.getSingleValue(args[5], srcRowIndex, srcColumnIndex);
            }
            double periods = OperandResolver.coerceValueToDouble(v1);
            double payment = OperandResolver.coerceValueToDouble(v2);
            double present_val = OperandResolver.coerceValueToDouble(v3);
            if (args.length >= 4) {
                future_val = OperandResolver.coerceValueToDouble(v4);
            }
            if (args.length >= 5) {
                type = OperandResolver.coerceValueToDouble(v5);
            }
            if (args.length >= 6) {
                estimate = OperandResolver.coerceValueToDouble(v6);
            }
            double rate = calculateRate(periods, payment, present_val, future_val, type, estimate);
            checkValue(rate);
            return new NumberEval(rate);
        } catch (EvaluationException e) {
            LOG.log(7, "Can't evaluate rate function", e);
            return e.getErrorEval();
        }
    }

    private double calculateRate(double nper, double pmt, double pv, double fv, double type, double guess) {
        double d;
        double d2;
        double d3;
        double f = 0.0d;
        double rate = guess;
        if (Math.abs(rate) < 1.0E-7d) {
            double d4 = (pv * (1.0d + (nper * rate))) + (pmt * (1.0d + (rate * type)) * nper) + fv;
        } else {
            f = Math.exp(nper * Math.log(1.0d + rate));
            double d5 = (pv * f) + (pmt * ((1.0d / rate) + type) * (f - 1.0d)) + fv;
        }
        double y0 = pv + (pmt * nper) + fv;
        double y1 = (pv * f) + (pmt * ((1.0d / rate) + type) * (f - 1.0d)) + fv;
        double x0 = 0.0d;
        double x1 = rate;
        for (double i = 0.0d; Math.abs(y0 - y1) > 1.0E-7d && i < 20; i += 1.0d) {
            rate = ((y1 * x0) - (y0 * x1)) / (y1 - y0);
            x0 = x1;
            x1 = rate;
            if (Math.abs(rate) < 1.0E-7d) {
                d = pv * (1.0d + (nper * rate));
                d2 = pmt * (1.0d + (rate * type));
                d3 = nper;
            } else {
                double f2 = Math.exp(nper * Math.log(1.0d + rate));
                d = pv * f2;
                d2 = pmt * ((1.0d / rate) + type);
                d3 = f2 - 1.0d;
            }
            double y = d + (d2 * d3) + fv;
            y0 = y1;
            y1 = y;
        }
        return rate;
    }

    static final void checkValue(double result) throws EvaluationException {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new EvaluationException(ErrorEval.NUM_ERROR);
        }
    }
}
