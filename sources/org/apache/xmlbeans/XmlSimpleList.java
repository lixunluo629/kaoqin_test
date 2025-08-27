package org.apache.xmlbeans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlSimpleList.class */
public class XmlSimpleList implements List, Serializable {
    private static final long serialVersionUID = 1;
    private List underlying;

    public XmlSimpleList(List list) {
        this.underlying = list;
    }

    @Override // java.util.List, java.util.Collection
    public int size() {
        return this.underlying.size();
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return this.underlying.isEmpty();
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object o) {
        return this.underlying.contains(o);
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection coll) {
        return this.underlying.containsAll(coll);
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        return this.underlying.toArray();
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray(Object[] a) {
        return this.underlying.toArray(a);
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public Object get(int index) {
        return this.underlying.get(index);
    }

    @Override // java.util.List
    public Object set(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public void add(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public Object remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public int indexOf(Object o) {
        return this.underlying.indexOf(o);
    }

    @Override // java.util.List
    public int lastIndexOf(Object o) {
        return this.underlying.lastIndexOf(o);
    }

    @Override // java.util.List
    public boolean addAll(int index, Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public List subList(int from, int to) {
        return new XmlSimpleList(this.underlying.subList(from, to));
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        return new Iterator() { // from class: org.apache.xmlbeans.XmlSimpleList.1
            Iterator i;

            {
                this.i = XmlSimpleList.this.underlying.iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.i.hasNext();
            }

            @Override // java.util.Iterator
            public Object next() {
                return this.i.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // java.util.List
    public ListIterator listIterator() {
        return listIterator(0);
    }

    @Override // java.util.List
    public ListIterator listIterator(final int index) {
        return new ListIterator() { // from class: org.apache.xmlbeans.XmlSimpleList.2
            ListIterator i;

            {
                this.i = XmlSimpleList.this.underlying.listIterator(index);
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public boolean hasNext() {
                return this.i.hasNext();
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public Object next() {
                return this.i.next();
            }

            @Override // java.util.ListIterator
            public boolean hasPrevious() {
                return this.i.hasPrevious();
            }

            @Override // java.util.ListIterator
            public Object previous() {
                return this.i.previous();
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.i.nextIndex();
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.i.previousIndex();
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.ListIterator
            public void set(Object o) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.ListIterator
            public void add(Object o) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private String stringValue(Object o) {
        if (o instanceof SimpleValue) {
            return ((SimpleValue) o).stringValue();
        }
        return o.toString();
    }

    public String toString() {
        int size = this.underlying.size();
        if (size == 0) {
            return "";
        }
        String first = stringValue(this.underlying.get(0));
        if (size == 1) {
            return first;
        }
        StringBuffer result = new StringBuffer(first);
        for (int i = 1; i < size; i++) {
            result.append(' ');
            result.append(stringValue(this.underlying.get(i)));
        }
        return result.toString();
    }

    @Override // java.util.List, java.util.Collection
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XmlSimpleList)) {
            return false;
        }
        XmlSimpleList xmlSimpleList = (XmlSimpleList) o;
        List underlying2 = xmlSimpleList.underlying;
        int size = this.underlying.size();
        if (size != underlying2.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            Object item = this.underlying.get(i);
            Object item2 = underlying2.get(i);
            if (item == null) {
                if (item2 != null) {
                    return false;
                }
            } else if (!item.equals(item2)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.List, java.util.Collection
    public int hashCode() {
        int size = this.underlying.size();
        int hash = 0;
        for (int i = 0; i < size; i++) {
            Object item = this.underlying.get(i);
            hash = (hash * 19) + item.hashCode();
        }
        return hash;
    }
}
