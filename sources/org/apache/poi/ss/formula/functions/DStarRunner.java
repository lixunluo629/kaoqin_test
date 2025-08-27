package org.apache.poi.ss.formula.functions;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.ss.formula.eval.NumericValueEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.StringValueEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.util.NumberComparer;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/DStarRunner.class */
public final class DStarRunner implements Function3Arg {
    private final DStarAlgorithmEnum algoType;

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/DStarRunner$DStarAlgorithmEnum.class */
    public enum DStarAlgorithmEnum {
        DGET,
        DMIN
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/DStarRunner$operator.class */
    private enum operator {
        largerThan,
        largerEqualThan,
        smallerThan,
        smallerEqualThan,
        equal
    }

    public DStarRunner(DStarAlgorithmEnum algorithm) {
        this.algoType = algorithm;
    }

    @Override // org.apache.poi.ss.formula.functions.Function
    public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length == 3) {
            return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2]);
        }
        return ErrorEval.VALUE_INVALID;
    }

    @Override // org.apache.poi.ss.formula.functions.Function3Arg
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval database, ValueEval filterColumn, ValueEval conditionDatabase) {
        IDStarAlgorithm algorithm;
        if (!(database instanceof AreaEval) || !(conditionDatabase instanceof AreaEval)) {
            return ErrorEval.VALUE_INVALID;
        }
        AreaEval db = (AreaEval) database;
        AreaEval cdb = (AreaEval) conditionDatabase;
        try {
            try {
                int fc = getColumnForName(OperandResolver.getSingleValue(filterColumn, srcRowIndex, srcColumnIndex), db);
                if (fc == -1) {
                    return ErrorEval.VALUE_INVALID;
                }
                switch (this.algoType) {
                    case DGET:
                        algorithm = new DGet();
                        break;
                    case DMIN:
                        algorithm = new DMin();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected algorithm type " + this.algoType + " encountered.");
                }
                int height = db.getHeight();
                for (int row = 1; row < height; row++) {
                    try {
                        boolean matches = fullfillsConditions(db, row, cdb);
                        if (matches) {
                            ValueEval currentValueEval = resolveReference(db, row, fc);
                            boolean shouldContinue = algorithm.processMatch(currentValueEval);
                            if (!shouldContinue) {
                                return algorithm.getResult();
                            }
                        }
                    } catch (EvaluationException e) {
                        return ErrorEval.VALUE_INVALID;
                    }
                }
                return algorithm.getResult();
            } catch (EvaluationException e2) {
                return ErrorEval.VALUE_INVALID;
            }
        } catch (EvaluationException e3) {
            return e3.getErrorEval();
        }
    }

    private static int getColumnForName(ValueEval nameValueEval, AreaEval db) throws EvaluationException {
        String name = OperandResolver.coerceValueToString(nameValueEval);
        return getColumnForString(db, name);
    }

    private static int getColumnForString(AreaEval db, String name) throws EvaluationException {
        int resultColumn = -1;
        int width = db.getWidth();
        int column = 0;
        while (true) {
            if (column >= width) {
                break;
            }
            ValueEval columnNameValueEval = resolveReference(db, 0, column);
            if (!(columnNameValueEval instanceof BlankEval) && !(columnNameValueEval instanceof ErrorEval)) {
                String columnName = OperandResolver.coerceValueToString(columnNameValueEval);
                if (name.equals(columnName)) {
                    resultColumn = column;
                    break;
                }
            }
            column++;
        }
        return resultColumn;
    }

    private static boolean fullfillsConditions(AreaEval db, int row, AreaEval cdb) throws EvaluationException {
        int height = cdb.getHeight();
        for (int conditionRow = 1; conditionRow < height; conditionRow++) {
            boolean matches = true;
            int width = cdb.getWidth();
            int column = 0;
            while (true) {
                if (column >= width) {
                    break;
                }
                boolean columnCondition = true;
                ValueEval condition = resolveReference(cdb, conditionRow, column);
                if (!(condition instanceof BlankEval)) {
                    ValueEval targetHeader = resolveReference(cdb, 0, column);
                    if (!(targetHeader instanceof StringValueEval)) {
                        throw new EvaluationException(ErrorEval.VALUE_INVALID);
                    }
                    if (getColumnForName(targetHeader, db) == -1) {
                        columnCondition = false;
                    }
                    if (columnCondition) {
                        ValueEval value = resolveReference(db, row, getColumnForName(targetHeader, db));
                        if (!testNormalCondition(value, condition)) {
                            matches = false;
                            break;
                        }
                    } else {
                        if (OperandResolver.coerceValueToString(condition).isEmpty()) {
                            throw new EvaluationException(ErrorEval.VALUE_INVALID);
                        }
                        throw new NotImplementedException("D* function with formula conditions");
                    }
                }
                column++;
            }
            if (matches) {
                return true;
            }
        }
        return false;
    }

    private static boolean testNormalCondition(ValueEval value, ValueEval condition) throws NumberFormatException, EvaluationException {
        boolean itsANumber;
        if (condition instanceof StringEval) {
            String conditionString = ((StringEval) condition).getStringValue();
            if (conditionString.startsWith("<")) {
                String number = conditionString.substring(1);
                if (number.startsWith(SymbolConstants.EQUAL_SYMBOL)) {
                    return testNumericCondition(value, operator.smallerEqualThan, number.substring(1));
                }
                return testNumericCondition(value, operator.smallerThan, number);
            }
            if (conditionString.startsWith(">")) {
                String number2 = conditionString.substring(1);
                if (number2.startsWith(SymbolConstants.EQUAL_SYMBOL)) {
                    return testNumericCondition(value, operator.largerEqualThan, number2.substring(1));
                }
                return testNumericCondition(value, operator.largerThan, number2);
            }
            if (conditionString.startsWith(SymbolConstants.EQUAL_SYMBOL)) {
                String stringOrNumber = conditionString.substring(1);
                if (stringOrNumber.isEmpty()) {
                    return value instanceof BlankEval;
                }
                try {
                    Integer.parseInt(stringOrNumber);
                    itsANumber = true;
                } catch (NumberFormatException e) {
                    try {
                        Double.parseDouble(stringOrNumber);
                        itsANumber = true;
                    } catch (NumberFormatException e2) {
                        itsANumber = false;
                    }
                }
                if (itsANumber) {
                    return testNumericCondition(value, operator.equal, stringOrNumber);
                }
                String valueString = value instanceof BlankEval ? "" : OperandResolver.coerceValueToString(value);
                return stringOrNumber.equals(valueString);
            }
            if (conditionString.isEmpty()) {
                return value instanceof StringEval;
            }
            String valueString2 = value instanceof BlankEval ? "" : OperandResolver.coerceValueToString(value);
            return valueString2.startsWith(conditionString);
        }
        if (!(condition instanceof NumericValueEval)) {
            return (condition instanceof ErrorEval) && (value instanceof ErrorEval) && ((ErrorEval) condition).getErrorCode() == ((ErrorEval) value).getErrorCode();
        }
        double conditionNumber = ((NumericValueEval) condition).getNumberValue();
        Double valueNumber = getNumberFromValueEval(value);
        return valueNumber != null && conditionNumber == valueNumber.doubleValue();
    }

    private static boolean testNumericCondition(ValueEval valueEval, operator op, String condition) throws NumberFormatException, EvaluationException {
        double conditionValue;
        if (!(valueEval instanceof NumericValueEval)) {
            return false;
        }
        double value = ((NumericValueEval) valueEval).getNumberValue();
        try {
            int intValue = Integer.parseInt(condition);
            conditionValue = intValue;
        } catch (NumberFormatException e) {
            try {
                conditionValue = Double.parseDouble(condition);
            } catch (NumberFormatException e2) {
                throw new EvaluationException(ErrorEval.VALUE_INVALID);
            }
        }
        int result = NumberComparer.compare(value, conditionValue);
        switch (op) {
            case largerThan:
                return result > 0;
            case largerEqualThan:
                return result >= 0;
            case smallerThan:
                return result < 0;
            case smallerEqualThan:
                return result <= 0;
            case equal:
                return result == 0;
            default:
                return false;
        }
    }

    private static Double getNumberFromValueEval(ValueEval value) {
        if (value instanceof NumericValueEval) {
            return Double.valueOf(((NumericValueEval) value).getNumberValue());
        }
        if (value instanceof StringValueEval) {
            String stringValue = ((StringValueEval) value).getStringValue();
            try {
                return Double.valueOf(Double.parseDouble(stringValue));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private static ValueEval resolveReference(AreaEval db, int dbRow, int dbCol) {
        try {
            return OperandResolver.getSingleValue(db.getValue(dbRow, dbCol), db.getFirstRow() + dbRow, db.getFirstColumn() + dbCol);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}
