package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.xmp.XMPConst;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.properties.XMPProperty;
import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfAConformanceLevel.class */
public class PdfAConformanceLevel implements Serializable {
    public static final PdfAConformanceLevel PDF_A_1A = new PdfAConformanceLevel("1", "A");
    public static final PdfAConformanceLevel PDF_A_1B = new PdfAConformanceLevel("1", "B");
    public static final PdfAConformanceLevel PDF_A_2A = new PdfAConformanceLevel("2", "A");
    public static final PdfAConformanceLevel PDF_A_2B = new PdfAConformanceLevel("2", "B");
    public static final PdfAConformanceLevel PDF_A_2U = new PdfAConformanceLevel("2", "U");
    public static final PdfAConformanceLevel PDF_A_3A = new PdfAConformanceLevel("3", "A");
    public static final PdfAConformanceLevel PDF_A_3B = new PdfAConformanceLevel("3", "B");
    public static final PdfAConformanceLevel PDF_A_3U = new PdfAConformanceLevel("3", "U");
    private static final long serialVersionUID = 1481878095812910587L;
    private final String conformance;
    private final String part;

    private PdfAConformanceLevel(String part, String conformance) {
        this.conformance = conformance;
        this.part = part;
    }

    public String getConformance() {
        return this.conformance;
    }

    public String getPart() {
        return this.part;
    }

    public static PdfAConformanceLevel getConformanceLevel(String part, String conformance) {
        boolean aLevel;
        boolean bLevel;
        boolean uLevel;
        String lowLetter = conformance.toUpperCase();
        aLevel = "A".equals(lowLetter);
        bLevel = "B".equals(lowLetter);
        uLevel = "U".equals(lowLetter);
        switch (part) {
            case "1":
                if (aLevel) {
                    return PDF_A_1A;
                }
                if (bLevel) {
                    return PDF_A_1B;
                }
                return null;
            case "2":
                if (aLevel) {
                    return PDF_A_2A;
                }
                if (bLevel) {
                    return PDF_A_2B;
                }
                if (uLevel) {
                    return PDF_A_2U;
                }
                return null;
            case "3":
                if (aLevel) {
                    return PDF_A_3A;
                }
                if (bLevel) {
                    return PDF_A_3B;
                }
                if (uLevel) {
                    return PDF_A_3U;
                }
                return null;
            default:
                return null;
        }
    }

    public static PdfAConformanceLevel getConformanceLevel(XMPMeta meta) {
        XMPProperty conformanceXmpProperty = null;
        XMPProperty partXmpProperty = null;
        try {
            conformanceXmpProperty = meta.getProperty("http://www.aiim.org/pdfa/ns/id/", XMPConst.CONFORMANCE);
            partXmpProperty = meta.getProperty("http://www.aiim.org/pdfa/ns/id/", "part");
        } catch (XMPException e) {
        }
        if (conformanceXmpProperty == null || partXmpProperty == null) {
            return null;
        }
        String conformance = conformanceXmpProperty.getValue();
        String part = partXmpProperty.getValue();
        return getConformanceLevel(part, conformance);
    }
}
