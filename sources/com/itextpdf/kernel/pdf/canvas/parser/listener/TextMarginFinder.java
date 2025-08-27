package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/TextMarginFinder.class */
public class TextMarginFinder implements IEventListener {
    private Rectangle textRectangle = null;

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
    public void eventOccurred(IEventData data, EventType type) {
        if (type == EventType.RENDER_TEXT) {
            TextRenderInfo info = (TextRenderInfo) data;
            if (this.textRectangle == null) {
                this.textRectangle = info.getDescentLine().getBoundingRectangle();
            } else {
                this.textRectangle = Rectangle.getCommonRectangle(this.textRectangle, info.getDescentLine().getBoundingRectangle());
            }
            this.textRectangle = Rectangle.getCommonRectangle(this.textRectangle, info.getAscentLine().getBoundingRectangle());
            return;
        }
        throw new IllegalStateException(MessageFormatUtil.format("Event type not supported: {0}", type));
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
    public Set<EventType> getSupportedEvents() {
        return new LinkedHashSet(Collections.singletonList(EventType.RENDER_TEXT));
    }

    public Rectangle getTextRectangle() {
        return this.textRectangle;
    }
}
