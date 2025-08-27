package org.apache.poi.ss.util;

import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Internal;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/util/SheetUtil.class */
public class SheetUtil {
    private static final char defaultChar = '0';
    private static final double fontHeightMultiple = 2.0d;
    private static final FormulaEvaluator dummyEvaluator = new FormulaEvaluator() { // from class: org.apache.poi.ss.util.SheetUtil.1
        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public void clearAllCachedResultValues() {
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public void notifySetFormula(Cell cell) {
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public void notifyDeleteCell(Cell cell) {
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public void notifyUpdateCell(Cell cell) {
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public CellValue evaluate(Cell cell) {
            return null;
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public Cell evaluateInCell(Cell cell) {
            return null;
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public void setupReferencedWorkbooks(Map<String, FormulaEvaluator> workbooks) {
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public void setDebugEvaluationOutputForNextEval(boolean value) {
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public void setIgnoreMissingWorkbooks(boolean ignore) {
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public void evaluateAll() {
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        public int evaluateFormulaCell(Cell cell) {
            return cell.getCachedFormulaResultType();
        }

        @Override // org.apache.poi.ss.usermodel.FormulaEvaluator
        @Internal(since = "POI 3.15 beta 3")
        public CellType evaluateFormulaCellEnum(Cell cell) {
            return cell.getCachedFormulaResultTypeEnum();
        }
    };
    private static final FontRenderContext fontRenderContext = new FontRenderContext((AffineTransform) null, true, true);

    public static double getCellWidth(Cell cell, int defaultCharWidth, DataFormatter formatter, boolean useMergedCells) {
        Sheet sheet = cell.getSheet();
        Workbook wb = sheet.getWorkbook();
        Row row = cell.getRow();
        int column = cell.getColumnIndex();
        int colspan = 1;
        for (CellRangeAddress region : sheet.getMergedRegions()) {
            if (region.isInRange(row.getRowNum(), column)) {
                if (!useMergedCells) {
                    return -1.0d;
                }
                cell = row.getCell(region.getFirstColumn());
                colspan = (1 + region.getLastColumn()) - region.getFirstColumn();
            }
        }
        CellStyle style = cell.getCellStyle();
        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultTypeEnum();
        }
        Font font = wb.getFontAt(style.getFontIndex());
        double width = -1.0d;
        if (cellType == CellType.STRING) {
            RichTextString rt = cell.getRichStringCellValue();
            String[] lines = rt.getString().split("\\n");
            for (String line : lines) {
                String txt = line + '0';
                AttributedString str = new AttributedString(txt);
                copyAttributes(font, str, 0, txt.length());
                width = getCellWidth(defaultCharWidth, colspan, style, width, str);
            }
        } else {
            String sval = null;
            if (cellType == CellType.NUMERIC) {
                try {
                    sval = formatter.formatCellValue(cell, dummyEvaluator);
                } catch (Exception e) {
                    sval = String.valueOf(cell.getNumericCellValue());
                }
            } else if (cellType == CellType.BOOLEAN) {
                sval = String.valueOf(cell.getBooleanCellValue()).toUpperCase(Locale.ROOT);
            }
            if (sval != null) {
                String txt2 = sval + '0';
                AttributedString str2 = new AttributedString(txt2);
                copyAttributes(font, str2, 0, txt2.length());
                width = getCellWidth(defaultCharWidth, colspan, style, -1.0d, str2);
            }
        }
        return width;
    }

    private static double getCellWidth(int defaultCharWidth, int colspan, CellStyle style, double minWidth, AttributedString str) {
        Rectangle bounds;
        TextLayout layout = new TextLayout(str.getIterator(), fontRenderContext);
        if (style.getRotation() != 0) {
            AffineTransform trans = new AffineTransform();
            trans.concatenate(AffineTransform.getRotateInstance(((style.getRotation() * fontHeightMultiple) * 3.141592653589793d) / 360.0d));
            trans.concatenate(AffineTransform.getScaleInstance(1.0d, fontHeightMultiple));
            bounds = layout.getOutline(trans).getBounds();
        } else {
            bounds = layout.getBounds();
        }
        double frameWidth = bounds.getX() + bounds.getWidth();
        return Math.max(minWidth, ((frameWidth / colspan) / defaultCharWidth) + style.getIndention());
    }

    public static double getColumnWidth(Sheet sheet, int column, boolean useMergedCells) {
        return getColumnWidth(sheet, column, useMergedCells, sheet.getFirstRowNum(), sheet.getLastRowNum());
    }

    public static double getColumnWidth(Sheet sheet, int column, boolean useMergedCells, int firstRow, int lastRow) {
        DataFormatter formatter = new DataFormatter();
        int defaultCharWidth = getDefaultCharWidth(sheet.getWorkbook());
        double width = -1.0d;
        for (int rowIdx = firstRow; rowIdx <= lastRow; rowIdx++) {
            Row row = sheet.getRow(rowIdx);
            if (row != null) {
                double cellWidth = getColumnWidthForRow(row, column, defaultCharWidth, formatter, useMergedCells);
                width = Math.max(width, cellWidth);
            }
        }
        return width;
    }

    @Internal
    public static int getDefaultCharWidth(Workbook wb) {
        Font defaultFont = wb.getFontAt((short) 0);
        AttributedString str = new AttributedString(String.valueOf('0'));
        copyAttributes(defaultFont, str, 0, 1);
        TextLayout layout = new TextLayout(str.getIterator(), fontRenderContext);
        return (int) layout.getAdvance();
    }

    private static double getColumnWidthForRow(Row row, int column, int defaultCharWidth, DataFormatter formatter, boolean useMergedCells) {
        Cell cell;
        if (row == null || (cell = row.getCell(column)) == null) {
            return -1.0d;
        }
        return getCellWidth(cell, defaultCharWidth, formatter, useMergedCells);
    }

    public static boolean canComputeColumnWidth(Font font) {
        AttributedString str = new AttributedString("1w");
        copyAttributes(font, str, 0, "1w".length());
        TextLayout layout = new TextLayout(str.getIterator(), fontRenderContext);
        if (layout.getBounds().getWidth() > 0.0d) {
            return true;
        }
        return false;
    }

    private static void copyAttributes(Font font, AttributedString str, int startIdx, int endIdx) {
        str.addAttribute(TextAttribute.FAMILY, font.getFontName(), startIdx, endIdx);
        str.addAttribute(TextAttribute.SIZE, Float.valueOf(font.getFontHeightInPoints()));
        if (font.getBold()) {
            str.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, startIdx, endIdx);
        }
        if (font.getItalic()) {
            str.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, startIdx, endIdx);
        }
        if (font.getUnderline() == 1) {
            str.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, startIdx, endIdx);
        }
    }

    @Deprecated
    public static boolean containsCell(CellRangeAddress cr, int rowIx, int colIx) {
        return cr.isInRange(rowIx, colIx);
    }

    public static Cell getCell(Sheet sheet, int rowIx, int colIx) {
        Row r = sheet.getRow(rowIx);
        if (r != null) {
            return r.getCell(colIx);
        }
        return null;
    }

    public static Cell getCellWithMerges(Sheet sheet, int rowIx, int colIx) {
        Row r;
        Cell c = getCell(sheet, rowIx, colIx);
        if (c != null) {
            return c;
        }
        for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
            if (mergedRegion.isInRange(rowIx, colIx) && (r = sheet.getRow(mergedRegion.getFirstRow())) != null) {
                return r.getCell(mergedRegion.getFirstColumn());
            }
        }
        return null;
    }
}
