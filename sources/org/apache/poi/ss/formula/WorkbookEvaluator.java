package org.apache.poi.ss.formula;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TreeSet;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment;
import org.apache.poi.ss.formula.atp.AnalysisToolPak;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.ExternalNameEval;
import org.apache.poi.ss.formula.eval.FunctionEval;
import org.apache.poi.ss.formula.eval.FunctionNameEval;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.functions.Function;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.Area3DPxg;
import org.apache.poi.ss.formula.ptg.AreaErrPtg;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.BoolPtg;
import org.apache.poi.ss.formula.ptg.DeletedArea3DPtg;
import org.apache.poi.ss.formula.ptg.DeletedRef3DPtg;
import org.apache.poi.ss.formula.ptg.ErrPtg;
import org.apache.poi.ss.formula.ptg.ExpPtg;
import org.apache.poi.ss.formula.ptg.IntPtg;
import org.apache.poi.ss.formula.ptg.MissingArgPtg;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.NameXPxg;
import org.apache.poi.ss.formula.ptg.NumberPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.ptg.Ref3DPxg;
import org.apache.poi.ss.formula.ptg.RefErrorPtg;
import org.apache.poi.ss.formula.ptg.RefPtg;
import org.apache.poi.ss.formula.ptg.RefPtgBase;
import org.apache.poi.ss.formula.ptg.StringPtg;
import org.apache.poi.ss.formula.ptg.UnknownPtg;
import org.apache.poi.ss.formula.udf.AggregatingUDFFinder;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddressBase;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/WorkbookEvaluator.class */
public final class WorkbookEvaluator {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) WorkbookEvaluator.class);
    private final EvaluationWorkbook _workbook;
    private EvaluationCache _cache;
    private int _workbookIx;
    private final IEvaluationListener _evaluationListener;
    private final Map<EvaluationSheet, Integer> _sheetIndexesBySheet;
    private final Map<String, Integer> _sheetIndexesByName;
    private CollaboratingWorkbooksEnvironment _collaboratingWorkbookEnvironment;
    private final IStabilityClassifier _stabilityClassifier;
    private final AggregatingUDFFinder _udfFinder;
    private boolean _ignoreMissingWorkbooks;
    private boolean dbgEvaluationOutputForNextEval;
    private final POILogger EVAL_LOG;
    private int dbgEvaluationOutputIndent;

    public WorkbookEvaluator(EvaluationWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        this(workbook, null, stabilityClassifier, udfFinder);
    }

    WorkbookEvaluator(EvaluationWorkbook workbook, IEvaluationListener evaluationListener, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        this.EVAL_LOG = POILogFactory.getLogger("POI.FormulaEval");
        this.dbgEvaluationOutputIndent = -1;
        this._workbook = workbook;
        this._evaluationListener = evaluationListener;
        this._cache = new EvaluationCache(evaluationListener);
        this._sheetIndexesBySheet = new IdentityHashMap();
        this._sheetIndexesByName = new IdentityHashMap();
        this._collaboratingWorkbookEnvironment = CollaboratingWorkbooksEnvironment.EMPTY;
        this._workbookIx = 0;
        this._stabilityClassifier = stabilityClassifier;
        AggregatingUDFFinder defaultToolkit = workbook == null ? null : (AggregatingUDFFinder) workbook.getUDFFinder();
        if (defaultToolkit != null && udfFinder != null) {
            defaultToolkit.add(udfFinder);
        }
        this._udfFinder = defaultToolkit;
    }

    String getSheetName(int sheetIndex) {
        return this._workbook.getSheetName(sheetIndex);
    }

    EvaluationSheet getSheet(int sheetIndex) {
        return this._workbook.getSheet(sheetIndex);
    }

    EvaluationWorkbook getWorkbook() {
        return this._workbook;
    }

    EvaluationName getName(String name, int sheetIndex) {
        EvaluationName evalName = this._workbook.getName(name, sheetIndex);
        return evalName;
    }

    private static boolean isDebugLogEnabled() {
        return LOG.check(1);
    }

    private static boolean isInfoLogEnabled() {
        return LOG.check(3);
    }

    private static void logDebug(String s) {
        if (isDebugLogEnabled()) {
            LOG.log(1, s);
        }
    }

    private static void logInfo(String s) {
        if (isInfoLogEnabled()) {
            LOG.log(3, s);
        }
    }

    void attachToEnvironment(CollaboratingWorkbooksEnvironment collaboratingWorkbooksEnvironment, EvaluationCache cache, int workbookIx) {
        this._collaboratingWorkbookEnvironment = collaboratingWorkbooksEnvironment;
        this._cache = cache;
        this._workbookIx = workbookIx;
    }

    CollaboratingWorkbooksEnvironment getEnvironment() {
        return this._collaboratingWorkbookEnvironment;
    }

    void detachFromEnvironment() {
        this._collaboratingWorkbookEnvironment = CollaboratingWorkbooksEnvironment.EMPTY;
        this._cache = new EvaluationCache(this._evaluationListener);
        this._workbookIx = 0;
    }

    WorkbookEvaluator getOtherWorkbookEvaluator(String workbookName) throws CollaboratingWorkbooksEnvironment.WorkbookNotFoundException {
        return this._collaboratingWorkbookEnvironment.getWorkbookEvaluator(workbookName);
    }

    IEvaluationListener getEvaluationListener() {
        return this._evaluationListener;
    }

    public void clearAllCachedResultValues() {
        this._cache.clear();
        this._sheetIndexesBySheet.clear();
        this._workbook.clearAllCachedResultValues();
    }

    public void notifyUpdateCell(EvaluationCell cell) {
        int sheetIndex = getSheetIndex(cell.getSheet());
        this._cache.notifyUpdateCell(this._workbookIx, sheetIndex, cell);
    }

    public void notifyDeleteCell(EvaluationCell cell) {
        int sheetIndex = getSheetIndex(cell.getSheet());
        this._cache.notifyDeleteCell(this._workbookIx, sheetIndex, cell);
    }

    private int getSheetIndex(EvaluationSheet sheet) {
        Integer result = this._sheetIndexesBySheet.get(sheet);
        if (result == null) {
            int sheetIndex = this._workbook.getSheetIndex(sheet);
            if (sheetIndex < 0) {
                throw new RuntimeException("Specified sheet from a different book");
            }
            result = Integer.valueOf(sheetIndex);
            this._sheetIndexesBySheet.put(sheet, result);
        }
        return result.intValue();
    }

    public ValueEval evaluate(EvaluationCell srcCell) {
        int sheetIndex = getSheetIndex(srcCell.getSheet());
        return evaluateAny(srcCell, sheetIndex, srcCell.getRowIndex(), srcCell.getColumnIndex(), new EvaluationTracker(this._cache));
    }

    int getSheetIndex(String sheetName) {
        Integer result = this._sheetIndexesByName.get(sheetName);
        if (result == null) {
            int sheetIndex = this._workbook.getSheetIndex(sheetName);
            if (sheetIndex < 0) {
                return -1;
            }
            result = Integer.valueOf(sheetIndex);
            this._sheetIndexesByName.put(sheetName, result);
        }
        return result.intValue();
    }

    int getSheetIndexByExternIndex(int externSheetIndex) {
        return this._workbook.convertFromExternSheetIndex(externSheetIndex);
    }

    private ValueEval evaluateAny(EvaluationCell srcCell, int sheetIndex, int rowIndex, int columnIndex, EvaluationTracker tracker) {
        ValueEval result;
        boolean z = this._stabilityClassifier == null || !this._stabilityClassifier.isCellFinal(sheetIndex, rowIndex, columnIndex);
        boolean shouldCellDependencyBeRecorded = z;
        if (srcCell == null || srcCell.getCellTypeEnum() != CellType.FORMULA) {
            ValueEval result2 = getValueFromNonFormulaCell(srcCell);
            if (shouldCellDependencyBeRecorded) {
                tracker.acceptPlainValueDependency(this._workbookIx, sheetIndex, rowIndex, columnIndex, result2);
            }
            return result2;
        }
        FormulaCellCacheEntry cce = this._cache.getOrCreateFormulaCellEntry(srcCell);
        if (shouldCellDependencyBeRecorded || cce.isInputSensitive()) {
            tracker.acceptFormulaDependency(cce);
        }
        IEvaluationListener evalListener = this._evaluationListener;
        if (cce.getValue() == null) {
            if (!tracker.startEvaluate(cce)) {
                return ErrorEval.CIRCULAR_REF_ERROR;
            }
            OperationEvaluationContext ec = new OperationEvaluationContext(this, this._workbook, sheetIndex, rowIndex, columnIndex, tracker);
            try {
                try {
                    Ptg[] ptgs = this._workbook.getFormulaTokens(srcCell);
                    if (evalListener == null) {
                        result = evaluateFormula(ec, ptgs);
                    } else {
                        evalListener.onStartEvaluate(srcCell, cce);
                        result = evaluateFormula(ec, ptgs);
                        evalListener.onEndEvaluate(cce, result);
                    }
                    tracker.updateCacheResult(result);
                    tracker.endEvaluate(cce);
                } catch (NotImplementedException e) {
                    throw addExceptionInfo(e, sheetIndex, rowIndex, columnIndex);
                } catch (RuntimeException re) {
                    if ((re.getCause() instanceof CollaboratingWorkbooksEnvironment.WorkbookNotFoundException) && this._ignoreMissingWorkbooks) {
                        logInfo(re.getCause().getMessage() + " - Continuing with cached value!");
                        switch (srcCell.getCachedFormulaResultTypeEnum()) {
                            case NUMERIC:
                                result = new NumberEval(srcCell.getNumericCellValue());
                                break;
                            case STRING:
                                result = new StringEval(srcCell.getStringCellValue());
                                break;
                            case BLANK:
                                result = BlankEval.instance;
                                break;
                            case BOOLEAN:
                                result = BoolEval.valueOf(srcCell.getBooleanCellValue());
                                break;
                            case ERROR:
                                result = ErrorEval.valueOf(srcCell.getErrorCellValue());
                                break;
                            case FORMULA:
                            default:
                                throw new RuntimeException("Unexpected cell type '" + srcCell.getCellTypeEnum() + "' found!");
                        }
                        tracker.endEvaluate(cce);
                    } else {
                        throw re;
                    }
                }
                if (isDebugLogEnabled()) {
                    String sheetName = getSheetName(sheetIndex);
                    CellReference cr = new CellReference(rowIndex, columnIndex);
                    logDebug("Evaluated " + sheetName + "!" + cr.formatAsString() + " to " + result);
                }
                return result;
            } catch (Throwable th) {
                tracker.endEvaluate(cce);
                throw th;
            }
        }
        if (evalListener != null) {
            evalListener.onCacheHit(sheetIndex, rowIndex, columnIndex, cce.getValue());
        }
        return cce.getValue();
    }

    private NotImplementedException addExceptionInfo(NotImplementedException inner, int sheetIndex, int rowIndex, int columnIndex) {
        try {
            String sheetName = this._workbook.getSheetName(sheetIndex);
            CellReference cr = new CellReference(sheetName, rowIndex, columnIndex, false, false);
            String msg = "Error evaluating cell " + cr.formatAsString();
            return new NotImplementedException(msg, inner);
        } catch (Exception e) {
            LOG.log(7, "Can't add exception info", e);
            return inner;
        }
    }

    static ValueEval getValueFromNonFormulaCell(EvaluationCell cell) {
        if (cell == null) {
            return BlankEval.instance;
        }
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
            case NUMERIC:
                return new NumberEval(cell.getNumericCellValue());
            case STRING:
                return new StringEval(cell.getStringCellValue());
            case BLANK:
                return BlankEval.instance;
            case BOOLEAN:
                return BoolEval.valueOf(cell.getBooleanCellValue());
            case ERROR:
                return ErrorEval.valueOf(cell.getErrorCellValue());
            default:
                throw new RuntimeException("Unexpected cell type (" + cellType + ")");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x028e A[PHI: r18
  0x028e: PHI (r18v1 'ptg' org.apache.poi.ss.formula.ptg.Ptg) = (r18v0 'ptg' org.apache.poi.ss.formula.ptg.Ptg), (r18v2 'ptg' org.apache.poi.ss.formula.ptg.Ptg) binds: [B:15:0x00f8, B:50:0x0258] A[DONT_GENERATE, DONT_INLINE]] */
    @org.apache.poi.util.Internal
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    org.apache.poi.ss.formula.eval.ValueEval evaluateFormula(org.apache.poi.ss.formula.OperationEvaluationContext r12, org.apache.poi.ss.formula.ptg.Ptg[] r13) {
        /*
            Method dump skipped, instructions count: 1058
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ss.formula.WorkbookEvaluator.evaluateFormula(org.apache.poi.ss.formula.OperationEvaluationContext, org.apache.poi.ss.formula.ptg.Ptg[]):org.apache.poi.ss.formula.eval.ValueEval");
    }

    private static int countTokensToBeSkipped(Ptg[] ptgs, int startIndex, int distInBytes) {
        int remBytes = distInBytes;
        int index = startIndex;
        while (remBytes != 0) {
            index++;
            remBytes -= ptgs[index].getSize();
            if (remBytes < 0) {
                throw new RuntimeException("Bad skip distance (wrong token size calculation).");
            }
            if (index >= ptgs.length) {
                throw new RuntimeException("Skip distance too far (ran out of formula tokens).");
            }
        }
        return index - startIndex;
    }

    public static ValueEval dereferenceResult(ValueEval evaluationResult, int srcRowNum, int srcColNum) {
        try {
            ValueEval value = OperandResolver.getSingleValue(evaluationResult, srcRowNum, srcColNum);
            if (value == BlankEval.instance) {
                return NumberEval.ZERO;
            }
            return value;
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private ValueEval getEvalForPtg(Ptg ptg, OperationEvaluationContext ec) {
        if (ptg instanceof NamePtg) {
            NamePtg namePtg = (NamePtg) ptg;
            EvaluationName nameRecord = this._workbook.getName(namePtg);
            return getEvalForNameRecord(nameRecord, ec);
        }
        if (ptg instanceof NameXPtg) {
            return processNameEval(ec.getNameXEval((NameXPtg) ptg), ec);
        }
        if (ptg instanceof NameXPxg) {
            return processNameEval(ec.getNameXEval((NameXPxg) ptg), ec);
        }
        if (ptg instanceof IntPtg) {
            return new NumberEval(((IntPtg) ptg).getValue());
        }
        if (ptg instanceof NumberPtg) {
            return new NumberEval(((NumberPtg) ptg).getValue());
        }
        if (ptg instanceof StringPtg) {
            return new StringEval(((StringPtg) ptg).getValue());
        }
        if (ptg instanceof BoolPtg) {
            return BoolEval.valueOf(((BoolPtg) ptg).getValue());
        }
        if (ptg instanceof ErrPtg) {
            return ErrorEval.valueOf(((ErrPtg) ptg).getErrorCode());
        }
        if (ptg instanceof MissingArgPtg) {
            return MissingArgEval.instance;
        }
        if ((ptg instanceof AreaErrPtg) || (ptg instanceof RefErrorPtg) || (ptg instanceof DeletedArea3DPtg) || (ptg instanceof DeletedRef3DPtg)) {
            return ErrorEval.REF_INVALID;
        }
        if (ptg instanceof Ref3DPtg) {
            return ec.getRef3DEval((Ref3DPtg) ptg);
        }
        if (ptg instanceof Ref3DPxg) {
            return ec.getRef3DEval((Ref3DPxg) ptg);
        }
        if (ptg instanceof Area3DPtg) {
            return ec.getArea3DEval((Area3DPtg) ptg);
        }
        if (ptg instanceof Area3DPxg) {
            return ec.getArea3DEval((Area3DPxg) ptg);
        }
        if (ptg instanceof RefPtg) {
            RefPtg rptg = (RefPtg) ptg;
            return ec.getRefEval(rptg.getRow(), rptg.getColumn());
        }
        if (ptg instanceof AreaPtg) {
            AreaPtg aptg = (AreaPtg) ptg;
            return ec.getAreaEval(aptg.getFirstRow(), aptg.getFirstColumn(), aptg.getLastRow(), aptg.getLastColumn());
        }
        if (ptg instanceof UnknownPtg) {
            throw new RuntimeException("UnknownPtg not allowed");
        }
        if (ptg instanceof ExpPtg) {
            throw new RuntimeException("ExpPtg currently not supported");
        }
        throw new RuntimeException("Unexpected ptg class (" + ptg.getClass().getName() + ")");
    }

    private ValueEval processNameEval(ValueEval eval, OperationEvaluationContext ec) {
        if (eval instanceof ExternalNameEval) {
            EvaluationName name = ((ExternalNameEval) eval).getName();
            return getEvalForNameRecord(name, ec);
        }
        return eval;
    }

    private ValueEval getEvalForNameRecord(EvaluationName nameRecord, OperationEvaluationContext ec) {
        if (nameRecord.isFunctionName()) {
            return new FunctionNameEval(nameRecord.getNameText());
        }
        if (nameRecord.hasFormula()) {
            return evaluateNameFormula(nameRecord.getNameDefinition(), ec);
        }
        throw new RuntimeException("Don't now how to evalate name '" + nameRecord.getNameText() + "'");
    }

    ValueEval evaluateNameFormula(Ptg[] ptgs, OperationEvaluationContext ec) {
        if (ptgs.length == 1) {
            return getEvalForPtg(ptgs[0], ec);
        }
        return evaluateFormula(ec, ptgs);
    }

    ValueEval evaluateReference(EvaluationSheet sheet, int sheetIndex, int rowIndex, int columnIndex, EvaluationTracker tracker) {
        EvaluationCell cell = sheet.getCell(rowIndex, columnIndex);
        return evaluateAny(cell, sheetIndex, rowIndex, columnIndex, tracker);
    }

    public FreeRefFunction findUserDefinedFunction(String functionName) {
        return this._udfFinder.findFunction(functionName);
    }

    public ValueEval evaluate(String formula, CellReference ref) {
        int sheetIndex;
        String sheetName = ref == null ? null : ref.getSheetName();
        if (sheetName == null) {
            sheetIndex = -1;
        } else {
            sheetIndex = getWorkbook().getSheetIndex(sheetName);
        }
        int rowIndex = ref == null ? -1 : ref.getRow();
        short colIndex = ref == null ? (short) -1 : ref.getCol();
        OperationEvaluationContext ec = new OperationEvaluationContext(this, getWorkbook(), sheetIndex, rowIndex, colIndex, new EvaluationTracker(this._cache));
        Ptg[] ptgs = FormulaParser.parse(formula, (FormulaParsingWorkbook) getWorkbook(), FormulaType.CELL, sheetIndex, rowIndex);
        return evaluateNameFormula(ptgs, ec);
    }

    public ValueEval evaluate(String formula, CellReference target, CellRangeAddressBase region) {
        return evaluate(formula, target, region, FormulaType.CELL);
    }

    public ValueEval evaluateList(String formula, CellReference target, CellRangeAddressBase region) {
        return evaluate(formula, target, region, FormulaType.DATAVALIDATION_LIST);
    }

    private ValueEval evaluate(String formula, CellReference target, CellRangeAddressBase region, FormulaType formulaType) {
        String sheetName = target == null ? null : target.getSheetName();
        if (sheetName == null) {
            throw new IllegalArgumentException("Sheet name is required");
        }
        int sheetIndex = getWorkbook().getSheetIndex(sheetName);
        Ptg[] ptgs = FormulaParser.parse(formula, (FormulaParsingWorkbook) getWorkbook(), formulaType, sheetIndex, target.getRow());
        adjustRegionRelativeReference(ptgs, target, region);
        OperationEvaluationContext ec = new OperationEvaluationContext(this, getWorkbook(), sheetIndex, target.getRow(), target.getCol(), new EvaluationTracker(this._cache), formulaType.isSingleValue());
        return evaluateNameFormula(ptgs, ec);
    }

    protected boolean adjustRegionRelativeReference(Ptg[] ptgs, CellReference target, CellRangeAddressBase region) {
        if (!region.isInRange(target)) {
            throw new IllegalArgumentException(target + " is not within " + region);
        }
        return adjustRegionRelativeReference(ptgs, target.getRow() - region.getFirstRow(), target.getCol() - region.getFirstColumn());
    }

    protected boolean adjustRegionRelativeReference(Ptg[] ptgs, int deltaRow, int deltaColumn) {
        if (deltaRow < 0) {
            throw new IllegalArgumentException("offset row must be positive");
        }
        if (deltaColumn < 0) {
            throw new IllegalArgumentException("offset column must be positive");
        }
        boolean shifted = false;
        for (Ptg ptg : ptgs) {
            if (ptg instanceof RefPtgBase) {
                RefPtgBase ref = (RefPtgBase) ptg;
                SpreadsheetVersion version = this._workbook.getSpreadsheetVersion();
                if (ref.isRowRelative()) {
                    int rowIndex = ref.getRow() + deltaRow;
                    if (rowIndex > version.getMaxRows()) {
                        throw new IndexOutOfBoundsException(version.name() + " files can only have " + version.getMaxRows() + " rows, but row " + rowIndex + " was requested.");
                    }
                    ref.setRow(rowIndex);
                    shifted = true;
                }
                if (ref.isColRelative()) {
                    int colIndex = ref.getColumn() + deltaColumn;
                    if (colIndex > version.getMaxColumns()) {
                        throw new IndexOutOfBoundsException(version.name() + " files can only have " + version.getMaxColumns() + " columns, but column " + colIndex + " was requested.");
                    }
                    ref.setColumn(colIndex);
                    shifted = true;
                } else {
                    continue;
                }
            }
        }
        return shifted;
    }

    public void setIgnoreMissingWorkbooks(boolean ignore) {
        this._ignoreMissingWorkbooks = ignore;
    }

    public boolean isIgnoreMissingWorkbooks() {
        return this._ignoreMissingWorkbooks;
    }

    public static Collection<String> getSupportedFunctionNames() {
        Collection<String> lst = new TreeSet<>();
        lst.addAll(FunctionEval.getSupportedFunctionNames());
        lst.addAll(AnalysisToolPak.getSupportedFunctionNames());
        return Collections.unmodifiableCollection(lst);
    }

    public static Collection<String> getNotSupportedFunctionNames() {
        Collection<String> lst = new TreeSet<>();
        lst.addAll(FunctionEval.getNotSupportedFunctionNames());
        lst.addAll(AnalysisToolPak.getNotSupportedFunctionNames());
        return Collections.unmodifiableCollection(lst);
    }

    public static void registerFunction(String name, FreeRefFunction func) {
        AnalysisToolPak.registerFunction(name, func);
    }

    public static void registerFunction(String name, Function func) {
        FunctionEval.registerFunction(name, func);
    }

    public void setDebugEvaluationOutputForNextEval(boolean value) {
        this.dbgEvaluationOutputForNextEval = value;
    }

    public boolean isDebugEvaluationOutputForNextEval() {
        return this.dbgEvaluationOutputForNextEval;
    }
}
