package com.itextpdf.kernel.pdf;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfDictionaryEntrySet.class */
class PdfDictionaryEntrySet extends AbstractSet<Map.Entry<PdfName, PdfObject>> {
    private final Set<Map.Entry<PdfName, PdfObject>> set;

    PdfDictionaryEntrySet(Set<Map.Entry<PdfName, PdfObject>> set) {
        this.set = set;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o) {
        return this.set.contains(o) || super.contains(o);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o) {
        return this.set.remove(o) || super.remove(o);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<Map.Entry<PdfName, PdfObject>> iterator() {
        return new DirectIterator(this.set.iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.set.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.set.clear();
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfDictionaryEntrySet$DirectIterator.class */
    private static class DirectIterator implements Iterator<Map.Entry<PdfName, PdfObject>> {
        Iterator<Map.Entry<PdfName, PdfObject>> parentIterator;

        public DirectIterator(Iterator<Map.Entry<PdfName, PdfObject>> parentIterator) {
            this.parentIterator = parentIterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.parentIterator.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Map.Entry<PdfName, PdfObject> next() {
            return new DirectEntry(this.parentIterator.next());
        }

        @Override // java.util.Iterator
        public void remove() {
            this.parentIterator.remove();
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfDictionaryEntrySet$DirectEntry.class */
    private static class DirectEntry implements Map.Entry<PdfName, PdfObject> {
        Map.Entry<PdfName, PdfObject> entry;

        DirectEntry(Map.Entry<PdfName, PdfObject> entry) {
            this.entry = entry;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public PdfName getKey() {
            return this.entry.getKey();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public PdfObject getValue() {
            PdfObject obj = this.entry.getValue();
            if (obj != null && obj.isIndirectReference()) {
                obj = ((PdfIndirectReference) obj).getRefersTo(true);
            }
            return obj;
        }

        @Override // java.util.Map.Entry
        public PdfObject setValue(PdfObject value) {
            return this.entry.setValue(value);
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry) o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 != null && k1.equals(k2)) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 != null && v1.equals(v2)) {
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }
    }
}
