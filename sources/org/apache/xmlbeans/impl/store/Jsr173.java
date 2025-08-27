package org.apache.xmlbeans.impl.store;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlLineNumber;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Jsr173.class */
public class Jsr173 {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Jsr173.class.desiredAssertionStatus();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Node nodeFromStream(XMLStreamReader xMLStreamReader) {
        Node nodeNodeFromStreamImpl;
        if (!(xMLStreamReader instanceof Jsr173GateWay)) {
            return null;
        }
        Jsr173GateWay gw = (Jsr173GateWay) xMLStreamReader;
        Locale l = gw._l;
        if (l.noSync()) {
            l.enter();
            try {
                Node nodeNodeFromStreamImpl2 = nodeFromStreamImpl(gw);
                l.exit();
                return nodeNodeFromStreamImpl2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                nodeNodeFromStreamImpl = nodeFromStreamImpl(gw);
                l.exit();
            } finally {
            }
        }
        return nodeNodeFromStreamImpl;
    }

    public static Node nodeFromStreamImpl(Jsr173GateWay gw) {
        Cur c = gw._xs.getStreamCur();
        return c.isNode() ? (Node) c.getDom() : (Node) null;
    }

    public static XMLStreamReader newXmlStreamReader(Cur c, Object src, int off, int cch) {
        XMLStreamReaderBase xs = new XMLStreamReaderForString(c, src, off, cch);
        if (c._locale.noSync()) {
            return new UnsyncedJsr173(c._locale, xs);
        }
        return new SyncedJsr173(c._locale, xs);
    }

