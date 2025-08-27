package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/CharacterRenderInfo.class */
public class CharacterRenderInfo extends TextChunk {
    private Rectangle boundingBox;

    static StringConversionInfo mapString(List<CharacterRenderInfo> cris) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        CharacterRenderInfo lastChunk = null;
        for (int i = 0; i < cris.size(); i++) {
            CharacterRenderInfo chunk = cris.get(i);
            if (lastChunk == null) {
                putCharsWithIndex(chunk.getText(), i, indexMap, sb);
            } else if (chunk.sameLine(lastChunk)) {
                if (chunk.getLocation().isAtWordBoundary(lastChunk.getLocation()) && !chunk.getText().startsWith(SymbolConstants.SPACE_SYMBOL) && !chunk.getText().endsWith(SymbolConstants.SPACE_SYMBOL)) {
                    sb.append(' ');
                }
                putCharsWithIndex(chunk.getText(), i, indexMap, sb);
            } else {
                putCharsWithIndex(chunk.getText(), i, indexMap, sb);
            }
            lastChunk = chunk;
        }
        StringConversionInfo ret = new StringConversionInfo();
        ret.indexMap = indexMap;
        ret.text = sb.toString();
        return ret;
    }

    private static void putCharsWithIndex(CharSequence seq, int index, Map<Integer, Integer> indexMap, StringBuilder sb) {
        int charCount = seq.length();
        for (int i = 0; i < charCount; i++) {
            indexMap.put(Integer.valueOf(sb.length()), Integer.valueOf(index));
            sb.append(seq.charAt(i));
        }
    }

    public CharacterRenderInfo(TextRenderInfo tri) {
        super(tri == null ? "" : tri.getText(), tri == null ? null : getLocation(tri));
        if (tri == null) {
            throw new IllegalArgumentException("TextRenderInfo argument is not nullable.");
        }
        List<Point> points = new ArrayList<>();
        points.add(new Point(tri.getDescentLine().getStartPoint().get(0), tri.getDescentLine().getStartPoint().get(1)));
        points.add(new Point(tri.getDescentLine().getEndPoint().get(0), tri.getDescentLine().getEndPoint().get(1)));
        points.add(new Point(tri.getAscentLine().getStartPoint().get(0), tri.getAscentLine().getStartPoint().get(1)));
        points.add(new Point(tri.getAscentLine().getEndPoint().get(0), tri.getAscentLine().getEndPoint().get(1)));
        this.boundingBox = Rectangle.calculateBBox(points);
    }

    public Rectangle getBoundingBox() {
        return this.boundingBox;
    }

    private static ITextChunkLocation getLocation(TextRenderInfo tri) {
        LineSegment baseline = tri.getBaseline();
        return new TextChunkLocationDefaultImp(baseline.getStartPoint(), baseline.getEndPoint(), tri.getSingleSpaceWidth());
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/CharacterRenderInfo$StringConversionInfo.class */
    static class StringConversionInfo {
        Map<Integer, Integer> indexMap;
        String text;

        StringConversionInfo() {
        }
    }
}
