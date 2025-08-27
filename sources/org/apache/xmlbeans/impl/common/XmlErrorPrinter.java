package org.apache.xmlbeans.impl.common;

import java.net.URI;
import java.util.AbstractCollection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.xmlbeans.XmlError;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XmlErrorPrinter.class */
public class XmlErrorPrinter extends AbstractCollection {
    private boolean _noisy;
    private URI _baseURI;

    public XmlErrorPrinter(boolean noisy, URI baseURI) {
        this._noisy = noisy;
        this._baseURI = baseURI;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean add(Object o) {
        if (o instanceof XmlError) {
            XmlError err = (XmlError) o;
            if (err.getSeverity() == 0 || err.getSeverity() == 1) {
                System.err.println(err.toString(this._baseURI));
                return false;
            }
            if (this._noisy) {
                System.out.println(err.toString(this._baseURI));
                return false;
            }
            return false;
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return 0;
    }
}
