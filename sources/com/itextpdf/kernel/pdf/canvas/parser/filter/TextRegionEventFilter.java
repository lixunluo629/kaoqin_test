package com.itextpdf.kernel.pdf.canvas.parser.filter;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/filter/TextRegionEventFilter.class */
public class TextRegionEventFilter implements IEventFilter {
    private final Rectangle filterRect;

    public TextRegionEventFilter(Rectangle filterRect) {
        this.filterRect = filterRect;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.filter.IEventFilter
    public boolean accept(IEventData data, EventType type) {
        if (type.equals(EventType.RENDER_TEXT)) {
            TextRenderInfo renderInfo = (TextRenderInfo) data;
            LineSegment segment = renderInfo.getBaseline();
            Vector startPoint = segment.getStartPoint();
            Vector endPoint = segment.getEndPoint();
            float x1 = startPoint.get(0);
            float y1 = startPoint.get(1);
            float x2 = endPoint.get(0);
            float y2 = endPoint.get(1);
            return this.filterRect == null || this.filterRect.intersectsLine(x1, y1, x2, y2);
        }
        return false;
    }
}
