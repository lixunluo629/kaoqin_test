package com.itextpdf.layout.element;

import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.DefaultAccessibilityProperties;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/Cell.class */
public class Cell extends BlockElement<Cell> {
    private static final Border DEFAULT_BORDER = new SolidBorder(0.5f);
    private int row;
    private int col;
    private int rowspan;
    private int colspan;
    protected DefaultAccessibilityProperties tagProperties;

    public Cell(int rowspan, int colspan) {
        this.rowspan = Math.max(rowspan, 1);
        this.colspan = Math.max(colspan, 1);
    }

    public Cell() {
        this(1, 1);
    }

    @Override // com.itextpdf.layout.element.AbstractElement, com.itextpdf.layout.element.IElement
    public IRenderer getRenderer() {
        CellRenderer cellRenderer = null;
        if (this.nextRenderer != null) {
            if (this.nextRenderer instanceof CellRenderer) {
                IRenderer renderer = this.nextRenderer;
                this.nextRenderer = this.nextRenderer.getNextRenderer();
                cellRenderer = (CellRenderer) renderer;
            } else {
                Logger logger = LoggerFactory.getLogger((Class<?>) Table.class);
                logger.error("Invalid renderer for Table: must be inherited from TableRenderer");
            }
        }
        return cellRenderer == null ? makeNewRenderer() : cellRenderer;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public int getRowspan() {
        return this.rowspan;
    }

    public int getColspan() {
        return this.colspan;
    }

    public Cell add(IBlockElement element) {
        this.childElements.add(element);
        return this;
    }

    public Cell add(Image element) {
        this.childElements.add(element);
        return this;
    }

    public Cell clone(boolean includeContent) {
        Cell newCell = new Cell(this.rowspan, this.colspan);
        newCell.row = this.row;
        newCell.col = this.col;
        newCell.properties = new HashMap(this.properties);
        if (null != this.styles) {
            newCell.styles = new LinkedHashSet(this.styles);
        }
        if (includeContent) {
            newCell.childElements = new ArrayList(this.childElements);
        }
        return newCell;
    }

    @Override // com.itextpdf.layout.element.BlockElement, com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getDefaultProperty(int i) {
        switch (i) {
            case 9:
                return (T1) DEFAULT_BORDER;
            case 47:
            case 48:
            case 49:
            case 50:
                return (T1) UnitValue.createPointValue(2.0f);
            default:
                return (T1) super.getDefaultProperty(i);
        }
    }

    public String toString() {
        return MessageFormatUtil.format("Cell[row={0}, col={1}, rowspan={2}, colspan={3}]", Integer.valueOf(this.row), Integer.valueOf(this.col), Integer.valueOf(this.rowspan), Integer.valueOf(this.colspan));
    }

    @Override // com.itextpdf.layout.tagging.IAccessibleElement
    public AccessibilityProperties getAccessibilityProperties() {
        if (this.tagProperties == null) {
            this.tagProperties = new DefaultAccessibilityProperties(StandardRoles.TD);
        }
        return this.tagProperties;
    }

    @Override // com.itextpdf.layout.element.AbstractElement
    protected IRenderer makeNewRenderer() {
        return new CellRenderer(this);
    }

    protected Cell updateCellIndexes(int row, int col, int numberOfColumns) {
        this.row = row;
        this.col = col;
        this.colspan = Math.min(this.colspan, numberOfColumns - this.col);
        return this;
    }
}
