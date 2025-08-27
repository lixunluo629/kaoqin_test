package com.itextpdf.kernel.pdf.layer;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/layer/PdfLayerMembership.class */
public class PdfLayerMembership extends PdfObjectWrapper<PdfDictionary> implements IPdfOCG {
    private static final long serialVersionUID = -597407628148657784L;

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper, com.itextpdf.kernel.pdf.layer.IPdfOCG
    public /* bridge */ /* synthetic */ PdfDictionary getPdfObject() {
        return (PdfDictionary) super.getPdfObject();
    }

    public PdfLayerMembership(PdfDocument doc) {
        super(new PdfDictionary());
        makeIndirect(doc);
        ((PdfDictionary) getPdfObject()).put(PdfName.Type, PdfName.OCMD);
    }

    public PdfLayerMembership(PdfDictionary membershipDictionary) {
        super(membershipDictionary);
        ensureObjectIsAddedToDocument(membershipDictionary);
        if (!PdfName.OCMD.equals(membershipDictionary.getAsName(PdfName.Type))) {
            throw new IllegalArgumentException("Invalid membershipDictionary.");
        }
    }

    public Collection<PdfLayer> getLayers() {
        PdfObject layers = ((PdfDictionary) getPdfObject()).get(PdfName.OCGs);
        if (layers instanceof PdfDictionary) {
            List<PdfLayer> list = new ArrayList<>();
            list.add(new PdfLayer((PdfDictionary) ((PdfDictionary) layers).makeIndirect(getDocument())));
            return list;
        }
        if (layers instanceof PdfArray) {
            List<PdfLayer> layerList = new ArrayList<>();
            for (int ind = 0; ind < ((PdfArray) layers).size(); ind++) {
                layerList.add(new PdfLayer(((PdfArray) ((PdfArray) layers).makeIndirect(getDocument())).getAsDictionary(ind)));
            }
            return layerList;
        }
        return null;
    }

    public void addLayer(PdfLayer layer) {
        PdfArray layers = ((PdfDictionary) getPdfObject()).getAsArray(PdfName.OCGs);
        if (layers == null) {
            layers = new PdfArray();
            ((PdfDictionary) getPdfObject()).put(PdfName.OCGs, layers);
        }
        layers.add(layer.getPdfObject());
        layers.setModified();
    }

    public void setVisibilityPolicy(PdfName visibilityPolicy) {
        if (visibilityPolicy == null || (!PdfName.AllOn.equals(visibilityPolicy) && !PdfName.AnyOn.equals(visibilityPolicy) && !PdfName.AnyOff.equals(visibilityPolicy) && !PdfName.AllOff.equals(visibilityPolicy))) {
            throw new IllegalArgumentException("Argument: visibilityPolicy");
        }
        ((PdfDictionary) getPdfObject()).put(PdfName.P, visibilityPolicy);
        ((PdfDictionary) getPdfObject()).setModified();
    }

    public PdfName getVisibilityPolicy() {
        PdfName visibilityPolicy = ((PdfDictionary) getPdfObject()).getAsName(PdfName.P);
        if (visibilityPolicy == null || (!visibilityPolicy.equals(PdfName.AllOn) && !visibilityPolicy.equals(PdfName.AllOff) && !visibilityPolicy.equals(PdfName.AnyOn) && !visibilityPolicy.equals(PdfName.AnyOff))) {
            return PdfName.AnyOn;
        }
        return visibilityPolicy;
    }

    public void setVisibilityExpression(PdfVisibilityExpression visibilityExpression) {
        ((PdfDictionary) getPdfObject()).put(PdfName.VE, visibilityExpression.getPdfObject());
        ((PdfDictionary) getPdfObject()).setModified();
    }

    public PdfVisibilityExpression getVisibilityExpression() {
        PdfArray ve = ((PdfDictionary) getPdfObject()).getAsArray(PdfName.VE);
        if (ve != null) {
            return new PdfVisibilityExpression(ve);
        }
        return null;
    }

    @Override // com.itextpdf.kernel.pdf.layer.IPdfOCG
    public PdfIndirectReference getIndirectReference() {
        return ((PdfDictionary) getPdfObject()).getIndirectReference();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    protected PdfDocument getDocument() {
        return ((PdfDictionary) getPdfObject()).getIndirectReference().getDocument();
    }
}
