package org.apache.poi.ss.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.BorderExtent;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/util/PropertyTemplate.class */
public final class PropertyTemplate {
    private Map<CellAddress, Map<String, Object>> _propertyTemplate;

    public PropertyTemplate() {
        this._propertyTemplate = new HashMap();
    }

    public PropertyTemplate(PropertyTemplate template) {
        this();
        for (Map.Entry<CellAddress, Map<String, Object>> entry : template.getTemplate().entrySet()) {
            this._propertyTemplate.put(new CellAddress(entry.getKey()), cloneCellProperties(entry.getValue()));
        }
    }

    private Map<CellAddress, Map<String, Object>> getTemplate() {
        return this._propertyTemplate;
    }

    private static Map<String, Object> cloneCellProperties(Map<String, Object> properties) {
        Map<String, Object> newProperties = new HashMap<>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            newProperties.put(entry.getKey(), entry.getValue());
        }
        return newProperties;
    }

    public void drawBorders(CellRangeAddress range, BorderStyle borderType, BorderExtent extent) {
        switch (extent) {
            case NONE:
                removeBorders(range);
                break;
            case ALL:
                drawHorizontalBorders(range, borderType, BorderExtent.ALL);
                drawVerticalBorders(range, borderType, BorderExtent.ALL);
                break;
            case INSIDE:
                drawHorizontalBorders(range, borderType, BorderExtent.INSIDE);
                drawVerticalBorders(range, borderType, BorderExtent.INSIDE);
                break;
            case OUTSIDE:
                drawOutsideBorders(range, borderType, BorderExtent.ALL);
                break;
            case TOP:
                drawTopBorder(range, borderType);
                break;
            case BOTTOM:
                drawBottomBorder(range, borderType);
                break;
            case LEFT:
                drawLeftBorder(range, borderType);
                break;
            case RIGHT:
                drawRightBorder(range, borderType);
                break;
            case HORIZONTAL:
                drawHorizontalBorders(range, borderType, BorderExtent.ALL);
                break;
            case INSIDE_HORIZONTAL:
                drawHorizontalBorders(range, borderType, BorderExtent.INSIDE);
                break;
            case OUTSIDE_HORIZONTAL:
                drawOutsideBorders(range, borderType, BorderExtent.HORIZONTAL);
                break;
            case VERTICAL:
                drawVerticalBorders(range, borderType, BorderExtent.ALL);
                break;
            case INSIDE_VERTICAL:
                drawVerticalBorders(range, borderType, BorderExtent.INSIDE);
                break;
            case OUTSIDE_VERTICAL:
                drawOutsideBorders(range, borderType, BorderExtent.VERTICAL);
                break;
        }
    }

    public void drawBorders(CellRangeAddress range, BorderStyle borderType, short color, BorderExtent extent) {
        drawBorders(range, borderType, extent);
        if (borderType != BorderStyle.NONE) {
            drawBorderColors(range, color, extent);
        }
    }

    private void drawTopBorder(CellRangeAddress range, BorderStyle borderType) {
        int row = range.getFirstRow();
        int firstCol = range.getFirstColumn();
        int lastCol = range.getLastColumn();
        for (int i = firstCol; i <= lastCol; i++) {
            addProperty(row, i, CellUtil.BORDER_TOP, borderType);
            if (borderType == BorderStyle.NONE && row > 0) {
                addProperty(row - 1, i, CellUtil.BORDER_BOTTOM, borderType);
            }
        }
    }

    private void drawBottomBorder(CellRangeAddress range, BorderStyle borderType) {
        int row = range.getLastRow();
        int firstCol = range.getFirstColumn();
        int lastCol = range.getLastColumn();
        for (int i = firstCol; i <= lastCol; i++) {
            addProperty(row, i, CellUtil.BORDER_BOTTOM, borderType);
            if (borderType == BorderStyle.NONE && row < SpreadsheetVersion.EXCEL2007.getMaxRows() - 1) {
                addProperty(row + 1, i, CellUtil.BORDER_TOP, borderType);
            }
        }
    }

    private void drawLeftBorder(CellRangeAddress range, BorderStyle borderType) {
        int firstRow = range.getFirstRow();
        int lastRow = range.getLastRow();
        int col = range.getFirstColumn();
        for (int i = firstRow; i <= lastRow; i++) {
            addProperty(i, col, CellUtil.BORDER_LEFT, borderType);
            if (borderType == BorderStyle.NONE && col > 0) {
                addProperty(i, col - 1, CellUtil.BORDER_RIGHT, borderType);
            }
        }
    }

    private void drawRightBorder(CellRangeAddress range, BorderStyle borderType) {
        int firstRow = range.getFirstRow();
        int lastRow = range.getLastRow();
        int col = range.getLastColumn();
        for (int i = firstRow; i <= lastRow; i++) {
            addProperty(i, col, CellUtil.BORDER_RIGHT, borderType);
            if (borderType == BorderStyle.NONE && col < SpreadsheetVersion.EXCEL2007.getMaxColumns() - 1) {
                addProperty(i, col + 1, CellUtil.BORDER_LEFT, borderType);
            }
        }
    }

    private void drawOutsideBorders(CellRangeAddress range, BorderStyle borderType, BorderExtent extent) {
        switch (extent) {
            case ALL:
            case HORIZONTAL:
            case VERTICAL:
                if (extent == BorderExtent.ALL || extent == BorderExtent.HORIZONTAL) {
                    drawTopBorder(range, borderType);
                    drawBottomBorder(range, borderType);
                }
                if (extent == BorderExtent.ALL || extent == BorderExtent.VERTICAL) {
                    drawLeftBorder(range, borderType);
                    drawRightBorder(range, borderType);
                    return;
                }
                return;
            default:
                throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL, HORIZONTAL, and VERTICAL");
        }
    }

    private void drawHorizontalBorders(CellRangeAddress range, BorderStyle borderType, BorderExtent extent) {
        switch (extent) {
            case ALL:
            case INSIDE:
                int firstRow = range.getFirstRow();
                int lastRow = range.getLastRow();
                int firstCol = range.getFirstColumn();
                int lastCol = range.getLastColumn();
                for (int i = firstRow; i <= lastRow; i++) {
                    CellRangeAddress row = new CellRangeAddress(i, i, firstCol, lastCol);
                    if (extent == BorderExtent.ALL || i > firstRow) {
                        drawTopBorder(row, borderType);
                    }
                    if (extent == BorderExtent.ALL || i < lastRow) {
                        drawBottomBorder(row, borderType);
                    }
                }
                return;
            default:
                throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL and INSIDE");
        }
    }

    private void drawVerticalBorders(CellRangeAddress range, BorderStyle borderType, BorderExtent extent) {
        switch (extent) {
            case ALL:
            case INSIDE:
                int firstRow = range.getFirstRow();
                int lastRow = range.getLastRow();
                int firstCol = range.getFirstColumn();
                int lastCol = range.getLastColumn();
                for (int i = firstCol; i <= lastCol; i++) {
                    CellRangeAddress row = new CellRangeAddress(firstRow, lastRow, i, i);
                    if (extent == BorderExtent.ALL || i > firstCol) {
                        drawLeftBorder(row, borderType);
                    }
                    if (extent == BorderExtent.ALL || i < lastCol) {
                        drawRightBorder(row, borderType);
                    }
                }
                return;
            default:
                throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL and INSIDE");
        }
    }

    private void removeBorders(CellRangeAddress range) {
        Set<String> properties = new HashSet<>();
        properties.add(CellUtil.BORDER_TOP);
        properties.add(CellUtil.BORDER_BOTTOM);
        properties.add(CellUtil.BORDER_LEFT);
        properties.add(CellUtil.BORDER_RIGHT);
        for (int row = range.getFirstRow(); row <= range.getLastRow(); row++) {
            for (int col = range.getFirstColumn(); col <= range.getLastColumn(); col++) {
                removeProperties(row, col, properties);
            }
        }
        removeBorderColors(range);
    }

    public void applyBorders(Sheet sheet) {
        Workbook wb = sheet.getWorkbook();
        for (Map.Entry<CellAddress, Map<String, Object>> entry : this._propertyTemplate.entrySet()) {
            CellAddress cellAddress = entry.getKey();
            if (cellAddress.getRow() < wb.getSpreadsheetVersion().getMaxRows() && cellAddress.getColumn() < wb.getSpreadsheetVersion().getMaxColumns()) {
                Map<String, Object> properties = entry.getValue();
                Row row = CellUtil.getRow(cellAddress.getRow(), sheet);
                Cell cell = CellUtil.getCell(row, cellAddress.getColumn());
                CellUtil.setCellStyleProperties(cell, properties);
            }
        }
    }

    public void drawBorderColors(CellRangeAddress range, short color, BorderExtent extent) {
        switch (extent) {
            case NONE:
                removeBorderColors(range);
                break;
            case ALL:
                drawHorizontalBorderColors(range, color, BorderExtent.ALL);
                drawVerticalBorderColors(range, color, BorderExtent.ALL);
                break;
            case INSIDE:
                drawHorizontalBorderColors(range, color, BorderExtent.INSIDE);
                drawVerticalBorderColors(range, color, BorderExtent.INSIDE);
                break;
            case OUTSIDE:
                drawOutsideBorderColors(range, color, BorderExtent.ALL);
                break;
            case TOP:
                drawTopBorderColor(range, color);
                break;
            case BOTTOM:
                drawBottomBorderColor(range, color);
                break;
            case LEFT:
                drawLeftBorderColor(range, color);
                break;
            case RIGHT:
                drawRightBorderColor(range, color);
                break;
            case HORIZONTAL:
                drawHorizontalBorderColors(range, color, BorderExtent.ALL);
                break;
            case INSIDE_HORIZONTAL:
                drawHorizontalBorderColors(range, color, BorderExtent.INSIDE);
                break;
            case OUTSIDE_HORIZONTAL:
                drawOutsideBorderColors(range, color, BorderExtent.HORIZONTAL);
                break;
            case VERTICAL:
                drawVerticalBorderColors(range, color, BorderExtent.ALL);
                break;
            case INSIDE_VERTICAL:
                drawVerticalBorderColors(range, color, BorderExtent.INSIDE);
                break;
            case OUTSIDE_VERTICAL:
                drawOutsideBorderColors(range, color, BorderExtent.VERTICAL);
                break;
        }
    }

    private void drawTopBorderColor(CellRangeAddress range, short color) {
        int row = range.getFirstRow();
        int firstCol = range.getFirstColumn();
        int lastCol = range.getLastColumn();
        for (int i = firstCol; i <= lastCol; i++) {
            if (getBorderStyle(row, i, CellUtil.BORDER_TOP) == BorderStyle.NONE) {
                drawTopBorder(new CellRangeAddress(row, row, i, i), BorderStyle.THIN);
            }
            addProperty(row, i, CellUtil.TOP_BORDER_COLOR, color);
        }
    }

    private void drawBottomBorderColor(CellRangeAddress range, short color) {
        int row = range.getLastRow();
        int firstCol = range.getFirstColumn();
        int lastCol = range.getLastColumn();
        for (int i = firstCol; i <= lastCol; i++) {
            if (getBorderStyle(row, i, CellUtil.BORDER_BOTTOM) == BorderStyle.NONE) {
                drawBottomBorder(new CellRangeAddress(row, row, i, i), BorderStyle.THIN);
            }
            addProperty(row, i, CellUtil.BOTTOM_BORDER_COLOR, color);
        }
    }

    private void drawLeftBorderColor(CellRangeAddress range, short color) {
        int firstRow = range.getFirstRow();
        int lastRow = range.getLastRow();
        int col = range.getFirstColumn();
        for (int i = firstRow; i <= lastRow; i++) {
            if (getBorderStyle(i, col, CellUtil.BORDER_LEFT) == BorderStyle.NONE) {
                drawLeftBorder(new CellRangeAddress(i, i, col, col), BorderStyle.THIN);
            }
            addProperty(i, col, CellUtil.LEFT_BORDER_COLOR, color);
        }
    }

    private void drawRightBorderColor(CellRangeAddress range, short color) {
        int firstRow = range.getFirstRow();
        int lastRow = range.getLastRow();
        int col = range.getLastColumn();
        for (int i = firstRow; i <= lastRow; i++) {
            if (getBorderStyle(i, col, CellUtil.BORDER_RIGHT) == BorderStyle.NONE) {
                drawRightBorder(new CellRangeAddress(i, i, col, col), BorderStyle.THIN);
            }
            addProperty(i, col, CellUtil.RIGHT_BORDER_COLOR, color);
        }
    }

    private void drawOutsideBorderColors(CellRangeAddress range, short color, BorderExtent extent) {
        switch (extent) {
            case ALL:
            case HORIZONTAL:
            case VERTICAL:
                if (extent == BorderExtent.ALL || extent == BorderExtent.HORIZONTAL) {
                    drawTopBorderColor(range, color);
                    drawBottomBorderColor(range, color);
                }
                if (extent == BorderExtent.ALL || extent == BorderExtent.VERTICAL) {
                    drawLeftBorderColor(range, color);
                    drawRightBorderColor(range, color);
                    return;
                }
                return;
            default:
                throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL, HORIZONTAL, and VERTICAL");
        }
    }

    private void drawHorizontalBorderColors(CellRangeAddress range, short color, BorderExtent extent) {
        switch (extent) {
            case ALL:
            case INSIDE:
                int firstRow = range.getFirstRow();
                int lastRow = range.getLastRow();
                int firstCol = range.getFirstColumn();
                int lastCol = range.getLastColumn();
                for (int i = firstRow; i <= lastRow; i++) {
                    CellRangeAddress row = new CellRangeAddress(i, i, firstCol, lastCol);
                    if (extent == BorderExtent.ALL || i > firstRow) {
                        drawTopBorderColor(row, color);
                    }
                    if (extent == BorderExtent.ALL || i < lastRow) {
                        drawBottomBorderColor(row, color);
                    }
                }
                return;
            default:
                throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL and INSIDE");
        }
    }

    private void drawVerticalBorderColors(CellRangeAddress range, short color, BorderExtent extent) {
        switch (extent) {
            case ALL:
            case INSIDE:
                int firstRow = range.getFirstRow();
                int lastRow = range.getLastRow();
                int firstCol = range.getFirstColumn();
                int lastCol = range.getLastColumn();
                for (int i = firstCol; i <= lastCol; i++) {
                    CellRangeAddress row = new CellRangeAddress(firstRow, lastRow, i, i);
                    if (extent == BorderExtent.ALL || i > firstCol) {
                        drawLeftBorderColor(row, color);
                    }
                    if (extent == BorderExtent.ALL || i < lastCol) {
                        drawRightBorderColor(row, color);
                    }
                }
                return;
            default:
                throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL and INSIDE");
        }
    }

    private void removeBorderColors(CellRangeAddress range) {
        Set<String> properties = new HashSet<>();
        properties.add(CellUtil.TOP_BORDER_COLOR);
        properties.add(CellUtil.BOTTOM_BORDER_COLOR);
        properties.add(CellUtil.LEFT_BORDER_COLOR);
        properties.add(CellUtil.RIGHT_BORDER_COLOR);
        for (int row = range.getFirstRow(); row <= range.getLastRow(); row++) {
            for (int col = range.getFirstColumn(); col <= range.getLastColumn(); col++) {
                removeProperties(row, col, properties);
            }
        }
    }

    private void addProperty(int row, int col, String property, short value) {
        addProperty(row, col, property, Short.valueOf(value));
    }

    private void addProperty(int row, int col, String property, Object value) {
        CellAddress cell = new CellAddress(row, col);
        Map<String, Object> cellProperties = this._propertyTemplate.get(cell);
        if (cellProperties == null) {
            cellProperties = new HashMap();
        }
        cellProperties.put(property, value);
        this._propertyTemplate.put(cell, cellProperties);
    }

    private void removeProperties(int row, int col, Set<String> properties) {
        CellAddress cell = new CellAddress(row, col);
        Map<String, Object> cellProperties = this._propertyTemplate.get(cell);
        if (cellProperties != null) {
            cellProperties.keySet().removeAll(properties);
            if (cellProperties.isEmpty()) {
                this._propertyTemplate.remove(cell);
            } else {
                this._propertyTemplate.put(cell, cellProperties);
            }
        }
    }

    public int getNumBorders(CellAddress cell) {
        Map<String, Object> cellProperties = this._propertyTemplate.get(cell);
        if (cellProperties == null) {
            return 0;
        }
        int count = 0;
        for (String property : cellProperties.keySet()) {
            if (property.equals(CellUtil.BORDER_TOP)) {
                count++;
            }
            if (property.equals(CellUtil.BORDER_BOTTOM)) {
                count++;
            }
            if (property.equals(CellUtil.BORDER_LEFT)) {
                count++;
            }
            if (property.equals(CellUtil.BORDER_RIGHT)) {
                count++;
            }
        }
        return count;
    }

    public int getNumBorders(int row, int col) {
        return getNumBorders(new CellAddress(row, col));
    }

    public int getNumBorderColors(CellAddress cell) {
        Map<String, Object> cellProperties = this._propertyTemplate.get(cell);
        if (cellProperties == null) {
            return 0;
        }
        int count = 0;
        for (String property : cellProperties.keySet()) {
            if (property.equals(CellUtil.TOP_BORDER_COLOR)) {
                count++;
            }
            if (property.equals(CellUtil.BOTTOM_BORDER_COLOR)) {
                count++;
            }
            if (property.equals(CellUtil.LEFT_BORDER_COLOR)) {
                count++;
            }
            if (property.equals(CellUtil.RIGHT_BORDER_COLOR)) {
                count++;
            }
        }
        return count;
    }

    public int getNumBorderColors(int row, int col) {
        return getNumBorderColors(new CellAddress(row, col));
    }

    public BorderStyle getBorderStyle(CellAddress cell, String property) {
        BorderStyle value = BorderStyle.NONE;
        Map<String, Object> cellProperties = this._propertyTemplate.get(cell);
        if (cellProperties != null) {
            Object obj = cellProperties.get(property);
            if (obj instanceof BorderStyle) {
                value = (BorderStyle) obj;
            }
        }
        return value;
    }

    public BorderStyle getBorderStyle(int row, int col, String property) {
        return getBorderStyle(new CellAddress(row, col), property);
    }

    public short getTemplateProperty(CellAddress cell, String property) {
        Object obj;
        short value = 0;
        Map<String, Object> cellProperties = this._propertyTemplate.get(cell);
        if (cellProperties != null && (obj = cellProperties.get(property)) != null) {
            value = getShort(obj);
        }
        return value;
    }

    public short getTemplateProperty(int row, int col, String property) {
        return getTemplateProperty(new CellAddress(row, col), property);
    }

    private static short getShort(Object value) {
        if (value instanceof Short) {
            return ((Short) value).shortValue();
        }
        return (short) 0;
    }
}
