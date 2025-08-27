package org.apache.poi.ss.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.constant.ErrorConstant;
import org.apache.poi.ss.formula.function.FunctionMetadata;
import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
import org.apache.poi.ss.formula.ptg.AbstractFunctionPtg;
import org.apache.poi.ss.formula.ptg.AddPtg;
import org.apache.poi.ss.formula.ptg.Area3DPxg;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.ArrayPtg;
import org.apache.poi.ss.formula.ptg.AttrPtg;
import org.apache.poi.ss.formula.ptg.BoolPtg;
import org.apache.poi.ss.formula.ptg.ConcatPtg;
import org.apache.poi.ss.formula.ptg.DividePtg;
import org.apache.poi.ss.formula.ptg.EqualPtg;
import org.apache.poi.ss.formula.ptg.ErrPtg;
import org.apache.poi.ss.formula.ptg.FuncPtg;
import org.apache.poi.ss.formula.ptg.FuncVarPtg;
import org.apache.poi.ss.formula.ptg.GreaterEqualPtg;
import org.apache.poi.ss.formula.ptg.GreaterThanPtg;
import org.apache.poi.ss.formula.ptg.IntPtg;
import org.apache.poi.ss.formula.ptg.IntersectionPtg;
import org.apache.poi.ss.formula.ptg.LessEqualPtg;
import org.apache.poi.ss.formula.ptg.LessThanPtg;
import org.apache.poi.ss.formula.ptg.MemAreaPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.MissingArgPtg;
import org.apache.poi.ss.formula.ptg.MultiplyPtg;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.NameXPxg;
import org.apache.poi.ss.formula.ptg.NotEqualPtg;
import org.apache.poi.ss.formula.ptg.NumberPtg;
import org.apache.poi.ss.formula.ptg.OperandPtg;
import org.apache.poi.ss.formula.ptg.OperationPtg;
import org.apache.poi.ss.formula.ptg.ParenthesisPtg;
import org.apache.poi.ss.formula.ptg.PercentPtg;
import org.apache.poi.ss.formula.ptg.PowerPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.RangePtg;
import org.apache.poi.ss.formula.ptg.RefPtg;
import org.apache.poi.ss.formula.ptg.StringPtg;
import org.apache.poi.ss.formula.ptg.SubtractPtg;
import org.apache.poi.ss.formula.ptg.UnaryMinusPtg;
import org.apache.poi.ss.formula.ptg.UnaryPlusPtg;
import org.apache.poi.ss.formula.ptg.UnionPtg;
import org.apache.poi.ss.formula.ptg.ValueOperatorPtg;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/FormulaParser.class */
public final class FormulaParser {
    private final String _formulaString;
    private final int _formulaLength;
    private int _pointer = 0;
    private ParseNode _rootNode;
    private static final char TAB = '\t';
    private static final char CR = '\r';
    private static final char LF = '\n';
    private int look;
    private boolean _inIntersection;
    private final FormulaParsingWorkbook _book;
    private final SpreadsheetVersion _ssVersion;
    private final int _sheetIndex;
    private final int _rowIndex;
    private static final String specHeaders = "Headers";
    private static final String specAll = "All";
    private static final String specData = "Data";
    private static final String specTotals = "Totals";
    private static final String specThisRow = "This Row";
    private static final POILogger log = POILogFactory.getLogger((Class<?>) FormulaParser.class);
    private static final Pattern CELL_REF_PATTERN = Pattern.compile("(\\$?[A-Za-z]+)?(\\$?[0-9]+)?");

    private FormulaParser(String formula, FormulaParsingWorkbook book, int sheetIndex, int rowIndex) {
        this._formulaString = formula;
        this._book = book;
        this._ssVersion = book == null ? SpreadsheetVersion.EXCEL97 : book.getSpreadsheetVersion();
        this._formulaLength = this._formulaString.length();
        this._sheetIndex = sheetIndex;
        this._rowIndex = rowIndex;
    }

    public static Ptg[] parse(String formula, FormulaParsingWorkbook workbook, FormulaType formulaType, int sheetIndex, int rowIndex) {
        FormulaParser fp = new FormulaParser(formula, workbook, sheetIndex, rowIndex);
        fp.parse();
        return fp.getRPNPtg(formulaType);
    }

    public static Ptg[] parse(String formula, FormulaParsingWorkbook workbook, FormulaType formulaType, int sheetIndex) {
        return parse(formula, workbook, formulaType, sheetIndex, -1);
    }

    public static Area3DPxg parseStructuredReference(String tableText, FormulaParsingWorkbook workbook, int rowIndex) {
        Ptg[] arr = parse(tableText, workbook, FormulaType.CELL, -1, rowIndex);
        if (arr.length != 1 || !(arr[0] instanceof Area3DPxg)) {
            throw new IllegalStateException("Illegal structured reference");
        }
        return (Area3DPxg) arr[0];
    }

    private void GetChar() {
        if (IsWhite(this.look)) {
            if (this.look == 32) {
                this._inIntersection = true;
            }
        } else {
            this._inIntersection = false;
        }
        if (this._pointer > this._formulaLength) {
            throw new RuntimeException("too far");
        }
        if (this._pointer < this._formulaLength) {
            this.look = this._formulaString.codePointAt(this._pointer);
        } else {
            this.look = 0;
            this._inIntersection = false;
        }
        this._pointer += Character.charCount(this.look);
    }

    private void resetPointer(int ptr) {
        this._pointer = ptr;
        if (this._pointer <= this._formulaLength) {
            this.look = this._formulaString.codePointAt(this._pointer - Character.charCount(this.look));
        } else {
            this.look = 0;
        }
    }

    private RuntimeException expected(String s) {
        String msg;
        if (this.look == 61 && this._formulaString.substring(0, this._pointer - 1).trim().length() < 1) {
            msg = "The specified formula '" + this._formulaString + "' starts with an equals sign which is not allowed.";
        } else {
            msg = new StringBuilder("Parse error near char ").append(this._pointer - 1).append(" '").appendCodePoint(this.look).append("'").append(" in specified formula '").append(this._formulaString).append("'. Expected ").append(s).toString();
        }
        return new FormulaParseException(msg);
    }

