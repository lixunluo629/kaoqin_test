package org.apache.poi.ss.usermodel;

import java.util.List;
import org.apache.poi.ss.formula.ConditionalFormattingEvaluator;
import org.apache.poi.ss.formula.EvaluationConditionalFormatRule;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/ExcelNumberFormat.class */
public class ExcelNumberFormat {
    private final int idx;
    private final String format;

    public static ExcelNumberFormat from(CellStyle style) {
        if (style == null) {
            return null;
        }
        return new ExcelNumberFormat(style.getDataFormat(), style.getDataFormatString());
    }

    public static ExcelNumberFormat from(Cell cell, ConditionalFormattingEvaluator cfEvaluator) {
        if (cell == null) {
            return null;
        }
        ExcelNumberFormat nf = null;
        if (cfEvaluator != null) {
            List<EvaluationConditionalFormatRule> rules = cfEvaluator.getConditionalFormattingForCell(cell);
            for (EvaluationConditionalFormatRule rule : rules) {
                nf = rule.getNumberFormat();
                if (nf != null) {
                    break;
                }
            }
        }
        if (nf == null) {
            CellStyle style = cell.getCellStyle();
            nf = from(style);
        }
        return nf;
    }

    public ExcelNumberFormat(int idx, String format) {
        this.idx = idx;
        this.format = format;
    }

    public int getIdx() {
        return this.idx;
    }

    public String getFormat() {
        return this.format;
    }
}
