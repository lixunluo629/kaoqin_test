package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/GlyphEventListener.class */
public class GlyphEventListener implements IEventListener {
    protected final IEventListener delegate;

    public GlyphEventListener(IEventListener delegate) {
        this.delegate = delegate;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
    public void eventOccurred(IEventData data, EventType type) {
        if (type.equals(EventType.RENDER_TEXT)) {
            TextRenderInfo textRenderInfo = (TextRenderInfo) data;
            for (TextRenderInfo glyphRenderInfo : textRenderInfo.getCharacterRenderInfos()) {
                this.delegate.eventOccurred(glyphRenderInfo, type);
            }
            return;
        }
        this.delegate.eventOccurred(data, type);
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
    public Set<EventType> getSupportedEvents() {
        return this.delegate.getSupportedEvents();
    }
}