    private static boolean IsAlpha(int c) {
        return Character.isLetter(c) || c == 36 || c == 95;
    }

    private static boolean IsDigit(int c) {
        return Character.isDigit(c);
    }

    private static boolean IsWhite(int c) {
        return c == 32 || c == 9 || c == 13 || c == 10;
    }

    private void SkipWhite() {
        while (IsWhite(this.look)) {
            GetChar();
        }
    }

    private void Match(int x) {
        if (this.look != x) {
            throw expected(new StringBuilder().append("'").appendCodePoint(x).append("'").toString());
        }
        GetChar();
    }

    private String GetNum() {
        StringBuilder value = new StringBuilder();
        while (IsDigit(this.look)) {
            value.appendCodePoint(this.look);
            GetChar();
        }
        if (value.length() == 0) {
            return null;
        }
        return value.toString();
    }

    private ParseNode parseRangeExpression() throws NumberFormatException {
        boolean hasRange;
        ParseNode result = parseRangeable();
        boolean z = false;
        while (true) {
            hasRange = z;
            if (this.look != 58) {
                break;
            }
            int pos = this._pointer;
            GetChar();
            ParseNode nextPart = parseRangeable();
            checkValidRangeOperand("LHS", pos, result);
            checkValidRangeOperand("RHS", pos, nextPart);
            ParseNode[] children = {result, nextPart};
            result = new ParseNode(RangePtg.instance, children);
            z = true;
        }
        if (hasRange) {
            return augmentWithMemPtg(result);
        }
        return result;
    }

    private static ParseNode augmentWithMemPtg(ParseNode root) {
        Ptg memPtg;
        if (needsMemFunc(root)) {
            memPtg = new MemFuncPtg(root.getEncodedSize());
        } else {
            memPtg = new MemAreaPtg(root.getEncodedSize());
        }
        return new ParseNode(memPtg, root);
    }

    private static boolean needsMemFunc(ParseNode root) {
        Ptg token = root.getToken();
        if ((token instanceof AbstractFunctionPtg) || (token instanceof ExternSheetReferenceToken) || (token instanceof NamePtg) || (token instanceof NameXPtg)) {
            return true;
        }
        if ((token instanceof OperationPtg) || (token instanceof ParenthesisPtg)) {
            ParseNode[] arr$ = root.getChildren();
            for (ParseNode child : arr$) {
                if (needsMemFunc(child)) {
                    return true;
                }
            }
            return false;
        }
        if (!(token instanceof OperandPtg) && (token instanceof OperationPtg)) {
            return true;
        }
        return false;
    }

    private static void checkValidRangeOperand(String sideName, int currentParsePosition, ParseNode pn) {
        if (!isValidRangeOperand(pn)) {
            throw new FormulaParseException("The " + sideName + " of the range operator ':' at position " + currentParsePosition + " is not a proper reference.");
        }
    }

    private static boolean isValidRangeOperand(ParseNode a) {
        Ptg tkn = a.getToken();
        if (tkn instanceof OperandPtg) {
            return true;
        }
        if (tkn instanceof AbstractFunctionPtg) {
            AbstractFunctionPtg afp = (AbstractFunctionPtg) tkn;
            byte returnClass = afp.getDefaultOperandClass();
            return 0 == returnClass;
        }
        if (tkn instanceof ValueOperatorPtg) {
            return false;
        }
        if (tkn instanceof OperationPtg) {
            return true;
        }
        if (tkn instanceof ParenthesisPtg) {
            return isValidRangeOperand(a.getChildren()[0]);
        }
        if (tkn == ErrPtg.REF_INVALID) {
            return true;
        }
        return false;
    }

