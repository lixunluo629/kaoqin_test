package com.itextpdf.kernel.pdf;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfDeveloperExtension.class */
public class PdfDeveloperExtension {
    public static final PdfDeveloperExtension ADOBE_1_7_EXTENSIONLEVEL3 = new PdfDeveloperExtension(PdfName.ADBE, PdfName.Pdf_Version_1_7, 3);
    public static final PdfDeveloperExtension ESIC_1_7_EXTENSIONLEVEL2 = new PdfDeveloperExtension(PdfName.ESIC, PdfName.Pdf_Version_1_7, 2);
    public static final PdfDeveloperExtension ESIC_1_7_EXTENSIONLEVEL5 = new PdfDeveloperExtension(PdfName.ESIC, PdfName.Pdf_Version_1_7, 5);
    protected PdfName prefix;
    protected PdfName baseVersion;
    protected int extensionLevel;

    public PdfDeveloperExtension(PdfName prefix, PdfName baseVersion, int extensionLevel) {
        this.prefix = prefix;
        this.baseVersion = baseVersion;
        this.extensionLevel = extensionLevel;
    }

    public PdfName getPrefix() {
        return this.prefix;
    }

    public PdfName getBaseVersion() {
        return this.baseVersion;
    }

    public int getExtensionLevel() {
        return this.extensionLevel;
    }

    public PdfDictionary getDeveloperExtensions() {
        PdfDictionary developerextensions = new PdfDictionary();
        developerextensions.put(PdfName.BaseVersion, this.baseVersion);
        developerextensions.put(PdfName.ExtensionLevel, new PdfNumber(this.extensionLevel));
        return developerextensions;
    }
}
