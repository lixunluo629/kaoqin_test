package org.apache.poi.xssf.usermodel.helpers;

import java.util.List;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaRenderer;
import org.apache.poi.ss.formula.FormulaShifter;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.AreaErrPtg;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.helpers.RowShifter;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellFormula;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellFormulaType;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/helpers/XSSFRowShifter.class */
public final class XSSFRowShifter extends RowShifter {
    private static final POILogger logger = POILogFactory.getLogger((Class<?>) XSSFRowShifter.class);

    public XSSFRowShifter(XSSFSheet sh) {
        super(sh);
    }

    public List<CellRangeAddress> shiftMerged(int startRow, int endRow, int n) {
        return shiftMergedRegions(startRow, endRow, n);
    }

    @Override // org.apache.poi.ss.usermodel.helpers.RowShifter
    public void updateNamedRanges(FormulaShifter shifter) {
        Workbook wb = this.sheet.getWorkbook();
        XSSFEvaluationWorkbook fpb = XSSFEvaluationWorkbook.create((XSSFWorkbook) wb);
        for (Name name : wb.getAllNames()) {
            String formula = name.getRefersToFormula();
            int sheetIndex = name.getSheetIndex();
            Ptg[] ptgs = FormulaParser.parse(formula, fpb, FormulaType.NAMEDRANGE, sheetIndex, -1);
            if (shifter.adjustFormula(ptgs, sheetIndex)) {
                String shiftedFmla = FormulaRenderer.toFormulaString(fpb, ptgs);
                name.setRefersToFormula(shiftedFmla);
            }
        }
    }

    @Override // org.apache.poi.ss.usermodel.helpers.RowShifter
    public void updateFormulas(FormulaShifter shifter) {
        updateSheetFormulas(this.sheet, shifter);
        Workbook wb = this.sheet.getWorkbook();
        for (Sheet sh : wb) {
            if (this.sheet != sh) {
                updateSheetFormulas(sh, shifter);
            }
        }
    }

    private void updateSheetFormulas(Sheet sh, FormulaShifter shifter) {
        for (Row r : sh) {
            XSSFRow row = (XSSFRow) r;
            updateRowFormulas(row, shifter);
        }
    }

    @Override // org.apache.poi.ss.usermodel.helpers.RowShifter
    @Internal
    public void updateRowFormulas(Row row, FormulaShifter shifter) {
        String shiftedFormula;
        XSSFSheet sheet = (XSSFSheet) row.getSheet();
        for (Cell c : row) {
            XSSFCell cell = (XSSFCell) c;
            CTCell ctCell = cell.getCTCell();
            if (ctCell.isSetF()) {
                CTCellFormula f = ctCell.getF();
                String formula = f.getStringValue();
                if (formula.length() > 0 && (shiftedFormula = shiftFormula(row, formula, shifter)) != null) {
                    f.setStringValue(shiftedFormula);
                    if (f.getT() == STCellFormulaType.SHARED) {
                        int si = (int) f.getSi();
                        CTCellFormula sf = sheet.getSharedFormula(si);
                        sf.setStringValue(shiftedFormula);
                        updateRefInCTCellFormula(row, shifter, sf);
                    }
                }
                updateRefInCTCellFormula(row, shifter, f);
            }
        }
    }

    private void updateRefInCTCellFormula(Row row, FormulaShifter shifter, CTCellFormula f) {
        if (f.isSetRef()) {
            String ref = f.getRef();
            String shiftedRef = shiftFormula(row, ref, shifter);
            if (shiftedRef != null) {
                f.setRef(shiftedRef);
            }
        }
    }

    private static String shiftFormula(Row row, String formula, FormulaShifter shifter) {
        Sheet sheet = row.getSheet();
        Workbook wb = sheet.getWorkbook();
        int sheetIndex = wb.getSheetIndex(sheet);
        int rowIndex = row.getRowNum();
        XSSFEvaluationWorkbook fpb = XSSFEvaluationWorkbook.create((XSSFWorkbook) wb);
        try {
            Ptg[] ptgs = FormulaParser.parse(formula, fpb, FormulaType.CELL, sheetIndex, rowIndex);
            String shiftedFmla = null;
            if (shifter.adjustFormula(ptgs, sheetIndex)) {
                shiftedFmla = FormulaRenderer.toFormulaString(fpb, ptgs);
            }
            return shiftedFmla;
        } catch (FormulaParseException fpe) {
            logger.log(5, "Error shifting formula on row ", Integer.valueOf(row.getRowNum()), fpe);
            return formula;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0173  */
    @Override // org.apache.poi.ss.usermodel.helpers.RowShifter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void updateConditionalFormatting(org.apache.poi.ss.formula.FormulaShifter r7) {
        /*
            Method dump skipped, instructions count: 470
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.xssf.usermodel.helpers.XSSFRowShifter.updateConditionalFormatting(org.apache.poi.ss.formula.FormulaShifter):void");
    }

    @Override // org.apache.poi.ss.usermodel.helpers.RowShifter
    public void updateHyperlinks(FormulaShifter shifter) {
        int sheetIndex = this.sheet.getWorkbook().getSheetIndex(this.sheet);
        List<? extends Hyperlink> hyperlinkList = this.sheet.getHyperlinkList();
        for (Hyperlink hyperlink : hyperlinkList) {
            XSSFHyperlink xhyperlink = (XSSFHyperlink) hyperlink;
            String cellRef = xhyperlink.getCellRef();
            CellRangeAddress cra = CellRangeAddress.valueOf(cellRef);
            CellRangeAddress shiftedRange = shiftRange(shifter, cra, sheetIndex);
            if (shiftedRange != null && shiftedRange != cra) {
                xhyperlink.setCellReference(shiftedRange.formatAsString());
            }
        }
    }

    private static CellRangeAddress shiftRange(FormulaShifter shifter, CellRangeAddress cra, int currentExternSheetIx) {
        AreaPtg aptg = new AreaPtg(cra.getFirstRow(), cra.getLastRow(), cra.getFirstColumn(), cra.getLastColumn(), false, false, false, false);
        Ptg[] ptgs = {aptg};
        if (!shifter.adjustFormula(ptgs, currentExternSheetIx)) {
            return cra;
        }
        Ptg ptg0 = ptgs[0];
        if (ptg0 instanceof AreaPtg) {
            AreaPtg bptg = (AreaPtg) ptg0;
            return new CellRangeAddress(bptg.getFirstRow(), bptg.getLastRow(), bptg.getFirstColumn(), bptg.getLastColumn());
        }
        if (ptg0 instanceof AreaErrPtg) {
            return null;
        }
        throw new IllegalStateException("Unexpected shifted ptg class (" + ptg0.getClass().getName() + ")");
    }
}
