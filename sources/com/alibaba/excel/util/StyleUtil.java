package com.alibaba.excel.util;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/StyleUtil.class */
public class StyleUtil {
    private StyleUtil() {
    }

    public static CellStyle buildDefaultCellStyle(Workbook workbook) {
        CellStyle newCellStyle = workbook.createCellStyle();
        newCellStyle.setWrapText(true);
        newCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        newCellStyle.setAlignment(HorizontalAlignment.CENTER);
        newCellStyle.setLocked(true);
        newCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        newCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        newCellStyle.setBorderTop(BorderStyle.THIN);
        newCellStyle.setBorderBottom(BorderStyle.THIN);
        newCellStyle.setBorderLeft(BorderStyle.THIN);
        newCellStyle.setBorderRight(BorderStyle.THIN);
        return newCellStyle;
    }

    public static CellStyle buildHeadCellStyle(Workbook workbook, WriteCellStyle writeCellStyle) {
        CellStyle cellStyle = buildDefaultCellStyle(workbook);
        if (writeCellStyle == null) {
            return cellStyle;
        }
        buildCellStyle(workbook, cellStyle, writeCellStyle, true);
        return cellStyle;
    }

    public static CellStyle buildContentCellStyle(Workbook workbook, WriteCellStyle writeCellStyle) {
        CellStyle cellStyle = workbook.createCellStyle();
        if (writeCellStyle == null) {
            return cellStyle;
        }
        buildCellStyle(workbook, cellStyle, writeCellStyle, false);
        return cellStyle;
    }

    private static void buildCellStyle(Workbook workbook, CellStyle cellStyle, WriteCellStyle writeCellStyle, boolean isHead) {
        buildFont(workbook, cellStyle, writeCellStyle.getWriteFont(), isHead);
        if (writeCellStyle.getDataFormat() != null) {
            cellStyle.setDataFormat(writeCellStyle.getDataFormat().shortValue());
        }
        if (writeCellStyle.getHidden() != null) {
            cellStyle.setHidden(writeCellStyle.getHidden().booleanValue());
        }
        if (writeCellStyle.getLocked() != null) {
            cellStyle.setLocked(writeCellStyle.getLocked().booleanValue());
        }
        if (writeCellStyle.getQuotePrefix() != null) {
            cellStyle.setQuotePrefixed(writeCellStyle.getQuotePrefix().booleanValue());
        }
        if (writeCellStyle.getHorizontalAlignment() != null) {
            cellStyle.setAlignment(writeCellStyle.getHorizontalAlignment());
        }
        if (writeCellStyle.getWrapped() != null) {
            cellStyle.setWrapText(writeCellStyle.getWrapped().booleanValue());
        }
        if (writeCellStyle.getVerticalAlignment() != null) {
            cellStyle.setVerticalAlignment(writeCellStyle.getVerticalAlignment());
        }
        if (writeCellStyle.getRotation() != null) {
            cellStyle.setRotation(writeCellStyle.getRotation().shortValue());
        }
        if (writeCellStyle.getIndent() != null) {
            cellStyle.setIndention(writeCellStyle.getIndent().shortValue());
        }
        if (writeCellStyle.getBorderLeft() != null) {
            cellStyle.setBorderLeft(writeCellStyle.getBorderLeft());
        }
        if (writeCellStyle.getBorderRight() != null) {
            cellStyle.setBorderRight(writeCellStyle.getBorderRight());
        }
        if (writeCellStyle.getBorderTop() != null) {
            cellStyle.setBorderTop(writeCellStyle.getBorderTop());
        }
        if (writeCellStyle.getBorderBottom() != null) {
            cellStyle.setBorderBottom(writeCellStyle.getBorderBottom());
        }
        if (writeCellStyle.getLeftBorderColor() != null) {
            cellStyle.setLeftBorderColor(writeCellStyle.getLeftBorderColor().shortValue());
        }
        if (writeCellStyle.getRightBorderColor() != null) {
            cellStyle.setRightBorderColor(writeCellStyle.getRightBorderColor().shortValue());
        }
        if (writeCellStyle.getTopBorderColor() != null) {
            cellStyle.setTopBorderColor(writeCellStyle.getTopBorderColor().shortValue());
        }
        if (writeCellStyle.getBottomBorderColor() != null) {
            cellStyle.setBottomBorderColor(writeCellStyle.getBottomBorderColor().shortValue());
        }
        if (writeCellStyle.getFillPatternType() != null) {
            cellStyle.setFillPattern(writeCellStyle.getFillPatternType());
        }
        if (writeCellStyle.getFillBackgroundColor() != null) {
            cellStyle.setFillBackgroundColor(writeCellStyle.getFillBackgroundColor().shortValue());
        }
        if (writeCellStyle.getFillForegroundColor() != null) {
            cellStyle.setFillForegroundColor(writeCellStyle.getFillForegroundColor().shortValue());
        }
        if (writeCellStyle.getShrinkToFit() != null) {
            cellStyle.setShrinkToFit(writeCellStyle.getShrinkToFit().booleanValue());
        }
    }

    private static void buildFont(Workbook workbook, CellStyle cellStyle, WriteFont writeFont, boolean isHead) {
        Font font = null;
        if (isHead) {
            font = workbook.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 14);
            font.setBold(true);
            cellStyle.setFont(font);
        }
        if (writeFont == null) {
            return;
        }
        if (!isHead) {
            font = workbook.createFont();
            cellStyle.setFont(font);
        }
        if (writeFont.getFontName() != null) {
            font.setFontName(writeFont.getFontName());
        }
        if (writeFont.getFontHeightInPoints() != null) {
            font.setFontHeightInPoints(writeFont.getFontHeightInPoints().shortValue());
        }
        if (writeFont.getItalic() != null) {
            font.setItalic(writeFont.getItalic().booleanValue());
        }
        if (writeFont.getStrikeout() != null) {
            font.setStrikeout(writeFont.getStrikeout().booleanValue());
        }
        if (writeFont.getColor() != null) {
            font.setColor(writeFont.getColor().shortValue());
        }
        if (writeFont.getTypeOffset() != null) {
            font.setTypeOffset(writeFont.getTypeOffset().shortValue());
        }
        if (writeFont.getUnderline() != null) {
            font.setUnderline(writeFont.getUnderline().byteValue());
        }
        if (writeFont.getCharset() != null) {
            font.setCharSet(writeFont.getCharset().intValue());
        }
        if (writeFont.getBold() != null) {
            font.setBold(writeFont.getBold().booleanValue());
        }
    }
}
