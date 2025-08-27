package org.apache.poi.xssf.usermodel;

import java.util.Arrays;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFDataValidationConstraint.class */
public class XSSFDataValidationConstraint implements DataValidationConstraint {
    private static final String LIST_SEPARATOR = ",";
    private static final Pattern LIST_SPLIT_REGEX = Pattern.compile("\\s*,\\s*");
    private static final String QUOTE = "\"";
    private String formula1;
    private String formula2;
    private int validationType;
    private int operator;
    private String[] explicitListOfValues;

    public XSSFDataValidationConstraint(String[] explicitListOfValues) {
        this.validationType = -1;
        this.operator = -1;
        if (explicitListOfValues == null || explicitListOfValues.length == 0) {
            throw new IllegalArgumentException("List validation with explicit values must specify at least one value");
        }
        this.validationType = 3;
        setExplicitListValues(explicitListOfValues);
        validate();
    }

    public XSSFDataValidationConstraint(int validationType, String formula1) {
        this.validationType = -1;
        this.operator = -1;
        setFormula1(formula1);
        this.validationType = validationType;
        validate();
    }

    public XSSFDataValidationConstraint(int validationType, int operator, String formula1) {
        this.validationType = -1;
        this.operator = -1;
        setFormula1(formula1);
        this.validationType = validationType;
        this.operator = operator;
        validate();
    }

    public XSSFDataValidationConstraint(int validationType, int operator, String formula1, String formula2) {
        this.validationType = -1;
        this.operator = -1;
        setFormula1(formula1);
        setFormula2(formula2);
        this.validationType = validationType;
        this.operator = operator;
        validate();
        if (3 == validationType && this.formula1 != null && isQuoted(this.formula1)) {
            this.explicitListOfValues = LIST_SPLIT_REGEX.split(unquote(this.formula1));
        }
    }

    @Override // org.apache.poi.ss.usermodel.DataValidationConstraint
    public String[] getExplicitListValues() {
        return this.explicitListOfValues;
    }

    @Override // org.apache.poi.ss.usermodel.DataValidationConstraint
    public String getFormula1() {
        return this.formula1;
    }

    @Override // org.apache.poi.ss.usermodel.DataValidationConstraint
    public String getFormula2() {
        return this.formula2;
    }

    @Override // org.apache.poi.ss.usermodel.DataValidationConstraint
    public int getOperator() {
        return this.operator;
    }

    @Override // org.apache.poi.ss.usermodel.DataValidationConstraint
    public int getValidationType() {
        return this.validationType;
    }

    @Override // org.apache.poi.ss.usermodel.DataValidationConstraint
    public void setExplicitListValues(String[] explicitListValues) {
        this.explicitListOfValues = explicitListValues;
        if (this.explicitListOfValues != null && this.explicitListOfValues.length > 0) {
            StringBuilder builder = new StringBuilder("\"");
            for (String string : explicitListValues) {
                if (builder.length() > 1) {
                    builder.append(",");
                }
                builder.append(string);
            }
            builder.append("\"");
            setFormula1(builder.toString());
        }
    }

    @Override // org.apache.poi.ss.usermodel.DataValidationConstraint
    public void setFormula1(String formula1) {
        this.formula1 = removeLeadingEquals(formula1);
    }

    protected static String removeLeadingEquals(String formula1) {
        if (!isFormulaEmpty(formula1) && formula1.charAt(0) == '=') {
            return formula1.substring(1);
        }
        return formula1;
    }

    private static boolean isQuoted(String s) {
        return s.startsWith("\"") && s.endsWith("\"");
    }

    private static String unquote(String s) {
        if (isQuoted(s)) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    protected static boolean isFormulaEmpty(String formula1) {
        return formula1 == null || formula1.trim().length() == 0;
    }

    @Override // org.apache.poi.ss.usermodel.DataValidationConstraint
    public void setFormula2(String formula2) {
        this.formula2 = removeLeadingEquals(formula2);
    }

    @Override // org.apache.poi.ss.usermodel.DataValidationConstraint
    public void setOperator(int operator) {
        this.operator = operator;
    }

    public void validate() {
        if (this.validationType == 0) {
            return;
        }
        if (this.validationType == 3) {
            if (isFormulaEmpty(this.formula1)) {
                throw new IllegalArgumentException("A valid formula or a list of values must be specified for list validation.");
            }
        } else {
            if (isFormulaEmpty(this.formula1)) {
                throw new IllegalArgumentException("Formula is not specified. Formula is required for all validation types except explicit list validation.");
            }
            if (this.validationType != 7) {
                if (this.operator == -1) {
                    throw new IllegalArgumentException("This validation type requires an operator to be specified.");
                }
                if ((this.operator == 0 || this.operator == 1) && isFormulaEmpty(this.formula2)) {
                    throw new IllegalArgumentException("Between and not between comparisons require two formulae to be specified.");
                }
            }
        }
    }

    public String prettyPrint() {
        StringBuilder builder = new StringBuilder();
        STDataValidationType.Enum vt = XSSFDataValidation.validationTypeMappings.get(Integer.valueOf(this.validationType));
        STDataValidationOperator.Enum ot = XSSFDataValidation.operatorTypeMappings.get(Integer.valueOf(this.operator));
        builder.append(vt);
        builder.append(' ');
        if (this.validationType != 0) {
            if (this.validationType != 3 && this.validationType != 7) {
                builder.append(",").append(ot).append(", ");
            }
            if (this.validationType == 3 && this.explicitListOfValues != null) {
                builder.append("").append(Arrays.asList(this.explicitListOfValues)).append("").append(' ');
            } else {
                builder.append("").append(this.formula1).append("").append(' ');
            }
            if (this.formula2 != null) {
                builder.append("").append(this.formula2).append("").append(' ');
            }
        }
        return builder.toString();
    }
}
