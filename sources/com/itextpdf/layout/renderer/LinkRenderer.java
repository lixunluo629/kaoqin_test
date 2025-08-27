package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.layout.element.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/LinkRenderer.class */
public class LinkRenderer extends TextRenderer {
    public LinkRenderer(Link link) {
        this(link, link.getText());
    }

    public LinkRenderer(Link linkElement, String text) {
        super(linkElement, text);
    }

    @Override // com.itextpdf.layout.renderer.TextRenderer, com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void draw(DrawContext drawContext) {
        if (this.occupiedArea == null) {
            Logger logger = LoggerFactory.getLogger((Class<?>) LinkRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.OCCUPIED_AREA_HAS_NOT_BEEN_INITIALIZED, "Drawing won't be performed."));
            return;
        }
        super.draw(drawContext);
        boolean isRelativePosition = isRelativePosition();
        if (isRelativePosition) {
            applyRelativePositioningTranslation(false);
        }
    }

    @Override // com.itextpdf.layout.renderer.TextRenderer, com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new LinkRenderer((Link) this.modelElement);
    }
}
