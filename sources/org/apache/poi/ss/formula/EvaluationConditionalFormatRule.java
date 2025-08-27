package org.apache.poi.ss.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.AggregateFunction;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ConditionFilterData;
import org.apache.poi.ss.usermodel.ConditionFilterType;
import org.apache.poi.ss.usermodel.ConditionType;
import org.apache.poi.ss.usermodel.ConditionalFormatting;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.ExcelNumberFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationConditionalFormatRule.class */
public class EvaluationConditionalFormatRule implements Comparable<EvaluationConditionalFormatRule> {
    private final WorkbookEvaluator workbookEvaluator;
    private final Sheet sheet;
    private final ConditionalFormatting formatting;
    private final ConditionalFormattingRule rule;
    private final CellRangeAddress[] regions;
    private final Map<CellRangeAddress, Set<ValueAndFormat>> meaningfulRegionValues = new HashMap();
    private final int priority;
    private final int formattingIndex;
    private final int ruleIndex;
    private final String formula1;
    private final String formula2;
    private final OperatorEnum operator;
    private final ConditionType type;
    private final ExcelNumberFormat numberFormat;

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationConditionalFormatRule$OperatorEnum.class */
    public enum OperatorEnum {
        NO_COMPARISON { // from class: org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum.1
            @Override // org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum
            public <C extends Comparable<C>> boolean isValid(C cellValue, C v1, C v2) {
                return false;
            }
        },
        BETWEEN { // from class: org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum.2
            @Override // org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum
            public <C extends Comparable<C>> boolean isValid(C cellValue, C v1, C v2) {
                return cellValue.compareTo(v1) >= 0 && cellValue.compareTo(v2) <= 0;
            }
        },
        NOT_BETWEEN { // from class: org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum.3
            @Override // org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum
            public <C extends Comparable<C>> boolean isValid(C cellValue, C v1, C v2) {
                return cellValue.compareTo(v1) < 0 || cellValue.compareTo(v2) > 0;
            }
        },
        EQUAL { // from class: org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum.4
            @Override // org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum
            public <C extends Comparable<C>> boolean isValid(C cellValue, C v1, C v2) {
                return cellValue.getClass() == String.class ? cellValue.toString().compareToIgnoreCase(v1.toString()) == 0 : cellValue.compareTo(v1) == 0;
            }
        },
        NOT_EQUAL { // from class: org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum.5
            @Override // org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum
            public <C extends Comparable<C>> boolean isValid(C cellValue, C v1, C v2) {
                return cellValue.getClass() == String.class ? cellValue.toString().compareToIgnoreCase(v1.toString()) == 0 : cellValue.compareTo(v1) != 0;
            }
        },
        GREATER_THAN { // from class: org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum.6
            @Override // org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum
            public <C extends Comparable<C>> boolean isValid(C cellValue, C v1, C v2) {
                return cellValue.compareTo(v1) > 0;
            }
        },
        LESS_THAN { // from class: org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum.7
            @Override // org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum
            public <C extends Comparable<C>> boolean isValid(C cellValue, C v1, C v2) {
                return cellValue.compareTo(v1) < 0;
            }
        },
        GREATER_OR_EQUAL { // from class: org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum.8
            @Override // org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum
            public <C extends Comparable<C>> boolean isValid(C cellValue, C v1, C v2) {
                return cellValue.compareTo(v1) >= 0;
            }
        },
        LESS_OR_EQUAL { // from class: org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum.9
            @Override // org.apache.poi.ss.formula.EvaluationConditionalFormatRule.OperatorEnum
            public <C extends Comparable<C>> boolean isValid(C cellValue, C v1, C v2) {
                return cellValue.compareTo(v1) <= 0;
            }
        };

        public abstract <C extends Comparable<C>> boolean isValid(C c, C c2, C c3);
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationConditionalFormatRule$ValueFunction.class */
    protected interface ValueFunction {
        Set<ValueAndFormat> evaluate(List<ValueAndFormat> list);
    }

    public EvaluationConditionalFormatRule(WorkbookEvaluator workbookEvaluator, Sheet sheet, ConditionalFormatting formatting, int formattingIndex, ConditionalFormattingRule rule, int ruleIndex, CellRangeAddress[] regions) {
        this.workbookEvaluator = workbookEvaluator;
        this.sheet = sheet;
        this.formatting = formatting;
        this.rule = rule;
        this.formattingIndex = formattingIndex;
        this.ruleIndex = ruleIndex;
        this.priority = rule.getPriority();
        this.regions = regions;
        this.formula1 = rule.getFormula1();
        this.formula2 = rule.getFormula2();
        this.numberFormat = rule.getNumberFormat();
        this.operator = OperatorEnum.values()[rule.getComparisonOperation()];
        this.type = rule.getConditionType();
    }

