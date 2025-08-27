package com.itextpdf.kernel.pdf;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfDictionaryValues.class */
class PdfDictionaryValues extends AbstractCollection<PdfObject> {
    private final Collection<PdfObject> collection;

    PdfDictionaryValues(Collection<PdfObject> collection) {
        this.collection = collection;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean add(PdfObject object) {
        return this.collection.add(object);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object o) {
        if (this.collection.contains(o)) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Iterator<PdfObject> it = iterator();
        while (it.hasNext()) {
            PdfObject pdfObject = it.next();
            if (PdfObject.equalContent((PdfObject) o, pdfObject)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean remove(Object o) {
        if (this.collection.remove(o)) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Iterator<PdfObject> it = iterator();
        while (it.hasNext()) {
            if (PdfObject.equalContent((PdfObject) o, it.next())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.collection.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.collection.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<PdfObject> iterator() {
        return new DirectIterator(this.collection.iterator());
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfDictionaryValues$DirectIterator.class */
    private static class DirectIterator implements Iterator<PdfObject> {
        Iterator<PdfObject> parentIterator;

        DirectIterator(Iterator<PdfObject> parentIterator) {
            this.parentIterator = parentIterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.parentIterator.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public PdfObject next() {
            PdfObject obj = this.parentIterator.next();
            if (obj != null && obj.isIndirectReference()) {
                obj = ((PdfIndirectReference) obj).getRefersTo(true);
            }
            return obj;
        }

        @Override // java.util.Iterator
        public void remove() {
            this.parentIterator.remove();
        }
    }
}
