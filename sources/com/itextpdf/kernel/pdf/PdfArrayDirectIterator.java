package com.itextpdf.kernel.pdf;

import java.util.Iterator;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfArrayDirectIterator.class */
class PdfArrayDirectIterator implements Iterator<PdfObject> {
    Iterator<PdfObject> array;

    PdfArrayDirectIterator(List<PdfObject> array) {
        this.array = array.iterator();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.array.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public PdfObject next() {
        PdfObject obj = this.array.next();
        if (obj.isIndirectReference()) {
            obj = ((PdfIndirectReference) obj).getRefersTo(true);
        }
        return obj;
    }

    @Override // java.util.Iterator
    public void remove() {
        this.array.remove();
    }
}
