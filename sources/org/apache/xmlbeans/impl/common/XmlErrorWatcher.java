package org.apache.xmlbeans.impl.common;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.xmlbeans.XmlError;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XmlErrorWatcher.class */
public class XmlErrorWatcher extends AbstractCollection {
    private Collection _underlying;
    private XmlError _firstError;

    public XmlErrorWatcher(Collection underlying) {
        this._underlying = underlying;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean add(Object o) {
        if (this._firstError == null && (o instanceof XmlError) && ((XmlError) o).getSeverity() == 0) {
            this._firstError = (XmlError) o;
        }
        if (this._underlying == null) {
            return false;
        }
        return this._underlying.add(o);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        if (this._underlying == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        return this._underlying.iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        if (this._underlying == null) {
            return 0;
        }
        return this._underlying.size();
    }

    public boolean hasError() {
        return this._firstError != null;
    }

    public XmlError firstError() {
        return this._firstError;
    }
}
