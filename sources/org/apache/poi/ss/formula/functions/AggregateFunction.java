package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/AggregateFunction.class */
public abstract class AggregateFunction extends MultiOperandNumericFunction {
    public static final Function AVEDEV = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.2
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) {
            return StatsLib.avedev(values);
        }
    };
    public static final Function AVERAGE = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.3
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length < 1) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return MathX.average(values);
        }
    };
    public static final Function DEVSQ = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.4
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) {
            return StatsLib.devsq(values);
        }
    };
    public static final Function LARGE = new LargeSmall(true);
    public static final Function MAX = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.5
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) {
            if (values.length > 0) {
                return MathX.max(values);
            }
            return 0.0d;
        }
    };
    public static final Function MEDIAN = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.6
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) {
            return StatsLib.median(values);
        }
    };
    public static final Function MIN = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.7
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) {
            if (values.length > 0) {
                return MathX.min(values);
            }
            return 0.0d;
        }
    };
    public static final Function PERCENTILE = new Percentile();
    public static final Function PRODUCT = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.8
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) {
            return MathX.product(values);
        }
    };
    public static final Function SMALL = new LargeSmall(false);
    public static final Function STDEV = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.9
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length < 1) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return StatsLib.stdev(values);
        }
    };
    public static final Function SUM = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.10
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) {
            return MathX.sum(values);
        }
    };
    public static final Function SUMSQ = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.11
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) {
            return MathX.sumsq(values);
        }
    };
    public static final Function VAR = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.12
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length < 1) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return StatsLib.var(values);
        }
    };
    public static final Function VARP = new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.13
        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length < 1) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return StatsLib.varp(values);
        }
    };

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/AggregateFunction$LargeSmall.class */
    private static final class LargeSmall extends Fixed2ArgFunction {
        private final boolean _isLarge;

        protected LargeSmall(boolean isLarge) {
            this._isLarge = isLarge;
        }

        @Override // org.apache.poi.ss.formula.functions.Function2Arg
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            try {
                ValueEval ve1 = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
                double dn = OperandResolver.coerceValueToDouble(ve1);
                if (dn < 1.0d) {
                    return ErrorEval.NUM_ERROR;
                }
                int k = (int) Math.ceil(dn);
                try {
                    double[] ds = ValueCollector.collectValues(arg0);
                    if (k > ds.length) {
                        return ErrorEval.NUM_ERROR;
                    }
                    double result = this._isLarge ? StatsLib.kthLargest(ds, k) : StatsLib.kthSmallest(ds, k);
                    NumericFunction.checkValue(result);
                    return new NumberEval(result);
                } catch (EvaluationException e) {
                    return e.getErrorEval();
                }
            } catch (EvaluationException e2) {
                return ErrorEval.VALUE_INVALID;
            }
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/AggregateFunction$Percentile.class */
    private static final class Percentile extends Fixed2ArgFunction {
        protected Percentile() {
        }

        @Override // org.apache.poi.ss.formula.functions.Function2Arg
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            double result;
            try {
                ValueEval ve1 = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
                double dn = OperandResolver.coerceValueToDouble(ve1);
                if (dn < 0.0d || dn > 1.0d) {
                    return ErrorEval.NUM_ERROR;
                }
                try {
                    double[] ds = ValueCollector.collectValues(arg0);
                    int N = ds.length;
                    if (N == 0 || N > 8191) {
                        return ErrorEval.NUM_ERROR;
                    }
                    double n = ((N - 1) * dn) + 1.0d;
                    if (n == 1.0d) {
                        result = StatsLib.kthSmallest(ds, 1);
                    } else if (Double.compare(n, N) == 0) {
                        result = StatsLib.kthLargest(ds, 1);
                    } else {
                        int k = (int) n;
                        double d = n - k;
                        result = StatsLib.kthSmallest(ds, k) + (d * (StatsLib.kthSmallest(ds, k + 1) - StatsLib.kthSmallest(ds, k)));
                    }
                    NumericFunction.checkValue(result);
                    return new NumberEval(result);
                } catch (EvaluationException e) {
                    return e.getErrorEval();
                }
            } catch (EvaluationException e2) {
                return ErrorEval.VALUE_INVALID;
            }
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/AggregateFunction$ValueCollector.class */
    static final class ValueCollector extends MultiOperandNumericFunction {
        private static final ValueCollector instance = new ValueCollector();

        public ValueCollector() {
            super(false, false);
        }

        public static double[] collectValues(ValueEval... operands) throws EvaluationException {
            return instance.getNumberArray(operands);
        }

        @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
        protected double evaluate(double[] values) {
            throw new IllegalStateException("should not be called");
        }
    }

    protected AggregateFunction() {
        super(false, false);
    }

    static Function subtotalInstance(Function func) {
        AggregateFunction arg = (AggregateFunction) func;
        return new AggregateFunction() { // from class: org.apache.poi.ss.formula.functions.AggregateFunction.1
            @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
            protected double evaluate(double[] values) throws EvaluationException {
                return AggregateFunction.this.evaluate(values);
            }

            @Override // org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
            public boolean isSubtotalCounted() {
                return false;
            }
        };
    }
}
