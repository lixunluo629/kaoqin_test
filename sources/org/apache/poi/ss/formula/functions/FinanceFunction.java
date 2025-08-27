package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/FinanceFunction.class */
public abstract class FinanceFunction implements Function3Arg, Function4Arg {
    private static final ValueEval DEFAULT_ARG3 = NumberEval.ZERO;
    private static final ValueEval DEFAULT_ARG4 = BoolEval.FALSE;
    public static final Function FV = new FinanceFunction() { // from class: org.apache.poi.ss.formula.functions.FinanceFunction.1
        @Override // org.apache.poi.ss.formula.functions.FinanceFunction
        protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.fv(rate, arg1, arg2, arg3, type);
        }
    };
    public static final Function NPER = new FinanceFunction() { // from class: org.apache.poi.ss.formula.functions.FinanceFunction.2
        @Override // org.apache.poi.ss.formula.functions.FinanceFunction
        protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.nper(rate, arg1, arg2, arg3, type);
        }
    };
    public static final Function PMT = new FinanceFunction() { // from class: org.apache.poi.ss.formula.functions.FinanceFunction.3
        @Override // org.apache.poi.ss.formula.functions.FinanceFunction
        protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.pmt(rate, arg1, arg2, arg3, type);
        }
    };
    public static final Function PV = new FinanceFunction() { // from class: org.apache.poi.ss.formula.functions.FinanceFunction.4
        @Override // org.apache.poi.ss.formula.functions.FinanceFunction
        protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.pv(rate, arg1, arg2, arg3, type);
        }
    };

    protected abstract double evaluate(double d, double d2, double d3, double d4, boolean z) throws EvaluationException;

    protected FinanceFunction() {
    }

    @Override // org.apache.poi.ss.formula.functions.Function3Arg
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, DEFAULT_ARG3);
    }

    @Override // org.apache.poi.ss.formula.functions.Function4Arg
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3) {
        return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, arg3, DEFAULT_ARG4);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3, ValueEval arg4) {
        try {
            double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
            double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
            double d2 = NumericFunction.singleOperandEvaluate(arg2, srcRowIndex, srcColumnIndex);
            double d3 = NumericFunction.singleOperandEvaluate(arg3, srcRowIndex, srcColumnIndex);
            double d4 = NumericFunction.singleOperandEvaluate(arg4, srcRowIndex, srcColumnIndex);
            double result = evaluate(d0, d1, d2, d3, d4 != 0.0d);
            NumericFunction.checkValue(result);
            return new NumberEval(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    @Override // org.apache.poi.ss.formula.functions.Function
    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        switch (args.length) {
            case 3:
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], DEFAULT_ARG3, DEFAULT_ARG4);
            case 4:
                ValueEval arg3 = args[3];
                if (arg3 == MissingArgEval.instance) {
                    arg3 = DEFAULT_ARG3;
                }
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], arg3, DEFAULT_ARG4);
            case 5:
                ValueEval arg32 = args[3];
                if (arg32 == MissingArgEval.instance) {
                    arg32 = DEFAULT_ARG3;
                }
                ValueEval arg4 = args[4];
                if (arg4 == MissingArgEval.instance) {
                    arg4 = DEFAULT_ARG4;
                }
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], arg32, arg4);
            default:
                return ErrorEval.VALUE_INVALID;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x004c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected double evaluate(double[] r15) throws org.apache.poi.ss.formula.eval.EvaluationException {
        /*
            r14 = this;
            r0 = 0
            r16 = r0
            r0 = 0
            r18 = r0
            r0 = r15
            int r0 = r0.length
            switch(r0) {
                case 3: goto L29;
                case 4: goto L25;
                case 5: goto L20;
                default: goto L2c;
            }
        L20:
            r0 = r15
            r1 = 4
            r0 = r0[r1]
            r18 = r0
        L25:
            r0 = r15
            r1 = 3
            r0 = r0[r1]
            r16 = r0
        L29:
            goto L36
        L2c:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            r1 = r0
            java.lang.String r2 = "Wrong number of arguments"
            r1.<init>(r2)
            throw r0
        L36:
            r0 = r14
            r1 = r15
            r2 = 0
            r1 = r1[r2]
            r2 = r15
            r3 = 1
            r2 = r2[r3]
            r3 = r15
            r4 = 2
            r3 = r3[r4]
            r4 = r16
            r5 = r18
            r6 = 0
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 == 0) goto L4c
            r5 = 1
            goto L4d
        L4c:
            r5 = 0
        L4d:
            double r0 = r0.evaluate(r1, r2, r3, r4, r5)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ss.formula.functions.FinanceFunction.evaluate(double[]):double");
    }
}
