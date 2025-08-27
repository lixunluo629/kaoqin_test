package org.apache.xmlbeans;

import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/QNameCache.class */
public final class QNameCache {
    private static final float DEFAULT_LOAD = 0.7f;
    private final float loadFactor;
    private int numEntries;
    private int threshold;
    private int hashmask;
    private QName[] table;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !QNameCache.class.desiredAssertionStatus();
    }

    public QNameCache(int initialCapacity, float loadFactor) {
        this.numEntries = 0;
        if (!$assertionsDisabled && initialCapacity <= 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (loadFactor <= 0.0f || loadFactor >= 1.0f)) {
            throw new AssertionError();
        }
        int i = 16;
        while (true) {
            int capacity = i;
            if (capacity < initialCapacity) {
                i = capacity << 1;
            } else {
                this.loadFactor = loadFactor;
                this.hashmask = capacity - 1;
                this.threshold = (int) (capacity * loadFactor);
                this.table = new QName[capacity];
                return;
            }
        }
    }

    public QNameCache(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD);
    }

    public QName getName(String uri, String localName) {
        return getName(uri, localName, "");
    }

    public QName getName(String uri, String localName, String prefix) {
        if (!$assertionsDisabled && localName == null) {
            throw new AssertionError();
        }
        if (uri == null) {
            uri = "";
        }
        if (prefix == null) {
            prefix = "";
        }
        int iHash = hash(uri, localName, prefix);
        int i = this.hashmask;
        while (true) {
            int index = iHash & i;
            QName q = this.table[index];
            if (q == null) {
                this.numEntries++;
                if (this.numEntries >= this.threshold) {
                    rehash();
                }
                QName[] qNameArr = this.table;
                QName qName = new QName(uri, localName, prefix);
                qNameArr[index] = qName;
                return qName;
            }
            if (equals(q, uri, localName, prefix)) {
                return q;
            }
            iHash = index - 1;
            i = this.hashmask;
        }
    }

    private void rehash() {
        int newIndex;
        int newLength = this.table.length * 2;
        QName[] newTable = new QName[newLength];
        int newHashmask = newLength - 1;
        for (int i = 0; i < this.table.length; i++) {
            QName q = this.table[i];
            if (q != null) {
                int iHash = hash(q.getNamespaceURI(), q.getLocalPart(), q.getPrefix());
                while (true) {
                    newIndex = iHash & newHashmask;
                    if (newTable[newIndex] == null) {
                        break;
                    } else {
                        iHash = newIndex - 1;
                    }
                }
                newTable[newIndex] = q;
            }
        }
        this.table = newTable;
        this.hashmask = newHashmask;
        this.threshold = (int) (newLength * this.loadFactor);
    }

    private static int hash(String uri, String localName, String prefix) {
        int h = 0 + (prefix.hashCode() << 10);
        return h + (uri.hashCode() << 5) + localName.hashCode();
    }

    private static boolean equals(QName q, String uri, String localName, String prefix) {
        return q.getLocalPart().equals(localName) && q.getNamespaceURI().equals(uri) && q.getPrefix().equals(prefix);
    }
}
