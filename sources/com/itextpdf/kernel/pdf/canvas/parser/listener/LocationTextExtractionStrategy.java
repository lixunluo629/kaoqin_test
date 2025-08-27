package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.CanvasTag;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/LocationTextExtractionStrategy.class */
public class LocationTextExtractionStrategy implements ITextExtractionStrategy {
    private static boolean DUMP_STATE = false;
    private final List<TextChunk> locationalResult;
    private final ITextChunkLocationStrategy tclStrat;
    private boolean useActualText;
    private boolean rightToLeftRunDirection;
    private TextRenderInfo lastTextRenderInfo;

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/LocationTextExtractionStrategy$ITextChunkLocationStrategy.class */
    public interface ITextChunkLocationStrategy {
        ITextChunkLocation createLocation(TextRenderInfo textRenderInfo, LineSegment lineSegment);
    }

    public LocationTextExtractionStrategy() {
        this(new ITextChunkLocationStrategy() { // from class: com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.1
            @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.ITextChunkLocationStrategy
            public ITextChunkLocation createLocation(TextRenderInfo renderInfo, LineSegment baseline) {
                return new TextChunkLocationDefaultImp(baseline.getStartPoint(), baseline.getEndPoint(), renderInfo.getSingleSpaceWidth());
            }
        });
    }

    public LocationTextExtractionStrategy(ITextChunkLocationStrategy strat) {
        this.locationalResult = new ArrayList();
        this.useActualText = false;
        this.rightToLeftRunDirection = false;
        this.tclStrat = strat;
    }

    public LocationTextExtractionStrategy setUseActualText(boolean useActualText) {
        this.useActualText = useActualText;
        return this;
    }

    public LocationTextExtractionStrategy setRightToLeftRunDirection(boolean rightToLeftRunDirection) {
        this.rightToLeftRunDirection = rightToLeftRunDirection;
        return this;
    }

