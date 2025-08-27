package org.apache.poi.ss.formula.eval.forked;

import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/eval/forked/ForkedEvaluationCell.class */
final class ForkedEvaluationCell implements EvaluationCell {
    private final EvaluationSheet _sheet;
    private final EvaluationCell _masterCell;
    private boolean _booleanValue;
    private CellType _cellType;
    private int _errorValue;
    private double _numberValue;
    private String _stringValue;

    public ForkedEvaluationCell(ForkedEvaluationSheet sheet, EvaluationCell masterCell) {
        this._sheet = sheet;
        this._masterCell = masterCell;
        setValue(BlankEval.instance);
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public Object getIdentityKey() {
        return this._masterCell.getIdentityKey();
    }

    public void setValue(ValueEval value) {
        Class<?> cls = value.getClass();
        if (cls == NumberEval.class) {
            this._cellType = CellType.NUMERIC;
            this._numberValue = ((NumberEval) value).getNumberValue();
            return;
        }
        if (cls == StringEval.class) {
            this._cellType = CellType.STRING;
            this._stringValue = ((StringEval) value).getStringValue();
            return;
        }
        if (cls == BoolEval.class) {
            this._cellType = CellType.BOOLEAN;
            this._booleanValue = ((BoolEval) value).getBooleanValue();
        } else if (cls == ErrorEval.class) {
            this._cellType = CellType.ERROR;
            this._errorValue = ((ErrorEval) value).getErrorCode();
        } else {
            if (cls == BlankEval.class) {
                this._cellType = CellType.BLANK;
                return;
            }
            throw new IllegalArgumentException("Unexpected value class (" + cls.getName() + ")");
        }
    }

    public void copyValue(Cell destCell) {
        switch (this._cellType) {
            case BLANK:
                destCell.setCellType(CellType.BLANK);
                return;
            case NUMERIC:
                destCell.setCellValue(this._numberValue);
                return;
            case BOOLEAN:
                destCell.setCellValue(this._booleanValue);
                return;
            case STRING:
                destCell.setCellValue(this._stringValue);
                return;
            case ERROR:
                destCell.setCellErrorValue((byte) this._errorValue);
                return;
            default:
                throw new IllegalStateException("Unexpected data type (" + this._cellType + ")");
        }
    }

    private void checkCellType(CellType expectedCellType) {
        if (this._cellType != expectedCellType) {
            throw new RuntimeException("Wrong data type (" + this._cellType + ")");
        }
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public int getCellType() {
        return this._cellType.getCode();
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public CellType getCellTypeEnum() {
        return this._cellType;
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public boolean getBooleanCellValue() {
        checkCellType(CellType.BOOLEAN);
        return this._booleanValue;
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public int getErrorCellValue() {
        checkCellType(CellType.ERROR);
        return this._errorValue;
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public double getNumericCellValue() {
        checkCellType(CellType.NUMERIC);
        return this._numberValue;
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public String getStringCellValue() {
        checkCellType(CellType.STRING);
        return this._stringValue;
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public EvaluationSheet getSheet() {
        return this._sheet;
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public int getRowIndex() {
        return this._masterCell.getRowIndex();
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public int getColumnIndex() {
        return this._masterCell.getColumnIndex();
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public int getCachedFormulaResultType() {
        return this._masterCell.getCachedFormulaResultType();
    }

    @Override // org.apache.poi.ss.formula.EvaluationCell
    public CellType getCachedFormulaResultTypeEnum() {
        return this._masterCell.getCachedFormulaResultTypeEnum();
    }
}
