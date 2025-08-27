package com.itextpdf.kernel.pdf.layer;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/layer/PdfVisibilityExpression.class */
public class PdfVisibilityExpression extends PdfObjectWrapper<PdfArray> {
    private static final long serialVersionUID = 4152369893262322542L;

    public PdfVisibilityExpression(PdfArray visibilityExpressionArray) {
        super(visibilityExpressionArray);
        PdfName operator = visibilityExpressionArray.getAsName(0);
        if (visibilityExpressionArray.size() < 1 || (!PdfName.Or.equals(operator) && !PdfName.And.equals(operator) && !PdfName.Not.equals(operator))) {
            throw new IllegalArgumentException("Invalid visibilityExpressionArray");
        }
    }

    public PdfVisibilityExpression(PdfName operator) {
        super(new PdfArray());
        if (operator == null || (!PdfName.Or.equals(operator) && !PdfName.And.equals(operator) && !PdfName.Not.equals(operator))) {
            throw new IllegalArgumentException("Invalid operator");
        }
        getPdfObject().add(operator);
    }

    public void addOperand(PdfLayer layer) {
        getPdfObject().add(layer.getPdfObject());
        getPdfObject().setModified();
    }

    public void addOperand(PdfVisibilityExpression expression) {
        getPdfObject().add(expression.getPdfObject());
        getPdfObject().setModified();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
