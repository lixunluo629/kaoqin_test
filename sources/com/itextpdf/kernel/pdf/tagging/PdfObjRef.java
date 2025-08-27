package com.itextpdf.kernel.pdf.tagging;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagging/PdfObjRef.class */
public class PdfObjRef extends PdfMcr {
    private static final long serialVersionUID = 344098256404114906L;

    public PdfObjRef(PdfDictionary pdfObject, PdfStructElem parent) {
        super(pdfObject, parent);
    }

    public PdfObjRef(PdfAnnotation annot, PdfStructElem parent, int nextStructParentIndex) {
        super(new PdfDictionary(), parent);
        annot.getPdfObject().put(PdfName.StructParent, new PdfNumber(nextStructParentIndex));
        annot.setModified();
        PdfDictionary dict = (PdfDictionary) getPdfObject();
        dict.put(PdfName.Type, PdfName.OBJR);
        dict.put(PdfName.Obj, annot.getPdfObject());
    }

    @Override // com.itextpdf.kernel.pdf.tagging.PdfMcr
    public int getMcid() {
        return -1;
    }

    @Override // com.itextpdf.kernel.pdf.tagging.PdfMcr
    public PdfDictionary getPageObject() {
        return super.getPageObject();
    }

    public PdfDictionary getReferencedObject() {
        return ((PdfDictionary) getPdfObject()).getAsDictionary(PdfName.Obj);
    }

    private static PdfDocument getDocEnsureIndirect(PdfStructElem structElem) {
        PdfIndirectReference indRef = structElem.getPdfObject().getIndirectReference();
        if (indRef == null) {
            throw new PdfException(PdfException.StructureElementDictionaryShallBeAnIndirectObjectInOrderToHaveChildren);
        }
        return indRef.getDocument();
    }
}
