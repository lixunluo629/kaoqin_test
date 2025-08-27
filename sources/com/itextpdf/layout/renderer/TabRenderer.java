package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.CanvasArtifact;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TabRenderer.class */
public class TabRenderer extends AbstractRenderer {
    public TabRenderer(Tab tab) {
        super(tab);
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public LayoutResult layout(LayoutContext layoutContext) {
        LayoutArea area = layoutContext.getArea();
        Float width = retrieveWidth(area.getBBox().getWidth());
        UnitValue height = (UnitValue) getProperty(85);
        this.occupiedArea = new LayoutArea(area.getPageNumber(), new Rectangle(area.getBBox().getX(), area.getBBox().getY() + area.getBBox().getHeight(), width.floatValue(), height.getValue()));
        return new LayoutResult(1, this.occupiedArea, null, null);
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void draw(DrawContext drawContext) {
        if (this.occupiedArea == null) {
            Logger logger = LoggerFactory.getLogger((Class<?>) TabRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.OCCUPIED_AREA_HAS_NOT_BEEN_INITIALIZED, "Drawing won't be performed."));
            return;
        }
        ILineDrawer leader = (ILineDrawer) getProperty(68);
        if (leader == null) {
            return;
        }
        boolean isTagged = drawContext.isTaggingEnabled();
        if (isTagged) {
            drawContext.getCanvas().openTag(new CanvasArtifact());
        }
        beginElementOpacityApplying(drawContext);
        leader.draw(drawContext.getCanvas(), this.occupiedArea.getBBox());
        endElementOpacityApplying(drawContext);
        if (isTagged) {
            drawContext.getCanvas().closeTag();
        }
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new TabRenderer((Tab) this.modelElement);
    }
}