    public Sheet getSheet() {
        return this.sheet;
    }

    public ConditionalFormatting getFormatting() {
        return this.formatting;
    }

    public int getFormattingIndex() {
        return this.formattingIndex;
    }

    public ExcelNumberFormat getNumberFormat() {
        return this.numberFormat;
    }

    public ConditionalFormattingRule getRule() {
        return this.rule;
    }

    public int getRuleIndex() {
        return this.ruleIndex;
    }

    public CellRangeAddress[] getRegions() {
        return this.regions;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getFormula1() {
        return this.formula1;
    }

    public String getFormula2() {
        return this.formula2;
    }

    public OperatorEnum getOperator() {
        return this.operator;
    }

    public ConditionType getType() {
        return this.type;
    }

    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        EvaluationConditionalFormatRule r = (EvaluationConditionalFormatRule) obj;
        return getSheet().getSheetName().equalsIgnoreCase(r.getSheet().getSheetName()) && getFormattingIndex() == r.getFormattingIndex() && getRuleIndex() == r.getRuleIndex();
    }

    @Override // java.lang.Comparable
    public int compareTo(EvaluationConditionalFormatRule o) {
        int cmp = getSheet().getSheetName().compareToIgnoreCase(o.getSheet().getSheetName());
        if (cmp != 0) {
            return cmp;
        }
        int x = getPriority();
        int y = o.getPriority();
        int cmp2 = x < y ? -1 : x == y ? 0 : 1;
        if (cmp2 != 0) {
            return cmp2;
        }
        int cmp3 = new Integer(getFormattingIndex()).compareTo(new Integer(o.getFormattingIndex()));
        if (cmp3 != 0) {
            return cmp3;
        }
        return new Integer(getRuleIndex()).compareTo(new Integer(o.getRuleIndex()));
    }

    public int hashCode() {
        int hash = this.sheet.getSheetName().hashCode();
        return (31 * ((31 * hash) + this.formattingIndex)) + this.ruleIndex;
    }

    boolean matches(CellReference ref) {
        CellRangeAddress region = null;
        CellRangeAddress[] arr$ = this.regions;
        int len$ = arr$.length;
        int i$ = 0;
        while (true) {
            if (i$ >= len$) {
                break;
            }
            CellRangeAddress r = arr$[i$];
            if (!r.isInRange(ref)) {
                i$++;
            } else {
                region = r;
                break;
            }
        }
        if (region == null) {
            return false;
        }
        ConditionType ruleType = getRule().getConditionType();
        if (ruleType.equals(ConditionType.COLOR_SCALE) || ruleType.equals(ConditionType.DATA_BAR) || ruleType.equals(ConditionType.ICON_SET)) {
            return true;
        }
        Cell cell = null;
        Row row = this.sheet.getRow(ref.getRow());
        if (row != null) {
            cell = row.getCell(ref.getCol());
        }
        if (ruleType.equals(ConditionType.CELL_VALUE_IS)) {
            if (cell == null) {
                return false;
            }
            return checkValue(cell, region);
        }
        if (ruleType.equals(ConditionType.FORMULA)) {
            return checkFormula(ref, region);
        }
        if (ruleType.equals(ConditionType.FILTER)) {
            return checkFilter(cell, ref, region);
        }
        return false;
    }

    private boolean checkValue(Cell cell, CellRangeAddress region) {
        if (cell == null || DataValidationEvaluator.isType(cell, CellType.BLANK) || DataValidationEvaluator.isType(cell, CellType.ERROR)) {
            return false;
        }
        if (DataValidationEvaluator.isType(cell, CellType.STRING) && (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())) {
            return false;
        }
        ValueEval eval = unwrapEval(this.workbookEvaluator.evaluate(this.rule.getFormula1(), ConditionalFormattingEvaluator.getRef(cell), region));
        String f2 = this.rule.getFormula2();
        ValueEval eval2 = null;
        if (f2 != null && f2.length() > 0) {
            eval2 = unwrapEval(this.workbookEvaluator.evaluate(f2, ConditionalFormattingEvaluator.getRef(cell), region));
        }
        if (DataValidationEvaluator.isType(cell, CellType.BOOLEAN)) {
            if (!(eval instanceof BoolEval)) {
                return false;
            }
            if (eval2 == null || (eval2 instanceof BoolEval)) {
                return this.operator.isValid(Boolean.valueOf(cell.getBooleanCellValue()), Boolean.valueOf(((BoolEval) eval).getBooleanValue()), eval2 == null ? null : Boolean.valueOf(((BoolEval) eval2).getBooleanValue()));
            }
            return false;
        }
        if (DataValidationEvaluator.isType(cell, CellType.NUMERIC)) {
            if (!(eval instanceof NumberEval)) {
                return false;
            }
            if (eval2 == null || (eval2 instanceof NumberEval)) {
                return this.operator.isValid(Double.valueOf(cell.getNumericCellValue()), Double.valueOf(((NumberEval) eval).getNumberValue()), eval2 == null ? null : Double.valueOf(((NumberEval) eval2).getNumberValue()));
            }
            return false;
        }
        if (!DataValidationEvaluator.isType(cell, CellType.STRING) || !(eval instanceof StringEval)) {
            return false;
        }
        if (eval2 == null || (eval2 instanceof StringEval)) {
            return this.operator.isValid(cell.getStringCellValue(), ((StringEval) eval).getStringValue(), eval2 == null ? null : ((StringEval) eval2).getStringValue());
        }
        return false;
    }

