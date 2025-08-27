package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaParsingWorkbook;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.ptg.Area3DPxg;
import org.apache.poi.ss.usermodel.Table;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Indirect.class */
public final class Indirect implements FreeRefFunction {
    public static final FreeRefFunction instance = new Indirect();

    private Indirect() {
    }

    @Override // org.apache.poi.ss.formula.functions.FreeRefFunction
    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        boolean isA1style;
        if (args.length < 1) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            ValueEval ve = OperandResolver.getSingleValue(args[0], ec.getRowIndex(), ec.getColumnIndex());
            String text = OperandResolver.coerceValueToString(ve);
            switch (args.length) {
                case 1:
                    isA1style = true;
                    break;
                case 2:
                    isA1style = evaluateBooleanArg(args[1], ec);
                    break;
                default:
                    return ErrorEval.VALUE_INVALID;
            }
            return evaluateIndirect(ec, text, isA1style);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static boolean evaluateBooleanArg(ValueEval arg, OperationEvaluationContext ec) throws EvaluationException {
        ValueEval ve = OperandResolver.getSingleValue(arg, ec.getRowIndex(), ec.getColumnIndex());
        if (ve == BlankEval.instance || ve == MissingArgEval.instance) {
            return false;
        }
        return OperandResolver.coerceValueToBoolean(ve, false).booleanValue();
    }

    private static ValueEval evaluateIndirect(OperationEvaluationContext ec, String text, boolean isA1style) {
        String workbookName;
        String sheetName;
        String refText;
        String refStrPart1;
        String refStrPart2;
        int plingPos = text.lastIndexOf(33);
        if (plingPos < 0) {
            workbookName = null;
            sheetName = null;
            refText = text;
        } else {
            String[] parts = parseWorkbookAndSheetName(text.subSequence(0, plingPos));
            if (parts == null) {
                return ErrorEval.REF_INVALID;
            }
            workbookName = parts[0];
            sheetName = parts[1];
            refText = text.substring(plingPos + 1);
        }
        if (Table.isStructuredReference.matcher(refText).matches()) {
            try {
                Area3DPxg areaPtg = FormulaParser.parseStructuredReference(refText, (FormulaParsingWorkbook) ec.getWorkbook(), ec.getRowIndex());
                return ec.getArea3DEval(areaPtg);
            } catch (FormulaParseException e) {
                return ErrorEval.REF_INVALID;
            }
        }
        int colonPos = refText.indexOf(58);
        if (colonPos < 0) {
            refStrPart1 = refText.trim();
            refStrPart2 = null;
        } else {
            refStrPart1 = refText.substring(0, colonPos).trim();
            refStrPart2 = refText.substring(colonPos + 1).trim();
        }
        return ec.getDynamicReference(workbookName, sheetName, refStrPart1, refStrPart2, isA1style);
    }

    private static String[] parseWorkbookAndSheetName(CharSequence text) {
        String wbName;
        int sheetStartPos;
        int lastIx = text.length() - 1;
        if (lastIx < 0 || canTrim(text)) {
            return null;
        }
        char firstChar = text.charAt(0);
        if (Character.isWhitespace(firstChar)) {
            return null;
        }
        if (firstChar == '\'') {
            if (text.charAt(lastIx) != '\'') {
                return null;
            }
            char firstChar2 = text.charAt(1);
            if (Character.isWhitespace(firstChar2)) {
                return null;
            }
            if (firstChar2 == '[') {
                int rbPos = text.toString().lastIndexOf(93);
                if (rbPos < 0) {
                    return null;
                }
                wbName = unescapeString(text.subSequence(2, rbPos));
                if (wbName == null || canTrim(wbName)) {
                    return null;
                }
                sheetStartPos = rbPos + 1;
            } else {
                wbName = null;
                sheetStartPos = 1;
            }
            String sheetName = unescapeString(text.subSequence(sheetStartPos, lastIx));
            if (sheetName == null) {
                return null;
            }
            return new String[]{wbName, sheetName};
        }
        if (firstChar == '[') {
            int rbPos2 = text.toString().lastIndexOf(93);
            if (rbPos2 < 0) {
                return null;
            }
            CharSequence wbName2 = text.subSequence(1, rbPos2);
            if (canTrim(wbName2)) {
                return null;
            }
            CharSequence sheetName2 = text.subSequence(rbPos2 + 1, text.length());
            if (canTrim(sheetName2)) {
                return null;
            }
            return new String[]{wbName2.toString(), sheetName2.toString()};
        }
        return new String[]{null, text.toString()};
    }

    private static String unescapeString(CharSequence text) {
        int len = text.length();
        StringBuilder sb = new StringBuilder(len);
        int i = 0;
        while (i < len) {
            char ch2 = text.charAt(i);
            if (ch2 == '\'') {
                i++;
                if (i >= len) {
                    return null;
                }
                ch2 = text.charAt(i);
                if (ch2 != '\'') {
                    return null;
                }
            }
            sb.append(ch2);
            i++;
        }
        return sb.toString();
    }

    private static boolean canTrim(CharSequence text) {
        int lastIx = text.length() - 1;
        if (lastIx < 0) {
            return false;
        }
        if (Character.isWhitespace(text.charAt(0)) || Character.isWhitespace(text.charAt(lastIx))) {
            return true;
        }
        return false;
    }
}
