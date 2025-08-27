package org.apache.poi.ss.formula.functions;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.regex.Pattern;
import org.apache.poi.ss.formula.ThreeDEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.CountUtils;
import org.apache.poi.ss.usermodel.FormulaError;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Countif.class */
public final class Countif extends Fixed2ArgFunction {

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Countif$CmpOp.class */
    private static final class CmpOp {
        public static final int NONE = 0;
        public static final int EQ = 1;
        public static final int NE = 2;
        public static final int LE = 3;
        public static final int LT = 4;
        public static final int GT = 5;
        public static final int GE = 6;
        public static final CmpOp OP_NONE = op("", 0);
        public static final CmpOp OP_EQ = op(SymbolConstants.EQUAL_SYMBOL, 1);
        public static final CmpOp OP_NE = op("<>", 2);
        public static final CmpOp OP_LE = op("<=", 3);
        public static final CmpOp OP_LT = op("<", 4);
        public static final CmpOp OP_GT = op(">", 5);
        public static final CmpOp OP_GE = op(">=", 6);
        private final String _representation;
        private final int _code;

        private static CmpOp op(String rep, int code) {
            return new CmpOp(rep, code);
        }

        private CmpOp(String representation, int code) {
            this._representation = representation;
            this._code = code;
        }

        public int getLength() {
            return this._representation.length();
        }

        public int getCode() {
            return this._code;
        }

        public static CmpOp getOperator(String value) {
            int len = value.length();
            if (len < 1) {
                return OP_NONE;
            }
            char firstChar = value.charAt(0);
            switch (firstChar) {
                case '<':
                    if (len > 1) {
                        switch (value.charAt(1)) {
                            case '=':
                                return OP_LE;
                            case '>':
                                return OP_NE;
                        }
                    }
                    return OP_LT;
                case '=':
                    return OP_EQ;
                case '>':
                    if (len > 1) {
                        switch (value.charAt(1)) {
                            case '=':
                                return OP_GE;
                        }
                    }
                    return OP_GT;
                default:
                    return OP_NONE;
            }
        }

        public boolean evaluate(boolean cmpResult) {
            switch (this._code) {
                case 0:
                case 1:
                    return cmpResult;
                case 2:
                    return !cmpResult;
                default:
                    throw new RuntimeException("Cannot call boolean evaluate on non-equality operator '" + this._representation + "'");
            }
        }

        public boolean evaluate(int cmpResult) {
            switch (this._code) {
                case 0:
                case 1:
                    return cmpResult == 0;
                case 2:
                    return cmpResult != 0;
                case 3:
                    return cmpResult <= 0;
                case 4:
                    return cmpResult < 0;
                case 5:
                    return cmpResult > 0;
                case 6:
                    return cmpResult >= 0;
                default:
                    throw new RuntimeException("Cannot call boolean evaluate on non-equality operator '" + this._representation + "'");
            }
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName());
            sb.append(" [").append(this._representation).append("]");
            return sb.toString();
        }

