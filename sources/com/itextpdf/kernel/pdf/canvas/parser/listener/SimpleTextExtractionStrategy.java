package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/SimpleTextExtractionStrategy.class */
public class SimpleTextExtractionStrategy implements ITextExtractionStrategy {
    private Vector lastStart;
    private Vector lastEnd;
    private final StringBuilder result = new StringBuilder();

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
    public void eventOccurred(IEventData data, EventType type) {
        if (type.equals(EventType.RENDER_TEXT)) {
            TextRenderInfo renderInfo = (TextRenderInfo) data;
            boolean firstRender = this.result.length() == 0;
            boolean hardReturn = false;
            LineSegment segment = renderInfo.getBaseline();
            Vector start = segment.getStartPoint();
            Vector end = segment.getEndPoint();
            if (!firstRender) {
                Vector x1 = this.lastStart;
                Vector x2 = this.lastEnd;
                float dist = x2.subtract(x1).cross(x1.subtract(start)).lengthSquared() / x2.subtract(x1).lengthSquared();
                if (dist > 1.0f) {
                    hardReturn = true;
                }
            }
            if (hardReturn) {
                appendTextChunk(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            } else if (!firstRender && this.result.charAt(this.result.length() - 1) != ' ' && renderInfo.getText().length() > 0 && renderInfo.getText().charAt(0) != ' ') {
                float spacing = this.lastEnd.subtract(start).length();
                if (spacing > renderInfo.getSingleSpaceWidth() / 2.0f) {
                    appendTextChunk(SymbolConstants.SPACE_SYMBOL);
                }
            }
            appendTextChunk(renderInfo.getText());
            this.lastStart = start;
            this.lastEnd = end;
        }
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
    public Set<EventType> getSupportedEvents() {
        return Collections.unmodifiableSet(new LinkedHashSet(Collections.singletonList(EventType.RENDER_TEXT)));
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy
    public String getResultantText() {
        return this.result.toString();
    }

    protected final void appendTextChunk(CharSequence text) {
        this.result.append(text);
    }
}
