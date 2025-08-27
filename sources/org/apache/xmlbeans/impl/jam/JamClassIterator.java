package org.apache.xmlbeans.impl.jam;

import java.util.Iterator;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JamClassIterator.class */
public class JamClassIterator implements Iterator {
    private JamClassLoader mLoader;
    private String[] mClassNames;
    private int mIndex = 0;

    public JamClassIterator(JamClassLoader loader, String[] classes) {
        if (loader == null) {
            throw new IllegalArgumentException("null loader");
        }
        if (classes == null) {
            throw new IllegalArgumentException("null classes");
        }
        this.mLoader = loader;
        this.mClassNames = classes;
    }

    public JClass nextClass() {
        if (!hasNext()) {
            throw new IndexOutOfBoundsException();
        }
        this.mIndex++;
        return this.mLoader.loadClass(this.mClassNames[this.mIndex - 1]);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.mIndex < this.mClassNames.length;
    }

    @Override // java.util.Iterator
    public Object next() {
        return nextClass();
    }

    public int getSize() {
        return this.mClassNames.length;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