    private ValueEval unwrapEval(ValueEval eval) {
        ValueEval innerValueEval = eval;
        while (true) {
            ValueEval comp = innerValueEval;
            if (comp instanceof RefEval) {
                RefEval ref = (RefEval) comp;
                innerValueEval = ref.getInnerValueEval(ref.getFirstSheetIndex());
            } else {
                return comp;
            }
        }
    }

    private boolean checkFormula(CellReference ref, CellRangeAddress region) {
        ValueEval comp = unwrapEval(this.workbookEvaluator.evaluate(this.rule.getFormula1(), ref, region));
        if (comp instanceof BlankEval) {
            return true;
        }
        if (comp instanceof ErrorEval) {
            return false;
        }
        if (comp instanceof BoolEval) {
            return ((BoolEval) comp).getBooleanValue();
        }
        return (comp instanceof NumberEval) && ((NumberEval) comp).getNumberValue() != 0.0d;
    }

    private boolean checkFilter(Cell cell, CellReference ref, CellRangeAddress region) {
        double stdDev;
        OperatorEnum op;
        ConditionFilterType filterType = this.rule.getConditionFilterType();
        if (filterType == null) {
            return false;
        }
        ValueAndFormat cv = getCellValue(cell);
        switch (filterType) {
            case TOP_10:
                if (cv.isNumber()) {
                    break;
                }
                break;
            case ABOVE_AVERAGE:
                ConditionFilterData conf = this.rule.getFilterConfiguration();
                List<ValueAndFormat> values = new ArrayList<>(getMeaningfulValues(region, false, new ValueFunction() { // from class: org.apache.poi.ss.formula.EvaluationConditionalFormatRule.4
                    @Override // org.apache.poi.ss.formula.EvaluationConditionalFormatRule.ValueFunction
                    public Set<ValueAndFormat> evaluate(List<ValueAndFormat> allValues) {
                        double total = 0.0d;
                        ValueEval[] pop = new ValueEval[allValues.size()];
                        for (int i = 0; i < allValues.size(); i++) {
                            ValueAndFormat v = allValues.get(i);
                            total += v.value.doubleValue();
                            pop[i] = new NumberEval(v.value.doubleValue());
                        }
                        Set<ValueAndFormat> avgSet = new LinkedHashSet<>(1);
                        avgSet.add(new ValueAndFormat(new Double(allValues.size() == 0 ? 0.0d : total / allValues.size()), (String) null));
                        double stdDev2 = allValues.size() <= 1 ? 0.0d : ((NumberEval) AggregateFunction.STDEV.evaluate(pop, 0, 0)).getNumberValue();
                        avgSet.add(new ValueAndFormat(new Double(stdDev2), (String) null));
                        return avgSet;
                    }
                }));
                Double val = cv.isNumber() ? cv.getValue() : null;
                if (val != null) {
                    double avg = values.get(0).value.doubleValue();
                    double stdDev2 = values.get(1).value.doubleValue();
                    if (conf.getStdDev() > 0) {
                        stdDev = avg + ((conf.getAboveAverage() ? 1 : -1) * stdDev2 * conf.getStdDev());
                    } else {
                        stdDev = avg;
                    }
                    Double comp = new Double(stdDev);
                    if (conf.getAboveAverage()) {
                        if (conf.getEqualAverage()) {
                            op = OperatorEnum.GREATER_OR_EQUAL;
                        } else {
                            op = OperatorEnum.GREATER_THAN;
                        }
                    } else if (conf.getEqualAverage()) {
                        op = OperatorEnum.LESS_OR_EQUAL;
                    } else {
                        op = OperatorEnum.LESS_THAN;
                    }
                    if (op == null || !op.isValid(val, comp, null)) {
                    }
                }
                break;
            case CONTAINS_BLANKS:
                try {
                    String v = cv.getString();
                    if (v != null) {
                        if (v.trim().length() != 0) {
                        }
                    }
                    break;
                } catch (Exception e) {
                    return false;
                }
                break;
            case NOT_CONTAINS_BLANKS:
                try {
                    String v2 = cv.getString();
                    if (v2 != null) {
                        if (v2.trim().length() > 0) {
                            break;
                        }
                    }
                } catch (Exception e2) {
                    return true;
                }
                break;
            case CONTAINS_ERRORS:
                if (cell == null || !DataValidationEvaluator.isType(cell, CellType.ERROR)) {
                }
                break;
            case NOT_CONTAINS_ERRORS:
                if (cell == null || !DataValidationEvaluator.isType(cell, CellType.ERROR)) {
                }
                break;
        }
        return false;
    }

