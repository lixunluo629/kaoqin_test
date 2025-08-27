package org.apache.poi.xssf.usermodel.extensions;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.util.Internal;
import org.apache.poi.xssf.model.ThemesTable;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/extensions/XSSFCellBorder.class */
public class XSSFCellBorder {
    private IndexedColorMap _indexedColorMap;
    private ThemesTable _theme;
    private CTBorder border;

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/extensions/XSSFCellBorder$BorderSide.class */
    public enum BorderSide {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

    public XSSFCellBorder(CTBorder border, ThemesTable theme, IndexedColorMap colorMap) {
        this(border, colorMap);
        this._theme = theme;
    }

    public XSSFCellBorder(CTBorder border) {
        this(border, null);
    }

    public XSSFCellBorder(CTBorder border, IndexedColorMap colorMap) {
        this.border = border;
        this._indexedColorMap = colorMap;
    }

    public XSSFCellBorder() {
        this.border = CTBorder.Factory.newInstance();
    }

    public void setThemesTable(ThemesTable themes) {
        this._theme = themes;
    }

    @Internal
    public CTBorder getCTBorder() {
        return this.border;
    }

    public BorderStyle getBorderStyle(BorderSide side) {
        CTBorderPr ctBorder = getBorder(side);
        STBorderStyle.Enum border = ctBorder == null ? STBorderStyle.NONE : ctBorder.getStyle();
        return BorderStyle.values()[border.intValue() - 1];
    }

    public void setBorderStyle(BorderSide side, BorderStyle style) {
        getBorder(side, true).setStyle(STBorderStyle.Enum.forInt(style.ordinal() + 1));
    }

    public XSSFColor getBorderColor(BorderSide side) {
        CTBorderPr borderPr = getBorder(side);
        if (borderPr != null && borderPr.isSetColor()) {
            XSSFColor clr = new XSSFColor(borderPr.getColor(), this._indexedColorMap);
            if (this._theme != null) {
                this._theme.inheritFromThemeAsRequired(clr);
            }
            return clr;
        }
        return null;
    }

    public void setBorderColor(BorderSide side, XSSFColor color) {
        CTBorderPr borderPr = getBorder(side, true);
        if (color != null) {
            borderPr.setColor(color.getCTColor());
        } else {
            borderPr.unsetColor();
        }
    }

    private CTBorderPr getBorder(BorderSide side) {
        return getBorder(side, false);
    }

    private CTBorderPr getBorder(BorderSide side, boolean ensure) {
        CTBorderPr borderPr;
        switch (side) {
            case TOP:
                borderPr = this.border.getTop();
                if (ensure && borderPr == null) {
                    borderPr = this.border.addNewTop();
                    break;
                }
                break;
            case RIGHT:
                borderPr = this.border.getRight();
                if (ensure && borderPr == null) {
                    borderPr = this.border.addNewRight();
                    break;
                }
                break;
            case BOTTOM:
                borderPr = this.border.getBottom();
                if (ensure && borderPr == null) {
                    borderPr = this.border.addNewBottom();
                    break;
                }
                break;
            case LEFT:
                borderPr = this.border.getLeft();
                if (ensure && borderPr == null) {
                    borderPr = this.border.addNewLeft();
                    break;
                }
                break;
            default:
                throw new IllegalArgumentException("No suitable side specified for the border");
        }
        return borderPr;
    }

    public int hashCode() {
        return this.border.toString().hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof XSSFCellBorder)) {
            return false;
        }
        XSSFCellBorder cf = (XSSFCellBorder) o;
        return this.border.toString().equals(cf.getCTBorder().toString());
    }
}
