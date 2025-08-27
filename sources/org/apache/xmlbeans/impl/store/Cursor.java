package org.apache.xmlbeans.impl.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.GlobalLock;
import org.apache.xmlbeans.impl.common.XMLChar;
import org.apache.xmlbeans.impl.store.Locale;
import org.apache.xmlbeans.impl.store.Path;
import org.apache.xmlbeans.impl.store.Saver;
import org.apache.xmlbeans.impl.store.Xobj;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Cursor.class */
public final class Cursor implements XmlCursor, Locale.ChangeListener {
    static final int ROOT = 1;
    static final int ELEM = 2;
    static final int ATTR = 3;
    static final int COMMENT = 4;
    static final int PROCINST = 5;
    static final int TEXT = 0;
    private static final int MOVE_XML = 0;
    private static final int COPY_XML = 1;
    private static final int MOVE_XML_CONTENTS = 2;
    private static final int COPY_XML_CONTENTS = 3;
    private static final int MOVE_CHARS = 4;
    private static final int COPY_CHARS = 5;
    private Cur _cur;
    private Path.PathEngine _pathEngine;
    private int _currentSelection;
    private Locale.ChangeListener _nextChangeListener;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Cursor.class.desiredAssertionStatus();
    }

    Cursor(Xobj x, int p) {
        this._cur = x._locale.weakCur(this);
        this._cur.moveTo(x, p);
        this._currentSelection = -1;
    }

    Cursor(Cur c) {
        this(c._xobj, c._pos);
    }

    private static boolean isValid(Cur c) {
        int pk;
        if (c.kind() <= 0) {
            c.push();
            if (c.toParentRaw() && ((pk = c.kind()) == 4 || pk == 5 || pk == 3)) {
                return false;
            }
            c.pop();
            return true;
        }
        return true;
    }

    private boolean isValid() {
        return isValid(this._cur);
    }

    Locale locale() {
        return this._cur._locale;
    }

    Cur tempCur() {
        return this._cur.tempCur();
    }

    public void dump(PrintStream o) {
        this._cur.dump(o);
    }

    static void validateLocalName(QName name) {
        if (name == null) {
            throw new IllegalArgumentException("QName is null");
        }
        validateLocalName(name.getLocalPart());
    }

    static void validateLocalName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("Name is empty");
        }
        if (!XMLChar.isValidNCName(name)) {
            throw new IllegalArgumentException("Name is not valid");
        }
    }

    static void validatePrefix(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Prefix is null");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("Prefix is empty");
        }
        if (Locale.beginsWithXml(name)) {
            throw new IllegalArgumentException("Prefix begins with 'xml'");
        }
        if (!XMLChar.isValidNCName(name)) {
            throw new IllegalArgumentException("Prefix is not valid");
        }
    }

    private static void complain(String msg) {
        throw new IllegalArgumentException(msg);
    }

    private void checkInsertionValidity(Cur that) {
        int thatKind = that.kind();
        if (thatKind < 0) {
            complain("Can't move/copy/insert an end token.");
        }
        if (thatKind == 1) {
            complain("Can't move/copy/insert a whole document.");
        }
        int thisKind = this._cur.kind();
        if (thisKind == 1) {
            complain("Can't insert before the start of the document.");
        }
        if (thatKind == 3) {
            this._cur.push();
            this._cur.prevWithAttrs();
            int pk = this._cur.kind();
            this._cur.pop();
            if (pk != 2 && pk != 1 && pk != -3) {
                complain("Can only insert attributes before other attributes or after containers.");
            }
        }
        if (thisKind == 3 && thatKind != 3) {
            complain("Can only insert attributes before other attributes or after containers.");
        }
    }

    private void insertNode(Cur that, String text) {
        if (!$assertionsDisabled && that.isRoot()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !that.isNode()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !isValid(that)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !isValid()) {
            throw new AssertionError();
        }
        if (text != null && text.length() > 0) {
            that.next();
            that.insertString(text);
            that.toParent();
        }
        checkInsertionValidity(that);
        that.moveNode(this._cur);
        this._cur.toEnd();
        this._cur.nextWithAttrs();
    }

    public void _dispose() {
        this._cur.release();
        this._cur = null;
    }

    public XmlCursor _newCursor() {
        return new Cursor(this._cur);
    }

    public QName _getName() {
        switch (this._cur.kind()) {
            case 2:
            case 5:
                break;
            case 3:
                if (this._cur.isXmlns()) {
                    return this._cur._locale.makeQNameNoCheck(this._cur.getXmlnsUri(), this._cur.getXmlnsPrefix());
                }
                break;
            case 4:
            default:
                return null;
        }
        return this._cur.getName();
    }

    public void _setName(QName name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
        switch (this._cur.kind()) {
            case 2:
            case 3:
                validateLocalName(name.getLocalPart());
                break;
            case 4:
            default:
                throw new IllegalStateException("Can set name on element, atrtribute and procinst only");
            case 5:
                validatePrefix(name.getLocalPart());
                if (name.getNamespaceURI().length() > 0) {
                    throw new IllegalArgumentException("Procinst name must have no URI");
                }
                if (name.getPrefix().length() > 0) {
                    throw new IllegalArgumentException("Procinst name must have no prefix");
                }
                break;
        }
        this._cur.setName(name);
    }

    public XmlCursor.TokenType _currentTokenType() {
        if (!$assertionsDisabled && !isValid()) {
            throw new AssertionError();
        }
        switch (this._cur.kind()) {
            case -2:
                return XmlCursor.TokenType.END;
            case -1:
                return XmlCursor.TokenType.ENDDOC;
            case 0:
                return XmlCursor.TokenType.TEXT;
            case 1:
                return XmlCursor.TokenType.STARTDOC;
            case 2:
                return XmlCursor.TokenType.START;
            case 3:
                return this._cur.isXmlns() ? XmlCursor.TokenType.NAMESPACE : XmlCursor.TokenType.ATTR;
            case 4:
                return XmlCursor.TokenType.COMMENT;
            case 5:
                return XmlCursor.TokenType.PROCINST;
            default:
                throw new IllegalStateException();
        }
    }

    public boolean _isStartdoc() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isRoot();
        }
        throw new AssertionError();
    }

    public boolean _isEnddoc() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isEndRoot();
        }
        throw new AssertionError();
    }

    public boolean _isStart() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isElem();
        }
        throw new AssertionError();
    }

    public boolean _isEnd() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isEnd();
        }
        throw new AssertionError();
    }

    public boolean _isText() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isText();
        }
        throw new AssertionError();
    }

    public boolean _isAttr() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isNormalAttr();
        }
        throw new AssertionError();
    }

    public boolean _isNamespace() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isXmlns();
        }
        throw new AssertionError();
    }

    public boolean _isComment() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isComment();
        }
        throw new AssertionError();
    }

    public boolean _isProcinst() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isProcinst();
        }
        throw new AssertionError();
    }

    public boolean _isContainer() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isContainer();
        }
        throw new AssertionError();
    }

    public boolean _isFinish() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isFinish();
        }
        throw new AssertionError();
    }

    public boolean _isAnyAttr() {
        if ($assertionsDisabled || isValid()) {
            return this._cur.isAttr();
        }
        throw new AssertionError();
    }

    public XmlCursor.TokenType _toNextToken() {
        if (!$assertionsDisabled && !isValid()) {
            throw new AssertionError();
        }
        switch (this._cur.kind()) {
            case 1:
            case 2:
                if (!this._cur.toFirstAttr()) {
                    this._cur.next();
                    break;
                }
                break;
            case 3:
                if (!this._cur.toNextSibling()) {
                    this._cur.toParent();
                    this._cur.next();
                    break;
                }
                break;
            case 4:
            case 5:
                this._cur.skip();
                break;
            default:
                if (!this._cur.next()) {
                    return XmlCursor.TokenType.NONE;
                }
                break;
        }
        return _currentTokenType();
    }

    public XmlCursor.TokenType _toPrevToken() {
        if (!$assertionsDisabled && !isValid()) {
            throw new AssertionError();
        }
        boolean wasText = this._cur.isText();
        if (!this._cur.prev()) {
            if (!$assertionsDisabled && !this._cur.isRoot() && !this._cur.isAttr()) {
                throw new AssertionError();
            }
            if (this._cur.isRoot()) {
                return XmlCursor.TokenType.NONE;
            }
            this._cur.toParent();
        } else {
            int k = this._cur.kind();
            if (k < 0 && (k == -4 || k == -5 || k == -3)) {
                this._cur.toParent();
            } else if (this._cur.isContainer()) {
                this._cur.toLastAttr();
            } else if (wasText && this._cur.isText()) {
                return _toPrevToken();
            }
        }
        return _currentTokenType();
    }

    public Object _monitor() {
        return this._cur._locale;
    }

    public boolean _toParent() {
        Cur c = this._cur.tempCur();
        if (!c.toParent()) {
            return false;
        }
        this._cur.moveToCur(c);
        c.release();
        return true;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Cursor$ChangeStampImpl.class */
    private static final class ChangeStampImpl implements XmlCursor.ChangeStamp {
        private final Locale _locale;
        private final long _versionStamp;

        ChangeStampImpl(Locale l) {
            this._locale = l;
            this._versionStamp = this._locale.version();
        }

        @Override // org.apache.xmlbeans.XmlCursor.ChangeStamp
        public boolean hasChanged() {
            return this._versionStamp != this._locale.version();
        }
    }

    public XmlCursor.ChangeStamp _getDocChangeStamp() {
        return new ChangeStampImpl(this._cur._locale);
    }

    public XMLInputStream _newXMLInputStream() {
        return _newXMLInputStream(null);
    }

    public XMLStreamReader _newXMLStreamReader() {
        return _newXMLStreamReader(null);
    }

    public Node _newDomNode() {
        return _newDomNode(null);
    }

    public InputStream _newInputStream() {
        return _newInputStream(null);
    }

    public String _xmlText() {
        return _xmlText(null);
    }

    public Reader _newReader() {
        return _newReader(null);
    }

    public void _save(File file) throws IOException {
        _save(file, (XmlOptions) null);
    }

    public void _save(OutputStream os) throws IOException {
        _save(os, (XmlOptions) null);
    }

    public void _save(Writer w) throws IOException {
        _save(w, (XmlOptions) null);
    }

    public void _save(ContentHandler ch2, LexicalHandler lh) throws SAXException {
        _save(ch2, lh, null);
    }

    public XmlDocumentProperties _documentProperties() {
        return Locale.getDocProps(this._cur, true);
    }

    public XMLStreamReader _newXMLStreamReader(XmlOptions options) {
        return Jsr173.newXmlStreamReader(this._cur, options);
    }

    public XMLInputStream _newXMLInputStream(XmlOptions options) {
        return new Saver.XmlInputStreamImpl(this._cur, options);
    }

    public String _xmlText(XmlOptions options) {
        if ($assertionsDisabled || isValid()) {
            return new Saver.TextSaver(this._cur, options, null).saveToString();
        }
        throw new AssertionError();
    }

    public InputStream _newInputStream(XmlOptions options) {
        return new Saver.InputStreamSaver(this._cur, options);
    }

    public Reader _newReader(XmlOptions options) {
        return new Saver.TextReader(this._cur, options);
    }

    public void _save(ContentHandler ch2, LexicalHandler lh, XmlOptions options) throws SAXException {
        new Saver.SaxSaver(this._cur, options, ch2, lh);
    }

    public void _save(File file, XmlOptions options) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Null file specified");
        }
        OutputStream os = new FileOutputStream(file);
        try {
            _save(os, options);
            os.close();
        } catch (Throwable th) {
            os.close();
            throw th;
        }
    }

    public void _save(OutputStream os, XmlOptions options) throws IOException {
        if (os == null) {
            throw new IllegalArgumentException("Null OutputStream specified");
        }
        InputStream is = _newInputStream(options);
        try {
            byte[] bytes = new byte[8192];
            while (true) {
                int n = is.read(bytes);
                if (n >= 0) {
                    os.write(bytes, 0, n);
                } else {
                    return;
                }
            }
        } finally {
            is.close();
        }
    }

    public void _save(Writer w, XmlOptions options) throws IOException {
        if (w == null) {
            throw new IllegalArgumentException("Null Writer specified");
        }
        if (options != null && options.hasOption(XmlOptions.SAVE_OPTIMIZE_FOR_SPEED)) {
            Saver.OptimizedForSpeedSaver.save(this._cur, w);
            return;
        }
        Reader r = _newReader(options);
        try {
            char[] chars = new char[8192];
            while (true) {
                int n = r.read(chars);
                if (n >= 0) {
                    w.write(chars, 0, n);
                } else {
                    return;
                }
            }
        } finally {
            r.close();
        }
    }

    public Node _getDomNode() {
        return (Node) this._cur.getDom();
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x00c2, code lost:
    
        r0.dispose();
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00d6, code lost:
    
        if (r4 != false) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00d9, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00dd, code lost:
    
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean isDomFragment() {
        /*
            r3 = this;
            r0 = r3
            boolean r0 = r0.isStartdoc()
            if (r0 != 0) goto L9
            r0 = 1
            return r0
        L9:
            r0 = 0
            r4 = r0
            r0 = r3
            org.apache.xmlbeans.XmlCursor r0 = r0.newCursor()
            r5 = r0
            r0 = r5
            org.apache.xmlbeans.XmlCursor$TokenType r0 = r0.toNextToken()
            int r0 = r0.intValue()
            r6 = r0
        L1a:
            r0 = r6
            switch(r0) {
                case 0: goto L94;
                case 1: goto Lb0;
                case 2: goto L94;
                case 3: goto L50;
                case 4: goto La3;
                case 5: goto L6f;
                case 6: goto L97;
                case 7: goto L97;
                case 8: goto La3;
                case 9: goto La3;
                default: goto Lbe;
            }     // Catch: java.lang.Throwable -> Lca
        L50:
            r0 = r4
            if (r0 == 0) goto L60
            r0 = 1
            r7 = r0
            r0 = r5
            r0.dispose()
            r0 = r7
            return r0
        L60:
            r0 = 1
            r4 = r0
            r0 = r5
            org.apache.xmlbeans.XmlCursor$TokenType r0 = r0.toEndToken()     // Catch: java.lang.Throwable -> Lca
            int r0 = r0.intValue()     // Catch: java.lang.Throwable -> Lca
            r6 = r0
            goto L1a
        L6f:
            r0 = r5
            java.lang.String r0 = r0.getChars()     // Catch: java.lang.Throwable -> Lca
            boolean r0 = org.apache.xmlbeans.impl.store.Locale.isWhiteSpace(r0)     // Catch: java.lang.Throwable -> Lca
            if (r0 != 0) goto L87
            r0 = 1
            r7 = r0
            r0 = r5
            r0.dispose()
            r0 = r7
            return r0
        L87:
            r0 = r5
            org.apache.xmlbeans.XmlCursor$TokenType r0 = r0.toNextToken()     // Catch: java.lang.Throwable -> Lca
            int r0 = r0.intValue()     // Catch: java.lang.Throwable -> Lca
            r6 = r0
            goto L1a
        L94:
            goto Lc1
        L97:
            r0 = 1
            r7 = r0
            r0 = r5
            r0.dispose()
            r0 = r7
            return r0
        La3:
            r0 = r5
            org.apache.xmlbeans.XmlCursor$TokenType r0 = r0.toNextToken()     // Catch: java.lang.Throwable -> Lca
            int r0 = r0.intValue()     // Catch: java.lang.Throwable -> Lca
            r6 = r0
            goto L1a
        Lb0:
            boolean r0 = org.apache.xmlbeans.impl.store.Cursor.$assertionsDisabled     // Catch: java.lang.Throwable -> Lca
            if (r0 != 0) goto Lc1
            java.lang.AssertionError r0 = new java.lang.AssertionError     // Catch: java.lang.Throwable -> Lca
            r1 = r0
            r1.<init>()     // Catch: java.lang.Throwable -> Lca
            throw r0     // Catch: java.lang.Throwable -> Lca
        Lbe:
            goto L1a
        Lc1:
            r0 = r5
            r0.dispose()
            goto Ld5
        Lca:
            r8 = move-exception
            r0 = r5
            r0.dispose()
            r0 = r8
            throw r0
        Ld5:
            r0 = r4
            if (r0 != 0) goto Ldd
            r0 = 1
            goto Lde
        Ldd:
            r0 = 0
        Lde:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Cursor.isDomFragment():boolean");
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Cursor$DomSaver.class */
    private static final class DomSaver extends Saver {
        private Cur _nodeCur;
        private SchemaType _type;
        private SchemaTypeLoader _stl;
        private XmlOptions _options;
        private boolean _isFrag;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Cursor.class.desiredAssertionStatus();
        }

        DomSaver(Cur c, boolean isFrag, XmlOptions options) {
            super(c, options);
            if (c.isUserNode()) {
                this._type = c.getUser().get_schema_type();
            }
            this._stl = c._locale._schemaTypeLoader;
            this._options = options;
            this._isFrag = isFrag;
        }

        Node saveDom() {
            Locale l = Locale.getLocale(this._stl, this._options);
            l.enter();
            try {
                this._nodeCur = l.getCur();
                while (process()) {
                }
                while (!this._nodeCur.isRoot()) {
                    this._nodeCur.toParent();
                }
                if (this._type != null) {
                    this._nodeCur.setType(this._type);
                }
                Node node = (Node) this._nodeCur.getDom();
                this._nodeCur.release();
                this._nodeCur = null;
                l.exit();
                return node;
            } catch (Throwable th) {
                l.exit();
                throw th;
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected boolean emitElement(Saver.SaveCur c, ArrayList attrNames, ArrayList attrValues) {
            if (Locale.isFragmentQName(c.getName())) {
                this._nodeCur.moveTo(null, -2);
            }
            ensureDoc();
            this._nodeCur.createElement(getQualifiedName(c, c.getName()));
            this._nodeCur.next();
            iterateMappings();
            while (hasMapping()) {
                this._nodeCur.createAttr(this._nodeCur._locale.createXmlns(mappingPrefix()));
                this._nodeCur.next();
                this._nodeCur.insertString(mappingUri());
                this._nodeCur.toParent();
                this._nodeCur.skipWithAttrs();
                nextMapping();
            }
            for (int i = 0; i < attrNames.size(); i++) {
                this._nodeCur.createAttr(getQualifiedName(c, (QName) attrNames.get(i)));
                this._nodeCur.next();
                this._nodeCur.insertString((String) attrValues.get(i));
                this._nodeCur.toParent();
                this._nodeCur.skipWithAttrs();
            }
            return false;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitFinish(Saver.SaveCur c) {
            if (!Locale.isFragmentQName(c.getName())) {
                if (!$assertionsDisabled && !this._nodeCur.isEnd()) {
                    throw new AssertionError();
                }
                this._nodeCur.next();
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitText(Saver.SaveCur c) {
            ensureDoc();
            Object src = c.getChars();
            if (c._cchSrc > 0) {
                this._nodeCur.insertChars(src, c._offSrc, c._cchSrc);
                this._nodeCur.next();
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitComment(Saver.SaveCur c) {
            ensureDoc();
            this._nodeCur.createComment();
            emitTextValue(c);
            this._nodeCur.skip();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitProcinst(Saver.SaveCur c) {
            ensureDoc();
            this._nodeCur.createProcinst(c.getName().getLocalPart());
            emitTextValue(c);
            this._nodeCur.skip();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitDocType(String docTypeName, String publicId, String systemId) {
            ensureDoc();
            XmlDocumentProperties props = Locale.getDocProps(this._nodeCur, true);
            props.setDoctypeName(docTypeName);
            props.setDoctypePublicId(publicId);
            props.setDoctypeSystemId(systemId);
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitStartDoc(Saver.SaveCur c) {
            ensureDoc();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitEndDoc(Saver.SaveCur c) {
        }

        private QName getQualifiedName(Saver.SaveCur c, QName name) {
            String uri = name.getNamespaceURI();
            String prefix = uri.length() > 0 ? getUriMapping(uri) : "";
            if (prefix.equals(name.getPrefix())) {
                return name;
            }
            return this._nodeCur._locale.makeQName(uri, name.getLocalPart(), prefix);
        }

        private void emitTextValue(Saver.SaveCur c) {
            c.push();
            c.next();
            if (c.isText()) {
                this._nodeCur.next();
                this._nodeCur.insertChars(c.getChars(), c._offSrc, c._cchSrc);
                this._nodeCur.toParent();
            }
            c.pop();
        }

        private void ensureDoc() {
            if (!this._nodeCur.isPositioned()) {
                if (this._isFrag) {
                    this._nodeCur.createDomDocFragRoot();
                } else {
                    this._nodeCur.createDomDocumentRoot();
                }
                this._nodeCur.next();
            }
        }
    }

    public Node _newDomNode(XmlOptions options) {
        if (XmlOptions.hasOption(options, XmlOptions.SAVE_INNER)) {
            options = new XmlOptions(options);
            options.remove(XmlOptions.SAVE_INNER);
        }
        return new DomSaver(this._cur, isDomFragment(), options).saveDom();
    }

    public boolean _toCursor(Cursor other) {
        if (!$assertionsDisabled && this._cur._locale != other._cur._locale) {
            throw new AssertionError();
        }
        this._cur.moveToCur(other._cur);
        return true;
    }

    public void _push() {
        this._cur.push();
    }

    public boolean _pop() {
        return this._cur.pop();
    }

    @Override // org.apache.xmlbeans.impl.store.Locale.ChangeListener
    public void notifyChange() {
        if (this._cur != null) {
            _getSelectionCount();
        }
    }

    @Override // org.apache.xmlbeans.impl.store.Locale.ChangeListener
    public void setNextChangeListener(Locale.ChangeListener listener) {
        this._nextChangeListener = listener;
    }

    @Override // org.apache.xmlbeans.impl.store.Locale.ChangeListener
    public Locale.ChangeListener getNextChangeListener() {
        return this._nextChangeListener;
    }

    public void _selectPath(String path) {
        _selectPath(path, null);
    }

    public void _selectPath(String pathExpr, XmlOptions options) {
        _clearSelections();
        if (!$assertionsDisabled && this._pathEngine != null) {
            throw new AssertionError();
        }
        this._pathEngine = Path.getCompiledPath(pathExpr, options).execute(this._cur, options);
        this._cur._locale.registerForChange(this);
    }

    public boolean _hasNextSelection() {
        int curr = this._currentSelection;
        push();
        try {
            boolean z_toNextSelection = _toNextSelection();
            this._currentSelection = curr;
            pop();
            return z_toNextSelection;
        } catch (Throwable th) {
            this._currentSelection = curr;
            pop();
            throw th;
        }
    }

    public boolean _toNextSelection() {
        return _toSelection(this._currentSelection + 1);
    }

    public boolean _toSelection(int i) {
        if (i < 0) {
            return false;
        }
        while (i >= this._cur.selectionCount()) {
            if (this._pathEngine == null) {
                return false;
            }
            if (!this._pathEngine.next(this._cur)) {
                this._pathEngine.release();
                this._pathEngine = null;
                return false;
            }
        }
        Cur cur = this._cur;
        this._currentSelection = i;
        cur.moveToSelection(i);
        return true;
    }

    public int _getSelectionCount() {
        _toSelection(Integer.MAX_VALUE);
        return this._cur.selectionCount();
    }

    public void _addToSelection() {
        _toSelection(Integer.MAX_VALUE);
        this._cur.addToSelection();
    }

    public void _clearSelections() {
        if (this._pathEngine != null) {
            this._pathEngine.release();
            this._pathEngine = null;
        }
        this._cur.clearSelection();
        this._currentSelection = -1;
    }

    public String _namespaceForPrefix(String prefix) {
        if (!this._cur.isContainer()) {
            throw new IllegalStateException("Not on a container");
        }
        return this._cur.namespaceForPrefix(prefix, true);
    }

    public String _prefixForNamespace(String ns) {
        if (ns == null || ns.length() == 0) {
            throw new IllegalArgumentException("Must specify a namespace");
        }
        return this._cur.prefixForNamespace(ns, null, true);
    }

    public void _getAllNamespaces(Map addToThis) {
        if (!this._cur.isContainer()) {
            throw new IllegalStateException("Not on a container");
        }
        if (addToThis != null) {
            Locale.getAllNamespaces(this._cur, addToThis);
        }
    }

    public XmlObject _getObject() {
        return this._cur.getObject();
    }

    public XmlCursor.TokenType _prevTokenType() {
        this._cur.push();
        XmlCursor.TokenType tt = _toPrevToken();
        this._cur.pop();
        return tt;
    }

    public boolean _hasNextToken() {
        return (this._cur._pos == -1 && this._cur._xobj.kind() == 1) ? false : true;
    }

    public boolean _hasPrevToken() {
        return this._cur.kind() != 1;
    }

    public XmlCursor.TokenType _toFirstContentToken() {
        if (!this._cur.isContainer()) {
            return XmlCursor.TokenType.NONE;
        }
        this._cur.next();
        return currentTokenType();
    }

    public XmlCursor.TokenType _toEndToken() {
        if (!this._cur.isContainer()) {
            return XmlCursor.TokenType.NONE;
        }
        this._cur.toEnd();
        return currentTokenType();
    }

    public boolean _toChild(String local) {
        return _toChild((String) null, local);
    }

    public boolean _toChild(QName name) {
        return _toChild(name, 0);
    }

    public boolean _toChild(int index) {
        return _toChild((QName) null, index);
    }

    public boolean _toChild(String uri, String local) {
        validateLocalName(local);
        return _toChild(this._cur._locale.makeQName(uri, local), 0);
    }

    public boolean _toChild(QName name, int index) {
        return Locale.toChild(this._cur, name, index);
    }

    public int _toNextChar(int maxCharacterCount) {
        return this._cur.nextChars(maxCharacterCount);
    }

    public int _toPrevChar(int maxCharacterCount) {
        return this._cur.prevChars(maxCharacterCount);
    }

    public boolean _toPrevSibling() {
        return Locale.toPrevSiblingElement(this._cur);
    }

    public boolean _toLastChild() {
        return Locale.toLastChildElement(this._cur);
    }

    public boolean _toFirstChild() {
        return Locale.toFirstChildElement(this._cur);
    }

    public boolean _toNextSibling(String name) {
        return _toNextSibling(new QName(name));
    }

    public boolean _toNextSibling(String uri, String local) {
        validateLocalName(local);
        return _toNextSibling(this._cur._locale._qnameFactory.getQName(uri, local));
    }

    public boolean _toNextSibling(QName name) {
        this._cur.push();
        while (___toNextSibling()) {
            if (this._cur.getName().equals(name)) {
                this._cur.popButStay();
                return true;
            }
        }
        this._cur.pop();
        return false;
    }

    public boolean _toFirstAttribute() {
        return this._cur.isContainer() && Locale.toFirstNormalAttr(this._cur);
    }

    public boolean _toLastAttribute() {
        if (this._cur.isContainer()) {
            this._cur.push();
            this._cur.push();
            boolean foundAttr = false;
            while (this._cur.toNextAttr()) {
                if (this._cur.isNormalAttr()) {
                    this._cur.popButStay();
                    this._cur.push();
                    foundAttr = true;
                }
            }
            this._cur.pop();
            if (foundAttr) {
                this._cur.popButStay();
                return true;
            }
            this._cur.pop();
            return false;
        }
        return false;
    }

    public boolean _toNextAttribute() {
        return this._cur.isAttr() && Locale.toNextNormalAttr(this._cur);
    }

    public boolean _toPrevAttribute() {
        return this._cur.isAttr() && Locale.toPrevNormalAttr(this._cur);
    }

    public String _getAttributeText(QName attrName) {
        if (attrName == null) {
            throw new IllegalArgumentException("Attr name is null");
        }
        if (!this._cur.isContainer()) {
            return null;
        }
        return this._cur.getAttrValue(attrName);
    }

    public boolean _setAttributeText(QName attrName, String value) {
        if (attrName == null) {
            throw new IllegalArgumentException("Attr name is null");
        }
        validateLocalName(attrName.getLocalPart());
        if (!this._cur.isContainer()) {
            return false;
        }
        this._cur.setAttrValue(attrName, value);
        return true;
    }

    public boolean _removeAttribute(QName attrName) {
        if (attrName == null) {
            throw new IllegalArgumentException("Attr name is null");
        }
        if (!this._cur.isContainer()) {
            return false;
        }
        return this._cur.removeAttr(attrName);
    }

    public String _getTextValue() {
        if (this._cur.isText()) {
            return _getChars();
        }
        if (this._cur.isNode()) {
            return this._cur.hasChildren() ? Locale.getTextValue(this._cur) : this._cur.getValueAsString();
        }
        throw new IllegalStateException("Can't get text value, current token can have no text value");
    }

    public int _getTextValue(char[] chars, int offset, int max) {
        if (this._cur.isText()) {
            return _getChars(chars, offset, max);
        }
        if (chars == null) {
            throw new IllegalArgumentException("char buffer is null");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        if (offset >= chars.length) {
            throw new IllegalArgumentException("offset off end");
        }
        if (max < 0) {
            max = Integer.MAX_VALUE;
        }
        if (offset + max > chars.length) {
            max = chars.length - offset;
        }
        if (!this._cur.isNode()) {
            throw new IllegalStateException("Can't get text value, current token can have no text value");
        }
        if (this._cur.hasChildren()) {
            return Locale.getTextValue(this._cur, 1, chars, offset, max);
        }
        Object src = this._cur.getFirstChars();
        if (this._cur._cchSrc > max) {
            this._cur._cchSrc = max;
        }
        if (this._cur._cchSrc <= 0) {
            return 0;
        }
        CharUtil.getChars(chars, offset, src, this._cur._offSrc, this._cur._cchSrc);
        return this._cur._cchSrc;
    }

    private void setTextValue(Object src, int off, int cch) {
        if (!this._cur.isNode()) {
            throw new IllegalStateException("Can't set text value, current token can have no text value");
        }
        this._cur.moveNodeContents(null, false);
        this._cur.next();
        this._cur.insertChars(src, off, cch);
        this._cur.toParent();
    }

    public void _setTextValue(String text) {
        if (text == null) {
            text = "";
        }
        setTextValue(text, 0, text.length());
    }

    public void _setTextValue(char[] sourceChars, int offset, int length) {
        if (length < 0) {
            throw new IndexOutOfBoundsException("setTextValue: length < 0");
        }
        if (sourceChars == null) {
            if (length > 0) {
                throw new IllegalArgumentException("setTextValue: sourceChars == null");
            }
            setTextValue((char[]) null, 0, 0);
        } else {
            if (offset < 0 || offset >= sourceChars.length) {
                throw new IndexOutOfBoundsException("setTextValue: offset out of bounds");
            }
            if (offset + length > sourceChars.length) {
                length = sourceChars.length - offset;
            }
            CharUtil cu = this._cur._locale.getCharUtil();
            setTextValue(cu.saveChars(sourceChars, offset, length), cu._offSrc, cu._cchSrc);
        }
    }

    public String _getChars() {
        return this._cur.getCharsAsString(-1);
    }

    public int _getChars(char[] buf, int off, int cch) {
        int cchRight = this._cur.cchRight();
        if (cch < 0 || cch > cchRight) {
            cch = cchRight;
        }
        if (buf == null || off >= buf.length) {
            return 0;
        }
        if (buf.length - off < cch) {
            cch = buf.length - off;
        }
        Object src = this._cur.getChars(cch);
        CharUtil.getChars(buf, off, src, this._cur._offSrc, this._cur._cchSrc);
        return this._cur._cchSrc;
    }

    public void _toStartDoc() {
        this._cur.toRoot();
    }

    public void _toEndDoc() {
        _toStartDoc();
        this._cur.toEnd();
    }

    public int _comparePosition(Cursor other) {
        int s = this._cur.comparePosition(other._cur);
        if (s == 2) {
            throw new IllegalArgumentException("Cursors not in same document");
        }
        if ($assertionsDisabled || (s >= -1 && s <= 1)) {
            return s;
        }
        throw new AssertionError();
    }

    public boolean _isLeftOf(Cursor other) {
        return _comparePosition(other) < 0;
    }

    public boolean _isAtSamePositionAs(Cursor other) {
        return this._cur.isSamePos(other._cur);
    }

    public boolean _isRightOf(Cursor other) {
        return _comparePosition(other) > 0;
    }

    public XmlCursor _execQuery(String query) {
        return _execQuery(query, null);
    }

    public XmlCursor _execQuery(String query, XmlOptions options) {
        checkThisCursor();
        return Query.cursorExecQuery(this._cur, query, options);
    }

    public boolean _toBookmark(XmlCursor.XmlBookmark bookmark) {
        if (bookmark == null || !(bookmark._currentMark instanceof Xobj.Bookmark)) {
            return false;
        }
        Xobj.Bookmark m = (Xobj.Bookmark) bookmark._currentMark;
        if (m._xobj == null || m._xobj._locale != this._cur._locale) {
            return false;
        }
        this._cur.moveTo(m._xobj, m._pos);
        return true;
    }

    public XmlCursor.XmlBookmark _toNextBookmark(Object key) {
        if (key == null) {
            return null;
        }
        this._cur.push();
        do {
            int cch = this._cur.cchRight();
            if (cch > 1) {
                this._cur.nextChars(1);
                Cur cur = this._cur;
                int cch2 = this._cur.firstBookmarkInChars(key, cch - 1);
                cur.nextChars(cch2 >= 0 ? cch2 : -1);
            } else if (_toNextToken().isNone()) {
                this._cur.pop();
                return null;
            }
            XmlCursor.XmlBookmark bm = getBookmark(key, this._cur);
            if (bm != null) {
                this._cur.popButStay();
                return bm;
            }
        } while (this._cur.kind() != -1);
        this._cur.pop();
        return null;
    }

    public XmlCursor.XmlBookmark _toPrevBookmark(Object key) {
        if (key == null) {
            return null;
        }
        this._cur.push();
        do {
            int cch = this._cur.cchLeft();
            if (cch > 1) {
                this._cur.prevChars(1);
                Cur cur = this._cur;
                int cch2 = this._cur.firstBookmarkInCharsLeft(key, cch - 1);
                cur.prevChars(cch2 >= 0 ? cch2 : -1);
            } else if (cch == 1) {
                this._cur.prevChars(1);
            } else if (_toPrevToken().isNone()) {
                this._cur.pop();
                return null;
            }
            XmlCursor.XmlBookmark bm = getBookmark(key, this._cur);
            if (bm != null) {
                this._cur.popButStay();
                return bm;
            }
        } while (this._cur.kind() != 1);
        this._cur.pop();
        return null;
    }

    public void _setBookmark(XmlCursor.XmlBookmark bookmark) {
        if (bookmark != null) {
            if (bookmark.getKey() == null) {
                throw new IllegalArgumentException("Annotation key is null");
            }
            bookmark._currentMark = this._cur.setBookmark(bookmark.getKey(), bookmark);
        }
    }

    static XmlCursor.XmlBookmark getBookmark(Object key, Cur c) {
        Object bm;
        if (key == null || (bm = c.getBookmark(key)) == null || !(bm instanceof XmlCursor.XmlBookmark)) {
            return null;
        }
        return (XmlCursor.XmlBookmark) bm;
    }

    public XmlCursor.XmlBookmark _getBookmark(Object key) {
        if (key == null) {
            return null;
        }
        return getBookmark(key, this._cur);
    }

    public void _clearBookmark(Object key) {
        if (key != null) {
            this._cur.setBookmark(key, null);
        }
    }

    public void _getAllBookmarkRefs(Collection listToFill) {
        if (listToFill != null) {
            Xobj.Bookmark bookmark = this._cur._xobj._bookmarks;
            while (true) {
                Xobj.Bookmark b = bookmark;
                if (b != null) {
                    if (b._value instanceof XmlCursor.XmlBookmark) {
                        listToFill.add(b._value);
                    }
                    bookmark = b._next;
                } else {
                    return;
                }
            }
        }
    }

    public boolean _removeXml() {
        if (this._cur.isRoot()) {
            throw new IllegalStateException("Can't remove a whole document.");
        }
        if (this._cur.isFinish()) {
            return false;
        }
        if (!$assertionsDisabled && !this._cur.isText() && !this._cur.isNode()) {
            throw new AssertionError();
        }
        if (this._cur.isText()) {
            this._cur.moveChars(null, -1);
            return true;
        }
        this._cur.moveNode(null);
        return true;
    }

    public boolean _moveXml(Cursor to) {
        to.checkInsertionValidity(this._cur);
        if (this._cur.isText()) {
            int cchRight = this._cur.cchRight();
            if (!$assertionsDisabled && cchRight <= 0) {
                throw new AssertionError();
            }
            if (this._cur.inChars(to._cur, cchRight, true)) {
                return false;
            }
            this._cur.moveChars(to._cur, cchRight);
            to._cur.nextChars(cchRight);
            return true;
        }
        if (this._cur.contains(to._cur)) {
            return false;
        }
        Cur c = to.tempCur();
        this._cur.moveNode(to._cur);
        to._cur.moveToCur(c);
        c.release();
        return true;
    }

    public boolean _copyXml(Cursor to) {
        to.checkInsertionValidity(this._cur);
        if (!$assertionsDisabled && !this._cur.isText() && !this._cur.isNode()) {
            throw new AssertionError();
        }
        Cur c = to.tempCur();
        if (this._cur.isText()) {
            to._cur.insertChars(this._cur.getChars(-1), this._cur._offSrc, this._cur._cchSrc);
        } else {
            this._cur.copyNode(to._cur);
        }
        to._cur.moveToCur(c);
        c.release();
        return true;
    }

    public boolean _removeXmlContents() {
        if (!this._cur.isContainer()) {
            return false;
        }
        this._cur.moveNodeContents(null, false);
        return true;
    }

    private boolean checkContentInsertionValidity(Cursor to) {
        this._cur.push();
        this._cur.next();
        if (this._cur.isFinish()) {
            this._cur.pop();
            return false;
        }
        try {
            to.checkInsertionValidity(this._cur);
            this._cur.pop();
            return true;
        } catch (IllegalArgumentException e) {
            this._cur.pop();
            throw e;
        }
    }

    public boolean _moveXmlContents(Cursor to) {
        if (!this._cur.isContainer() || this._cur.contains(to._cur) || !checkContentInsertionValidity(to)) {
            return false;
        }
        Cur c = to.tempCur();
        this._cur.moveNodeContents(to._cur, false);
        to._cur.moveToCur(c);
        c.release();
        return true;
    }

    public boolean _copyXmlContents(Cursor to) {
        if (!this._cur.isContainer() || this._cur.contains(to._cur) || !checkContentInsertionValidity(to)) {
            return false;
        }
        Cur c = this._cur._locale.tempCur();
        this._cur.copyNode(c);
        Cur c2 = to._cur.tempCur();
        c.moveNodeContents(to._cur, false);
        c.release();
        to._cur.moveToCur(c2);
        c2.release();
        return true;
    }

    public int _removeChars(int cch) {
        int cchRight = this._cur.cchRight();
        if (cchRight == 0 || cch == 0) {
            return 0;
        }
        if (cch < 0 || cch > cchRight) {
            cch = cchRight;
        }
        this._cur.moveChars(null, cch);
        return this._cur._cchSrc;
    }

    public int _moveChars(int cch, Cursor to) {
        int cchRight = this._cur.cchRight();
        if (cchRight <= 0 || cch == 0) {
            return 0;
        }
        if (cch < 0 || cch > cchRight) {
            cch = cchRight;
        }
        to.checkInsertionValidity(this._cur);
        this._cur.moveChars(to._cur, cch);
        to._cur.nextChars(this._cur._cchSrc);
        return this._cur._cchSrc;
    }

    public int _copyChars(int cch, Cursor to) {
        int cchRight = this._cur.cchRight();
        if (cchRight <= 0 || cch == 0) {
            return 0;
        }
        if (cch < 0 || cch > cchRight) {
            cch = cchRight;
        }
        to.checkInsertionValidity(this._cur);
        to._cur.insertChars(this._cur.getChars(cch), this._cur._offSrc, this._cur._cchSrc);
        to._cur.nextChars(this._cur._cchSrc);
        return this._cur._cchSrc;
    }

    public void _insertChars(String text) {
        int l = text == null ? 0 : text.length();
        if (l > 0) {
            if (this._cur.isRoot() || this._cur.isAttr()) {
                throw new IllegalStateException("Can't insert before the document or an attribute.");
            }
            this._cur.insertChars(text, 0, l);
            this._cur.nextChars(l);
        }
    }

    public void _beginElement(String localName) {
        _insertElementWithText(localName, null, null);
        _toPrevToken();
    }

    public void _beginElement(String localName, String uri) {
        _insertElementWithText(localName, uri, null);
        _toPrevToken();
    }

    public void _beginElement(QName name) {
        _insertElementWithText(name, (String) null);
        _toPrevToken();
    }

    public void _insertElement(String localName) {
        _insertElementWithText(localName, null, null);
    }

    public void _insertElement(String localName, String uri) {
        _insertElementWithText(localName, uri, null);
    }

    public void _insertElement(QName name) {
        _insertElementWithText(name, (String) null);
    }

    public void _insertElementWithText(String localName, String text) {
        _insertElementWithText(localName, null, text);
    }

    public void _insertElementWithText(String localName, String uri, String text) {
        validateLocalName(localName);
        _insertElementWithText(this._cur._locale.makeQName(uri, localName), text);
    }

    public void _insertElementWithText(QName name, String text) {
        validateLocalName(name.getLocalPart());
        Cur c = this._cur._locale.tempCur();
        c.createElement(name);
        insertNode(c, text);
        c.release();
    }

    public void _insertAttribute(String localName) {
        _insertAttributeWithValue(localName, (String) null);
    }

    public void _insertAttribute(String localName, String uri) {
        _insertAttributeWithValue(localName, uri, null);
    }

    public void _insertAttribute(QName name) {
        _insertAttributeWithValue(name, (String) null);
    }

    public void _insertAttributeWithValue(String localName, String value) {
        _insertAttributeWithValue(localName, null, value);
    }

    public void _insertAttributeWithValue(String localName, String uri, String value) {
        validateLocalName(localName);
        _insertAttributeWithValue(this._cur._locale.makeQName(uri, localName), value);
    }

    public void _insertAttributeWithValue(QName name, String text) {
        validateLocalName(name.getLocalPart());
        Cur c = this._cur._locale.tempCur();
        c.createAttr(name);
        insertNode(c, text);
        c.release();
    }

    public void _insertNamespace(String prefix, String namespace) {
        _insertAttributeWithValue(this._cur._locale.createXmlns(prefix), namespace);
    }

    public void _insertComment(String text) {
        Cur c = this._cur._locale.tempCur();
        c.createComment();
        insertNode(c, text);
        c.release();
    }

    public void _insertProcInst(String target, String text) {
        validateLocalName(target);
        if (Locale.beginsWithXml(target) && target.length() == 3) {
            throw new IllegalArgumentException("Target is 'xml'");
        }
        Cur c = this._cur._locale.tempCur();
        c.createProcinst(target);
        insertNode(c, text);
        c.release();
    }

    public void _dump() {
        this._cur.dump();
    }

    private void checkThisCursor() {
        if (this._cur == null) {
            throw new IllegalStateException("This cursor has been disposed");
        }
    }

    private Cursor checkCursors(XmlCursor xOther) {
        checkThisCursor();
        if (xOther == null) {
            throw new IllegalArgumentException("Other cursor is <null>");
        }
        if (!(xOther instanceof Cursor)) {
            throw new IllegalArgumentException("Incompatible cursors: " + xOther);
        }
        Cursor other = (Cursor) xOther;
        if (other._cur == null) {
            throw new IllegalStateException("Other cursor has been disposed");
        }
        return other;
    }

    private int twoLocaleOp(XmlCursor xOther, int op, int arg) {
        int iTwoLocaleOp;
        int iTwoLocaleOp2;
        int iTwoLocaleOp3;
        int iTwoLocaleOp4;
        Cursor other = checkCursors(xOther);
        Locale locale = this._cur._locale;
        Locale otherLocale = other._cur._locale;
        if (locale == otherLocale) {
            if (locale.noSync()) {
                return twoLocaleOp(other, op, arg);
            }
            synchronized (locale) {
                iTwoLocaleOp4 = twoLocaleOp(other, op, arg);
            }
            return iTwoLocaleOp4;
        }
        if (locale.noSync()) {
            if (otherLocale.noSync()) {
                return twoLocaleOp(other, op, arg);
            }
            synchronized (otherLocale) {
                iTwoLocaleOp3 = twoLocaleOp(other, op, arg);
            }
            return iTwoLocaleOp3;
        }
        if (otherLocale.noSync()) {
            synchronized (locale) {
                iTwoLocaleOp2 = twoLocaleOp(other, op, arg);
            }
            return iTwoLocaleOp2;
        }
        boolean acquired = false;
        try {
            try {
                GlobalLock.acquire();
                synchronized (locale) {
                    synchronized (otherLocale) {
                        GlobalLock.release();
                        acquired = false;
                        iTwoLocaleOp = twoLocaleOp(other, op, arg);
                    }
                }
                return iTwoLocaleOp;
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } finally {
            if (acquired) {
                GlobalLock.release();
            }
        }
    }

    private int twoLocaleOp(Cursor other, int op, int arg) {
        Locale locale = this._cur._locale;
        Locale otherLocale = other._cur._locale;
        locale.enter(otherLocale);
        try {
            switch (op) {
                case 0:
                    return _moveXml(other) ? 1 : 0;
                case 1:
                    int i = _copyXml(other) ? 1 : 0;
                    locale.exit(otherLocale);
                    return i;
                case 2:
                    int i2 = _moveXmlContents(other) ? 1 : 0;
                    locale.exit(otherLocale);
                    return i2;
                case 3:
                    int i3 = _copyXmlContents(other) ? 1 : 0;
                    locale.exit(otherLocale);
                    return i3;
                case 4:
                    int i_moveChars = _moveChars(arg, other);
                    locale.exit(otherLocale);
                    return i_moveChars;
                case 5:
                    int i_copyChars = _copyChars(arg, other);
                    locale.exit(otherLocale);
                    return i_copyChars;
                default:
                    throw new RuntimeException("Unknown operation: " + op);
            }
        } finally {
            locale.exit(otherLocale);
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean moveXml(XmlCursor xTo) {
        return twoLocaleOp(xTo, 0, 0) == 1;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean copyXml(XmlCursor xTo) {
        return twoLocaleOp(xTo, 1, 0) == 1;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean moveXmlContents(XmlCursor xTo) {
        return twoLocaleOp(xTo, 2, 0) == 1;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean copyXmlContents(XmlCursor xTo) {
        return twoLocaleOp(xTo, 3, 0) == 1;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public int moveChars(int cch, XmlCursor xTo) {
        return twoLocaleOp(xTo, 4, cch);
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public int copyChars(int cch, XmlCursor xTo) {
        return twoLocaleOp(xTo, 5, cch);
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toCursor(XmlCursor xOther) {
        boolean z_toCursor;
        Cursor other = checkCursors(xOther);
        if (this._cur._locale != other._cur._locale) {
            return false;
        }
        if (this._cur._locale.noSync()) {
            this._cur._locale.enter();
            try {
                boolean z_toCursor2 = _toCursor(other);
                this._cur._locale.exit();
                return z_toCursor2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toCursor = _toCursor(other);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toCursor;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isInSameDocument(XmlCursor xOther) {
        if (xOther == null) {
            return false;
        }
        return this._cur.isInSameTree(checkCursors(xOther)._cur);
    }

    private Cursor preCheck(XmlCursor xOther) {
        Cursor other = checkCursors(xOther);
        if (this._cur._locale != other._cur._locale) {
            throw new IllegalArgumentException("Cursors not in same document");
        }
        return other;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public int comparePosition(XmlCursor xOther) {
        int i_comparePosition;
        Cursor other = preCheck(xOther);
        if (this._cur._locale.noSync()) {
            this._cur._locale.enter();
            try {
                int i_comparePosition2 = _comparePosition(other);
                this._cur._locale.exit();
                return i_comparePosition2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                i_comparePosition = _comparePosition(other);
                this._cur._locale.exit();
            } finally {
            }
        }
        return i_comparePosition;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isLeftOf(XmlCursor xOther) {
        boolean z_isLeftOf;
        Cursor other = preCheck(xOther);
        if (this._cur._locale.noSync()) {
            this._cur._locale.enter();
            try {
                boolean z_isLeftOf2 = _isLeftOf(other);
                this._cur._locale.exit();
                return z_isLeftOf2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_isLeftOf = _isLeftOf(other);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_isLeftOf;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isAtSamePositionAs(XmlCursor xOther) {
        boolean z_isAtSamePositionAs;
        Cursor other = preCheck(xOther);
        if (this._cur._locale.noSync()) {
            this._cur._locale.enter();
            try {
                boolean z_isAtSamePositionAs2 = _isAtSamePositionAs(other);
                this._cur._locale.exit();
                return z_isAtSamePositionAs2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_isAtSamePositionAs = _isAtSamePositionAs(other);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_isAtSamePositionAs;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isRightOf(XmlCursor xOther) {
        boolean z_isRightOf;
        Cursor other = preCheck(xOther);
        if (this._cur._locale.noSync()) {
            this._cur._locale.enter();
            try {
                boolean z_isRightOf2 = _isRightOf(other);
                this._cur._locale.exit();
                return z_isRightOf2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_isRightOf = _isRightOf(other);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_isRightOf;
    }

    public static XmlCursor newCursor(Xobj x, int p) {
        Cursor cursor;
        Locale l = x._locale;
        if (l.noSync()) {
            l.enter();
            try {
                Cursor cursor2 = new Cursor(x, p);
                l.exit();
                return cursor2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                cursor = new Cursor(x, p);
                l.exit();
            } finally {
            }
        }
        return cursor;
    }

    private boolean preCheck() {
        checkThisCursor();
        return this._cur._locale.noSync();
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void dispose() {
        if (this._cur != null) {
            Locale l = this._cur._locale;
            if (preCheck()) {
                l.enter();
                try {
                    _dispose();
                    l.exit();
                    return;
                } finally {
                }
            }
            synchronized (l) {
                l.enter();
                try {
                    _dispose();
                    l.exit();
                } finally {
                }
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Object monitor() {
        Object obj_monitor;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                Object obj_monitor2 = _monitor();
                this._cur._locale.exit();
                return obj_monitor2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                obj_monitor = _monitor();
                this._cur._locale.exit();
            } finally {
            }
        }
        return obj_monitor;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XmlDocumentProperties documentProperties() {
        XmlDocumentProperties xmlDocumentProperties_documentProperties;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlDocumentProperties xmlDocumentProperties_documentProperties2 = _documentProperties();
                this._cur._locale.exit();
                return xmlDocumentProperties_documentProperties2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xmlDocumentProperties_documentProperties = _documentProperties();
                this._cur._locale.exit();
            } finally {
            }
        }
        return xmlDocumentProperties_documentProperties;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XmlCursor newCursor() {
        XmlCursor xmlCursor_newCursor;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor xmlCursor_newCursor2 = _newCursor();
                this._cur._locale.exit();
                return xmlCursor_newCursor2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xmlCursor_newCursor = _newCursor();
                this._cur._locale.exit();
            } finally {
            }
        }
        return xmlCursor_newCursor;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLStreamReader newXMLStreamReader() {
        XMLStreamReader xMLStreamReader_newXMLStreamReader;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XMLStreamReader xMLStreamReader_newXMLStreamReader2 = _newXMLStreamReader();
                this._cur._locale.exit();
                return xMLStreamReader_newXMLStreamReader2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xMLStreamReader_newXMLStreamReader = _newXMLStreamReader();
                this._cur._locale.exit();
            } finally {
            }
        }
        return xMLStreamReader_newXMLStreamReader;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLStreamReader newXMLStreamReader(XmlOptions options) {
        XMLStreamReader xMLStreamReader_newXMLStreamReader;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XMLStreamReader xMLStreamReader_newXMLStreamReader2 = _newXMLStreamReader(options);
                this._cur._locale.exit();
                return xMLStreamReader_newXMLStreamReader2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xMLStreamReader_newXMLStreamReader = _newXMLStreamReader(options);
                this._cur._locale.exit();
            } finally {
            }
        }
        return xMLStreamReader_newXMLStreamReader;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLInputStream newXMLInputStream() {
        XMLInputStream xMLInputStream_newXMLInputStream;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XMLInputStream xMLInputStream_newXMLInputStream2 = _newXMLInputStream();
                this._cur._locale.exit();
                return xMLInputStream_newXMLInputStream2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xMLInputStream_newXMLInputStream = _newXMLInputStream();
                this._cur._locale.exit();
            } finally {
            }
        }
        return xMLInputStream_newXMLInputStream;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public String xmlText() {
        String str_xmlText;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                String str_xmlText2 = _xmlText();
                this._cur._locale.exit();
                return str_xmlText2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                str_xmlText = _xmlText();
                this._cur._locale.exit();
            } finally {
            }
        }
        return str_xmlText;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public InputStream newInputStream() {
        InputStream inputStream_newInputStream;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                InputStream inputStream_newInputStream2 = _newInputStream();
                this._cur._locale.exit();
                return inputStream_newInputStream2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                inputStream_newInputStream = _newInputStream();
                this._cur._locale.exit();
            } finally {
            }
        }
        return inputStream_newInputStream;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Reader newReader() {
        Reader reader_newReader;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                Reader reader_newReader2 = _newReader();
                this._cur._locale.exit();
                return reader_newReader2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                reader_newReader = _newReader();
                this._cur._locale.exit();
            } finally {
            }
        }
        return reader_newReader;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Node newDomNode() {
        Node node_newDomNode;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                Node node_newDomNode2 = _newDomNode();
                this._cur._locale.exit();
                return node_newDomNode2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                node_newDomNode = _newDomNode();
                this._cur._locale.exit();
            } finally {
            }
        }
        return node_newDomNode;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Node getDomNode() {
        Node node_getDomNode;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                Node node_getDomNode2 = _getDomNode();
                this._cur._locale.exit();
                return node_getDomNode2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                node_getDomNode = _getDomNode();
                this._cur._locale.exit();
            } finally {
            }
        }
        return node_getDomNode;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(ContentHandler ch2, LexicalHandler lh) throws SAXException {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _save(ch2, lh);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _save(ch2, lh);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(File file) throws IOException {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _save(file);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _save(file);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(OutputStream os) throws IOException {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _save(os);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _save(os);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(Writer w) throws IOException {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _save(w);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _save(w);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLInputStream newXMLInputStream(XmlOptions options) {
        XMLInputStream xMLInputStream_newXMLInputStream;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XMLInputStream xMLInputStream_newXMLInputStream2 = _newXMLInputStream(options);
                this._cur._locale.exit();
                return xMLInputStream_newXMLInputStream2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xMLInputStream_newXMLInputStream = _newXMLInputStream(options);
                this._cur._locale.exit();
            } finally {
            }
        }
        return xMLInputStream_newXMLInputStream;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public String xmlText(XmlOptions options) {
        String str_xmlText;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                String str_xmlText2 = _xmlText(options);
                this._cur._locale.exit();
                return str_xmlText2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                str_xmlText = _xmlText(options);
                this._cur._locale.exit();
            } finally {
            }
        }
        return str_xmlText;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public InputStream newInputStream(XmlOptions options) {
        InputStream inputStream_newInputStream;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                InputStream inputStream_newInputStream2 = _newInputStream(options);
                this._cur._locale.exit();
                return inputStream_newInputStream2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                inputStream_newInputStream = _newInputStream(options);
                this._cur._locale.exit();
            } finally {
            }
        }
        return inputStream_newInputStream;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Reader newReader(XmlOptions options) {
        Reader reader_newReader;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                Reader reader_newReader2 = _newReader(options);
                this._cur._locale.exit();
                return reader_newReader2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                reader_newReader = _newReader(options);
                this._cur._locale.exit();
            } finally {
            }
        }
        return reader_newReader;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Node newDomNode(XmlOptions options) {
        Node node_newDomNode;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                Node node_newDomNode2 = _newDomNode(options);
                this._cur._locale.exit();
                return node_newDomNode2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                node_newDomNode = _newDomNode(options);
                this._cur._locale.exit();
            } finally {
            }
        }
        return node_newDomNode;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(ContentHandler ch2, LexicalHandler lh, XmlOptions options) throws SAXException {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _save(ch2, lh, options);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _save(ch2, lh, options);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(File file, XmlOptions options) throws IOException {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _save(file, options);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _save(file, options);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(OutputStream os, XmlOptions options) throws IOException {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _save(os, options);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _save(os, options);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(Writer w, XmlOptions options) throws IOException {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _save(w, options);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _save(w, options);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void push() {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _push();
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _push();
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean pop() {
        boolean z_pop;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_pop2 = _pop();
                this._cur._locale.exit();
                return z_pop2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_pop = _pop();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_pop;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void selectPath(String path) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _selectPath(path);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _selectPath(path);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void selectPath(String path, XmlOptions options) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _selectPath(path, options);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _selectPath(path, options);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean hasNextSelection() {
        boolean z_hasNextSelection;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_hasNextSelection2 = _hasNextSelection();
                this._cur._locale.exit();
                return z_hasNextSelection2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_hasNextSelection = _hasNextSelection();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_hasNextSelection;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toNextSelection() {
        boolean z_toNextSelection;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toNextSelection2 = _toNextSelection();
                this._cur._locale.exit();
                return z_toNextSelection2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toNextSelection = _toNextSelection();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toNextSelection;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toSelection(int i) {
        boolean z_toSelection;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toSelection2 = _toSelection(i);
                this._cur._locale.exit();
                return z_toSelection2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toSelection = _toSelection(i);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toSelection;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public int getSelectionCount() {
        int i_getSelectionCount;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                int i_getSelectionCount2 = _getSelectionCount();
                this._cur._locale.exit();
                return i_getSelectionCount2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                i_getSelectionCount = _getSelectionCount();
                this._cur._locale.exit();
            } finally {
            }
        }
        return i_getSelectionCount;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void addToSelection() {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _addToSelection();
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _addToSelection();
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void clearSelections() {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _clearSelections();
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _clearSelections();
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toBookmark(XmlCursor.XmlBookmark bookmark) {
        boolean z_toBookmark;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toBookmark2 = _toBookmark(bookmark);
                this._cur._locale.exit();
                return z_toBookmark2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toBookmark = _toBookmark(bookmark);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toBookmark;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor.XmlBookmark toNextBookmark(Object key) {
        XmlCursor.XmlBookmark xmlBookmark_toNextBookmark;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor.XmlBookmark xmlBookmark_toNextBookmark2 = _toNextBookmark(key);
                this._cur._locale.exit();
                return xmlBookmark_toNextBookmark2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xmlBookmark_toNextBookmark = _toNextBookmark(key);
                this._cur._locale.exit();
            } finally {
            }
        }
        return xmlBookmark_toNextBookmark;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor.XmlBookmark toPrevBookmark(Object key) {
        XmlCursor.XmlBookmark xmlBookmark_toPrevBookmark;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor.XmlBookmark xmlBookmark_toPrevBookmark2 = _toPrevBookmark(key);
                this._cur._locale.exit();
                return xmlBookmark_toPrevBookmark2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xmlBookmark_toPrevBookmark = _toPrevBookmark(key);
                this._cur._locale.exit();
            } finally {
            }
        }
        return xmlBookmark_toPrevBookmark;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public QName getName() {
        QName qName_getName;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                QName qName_getName2 = _getName();
                this._cur._locale.exit();
                return qName_getName2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                qName_getName = _getName();
                this._cur._locale.exit();
            } finally {
            }
        }
        return qName_getName;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void setName(QName name) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _setName(name);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _setName(name);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public String namespaceForPrefix(String prefix) {
        String str_namespaceForPrefix;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                String str_namespaceForPrefix2 = _namespaceForPrefix(prefix);
                this._cur._locale.exit();
                return str_namespaceForPrefix2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                str_namespaceForPrefix = _namespaceForPrefix(prefix);
                this._cur._locale.exit();
            } finally {
            }
        }
        return str_namespaceForPrefix;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public String prefixForNamespace(String namespaceURI) {
        String str_prefixForNamespace;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                String str_prefixForNamespace2 = _prefixForNamespace(namespaceURI);
                this._cur._locale.exit();
                return str_prefixForNamespace2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                str_prefixForNamespace = _prefixForNamespace(namespaceURI);
                this._cur._locale.exit();
            } finally {
            }
        }
        return str_prefixForNamespace;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void getAllNamespaces(Map addToThis) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _getAllNamespaces(addToThis);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _getAllNamespaces(addToThis);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlObject getObject() {
        XmlObject xmlObject_getObject;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlObject xmlObject_getObject2 = _getObject();
                this._cur._locale.exit();
                return xmlObject_getObject2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xmlObject_getObject = _getObject();
                this._cur._locale.exit();
            } finally {
            }
        }
        return xmlObject_getObject;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor.TokenType currentTokenType() {
        XmlCursor.TokenType tokenType_currentTokenType;
        if (preCheck()) {
            return _currentTokenType();
        }
        synchronized (this._cur._locale) {
            tokenType_currentTokenType = _currentTokenType();
        }
        return tokenType_currentTokenType;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isStartdoc() {
        boolean z_isStartdoc;
        if (preCheck()) {
            return _isStartdoc();
        }
        synchronized (this._cur._locale) {
            z_isStartdoc = _isStartdoc();
        }
        return z_isStartdoc;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isEnddoc() {
        boolean z_isEnddoc;
        if (preCheck()) {
            return _isEnddoc();
        }
        synchronized (this._cur._locale) {
            z_isEnddoc = _isEnddoc();
        }
        return z_isEnddoc;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isStart() {
        boolean z_isStart;
        if (preCheck()) {
            return _isStart();
        }
        synchronized (this._cur._locale) {
            z_isStart = _isStart();
        }
        return z_isStart;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isEnd() {
        boolean z_isEnd;
        if (preCheck()) {
            return _isEnd();
        }
        synchronized (this._cur._locale) {
            z_isEnd = _isEnd();
        }
        return z_isEnd;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isText() {
        boolean z_isText;
        if (preCheck()) {
            return _isText();
        }
        synchronized (this._cur._locale) {
            z_isText = _isText();
        }
        return z_isText;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isAttr() {
        boolean z_isAttr;
        if (preCheck()) {
            return _isAttr();
        }
        synchronized (this._cur._locale) {
            z_isAttr = _isAttr();
        }
        return z_isAttr;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isNamespace() {
        boolean z_isNamespace;
        if (preCheck()) {
            return _isNamespace();
        }
        synchronized (this._cur._locale) {
            z_isNamespace = _isNamespace();
        }
        return z_isNamespace;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isComment() {
        boolean z_isComment;
        if (preCheck()) {
            return _isComment();
        }
        synchronized (this._cur._locale) {
            z_isComment = _isComment();
        }
        return z_isComment;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isProcinst() {
        boolean z_isProcinst;
        if (preCheck()) {
            return _isProcinst();
        }
        synchronized (this._cur._locale) {
            z_isProcinst = _isProcinst();
        }
        return z_isProcinst;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isContainer() {
        boolean z_isContainer;
        if (preCheck()) {
            return _isContainer();
        }
        synchronized (this._cur._locale) {
            z_isContainer = _isContainer();
        }
        return z_isContainer;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isFinish() {
        boolean z_isFinish;
        if (preCheck()) {
            return _isFinish();
        }
        synchronized (this._cur._locale) {
            z_isFinish = _isFinish();
        }
        return z_isFinish;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean isAnyAttr() {
        boolean z_isAnyAttr;
        if (preCheck()) {
            return _isAnyAttr();
        }
        synchronized (this._cur._locale) {
            z_isAnyAttr = _isAnyAttr();
        }
        return z_isAnyAttr;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor.TokenType prevTokenType() {
        XmlCursor.TokenType tokenType_prevTokenType;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor.TokenType tokenType_prevTokenType2 = _prevTokenType();
                this._cur._locale.exit();
                return tokenType_prevTokenType2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                tokenType_prevTokenType = _prevTokenType();
                this._cur._locale.exit();
            } finally {
            }
        }
        return tokenType_prevTokenType;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean hasNextToken() {
        boolean z_hasNextToken;
        if (preCheck()) {
            return _hasNextToken();
        }
        synchronized (this._cur._locale) {
            z_hasNextToken = _hasNextToken();
        }
        return z_hasNextToken;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean hasPrevToken() {
        boolean z_hasPrevToken;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_hasPrevToken2 = _hasPrevToken();
                this._cur._locale.exit();
                return z_hasPrevToken2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_hasPrevToken = _hasPrevToken();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_hasPrevToken;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor.TokenType toNextToken() {
        XmlCursor.TokenType tokenType_toNextToken;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor.TokenType tokenType_toNextToken2 = _toNextToken();
                this._cur._locale.exit();
                return tokenType_toNextToken2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                tokenType_toNextToken = _toNextToken();
                this._cur._locale.exit();
            } finally {
            }
        }
        return tokenType_toNextToken;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor.TokenType toPrevToken() {
        XmlCursor.TokenType tokenType_toPrevToken;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor.TokenType tokenType_toPrevToken2 = _toPrevToken();
                this._cur._locale.exit();
                return tokenType_toPrevToken2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                tokenType_toPrevToken = _toPrevToken();
                this._cur._locale.exit();
            } finally {
            }
        }
        return tokenType_toPrevToken;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor.TokenType toFirstContentToken() {
        XmlCursor.TokenType tokenType_toFirstContentToken;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor.TokenType tokenType_toFirstContentToken2 = _toFirstContentToken();
                this._cur._locale.exit();
                return tokenType_toFirstContentToken2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                tokenType_toFirstContentToken = _toFirstContentToken();
                this._cur._locale.exit();
            } finally {
            }
        }
        return tokenType_toFirstContentToken;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor.TokenType toEndToken() {
        XmlCursor.TokenType tokenType_toEndToken;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor.TokenType tokenType_toEndToken2 = _toEndToken();
                this._cur._locale.exit();
                return tokenType_toEndToken2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                tokenType_toEndToken = _toEndToken();
                this._cur._locale.exit();
            } finally {
            }
        }
        return tokenType_toEndToken;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public int toNextChar(int cch) {
        int i_toNextChar;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                int i_toNextChar2 = _toNextChar(cch);
                this._cur._locale.exit();
                return i_toNextChar2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                i_toNextChar = _toNextChar(cch);
                this._cur._locale.exit();
            } finally {
            }
        }
        return i_toNextChar;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public int toPrevChar(int cch) {
        int i_toPrevChar;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                int i_toPrevChar2 = _toPrevChar(cch);
                this._cur._locale.exit();
                return i_toPrevChar2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                i_toPrevChar = _toPrevChar(cch);
                this._cur._locale.exit();
            } finally {
            }
        }
        return i_toPrevChar;
    }

    public boolean ___toNextSibling() {
        if (!this._cur.hasParent()) {
            return false;
        }
        Xobj parent = this._cur.getParentNoRoot();
        if (parent == null) {
            this._cur._locale.enter();
            try {
                parent = this._cur.getParent();
                this._cur._locale.exit();
            } catch (Throwable th) {
                this._cur._locale.exit();
                throw th;
            }
        }
        return Locale.toNextSiblingElement(this._cur, parent);
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toNextSibling() {
        boolean z___toNextSibling;
        if (preCheck()) {
            return ___toNextSibling();
        }
        synchronized (this._cur._locale) {
            z___toNextSibling = ___toNextSibling();
        }
        return z___toNextSibling;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toPrevSibling() {
        boolean z_toPrevSibling;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toPrevSibling2 = _toPrevSibling();
                this._cur._locale.exit();
                return z_toPrevSibling2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toPrevSibling = _toPrevSibling();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toPrevSibling;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toParent() {
        boolean z_toParent;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toParent2 = _toParent();
                this._cur._locale.exit();
                return z_toParent2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toParent = _toParent();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toParent;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toFirstChild() {
        boolean z_toFirstChild;
        if (preCheck()) {
            return _toFirstChild();
        }
        synchronized (this._cur._locale) {
            z_toFirstChild = _toFirstChild();
        }
        return z_toFirstChild;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toLastChild() {
        boolean z_toLastChild;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toLastChild2 = _toLastChild();
                this._cur._locale.exit();
                return z_toLastChild2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toLastChild = _toLastChild();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toLastChild;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toChild(String name) {
        boolean z_toChild;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toChild2 = _toChild(name);
                this._cur._locale.exit();
                return z_toChild2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toChild = _toChild(name);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toChild;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toChild(String namespace, String name) {
        boolean z_toChild;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toChild2 = _toChild(namespace, name);
                this._cur._locale.exit();
                return z_toChild2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toChild = _toChild(namespace, name);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toChild;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toChild(QName name) {
        boolean z_toChild;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toChild2 = _toChild(name);
                this._cur._locale.exit();
                return z_toChild2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toChild = _toChild(name);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toChild;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toChild(int index) {
        boolean z_toChild;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toChild2 = _toChild(index);
                this._cur._locale.exit();
                return z_toChild2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toChild = _toChild(index);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toChild;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toChild(QName name, int index) {
        boolean z_toChild;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toChild2 = _toChild(name, index);
                this._cur._locale.exit();
                return z_toChild2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toChild = _toChild(name, index);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toChild;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toNextSibling(String name) {
        boolean z_toNextSibling;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toNextSibling2 = _toNextSibling(name);
                this._cur._locale.exit();
                return z_toNextSibling2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toNextSibling = _toNextSibling(name);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toNextSibling;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toNextSibling(String namespace, String name) {
        boolean z_toNextSibling;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toNextSibling2 = _toNextSibling(namespace, name);
                this._cur._locale.exit();
                return z_toNextSibling2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toNextSibling = _toNextSibling(namespace, name);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toNextSibling;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toNextSibling(QName name) {
        boolean z_toNextSibling;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toNextSibling2 = _toNextSibling(name);
                this._cur._locale.exit();
                return z_toNextSibling2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toNextSibling = _toNextSibling(name);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toNextSibling;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toFirstAttribute() {
        boolean z_toFirstAttribute;
        if (preCheck()) {
            return _toFirstAttribute();
        }
        synchronized (this._cur._locale) {
            z_toFirstAttribute = _toFirstAttribute();
        }
        return z_toFirstAttribute;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toLastAttribute() {
        boolean z_toLastAttribute;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toLastAttribute2 = _toLastAttribute();
                this._cur._locale.exit();
                return z_toLastAttribute2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toLastAttribute = _toLastAttribute();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toLastAttribute;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toNextAttribute() {
        boolean z_toNextAttribute;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toNextAttribute2 = _toNextAttribute();
                this._cur._locale.exit();
                return z_toNextAttribute2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toNextAttribute = _toNextAttribute();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toNextAttribute;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean toPrevAttribute() {
        boolean z_toPrevAttribute;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_toPrevAttribute2 = _toPrevAttribute();
                this._cur._locale.exit();
                return z_toPrevAttribute2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_toPrevAttribute = _toPrevAttribute();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_toPrevAttribute;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public String getAttributeText(QName attrName) {
        String str_getAttributeText;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                String str_getAttributeText2 = _getAttributeText(attrName);
                this._cur._locale.exit();
                return str_getAttributeText2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                str_getAttributeText = _getAttributeText(attrName);
                this._cur._locale.exit();
            } finally {
            }
        }
        return str_getAttributeText;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean setAttributeText(QName attrName, String value) {
        boolean z_setAttributeText;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_setAttributeText2 = _setAttributeText(attrName, value);
                this._cur._locale.exit();
                return z_setAttributeText2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_setAttributeText = _setAttributeText(attrName, value);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_setAttributeText;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean removeAttribute(QName attrName) {
        boolean z_removeAttribute;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_removeAttribute2 = _removeAttribute(attrName);
                this._cur._locale.exit();
                return z_removeAttribute2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_removeAttribute = _removeAttribute(attrName);
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_removeAttribute;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public String getTextValue() {
        String str_getTextValue;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                String str_getTextValue2 = _getTextValue();
                this._cur._locale.exit();
                return str_getTextValue2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                str_getTextValue = _getTextValue();
                this._cur._locale.exit();
            } finally {
            }
        }
        return str_getTextValue;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public int getTextValue(char[] chars, int offset, int cch) {
        int i_getTextValue;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                int i_getTextValue2 = _getTextValue(chars, offset, cch);
                this._cur._locale.exit();
                return i_getTextValue2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                i_getTextValue = _getTextValue(chars, offset, cch);
                this._cur._locale.exit();
            } finally {
            }
        }
        return i_getTextValue;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void setTextValue(String text) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _setTextValue(text);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _setTextValue(text);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void setTextValue(char[] sourceChars, int offset, int length) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _setTextValue(sourceChars, offset, length);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _setTextValue(sourceChars, offset, length);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public String getChars() {
        String str_getChars;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                String str_getChars2 = _getChars();
                this._cur._locale.exit();
                return str_getChars2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                str_getChars = _getChars();
                this._cur._locale.exit();
            } finally {
            }
        }
        return str_getChars;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public int getChars(char[] chars, int offset, int cch) {
        int i_getChars;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                int i_getChars2 = _getChars(chars, offset, cch);
                this._cur._locale.exit();
                return i_getChars2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                i_getChars = _getChars(chars, offset, cch);
                this._cur._locale.exit();
            } finally {
            }
        }
        return i_getChars;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void toStartDoc() {
        if (preCheck()) {
            _toStartDoc();
            return;
        }
        synchronized (this._cur._locale) {
            _toStartDoc();
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void toEndDoc() {
        if (preCheck()) {
            _toEndDoc();
            return;
        }
        synchronized (this._cur._locale) {
            _toEndDoc();
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor execQuery(String query) {
        XmlCursor xmlCursor_execQuery;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor xmlCursor_execQuery2 = _execQuery(query);
                this._cur._locale.exit();
                return xmlCursor_execQuery2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xmlCursor_execQuery = _execQuery(query);
                this._cur._locale.exit();
            } finally {
            }
        }
        return xmlCursor_execQuery;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor execQuery(String query, XmlOptions options) {
        XmlCursor xmlCursor_execQuery;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor xmlCursor_execQuery2 = _execQuery(query, options);
                this._cur._locale.exit();
                return xmlCursor_execQuery2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xmlCursor_execQuery = _execQuery(query, options);
                this._cur._locale.exit();
            } finally {
            }
        }
        return xmlCursor_execQuery;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor.ChangeStamp getDocChangeStamp() {
        XmlCursor.ChangeStamp changeStamp_getDocChangeStamp;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor.ChangeStamp changeStamp_getDocChangeStamp2 = _getDocChangeStamp();
                this._cur._locale.exit();
                return changeStamp_getDocChangeStamp2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                changeStamp_getDocChangeStamp = _getDocChangeStamp();
                this._cur._locale.exit();
            } finally {
            }
        }
        return changeStamp_getDocChangeStamp;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void setBookmark(XmlCursor.XmlBookmark bookmark) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _setBookmark(bookmark);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _setBookmark(bookmark);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public XmlCursor.XmlBookmark getBookmark(Object key) {
        XmlCursor.XmlBookmark xmlBookmark_getBookmark;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                XmlCursor.XmlBookmark xmlBookmark_getBookmark2 = _getBookmark(key);
                this._cur._locale.exit();
                return xmlBookmark_getBookmark2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                xmlBookmark_getBookmark = _getBookmark(key);
                this._cur._locale.exit();
            } finally {
            }
        }
        return xmlBookmark_getBookmark;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void clearBookmark(Object key) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _clearBookmark(key);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _clearBookmark(key);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void getAllBookmarkRefs(Collection listToFill) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _getAllBookmarkRefs(listToFill);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _getAllBookmarkRefs(listToFill);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean removeXml() {
        boolean z_removeXml;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_removeXml2 = _removeXml();
                this._cur._locale.exit();
                return z_removeXml2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_removeXml = _removeXml();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_removeXml;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public boolean removeXmlContents() {
        boolean z_removeXmlContents;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                boolean z_removeXmlContents2 = _removeXmlContents();
                this._cur._locale.exit();
                return z_removeXmlContents2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                z_removeXmlContents = _removeXmlContents();
                this._cur._locale.exit();
            } finally {
            }
        }
        return z_removeXmlContents;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public int removeChars(int cch) {
        int i_removeChars;
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                int i_removeChars2 = _removeChars(cch);
                this._cur._locale.exit();
                return i_removeChars2;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                i_removeChars = _removeChars(cch);
                this._cur._locale.exit();
            } finally {
            }
        }
        return i_removeChars;
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertChars(String text) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertChars(text);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertChars(text);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertElement(QName name) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertElement(name);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertElement(name);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertElement(String localName) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertElement(localName);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertElement(localName);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertElement(String localName, String uri) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertElement(localName, uri);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertElement(localName, uri);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void beginElement(QName name) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _beginElement(name);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _beginElement(name);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void beginElement(String localName) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _beginElement(localName);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _beginElement(localName);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void beginElement(String localName, String uri) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _beginElement(localName, uri);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _beginElement(localName, uri);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertElementWithText(QName name, String text) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertElementWithText(name, text);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertElementWithText(name, text);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertElementWithText(String localName, String text) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertElementWithText(localName, text);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertElementWithText(localName, text);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertElementWithText(String localName, String uri, String text) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertElementWithText(localName, uri, text);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertElementWithText(localName, uri, text);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertAttribute(String localName) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertAttribute(localName);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertAttribute(localName);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertAttribute(String localName, String uri) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertAttribute(localName, uri);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertAttribute(localName, uri);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertAttribute(QName name) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertAttribute(name);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertAttribute(name);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertAttributeWithValue(String Name, String value) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertAttributeWithValue(Name, value);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertAttributeWithValue(Name, value);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertAttributeWithValue(String name, String uri, String value) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertAttributeWithValue(name, uri, value);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertAttributeWithValue(name, uri, value);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertAttributeWithValue(QName name, String value) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertAttributeWithValue(name, value);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertAttributeWithValue(name, value);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertNamespace(String prefix, String namespace) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertNamespace(prefix, namespace);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertNamespace(prefix, namespace);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertComment(String text) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertComment(text);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertComment(text);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlCursor
    public void insertProcInst(String target, String text) {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _insertProcInst(target, text);
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _insertProcInst(target, text);
                this._cur._locale.exit();
            } finally {
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void dump() {
        if (preCheck()) {
            this._cur._locale.enter();
            try {
                _dump();
                this._cur._locale.exit();
                return;
            } finally {
            }
        }
        synchronized (this._cur._locale) {
            this._cur._locale.enter();
            try {
                _dump();
                this._cur._locale.exit();
            } finally {
            }
        }
    }
}
