package com.itextpdf.layout.element;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.renderer.AreaBreakRenderer;
import com.itextpdf.layout.renderer.IRenderer;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/AreaBreak.class */
public class AreaBreak extends AbstractElement<AreaBreak> {
    protected PageSize pageSize;

    public AreaBreak() {
        this(AreaBreakType.NEXT_AREA);
    }

    public AreaBreak(AreaBreakType areaBreakType) {
        setProperty(2, areaBreakType);
    }

    public AreaBreak(PageSize pageSize) {
        this(AreaBreakType.NEXT_PAGE);
        this.pageSize = pageSize;
    }

    public PageSize getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(PageSize pageSize) {
        this.pageSize = pageSize;
    }

    public AreaBreakType getType() {
        return (AreaBreakType) getProperty(2);
    }

    @Override // com.itextpdf.layout.element.AbstractElement
    protected IRenderer makeNewRenderer() {
        return new AreaBreakRenderer(this);
    }
}
