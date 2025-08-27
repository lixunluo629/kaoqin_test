package com.itextpdf.kernel.pdf.canvas.wmf;

import com.itextpdf.io.font.FontEncoding;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/wmf/MetaFont.class */
public class MetaFont extends MetaObject {
    static final String[] fontNames = {"Courier", "Courier-Bold", "Courier-Oblique", "Courier-BoldOblique", "Helvetica", "Helvetica-Bold", "Helvetica-Oblique", "Helvetica-BoldOblique", "Times-Roman", "Times-Bold", "Times-Italic", "Times-BoldItalic", "Symbol", "ZapfDingbats"};
    static final int MARKER_BOLD = 1;
    static final int MARKER_ITALIC = 2;
    static final int MARKER_COURIER = 0;
    static final int MARKER_HELVETICA = 4;
    static final int MARKER_TIMES = 8;
    static final int MARKER_SYMBOL = 12;
    static final int DEFAULT_PITCH = 0;
    static final int FIXED_PITCH = 1;
    static final int VARIABLE_PITCH = 2;
    static final int FF_DONTCARE = 0;
    static final int FF_ROMAN = 1;
    static final int FF_SWISS = 2;
    static final int FF_MODERN = 3;
    static final int FF_SCRIPT = 4;
    static final int FF_DECORATIVE = 5;
    static final int BOLDTHRESHOLD = 600;
    static final int NAME_SIZE = 32;
    static final int ETO_OPAQUE = 2;
    static final int ETO_CLIPPED = 4;
    int height;
    float angle;
    int bold;
    int italic;
    boolean underline;
    boolean strikeout;
    int charset;
    int pitchAndFamily;
    String faceName;
    FontProgram font;
    FontEncoding encoding;

    public MetaFont() {
        super(3);
        this.faceName = "arial";
        this.font = null;
        this.encoding = null;
    }

    public void init(InputMeta in) throws IOException {
        int c;
        this.height = Math.abs(in.readShort());
        in.skip(2);
        this.angle = (float) ((in.readShort() / 1800.0d) * 3.141592653589793d);
        in.skip(2);
        this.bold = in.readShort() >= 600 ? 1 : 0;
        this.italic = in.readByte() != 0 ? 2 : 0;
        this.underline = in.readByte() != 0;
        this.strikeout = in.readByte() != 0;
        this.charset = in.readByte();
        in.skip(3);
        this.pitchAndFamily = in.readByte();
        byte[] name = new byte[32];
        int k = 0;
        while (k < 32 && (c = in.readByte()) != 0) {
            name[k] = (byte) c;
            k++;
        }
        try {
            this.faceName = new String(name, 0, k, "Cp1252");
        } catch (UnsupportedEncodingException e) {
            this.faceName = new String(name, 0, k);
        }
        this.faceName = this.faceName.toLowerCase();
    }

    public FontProgram getFont() throws IOException {
        String fontName;
        if (this.font != null) {
            return this.font;
        }
        FontProgram ff2 = FontProgramFactory.createRegisteredFont(this.faceName, (this.italic != 0 ? 2 : 0) | (this.bold != 0 ? 1 : 0));
        this.encoding = FontEncoding.createFontEncoding("Cp1252");
        this.font = ff2;
        if (this.font != null) {
            return this.font;
        }
        if (this.faceName.contains("courier") || this.faceName.contains("terminal") || this.faceName.contains("fixedsys")) {
            fontName = fontNames[0 + this.italic + this.bold];
        } else if (this.faceName.contains("ms sans serif") || this.faceName.contains("arial") || this.faceName.contains("system")) {
            fontName = fontNames[4 + this.italic + this.bold];
        } else if (this.faceName.contains("arial black")) {
            fontName = fontNames[4 + this.italic + 1];
        } else if (this.faceName.contains("times") || this.faceName.contains("ms serif") || this.faceName.contains("roman")) {
            fontName = fontNames[8 + this.italic + this.bold];
        } else if (this.faceName.contains("symbol")) {
            fontName = fontNames[12];
        } else {
            int pitch = this.pitchAndFamily & 3;
            int family = (this.pitchAndFamily >> 4) & 7;
            switch (family) {
                case 1:
                    fontName = fontNames[8 + this.italic + this.bold];
                    break;
                case 2:
                case 4:
                case 5:
                    fontName = fontNames[4 + this.italic + this.bold];
                    break;
                case 3:
                    fontName = fontNames[0 + this.italic + this.bold];
                    break;
                default:
                    switch (pitch) {
                        case 1:
                            fontName = fontNames[0 + this.italic + this.bold];
                            break;
                        default:
                            fontName = fontNames[4 + this.italic + this.bold];
                            break;
                    }
            }
        }
        try {
            this.font = FontProgramFactory.createFont(fontName);
            this.encoding = FontEncoding.createFontEncoding("Cp1252");
            return this.font;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public FontEncoding getEncoding() {
        return this.encoding;
    }

    public float getAngle() {
        return this.angle;
    }

    public boolean isUnderline() {
        return this.underline;
    }

    public boolean isStrikeout() {
        return this.strikeout;
    }

    public float getFontSize(MetaState state) {
        return Math.abs(state.transformY(this.height) - state.transformY(0)) * WmfImageHelper.wmfFontCorrection;
    }
}