    private Set<ValueAndFormat> getMeaningfulValues(CellRangeAddress region, boolean withText, ValueFunction func) {
        Set<ValueAndFormat> values = this.meaningfulRegionValues.get(region);
        if (values != null) {
            return values;
        }
        List<ValueAndFormat> allValues = new ArrayList<>(((region.getLastColumn() - region.getFirstColumn()) + 1) * ((region.getLastRow() - region.getFirstRow()) + 1));
        for (int r = region.getFirstRow(); r <= region.getLastRow(); r++) {
            Row row = this.sheet.getRow(r);
            if (row != null) {
                for (int c = region.getFirstColumn(); c <= region.getLastColumn(); c++) {
                    Cell cell = row.getCell(c);
                    ValueAndFormat cv = getCellValue(cell);
                    if (cv != null && (withText || cv.isNumber())) {
                        allValues.add(cv);
                    }
                }
            }
        }
        Set<ValueAndFormat> values2 = func.evaluate(allValues);
        this.meaningfulRegionValues.put(region, values2);
        return values2;
    }

    private ValueAndFormat getCellValue(Cell cell) {
        if (cell != null) {
            CellType type = cell.getCellTypeEnum();
            if (type == CellType.NUMERIC || (type == CellType.FORMULA && cell.getCachedFormulaResultTypeEnum() == CellType.NUMERIC)) {
                return new ValueAndFormat(new Double(cell.getNumericCellValue()), cell.getCellStyle().getDataFormatString());
            }
            if (type == CellType.STRING || (type == CellType.FORMULA && cell.getCachedFormulaResultTypeEnum() == CellType.STRING)) {
                return new ValueAndFormat(cell.getStringCellValue(), cell.getCellStyle().getDataFormatString());
            }
            if (type == CellType.BOOLEAN || (type == CellType.FORMULA && cell.getCachedFormulaResultTypeEnum() == CellType.BOOLEAN)) {
                return new ValueAndFormat(cell.getStringCellValue(), cell.getCellStyle().getDataFormatString());
            }
        }
        return new ValueAndFormat("", "");
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationConditionalFormatRule$ValueAndFormat.class */
    protected static class ValueAndFormat implements Comparable<ValueAndFormat> {
        private final Double value;
        private final String string;
        private final String format;

        public ValueAndFormat(Double value, String format) {
            this.value = value;
            this.format = format;
            this.string = null;
        }

        public ValueAndFormat(String value, String format) {
            this.value = null;
            this.format = format;
            this.string = value;
        }

        public boolean isNumber() {
            return this.value != null;
        }

        public Double getValue() {
            return this.value;
        }

        public String getString() {
            return this.string;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ValueAndFormat)) {
                return false;
            }
            ValueAndFormat o = (ValueAndFormat) obj;
            return (this.value == o.value || this.value.equals(o.value)) && (this.format == o.format || this.format.equals(o.format)) && (this.string == o.string || this.string.equals(o.string));
        }

        @Override // java.lang.Comparable
        public int compareTo(ValueAndFormat o) {
            if (this.value == null && o.value != null) {
                return 1;
            }
            if (o.value == null && this.value != null) {
                return -1;
            }
            int cmp = this.value == null ? 0 : this.value.compareTo(o.value);
            if (cmp != 0) {
                return cmp;
            }
            if (this.string == null && o.string != null) {
                return 1;
            }
            if (o.string == null && this.string != null) {
                return -1;
            }
            if (this.string == null) {
                return 0;
            }
            return this.string.compareTo(o.string);
        }

        public int hashCode() {
            return ((this.string == null ? 0 : this.string.hashCode()) * 37 * 37) + (37 * (this.value == null ? 0 : this.value.hashCode())) + (this.format == null ? 0 : this.format.hashCode());
        }
    }
}