    public static XMLStreamReader newXmlStreamReader(Cur c, XmlOptions options) {
        XMLStreamReaderBase xs;
        XmlOptions options2 = XmlOptions.maskNull(options);
        boolean inner = options2.hasOption(XmlOptions.SAVE_INNER) && !options2.hasOption(XmlOptions.SAVE_OUTER);
        int k = c.kind();
        if (k == 0 || k < 0) {
            xs = new XMLStreamReaderForString(c, c.getChars(-1), c._offSrc, c._cchSrc);
        } else if (inner) {
            if (!c.hasAttrs() && !c.hasChildren()) {
                xs = new XMLStreamReaderForString(c, c.getFirstChars(), c._offSrc, c._cchSrc);
            } else {
                if (!$assertionsDisabled && !c.isContainer()) {
                    throw new AssertionError();
                }
                xs = new XMLStreamReaderForNode(c, true);
            }
        } else {
            xs = new XMLStreamReaderForNode(c, false);
        }
        if (c._locale.noSync()) {
            return new UnsyncedJsr173(c._locale, xs);
        }
        return new SyncedJsr173(c._locale, xs);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Jsr173$XMLStreamReaderForNode.class */
    private static final class XMLStreamReaderForNode extends XMLStreamReaderBase {
        private boolean _wholeDoc;
        private boolean _done;
        private Cur _cur;
        private Cur _end;
        private boolean _srcFetched;
        private Object _src;
        private int _offSrc;
        private int _cchSrc;
        private boolean _textFetched;
        private char[] _chars;
        private int _offChars;
        private int _cchChars;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Jsr173.class.desiredAssertionStatus();
        }

        public XMLStreamReaderForNode(Cur c, boolean inner) {
            super(c);
            if (!$assertionsDisabled && !c.isContainer() && !c.isComment() && !c.isProcinst() && !c.isAttr()) {
                throw new AssertionError();
            }
            if (inner) {
                if (!$assertionsDisabled && !c.isContainer()) {
                    throw new AssertionError();
                }
                this._cur = c.weakCur(this);
                if (!this._cur.toFirstAttr()) {
                    this._cur.next();
                }
                this._end = c.weakCur(this);
                this._end.toEnd();
            } else {
                this._cur = c.weakCur(this);
                if (c.isRoot()) {
                    this._wholeDoc = true;
                } else {
                    this._end = c.weakCur(this);
                    if (c.isAttr()) {
                        if (!this._end.toNextAttr()) {
                            this._end.toParent();
                            this._end.next();
                        }
                    } else {
                        this._end.skip();
                    }
                }
            }
            if (!this._wholeDoc) {
                this._cur.push();
                try {
                    next();
                    this._cur.pop();
                } catch (XMLStreamException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            if (!$assertionsDisabled && !this._wholeDoc && this._cur.isSamePos(this._end)) {
                throw new AssertionError();
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Jsr173.XMLStreamReaderBase
        protected Cur getStreamCur() {
            return this._cur;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasNext() throws XMLStreamException {
            checkChanged();
            return !this._done;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getEventType() {
            switch (this._cur.kind()) {
                case -2:
                    return 2;
                case -1:
                    return 8;
                case 0:
                    return 4;
                case 1:
                    return 7;
                case 2:
                    return 1;
                case 3:
                    return this._cur.isXmlns() ? 13 : 10;
                case 4:
                    return 5;
                case 5:
                    return 3;
                default:
                    throw new IllegalStateException();
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int next() throws XMLStreamException {
            checkChanged();
            if (!hasNext()) {
                throw new IllegalStateException("No next event in stream");
            }
            int kind = this._cur.kind();
            if (kind == -1) {
                if (!$assertionsDisabled && !this._wholeDoc) {
                    throw new AssertionError();
                }
                this._done = true;
            } else {
                if (kind == 3) {
                    if (!this._cur.toNextAttr()) {
                        this._cur.toParent();
                        this._cur.next();
                    }
                } else if (kind == 4 || kind == 5) {
                    this._cur.skip();
                } else if (kind != 1 || !this._cur.toFirstAttr()) {
                    this._cur.next();
                }
                if (!$assertionsDisabled && !this._wholeDoc && this._end == null) {
                    throw new AssertionError();
                }
                this._done = this._wholeDoc ? this._cur.kind() == -1 : this._cur.isSamePos(this._end);
            }
            this._textFetched = false;
            this._srcFetched = false;
            return getEventType();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getText() {
            checkChanged();
            int k = this._cur.kind();
            if (k == 4) {
                return this._cur.getValueAsString();
            }
            if (k == 0) {
                return this._cur.getCharsAsString(-1);
            }
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isStartElement() {
            return getEventType() == 1;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isEndElement() {
            return getEventType() == 2;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isCharacters() {
            return getEventType() == 4;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getElementText() throws XMLStreamException {
            checkChanged();
            if (!isStartElement()) {
                throw new IllegalStateException();
            }
            StringBuffer sb = new StringBuffer();
            while (hasNext()) {
                int e = next();
                if (e != 2) {
                    if (e == 1) {
                        throw new XMLStreamException();
                    }
                    if (e != 5 && e != 3) {
                        sb.append(getText());
                    }
                } else {
                    return sb.toString();
                }
            }
            throw new XMLStreamException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int nextTag() throws XMLStreamException {
            checkChanged();
            while (!isStartElement() && !isEndElement()) {
                if (!isWhiteSpace()) {
                    throw new XMLStreamException();
                }
                if (!hasNext()) {
                    throw new XMLStreamException();
                }
                next();
            }
            return getEventType();
        }

        private static boolean matchAttr(Cur c, String uri, String local) {
            if (!$assertionsDisabled && !c.isNormalAttr()) {
                throw new AssertionError();
            }
            QName name = c.getName();
            return name.getLocalPart().equals(local) && (uri == null || name.getNamespaceURI().equals(uri));
        }

        private static Cur toAttr(Cur c, String uri, String local) {
            if (uri == null || local == null || local.length() == 0) {
                throw new IllegalArgumentException();
            }
            Cur ca = c.tempCur();
            boolean match = false;
            if (c.isElem()) {
                if (ca.toFirstAttr()) {
                    while (true) {
                        if (ca.isNormalAttr() && matchAttr(ca, uri, local)) {
                            match = true;
                            break;
                        }
                        if (!ca.toNextSibling()) {
                            break;
                        }
                    }
                }
            } else if (c.isNormalAttr()) {
                match = matchAttr(c, uri, local);
            } else {
                throw new IllegalStateException();
            }
            if (!match) {
                ca.release();
                ca = null;
            }
            return ca;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeValue(String uri, String local) {
            Cur ca = toAttr(this._cur, uri, local);
            String value = null;
            if (ca != null) {
                value = ca.getValueAsString();
                ca.release();
            }
            return value;
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x0036 A[PHI: r5
  0x0036: PHI (r5v2 'i' int) = (r5v1 'i' int), (r5v3 'i' int) binds: [B:11:0x0027, B:13:0x002e] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private static org.apache.xmlbeans.impl.store.Cur toAttr(org.apache.xmlbeans.impl.store.Cur r4, int r5) {
            /*
                r0 = r5
                if (r0 >= 0) goto Le
                java.lang.IndexOutOfBoundsException r0 = new java.lang.IndexOutOfBoundsException
                r1 = r0
                java.lang.String r2 = "Attribute index is negative"
                r1.<init>(r2)
                throw r0
            Le:
                r0 = r4
                org.apache.xmlbeans.impl.store.Cur r0 = r0.tempCur()
                r6 = r0
                r0 = 0
                r7 = r0
                r0 = r4
                boolean r0 = r0.isElem()
                if (r0 == 0) goto L40
                r0 = r6
                boolean r0 = r0.toFirstAttr()
                if (r0 == 0) goto L5c
            L23:
                r0 = r6
                boolean r0 = r0.isNormalAttr()
                if (r0 == 0) goto L36
                r0 = r5
                int r5 = r5 + (-1)
                if (r0 != 0) goto L36
                r0 = 1
                r7 = r0
                goto L5c
            L36:
                r0 = r6
                boolean r0 = r0.toNextSibling()
                if (r0 != 0) goto L23
                goto L5c
            L40:
                r0 = r4
                boolean r0 = r0.isNormalAttr()
                if (r0 == 0) goto L54
                r0 = r5
                if (r0 != 0) goto L4f
                r0 = 1
                goto L50
            L4f:
                r0 = 0
            L50:
                r7 = r0
                goto L5c
            L54:
                java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                r1 = r0
                r1.<init>()
                throw r0
            L5c:
                r0 = r7
                if (r0 != 0) goto L6e
                r0 = r6
                r0.release()
                java.lang.IndexOutOfBoundsException r0 = new java.lang.IndexOutOfBoundsException
                r1 = r0
                java.lang.String r2 = "Attribute index is too large"
                r1.<init>(r2)
                throw r0
            L6e:
                r0 = r6
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Jsr173.XMLStreamReaderForNode.toAttr(org.apache.xmlbeans.impl.store.Cur, int):org.apache.xmlbeans.impl.store.Cur");
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getAttributeCount() {
            int n = 0;
            if (this._cur.isElem()) {
                Cur ca = this._cur.tempCur();
                if (ca.toFirstAttr()) {
                    do {
                        if (ca.isNormalAttr()) {
                            n++;
                        }
                    } while (ca.toNextSibling());
                }
                ca.release();
            } else if (this._cur.isNormalAttr()) {
                n = 0 + 1;
            } else {
                throw new IllegalStateException();
            }
            return n;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public QName getAttributeName(int index) {
            Cur ca = toAttr(this._cur, index);
            QName name = ca.getName();
            ca.release();
            return name;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeNamespace(int index) {
            return getAttributeName(index).getNamespaceURI();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeLocalName(int index) {
            return getAttributeName(index).getLocalPart();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributePrefix(int index) {
            return getAttributeName(index).getPrefix();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeType(int index) {
            toAttr(this._cur, index).release();
            return "CDATA";
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeValue(int index) {
            Cur ca = toAttr(this._cur, index);
            String value = null;
            if (ca != null) {
                value = ca.getValueAsString();
                ca.release();
            }
            return value;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isAttributeSpecified(int index) {
            Cur ca = toAttr(this._cur, index);
            ca.release();
            return false;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getNamespaceCount() {
            int n = 0;
            if (this._cur.isElem() || this._cur.kind() == -2) {
                Cur ca = this._cur.tempCur();
                if (this._cur.kind() == -2) {
                    ca.toParent();
                }
                if (ca.toFirstAttr()) {
                    do {
                        if (ca.isXmlns()) {
                            n++;
                        }
                    } while (ca.toNextSibling());
                }
                ca.release();
            } else if (this._cur.isXmlns()) {
                n = 0 + 1;
            } else {
                throw new IllegalStateException();
            }
            return n;
        }

        /* JADX WARN: Removed duplicated region for block: B:20:0x004d A[PHI: r5
  0x004d: PHI (r5v2 'i' int) = (r5v1 'i' int), (r5v3 'i' int) binds: [B:16:0x003e, B:18:0x0045] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private static org.apache.xmlbeans.impl.store.Cur toXmlns(org.apache.xmlbeans.impl.store.Cur r4, int r5) {
            /*
                r0 = r5
                if (r0 >= 0) goto Le
                java.lang.IndexOutOfBoundsException r0 = new java.lang.IndexOutOfBoundsException
                r1 = r0
                java.lang.String r2 = "Namespace index is negative"
                r1.<init>(r2)
                throw r0
            Le:
                r0 = r4
                org.apache.xmlbeans.impl.store.Cur r0 = r0.tempCur()
                r6 = r0
                r0 = 0
                r7 = r0
                r0 = r4
                boolean r0 = r0.isElem()
                if (r0 != 0) goto L25
                r0 = r4
                int r0 = r0.kind()
                r1 = -2
                if (r0 != r1) goto L57
            L25:
                r0 = r4
                int r0 = r0.kind()
                r1 = -2
                if (r0 != r1) goto L33
                r0 = r6
                boolean r0 = r0.toParent()
            L33:
                r0 = r6
                boolean r0 = r0.toFirstAttr()
                if (r0 == 0) goto L73
            L3a:
                r0 = r6
                boolean r0 = r0.isXmlns()
                if (r0 == 0) goto L4d
                r0 = r5
                int r5 = r5 + (-1)
                if (r0 != 0) goto L4d
                r0 = 1
                r7 = r0
                goto L73
            L4d:
                r0 = r6
                boolean r0 = r0.toNextSibling()
                if (r0 != 0) goto L3a
                goto L73
            L57:
                r0 = r4
                boolean r0 = r0.isXmlns()
                if (r0 == 0) goto L6b
                r0 = r5
                if (r0 != 0) goto L66
                r0 = 1
                goto L67
            L66:
                r0 = 0
            L67:
                r7 = r0
                goto L73
            L6b:
                java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                r1 = r0
                r1.<init>()
                throw r0
            L73:
                r0 = r7
                if (r0 != 0) goto L85
                r0 = r6
                r0.release()
                java.lang.IndexOutOfBoundsException r0 = new java.lang.IndexOutOfBoundsException
                r1 = r0
                java.lang.String r2 = "Namespace index is too large"
                r1.<init>(r2)
                throw r0
            L85:
                r0 = r6
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Jsr173.XMLStreamReaderForNode.toXmlns(org.apache.xmlbeans.impl.store.Cur, int):org.apache.xmlbeans.impl.store.Cur");
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespacePrefix(int index) {
            Cur ca = toXmlns(this._cur, index);
            String prefix = ca.getXmlnsPrefix();
            ca.release();
            return prefix;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI(int index) {
            Cur ca = toXmlns(this._cur, index);
            String uri = ca.getXmlnsUri();
            ca.release();
            return uri;
        }

        private void fetchChars() {
            Cur cText;
            if (!this._textFetched) {
                int k = this._cur.kind();
                if (k == 4) {
                    cText = this._cur.tempCur();
                    cText.next();
                } else if (k == 0) {
                    cText = this._cur;
                } else {
                    throw new IllegalStateException();
                }
                Object src = cText.getChars(-1);
                ensureCharBufLen(cText._cchSrc);
                char[] cArr = this._chars;
                this._offChars = 0;
                int i = cText._offSrc;
                int i2 = cText._cchSrc;
                this._cchChars = i2;
                CharUtil.getChars(cArr, 0, src, i, i2);
                if (cText != this._cur) {
                    cText.release();
                }
                this._textFetched = true;
            }
        }

        private void ensureCharBufLen(int cch) {
            if (this._chars == null || this._chars.length < cch) {
                int i = 256;
                while (true) {
                    int l = i;
                    if (l < cch) {
                        i = l * 2;
                    } else {
                        this._chars = new char[l];
                        return;
                    }
                }
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public char[] getTextCharacters() {
            checkChanged();
            fetchChars();
            return this._chars;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextStart() {
            checkChanged();
            fetchChars();
            return this._offChars;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextLength() {
            checkChanged();
            fetchChars();
            return this._cchChars;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
            Cur cText;
            if (length < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (targetStart < 0 || targetStart >= target.length) {
                throw new IndexOutOfBoundsException();
            }
            if (targetStart + length > target.length) {
                throw new IndexOutOfBoundsException();
            }
            if (!this._srcFetched) {
                int k = this._cur.kind();
                if (k == 4) {
                    cText = this._cur.tempCur();
                    cText.next();
                } else if (k == 0) {
                    cText = this._cur;
                } else {
                    throw new IllegalStateException();
                }
                this._src = cText.getChars(-1);
                this._offSrc = cText._offSrc;
                this._cchSrc = cText._cchSrc;
                if (cText != this._cur) {
                    cText.release();
                }
                this._srcFetched = true;
            }
            if (sourceStart > this._cchSrc) {
                throw new IndexOutOfBoundsException();
            }
            if (sourceStart + length > this._cchSrc) {
                length = this._cchSrc - sourceStart;
            }
            CharUtil.getChars(target, targetStart, this._src, this._offSrc, length);
            return length;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasText() {
            int k = this._cur.kind();
            return k == 4 || k == 0;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasName() {
            int k = this._cur.kind();
            return k == 2 || k == -2;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public QName getName() {
            if (!hasName()) {
                throw new IllegalStateException();
            }
            return this._cur.getName();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI() {
            return getName().getNamespaceURI();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getLocalName() {
            return getName().getLocalPart();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPrefix() {
            return getName().getPrefix();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPITarget() {
            if (this._cur.kind() == 5) {
                return this._cur.getName().getLocalPart();
            }
            return null;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPIData() {
            if (this._cur.kind() == 5) {
                return this._cur.getValueAsString();
            }
            return null;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Jsr173$XMLStreamReaderBase.class */
    private static abstract class XMLStreamReaderBase implements XMLStreamReader, NamespaceContext, Location {
        private Locale _locale;
        private long _version;
        String _uri;
        int _line = -1;
        int _column = -1;
        int _offset = -1;

        protected abstract Cur getStreamCur();

        XMLStreamReaderBase(Cur c) {
            this._locale = c._locale;
            this._version = this._locale.version();
        }

        protected final void checkChanged() {
            if (this._version != this._locale.version()) {
                throw new ConcurrentModificationException("Document changed while streaming");
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public void close() throws XMLStreamException {
            checkChanged();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isWhiteSpace() {
            checkChanged();
            String s = getText();
            return this._locale.getCharUtil().isWhiteSpace(s, 0, s.length());
        }

        @Override // javax.xml.stream.XMLStreamReader
        public Location getLocation() {
            checkChanged();
            Cur c = getStreamCur();
            XmlLineNumber ln = (XmlLineNumber) c.getBookmark(XmlLineNumber.class);
            this._uri = null;
            if (ln != null) {
                this._line = ln.getLine();
                this._column = ln.getColumn();
                this._offset = ln.getOffset();
            } else {
                this._line = -1;
                this._column = -1;
                this._offset = -1;
            }
            return this;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public Object getProperty(String name) {
            checkChanged();
            if (name == null) {
                throw new IllegalArgumentException("Property name is null");
            }
            return null;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getCharacterEncodingScheme() {
            checkChanged();
            Locale locale = this._locale;
            XmlDocumentProperties props = Locale.getDocProps(getStreamCur(), false);
            if (props == null) {
                return null;
            }
            return props.getEncoding();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getEncoding() {
            return null;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getVersion() {
            checkChanged();
            Locale locale = this._locale;
            XmlDocumentProperties props = Locale.getDocProps(getStreamCur(), false);
            if (props == null) {
                return null;
            }
            return props.getVersion();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isStandalone() {
            checkChanged();
            Locale locale = this._locale;
            XmlDocumentProperties props = Locale.getDocProps(getStreamCur(), false);
            if (props == null) {
                return false;
            }
            return props.getStandalone();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean standaloneSet() {
            checkChanged();
            return false;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
            checkChanged();
            if (type != getEventType()) {
                throw new XMLStreamException();
            }
            if (namespaceURI != null && !getNamespaceURI().equals(namespaceURI)) {
                throw new XMLStreamException();
            }
            if (localName != null && !getLocalName().equals(localName)) {
                throw new XMLStreamException();
            }
        }

        @Override // javax.xml.stream.Location
        public int getCharacterOffset() {
            return this._offset;
        }

        @Override // javax.xml.stream.Location
        public int getColumnNumber() {
            return this._column;
        }

        @Override // javax.xml.stream.Location
        public int getLineNumber() {
            return this._line;
        }

        public String getLocationURI() {
            return this._uri;
        }

        @Override // javax.xml.stream.Location
        public String getPublicId() {
            return null;
        }

        @Override // javax.xml.stream.Location
        public String getSystemId() {
            return null;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public NamespaceContext getNamespaceContext() {
            throw new RuntimeException("This version of getNamespaceContext should not be called");
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI(String prefix) {
            checkChanged();
            Cur c = getStreamCur();
            c.push();
            if (!c.isContainer()) {
                c.toParent();
            }
            String ns = c.namespaceForPrefix(prefix, true);
            c.pop();
            return ns;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getPrefix(String namespaceURI) {
            checkChanged();
            Cur c = getStreamCur();
            c.push();
            if (!c.isContainer()) {
                c.toParent();
            }
            String prefix = c.prefixForNamespace(namespaceURI, null, false);
            c.pop();
            return prefix;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public Iterator getPrefixes(String namespaceURI) {
            checkChanged();
            HashMap map = new HashMap();
            map.put(namespaceURI, getPrefix(namespaceURI));
            return map.values().iterator();
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Jsr173$XMLStreamReaderForString.class */
    private static final class XMLStreamReaderForString extends XMLStreamReaderBase {
        private Cur _cur;
        private Object _src;
        private int _off;
        private int _cch;

        XMLStreamReaderForString(Cur c, Object src, int off, int cch) {
            super(c);
            this._src = src;
            this._off = off;
            this._cch = cch;
            this._cur = c;
        }

        @Override // org.apache.xmlbeans.impl.store.Jsr173.XMLStreamReaderBase
        protected Cur getStreamCur() {
            return this._cur;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getText() {
            checkChanged();
            return CharUtil.getString(this._src, this._off, this._cch);
        }

        @Override // javax.xml.stream.XMLStreamReader
        public char[] getTextCharacters() {
            checkChanged();
            char[] chars = new char[this._cch];
            CharUtil.getChars(chars, 0, this._src, this._off, this._cch);
            return chars;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextStart() {
            checkChanged();
            return this._off;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextLength() {
            checkChanged();
            return this._cch;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) {
            checkChanged();
            if (length < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (sourceStart > this._cch) {
                throw new IndexOutOfBoundsException();
            }
            if (sourceStart + length > this._cch) {
                length = this._cch - sourceStart;
            }
            CharUtil.getChars(target, targetStart, this._src, this._off + sourceStart, length);
            return length;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getEventType() {
            checkChanged();
            return 4;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasName() {
            checkChanged();
            return false;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasNext() {
            checkChanged();
            return false;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasText() {
            checkChanged();
            return true;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isCharacters() {
            checkChanged();
            return true;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isEndElement() {
            checkChanged();
            return false;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isStartElement() {
            checkChanged();
            return false;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getAttributeCount() {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeLocalName(int index) {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public QName getAttributeName(int index) {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeNamespace(int index) {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributePrefix(int index) {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeType(int index) {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeValue(int index) {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeValue(String namespaceURI, String localName) {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getElementText() {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getLocalName() {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public QName getName() {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getNamespaceCount() {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespacePrefix(int index) {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI(int index) {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI() {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPIData() {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPITarget() {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPrefix() {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isAttributeSpecified(int index) {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int next() {
            throw new IllegalStateException();
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int nextTag() {
            throw new IllegalStateException();
        }

        @Override // org.apache.xmlbeans.impl.store.Jsr173.XMLStreamReaderBase, javax.xml.stream.Location
        public String getPublicId() {
            throw new IllegalStateException();
        }

        @Override // org.apache.xmlbeans.impl.store.Jsr173.XMLStreamReaderBase, javax.xml.stream.Location
        public String getSystemId() {
            throw new IllegalStateException();
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Jsr173$Jsr173GateWay.class */
    private static abstract class Jsr173GateWay {
        Locale _l;
        XMLStreamReaderBase _xs;

        public Jsr173GateWay(Locale l, XMLStreamReaderBase xs) {
            this._l = l;
            this._xs = xs;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Jsr173$SyncedJsr173.class */
    private static final class SyncedJsr173 extends Jsr173GateWay implements XMLStreamReader, Location, NamespaceContext {
        public SyncedJsr173(Locale l, XMLStreamReaderBase xs) {
            super(l, xs);
        }

        @Override // javax.xml.stream.XMLStreamReader
        public Object getProperty(String name) {
            Object property;
            synchronized (this._l) {
                this._l.enter();
                try {
                    property = this._xs.getProperty(name);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return property;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int next() throws XMLStreamException {
            int next;
            synchronized (this._l) {
                this._l.enter();
                try {
                    next = this._xs.next();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return next;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
            synchronized (this._l) {
                this._l.enter();
                try {
                    this._xs.require(type, namespaceURI, localName);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getElementText() throws XMLStreamException {
            String elementText;
            synchronized (this._l) {
                this._l.enter();
                try {
                    elementText = this._xs.getElementText();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return elementText;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int nextTag() throws XMLStreamException {
            int iNextTag;
            synchronized (this._l) {
                this._l.enter();
                try {
                    iNextTag = this._xs.nextTag();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return iNextTag;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasNext() throws XMLStreamException {
            boolean zHasNext;
            synchronized (this._l) {
                this._l.enter();
                try {
                    zHasNext = this._xs.hasNext();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return zHasNext;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public void close() throws XMLStreamException {
            synchronized (this._l) {
                this._l.enter();
                try {
                    this._xs.close();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI(String prefix) {
            String namespaceURI;
            synchronized (this._l) {
                this._l.enter();
                try {
                    namespaceURI = this._xs.getNamespaceURI(prefix);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return namespaceURI;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isStartElement() {
            boolean zIsStartElement;
            synchronized (this._l) {
                this._l.enter();
                try {
                    zIsStartElement = this._xs.isStartElement();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return zIsStartElement;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isEndElement() {
            boolean zIsEndElement;
            synchronized (this._l) {
                this._l.enter();
                try {
                    zIsEndElement = this._xs.isEndElement();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return zIsEndElement;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isCharacters() {
            boolean zIsCharacters;
            synchronized (this._l) {
                this._l.enter();
                try {
                    zIsCharacters = this._xs.isCharacters();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return zIsCharacters;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isWhiteSpace() {
            boolean zIsWhiteSpace;
            synchronized (this._l) {
                this._l.enter();
                try {
                    zIsWhiteSpace = this._xs.isWhiteSpace();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return zIsWhiteSpace;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeValue(String namespaceURI, String localName) {
            String attributeValue;
            synchronized (this._l) {
                this._l.enter();
                try {
                    attributeValue = this._xs.getAttributeValue(namespaceURI, localName);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return attributeValue;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getAttributeCount() {
            int attributeCount;
            synchronized (this._l) {
                this._l.enter();
                try {
                    attributeCount = this._xs.getAttributeCount();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return attributeCount;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public QName getAttributeName(int index) {
            QName attributeName;
            synchronized (this._l) {
                this._l.enter();
                try {
                    attributeName = this._xs.getAttributeName(index);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return attributeName;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeNamespace(int index) {
            String attributeNamespace;
            synchronized (this._l) {
                this._l.enter();
                try {
                    attributeNamespace = this._xs.getAttributeNamespace(index);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return attributeNamespace;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeLocalName(int index) {
            String attributeLocalName;
            synchronized (this._l) {
                this._l.enter();
                try {
                    attributeLocalName = this._xs.getAttributeLocalName(index);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return attributeLocalName;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributePrefix(int index) {
            String attributePrefix;
            synchronized (this._l) {
                this._l.enter();
                try {
                    attributePrefix = this._xs.getAttributePrefix(index);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return attributePrefix;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeType(int index) {
            String attributeType;
            synchronized (this._l) {
                this._l.enter();
                try {
                    attributeType = this._xs.getAttributeType(index);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return attributeType;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeValue(int index) {
            String attributeValue;
            synchronized (this._l) {
                this._l.enter();
                try {
                    attributeValue = this._xs.getAttributeValue(index);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return attributeValue;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isAttributeSpecified(int index) {
            boolean zIsAttributeSpecified;
            synchronized (this._l) {
                this._l.enter();
                try {
                    zIsAttributeSpecified = this._xs.isAttributeSpecified(index);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return zIsAttributeSpecified;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getNamespaceCount() {
            int namespaceCount;
            synchronized (this._l) {
                this._l.enter();
                try {
                    namespaceCount = this._xs.getNamespaceCount();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return namespaceCount;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespacePrefix(int index) {
            String namespacePrefix;
            synchronized (this._l) {
                this._l.enter();
                try {
                    namespacePrefix = this._xs.getNamespacePrefix(index);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return namespacePrefix;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI(int index) {
            String namespaceURI;
            synchronized (this._l) {
                this._l.enter();
                try {
                    namespaceURI = this._xs.getNamespaceURI(index);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return namespaceURI;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public NamespaceContext getNamespaceContext() {
            return this;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getEventType() {
            int eventType;
            synchronized (this._l) {
                this._l.enter();
                try {
                    eventType = this._xs.getEventType();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return eventType;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getText() {
            String text;
            synchronized (this._l) {
                this._l.enter();
                try {
                    text = this._xs.getText();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return text;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public char[] getTextCharacters() {
            char[] textCharacters;
            synchronized (this._l) {
                this._l.enter();
                try {
                    textCharacters = this._xs.getTextCharacters();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return textCharacters;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
            int textCharacters;
            synchronized (this._l) {
                this._l.enter();
                try {
                    textCharacters = this._xs.getTextCharacters(sourceStart, target, targetStart, length);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return textCharacters;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextStart() {
            int textStart;
            synchronized (this._l) {
                this._l.enter();
                try {
                    textStart = this._xs.getTextStart();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return textStart;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextLength() {
            int textLength;
            synchronized (this._l) {
                this._l.enter();
                try {
                    textLength = this._xs.getTextLength();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return textLength;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getEncoding() {
            String encoding;
            synchronized (this._l) {
                this._l.enter();
                try {
                    encoding = this._xs.getEncoding();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return encoding;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasText() {
            boolean zHasText;
            synchronized (this._l) {
                this._l.enter();
                try {
                    zHasText = this._xs.hasText();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return zHasText;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public Location getLocation() {
            Location location;
            synchronized (this._l) {
                this._l.enter();
                try {
                    location = this._xs.getLocation();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return location;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public QName getName() {
            QName name;
            synchronized (this._l) {
                this._l.enter();
                try {
                    name = this._xs.getName();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return name;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getLocalName() {
            String localName;
            synchronized (this._l) {
                this._l.enter();
                try {
                    localName = this._xs.getLocalName();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return localName;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasName() {
            boolean zHasName;
            synchronized (this._l) {
                this._l.enter();
                try {
                    zHasName = this._xs.hasName();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return zHasName;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI() {
            String namespaceURI;
            synchronized (this._l) {
                this._l.enter();
                try {
                    namespaceURI = this._xs.getNamespaceURI();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return namespaceURI;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPrefix() {
            String prefix;
            synchronized (this._l) {
                this._l.enter();
                try {
                    prefix = this._xs.getPrefix();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return prefix;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getVersion() {
            String version;
            synchronized (this._l) {
                this._l.enter();
                try {
                    version = this._xs.getVersion();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return version;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isStandalone() {
            boolean zIsStandalone;
            synchronized (this._l) {
                this._l.enter();
                try {
                    zIsStandalone = this._xs.isStandalone();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return zIsStandalone;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean standaloneSet() {
            boolean zStandaloneSet;
            synchronized (this._l) {
                this._l.enter();
                try {
                    zStandaloneSet = this._xs.standaloneSet();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return zStandaloneSet;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getCharacterEncodingScheme() {
            String characterEncodingScheme;
            synchronized (this._l) {
                this._l.enter();
                try {
                    characterEncodingScheme = this._xs.getCharacterEncodingScheme();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return characterEncodingScheme;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPITarget() {
            String pITarget;
            synchronized (this._l) {
                this._l.enter();
                try {
                    pITarget = this._xs.getPITarget();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return pITarget;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPIData() {
            String pIData;
            synchronized (this._l) {
                this._l.enter();
                try {
                    pIData = this._xs.getPIData();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return pIData;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getPrefix(String namespaceURI) {
            String prefix;
            synchronized (this._l) {
                this._l.enter();
                try {
                    prefix = this._xs.getPrefix(namespaceURI);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return prefix;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public Iterator getPrefixes(String namespaceURI) {
            Iterator prefixes;
            synchronized (this._l) {
                this._l.enter();
                try {
                    prefixes = this._xs.getPrefixes(namespaceURI);
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return prefixes;
        }

        @Override // javax.xml.stream.Location
        public int getCharacterOffset() {
            int characterOffset;
            synchronized (this._l) {
                this._l.enter();
                try {
                    characterOffset = this._xs.getCharacterOffset();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return characterOffset;
        }

        @Override // javax.xml.stream.Location
        public int getColumnNumber() {
            int columnNumber;
            synchronized (this._l) {
                this._l.enter();
                try {
                    columnNumber = this._xs.getColumnNumber();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return columnNumber;
        }

        @Override // javax.xml.stream.Location
        public int getLineNumber() {
            int lineNumber;
            synchronized (this._l) {
                this._l.enter();
                try {
                    lineNumber = this._xs.getLineNumber();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return lineNumber;
        }

        public String getLocationURI() {
            String locationURI;
            synchronized (this._l) {
                this._l.enter();
                try {
                    locationURI = this._xs.getLocationURI();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return locationURI;
        }

        @Override // javax.xml.stream.Location
        public String getPublicId() {
            String publicId;
            synchronized (this._l) {
                this._l.enter();
                try {
                    publicId = this._xs.getPublicId();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return publicId;
        }

        @Override // javax.xml.stream.Location
        public String getSystemId() {
            String systemId;
            synchronized (this._l) {
                this._l.enter();
                try {
                    systemId = this._xs.getSystemId();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return systemId;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Jsr173$UnsyncedJsr173.class */
    private static final class UnsyncedJsr173 extends Jsr173GateWay implements XMLStreamReader, Location, NamespaceContext {
        public UnsyncedJsr173(Locale l, XMLStreamReaderBase xs) {
            super(l, xs);
        }

        @Override // javax.xml.stream.XMLStreamReader
        public Object getProperty(String name) {
            try {
                this._l.enter();
                Object property = this._xs.getProperty(name);
                this._l.exit();
                return property;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int next() throws XMLStreamException {
            try {
                this._l.enter();
                int next = this._xs.next();
                this._l.exit();
                return next;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
            try {
                this._l.enter();
                this._xs.require(type, namespaceURI, localName);
                this._l.exit();
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getElementText() throws XMLStreamException {
            try {
                this._l.enter();
                String elementText = this._xs.getElementText();
                this._l.exit();
                return elementText;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int nextTag() throws XMLStreamException {
            try {
                this._l.enter();
                int iNextTag = this._xs.nextTag();
                this._l.exit();
                return iNextTag;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasNext() throws XMLStreamException {
            try {
                this._l.enter();
                boolean zHasNext = this._xs.hasNext();
                this._l.exit();
                return zHasNext;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public void close() throws XMLStreamException {
            try {
                this._l.enter();
                this._xs.close();
                this._l.exit();
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI(String prefix) {
            try {
                this._l.enter();
                String namespaceURI = this._xs.getNamespaceURI(prefix);
                this._l.exit();
                return namespaceURI;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isStartElement() {
            try {
                this._l.enter();
                boolean zIsStartElement = this._xs.isStartElement();
                this._l.exit();
                return zIsStartElement;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isEndElement() {
            try {
                this._l.enter();
                boolean zIsEndElement = this._xs.isEndElement();
                this._l.exit();
                return zIsEndElement;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isCharacters() {
            try {
                this._l.enter();
                boolean zIsCharacters = this._xs.isCharacters();
                this._l.exit();
                return zIsCharacters;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isWhiteSpace() {
            try {
                this._l.enter();
                boolean zIsWhiteSpace = this._xs.isWhiteSpace();
                this._l.exit();
                return zIsWhiteSpace;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeValue(String namespaceURI, String localName) {
            try {
                this._l.enter();
                String attributeValue = this._xs.getAttributeValue(namespaceURI, localName);
                this._l.exit();
                return attributeValue;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getAttributeCount() {
            try {
                this._l.enter();
                int attributeCount = this._xs.getAttributeCount();
                this._l.exit();
                return attributeCount;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public QName getAttributeName(int index) {
            try {
                this._l.enter();
                QName attributeName = this._xs.getAttributeName(index);
                this._l.exit();
                return attributeName;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeNamespace(int index) {
            try {
                this._l.enter();
                String attributeNamespace = this._xs.getAttributeNamespace(index);
                this._l.exit();
                return attributeNamespace;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeLocalName(int index) {
            try {
                this._l.enter();
                String attributeLocalName = this._xs.getAttributeLocalName(index);
                this._l.exit();
                return attributeLocalName;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributePrefix(int index) {
            try {
                this._l.enter();
                String attributePrefix = this._xs.getAttributePrefix(index);
                this._l.exit();
                return attributePrefix;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeType(int index) {
            try {
                this._l.enter();
                String attributeType = this._xs.getAttributeType(index);
                this._l.exit();
                return attributeType;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getAttributeValue(int index) {
            try {
                this._l.enter();
                String attributeValue = this._xs.getAttributeValue(index);
                this._l.exit();
                return attributeValue;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isAttributeSpecified(int index) {
            try {
                this._l.enter();
                boolean zIsAttributeSpecified = this._xs.isAttributeSpecified(index);
                this._l.exit();
                return zIsAttributeSpecified;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getNamespaceCount() {
            try {
                this._l.enter();
                int namespaceCount = this._xs.getNamespaceCount();
                this._l.exit();
                return namespaceCount;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespacePrefix(int index) {
            try {
                this._l.enter();
                String namespacePrefix = this._xs.getNamespacePrefix(index);
                this._l.exit();
                return namespacePrefix;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI(int index) {
            try {
                this._l.enter();
                String namespaceURI = this._xs.getNamespaceURI(index);
                this._l.exit();
                return namespaceURI;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public NamespaceContext getNamespaceContext() {
            return this;
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getEventType() {
            try {
                this._l.enter();
                int eventType = this._xs.getEventType();
                this._l.exit();
                return eventType;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getText() {
            try {
                this._l.enter();
                String text = this._xs.getText();
                this._l.exit();
                return text;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public char[] getTextCharacters() {
            try {
                this._l.enter();
                char[] textCharacters = this._xs.getTextCharacters();
                this._l.exit();
                return textCharacters;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
            try {
                this._l.enter();
                int textCharacters = this._xs.getTextCharacters(sourceStart, target, targetStart, length);
                this._l.exit();
                return textCharacters;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextStart() {
            try {
                this._l.enter();
                int textStart = this._xs.getTextStart();
                this._l.exit();
                return textStart;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public int getTextLength() {
            try {
                this._l.enter();
                int textLength = this._xs.getTextLength();
                this._l.exit();
                return textLength;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getEncoding() {
            try {
                this._l.enter();
                String encoding = this._xs.getEncoding();
                this._l.exit();
                return encoding;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasText() {
            try {
                this._l.enter();
                boolean zHasText = this._xs.hasText();
                this._l.exit();
                return zHasText;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public Location getLocation() {
            try {
                this._l.enter();
                Location location = this._xs.getLocation();
                this._l.exit();
                return location;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public QName getName() {
            try {
                this._l.enter();
                QName name = this._xs.getName();
                this._l.exit();
                return name;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getLocalName() {
            try {
                this._l.enter();
                String localName = this._xs.getLocalName();
                this._l.exit();
                return localName;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean hasName() {
            try {
                this._l.enter();
                boolean zHasName = this._xs.hasName();
                this._l.exit();
                return zHasName;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getNamespaceURI() {
            try {
                this._l.enter();
                String namespaceURI = this._xs.getNamespaceURI();
                this._l.exit();
                return namespaceURI;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPrefix() {
            try {
                this._l.enter();
                String prefix = this._xs.getPrefix();
                this._l.exit();
                return prefix;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getVersion() {
            try {
                this._l.enter();
                String version = this._xs.getVersion();
                this._l.exit();
                return version;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean isStandalone() {
            try {
                this._l.enter();
                boolean zIsStandalone = this._xs.isStandalone();
                this._l.exit();
                return zIsStandalone;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public boolean standaloneSet() {
            try {
                this._l.enter();
                boolean zStandaloneSet = this._xs.standaloneSet();
                this._l.exit();
                return zStandaloneSet;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getCharacterEncodingScheme() {
            try {
                this._l.enter();
                String characterEncodingScheme = this._xs.getCharacterEncodingScheme();
                this._l.exit();
                return characterEncodingScheme;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPITarget() {
            try {
                this._l.enter();
                String pITarget = this._xs.getPITarget();
                this._l.exit();
                return pITarget;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.XMLStreamReader
        public String getPIData() {
            try {
                this._l.enter();
                String pIData = this._xs.getPIData();
                this._l.exit();
                return pIData;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getPrefix(String namespaceURI) {
            try {
                this._l.enter();
                String prefix = this._xs.getPrefix(namespaceURI);
                this._l.exit();
                return prefix;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.namespace.NamespaceContext
        public Iterator getPrefixes(String namespaceURI) {
            try {
                this._l.enter();
                Iterator prefixes = this._xs.getPrefixes(namespaceURI);
                this._l.exit();
                return prefixes;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.Location
        public int getCharacterOffset() {
            try {
                this._l.enter();
                int characterOffset = this._xs.getCharacterOffset();
                this._l.exit();
                return characterOffset;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.Location
        public int getColumnNumber() {
            try {
                this._l.enter();
                int columnNumber = this._xs.getColumnNumber();
                this._l.exit();
                return columnNumber;
            } catch (Throwable th) {
                this._l.exit();
                throw th;
            }
        }

        @Override // javax.xml.stream.Location
        public int getLineNumber() {
            int lineNumber;
            synchronized (this._l) {
                this._l.enter();
                try {
                    lineNumber = this._xs.getLineNumber();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return lineNumber;
        }

        public String getLocationURI() {
            String locationURI;
            synchronized (this._l) {
                this._l.enter();
                try {
                    locationURI = this._xs.getLocationURI();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return locationURI;
        }

        @Override // javax.xml.stream.Location
        public String getPublicId() {
            String publicId;
            synchronized (this._l) {
                this._l.enter();
                try {
                    publicId = this._xs.getPublicId();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return publicId;
        }

        @Override // javax.xml.stream.Location
        public String getSystemId() {
            String systemId;
            synchronized (this._l) {
                this._l.enter();
                try {
                    systemId = this._xs.getSystemId();
                    this._l.exit();
                } catch (Throwable th) {
                    this._l.exit();
                    throw th;
                }
            }
            return systemId;
        }
    }
}
