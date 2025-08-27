package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.FormulaCellCache;
import org.apache.poi.ss.formula.FormulaUsedBlankCellSet;
import org.apache.poi.ss.formula.PlainCellCache;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.CellType;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationCache.class */
final class EvaluationCache {
    private final PlainCellCache _plainCellCache = new PlainCellCache();
    private final FormulaCellCache _formulaCellCache = new FormulaCellCache();
    final IEvaluationListener _evaluationListener;

    EvaluationCache(IEvaluationListener evaluationListener) {
        this._evaluationListener = evaluationListener;
    }

    public void notifyUpdateCell(int bookIndex, int sheetIndex, EvaluationCell cell) {
        FormulaCellCacheEntry fcce = this._formulaCellCache.get(cell);
        int rowIndex = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();
        PlainCellCache.Loc loc = new PlainCellCache.Loc(bookIndex, sheetIndex, rowIndex, columnIndex);
        PlainValueCellCacheEntry pcce = this._plainCellCache.get(loc);
        if (cell.getCellTypeEnum() == CellType.FORMULA) {
            if (fcce == null) {
                FormulaCellCacheEntry fcce2 = new FormulaCellCacheEntry();
                if (pcce == null) {
                    if (this._evaluationListener != null) {
                        this._evaluationListener.onChangeFromBlankValue(sheetIndex, rowIndex, columnIndex, cell, fcce2);
                    }
                    updateAnyBlankReferencingFormulas(bookIndex, sheetIndex, rowIndex, columnIndex);
                }
                this._formulaCellCache.put(cell, fcce2);
            } else {
                fcce.recurseClearCachedFormulaResults(this._evaluationListener);
                fcce.clearFormulaEntry();
            }
            if (pcce != null) {
                pcce.recurseClearCachedFormulaResults(this._evaluationListener);
                this._plainCellCache.remove(loc);
                return;
            }
            return;
        }
        ValueEval value = WorkbookEvaluator.getValueFromNonFormulaCell(cell);
        if (pcce == null) {
            if (value != BlankEval.instance) {
                PlainValueCellCacheEntry pcce2 = new PlainValueCellCacheEntry(value);
                if (fcce == null) {
                    if (this._evaluationListener != null) {
                        this._evaluationListener.onChangeFromBlankValue(sheetIndex, rowIndex, columnIndex, cell, pcce2);
                    }
                    updateAnyBlankReferencingFormulas(bookIndex, sheetIndex, rowIndex, columnIndex);
                }
                this._plainCellCache.put(loc, pcce2);
            }
        } else {
            if (pcce.updateValue(value)) {
                pcce.recurseClearCachedFormulaResults(this._evaluationListener);
            }
            if (value == BlankEval.instance) {
                this._plainCellCache.remove(loc);
            }
        }
        if (fcce != null) {
            this._formulaCellCache.remove(cell);
            fcce.setSensitiveInputCells(null);
            fcce.recurseClearCachedFormulaResults(this._evaluationListener);
        }
    }

    private void updateAnyBlankReferencingFormulas(int bookIndex, int sheetIndex, final int rowIndex, final int columnIndex) {
        final FormulaUsedBlankCellSet.BookSheetKey bsk = new FormulaUsedBlankCellSet.BookSheetKey(bookIndex, sheetIndex);
        this._formulaCellCache.applyOperation(new FormulaCellCache.IEntryOperation() { // from class: org.apache.poi.ss.formula.EvaluationCache.1
            @Override // org.apache.poi.ss.formula.FormulaCellCache.IEntryOperation
            public void processEntry(FormulaCellCacheEntry entry) {
                entry.notifyUpdatedBlankCell(bsk, rowIndex, columnIndex, EvaluationCache.this._evaluationListener);
            }
        });
    }

    public PlainValueCellCacheEntry getPlainValueEntry(int bookIndex, int sheetIndex, int rowIndex, int columnIndex, ValueEval value) {
        PlainCellCache.Loc loc = new PlainCellCache.Loc(bookIndex, sheetIndex, rowIndex, columnIndex);
        PlainValueCellCacheEntry result = this._plainCellCache.get(loc);
        if (result == null) {
            result = new PlainValueCellCacheEntry(value);
            this._plainCellCache.put(loc, result);
            if (this._evaluationListener != null) {
                this._evaluationListener.onReadPlainValue(sheetIndex, rowIndex, columnIndex, result);
            }
        } else {
            if (!areValuesEqual(result.getValue(), value)) {
                throw new IllegalStateException("value changed");
            }
            if (this._evaluationListener != null) {
                this._evaluationListener.onCacheHit(sheetIndex, rowIndex, columnIndex, value);
            }
        }
        return result;
    }

    private boolean areValuesEqual(ValueEval a, ValueEval b) {
        Class<?> cls;
        if (a == null || (cls = a.getClass()) != b.getClass()) {
            return false;
        }
        if (a == BlankEval.instance) {
            return b == a;
        }
        if (cls == NumberEval.class) {
            return ((NumberEval) a).getNumberValue() == ((NumberEval) b).getNumberValue();
        }
        if (cls == StringEval.class) {
            return ((StringEval) a).getStringValue().equals(((StringEval) b).getStringValue());
        }
        if (cls == BoolEval.class) {
            return ((BoolEval) a).getBooleanValue() == ((BoolEval) b).getBooleanValue();
        }
        if (cls == ErrorEval.class) {
            return ((ErrorEval) a).getErrorCode() == ((ErrorEval) b).getErrorCode();
        }
        throw new IllegalStateException("Unexpected value class (" + cls.getName() + ")");
    }

    public FormulaCellCacheEntry getOrCreateFormulaCellEntry(EvaluationCell cell) {
        FormulaCellCacheEntry result = this._formulaCellCache.get(cell);
        if (result == null) {
            result = new FormulaCellCacheEntry();
            this._formulaCellCache.put(cell, result);
        }
        return result;
    }

    public void clear() {
        if (this._evaluationListener != null) {
            this._evaluationListener.onClearWholeCache();
        }
        this._plainCellCache.clear();
        this._formulaCellCache.clear();
    }

    public void notifyDeleteCell(int bookIndex, int sheetIndex, EvaluationCell cell) {
        if (cell.getCellTypeEnum() == CellType.FORMULA) {
            FormulaCellCacheEntry fcce = this._formulaCellCache.remove(cell);
            if (fcce != null) {
                fcce.setSensitiveInputCells(null);
                fcce.recurseClearCachedFormulaResults(this._evaluationListener);
                return;
            }
            return;
        }
        PlainCellCache.Loc loc = new PlainCellCache.Loc(bookIndex, sheetIndex, cell.getRowIndex(), cell.getColumnIndex());
        PlainValueCellCacheEntry pcce = this._plainCellCache.get(loc);
        if (pcce != null) {
            pcce.recurseClearCachedFormulaResults(this._evaluationListener);
        }
    }
}