    public boolean isUseActualText() {
        return this.useActualText;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
    public void eventOccurred(IEventData data, EventType type) {
        if (type.equals(EventType.RENDER_TEXT)) {
            TextRenderInfo renderInfo = (TextRenderInfo) data;
            LineSegment segment = renderInfo.getBaseline();
            if (renderInfo.getRise() != 0.0f) {
                Matrix riseOffsetTransform = new Matrix(0.0f, -renderInfo.getRise());
                segment = segment.transformBy(riseOffsetTransform);
            }
            if (this.useActualText) {
                CanvasTag lastTagWithActualText = this.lastTextRenderInfo != null ? findLastTagWithActualText(this.lastTextRenderInfo.getCanvasTagHierarchy()) : null;
                if (lastTagWithActualText != null && lastTagWithActualText == findLastTagWithActualText(renderInfo.getCanvasTagHierarchy())) {
                    TextChunk lastTextChunk = this.locationalResult.get(this.locationalResult.size() - 1);
                    Vector mergedStart = new Vector(Math.min(lastTextChunk.getLocation().getStartLocation().get(0), segment.getStartPoint().get(0)), Math.min(lastTextChunk.getLocation().getStartLocation().get(1), segment.getStartPoint().get(1)), Math.min(lastTextChunk.getLocation().getStartLocation().get(2), segment.getStartPoint().get(2)));
                    Vector mergedEnd = new Vector(Math.max(lastTextChunk.getLocation().getEndLocation().get(0), segment.getEndPoint().get(0)), Math.max(lastTextChunk.getLocation().getEndLocation().get(1), segment.getEndPoint().get(1)), Math.max(lastTextChunk.getLocation().getEndLocation().get(2), segment.getEndPoint().get(2)));
                    TextChunk merged = new TextChunk(lastTextChunk.getText(), this.tclStrat.createLocation(renderInfo, new LineSegment(mergedStart, mergedEnd)));
                    this.locationalResult.set(this.locationalResult.size() - 1, merged);
                } else {
                    String actualText = renderInfo.getActualText();
                    TextChunk tc = new TextChunk(actualText != null ? actualText : renderInfo.getText(), this.tclStrat.createLocation(renderInfo, segment));
                    this.locationalResult.add(tc);
                }
            } else {
                TextChunk tc2 = new TextChunk(renderInfo.getText(), this.tclStrat.createLocation(renderInfo, segment));
                this.locationalResult.add(tc2);
            }
            this.lastTextRenderInfo = renderInfo;
        }
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
    public Set<EventType> getSupportedEvents() {
        return null;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy
    public String getResultantText() {
        if (DUMP_STATE) {
            dumpState();
        }
        List<TextChunk> textChunks = new ArrayList<>(this.locationalResult);
        sortWithMarks(textChunks);
        StringBuilder sb = new StringBuilder();
        TextChunk lastChunk = null;
        for (TextChunk chunk : textChunks) {
            if (lastChunk == null) {
                sb.append(chunk.text);
            } else if (chunk.sameLine(lastChunk)) {
                if (isChunkAtWordBoundary(chunk, lastChunk) && !startsWithSpace(chunk.text) && !endsWithSpace(lastChunk.text)) {
                    sb.append(' ');
                }
                sb.append(chunk.text);
            } else {
                sb.append('\n');
                sb.append(chunk.text);
            }
            lastChunk = chunk;
        }
        return sb.toString();
    }

    protected boolean isChunkAtWordBoundary(TextChunk chunk, TextChunk previousChunk) {
        return chunk.getLocation().isAtWordBoundary(previousChunk.getLocation());
    }

    private boolean startsWithSpace(String str) {
        return str.length() != 0 && str.charAt(0) == ' ';
    }

    private boolean endsWithSpace(String str) {
        return str.length() != 0 && str.charAt(str.length() - 1) == ' ';
    }

    private void dumpState() {
        for (TextChunk location : this.locationalResult) {
            location.printDiagnostics();
            System.out.println();
        }
    }

    private CanvasTag findLastTagWithActualText(List<CanvasTag> canvasTagHierarchy) {
        CanvasTag lastActualText = null;
        Iterator<CanvasTag> it = canvasTagHierarchy.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            CanvasTag tag = it.next();
            if (tag.getActualText() != null) {
                lastActualText = tag;
                break;
            }
        }
        return lastActualText;
    }

    private void sortWithMarks(List<TextChunk> textChunks) {
        Map<TextChunk, TextChunkMarks> marks = new HashMap<>();
        List<TextChunk> toSort = new ArrayList<>();
        for (int markInd = 0; markInd < textChunks.size(); markInd++) {
            ITextChunkLocation location = textChunks.get(markInd).getLocation();
            if (location.getStartLocation().equals(location.getEndLocation())) {
                boolean foundBaseToAttachTo = false;
                int baseInd = 0;
                while (true) {
                    if (baseInd >= textChunks.size()) {
                        break;
                    }
                    if (markInd != baseInd) {
                        ITextChunkLocation baseLocation = textChunks.get(baseInd).getLocation();
                        if (!baseLocation.getStartLocation().equals(baseLocation.getEndLocation()) && TextChunkLocationDefaultImp.containsMark(baseLocation, location)) {
                            TextChunkMarks currentMarks = marks.get(textChunks.get(baseInd));
                            if (currentMarks == null) {
                                currentMarks = new TextChunkMarks();
                                marks.put(textChunks.get(baseInd), currentMarks);
                            }
                            if (markInd < baseInd) {
                                currentMarks.preceding.add(textChunks.get(markInd));
                            } else {
                                currentMarks.succeeding.add(textChunks.get(markInd));
                            }
                            foundBaseToAttachTo = true;
                        }
                    }
                    baseInd++;
                }
                if (!foundBaseToAttachTo) {
                    toSort.add(textChunks.get(markInd));
                }
            } else {
                toSort.add(textChunks.get(markInd));
            }
        }
        Collections.sort(toSort, new TextChunkLocationBasedComparator(new DefaultTextChunkLocationComparator(!this.rightToLeftRunDirection)));
        textChunks.clear();
        for (TextChunk current : toSort) {
            TextChunkMarks currentMarks2 = marks.get(current);
            if (currentMarks2 != null) {
                if (!this.rightToLeftRunDirection) {
                    for (int j = 0; j < currentMarks2.preceding.size(); j++) {
                        textChunks.add(currentMarks2.preceding.get(j));
                    }
                } else {
                    for (int j2 = currentMarks2.succeeding.size() - 1; j2 >= 0; j2--) {
                        textChunks.add(currentMarks2.succeeding.get(j2));
                    }
                }
            }
            textChunks.add(current);
            if (currentMarks2 != null) {
                if (!this.rightToLeftRunDirection) {
                    for (int j3 = 0; j3 < currentMarks2.succeeding.size(); j3++) {
                        textChunks.add(currentMarks2.succeeding.get(j3));
                    }
                } else {
                    for (int j4 = currentMarks2.preceding.size() - 1; j4 >= 0; j4--) {
                        textChunks.add(currentMarks2.preceding.get(j4));
                    }
                }
            }
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/LocationTextExtractionStrategy$TextChunkMarks.class */
    private static class TextChunkMarks {
        List<TextChunk> preceding;
        List<TextChunk> succeeding;

        private TextChunkMarks() {
            this.preceding = new ArrayList();
            this.succeeding = new ArrayList();
        }
    }
}
