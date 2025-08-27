package org.apache.xmlbeans.impl.soap;

import java.util.Iterator;
import java.util.Vector;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/MimeHeaders.class */
public class MimeHeaders {
    protected Vector headers = new Vector();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/MimeHeaders$MatchingIterator.class */
    class MatchingIterator implements Iterator {
        private boolean match;
        private Iterator iterator;
        private String[] names;
        private Object nextHeader;

        private Object nextMatch() {
            while (this.iterator.hasNext()) {
                MimeHeader mimeheader = (MimeHeader) this.iterator.next();
                if (this.names == null) {
                    if (this.match) {
                        return null;
                    }
                    return mimeheader;
                }
                int i = 0;
                while (true) {
                    if (i < this.names.length) {
                        if (!mimeheader.getName().equalsIgnoreCase(this.names[i])) {
                            i++;
                        } else if (this.match) {
                            return mimeheader;
                        }
                    } else if (!this.match) {
                        return mimeheader;
                    }
                }
            }
            return null;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextHeader == null) {
                this.nextHeader = nextMatch();
            }
            return this.nextHeader != null;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (this.nextHeader != null) {
                Object obj = this.nextHeader;
                this.nextHeader = null;
                return obj;
            }
            if (hasNext()) {
                return this.nextHeader;
            }
            return null;
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }

        MatchingIterator(String[] as, boolean flag) {
            this.match = flag;
            this.names = as;
            this.iterator = MimeHeaders.this.headers.iterator();
        }
    }

    public String[] getHeader(String name) {
        Vector vector = new Vector();
        for (int i = 0; i < this.headers.size(); i++) {
            MimeHeader mimeheader = (MimeHeader) this.headers.elementAt(i);
            if (mimeheader.getName().equalsIgnoreCase(name) && mimeheader.getValue() != null) {
                vector.addElement(mimeheader.getValue());
            }
        }
        if (vector.size() == 0) {
            return null;
        }
        String[] as = new String[vector.size()];
        vector.copyInto(as);
        return as;
    }

    public void setHeader(String name, String value) {
        boolean flag = false;
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Illegal MimeHeader name");
        }
        int i = 0;
        while (i < this.headers.size()) {
            MimeHeader mimeheader = (MimeHeader) this.headers.elementAt(i);
            if (mimeheader.getName().equalsIgnoreCase(name)) {
                if (!flag) {
                    this.headers.setElementAt(new MimeHeader(mimeheader.getName(), value), i);
                    flag = true;
                } else {
                    int i2 = i;
                    i--;
                    this.headers.removeElementAt(i2);
                }
            }
            i++;
        }
        if (!flag) {
            addHeader(name, value);
        }
    }

    public void addHeader(String name, String value) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Illegal MimeHeader name");
        }
        int i = this.headers.size();
        for (int j = i - 1; j >= 0; j--) {
            MimeHeader mimeheader = (MimeHeader) this.headers.elementAt(j);
            if (mimeheader.getName().equalsIgnoreCase(name)) {
                this.headers.insertElementAt(new MimeHeader(name, value), j + 1);
                return;
            }
        }
        this.headers.addElement(new MimeHeader(name, value));
    }

    public void removeHeader(String name) {
        int i = 0;
        while (i < this.headers.size()) {
            MimeHeader mimeheader = (MimeHeader) this.headers.elementAt(i);
            if (mimeheader.getName().equalsIgnoreCase(name)) {
                int i2 = i;
                i--;
                this.headers.removeElementAt(i2);
            }
            i++;
        }
    }

    public void removeAllHeaders() {
        this.headers.removeAllElements();
    }

    public Iterator getAllHeaders() {
        return this.headers.iterator();
    }

    public Iterator getMatchingHeaders(String[] names) {
        return new MatchingIterator(names, true);
    }

    public Iterator getNonMatchingHeaders(String[] names) {
        return new MatchingIterator(names, false);
    }
}
