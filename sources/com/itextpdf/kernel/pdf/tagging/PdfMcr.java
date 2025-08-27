package com.itextpdf.kernel.pdf.tagging;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagging/PdfMcr.class */
public abstract class PdfMcr extends PdfObjectWrapper<PdfObject> implements IStructureNode {
    private static final long serialVersionUID = -6453225665665080940L;
    protected PdfStructElem parent;

    public abstract int getMcid();

    protected PdfMcr(PdfObject pdfObject, PdfStructElem parent) {
        super(pdfObject);
        this.parent = parent;
    }

    public PdfDictionary getPageObject() {
        PdfObject pageObject = getPageIndirectReference().getRefersTo();
        if (pageObject instanceof PdfDictionary) {
            return (PdfDictionary) pageObject;
        }
        return null;
    }

    public PdfIndirectReference getPageIndirectReference() {
        PdfObject page = null;
        if (getPdfObject() instanceof PdfDictionary) {
            page = ((PdfDictionary) getPdfObject()).get(PdfName.Pg, false);
        }
        if (page == null) {
            page = this.parent.getPdfObject().get(PdfName.Pg, false);
        }
        if (page instanceof PdfIndirectReference) {
            return (PdfIndirectReference) page;
        }
        if (page instanceof PdfDictionary) {
            return page.getIndirectReference();
        }
        return null;
    }

    @Override // com.itextpdf.kernel.pdf.tagging.IStructureNode
    public PdfName getRole() {
        return this.parent.getRole();
    }

    @Override // com.itextpdf.kernel.pdf.tagging.IStructureNode
    public IStructureNode getParent() {
        return this.parent;
    }

    @Override // com.itextpdf.kernel.pdf.tagging.IStructureNode
    public List<IStructureNode> getKids() {
        return null;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
