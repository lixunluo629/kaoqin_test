package org.apache.poi.ss.formula;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.poi.ss.formula.FormulaUsedBlankCellSet;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/FormulaCellCacheEntry.class */
final class FormulaCellCacheEntry extends CellCacheEntry {
    private CellCacheEntry[] _sensitiveInputCells;
    private FormulaUsedBlankCellSet _usedBlankCellGroup;

    public boolean isInputSensitive() {
        if (this._sensitiveInputCells == null || this._sensitiveInputCells.length <= 0) {
            return (this._usedBlankCellGroup == null || this._usedBlankCellGroup.isEmpty()) ? false : true;
        }
        return true;
    }

    public void setSensitiveInputCells(CellCacheEntry[] sensitiveInputCells) {
        if (sensitiveInputCells == null) {
            this._sensitiveInputCells = null;
            changeConsumingCells(CellCacheEntry.EMPTY_ARRAY);
        } else {
            this._sensitiveInputCells = (CellCacheEntry[]) sensitiveInputCells.clone();
            changeConsumingCells(this._sensitiveInputCells);
        }
    }

    public void clearFormulaEntry() {
        CellCacheEntry[] usedCells = this._sensitiveInputCells;
        if (usedCells != null) {
            for (int i = usedCells.length - 1; i >= 0; i--) {
                usedCells[i].clearConsumingCell(this);
            }
        }
        this._sensitiveInputCells = null;
        clearValue();
    }

    private void changeConsumingCells(CellCacheEntry[] usedCells) {
        Set<CellCacheEntry> usedSet;
        CellCacheEntry[] prevUsedCells = this._sensitiveInputCells;
        int nUsed = usedCells.length;
        for (CellCacheEntry cellCacheEntry : usedCells) {
            cellCacheEntry.addConsumingCell(this);
        }
        if (prevUsedCells == null || (prevUsedCells.length) < 1) {
            return;
        }
        if (nUsed < 1) {
            usedSet = Collections.emptySet();
        } else {
            usedSet = new HashSet<>((nUsed * 3) / 2);
            for (CellCacheEntry cellCacheEntry2 : usedCells) {
                usedSet.add(cellCacheEntry2);
            }
        }
        for (CellCacheEntry prevUsed : prevUsedCells) {
            if (!usedSet.contains(prevUsed)) {
                prevUsed.clearConsumingCell(this);
            }
        }
    }

    public void updateFormulaResult(ValueEval result, CellCacheEntry[] sensitiveInputCells, FormulaUsedBlankCellSet usedBlankAreas) {
        updateValue(result);
        setSensitiveInputCells(sensitiveInputCells);
        this._usedBlankCellGroup = usedBlankAreas;
    }

    public void notifyUpdatedBlankCell(FormulaUsedBlankCellSet.BookSheetKey bsk, int rowIndex, int columnIndex, IEvaluationListener evaluationListener) {
        if (this._usedBlankCellGroup != null && this._usedBlankCellGroup.containsCell(bsk, rowIndex, columnIndex)) {
            clearFormulaEntry();
            recurseClearCachedFormulaResults(evaluationListener);
        }
    }
}