    private ParseNode parseRangeable() throws NumberFormatException {
        SkipWhite();
        int savePointer = this._pointer;
        SheetIdentifier sheetIden = parseSheetName();
        if (sheetIden == null) {
            resetPointer(savePointer);
        } else {
            SkipWhite();
            savePointer = this._pointer;
        }
        SimpleRangePart part1 = parseSimpleRangePart();
        if (part1 == null) {
            if (sheetIden != null) {
                if (this.look == 35) {
                    return new ParseNode(ErrPtg.valueOf(parseErrorLiteral()));
                }
                String name = parseAsName();
                if (name.length() == 0) {
                    throw new FormulaParseException("Cell reference or Named Range expected after sheet name at index " + this._pointer + ".");
                }
                Ptg nameXPtg = this._book.getNameXPtg(name, sheetIden);
                if (nameXPtg == null) {
                    throw new FormulaParseException("Specified name '" + name + "' for sheet " + sheetIden.asFormulaString() + " not found");
                }
                return new ParseNode(nameXPtg);
            }
            return parseNonRange(savePointer);
        }
        boolean whiteAfterPart1 = IsWhite(this.look);
        if (whiteAfterPart1) {
            SkipWhite();
        }
        if (this.look == 58) {
            int colonPos = this._pointer;
            GetChar();
            SkipWhite();
            SimpleRangePart part2 = parseSimpleRangePart();
            if (part2 != null && !part1.isCompatibleForArea(part2)) {
                part2 = null;
            }
            if (part2 == null) {
                resetPointer(colonPos);
                if (!part1.isCell()) {
                    String prefix = "";
                    if (sheetIden != null) {
                        prefix = "'" + sheetIden.getSheetIdentifier().getName() + '!';
                    }
                    throw new FormulaParseException(prefix + part1.getRep() + "' is not a proper reference.");
                }
            }
            return createAreaRefParseNode(sheetIden, part1, part2);
        }
        if (this.look == 46) {
            GetChar();
            int dotCount = 1;
            while (this.look == 46) {
                dotCount++;
                GetChar();
            }
            boolean whiteBeforePart2 = IsWhite(this.look);
            SkipWhite();
            SimpleRangePart part22 = parseSimpleRangePart();
            String part1And2 = this._formulaString.substring(savePointer - 1, this._pointer - 1);
            if (part22 == null) {
                if (sheetIden != null) {
                    throw new FormulaParseException("Complete area reference expected after sheet name at index " + this._pointer + ".");
                }
                return parseNonRange(savePointer);
            }
            if (whiteAfterPart1 || whiteBeforePart2) {
                if (part1.isRowOrColumn() || part22.isRowOrColumn()) {
                    throw new FormulaParseException("Dotted range (full row or column) expression '" + part1And2 + "' must not contain whitespace.");
                }
                return createAreaRefParseNode(sheetIden, part1, part22);
            }
            if (dotCount == 1 && part1.isRow() && part22.isRow()) {
                return parseNonRange(savePointer);
            }
            if ((part1.isRowOrColumn() || part22.isRowOrColumn()) && dotCount != 2) {
                throw new FormulaParseException("Dotted range (full row or column) expression '" + part1And2 + "' must have exactly 2 dots.");
            }
            return createAreaRefParseNode(sheetIden, part1, part22);
        }
        if (part1.isCell() && isValidCellReference(part1.getRep())) {
            return createAreaRefParseNode(sheetIden, part1, null);
        }
        if (sheetIden != null) {
            throw new FormulaParseException("Second part of cell reference expected after sheet name at index " + this._pointer + ".");
        }
        return parseNonRange(savePointer);
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x02ec, code lost:
    
        throw new org.apache.poi.ss.formula.FormulaParseException("Formula contained [#This Row] or [@] structured reference but this row < 0. Row index must be specified for row-referencing structured references.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x02ed, code lost:
    
        r28 = r0;
        r29 = r0;
        r30 = r0;
        r31 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x02ff, code lost:
    
        if (r22 <= 0) goto L146;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0305, code lost:
    
        if (r22 != 1) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x030a, code lost:
    
        if (r21 == false) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0312, code lost:
    
        if (r19 == false) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0317, code lost:
    
        if (r20 == false) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x0320, code lost:
    
        if (r0.getTotalsRowCount() <= 0) goto L152;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0323, code lost:
    
        r29 = r0 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x032e, code lost:
    
        if (r19 == false) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x0333, code lost:
    
        if (r17 == false) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0336, code lost:
    
        r28 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x0342, code lost:
    
        if (r22 != 1) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x0347, code lost:
    
        if (r19 == false) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x034a, code lost:
    
        r28 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0356, code lost:
    
        if (r0.getTotalsRowCount() <= 0) goto L152;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x0359, code lost:
    
        r29 = r0 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x0365, code lost:
    
        if (r22 != 1) goto L132;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x036a, code lost:
    
        if (r20 == false) goto L132;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x036d, code lost:
    
        r29 = r28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0377, code lost:
    
        if (r22 != 1) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x037c, code lost:
    
        if (r17 == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x037f, code lost:
    
        r28 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0389, code lost:
    
        if (r22 != 1) goto L141;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x038e, code lost:
    
        if (r18 != false) goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0393, code lost:
    
        if (r23 == false) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x0396, code lost:
    
        r28 = r8._rowIndex;
        r29 = r8._rowIndex;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x03c7, code lost:
    
        throw new org.apache.poi.ss.formula.FormulaParseException("The formula " + r8._formulaString + " is illegal");
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x03ca, code lost:
    
        if (r23 == false) goto L149;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x03cd, code lost:
    
        r28 = r8._rowIndex;
        r29 = r8._rowIndex;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x03dc, code lost:
    
        r28 = r28 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x03e5, code lost:
    
        if (r0.getTotalsRowCount() <= 0) goto L152;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x03e8, code lost:
    
        r29 = r29 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x03ee, code lost:
    
        if (r26 != 2) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x03f3, code lost:
    
        if (r24 == null) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x03f8, code lost:
    
        if (r25 != null) goto L160;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x0404, code lost:
    
        throw new java.lang.IllegalStateException("Fatal error");
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x0405, code lost:
    
        r0 = r0.findColumnIndex(r24);
        r0 = r0.findColumnIndex(r25);
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x041c, code lost:
    
        if (r0 == (-1)) goto L164;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x0422, code lost:
    
        if (r0 != (-1)) goto L166;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0458, code lost:
    
        throw new org.apache.poi.ss.formula.FormulaParseException("One of the columns " + r24 + ", " + r25 + " doesn't exist in table " + r0.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0459, code lost:
    
        r30 = r0 + r0;
        r31 = r0 + r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x046d, code lost:
    
        if (r26 != 1) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0472, code lost:
    
        if (r23 != false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0477, code lost:
    
        if (r24 != null) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x0483, code lost:
    
        throw new java.lang.IllegalStateException("Fatal error");
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x0484, code lost:
    
        r0 = r0.findColumnIndex(r24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x0491, code lost:
    
        if (r0 != (-1)) goto L179;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x04bd, code lost:
    
        throw new org.apache.poi.ss.formula.FormulaParseException("The column " + r24 + " doesn't exist in table " + r0.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x04be, code lost:
    
        r30 = r0 + r0;
        r31 = r30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x04c9, code lost:
    
        r0 = new org.apache.poi.ss.util.CellReference(r28, r30);
        r0 = new org.apache.poi.ss.util.CellReference(r29, r31);
        r0 = new org.apache.poi.ss.formula.SheetIdentifier(null, new org.apache.poi.ss.formula.NameIdentifier(r0, true));
        r0 = r8._book.get3DReferencePtg(new org.apache.poi.ss.util.AreaReference(r0, r0, r8._ssVersion), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x051b, code lost:
    
        return new org.apache.poi.ss.formula.ParseNode(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0122, code lost:
    
        r23 = false;
        SkipWhite();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x012f, code lost:
    
        if (r8.look != 64) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0132, code lost:
    
        r23 = true;
        GetChar();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0139, code lost:
    
        r25 = null;
        r26 = 0;
        r0 = r8._pointer;
        r24 = parseAsColumnQuantifier();
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x014d, code lost:
    
        if (r24 != null) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0150, code lost:
    
        resetPointer(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0159, code lost:
    
        r26 = 0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0162, code lost:
    
        if (r8.look != 44) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0187, code lost:
    
        throw new org.apache.poi.ss.formula.FormulaParseException("The formula " + r8._formulaString + "is illegal: you should not use ',' with column quantifiers");
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x018e, code lost:
    
        if (r8.look != 58) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0191, code lost:
    
        GetChar();
        r25 = parseAsColumnQuantifier();
        r26 = r26 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x01a0, code lost:
    
        if (r25 != null) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x01c5, code lost:
    
        throw new org.apache.poi.ss.formula.FormulaParseException("The formula " + r8._formulaString + "is illegal: the string after ':' must be column quantifier");
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x01c8, code lost:
    
        if (r26 != 0) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x01cd, code lost:
    
        if (r22 != 0) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01d0, code lost:
    
        resetPointer(r0);
        r0 = r8._pointer;
        r24 = parseAsColumnQuantifier();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01e4, code lost:
    
        if (r24 == null) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x01e7, code lost:
    
        r26 = r26 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01ed, code lost:
    
        resetPointer(r0);
        r0 = parseAsSpecialQuantifier();
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x01fb, code lost:
    
        if (r0 == null) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0205, code lost:
    
        if (r0.equals(org.apache.poi.ss.formula.FormulaParser.specAll) == false) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0208, code lost:
    
        r21 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0215, code lost:
    
        if (r0.equals(org.apache.poi.ss.formula.FormulaParser.specData) == false) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0218, code lost:
    
        r19 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0225, code lost:
    
        if (r0.equals(org.apache.poi.ss.formula.FormulaParser.specHeaders) == false) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0228, code lost:
    
        r20 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0235, code lost:
    
        if (r0.equals(org.apache.poi.ss.formula.FormulaParser.specThisRow) == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0238, code lost:
    
        r18 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0245, code lost:
    
        if (r0.equals(org.apache.poi.ss.formula.FormulaParser.specTotals) == false) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0248, code lost:
    
        r17 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0269, code lost:
    
        throw new org.apache.poi.ss.formula.FormulaParseException("Unknown special quantifier " + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x026a, code lost:
    
        r22 = r22 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0292, code lost:
    
        throw new org.apache.poi.ss.formula.FormulaParseException("The formula " + r8._formulaString + " is illegal");
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0296, code lost:
    
        Match(93);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x029e, code lost:
    
        if (r17 == false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x02a7, code lost:
    
        if (r0.getTotalsRowCount() != 0) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x02b4, code lost:
    
        return new org.apache.poi.ss.formula.ParseNode(org.apache.poi.ss.formula.ptg.ErrPtg.REF_INVALID);
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x02b7, code lost:
    
        if (r23 != false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x02bc, code lost:
    
        if (r18 == false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x02c5, code lost:
    
        if (r8._rowIndex < r0) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x02ce, code lost:
    
        if (r0 >= r8._rowIndex) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x02d5, code lost:
    
        if (r8._rowIndex < 0) goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x02e2, code lost:
    
        return new org.apache.poi.ss.formula.ParseNode(org.apache.poi.ss.formula.ptg.ErrPtg.VALUE_INVALID);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.apache.poi.ss.formula.ParseNode parseStructuredReference(java.lang.String r9) {
        /*
            Method dump skipped, instructions count: 1308
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ss.formula.FormulaParser.parseStructuredReference(java.lang.String):org.apache.poi.ss.formula.ParseNode");
    }

    private String parseAsColumnQuantifier() {
        if (this.look != 91) {
            return null;
        }
        GetChar();
        if (this.look == 35) {
            return null;
        }
        if (this.look == 64) {
            GetChar();
        }
        StringBuilder name = new StringBuilder();
        while (this.look != 93) {
            name.appendCodePoint(this.look);
            GetChar();
        }
        Match(93);
        return name.toString();
    }

    private String parseAsSpecialQuantifier() {
        if (this.look != 91) {
            return null;
        }
        GetChar();
        if (this.look != 35) {
            return null;
        }
        GetChar();
        String name = parseAsName();
        if (name.equals("This")) {
            name = name + ' ' + parseAsName();
        }
        Match(93);
        return name;
    }

    private ParseNode parseNonRange(int savePointer) {
        resetPointer(savePointer);
        if (Character.isDigit(this.look)) {
            return new ParseNode(parseNumber());
        }
        if (this.look == 34) {
            return new ParseNode(new StringPtg(parseStringLiteral()));
        }
        String name = parseAsName();
        if (this.look == 40) {
            return function(name);
        }
        if (this.look == 91) {
            return parseStructuredReference(name);
        }
        if (name.equalsIgnoreCase("TRUE") || name.equalsIgnoreCase("FALSE")) {
            return new ParseNode(BoolPtg.valueOf(name.equalsIgnoreCase("TRUE")));
        }
        if (this._book == null) {
            throw new IllegalStateException("Need book to evaluate name '" + name + "'");
        }
        EvaluationName evalName = this._book.getName(name, this._sheetIndex);
        if (evalName == null) {
            throw new FormulaParseException("Specified named range '" + name + "' does not exist in the current workbook.");
        }
        if (evalName.isRange()) {
            return new ParseNode(evalName.createPtg());
        }
        throw new FormulaParseException("Specified name '" + name + "' is not a range as expected.");
    }

    private String parseAsName() {
        StringBuilder sb = new StringBuilder();
        if (!Character.isLetter(this.look) && this.look != 95 && this.look != 92) {
            throw expected("number, string, defined name, or data table");
        }
        while (isValidDefinedNameChar(this.look)) {
            sb.appendCodePoint(this.look);
            GetChar();
        }
        SkipWhite();
        return sb.toString();
    }

    private static boolean isValidDefinedNameChar(int ch2) {
        if (!Character.isLetterOrDigit(ch2) && ch2 <= 128) {
            switch (ch2) {
                case 46:
                case 63:
                case 92:
                case 95:
                    break;
            }
            return true;
        }
        return true;
    }

    private ParseNode createAreaRefParseNode(SheetIdentifier sheetIden, SimpleRangePart part1, SimpleRangePart part2) throws FormulaParseException {
        Ptg ptg;
        if (part2 == null) {
            CellReference cr = part1.getCellReference();
            if (sheetIden == null) {
                ptg = new RefPtg(cr);
            } else {
                ptg = this._book.get3DReferencePtg(cr, sheetIden);
            }
        } else {
            AreaReference areaRef = createAreaRef(part1, part2);
            if (sheetIden == null) {
                ptg = new AreaPtg(areaRef);
            } else {
                ptg = this._book.get3DReferencePtg(areaRef, sheetIden);
            }
        }
        return new ParseNode(ptg);
    }

    private AreaReference createAreaRef(SimpleRangePart part1, SimpleRangePart part2) {
        if (!part1.isCompatibleForArea(part2)) {
            throw new FormulaParseException("has incompatible parts: '" + part1.getRep() + "' and '" + part2.getRep() + "'.");
        }
        if (part1.isRow()) {
            return AreaReference.getWholeRow(this._ssVersion, part1.getRep(), part2.getRep());
        }
        if (part1.isColumn()) {
            return AreaReference.getWholeColumn(this._ssVersion, part1.getRep(), part2.getRep());
        }
        return new AreaReference(part1.getCellReference(), part2.getCellReference(), this._ssVersion);
    }

    private SimpleRangePart parseSimpleRangePart() throws NumberFormatException {
        int ptr = this._pointer - 1;
        boolean hasDigits = false;
        boolean hasLetters = false;
        while (ptr < this._formulaLength) {
            char ch2 = this._formulaString.charAt(ptr);
            if (Character.isDigit(ch2)) {
                hasDigits = true;
            } else if (Character.isLetter(ch2)) {
                hasLetters = true;
            } else if (ch2 != '$' && ch2 != '_') {
                break;
            }
            ptr++;
        }
        if (ptr <= this._pointer - 1) {
            return null;
        }
        String rep = this._formulaString.substring(this._pointer - 1, ptr);
        if (!CELL_REF_PATTERN.matcher(rep).matches()) {
            return null;
        }
        if (hasLetters && hasDigits) {
            if (!isValidCellReference(rep)) {
                return null;
            }
        } else if (hasLetters) {
            if (!CellReference.isColumnWithinRange(rep.replace(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, ""), this._ssVersion)) {
                return null;
            }
        } else if (hasDigits) {
            try {
                int i = Integer.parseInt(rep.replace(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, ""));
                if (i < 1 || i > this._ssVersion.getMaxRows()) {
                    return null;
                }
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
        resetPointer(ptr + 1);
        return new SimpleRangePart(rep, hasLetters, hasDigits);
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/FormulaParser$SimpleRangePart.class */
    private static final class SimpleRangePart {
        private final Type _type;
        private final String _rep;

        /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/FormulaParser$SimpleRangePart$Type.class */
        private enum Type {
            CELL,
            ROW,
            COLUMN;

            public static Type get(boolean hasLetters, boolean hasDigits) {
                if (hasLetters) {
                    return hasDigits ? CELL : COLUMN;
                }
                if (!hasDigits) {
                    throw new IllegalArgumentException("must have either letters or numbers");
                }
                return ROW;
            }
        }

        public SimpleRangePart(String rep, boolean hasLetters, boolean hasNumbers) {
            this._rep = rep;
            this._type = Type.get(hasLetters, hasNumbers);
        }

        public boolean isCell() {
            return this._type == Type.CELL;
        }

        public boolean isRowOrColumn() {
            return this._type != Type.CELL;
        }

        public CellReference getCellReference() {
            if (this._type != Type.CELL) {
                throw new IllegalStateException("Not applicable to this type");
            }
            return new CellReference(this._rep);
        }

        public boolean isColumn() {
            return this._type == Type.COLUMN;
        }

        public boolean isRow() {
            return this._type == Type.ROW;
        }

        public String getRep() {
            return this._rep;
        }

        public boolean isCompatibleForArea(SimpleRangePart part2) {
            return this._type == part2._type;
        }

        public String toString() {
            return getClass().getName() + " [" + this._rep + "]";
        }
    }

    private String getBookName() {
        StringBuilder sb = new StringBuilder();
        GetChar();
        while (this.look != 93) {
            sb.appendCodePoint(this.look);
            GetChar();
        }
        GetChar();
        return sb.toString();
    }

    private SheetIdentifier parseSheetName() {
        String bookName;
        if (this.look == 91) {
            bookName = getBookName();
        } else {
            bookName = null;
        }
        if (this.look == 39) {
            Match(39);
            if (this.look == 91) {
                bookName = getBookName();
            }
            StringBuilder sb = new StringBuilder();
            boolean done = this.look == 39;
            while (!done) {
                sb.appendCodePoint(this.look);
                GetChar();
                if (this.look == 39) {
                    Match(39);
                    done = this.look != 39;
                }
            }
            NameIdentifier iden = new NameIdentifier(sb.toString(), true);
            SkipWhite();
            if (this.look == 33) {
                GetChar();
                return new SheetIdentifier(bookName, iden);
            }
            if (this.look == 58) {
                return parseSheetRange(bookName, iden);
            }
            return null;
        }
        if (this.look == 95 || Character.isLetter(this.look)) {
            StringBuilder sb2 = new StringBuilder();
            while (isUnquotedSheetNameChar(this.look)) {
                sb2.appendCodePoint(this.look);
                GetChar();
            }
            NameIdentifier iden2 = new NameIdentifier(sb2.toString(), false);
            SkipWhite();
            if (this.look == 33) {
                GetChar();
                return new SheetIdentifier(bookName, iden2);
            }
            if (this.look == 58) {
                return parseSheetRange(bookName, iden2);
            }
            return null;
        }
        if (this.look == 33 && bookName != null) {
            GetChar();
            return new SheetIdentifier(bookName, null);
        }
        return null;
    }

    private SheetIdentifier parseSheetRange(String bookname, NameIdentifier sheet1Name) {
        GetChar();
        SheetIdentifier sheet2 = parseSheetName();
        if (sheet2 != null) {
            return new SheetRangeIdentifier(bookname, sheet1Name, sheet2.getSheetIdentifier());
        }
        return null;
    }

    private static boolean isUnquotedSheetNameChar(int ch2) {
        if (!Character.isLetterOrDigit(ch2) && ch2 <= 128) {
            switch (ch2) {
                case 46:
                case 95:
                    break;
            }
            return true;
        }
        return true;
    }

    private boolean isValidCellReference(String str) {
        boolean result = CellReference.classifyCellReference(str, this._ssVersion) == CellReference.NameType.CELL;
        if (result) {
            boolean isFunc = FunctionMetadataRegistry.getFunctionByName(str.toUpperCase(Locale.ROOT)) != null;
            if (isFunc) {
                int savePointer = this._pointer;
                resetPointer(this._pointer + str.length());
                SkipWhite();
                result = this.look != 40;
                resetPointer(savePointer);
            }
        }
        return result;
    }

    private ParseNode function(String name) {
        Ptg nameToken = null;
        if (!AbstractFunctionPtg.isBuiltInFunctionName(name)) {
            if (this._book == null) {
                throw new IllegalStateException("Need book to evaluate name '" + name + "'");
            }
            EvaluationName hName = this._book.getName(name, this._sheetIndex);
            if (hName != null) {
                if (!hName.isFunctionName()) {
                    throw new FormulaParseException("Attempt to use name '" + name + "' as a function, but defined name in workbook does not refer to a function");
                }
                nameToken = hName.createPtg();
            } else {
                nameToken = this._book.getNameXPtg(name, null);
                if (nameToken == null) {
                    if (log.check(5)) {
                        log.log(5, "FormulaParser.function: Name '" + name + "' is completely unknown in the current workbook.");
                    }
                    switch (this._book.getSpreadsheetVersion()) {
                        case EXCEL97:
                            addName(name);
                            nameToken = this._book.getName(name, this._sheetIndex).createPtg();
                            break;
                        case EXCEL2007:
                            nameToken = new NameXPxg(name);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected spreadsheet version: " + this._book.getSpreadsheetVersion().name());
                    }
                }
            }
        }
        Match(40);
        ParseNode[] args = Arguments();
        Match(41);
        return getFunction(name, nameToken, args);
    }

    private void addName(String functionName) {
        Name name = this._book.createName();
        name.setFunction(true);
        name.setNameName(functionName);
        name.setSheetIndex(this._sheetIndex);
    }

    private ParseNode getFunction(String name, Ptg namePtg, ParseNode[] args) {
        AbstractFunctionPtg retval;
        FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByName(name.toUpperCase(Locale.ROOT));
        int numArgs = args.length;
        if (fm == null) {
            if (namePtg == null) {
                throw new IllegalStateException("NamePtg must be supplied for external functions");
            }
            ParseNode[] allArgs = new ParseNode[numArgs + 1];
            allArgs[0] = new ParseNode(namePtg);
            System.arraycopy(args, 0, allArgs, 1, numArgs);
            return new ParseNode(FuncVarPtg.create(name, numArgs + 1), allArgs);
        }
        if (namePtg != null) {
            throw new IllegalStateException("NamePtg no applicable to internal functions");
        }
        boolean isVarArgs = !fm.hasFixedArgsLength();
        int funcIx = fm.getIndex();
        if (funcIx == 4 && args.length == 1) {
            return new ParseNode(AttrPtg.getSumSingle(), args);
        }
        validateNumArgs(args.length, fm);
        if (isVarArgs) {
            retval = FuncVarPtg.create(name, numArgs);
        } else {
            retval = FuncPtg.create(funcIx);
        }
        return new ParseNode(retval, args);
    }

    private void validateNumArgs(int numArgs, FunctionMetadata fm) {
        int maxArgs;
        String msg;
        String msg2;
        if (numArgs < fm.getMinParams()) {
            String msg3 = "Too few arguments to function '" + fm.getName() + "'. ";
            if (fm.hasFixedArgsLength()) {
                msg2 = msg3 + "Expected " + fm.getMinParams();
            } else {
                msg2 = msg3 + "At least " + fm.getMinParams() + " were expected";
            }
            throw new FormulaParseException(msg2 + " but got " + numArgs + ".");
        }
        if (fm.hasUnlimitedVarags() && this._book != null) {
            maxArgs = this._book.getSpreadsheetVersion().getMaxFunctionArgs();
        } else {
            maxArgs = fm.getMaxParams();
        }
        if (numArgs > maxArgs) {
            String msg4 = "Too many arguments to function '" + fm.getName() + "'. ";
            if (fm.hasFixedArgsLength()) {
                msg = msg4 + "Expected " + maxArgs;
            } else {
                msg = msg4 + "At most " + maxArgs + " were expected";
            }
            throw new FormulaParseException(msg + " but got " + numArgs + ".");
        }
    }

    private static boolean isArgumentDelimiter(int ch2) {
        return ch2 == 44 || ch2 == 41;
    }

    private ParseNode[] Arguments() {
        List<ParseNode> temp = new ArrayList<>(2);
        SkipWhite();
        if (this.look == 41) {
            return ParseNode.EMPTY_ARRAY;
        }
        boolean missedPrevArg = true;
        while (true) {
            SkipWhite();
            if (isArgumentDelimiter(this.look)) {
                if (missedPrevArg) {
                    temp.add(new ParseNode(MissingArgPtg.instance));
                }
                if (this.look != 41) {
                    Match(44);
                    missedPrevArg = true;
                } else {
                    ParseNode[] result = new ParseNode[temp.size()];
                    temp.toArray(result);
                    return result;
                }
            } else {
                temp.add(comparisonExpression());
                missedPrevArg = false;
                SkipWhite();
                if (!isArgumentDelimiter(this.look)) {
                    throw expected("',' or ')'");
                }
            }
        }
    }

    private ParseNode powerFactor() {
        ParseNode parseNodePercentFactor = percentFactor();
        while (true) {
            ParseNode result = parseNodePercentFactor;
            SkipWhite();
            if (this.look != 94) {
                return result;
            }
            Match(94);
            ParseNode other = percentFactor();
            parseNodePercentFactor = new ParseNode(PowerPtg.instance, result, other);
        }
    }

    private ParseNode percentFactor() {
        ParseNode simpleFactor = parseSimpleFactor();
        while (true) {
            ParseNode result = simpleFactor;
            SkipWhite();
            if (this.look != 37) {
                return result;
            }
            Match(37);
            simpleFactor = new ParseNode(PercentPtg.instance, result);
        }
    }

    private ParseNode parseSimpleFactor() {
        SkipWhite();
        switch (this.look) {
            case 34:
                return new ParseNode(new StringPtg(parseStringLiteral()));
            case 35:
                return new ParseNode(ErrPtg.valueOf(parseErrorLiteral()));
            case 40:
                Match(40);
                ParseNode inside = unionExpression();
                Match(41);
                return new ParseNode(ParenthesisPtg.instance, inside);
            case 43:
                Match(43);
                return parseUnary(true);
            case 45:
                Match(45);
                return parseUnary(false);
            case 123:
                Match(123);
                ParseNode arrayNode = parseArray();
                Match(125);
                return arrayNode;
            default:
                if (IsAlpha(this.look) || Character.isDigit(this.look) || this.look == 39 || this.look == 91 || this.look == 95 || this.look == 92) {
                    return parseRangeExpression();
                }
                if (this.look == 46) {
                    return new ParseNode(parseNumber());
                }
                throw expected("cell ref or constant literal");
        }
    }

    private ParseNode parseUnary(boolean isPlus) {
        boolean numberFollows = IsDigit(this.look) || this.look == 46;
        ParseNode factor = powerFactor();
        if (numberFollows) {
            Ptg token = factor.getToken();
            if (token instanceof NumberPtg) {
                if (isPlus) {
                    return factor;
                }
                return new ParseNode(new NumberPtg(-((NumberPtg) token).getValue()));
            }
            if (token instanceof IntPtg) {
                if (isPlus) {
                    return factor;
                }
                int intVal = ((IntPtg) token).getValue();
                return new ParseNode(new NumberPtg(-intVal));
            }
        }
        return new ParseNode(isPlus ? UnaryPlusPtg.instance : UnaryMinusPtg.instance, factor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.lang.Object[], java.lang.Object[][]] */
    private ParseNode parseArray() {
        List<Object[]> rowsData = new ArrayList<>();
        while (true) {
            Object[] singleRowData = parseArrayRow();
            rowsData.add(singleRowData);
            if (this.look != 125) {
                if (this.look != 59) {
                    throw expected("'}' or ';'");
                }
                Match(59);
            } else {
                int nRows = rowsData.size();
                ?? r0 = new Object[nRows];
                rowsData.toArray((Object[]) r0);
                int nColumns = r0[0].length;
                checkRowLengths(r0, nColumns);
                return new ParseNode(new ArrayPtg(r0));
            }
        }
    }

    private void checkRowLengths(Object[][] values2d, int nColumns) {
        for (int i = 0; i < values2d.length; i++) {
            int rowLen = values2d[i].length;
            if (rowLen != nColumns) {
                throw new FormulaParseException("Array row " + i + " has length " + rowLen + " but row 0 has length " + nColumns);
            }
        }
    }

    private Object[] parseArrayRow() {
        List<Object> temp = new ArrayList<>();
        while (true) {
            temp.add(parseArrayItem());
            SkipWhite();
            switch (this.look) {
                case 44:
                    Match(44);
                case 59:
                case 125:
                    Object[] result = new Object[temp.size()];
                    temp.toArray(result);
                    return result;
                default:
                    throw expected("'}' or ','");
            }
        }
    }

    private Object parseArrayItem() {
        SkipWhite();
        switch (this.look) {
            case 34:
                return parseStringLiteral();
            case 35:
                return ErrorConstant.valueOf(parseErrorLiteral());
            case 45:
                Match(45);
                SkipWhite();
                return convertArrayNumber(parseNumber(), false);
            case 70:
            case 84:
            case 102:
            case 116:
                return parseBooleanLiteral();
            default:
                return convertArrayNumber(parseNumber(), true);
        }
    }

    private Boolean parseBooleanLiteral() {
        String iden = parseUnquotedIdentifier();
        if ("TRUE".equalsIgnoreCase(iden)) {
            return Boolean.TRUE;
        }
        if ("FALSE".equalsIgnoreCase(iden)) {
            return Boolean.FALSE;
        }
        throw expected("'TRUE' or 'FALSE'");
    }

    private static Double convertArrayNumber(Ptg ptg, boolean isPositive) {
        double value;
        if (ptg instanceof IntPtg) {
            value = ((IntPtg) ptg).getValue();
        } else if (ptg instanceof NumberPtg) {
            value = ((NumberPtg) ptg).getValue();
        } else {
            throw new RuntimeException("Unexpected ptg (" + ptg.getClass().getName() + ")");
        }
        if (!isPositive) {
            value = -value;
        }
        return new Double(value);
    }

    private Ptg parseNumber() {
        String number2 = null;
        String exponent = null;
        String number1 = GetNum();
        if (this.look == 46) {
            GetChar();
            number2 = GetNum();
        }
        if (this.look == 69) {
            GetChar();
            String sign = "";
            if (this.look == 43) {
                GetChar();
            } else if (this.look == 45) {
                GetChar();
                sign = "-";
            }
            String number = GetNum();
            if (number == null) {
                throw expected("Integer");
            }
            exponent = sign + number;
        }
        if (number1 == null && number2 == null) {
            throw expected("Integer");
        }
        return getNumberPtgFromString(number1, number2, exponent);
    }

    private int parseErrorLiteral() {
        Match(35);
        String part1 = parseUnquotedIdentifier().toUpperCase(Locale.ROOT);
        if (part1 == null) {
            throw expected("remainder of error constant literal");
        }
        switch (part1.charAt(0)) {
            case 'D':
                FormulaError fe = FormulaError.DIV0;
                if (part1.equals("DIV")) {
                    Match(47);
                    Match(48);
                    Match(33);
                    return fe.getCode();
                }
                throw expected(fe.getString());
            case 'N':
                FormulaError fe2 = FormulaError.NAME;
                if (part1.equals(fe2.name())) {
                    Match(63);
                    return fe2.getCode();
                }
                FormulaError fe3 = FormulaError.NUM;
                if (part1.equals(fe3.name())) {
                    Match(33);
                    return fe3.getCode();
                }
                FormulaError fe4 = FormulaError.NULL;
                if (part1.equals(fe4.name())) {
                    Match(33);
                    return fe4.getCode();
                }
                FormulaError fe5 = FormulaError.NA;
                if (part1.equals("N")) {
                    Match(47);
                    if (this.look != 65 && this.look != 97) {
                        throw expected(fe5.getString());
                    }
                    Match(this.look);
                    return fe5.getCode();
                }
                throw expected("#NAME?, #NUM!, #NULL! or #N/A");
            case 'R':
                FormulaError fe6 = FormulaError.REF;
                if (part1.equals(fe6.name())) {
                    Match(33);
                    return fe6.getCode();
                }
                throw expected(fe6.getString());
            case 'V':
                FormulaError fe7 = FormulaError.VALUE;
                if (part1.equals(fe7.name())) {
                    Match(33);
                    return fe7.getCode();
                }
                throw expected(fe7.getString());
            default:
                throw expected("#VALUE!, #REF!, #DIV/0!, #NAME?, #NUM!, #NULL! or #N/A");
        }
    }

    private String parseUnquotedIdentifier() {
        if (this.look == 39) {
            throw expected("unquoted identifier");
        }
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (!Character.isLetterOrDigit(this.look) && this.look != 46) {
                break;
            }
            sb.appendCodePoint(this.look);
            GetChar();
        }
        if (sb.length() < 1) {
            return null;
        }
        return sb.toString();
    }

    private static Ptg getNumberPtgFromString(String number1, String number2, String exponent) throws NumberFormatException {
        StringBuilder number = new StringBuilder();
        if (number2 == null) {
            number.append(number1);
            if (exponent != null) {
                number.append('E');
                number.append(exponent);
            }
            String numberStr = number.toString();
            try {
                int intVal = Integer.parseInt(numberStr);
                if (IntPtg.isInRange(intVal)) {
                    return new IntPtg(intVal);
                }
                return new NumberPtg(numberStr);
            } catch (NumberFormatException e) {
                return new NumberPtg(numberStr);
            }
        }
        if (number1 != null) {
            number.append(number1);
        }
        number.append('.');
        number.append(number2);
        if (exponent != null) {
            number.append('E');
            number.append(exponent);
        }
        return new NumberPtg(number.toString());
    }

    private String parseStringLiteral() {
        Match(34);
        StringBuilder token = new StringBuilder();
        while (true) {
            if (this.look == 34) {
                GetChar();
                if (this.look != 34) {
                    return token.toString();
                }
            }
            token.appendCodePoint(this.look);
            GetChar();
        }
    }

    private ParseNode Term() {
        Ptg ptg;
        ParseNode parseNodePowerFactor = powerFactor();
        while (true) {
            ParseNode result = parseNodePowerFactor;
            SkipWhite();
            switch (this.look) {
                case 42:
                    Match(42);
                    ptg = MultiplyPtg.instance;
                    break;
                case 47:
                    Match(47);
                    ptg = DividePtg.instance;
                    break;
                default:
                    return result;
            }
            Ptg operator = ptg;
            ParseNode other = powerFactor();
            parseNodePowerFactor = new ParseNode(operator, result, other);
        }
    }

    private ParseNode unionExpression() {
        ParseNode result = intersectionExpression();
        boolean hasUnions = false;
        while (true) {
            SkipWhite();
            switch (this.look) {
                case 44:
                    GetChar();
                    hasUnions = true;
                    ParseNode other = intersectionExpression();
                    result = new ParseNode(UnionPtg.instance, result, other);
                default:
                    if (hasUnions) {
                        return augmentWithMemPtg(result);
                    }
                    return result;
            }
        }
    }

    private ParseNode intersectionExpression() {
        ParseNode result = comparisonExpression();
        boolean hasIntersections = false;
        while (true) {
            SkipWhite();
            if (!this._inIntersection) {
                break;
            }
            int savePointer = this._pointer;
            try {
                ParseNode other = comparisonExpression();
                result = new ParseNode(IntersectionPtg.instance, result, other);
                hasIntersections = true;
            } catch (FormulaParseException e) {
                resetPointer(savePointer);
            }
        }
        if (hasIntersections) {
            return augmentWithMemPtg(result);
        }
        return result;
    }

    private ParseNode comparisonExpression() {
        ParseNode parseNodeConcatExpression = concatExpression();
        while (true) {
            ParseNode result = parseNodeConcatExpression;
            SkipWhite();
            switch (this.look) {
                case 60:
                case 61:
                case 62:
                    Ptg comparisonToken = getComparisonToken();
                    ParseNode other = concatExpression();
                    parseNodeConcatExpression = new ParseNode(comparisonToken, result, other);
                default:
                    return result;
            }
        }
    }

    private Ptg getComparisonToken() {
        if (this.look == 61) {
            Match(this.look);
            return EqualPtg.instance;
        }
        boolean isGreater = this.look == 62;
        Match(this.look);
        if (isGreater) {
            if (this.look == 61) {
                Match(61);
                return GreaterEqualPtg.instance;
            }
            return GreaterThanPtg.instance;
        }
        switch (this.look) {
            case 61:
                Match(61);
                return LessEqualPtg.instance;
            case 62:
                Match(62);
                return NotEqualPtg.instance;
            default:
                return LessThanPtg.instance;
        }
    }

    private ParseNode concatExpression() {
        ParseNode parseNodeAdditiveExpression = additiveExpression();
        while (true) {
            ParseNode result = parseNodeAdditiveExpression;
            SkipWhite();
            if (this.look == 38) {
                Match(38);
                ParseNode other = additiveExpression();
                parseNodeAdditiveExpression = new ParseNode(ConcatPtg.instance, result, other);
            } else {
                return result;
            }
        }
    }

    private ParseNode additiveExpression() {
        Ptg ptg;
        ParseNode parseNodeTerm = Term();
        while (true) {
            ParseNode result = parseNodeTerm;
            SkipWhite();
            switch (this.look) {
                case 43:
                    Match(43);
                    ptg = AddPtg.instance;
                    break;
                case 45:
                    Match(45);
                    ptg = SubtractPtg.instance;
                    break;
                default:
                    return result;
            }
            Ptg operator = ptg;
            ParseNode other = Term();
            parseNodeTerm = new ParseNode(operator, result, other);
        }
    }

    private void parse() {
        this._pointer = 0;
        GetChar();
        this._rootNode = unionExpression();
        if (this._pointer <= this._formulaLength) {
            String msg = "Unused input [" + this._formulaString.substring(this._pointer - 1) + "] after attempting to parse the formula [" + this._formulaString + "]";
            throw new FormulaParseException(msg);
        }
    }

    private Ptg[] getRPNPtg(FormulaType formulaType) {
        OperandClassTransformer oct = new OperandClassTransformer(formulaType);
        oct.transformFormula(this._rootNode);
        return ParseNode.toTokenArray(this._rootNode);
    }
}
