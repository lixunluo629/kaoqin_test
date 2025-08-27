package org.apache.poi.ss.formula.functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.NumericValueEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.Countif;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils.class */
final class LookupUtils {

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$LookupValueComparer.class */
    public interface LookupValueComparer {
        CompareResult compareTo(ValueEval valueEval);
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$ValueVector.class */
    public interface ValueVector {
        ValueEval getItem(int i);

        int getSize();
    }

    LookupUtils() {
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$RowVector.class */
    private static final class RowVector implements ValueVector {
        private final TwoDEval _tableArray;
        private final int _size;
        private final int _rowIndex;

        public RowVector(TwoDEval tableArray, int rowIndex) {
            this._rowIndex = rowIndex;
            int lastRowIx = tableArray.getHeight() - 1;
            if (rowIndex < 0 || rowIndex > lastRowIx) {
                throw new IllegalArgumentException("Specified row index (" + rowIndex + ") is outside the allowed range (0.." + lastRowIx + ")");
            }
            this._tableArray = tableArray;
            this._size = tableArray.getWidth();
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.ValueVector
        public ValueEval getItem(int index) {
            if (index > this._size) {
                throw new ArrayIndexOutOfBoundsException("Specified index (" + index + ") is outside the allowed range (0.." + (this._size - 1) + ")");
            }
            return this._tableArray.getValue(this._rowIndex, index);
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.ValueVector
        public int getSize() {
            return this._size;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$ColumnVector.class */
    private static final class ColumnVector implements ValueVector {
        private final TwoDEval _tableArray;
        private final int _size;
        private final int _columnIndex;

        public ColumnVector(TwoDEval tableArray, int columnIndex) {
            this._columnIndex = columnIndex;
            int lastColIx = tableArray.getWidth() - 1;
            if (columnIndex < 0 || columnIndex > lastColIx) {
                throw new IllegalArgumentException("Specified column index (" + columnIndex + ") is outside the allowed range (0.." + lastColIx + ")");
            }
            this._tableArray = tableArray;
            this._size = this._tableArray.getHeight();
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.ValueVector
        public ValueEval getItem(int index) {
            if (index > this._size) {
                throw new ArrayIndexOutOfBoundsException("Specified index (" + index + ") is outside the allowed range (0.." + (this._size - 1) + ")");
            }
            return this._tableArray.getValue(index, this._columnIndex);
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.ValueVector
        public int getSize() {
            return this._size;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$SheetVector.class */
    private static final class SheetVector implements ValueVector {
        private final RefEval _re;
        private final int _size;

        public SheetVector(RefEval re) {
            this._size = re.getNumberOfSheets();
            this._re = re;
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.ValueVector
        public ValueEval getItem(int index) {
            if (index >= this._size) {
                throw new ArrayIndexOutOfBoundsException("Specified index (" + index + ") is outside the allowed range (0.." + (this._size - 1) + ")");
            }
            int sheetIndex = this._re.getFirstSheetIndex() + index;
            return this._re.getInnerValueEval(sheetIndex);
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.ValueVector
        public int getSize() {
            return this._size;
        }
    }

    public static ValueVector createRowVector(TwoDEval tableArray, int relativeRowIndex) {
        return new RowVector(tableArray, relativeRowIndex);
    }

    public static ValueVector createColumnVector(TwoDEval tableArray, int relativeColumnIndex) {
        return new ColumnVector(tableArray, relativeColumnIndex);
    }

    public static ValueVector createVector(TwoDEval ae) {
        if (ae.isColumn()) {
            return createColumnVector(ae, 0);
        }
        if (ae.isRow()) {
            return createRowVector(ae, 0);
        }
        return null;
    }

    public static ValueVector createVector(RefEval re) {
        return new SheetVector(re);
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$CompareResult.class */
    public static final class CompareResult {
        private final boolean _isTypeMismatch;
        private final boolean _isLessThan;
        private final boolean _isEqual;
        private final boolean _isGreaterThan;
        public static final CompareResult TYPE_MISMATCH = new CompareResult(true, 0);
        public static final CompareResult LESS_THAN = new CompareResult(false, -1);
        public static final CompareResult EQUAL = new CompareResult(false, 0);
        public static final CompareResult GREATER_THAN = new CompareResult(false, 1);

        private CompareResult(boolean isTypeMismatch, int simpleCompareResult) {
            if (isTypeMismatch) {
                this._isTypeMismatch = true;
                this._isLessThan = false;
                this._isEqual = false;
                this._isGreaterThan = false;
                return;
            }
            this._isTypeMismatch = false;
            this._isLessThan = simpleCompareResult < 0;
            this._isEqual = simpleCompareResult == 0;
            this._isGreaterThan = simpleCompareResult > 0;
        }

        public static CompareResult valueOf(int simpleCompareResult) {
            if (simpleCompareResult < 0) {
                return LESS_THAN;
            }
            if (simpleCompareResult > 0) {
                return GREATER_THAN;
            }
            return EQUAL;
        }

        public static CompareResult valueOf(boolean matches) {
            if (matches) {
                return EQUAL;
            }
            return LESS_THAN;
        }

        public boolean isTypeMismatch() {
            return this._isTypeMismatch;
        }

        public boolean isLessThan() {
            return this._isLessThan;
        }

        public boolean isEqual() {
            return this._isEqual;
        }

        public boolean isGreaterThan() {
            return this._isGreaterThan;
        }

        public String toString() {
            return getClass().getName() + " [" + formatAsString() + "]";
        }

        private String formatAsString() {
            if (this._isTypeMismatch) {
                return "TYPE_MISMATCH";
            }
            if (this._isLessThan) {
                return "LESS_THAN";
            }
            if (this._isEqual) {
                return "EQUAL";
            }
            if (this._isGreaterThan) {
                return "GREATER_THAN";
            }
            return "??error??";
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$LookupValueComparerBase.class */
    private static abstract class LookupValueComparerBase implements LookupValueComparer {
        private final Class<? extends ValueEval> _targetClass;

        protected abstract CompareResult compareSameType(ValueEval valueEval);

        protected abstract String getValueAsString();

        protected LookupValueComparerBase(ValueEval targetValue) {
            if (targetValue == null) {
                throw new RuntimeException("targetValue cannot be null");
            }
            this._targetClass = targetValue.getClass();
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.LookupValueComparer
        public final CompareResult compareTo(ValueEval other) {
            if (other == null) {
                throw new RuntimeException("compare to value cannot be null");
            }
            if (this._targetClass != other.getClass()) {
                return CompareResult.TYPE_MISMATCH;
            }
            return compareSameType(other);
        }

        public String toString() {
            return getClass().getName() + " [" + getValueAsString() + "]";
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$StringLookupComparer.class */
    private static final class StringLookupComparer extends LookupValueComparerBase {
        private String _value;
        private final Pattern _wildCardPattern;
        private boolean _matchExact;
        private boolean _isMatchFunction;

        protected StringLookupComparer(StringEval se, boolean matchExact, boolean isMatchFunction) {
            super(se);
            this._value = se.getStringValue();
            this._wildCardPattern = Countif.StringMatcher.getWildCardPattern(this._value);
            this._matchExact = matchExact;
            this._isMatchFunction = isMatchFunction;
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.LookupValueComparerBase
        protected CompareResult compareSameType(ValueEval other) {
            StringEval se = (StringEval) other;
            String stringValue = se.getStringValue();
            if (this._wildCardPattern != null) {
                Matcher matcher = this._wildCardPattern.matcher(stringValue);
                boolean matches = matcher.matches();
                if (this._isMatchFunction || !this._matchExact) {
                    return CompareResult.valueOf(matches);
                }
            }
            return CompareResult.valueOf(this._value.compareToIgnoreCase(stringValue));
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.LookupValueComparerBase
        protected String getValueAsString() {
            return this._value;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$NumberLookupComparer.class */
    private static final class NumberLookupComparer extends LookupValueComparerBase {
        private double _value;

        protected NumberLookupComparer(NumberEval ne) {
            super(ne);
            this._value = ne.getNumberValue();
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.LookupValueComparerBase
        protected CompareResult compareSameType(ValueEval other) {
            NumberEval ne = (NumberEval) other;
            return CompareResult.valueOf(Double.compare(this._value, ne.getNumberValue()));
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.LookupValueComparerBase
        protected String getValueAsString() {
            return String.valueOf(this._value);
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$BooleanLookupComparer.class */
    private static final class BooleanLookupComparer extends LookupValueComparerBase {
        private boolean _value;

        protected BooleanLookupComparer(BoolEval be) {
            super(be);
            this._value = be.getBooleanValue();
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.LookupValueComparerBase
        protected CompareResult compareSameType(ValueEval other) {
            BoolEval be = (BoolEval) other;
            boolean otherVal = be.getBooleanValue();
            if (this._value == otherVal) {
                return CompareResult.EQUAL;
            }
            if (this._value) {
                return CompareResult.GREATER_THAN;
            }
            return CompareResult.LESS_THAN;
        }

        @Override // org.apache.poi.ss.formula.functions.LookupUtils.LookupValueComparerBase
        protected String getValueAsString() {
            return String.valueOf(this._value);
        }
    }

    public static int resolveRowOrColIndexArg(ValueEval rowColIndexArg, int srcCellRow, int srcCellCol) throws EvaluationException {
        if (rowColIndexArg == null) {
            throw new IllegalArgumentException("argument must not be null");
        }
        try {
            ValueEval veRowColIndexArg = OperandResolver.getSingleValue(rowColIndexArg, srcCellRow, (short) srcCellCol);
            if (veRowColIndexArg instanceof StringEval) {
                StringEval se = (StringEval) veRowColIndexArg;
                String strVal = se.getStringValue();
                Double dVal = OperandResolver.parseDouble(strVal);
                if (dVal == null) {
                    throw EvaluationException.invalidRef();
                }
            }
            int oneBasedIndex = OperandResolver.coerceValueToInt(veRowColIndexArg);
            if (oneBasedIndex < 1) {
                throw EvaluationException.invalidValue();
            }
            return oneBasedIndex - 1;
        } catch (EvaluationException e) {
            throw EvaluationException.invalidRef();
        }
    }

    public static TwoDEval resolveTableArrayArg(ValueEval eval) throws EvaluationException {
        if (eval instanceof TwoDEval) {
            return (TwoDEval) eval;
        }
        if (eval instanceof RefEval) {
            RefEval refEval = (RefEval) eval;
            return refEval.offset(0, 0, 0, 0);
        }
        throw EvaluationException.invalidValue();
    }

    public static boolean resolveRangeLookupArg(ValueEval rangeLookupArg, int srcCellRow, int srcCellCol) throws EvaluationException {
        ValueEval valEval = OperandResolver.getSingleValue(rangeLookupArg, srcCellRow, srcCellCol);
        if (valEval instanceof BlankEval) {
            return false;
        }
        if (valEval instanceof BoolEval) {
            BoolEval boolEval = (BoolEval) valEval;
            return boolEval.getBooleanValue();
        }
        if (valEval instanceof StringEval) {
            String stringValue = ((StringEval) valEval).getStringValue();
            if (stringValue.length() < 1) {
                throw EvaluationException.invalidValue();
            }
            Boolean b = Countif.parseBoolean(stringValue);
            if (b != null) {
                return b.booleanValue();
            }
            throw EvaluationException.invalidValue();
        }
        if (valEval instanceof NumericValueEval) {
            NumericValueEval nve = (NumericValueEval) valEval;
            return 0.0d != nve.getNumberValue();
        }
        throw new RuntimeException("Unexpected eval type (" + valEval + ")");
    }

    public static int lookupIndexOfValue(ValueEval lookupValue, ValueVector vector, boolean isRangeLookup) throws EvaluationException {
        int result;
        LookupValueComparer lookupComparer = createLookupComparer(lookupValue, isRangeLookup, false);
        if (isRangeLookup) {
            result = performBinarySearch(vector, lookupComparer);
        } else {
            result = lookupIndexOfExactValue(lookupComparer, vector);
        }
        if (result < 0) {
            throw new EvaluationException(ErrorEval.NA);
        }
        return result;
    }

    private static int lookupIndexOfExactValue(LookupValueComparer lookupComparer, ValueVector vector) {
        int size = vector.getSize();
        for (int i = 0; i < size; i++) {
            if (lookupComparer.compareTo(vector.getItem(i)).isEqual()) {
                return i;
            }
        }
        return -1;
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/LookupUtils$BinarySearchIndexes.class */
    private static final class BinarySearchIndexes {
        private int _lowIx = -1;
        private int _highIx;

        public BinarySearchIndexes(int highIx) {
            this._highIx = highIx;
        }

        public int getMidIx() {
            int ixDiff = this._highIx - this._lowIx;
            if (ixDiff < 2) {
                return -1;
            }
            return this._lowIx + (ixDiff / 2);
        }

        public int getLowIx() {
            return this._lowIx;
        }

        public int getHighIx() {
            return this._highIx;
        }

        public void narrowSearch(int midIx, boolean isLessThan) {
            if (isLessThan) {
                this._highIx = midIx;
            } else {
                this._lowIx = midIx;
            }
        }
    }

    private static int performBinarySearch(ValueVector vector, LookupValueComparer lookupComparer) {
        BinarySearchIndexes bsi = new BinarySearchIndexes(vector.getSize());
        while (true) {
            int midIx = bsi.getMidIx();
            if (midIx < 0) {
                return bsi.getLowIx();
            }
            CompareResult cr = lookupComparer.compareTo(vector.getItem(midIx));
            if (cr.isTypeMismatch()) {
                int newMidIx = handleMidValueTypeMismatch(lookupComparer, vector, bsi, midIx);
                if (newMidIx < 0) {
                    continue;
                } else {
                    midIx = newMidIx;
                    cr = lookupComparer.compareTo(vector.getItem(midIx));
                }
            }
            if (cr.isEqual()) {
                return findLastIndexInRunOfEqualValues(lookupComparer, vector, midIx, bsi.getHighIx());
            }
            bsi.narrowSearch(midIx, cr.isLessThan());
        }
    }

    private static int handleMidValueTypeMismatch(LookupValueComparer lookupComparer, ValueVector vector, BinarySearchIndexes bsi, int midIx) {
        CompareResult cr;
        int newMid = midIx;
        int highIx = bsi.getHighIx();
        do {
            newMid++;
            if (newMid == highIx) {
                bsi.narrowSearch(midIx, true);
                return -1;
            }
            cr = lookupComparer.compareTo(vector.getItem(newMid));
            if (cr.isLessThan() && newMid == highIx - 1) {
                bsi.narrowSearch(midIx, true);
                return -1;
            }
        } while (cr.isTypeMismatch());
        if (cr.isEqual()) {
            return newMid;
        }
        bsi.narrowSearch(newMid, cr.isLessThan());
        return -1;
    }

    private static int findLastIndexInRunOfEqualValues(LookupValueComparer lookupComparer, ValueVector vector, int firstFoundIndex, int maxIx) {
        for (int i = firstFoundIndex + 1; i < maxIx; i++) {
            if (!lookupComparer.compareTo(vector.getItem(i)).isEqual()) {
                return i - 1;
            }
        }
        return maxIx - 1;
    }

    public static LookupValueComparer createLookupComparer(ValueEval lookupValue, boolean matchExact, boolean isMatchFunction) {
        if (lookupValue == BlankEval.instance) {
            return new NumberLookupComparer(NumberEval.ZERO);
        }
        if (lookupValue instanceof StringEval) {
            return new StringLookupComparer((StringEval) lookupValue, matchExact, isMatchFunction);
        }
        if (lookupValue instanceof NumberEval) {
            return new NumberLookupComparer((NumberEval) lookupValue);
        }
        if (lookupValue instanceof BoolEval) {
            return new BooleanLookupComparer((BoolEval) lookupValue);
        }
        throw new IllegalArgumentException("Bad lookup value type (" + lookupValue.getClass().getName() + ")");
    }
}
