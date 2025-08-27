package com.itextpdf.io.font;

import com.itextpdf.io.IOException;
import com.itextpdf.io.font.cmap.CMapCidUni;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.util.IntHashtable;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CidFont.class */
public class CidFont extends FontProgram {
    private static final long serialVersionUID = 5444988003799502179L;
    private String fontName;
    private int pdfFontFlags;
    private Set<String> compatibleCmaps;

    CidFont(String fontName, Set<String> cmaps) throws NumberFormatException {
        this.fontName = fontName;
        this.compatibleCmaps = cmaps;
        this.fontNames = new FontNames();
        initializeCidFontNameAndStyle(fontName);
        Map<String, Object> fontDesc = CidFontProperties.getAllFonts().get(this.fontNames.getFontName());
        if (fontDesc == null) {
            throw new IOException("There is no such predefined font: {0}").setMessageParams(fontName);
        }
        initializeCidFontProperties(fontDesc);
    }

    CidFont(String fontName, Set<String> cmaps, Map<String, Object> fontDescription) throws NumberFormatException {
        initializeCidFontNameAndStyle(fontName);
        initializeCidFontProperties(fontDescription);
        this.compatibleCmaps = cmaps;
    }

    public boolean compatibleWith(String cmap) {
        if (cmap.equals(PdfEncodings.IDENTITY_H) || cmap.equals(PdfEncodings.IDENTITY_V)) {
            return true;
        }
        return this.compatibleCmaps != null && this.compatibleCmaps.contains(cmap);
    }

    @Override // com.itextpdf.io.font.FontProgram
    public int getKerning(Glyph glyph1, Glyph glyph2) {
        return 0;
    }

    @Override // com.itextpdf.io.font.FontProgram
    public int getPdfFontFlags() {
        return this.pdfFontFlags;
    }

    @Override // com.itextpdf.io.font.FontProgram
    public boolean isFontSpecific() {
        return false;
    }

    @Override // com.itextpdf.io.font.FontProgram
    public boolean isBuiltWith(String fontName) {
        return Objects.equals(this.fontName, fontName);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.lang.String[], java.lang.String[][]] */
    private void initializeCidFontNameAndStyle(String fontName) {
        String nameBase = trimFontStyle(fontName);
        if (nameBase.length() < fontName.length()) {
            this.fontNames.setFontName(fontName);
            this.fontNames.setStyle(fontName.substring(nameBase.length()));
        } else {
            this.fontNames.setFontName(fontName);
        }
        this.fontNames.setFullName((String[][]) new String[]{new String[]{"", "", "", this.fontNames.getFontName()}});
    }

    private void initializeCidFontProperties(Map<String, Object> fontDesc) throws NumberFormatException {
        this.fontIdentification.setPanose((String) fontDesc.get("Panose"));
        this.fontMetrics.setItalicAngle(Integer.parseInt((String) fontDesc.get("ItalicAngle")));
        this.fontMetrics.setCapHeight(Integer.parseInt((String) fontDesc.get("CapHeight")));
        this.fontMetrics.setTypoAscender(Integer.parseInt((String) fontDesc.get("Ascent")));
        this.fontMetrics.setTypoDescender(Integer.parseInt((String) fontDesc.get("Descent")));
        this.fontMetrics.setStemV(Integer.parseInt((String) fontDesc.get("StemV")));
        this.pdfFontFlags = Integer.parseInt((String) fontDesc.get("Flags"));
        String fontBBox = (String) fontDesc.get("FontBBox");
        StringTokenizer tk2 = new StringTokenizer(fontBBox, " []\r\n\t\f");
        int llx = Integer.parseInt(tk2.nextToken());
        int lly = Integer.parseInt(tk2.nextToken());
        int urx = Integer.parseInt(tk2.nextToken());
        int ury = Integer.parseInt(tk2.nextToken());
        this.fontMetrics.updateBbox(llx, lly, urx, ury);
        this.registry = (String) fontDesc.get("Registry");
        String uniMap = getCompatibleUniMap(this.registry);
        if (uniMap != null) {
            IntHashtable metrics = (IntHashtable) fontDesc.get("W");
            CMapCidUni cid2Uni = FontCache.getCid2UniCmap(uniMap);
            this.avgWidth = 0;
            for (int cid : cid2Uni.getCids()) {
                int uni = cid2Uni.lookup(cid);
                int width = metrics.containsKey(cid) ? metrics.get(cid) : 1000;
                Glyph glyph = new Glyph(cid, width, uni);
                this.avgWidth += glyph.getWidth();
                this.codeToGlyph.put(Integer.valueOf(cid), glyph);
                this.unicodeToGlyph.put(Integer.valueOf(uni), glyph);
            }
            fixSpaceIssue();
            if (this.codeToGlyph.size() != 0) {
                this.avgWidth /= this.codeToGlyph.size();
            }
        }
    }

    private static String getCompatibleUniMap(String registry) {
        String uniMap = "";
        for (String name : CidFontProperties.getRegistryNames().get(registry + "_Uni")) {
            uniMap = name;
            if (name.endsWith(StandardRoles.H)) {
                break;
            }
        }
        return uniMap;
    }
}