        public String getRepresentation() {
            return this._representation;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Countif$MatcherBase.class */
    private static abstract class MatcherBase implements CountUtils.I_MatchPredicate {
        private final CmpOp _operator;

        protected abstract String getValueText();

        MatcherBase(CmpOp operator) {
            this._operator = operator;
        }

        protected final int getCode() {
            return this._operator.getCode();
        }

        protected final boolean evaluate(int cmpResult) {
            return this._operator.evaluate(cmpResult);
        }

        protected final boolean evaluate(boolean cmpResult) {
            return this._operator.evaluate(cmpResult);
        }

        public final String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName()).append(" [");
            sb.append(this._operator.getRepresentation());
            sb.append(getValueText());
            sb.append("]");
            return sb.toString();
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Countif$NumberMatcher.class */
    private static final class NumberMatcher extends MatcherBase {
        private final double _value;

        public NumberMatcher(double value, CmpOp operator) {
            super(operator);
            this._value = value;
        }

        @Override // org.apache.poi.ss.formula.functions.Countif.MatcherBase
        protected String getValueText() {
            return String.valueOf(this._value);
        }

        @Override // org.apache.poi.ss.formula.functions.CountUtils.I_MatchPredicate
        public boolean matches(ValueEval x) {
            if (x instanceof StringEval) {
                switch (getCode()) {
                    case 0:
                    case 1:
                        StringEval se = (StringEval) x;
                        Double val = OperandResolver.parseDouble(se.getStringValue());
                        return val != null && this._value == val.doubleValue();
                    case 2:
                        return true;
                    default:
                        return false;
                }
            }
            if (x instanceof NumberEval) {
                NumberEval ne = (NumberEval) x;
                double testValue = ne.getNumberValue();
                return evaluate(Double.compare(testValue, this._value));
            }
            if (x instanceof BlankEval) {
                switch (getCode()) {
                    case 2:
                        return true;
                    default:
                        return false;
                }
            }
            return false;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Countif$BooleanMatcher.class */
    private static final class BooleanMatcher extends MatcherBase {
        private final int _value;

        public BooleanMatcher(boolean value, CmpOp operator) {
            super(operator);
            this._value = boolToInt(value);
        }

        @Override // org.apache.poi.ss.formula.functions.Countif.MatcherBase
        protected String getValueText() {
            return this._value == 1 ? "TRUE" : "FALSE";
        }

        private static int boolToInt(boolean value) {
            return value ? 1 : 0;
        }

        @Override // org.apache.poi.ss.formula.functions.CountUtils.I_MatchPredicate
        public boolean matches(ValueEval x) {
            if (x instanceof StringEval) {
                return false;
            }
            if (x instanceof BoolEval) {
                BoolEval be = (BoolEval) x;
                int testValue = boolToInt(be.getBooleanValue());
                return evaluate(testValue - this._value);
            }
            if (x instanceof BlankEval) {
                switch (getCode()) {
                    case 2:
                        return true;
                    default:
                        return false;
                }
            }
            if (x instanceof NumberEval) {
                switch (getCode()) {
                    case 2:
                        return true;
                    default:
                        return false;
                }
            }
            return false;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Countif$ErrorMatcher.class */
    public static final class ErrorMatcher extends MatcherBase {
        private final int _value;

        public ErrorMatcher(int errorCode, CmpOp operator) {
            super(operator);
            this._value = errorCode;
        }

        @Override // org.apache.poi.ss.formula.functions.Countif.MatcherBase
        protected String getValueText() {
            return FormulaError.forInt(this._value).getString();
        }

        @Override // org.apache.poi.ss.formula.functions.CountUtils.I_MatchPredicate
        public boolean matches(ValueEval x) {
            if (x instanceof ErrorEval) {
                int testValue = ((ErrorEval) x).getErrorCode();
                return evaluate(testValue - this._value);
            }
            return false;
        }

        public int getValue() {
            return this._value;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Countif$StringMatcher.class */
    public static final class StringMatcher extends MatcherBase {
        private final String _value;
        private final Pattern _pattern;

        public StringMatcher(String value, CmpOp operator) {
            super(operator);
            this._value = value;
            switch (operator.getCode()) {
                case 0:
                case 1:
                case 2:
                    this._pattern = getWildCardPattern(value);
                    break;
                default:
                    this._pattern = null;
                    break;
            }
        }

        @Override // org.apache.poi.ss.formula.functions.Countif.MatcherBase
        protected String getValueText() {
            if (this._pattern == null) {
                return this._value;
            }
            return this._pattern.pattern();
        }

        @Override // org.apache.poi.ss.formula.functions.CountUtils.I_MatchPredicate
        public boolean matches(ValueEval x) {
            if (x instanceof BlankEval) {
                switch (getCode()) {
                    case 0:
                    case 1:
                        if (this._value.length() == 0) {
                        }
                        break;
                    case 2:
                        if (this._value.length() != 0) {
                        }
                        break;
                }
                return false;
            }
            if (!(x instanceof StringEval)) {
                return false;
            }
            String testedValue = ((StringEval) x).getStringValue();
            if (testedValue.length() < 1 && this._value.length() < 1) {
                switch (getCode()) {
                }
                return false;
            }
            if (this._pattern != null) {
                return evaluate(this._pattern.matcher(testedValue).matches());
            }
            return evaluate(testedValue.compareToIgnoreCase(this._value));
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x00dd  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public static java.util.regex.Pattern getWildCardPattern(java.lang.String r4) {
            /*
                r0 = r4
                int r0 = r0.length()
                r5 = r0
                java.lang.StringBuffer r0 = new java.lang.StringBuffer
                r1 = r0
                r2 = r5
                r1.<init>(r2)
                r6 = r0
                r0 = 0
                r7 = r0
                r0 = 0
                r8 = r0
            L13:
                r0 = r8
                r1 = r5
                if (r0 >= r1) goto L103
                r0 = r4
                r1 = r8
                char r0 = r0.charAt(r1)
                r9 = r0
                r0 = r9
                switch(r0) {
                    case 36: goto Le7;
                    case 40: goto Le7;
                    case 41: goto Le7;
                    case 42: goto L88;
                    case 46: goto Le7;
                    case 63: goto L7c;
                    case 91: goto Le7;
                    case 93: goto Le7;
                    case 94: goto Le7;
                    case 126: goto L94;
                    default: goto Lf6;
                }
            L7c:
                r0 = 1
                r7 = r0
                r0 = r6
                r1 = 46
                java.lang.StringBuffer r0 = r0.append(r1)
                goto Lfd
            L88:
                r0 = 1
                r7 = r0
                r0 = r6
                java.lang.String r1 = ".*"
                java.lang.StringBuffer r0 = r0.append(r1)
                goto Lfd
            L94:
                r0 = r8
                r1 = 1
                int r0 = r0 + r1
                r1 = r5
                if (r0 >= r1) goto Ldd
                r0 = r4
                r1 = r8
                r2 = 1
                int r1 = r1 + r2
                char r0 = r0.charAt(r1)
                r9 = r0
                r0 = r9
                switch(r0) {
                    case 42: goto Lc4;
                    case 63: goto Lc4;
                    default: goto Ldd;
                }
            Lc4:
                r0 = 1
                r7 = r0
                r0 = r6
                r1 = 91
                java.lang.StringBuffer r0 = r0.append(r1)
                r1 = r9
                java.lang.StringBuffer r0 = r0.append(r1)
                r1 = 93
                java.lang.StringBuffer r0 = r0.append(r1)
                int r8 = r8 + 1
                goto Lfd
            Ldd:
                r0 = r6
                r1 = 126(0x7e, float:1.77E-43)
                java.lang.StringBuffer r0 = r0.append(r1)
                goto Lfd
            Le7:
                r0 = r6
                java.lang.String r1 = "\\"
                java.lang.StringBuffer r0 = r0.append(r1)
                r1 = r9
                java.lang.StringBuffer r0 = r0.append(r1)
                goto Lfd
            Lf6:
                r0 = r6
                r1 = r9
                java.lang.StringBuffer r0 = r0.append(r1)
            Lfd:
                int r8 = r8 + 1
                goto L13
            L103:
                r0 = r7
                if (r0 == 0) goto L110
                r0 = r6
                java.lang.String r0 = r0.toString()
                r1 = 2
                java.util.regex.Pattern r0 = java.util.regex.Pattern.compile(r0, r1)
                return r0
            L110:
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ss.formula.functions.Countif.StringMatcher.getWildCardPattern(java.lang.String):java.util.regex.Pattern");
        }
    }

    @Override // org.apache.poi.ss.formula.functions.Function2Arg
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        CountUtils.I_MatchPredicate mp = createCriteriaPredicate(arg1, srcRowIndex, srcColumnIndex);
        if (mp == null) {
            return NumberEval.ZERO;
        }
        double result = countMatchingCellsInArea(arg0, mp);
        return new NumberEval(result);
    }

    private double countMatchingCellsInArea(ValueEval rangeArg, CountUtils.I_MatchPredicate criteriaPredicate) {
        if (rangeArg instanceof RefEval) {
            return CountUtils.countMatchingCellsInRef((RefEval) rangeArg, criteriaPredicate);
        }
        if (rangeArg instanceof ThreeDEval) {
            return CountUtils.countMatchingCellsInArea((ThreeDEval) rangeArg, criteriaPredicate);
        }
        throw new IllegalArgumentException("Bad range arg type (" + rangeArg.getClass().getName() + ")");
    }

    static CountUtils.I_MatchPredicate createCriteriaPredicate(ValueEval arg, int srcRowIndex, int srcColumnIndex) {
        ValueEval evaluatedCriteriaArg = evaluateCriteriaArg(arg, srcRowIndex, srcColumnIndex);
        if (evaluatedCriteriaArg instanceof NumberEval) {
            return new NumberMatcher(((NumberEval) evaluatedCriteriaArg).getNumberValue(), CmpOp.OP_NONE);
        }
        if (evaluatedCriteriaArg instanceof BoolEval) {
            return new BooleanMatcher(((BoolEval) evaluatedCriteriaArg).getBooleanValue(), CmpOp.OP_NONE);
        }
        if (evaluatedCriteriaArg instanceof StringEval) {
            return createGeneralMatchPredicate((StringEval) evaluatedCriteriaArg);
        }
        if (evaluatedCriteriaArg instanceof ErrorEval) {
            return new ErrorMatcher(((ErrorEval) evaluatedCriteriaArg).getErrorCode(), CmpOp.OP_NONE);
        }
        if (evaluatedCriteriaArg == BlankEval.instance) {
            return null;
        }
        throw new RuntimeException("Unexpected type for criteria (" + evaluatedCriteriaArg.getClass().getName() + ")");
    }

    private static ValueEval evaluateCriteriaArg(ValueEval arg, int srcRowIndex, int srcColumnIndex) {
        try {
            return OperandResolver.getSingleValue(arg, srcRowIndex, srcColumnIndex);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static CountUtils.I_MatchPredicate createGeneralMatchPredicate(StringEval stringEval) {
        String value = stringEval.getStringValue();
        CmpOp operator = CmpOp.getOperator(value);
        String value2 = value.substring(operator.getLength());
        Boolean booleanVal = parseBoolean(value2);
        if (booleanVal != null) {
            return new BooleanMatcher(booleanVal.booleanValue(), operator);
        }
        Double doubleVal = OperandResolver.parseDouble(value2);
        if (doubleVal != null) {
            return new NumberMatcher(doubleVal.doubleValue(), operator);
        }
        ErrorEval ee = parseError(value2);
        if (ee != null) {
            return new ErrorMatcher(ee.getErrorCode(), operator);
        }
        return new StringMatcher(value2, operator);
    }

    private static ErrorEval parseError(String value) {
        if (value.length() < 4 || value.charAt(0) != '#') {
            return null;
        }
        if (value.equals("#NULL!")) {
            return ErrorEval.NULL_INTERSECTION;
        }
        if (value.equals("#DIV/0!")) {
            return ErrorEval.DIV_ZERO;
        }
        if (value.equals("#VALUE!")) {
            return ErrorEval.VALUE_INVALID;
        }
        if (value.equals("#REF!")) {
            return ErrorEval.REF_INVALID;
        }
        if (value.equals("#NAME?")) {
            return ErrorEval.NAME_INVALID;
        }
        if (value.equals("#NUM!")) {
            return ErrorEval.NUM_ERROR;
        }
        if (value.equals("#N/A")) {
            return ErrorEval.NA;
        }
        return null;
    }

    static Boolean parseBoolean(String strRep) {
        if (strRep.length() < 1) {
            return null;
        }
        switch (strRep.charAt(0)) {
            case 'F':
            case 'f':
                if ("FALSE".equalsIgnoreCase(strRep)) {
                    return Boolean.FALSE;
                }
                return null;
            case 'T':
            case 't':
                if ("TRUE".equalsIgnoreCase(strRep)) {
                    return Boolean.TRUE;
                }
                return null;
            default:
                return null;
        }
    }
}
