package org.apache.poi.ss.format;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.HashMap;
import java.util.Map;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellFormatCondition.class */
public abstract class CellFormatCondition {
    private static final int LT = 0;
    private static final int LE = 1;
    private static final int GT = 2;
    private static final int GE = 3;
    private static final int EQ = 4;
    private static final int NE = 5;
    private static final Map<String, Integer> TESTS = new HashMap();

    public abstract boolean pass(double d);

    static {
        TESTS.put("<", 0);
        TESTS.put("<=", 1);
        TESTS.put(">", 2);
        TESTS.put(">=", 3);
        TESTS.put(SymbolConstants.EQUAL_SYMBOL, 4);
        TESTS.put("==", 4);
        TESTS.put("!=", 5);
        TESTS.put("<>", 5);
    }

    public static CellFormatCondition getInstance(String opString, String constStr) throws NumberFormatException {
        if (!TESTS.containsKey(opString)) {
            throw new IllegalArgumentException("Unknown test: " + opString);
        }
        int test = TESTS.get(opString).intValue();
        final double c = Double.parseDouble(constStr);
        switch (test) {
            case 0:
                return new CellFormatCondition() { // from class: org.apache.poi.ss.format.CellFormatCondition.1
                    @Override // org.apache.poi.ss.format.CellFormatCondition
                    public boolean pass(double value) {
                        return value < c;
                    }
                };
            case 1:
                return new CellFormatCondition() { // from class: org.apache.poi.ss.format.CellFormatCondition.2
                    @Override // org.apache.poi.ss.format.CellFormatCondition
                    public boolean pass(double value) {
                        return value <= c;
                    }
                };
            case 2:
                return new CellFormatCondition() { // from class: org.apache.poi.ss.format.CellFormatCondition.3
                    @Override // org.apache.poi.ss.format.CellFormatCondition
                    public boolean pass(double value) {
                        return value > c;
                    }
                };
            case 3:
                return new CellFormatCondition() { // from class: org.apache.poi.ss.format.CellFormatCondition.4
                    @Override // org.apache.poi.ss.format.CellFormatCondition
                    public boolean pass(double value) {
                        return value >= c;
                    }
                };
            case 4:
                return new CellFormatCondition() { // from class: org.apache.poi.ss.format.CellFormatCondition.5
                    @Override // org.apache.poi.ss.format.CellFormatCondition
                    public boolean pass(double value) {
                        return value == c;
                    }
                };
            case 5:
                return new CellFormatCondition() { // from class: org.apache.poi.ss.format.CellFormatCondition.6
                    @Override // org.apache.poi.ss.format.CellFormatCondition
                    public boolean pass(double value) {
                        return value != c;
                    }
                };
            default:
                throw new IllegalArgumentException("Cannot create for test number " + test + "(\"" + opString + "\")");
        }
    }
}
