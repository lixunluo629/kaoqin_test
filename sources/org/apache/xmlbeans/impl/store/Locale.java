package org.apache.xmlbeans.impl.store;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.CDataBookmark;
import org.apache.xmlbeans.QNameCache;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlLineNumber;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlOptionsBean;
import org.apache.xmlbeans.XmlRuntimeException;
import org.apache.xmlbeans.XmlSaxHandler;
import org.apache.xmlbeans.XmlTokenSource;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ResolverUtil;
import org.apache.xmlbeans.impl.common.SAXHelper;
import org.apache.xmlbeans.impl.common.Sax2Dom;
import org.apache.xmlbeans.impl.common.XmlLocale;
import org.apache.xmlbeans.impl.store.Cur;
import org.apache.xmlbeans.impl.store.DomImpl;
import org.apache.xmlbeans.impl.store.Saaj;
import org.apache.xmlbeans.xml.stream.Location;
import org.apache.xmlbeans.xml.stream.XMLEvent;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale.class */
public final class Locale implements DOMImplementation, Saaj.SaajCallback, XmlLocale {
    static final int ROOT = 1;
    static final int ELEM = 2;
    static final int ATTR = 3;
    static final int COMMENT = 4;
    static final int PROCINST = 5;
    static final int TEXT = 0;
    static final int WS_UNSPECIFIED = 0;
    static final int WS_PRESERVE = 1;
    static final int WS_REPLACE = 2;
    static final int WS_COLLAPSE = 3;
    static final String _xsi = "http://www.w3.org/2001/XMLSchema-instance";
    static final String _schema = "http://www.w3.org/2001/XMLSchema";
    static final String _openFragUri = "http://www.openuri.org/fragment";
    static final String _xml1998Uri = "http://www.w3.org/XML/1998/namespace";
    static final String _xmlnsUri = "http://www.w3.org/2000/xmlns/";
    static final QName _xsiNil;
    static final QName _xsiType;
    static final QName _xsiLoc;
    static final QName _xsiNoLoc;
    static final QName _openuriFragment;
    static final QName _xmlFragment;
    public static final String USE_SAME_LOCALE = "USE_SAME_LOCALE";
    public static final String COPY_USE_NEW_LOCALE = "COPY_USE_NEW_LOCALE";
    private static ThreadLocal tl_scrubBuffer;
    boolean _noSync;
    SchemaTypeLoader _schemaTypeLoader;
    private ReferenceQueue _refQueue;
    private int _entryCount;
    int _numTempFramesLeft;
    Cur[] _tempFrames;
    Cur _curPool;
    int _curPoolCount;
    Cur _registered;
    ChangeListener _changeListeners;
    long _versionAll;
    long _versionSansText;
    Cur.Locations _locations;
    private CharUtil _charUtil;
    int _offSrc;
    int _cchSrc;
    Saaj _saaj;
    DomImpl.Dom _ownerDoc;
    QNameFactory _qnameFactory;
    boolean _validateOnSet;
    int _posTemp;
    nthCache _nthCache_A = new nthCache(this);
    nthCache _nthCache_B = new nthCache(this);
    domNthCache _domNthCache_A = new domNthCache();
    domNthCache _domNthCache_B = new domNthCache();
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$ChangeListener.class */
    interface ChangeListener {
        void notifyChange();

        void setNextChangeListener(ChangeListener changeListener);

        ChangeListener getNextChangeListener();
    }

    static {
        $assertionsDisabled = !Locale.class.desiredAssertionStatus();
        _xsiNil = new QName(_xsi, "nil", "xsi");
        _xsiType = new QName(_xsi, "type", "xsi");
        _xsiLoc = new QName(_xsi, "schemaLocation", "xsi");
        _xsiNoLoc = new QName(_xsi, "noNamespaceSchemaLocation", "xsi");
        _openuriFragment = new QName(_openFragUri, "fragment", "frag");
        _xmlFragment = new QName("xml-fragment");
        tl_scrubBuffer = new ThreadLocal() { // from class: org.apache.xmlbeans.impl.store.Locale.1
            @Override // java.lang.ThreadLocal
            protected Object initialValue() {
                return new SoftReference(new ScrubBuffer());
            }
        };
    }

    private Locale(SchemaTypeLoader stl, XmlOptions options) {
        XmlOptions options2 = XmlOptions.maskNull(options);
        this._noSync = options2.hasOption(XmlOptions.UNSYNCHRONIZED);
        this._numTempFramesLeft = 8;
        this._tempFrames = new Cur[8];
        this._qnameFactory = new DefaultQNameFactory();
        this._locations = new Cur.Locations(this);
        this._schemaTypeLoader = stl;
        this._validateOnSet = options2.hasOption(XmlOptions.VALIDATE_ON_SET);
        Object saajObj = options2.get(Saaj.SAAJ_IMPL);
        if (saajObj != null) {
            if (!(saajObj instanceof Saaj)) {
                throw new IllegalStateException("Saaj impl not correct type: " + saajObj);
            }
            this._saaj = (Saaj) saajObj;
            this._saaj.setCallback(this);
        }
    }

    static Locale getLocale(SchemaTypeLoader stl, XmlOptions options) {
        Locale l;
        if (stl == null) {
            stl = XmlBeans.getContextTypeLoader();
        }
        XmlOptions options2 = XmlOptions.maskNull(options);
        if (options2.hasOption(USE_SAME_LOCALE)) {
            Object source = options2.get(USE_SAME_LOCALE);
            if (source instanceof Locale) {
                l = (Locale) source;
            } else if (source instanceof XmlTokenSource) {
                l = (Locale) ((XmlTokenSource) source).monitor();
            } else {
                throw new IllegalArgumentException("Source locale not understood: " + source);
            }
            if (l._schemaTypeLoader != stl) {
                throw new IllegalArgumentException("Source locale does not support same schema type loader");
            }
            if (l._saaj != null && l._saaj != options2.get(Saaj.SAAJ_IMPL)) {
                throw new IllegalArgumentException("Source locale does not support same saaj");
            }
            if (l._validateOnSet && !options2.hasOption(XmlOptions.VALIDATE_ON_SET)) {
                throw new IllegalArgumentException("Source locale does not support same validate on set");
            }
        } else {
            l = new Locale(stl, options2);
        }
        return l;
    }

    static void associateSourceName(Cur c, XmlOptions options) {
        String sourceName = (String) XmlOptions.safeGet(options, XmlOptions.DOCUMENT_SOURCE_NAME);
        if (sourceName != null) {
            getDocProps(c, true).setSourceName(sourceName);
        }
    }

    static void autoTypeDocument(Cur c, SchemaType requestedType, XmlOptions options) throws XmlException {
        if (!$assertionsDisabled && !c.isRoot()) {
            throw new AssertionError();
        }
        SchemaType optionType = (SchemaType) XmlOptions.maskNull(options).get(XmlOptions.DOCUMENT_TYPE);
        if (optionType != null) {
            c.setType(optionType);
            return;
        }
        SchemaType type = null;
        if (requestedType == null || requestedType.getName() != null) {
            QName xsiTypeName = c.getXsiTypeName();
            SchemaType xsiSchemaType = xsiTypeName == null ? null : c._locale._schemaTypeLoader.findType(xsiTypeName);
            if (requestedType == null || requestedType.isAssignableFrom(xsiSchemaType)) {
                type = xsiSchemaType;
            }
        }
        if (type == null && (requestedType == null || requestedType.isDocumentType())) {
            if (!$assertionsDisabled && !c.isRoot()) {
                throw new AssertionError();
            }
            c.push();
            QName docElemName = (c.hasAttrs() || !toFirstChildElement(c) || toNextSiblingElement(c)) ? null : c.getName();
            c.pop();
            if (docElemName != null) {
                type = c._locale._schemaTypeLoader.findDocumentType(docElemName);
                if (type != null && requestedType != null) {
                    QName requesteddocElemNameName = requestedType.getDocumentElementName();
                    if (!requesteddocElemNameName.equals(docElemName) && !requestedType.isValidSubstitution(docElemName)) {
                        throw new XmlException("Element " + QNameHelper.pretty(docElemName) + " is not a valid " + QNameHelper.pretty(requesteddocElemNameName) + " document or a valid substitution.");
                    }
                }
            }
        }
        if (type == null && requestedType == null) {
            c.push();
            type = (!toFirstNormalAttr(c) || toNextNormalAttr(c)) ? null : c._locale._schemaTypeLoader.findAttributeType(c.getName());
            c.pop();
        }
        if (type == null) {
            type = requestedType;
        }
        if (type == null) {
            type = XmlBeans.NO_TYPE;
        }
        c.setType(type);
        if (requestedType != null) {
            if (type.isDocumentType()) {
                verifyDocumentType(c, type.getDocumentElementName());
            } else if (type.isAttributeType()) {
                verifyAttributeType(c, type.getAttributeTypeAttributeName());
            }
        }
    }

    private static boolean namespacesSame(QName n1, QName n2) {
        if (n1 == n2) {
            return true;
        }
        if (n1 == null || n2 == null) {
            return false;
        }
        if (n1.getNamespaceURI() == n2.getNamespaceURI()) {
            return true;
        }
        if (n1.getNamespaceURI() == null || n2.getNamespaceURI() == null) {
            return false;
        }
        return n1.getNamespaceURI().equals(n2.getNamespaceURI());
    }

    private static void addNamespace(StringBuffer sb, QName name) {
        if (name.getNamespaceURI() == null) {
            sb.append("<no namespace>");
            return;
        }
        sb.append(SymbolConstants.QUOTES_SYMBOL);
        sb.append(name.getNamespaceURI());
        sb.append(SymbolConstants.QUOTES_SYMBOL);
    }

    private static void verifyDocumentType(Cur c, QName docElemName) throws XmlException {
        if (!$assertionsDisabled && !c.isRoot()) {
            throw new AssertionError();
        }
        c.push();
        try {
            StringBuffer sb = null;
            if (!toFirstChildElement(c) || toNextSiblingElement(c)) {
                sb = new StringBuffer();
                sb.append("The document is not a ");
                sb.append(QNameHelper.pretty(docElemName));
                sb.append(c.isRoot() ? ": no document element" : ": multiple document elements");
            } else {
                QName name = c.getName();
                if (!name.equals(docElemName)) {
                    sb = new StringBuffer();
                    sb.append("The document is not a ");
                    sb.append(QNameHelper.pretty(docElemName));
                    if (docElemName.getLocalPart().equals(name.getLocalPart())) {
                        sb.append(": document element namespace mismatch ");
                        sb.append("expected ");
                        addNamespace(sb, docElemName);
                        sb.append(" got ");
                        addNamespace(sb, name);
                    } else if (namespacesSame(docElemName, name)) {
                        sb.append(": document element local name mismatch ");
                        sb.append("expected " + docElemName.getLocalPart());
                        sb.append(" got " + name.getLocalPart());
                    } else {
                        sb.append(": document element mismatch ");
                        sb.append("got ");
                        sb.append(QNameHelper.pretty(name));
                    }
                }
            }
            if (sb != null) {
                XmlError err = XmlError.forCursor(sb.toString(), new Cursor(c));
                throw new XmlException(err.toString(), (Throwable) null, err);
            }
        } finally {
            c.pop();
        }
    }

    private static void verifyAttributeType(Cur c, QName attrName) throws XmlException {
        if (!$assertionsDisabled && !c.isRoot()) {
            throw new AssertionError();
        }
        c.push();
        try {
            StringBuffer sb = null;
            if (!toFirstNormalAttr(c) || toNextNormalAttr(c)) {
                sb = new StringBuffer();
                sb.append("The document is not a ");
                sb.append(QNameHelper.pretty(attrName));
                sb.append(c.isRoot() ? ": no attributes" : ": multiple attributes");
            } else {
                QName name = c.getName();
                if (!name.equals(attrName)) {
                    sb = new StringBuffer();
                    sb.append("The document is not a ");
                    sb.append(QNameHelper.pretty(attrName));
                    if (attrName.getLocalPart().equals(name.getLocalPart())) {
                        sb.append(": attribute namespace mismatch ");
                        sb.append("expected ");
                        addNamespace(sb, attrName);
                        sb.append(" got ");
                        addNamespace(sb, name);
                    } else if (namespacesSame(attrName, name)) {
                        sb.append(": attribute local name mismatch ");
                        sb.append("expected " + attrName.getLocalPart());
                        sb.append(" got " + name.getLocalPart());
                    } else {
                        sb.append(": attribute element mismatch ");
                        sb.append("got ");
                        sb.append(QNameHelper.pretty(name));
                    }
                }
            }
            if (sb != null) {
                XmlError err = XmlError.forCursor(sb.toString(), new Cursor(c));
                throw new XmlException(err.toString(), (Throwable) null, err);
            }
        } finally {
            c.pop();
        }
    }

    static boolean isFragmentQName(QName name) {
        return name.equals(_openuriFragment) || name.equals(_xmlFragment);
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0085, code lost:
    
        r3.pop();
        r4.pop();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0090, code lost:
    
        if (r6 != false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0095, code lost:
    
        if (r5 == 1) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0098, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x009c, code lost:
    
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static boolean isFragment(org.apache.xmlbeans.impl.store.Cur r3, org.apache.xmlbeans.impl.store.Cur r4) {
        /*
            boolean r0 = org.apache.xmlbeans.impl.store.Locale.$assertionsDisabled
            if (r0 != 0) goto L15
            r0 = r4
            boolean r0 = r0.isAttr()
            if (r0 == 0) goto L15
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r1.<init>()
            throw r0
        L15:
            r0 = r3
            r0.push()
            r0 = r4
            r0.push()
            r0 = 0
            r5 = r0
            r0 = 0
            r6 = r0
        L21:
            r0 = r3
            r1 = r4
            boolean r0 = r0.isSamePos(r1)
            if (r0 != 0) goto L85
            r0 = r3
            int r0 = r0.kind()
            r7 = r0
            r0 = r7
            r1 = 3
            if (r0 != r1) goto L38
            goto L85
        L38:
            r0 = r7
            if (r0 != 0) goto L4d
            r0 = r3
            r1 = -1
            java.lang.String r0 = r0.getCharsAsString(r1)
            boolean r0 = isWhiteSpace(r0)
            if (r0 != 0) goto L4d
            r0 = 1
            r6 = r0
            goto L85
        L4d:
            r0 = r7
            r1 = 2
            if (r0 != r1) goto L60
            int r5 = r5 + 1
            r0 = r5
            r1 = 1
            if (r0 <= r1) goto L60
            r0 = 1
            r6 = r0
            goto L85
        L60:
            boolean r0 = org.apache.xmlbeans.impl.store.Locale.$assertionsDisabled
            if (r0 != 0) goto L74
            r0 = r7
            r1 = 3
            if (r0 != r1) goto L74
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r1.<init>()
            throw r0
        L74:
            r0 = r7
            if (r0 == 0) goto L7d
            r0 = r3
            r0.toEnd()
        L7d:
            r0 = r3
            boolean r0 = r0.next()
            goto L21
        L85:
            r0 = r3
            boolean r0 = r0.pop()
            r0 = r4
            boolean r0 = r0.pop()
            r0 = r6
            if (r0 != 0) goto L98
            r0 = r5
            r1 = 1
            if (r0 == r1) goto L9c
        L98:
            r0 = 1
            goto L9d
        L9c:
            r0 = 0
        L9d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Locale.isFragment(org.apache.xmlbeans.impl.store.Cur, org.apache.xmlbeans.impl.store.Cur):boolean");
    }

    public static XmlObject newInstance(SchemaTypeLoader stl, SchemaType type, XmlOptions options) {
        XmlObject xmlObjectNewInstance;
        Locale l = getLocale(stl, options);
        if (l.noSync()) {
            l.enter();
            try {
                XmlObject xmlObjectNewInstance2 = l.newInstance(type, options);
                l.exit();
                return xmlObjectNewInstance2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                xmlObjectNewInstance = l.newInstance(type, options);
                l.exit();
            } finally {
            }
        }
        return xmlObjectNewInstance;
    }

    private XmlObject newInstance(SchemaType type, XmlOptions options) {
        XmlOptions options2 = XmlOptions.maskNull(options);
        Cur c = tempCur();
        SchemaType sType = (SchemaType) options2.get(XmlOptions.DOCUMENT_TYPE);
        if (sType == null) {
            sType = type == null ? XmlObject.type : type;
        }
        if (sType.isDocumentType()) {
            c.createDomDocumentRoot();
        } else {
            c.createRoot();
        }
        c.setType(sType);
        XmlObject x = (XmlObject) c.getUser();
        c.release();
        return x;
    }

    public static DOMImplementation newDomImplementation(SchemaTypeLoader stl, XmlOptions options) {
        return getLocale(stl, options);
    }

    public static XmlObject parseToXmlObject(SchemaTypeLoader stl, String xmlText, SchemaType type, XmlOptions options) throws XmlException {
        XmlObject toXmlObject;
        Locale l = getLocale(stl, options);
        if (l.noSync()) {
            l.enter();
            try {
                XmlObject toXmlObject2 = l.parseToXmlObject(xmlText, type, options);
                l.exit();
                return toXmlObject2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                toXmlObject = l.parseToXmlObject(xmlText, type, options);
                l.exit();
            } finally {
            }
        }
        return toXmlObject;
    }

    private XmlObject parseToXmlObject(String xmlText, SchemaType type, XmlOptions options) throws XmlException, IOException {
        Cur c = parse(xmlText, type, options);
        XmlObject x = (XmlObject) c.getUser();
        c.release();
        return x;
    }

    Cur parse(String s, SchemaType type, XmlOptions options) throws XmlException, IOException {
        Reader r = new StringReader(s);
        try {
            try {
                Cur c = getSaxLoader(options).load(this, new InputSource(r), options);
                autoTypeDocument(c, type, options);
                return c;
            } catch (IOException e) {
                if ($assertionsDisabled) {
                    throw new XmlException(e.getMessage(), e);
                }
                throw new AssertionError("StringReader should not throw IOException");
            }
        } finally {
            try {
                r.close();
            } catch (IOException e2) {
            }
        }
    }

    public static XmlObject parseToXmlObject(SchemaTypeLoader stl, XMLInputStream xis, SchemaType type, XmlOptions options) throws XMLStreamException, XmlException {
        XmlObject toXmlObject;
        Locale l = getLocale(stl, options);
        if (l.noSync()) {
            l.enter();
            try {
                XmlObject toXmlObject2 = l.parseToXmlObject(xis, type, options);
                l.exit();
                return toXmlObject2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                toXmlObject = l.parseToXmlObject(xis, type, options);
                l.exit();
            } finally {
            }
        }
        return toXmlObject;
    }

    public XmlObject parseToXmlObject(XMLInputStream xis, SchemaType type, XmlOptions options) throws XMLStreamException, XmlException {
        try {
            Cur c = loadXMLInputStream(xis, options);
            autoTypeDocument(c, type, options);
            XmlObject x = (XmlObject) c.getUser();
            c.release();
            return x;
        } catch (XMLStreamException e) {
            throw new XmlException(e.getMessage(), e);
        }
    }

    public static XmlObject parseToXmlObject(SchemaTypeLoader stl, XMLStreamReader xsr, SchemaType type, XmlOptions options) throws XmlException {
        XmlObject toXmlObject;
        Locale l = getLocale(stl, options);
        if (l.noSync()) {
            l.enter();
            try {
                XmlObject toXmlObject2 = l.parseToXmlObject(xsr, type, options);
                l.exit();
                return toXmlObject2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                toXmlObject = l.parseToXmlObject(xsr, type, options);
                l.exit();
            } finally {
            }
        }
        return toXmlObject;
    }

    public XmlObject parseToXmlObject(XMLStreamReader xsr, SchemaType type, XmlOptions options) throws XmlException {
        try {
            Cur c = loadXMLStreamReader(xsr, options);
            autoTypeDocument(c, type, options);
            XmlObject x = (XmlObject) c.getUser();
            c.release();
            return x;
        } catch (javax.xml.stream.XMLStreamException e) {
            throw new XmlException(e.getMessage(), e);
        }
    }

    private static void lineNumber(XMLEvent xe, LoadContext context) {
        Location loc = xe.getLocation();
        if (loc != null) {
            context.lineNumber(loc.getLineNumber(), loc.getColumnNumber(), -1);
        }
    }

    private static void lineNumber(XMLStreamReader xsr, LoadContext context) {
        javax.xml.stream.Location loc = xsr.getLocation();
        if (loc != null) {
            context.lineNumber(loc.getLineNumber(), loc.getColumnNumber(), loc.getCharacterOffset());
        }
    }

    private void doAttributes(XMLStreamReader xsr, LoadContext context) {
        int n = xsr.getAttributeCount();
        for (int a = 0; a < n; a++) {
            context.attr(xsr.getAttributeLocalName(a), xsr.getAttributeNamespace(a), xsr.getAttributePrefix(a), xsr.getAttributeValue(a));
        }
    }

    private void doNamespaces(XMLStreamReader xsr, LoadContext context) {
        int n = xsr.getNamespaceCount();
        for (int a = 0; a < n; a++) {
            String prefix = xsr.getNamespacePrefix(a);
            if (prefix == null || prefix.length() == 0) {
                context.attr("xmlns", "http://www.w3.org/2000/xmlns/", null, xsr.getNamespaceURI(a));
            } else {
                context.attr(prefix, "http://www.w3.org/2000/xmlns/", "xmlns", xsr.getNamespaceURI(a));
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0236  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.apache.xmlbeans.impl.store.Cur loadXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream r6, org.apache.xmlbeans.XmlOptions r7) throws org.apache.xmlbeans.xml.stream.XMLStreamException {
        /*
            Method dump skipped, instructions count: 791
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Locale.loadXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream, org.apache.xmlbeans.XmlOptions):org.apache.xmlbeans.impl.store.Cur");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:35:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01a1 A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.apache.xmlbeans.impl.store.Cur loadXMLStreamReader(javax.xml.stream.XMLStreamReader r6, org.apache.xmlbeans.XmlOptions r7) throws javax.xml.stream.XMLStreamException {
        /*
            Method dump skipped, instructions count: 462
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Locale.loadXMLStreamReader(javax.xml.stream.XMLStreamReader, org.apache.xmlbeans.XmlOptions):org.apache.xmlbeans.impl.store.Cur");
    }

    public static XmlObject parseToXmlObject(SchemaTypeLoader stl, InputStream is, SchemaType type, XmlOptions options) throws XmlException, IOException {
        XmlObject toXmlObject;
        Locale l = getLocale(stl, options);
        if (l.noSync()) {
            l.enter();
            try {
                XmlObject toXmlObject2 = l.parseToXmlObject(is, type, options);
                l.exit();
                return toXmlObject2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                toXmlObject = l.parseToXmlObject(is, type, options);
                l.exit();
            } finally {
            }
        }
        return toXmlObject;
    }

    private XmlObject parseToXmlObject(InputStream is, SchemaType type, XmlOptions options) throws XmlException, SAXException, IOException {
        Cur c = getSaxLoader(options).load(this, new InputSource(is), options);
        autoTypeDocument(c, type, options);
        XmlObject x = (XmlObject) c.getUser();
        c.release();
        return x;
    }

    public static XmlObject parseToXmlObject(SchemaTypeLoader stl, Reader reader, SchemaType type, XmlOptions options) throws XmlException, IOException {
        XmlObject toXmlObject;
        Locale l = getLocale(stl, options);
        if (l.noSync()) {
            l.enter();
            try {
                XmlObject toXmlObject2 = l.parseToXmlObject(reader, type, options);
                l.exit();
                return toXmlObject2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                toXmlObject = l.parseToXmlObject(reader, type, options);
                l.exit();
            } finally {
            }
        }
        return toXmlObject;
    }

    private XmlObject parseToXmlObject(Reader reader, SchemaType type, XmlOptions options) throws XmlException, SAXException, IOException {
        Cur c = getSaxLoader(options).load(this, new InputSource(reader), options);
        autoTypeDocument(c, type, options);
        XmlObject x = (XmlObject) c.getUser();
        c.release();
        return x;
    }

    public static XmlObject parseToXmlObject(SchemaTypeLoader stl, Node node, SchemaType type, XmlOptions options) throws XmlException {
        XmlObject toXmlObject;
        Locale l = getLocale(stl, options);
        if (l.noSync()) {
            l.enter();
            try {
                XmlObject toXmlObject2 = l.parseToXmlObject(node, type, options);
                l.exit();
                return toXmlObject2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                toXmlObject = l.parseToXmlObject(node, type, options);
                l.exit();
            } finally {
            }
        }
        return toXmlObject;
    }

    public XmlObject parseToXmlObject(Node node, SchemaType type, XmlOptions options) throws XmlException, DOMException {
        LoadContext context = new Cur.CurLoadContext(this, options);
        loadNode(node, context);
        Cur c = context.finish();
        associateSourceName(c, options);
        autoTypeDocument(c, type, options);
        XmlObject x = (XmlObject) c.getUser();
        c.release();
        return x;
    }

    private void loadNodeChildren(Node n, LoadContext context) {
        Node firstChild = n.getFirstChild();
        while (true) {
            Node c = firstChild;
            if (c != null) {
                loadNode(c, context);
                firstChild = c.getNextSibling();
            } else {
                return;
            }
        }
    }

    void loadNode(Node n, LoadContext context) throws DOMException {
        switch (n.getNodeType()) {
            case 1:
                context.startElement(makeQualifiedQName(n.getNamespaceURI(), n.getNodeName()));
                NamedNodeMap attrs = n.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Node a = attrs.item(i);
                    String attrName = a.getNodeName();
                    String attrValue = a.getNodeValue();
                    if (attrName.toLowerCase().startsWith("xmlns")) {
                        if (attrName.length() == 5) {
                            context.xmlns(null, attrValue);
                        } else {
                            context.xmlns(attrName.substring(6), attrValue);
                        }
                    } else {
                        context.attr(makeQualifiedQName(a.getNamespaceURI(), attrName), attrValue);
                    }
                }
                loadNodeChildren(n, context);
                context.endElement();
                return;
            case 2:
                throw new RuntimeException("Unexpected node");
            case 3:
            case 4:
                context.text(n.getNodeValue());
                return;
            case 5:
            case 9:
            case 11:
                loadNodeChildren(n, context);
                return;
            case 6:
            case 10:
            case 12:
                Node next = n.getNextSibling();
                if (next != null) {
                    loadNode(next, context);
                    return;
                }
                return;
            case 7:
                context.procInst(n.getNodeName(), n.getNodeValue());
                return;
            case 8:
                context.comment(n.getNodeValue());
                return;
            default:
                return;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$XmlSaxHandlerImpl.class */
    private class XmlSaxHandlerImpl extends SaxHandler implements XmlSaxHandler {
        private SchemaType _type;
        private XmlOptions _options;

        XmlSaxHandlerImpl(Locale l, SchemaType type, XmlOptions options) {
            super(null);
            this._options = options;
            this._type = type;
            XmlOptions saxHandlerOptions = new XmlOptions(options);
            saxHandlerOptions.put("LOAD_USE_LOCALE_CHAR_UTIL");
            initSaxHandler(l, saxHandlerOptions);
        }

        @Override // org.apache.xmlbeans.XmlSaxHandler
        public ContentHandler getContentHandler() {
            if (this._context == null) {
                return null;
            }
            return this;
        }

        @Override // org.apache.xmlbeans.XmlSaxHandler
        public LexicalHandler getLexicalHandler() {
            if (this._context == null) {
                return null;
            }
            return this;
        }

        @Override // org.apache.xmlbeans.XmlSaxHandler
        public void bookmarkLastEvent(XmlCursor.XmlBookmark mark) {
            this._context.bookmarkLastNonAttr(mark);
        }

        @Override // org.apache.xmlbeans.XmlSaxHandler
        public void bookmarkLastAttr(QName attrName, XmlCursor.XmlBookmark mark) {
            this._context.bookmarkLastAttr(attrName, mark);
        }

        @Override // org.apache.xmlbeans.XmlSaxHandler
        public XmlObject getObject() throws XmlException {
            if (this._context == null) {
                return null;
            }
            this._locale.enter();
            try {
                Cur c = this._context.finish();
                Locale.autoTypeDocument(c, this._type, this._options);
                XmlObject x = (XmlObject) c.getUser();
                c.release();
                this._context = null;
                this._locale.exit();
                return x;
            } catch (Throwable th) {
                this._locale.exit();
                throw th;
            }
        }
    }

    public static XmlSaxHandler newSaxHandler(SchemaTypeLoader stl, SchemaType type, XmlOptions options) {
        XmlSaxHandler xmlSaxHandlerNewSaxHandler;
        Locale l = getLocale(stl, options);
        if (l.noSync()) {
            l.enter();
            try {
                XmlSaxHandler xmlSaxHandlerNewSaxHandler2 = l.newSaxHandler(type, options);
                l.exit();
                return xmlSaxHandlerNewSaxHandler2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                xmlSaxHandlerNewSaxHandler = l.newSaxHandler(type, options);
                l.exit();
            } finally {
            }
        }
        return xmlSaxHandlerNewSaxHandler;
    }

    public XmlSaxHandler newSaxHandler(SchemaType type, XmlOptions options) {
        return new XmlSaxHandlerImpl(this, type, options);
    }

    QName makeQName(String uri, String localPart) {
        if ($assertionsDisabled || (localPart != null && localPart.length() > 0)) {
            return this._qnameFactory.getQName(uri, localPart);
        }
        throw new AssertionError();
    }

    QName makeQNameNoCheck(String uri, String localPart) {
        return this._qnameFactory.getQName(uri, localPart);
    }

    QName makeQName(String uri, String local, String prefix) {
        return this._qnameFactory.getQName(uri, local, prefix == null ? "" : prefix);
    }

    QName makeQualifiedQName(String uri, String qname) {
        if (qname == null) {
            qname = "";
        }
        int i = qname.indexOf(58);
        return i < 0 ? this._qnameFactory.getQName(uri, qname) : this._qnameFactory.getQName(uri, qname.substring(i + 1), qname.substring(0, i));
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$DocProps.class */
    private static class DocProps extends XmlDocumentProperties {
        private HashMap _map;

        private DocProps() {
            this._map = new HashMap();
        }

        @Override // org.apache.xmlbeans.XmlDocumentProperties
        public Object put(Object key, Object value) {
            return this._map.put(key, value);
        }

        @Override // org.apache.xmlbeans.XmlDocumentProperties
        public Object get(Object key) {
            return this._map.get(key);
        }

        @Override // org.apache.xmlbeans.XmlDocumentProperties
        public Object remove(Object key) {
            return this._map.remove(key);
        }
    }

    static XmlDocumentProperties getDocProps(Cur c, boolean ensure) {
        c.push();
        while (c.toParent()) {
        }
        DocProps props = (DocProps) c.getBookmark(DocProps.class);
        if (props == null && ensure) {
            DocProps docProps = new DocProps();
            props = docProps;
            c.setBookmark(DocProps.class, docProps);
        }
        c.pop();
        return props;
    }

    void registerForChange(ChangeListener listener) {
        if (listener.getNextChangeListener() == null) {
            if (this._changeListeners == null) {
                listener.setNextChangeListener(listener);
            } else {
                listener.setNextChangeListener(this._changeListeners);
            }
            this._changeListeners = listener;
        }
    }

    void notifyChange() {
        while (this._changeListeners != null) {
            this._changeListeners.notifyChange();
            if (this._changeListeners.getNextChangeListener() == this._changeListeners) {
                this._changeListeners.setNextChangeListener(null);
            }
            ChangeListener next = this._changeListeners.getNextChangeListener();
            this._changeListeners.setNextChangeListener(null);
            this._changeListeners = next;
        }
        this._locations.notifyChange();
    }

    static String getTextValue(Cur c) {
        if (!$assertionsDisabled && !c.isNode()) {
            throw new AssertionError();
        }
        if (!c.hasChildren()) {
            return c.getValueAsString();
        }
        StringBuffer sb = new StringBuffer();
        c.push();
        c.next();
        while (!c.isAtEndOfLastPush()) {
            if (c.isText() && ((!c._xobj.isComment() && !c._xobj.isProcinst()) || c._pos >= c._xobj._cchValue)) {
                CharUtil.getString(sb, c.getChars(-1), c._offSrc, c._cchSrc);
            }
            c.next();
        }
        c.pop();
        return sb.toString();
    }

    static int getTextValue(Cur c, int wsr, char[] chars, int off, int maxCch) {
        if (!$assertionsDisabled && !c.isNode()) {
            throw new AssertionError();
        }
        String s = c._xobj.getValueAsString(wsr);
        int n = s.length();
        if (n > maxCch) {
            n = maxCch;
        }
        if (n <= 0) {
            return 0;
        }
        s.getChars(0, n, chars, off);
        return n;
    }

    static String applyWhiteSpaceRule(String s, int wsr) {
        int l = s == null ? 0 : s.length();
        if (l == 0 || wsr == 1) {
            return s;
        }
        if (wsr == 2) {
            for (int i = 0; i < l; i++) {
                char ch2 = s.charAt(i);
                if (ch2 == '\n' || ch2 == '\r' || ch2 == '\t') {
                    return processWhiteSpaceRule(s, wsr);
                }
            }
        } else if (wsr == 3) {
            if (CharUtil.isWhiteSpace(s.charAt(0)) || CharUtil.isWhiteSpace(s.charAt(l - 1))) {
                return processWhiteSpaceRule(s, wsr);
            }
            boolean lastWasWhite = false;
            for (int i2 = 1; i2 < l; i2++) {
                boolean isWhite = CharUtil.isWhiteSpace(s.charAt(i2));
                if (isWhite && lastWasWhite) {
                    return processWhiteSpaceRule(s, wsr);
                }
                lastWasWhite = isWhite;
            }
        }
        return s;
    }

    static String processWhiteSpaceRule(String s, int wsr) {
        ScrubBuffer sb = getScrubBuffer(wsr);
        sb.scrub(s, 0, s.length());
        return sb.getResultAsString();
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$ScrubBuffer.class */
    static final class ScrubBuffer {
        private static final int START_STATE = 0;
        private static final int SPACE_SEEN_STATE = 1;
        private static final int NOSPACE_STATE = 2;
        private int _state;
        private int _wsr;
        private char[] _srcBuf = new char[1024];
        private StringBuffer _sb = new StringBuffer();

        ScrubBuffer() {
        }

        void init(int wsr) {
            this._sb.delete(0, this._sb.length());
            this._wsr = wsr;
            this._state = 0;
        }

        void scrub(Object src, int off, int cch) {
            char[] chars;
            if (cch == 0) {
                return;
            }
            if (this._wsr == 1) {
                CharUtil.getString(this._sb, src, off, cch);
                return;
            }
            if (src instanceof char[]) {
                chars = (char[]) src;
            } else {
                if (cch <= this._srcBuf.length) {
                    chars = this._srcBuf;
                } else if (cch <= 16384) {
                    char[] cArr = new char[16384];
                    this._srcBuf = cArr;
                    chars = cArr;
                } else {
                    chars = new char[cch];
                }
                CharUtil.getChars(chars, 0, src, off, cch);
                off = 0;
            }
            int start = 0;
            for (int i = 0; i < cch; i++) {
                char ch2 = chars[off + i];
                if (ch2 == ' ' || ch2 == '\n' || ch2 == '\r' || ch2 == '\t') {
                    this._sb.append(chars, off + start, i - start);
                    start = i + 1;
                    if (this._wsr == 2) {
                        this._sb.append(' ');
                    } else if (this._state == 2) {
                        this._state = 1;
                    }
                } else {
                    if (this._state == 1) {
                        this._sb.append(' ');
                    }
                    this._state = 2;
                }
            }
            this._sb.append(chars, off + start, cch - start);
        }

        String getResultAsString() {
            return this._sb.toString();
        }
    }

    public static void clearThreadLocals() {
        tl_scrubBuffer.remove();
    }

    static ScrubBuffer getScrubBuffer(int wsr) {
        SoftReference softRef = (SoftReference) tl_scrubBuffer.get();
        ScrubBuffer scrubBuffer = (ScrubBuffer) softRef.get();
        if (scrubBuffer == null) {
            scrubBuffer = new ScrubBuffer();
            tl_scrubBuffer.set(new SoftReference(scrubBuffer));
        }
        scrubBuffer.init(wsr);
        return scrubBuffer;
    }

    static boolean pushToContainer(Cur c) {
        c.push();
        while (true) {
            switch (c.kind()) {
                case -2:
                case -1:
                    c.pop();
                    return false;
                case 0:
                case 3:
                default:
                    c.nextWithAttrs();
                    break;
                case 1:
                case 2:
                    return true;
                case 4:
                case 5:
                    c.skip();
                    break;
            }
        }
    }

    static boolean toFirstNormalAttr(Cur c) {
        c.push();
        if (c.toFirstAttr()) {
            while (c.isXmlns()) {
                if (!c.toNextAttr()) {
                }
            }
            c.popButStay();
            return true;
        }
        c.pop();
        return false;
    }

    static boolean toPrevNormalAttr(Cur c) {
        if (c.isAttr()) {
            c.push();
            do {
                if (!$assertionsDisabled && !c.isAttr()) {
                    throw new AssertionError();
                }
                if (c.prev()) {
                    c.prev();
                    if (!c.isAttr()) {
                        c.prev();
                    }
                } else {
                    c.pop();
                    return false;
                }
            } while (!c.isNormalAttr());
            c.popButStay();
            return true;
        }
        return false;
    }

    static boolean toNextNormalAttr(Cur c) {
        c.push();
        while (c.toNextAttr()) {
            if (!c.isXmlns()) {
                c.popButStay();
                return true;
            }
        }
        c.pop();
        return false;
    }

    Xobj findNthChildElem(Xobj parent, QName name, QNameSet set, int n) {
        if (!$assertionsDisabled && name != null && set != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && n < 0) {
            throw new AssertionError();
        }
        if (parent == null) {
            return null;
        }
        int da = this._nthCache_A.distance(parent, name, set, n);
        int db = this._nthCache_B.distance(parent, name, set, n);
        Xobj x = da <= db ? this._nthCache_A.fetch(parent, name, set, n) : this._nthCache_B.fetch(parent, name, set, n);
        if (da == db) {
            nthCache temp = this._nthCache_A;
            this._nthCache_A = this._nthCache_B;
            this._nthCache_B = temp;
        }
        return x;
    }

    int count(Xobj parent, QName name, QNameSet set) {
        int n = 0;
        Xobj xobjFindNthChildElem = findNthChildElem(parent, name, set, 0);
        while (true) {
            Xobj x = xobjFindNthChildElem;
            if (x != null) {
                if (x.isElem()) {
                    if (set == null) {
                        if (x._name.equals(name)) {
                            n++;
                        }
                    } else if (set.contains(x._name)) {
                        n++;
                    }
                }
                xobjFindNthChildElem = x._nextSibling;
            } else {
                return n;
            }
        }
    }

    static boolean toChild(Cur c, QName name, int n) {
        if (n >= 0 && pushToContainer(c)) {
            Xobj x = c._locale.findNthChildElem(c._xobj, name, null, n);
            c.pop();
            if (x != null) {
                c.moveTo(x);
                return true;
            }
            return false;
        }
        return false;
    }

    static boolean toFirstChildElement(Cur c) {
        Xobj originalXobj = c._xobj;
        int originalPos = c._pos;
        while (true) {
            switch (c.kind()) {
                case -2:
                case -1:
                    c.moveTo(originalXobj, originalPos);
                    return false;
                case 0:
                case 3:
                default:
                    c.nextWithAttrs();
                    break;
                case 1:
                case 2:
                    if (!c.toFirstChild() || (!c.isElem() && !toNextSiblingElement(c))) {
                        c.moveTo(originalXobj, originalPos);
                        return false;
                    }
                    return true;
                case 4:
                case 5:
                    c.skip();
                    break;
            }
        }
    }

    static boolean toLastChildElement(Cur c) {
        if (!pushToContainer(c)) {
            return false;
        }
        if (!c.toLastChild() || (!c.isElem() && !toPrevSiblingElement(c))) {
            c.pop();
            return false;
        }
        c.popButStay();
        return true;
    }

    static boolean toPrevSiblingElement(Cur cur) {
        int k;
        if (!cur.hasParent()) {
            return false;
        }
        Cur c = cur.tempCur();
        boolean moved = false;
        if (c.kind() != 3) {
            while (true) {
                if (!c.prev() || (k = c.kind()) == 1 || k == 2) {
                    break;
                }
                if (c.kind() == -2) {
                    c.toParent();
                    cur.moveToCur(c);
                    moved = true;
                    break;
                }
            }
        }
        c.release();
        return moved;
    }

    static boolean toNextSiblingElement(Cur c) {
        if (!c.hasParent()) {
            return false;
        }
        c.push();
        int k = c.kind();
        if (k == 3) {
            c.toParent();
            c.next();
        } else if (k == 2) {
            c.skip();
        }
        while (true) {
            int k2 = c.kind();
            if (k2 >= 0) {
                if (k2 == 2) {
                    c.popButStay();
                    return true;
                }
                if (k2 > 0) {
                    c.toEnd();
                }
                c.next();
            } else {
                c.pop();
                return false;
            }
        }
    }

    static boolean toNextSiblingElement(Cur c, Xobj parent) {
        Xobj originalXobj = c._xobj;
        int originalPos = c._pos;
        int k = c.kind();
        if (k == 3) {
            c.moveTo(parent);
            c.next();
        } else if (k == 2) {
            c.skip();
        }
        while (true) {
            int k2 = c.kind();
            if (k2 >= 0) {
                if (k2 == 2) {
                    return true;
                }
                if (k2 > 0) {
                    c.toEnd();
                }
                c.next();
            } else {
                c.moveTo(originalXobj, originalPos);
                return false;
            }
        }
    }

    static void applyNamespaces(Cur c, Map namespaces) {
        if (!$assertionsDisabled && !c.isContainer()) {
            throw new AssertionError();
        }
        for (String prefix : namespaces.keySet()) {
            if (!prefix.toLowerCase().startsWith("xml") && c.namespaceForPrefix(prefix, false) == null) {
                c.push();
                c.next();
                c.createAttr(c._locale.createXmlns(prefix));
                c.next();
                c.insertString((String) namespaces.get(prefix));
                c.pop();
            }
        }
    }

    static Map getAllNamespaces(Cur c, Map filleMe) {
        if (!$assertionsDisabled && !c.isNode()) {
            throw new AssertionError();
        }
        c.push();
        if (!c.isContainer()) {
            c.toParent();
        }
        if (!$assertionsDisabled && !c.isContainer()) {
            throw new AssertionError();
        }
        do {
            c.getName();
            while (c.toNextAttr()) {
                if (c.isXmlns()) {
                    String prefix = c.getXmlnsPrefix();
                    String uri = c.getXmlnsUri();
                    if (filleMe == null) {
                        filleMe = new HashMap();
                    }
                    if (!filleMe.containsKey(prefix)) {
                        filleMe.put(prefix, uri);
                    }
                }
            }
            if (!c.isContainer()) {
                c.toParentRaw();
            }
        } while (c.toParentRaw());
        c.pop();
        return filleMe;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$nthCache.class */
    class nthCache {
        private long _version;
        private Xobj _parent;
        private QName _name;
        private QNameSet _set;
        private Xobj _child;
        private int _n;
        static final /* synthetic */ boolean $assertionsDisabled;
        final /* synthetic */ Locale this$0;

        nthCache(Locale locale) {
            this.this$0 = locale;
        }

        static {
            $assertionsDisabled = !Locale.class.desiredAssertionStatus();
        }

        private boolean namesSame(QName pattern, QName name) {
            return pattern == null || pattern.equals(name);
        }

        private boolean setsSame(QNameSet patternSet, QNameSet set) {
            return patternSet != null && patternSet == set;
        }

        private boolean nameHit(QName namePattern, QNameSet setPattern, QName name) {
            return setPattern == null ? namesSame(namePattern, name) : setPattern.contains(name);
        }

        private boolean cacheSame(QName namePattern, QNameSet setPattern) {
            return setPattern == null ? namesSame(namePattern, this._name) : setsSame(setPattern, this._set);
        }

        int distance(Xobj parent, QName name, QNameSet set, int n) {
            if (!$assertionsDisabled && n < 0) {
                throw new AssertionError();
            }
            if (this._version != this.this$0.version()) {
                return 2147483646;
            }
            if (parent == this._parent && cacheSame(name, set)) {
                return n > this._n ? n - this._n : this._n - n;
            }
            return Integer.MAX_VALUE;
        }

        /* JADX WARN: Code restructure failed: missing block: B:42:0x00d1, code lost:
        
            r5._child = r10;
            r5._n++;
         */
        /* JADX WARN: Code restructure failed: missing block: B:58:0x0129, code lost:
        
            r5._child = r10;
            r5._n--;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        org.apache.xmlbeans.impl.store.Xobj fetch(org.apache.xmlbeans.impl.store.Xobj r6, javax.xml.namespace.QName r7, org.apache.xmlbeans.QNameSet r8, int r9) {
            /*
                Method dump skipped, instructions count: 334
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Locale.nthCache.fetch(org.apache.xmlbeans.impl.store.Xobj, javax.xml.namespace.QName, org.apache.xmlbeans.QNameSet, int):org.apache.xmlbeans.impl.store.Xobj");
        }
    }

    /* JADX WARN: Failed to check method for inline after forced processorg.apache.xmlbeans.impl.store.Locale.domNthCache.access$302(org.apache.xmlbeans.impl.store.Locale$domNthCache, long):long */
    DomImpl.Dom findDomNthChild(DomImpl.Dom parent, int n) {
        DomImpl.Dom x;
        if (!$assertionsDisabled && n < 0) {
            throw new AssertionError();
        }
        if (parent == null) {
            return null;
        }
        int da = this._domNthCache_A.distance(parent, n);
        int db = this._domNthCache_B.distance(parent, n);
        boolean bInvalidate = db - (this._domNthCache_B._len / 2) > 0 && (db - (this._domNthCache_B._len / 2)) - 40 > 0;
        boolean aInvalidate = da - (this._domNthCache_A._len / 2) > 0 && (da - (this._domNthCache_A._len / 2)) - 40 > 0;
        if (da <= db) {
            if (!aInvalidate) {
                x = this._domNthCache_A.fetch(parent, n);
            } else {
                domNthCache.access$302(this._domNthCache_B, -1L);
                x = this._domNthCache_B.fetch(parent, n);
            }
        } else if (!bInvalidate) {
            x = this._domNthCache_B.fetch(parent, n);
        } else {
            domNthCache.access$302(this._domNthCache_A, -1L);
            x = this._domNthCache_A.fetch(parent, n);
        }
        if (da == db) {
            domNthCache temp = this._domNthCache_A;
            this._domNthCache_A = this._domNthCache_B;
            this._domNthCache_B = temp;
        }
        return x;
    }

    int domLength(DomImpl.Dom parent) {
        if (parent == null) {
            return 0;
        }
        int da = this._domNthCache_A.distance(parent, 0);
        int db = this._domNthCache_B.distance(parent, 0);
        int len = da <= db ? this._domNthCache_A.length(parent) : this._domNthCache_B.length(parent);
        if (da == db) {
            domNthCache temp = this._domNthCache_A;
            this._domNthCache_A = this._domNthCache_B;
            this._domNthCache_B = temp;
        }
        return len;
    }

    /* JADX WARN: Failed to check method for inline after forced processorg.apache.xmlbeans.impl.store.Locale.domNthCache.access$302(org.apache.xmlbeans.impl.store.Locale$domNthCache, long):long */
    void invalidateDomCaches(DomImpl.Dom d) {
        if (this._domNthCache_A._parent == d) {
            domNthCache.access$302(this._domNthCache_A, -1L);
        }
        if (this._domNthCache_B._parent == d) {
            domNthCache.access$302(this._domNthCache_B, -1L);
        }
    }

    boolean isDomCached(DomImpl.Dom d) {
        return this._domNthCache_A._parent == d || this._domNthCache_B._parent == d;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$domNthCache.class */
    class domNthCache {
        public static final int BLITZ_BOUNDARY = 40;
        private long _version;
        private DomImpl.Dom _parent;
        private DomImpl.Dom _child;
        private int _n;
        private int _len;
        static final /* synthetic */ boolean $assertionsDisabled;

        /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ long access$302(org.apache.xmlbeans.impl.store.Locale.domNthCache r6, long r7) {
            /*
                r0 = r6
                r1 = r7
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0._version = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Locale.domNthCache.access$302(org.apache.xmlbeans.impl.store.Locale$domNthCache, long):long");
        }

        domNthCache() {
        }

        static {
            $assertionsDisabled = !Locale.class.desiredAssertionStatus();
        }

        int distance(DomImpl.Dom parent, int n) {
            if (!$assertionsDisabled && n < 0) {
                throw new AssertionError();
            }
            if (this._version != Locale.this.version()) {
                return 2147483646;
            }
            if (parent != this._parent) {
                return Integer.MAX_VALUE;
            }
            return n > this._n ? n - this._n : this._n - n;
        }

        int length(DomImpl.Dom parent) {
            DomImpl.Dom x;
            if (this._version != Locale.this.version() || this._parent != parent) {
                this._parent = parent;
                this._version = Locale.this.version();
                this._child = null;
                this._n = -1;
                this._len = -1;
            }
            if (this._len == -1) {
                if (this._child != null && this._n != -1) {
                    x = this._child;
                    this._len = this._n;
                } else {
                    x = DomImpl.firstChild(this._parent);
                    this._len = 0;
                    this._child = x;
                    this._n = 0;
                }
                while (x != null) {
                    this._len++;
                    x = DomImpl.nextSibling(x);
                }
            }
            return this._len;
        }

        DomImpl.Dom fetch(DomImpl.Dom parent, int n) {
            if (!$assertionsDisabled && n < 0) {
                throw new AssertionError();
            }
            if (this._version != Locale.this.version() || this._parent != parent) {
                this._parent = parent;
                this._version = Locale.this.version();
                this._child = null;
                this._n = -1;
                this._len = -1;
                DomImpl.Dom domFirstChild = DomImpl.firstChild(this._parent);
                while (true) {
                    DomImpl.Dom x = domFirstChild;
                    if (x == null) {
                        break;
                    }
                    this._n++;
                    if (this._child != null || n != this._n) {
                        domFirstChild = DomImpl.nextSibling(x);
                    } else {
                        this._child = x;
                        break;
                    }
                }
                return this._child;
            }
            if (this._n < 0) {
                return null;
            }
            if (n > this._n) {
                while (n > this._n) {
                    DomImpl.Dom x2 = DomImpl.nextSibling(this._child);
                    if (x2 == null) {
                        return null;
                    }
                    this._child = x2;
                    this._n++;
                }
            } else if (n < this._n) {
                while (n < this._n) {
                    DomImpl.Dom x3 = DomImpl.prevSibling(this._child);
                    if (x3 == null) {
                        return null;
                    }
                    this._child = x3;
                    this._n--;
                }
            }
            return this._child;
        }
    }

    CharUtil getCharUtil() {
        if (this._charUtil == null) {
            this._charUtil = new CharUtil(1024);
        }
        return this._charUtil;
    }

    long version() {
        return this._versionAll;
    }

    Cur weakCur(Object o) {
        if (!$assertionsDisabled && (o == null || (o instanceof Ref))) {
            throw new AssertionError();
        }
        Cur c = getCur();
        if (!$assertionsDisabled && c._tempFrame != -1) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && c._ref != null) {
            throw new AssertionError();
        }
        c._ref = new Ref(c, o);
        return c;
    }

    final ReferenceQueue refQueue() {
        if (this._refQueue == null) {
            this._refQueue = new ReferenceQueue();
        }
        return this._refQueue;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$Ref.class */
    static final class Ref extends PhantomReference {
        Cur _cur;

        Ref(Cur c, Object obj) {
            super(obj, c._locale.refQueue());
            this._cur = c;
        }
    }

    Cur tempCur() {
        return tempCur(null);
    }

    Cur tempCur(String id) {
        Cur c = getCur();
        if (!$assertionsDisabled && c._tempFrame != -1) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this._numTempFramesLeft >= this._tempFrames.length) {
            throw new AssertionError("Temp frame not pushed");
        }
        int frame = (this._tempFrames.length - this._numTempFramesLeft) - 1;
        if (!$assertionsDisabled && (frame < 0 || frame >= this._tempFrames.length)) {
            throw new AssertionError();
        }
        Cur next = this._tempFrames[frame];
        c._nextTemp = next;
        if (!$assertionsDisabled && c._prevTemp != null) {
            throw new AssertionError();
        }
        if (next != null) {
            if (!$assertionsDisabled && next._prevTemp != null) {
                throw new AssertionError();
            }
            next._prevTemp = c;
        }
        this._tempFrames[frame] = c;
        c._tempFrame = frame;
        c._id = id;
        return c;
    }

    Cur getCur() {
        Cur c;
        if (!$assertionsDisabled && this._curPool != null && this._curPoolCount <= 0) {
            throw new AssertionError();
        }
        if (this._curPool == null) {
            c = new Cur(this);
        } else {
            Cur cur = this._curPool;
            Cur cur2 = this._curPool;
            c = cur2;
            this._curPool = cur.listRemove(cur2);
            this._curPoolCount--;
        }
        if (!$assertionsDisabled && c._state != 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (c._prev != null || c._next != null)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (c._xobj != null || c._pos != -2)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && c._ref != null) {
            throw new AssertionError();
        }
        this._registered = c.listInsert(this._registered);
        c._state = 1;
        return c;
    }

    void embedCurs() {
        while (true) {
            Cur c = this._registered;
            if (c != null) {
                if (!$assertionsDisabled && c._xobj == null) {
                    throw new AssertionError();
                }
                this._registered = c.listRemove(this._registered);
                c._xobj._embedded = c.listInsert(c._xobj._embedded);
                c._state = 2;
            } else {
                return;
            }
        }
    }

    DomImpl.TextNode createTextNode() {
        return this._saaj == null ? new DomImpl.TextNode(this) : new DomImpl.SaajTextNode(this);
    }

    DomImpl.CdataNode createCdataNode() {
        return this._saaj == null ? new DomImpl.CdataNode(this) : new DomImpl.SaajCdataNode(this);
    }

    boolean entered() {
        return this._tempFrames.length - this._numTempFramesLeft > 0;
    }

    public void enter(Locale otherLocale) {
        enter();
        if (otherLocale != this) {
            otherLocale.enter();
        }
    }

    @Override // org.apache.xmlbeans.impl.common.XmlLocale
    public void enter() {
        if (!$assertionsDisabled && this._numTempFramesLeft < 0) {
            throw new AssertionError();
        }
        int i = this._numTempFramesLeft - 1;
        this._numTempFramesLeft = i;
        if (i <= 0) {
            Cur[] newTempFrames = new Cur[this._tempFrames.length * 2];
            this._numTempFramesLeft = this._tempFrames.length;
            System.arraycopy(this._tempFrames, 0, newTempFrames, 0, this._tempFrames.length);
            this._tempFrames = newTempFrames;
        }
        int i2 = this._entryCount + 1;
        this._entryCount = i2;
        if (i2 > 1000) {
            pollQueue();
            this._entryCount = 0;
        }
    }

    private void pollQueue() {
        if (this._refQueue == null) {
            return;
        }
        while (true) {
            Ref ref = (Ref) this._refQueue.poll();
            if (ref != null) {
                if (ref._cur != null) {
                    ref._cur.release();
                }
            } else {
                return;
            }
        }
    }

    public void exit(Locale otherLocale) {
        exit();
        if (otherLocale != this) {
            otherLocale.exit();
        }
    }

    @Override // org.apache.xmlbeans.impl.common.XmlLocale
    public void exit() {
        if (!$assertionsDisabled && (this._numTempFramesLeft < 0 || this._numTempFramesLeft > this._tempFrames.length - 1)) {
            throw new AssertionError(" Temp frames mismanaged. Impossible stack frame. Unsynchronized: " + noSync());
        }
        int length = this._tempFrames.length;
        int i = this._numTempFramesLeft + 1;
        this._numTempFramesLeft = i;
        int frame = length - i;
        while (this._tempFrames[frame] != null) {
            this._tempFrames[frame].release();
        }
    }

    @Override // org.apache.xmlbeans.impl.common.XmlLocale
    public boolean noSync() {
        return this._noSync;
    }

    @Override // org.apache.xmlbeans.impl.common.XmlLocale
    public boolean sync() {
        return !this._noSync;
    }

    static final boolean isWhiteSpace(String s) {
        int l = s.length();
        do {
            int i = l;
            l--;
            if (i <= 0) {
                return true;
            }
        } while (CharUtil.isWhiteSpace(s.charAt(l)));
        return false;
    }

    static final boolean isWhiteSpace(StringBuffer sb) {
        int l = sb.length();
        do {
            int i = l;
            l--;
            if (i <= 0) {
                return true;
            }
        } while (CharUtil.isWhiteSpace(sb.charAt(l)));
        return false;
    }

    static boolean beginsWithXml(String name) {
        if (name.length() < 3) {
            return false;
        }
        char ch2 = name.charAt(0);
        if (ch2 != 'x' && ch2 != 'X') {
            return false;
        }
        char ch3 = name.charAt(1);
        if (ch3 != 'm' && ch3 != 'M') {
            return false;
        }
        char ch4 = name.charAt(2);
        if (ch4 == 'l' || ch4 == 'L') {
            return true;
        }
        return false;
    }

    static boolean isXmlns(QName name) {
        String prefix = name.getPrefix();
        if (prefix.equals("xmlns")) {
            return true;
        }
        return prefix.length() == 0 && name.getLocalPart().equals("xmlns");
    }

    QName createXmlns(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        return prefix.length() == 0 ? makeQName("http://www.w3.org/2000/xmlns/", "xmlns", "") : makeQName("http://www.w3.org/2000/xmlns/", prefix, "xmlns");
    }

    static String xmlnsPrefix(QName name) {
        return name.getPrefix().equals("xmlns") ? name.getLocalPart() : "";
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$LoadContext.class */
    static abstract class LoadContext {
        private Hashtable _idAttrs;

        protected abstract void startDTD(String str, String str2, String str3);

        protected abstract void endDTD();

        protected abstract void startElement(QName qName);

        protected abstract void endElement();

        protected abstract void attr(QName qName, String str);

        protected abstract void attr(String str, String str2, String str3, String str4);

        protected abstract void xmlns(String str, String str2);

        protected abstract void comment(char[] cArr, int i, int i2);

        protected abstract void comment(String str);

        protected abstract void procInst(String str, String str2);

        protected abstract void text(char[] cArr, int i, int i2);

        protected abstract void text(String str);

        protected abstract Cur finish();

        protected abstract void abort();

        protected abstract void bookmark(XmlCursor.XmlBookmark xmlBookmark);

        protected abstract void bookmarkLastNonAttr(XmlCursor.XmlBookmark xmlBookmark);

        protected abstract void bookmarkLastAttr(QName qName, XmlCursor.XmlBookmark xmlBookmark);

        protected abstract void lineNumber(int i, int i2, int i3);

        LoadContext() {
        }

        protected void addIdAttr(String eName, String aName) {
            if (this._idAttrs == null) {
                this._idAttrs = new Hashtable();
            }
            this._idAttrs.put(aName, eName);
        }

        protected boolean isAttrOfTypeId(QName aqn, QName eqn) {
            if (this._idAttrs == null) {
                return false;
            }
            String pre = aqn.getPrefix();
            String lName = aqn.getLocalPart();
            String urnName = "".equals(pre) ? lName : pre + ":" + lName;
            String eName = (String) this._idAttrs.get(urnName);
            if (eName == null) {
                return false;
            }
            String pre2 = eqn.getPrefix();
            eqn.getLocalPart();
            String lName2 = eqn.getLocalPart();
            String urnName2 = "".equals(pre2) ? lName2 : pre2 + ":" + lName2;
            return eName.equals(urnName2);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$DefaultEntityResolver.class */
    private static class DefaultEntityResolver implements EntityResolver {
        private DefaultEntityResolver() {
        }

        @Override // org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new StringReader(""));
        }
    }

    private static SaxLoader getSaxLoader(XmlOptions options) throws XmlException {
        XmlOptions options2 = XmlOptions.maskNull(options);
        EntityResolver er = null;
        if (!options2.hasOption(XmlOptions.LOAD_USE_DEFAULT_RESOLVER)) {
            er = (EntityResolver) options2.get(XmlOptions.ENTITY_RESOLVER);
            if (er == null) {
                er = ResolverUtil.getGlobalEntityResolver();
            }
            if (er == null) {
                er = new DefaultEntityResolver();
            }
        }
        XMLReader xr = (XMLReader) options2.get(XmlOptions.LOAD_USE_XMLREADER);
        if (xr == null) {
            try {
                xr = SAXHelper.newXMLReader(new XmlOptionsBean(options2));
            } catch (Exception e) {
                throw new XmlException("Problem creating XMLReader", e);
            }
        }
        SaxLoader sl = new XmlReaderSaxLoader(xr);
        if (er != null) {
            xr.setEntityResolver(er);
        }
        return sl;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$XmlReaderSaxLoader.class */
    private static class XmlReaderSaxLoader extends SaxLoader {
        XmlReaderSaxLoader(XMLReader xr) {
            super(xr, null);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$SaxHandler.class */
    private static abstract class SaxHandler implements ContentHandler, LexicalHandler, DeclHandler, DTDHandler {
        protected Locale _locale;
        protected LoadContext _context;
        private boolean _wantLineNumbers;
        private boolean _wantLineNumbersAtEndElt;
        private boolean _wantCdataBookmarks;
        private Locator _startLocator;
        private boolean _insideCDATA;
        private int _entityBytesLimit;
        private int _entityBytes;
        private int _insideEntity;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Locale.class.desiredAssertionStatus();
        }

        SaxHandler(Locator startLocator) {
            this._insideCDATA = false;
            this._entityBytesLimit = 10240;
            this._entityBytes = 0;
            this._insideEntity = 0;
            this._startLocator = startLocator;
        }

        SaxHandler() {
            this(null);
        }

        void initSaxHandler(Locale l, XmlOptions options) {
            this._locale = l;
            XmlOptions safeOptions = XmlOptions.maskNull(options);
            this._context = new Cur.CurLoadContext(this._locale, safeOptions);
            this._wantLineNumbers = safeOptions.hasOption(XmlOptions.LOAD_LINE_NUMBERS);
            this._wantLineNumbersAtEndElt = safeOptions.hasOption(XmlOptions.LOAD_LINE_NUMBERS_END_ELEMENT);
            this._wantCdataBookmarks = safeOptions.hasOption(XmlOptions.LOAD_SAVE_CDATA_BOOKMARKS);
            if (safeOptions.hasOption(XmlOptions.LOAD_ENTITY_BYTES_LIMIT)) {
                this._entityBytesLimit = ((Integer) safeOptions.get(XmlOptions.LOAD_ENTITY_BYTES_LIMIT)).intValue();
            }
        }

        @Override // org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
        }

        @Override // org.xml.sax.ContentHandler
        public void endDocument() throws SAXException {
        }

        @Override // org.xml.sax.ContentHandler
        public void startElement(String uri, String local, String qName, Attributes atts) throws SAXException {
            if (local.length() == 0) {
            }
            if (qName.indexOf(58) >= 0 && uri.length() == 0) {
                XmlError err = XmlError.forMessage("Use of undefined namespace prefix: " + qName.substring(0, qName.indexOf(58)));
                throw new XmlRuntimeException(err.toString(), (Throwable) null, err);
            }
            this._context.startElement(this._locale.makeQualifiedQName(uri, qName));
            if (this._wantLineNumbers && this._startLocator != null) {
                this._context.bookmark(new XmlLineNumber(this._startLocator.getLineNumber(), this._startLocator.getColumnNumber() - 1, -1));
            }
            int len = atts.getLength();
            for (int i = 0; i < len; i++) {
                String aqn = atts.getQName(i);
                if (aqn.equals("xmlns")) {
                    this._context.xmlns("", atts.getValue(i));
                } else if (aqn.startsWith(Sax2Dom.XMLNS_STRING)) {
                    String prefix = aqn.substring(6);
                    if (prefix.length() == 0) {
                        XmlError err2 = XmlError.forMessage("Prefix not specified", 0);
                        throw new XmlRuntimeException(err2.toString(), (Throwable) null, err2);
                    }
                    String attrUri = atts.getValue(i);
                    if (attrUri.length() == 0) {
                        XmlError err3 = XmlError.forMessage("Prefix can't be mapped to no namespace: " + prefix, 0);
                        throw new XmlRuntimeException(err3.toString(), (Throwable) null, err3);
                    }
                    this._context.xmlns(prefix, attrUri);
                } else {
                    int colon = aqn.indexOf(58);
                    if (colon < 0) {
                        this._context.attr(aqn, atts.getURI(i), null, atts.getValue(i));
                    } else {
                        this._context.attr(aqn.substring(colon + 1), atts.getURI(i), aqn.substring(0, colon), atts.getValue(i));
                    }
                }
            }
        }

        @Override // org.xml.sax.ContentHandler
        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            this._context.endElement();
            if (this._wantLineNumbersAtEndElt && this._startLocator != null) {
                this._context.bookmark(new XmlLineNumber(this._startLocator.getLineNumber(), this._startLocator.getColumnNumber() - 1, -1));
            }
        }

        @Override // org.xml.sax.ContentHandler
        public void characters(char[] ch2, int start, int length) throws SAXException {
            this._context.text(ch2, start, length);
            if (this._wantCdataBookmarks && this._insideCDATA && this._startLocator != null) {
                this._context.bookmarkLastNonAttr(CDataBookmark.CDATA_BOOKMARK);
            }
            if (this._insideEntity != 0) {
                int i = this._entityBytes + length;
                this._entityBytes = i;
                if (i > this._entityBytesLimit) {
                    XmlError err = XmlError.forMessage(XmlErrorCodes.EXCEPTION_EXCEEDED_ENTITY_BYTES, new Integer[]{Integer.valueOf(this._entityBytesLimit)});
                    throw new SAXException(err.getMessage());
                }
            }
        }

        @Override // org.xml.sax.ContentHandler
        public void ignorableWhitespace(char[] ch2, int start, int length) throws SAXException {
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void comment(char[] ch2, int start, int length) throws SAXException {
            this._context.comment(ch2, start, length);
        }

        @Override // org.xml.sax.ContentHandler
        public void processingInstruction(String target, String data) throws SAXException {
            this._context.procInst(target, data);
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void startDTD(String name, String publicId, String systemId) throws SAXException {
            this._context.startDTD(name, publicId, systemId);
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void endDTD() throws SAXException {
            this._context.endDTD();
        }

        @Override // org.xml.sax.ContentHandler
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            if (Locale.beginsWithXml(prefix)) {
                if (!"xml".equals(prefix) || !"http://www.w3.org/XML/1998/namespace".equals(uri)) {
                    XmlError err = XmlError.forMessage("Prefix can't begin with XML: " + prefix, 0);
                    throw new XmlRuntimeException(err.toString(), (Throwable) null, err);
                }
            }
        }

        @Override // org.xml.sax.ContentHandler
        public void endPrefixMapping(String prefix) throws SAXException {
        }

        @Override // org.xml.sax.ContentHandler
        public void skippedEntity(String name) throws SAXException {
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void startCDATA() throws SAXException {
            this._insideCDATA = true;
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void endCDATA() throws SAXException {
            this._insideCDATA = false;
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void startEntity(String name) throws SAXException {
            this._insideEntity++;
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void endEntity(String name) throws SAXException {
            this._insideEntity--;
            if (!$assertionsDisabled && this._insideEntity < 0) {
                throw new AssertionError();
            }
            if (this._insideEntity == 0) {
                this._entityBytes = 0;
            }
        }

        @Override // org.xml.sax.ContentHandler
        public void setDocumentLocator(Locator locator) {
            if (this._startLocator == null) {
                this._startLocator = locator;
            }
        }

        @Override // org.xml.sax.ext.DeclHandler
        public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) {
            if (type.equals("ID")) {
                this._context.addIdAttr(eName, aName);
            }
        }

        @Override // org.xml.sax.ext.DeclHandler
        public void elementDecl(String name, String model) {
        }

        @Override // org.xml.sax.ext.DeclHandler
        public void externalEntityDecl(String name, String publicId, String systemId) {
        }

        @Override // org.xml.sax.ext.DeclHandler
        public void internalEntityDecl(String name, String value) {
        }

        @Override // org.xml.sax.DTDHandler
        public void notationDecl(String name, String publicId, String systemId) {
        }

        @Override // org.xml.sax.DTDHandler
        public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) {
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$SaxLoader.class */
    private static abstract class SaxLoader extends SaxHandler implements ErrorHandler {
        private XMLReader _xr;

        SaxLoader(XMLReader xr, Locator startLocator) {
            super(startLocator);
            this._xr = xr;
            try {
                this._xr.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                this._xr.setFeature("http://xml.org/sax/features/namespaces", true);
                this._xr.setFeature("http://xml.org/sax/features/validation", false);
                this._xr.setProperty("http://xml.org/sax/properties/lexical-handler", this);
                this._xr.setContentHandler(this);
                this._xr.setProperty("http://xml.org/sax/properties/declaration-handler", this);
                this._xr.setDTDHandler(this);
                this._xr.setErrorHandler(this);
            } catch (Throwable e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        void setEntityResolver(EntityResolver er) {
            this._xr.setEntityResolver(er);
        }

        void postLoad(Cur c) {
            this._locale = null;
            this._context = null;
        }

        public Cur load(Locale l, InputSource is, XmlOptions options) throws XmlException, SAXException, IOException {
            is.setSystemId("file://");
            initSaxHandler(l, options);
            try {
                this._xr.parse(is);
                Cur c = this._context.finish();
                Locale.associateSourceName(c, options);
                postLoad(c);
                return c;
            } catch (XmlRuntimeException e) {
                this._context.abort();
                throw new XmlException(e);
            } catch (RuntimeException e2) {
                this._context.abort();
                throw e2;
            } catch (SAXParseException e3) {
                this._context.abort();
                XmlError err = XmlError.forLocation(e3.getMessage(), (String) XmlOptions.safeGet(options, XmlOptions.DOCUMENT_SOURCE_NAME), e3.getLineNumber(), e3.getColumnNumber(), -1);
                throw new XmlException(err.toString(), e3, err);
            } catch (SAXException e4) {
                this._context.abort();
                XmlError err2 = XmlError.forMessage(e4.getMessage());
                throw new XmlException(err2.toString(), e4, err2);
            }
        }

        @Override // org.xml.sax.ErrorHandler
        public void fatalError(SAXParseException e) throws SAXException {
            throw e;
        }

        @Override // org.xml.sax.ErrorHandler
        public void error(SAXParseException e) throws SAXException {
            throw e;
        }

        @Override // org.xml.sax.ErrorHandler
        public void warning(SAXParseException e) throws SAXException {
            throw e;
        }
    }

    private DomImpl.Dom load(InputSource is, XmlOptions options) throws XmlException, IOException {
        return getSaxLoader(options).load(this, is, options).getDom();
    }

    public DomImpl.Dom load(Reader r) throws XmlException, IOException {
        return load(r, (XmlOptions) null);
    }

    public DomImpl.Dom load(Reader r, XmlOptions options) throws XmlException, IOException {
        return load(new InputSource(r), options);
    }

    public DomImpl.Dom load(InputStream in) throws XmlException, IOException {
        return load(in, (XmlOptions) null);
    }

    public DomImpl.Dom load(InputStream in, XmlOptions options) throws XmlException, IOException {
        return load(new InputSource(in), options);
    }

    public DomImpl.Dom load(String s) throws XmlException {
        return load(s, (XmlOptions) null);
    }

    public DomImpl.Dom load(String s, XmlOptions options) throws XmlException, IOException {
        Reader r = new StringReader(s);
        try {
            try {
                return load(r, options);
            } catch (IOException e) {
                if ($assertionsDisabled) {
                    throw new XmlException(e.getMessage(), e);
                }
                throw new AssertionError("StringReader should not throw IOException");
            }
        } finally {
            try {
                r.close();
            } catch (IOException e2) {
            }
        }
    }

    @Override // org.w3c.dom.DOMImplementation
    public Document createDocument(String uri, String qname, DocumentType doctype) {
        return DomImpl._domImplementation_createDocument(this, uri, qname, doctype);
    }

    @Override // org.w3c.dom.DOMImplementation
    public DocumentType createDocumentType(String qname, String publicId, String systemId) {
        throw new RuntimeException("Not implemented");
    }

    @Override // org.w3c.dom.DOMImplementation
    public boolean hasFeature(String feature, String version) {
        return DomImpl._domImplementation_hasFeature(this, feature, version);
    }

    @Override // org.w3c.dom.DOMImplementation
    public Object getFeature(String feature, String version) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    private static DomImpl.Dom checkNode(Node n) {
        if (n == null) {
            throw new IllegalArgumentException("Node is null");
        }
        if (!(n instanceof DomImpl.Dom)) {
            throw new IllegalArgumentException("Node is not an XmlBeans node");
        }
        return (DomImpl.Dom) n;
    }

    public static XmlCursor nodeToCursor(Node n) {
        return DomImpl._getXmlCursor(checkNode(n));
    }

    public static XmlObject nodeToXmlObject(Node n) {
        return DomImpl._getXmlObject(checkNode(n));
    }

    public static XMLStreamReader nodeToXmlStream(Node n) {
        return DomImpl._getXmlStreamReader(checkNode(n));
    }

    public static Node streamToNode(XMLStreamReader xs) {
        return Jsr173.nodeFromStream(xs);
    }

    @Override // org.apache.xmlbeans.impl.store.Saaj.SaajCallback
    public void setSaajData(Node n, Object o) {
        if (!$assertionsDisabled && !(n instanceof DomImpl.Dom)) {
            throw new AssertionError();
        }
        DomImpl.saajCallback_setSaajData((DomImpl.Dom) n, o);
    }

    @Override // org.apache.xmlbeans.impl.store.Saaj.SaajCallback
    public Object getSaajData(Node n) {
        if ($assertionsDisabled || (n instanceof DomImpl.Dom)) {
            return DomImpl.saajCallback_getSaajData((DomImpl.Dom) n);
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.impl.store.Saaj.SaajCallback
    public Element createSoapElement(QName name, QName parentName) {
        if ($assertionsDisabled || this._ownerDoc != null) {
            return DomImpl.saajCallback_createSoapElement(this._ownerDoc, name, parentName);
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.impl.store.Saaj.SaajCallback
    public Element importSoapElement(Document doc, Element elem, boolean deep, QName parentName) {
        if ($assertionsDisabled || (doc instanceof DomImpl.Dom)) {
            return DomImpl.saajCallback_importSoapElement((DomImpl.Dom) doc, elem, deep, parentName);
        }
        throw new AssertionError();
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$DefaultQNameFactory.class */
    private static final class DefaultQNameFactory implements QNameFactory {
        private QNameCache _cache;

        private DefaultQNameFactory() {
            this._cache = XmlBeans.getQNameCache();
        }

        @Override // org.apache.xmlbeans.impl.store.QNameFactory
        public QName getQName(String uri, String local) {
            return this._cache.getName(uri, local, "");
        }

        @Override // org.apache.xmlbeans.impl.store.QNameFactory
        public QName getQName(String uri, String local, String prefix) {
            return this._cache.getName(uri, local, prefix);
        }

        @Override // org.apache.xmlbeans.impl.store.QNameFactory
        public QName getQName(char[] uriSrc, int uriPos, int uriCch, char[] localSrc, int localPos, int localCch) {
            return this._cache.getName(new String(uriSrc, uriPos, uriCch), new String(localSrc, localPos, localCch), "");
        }

        @Override // org.apache.xmlbeans.impl.store.QNameFactory
        public QName getQName(char[] uriSrc, int uriPos, int uriCch, char[] localSrc, int localPos, int localCch, char[] prefixSrc, int prefixPos, int prefixCch) {
            return this._cache.getName(new String(uriSrc, uriPos, uriCch), new String(localSrc, localPos, localCch), new String(prefixSrc, prefixPos, prefixCch));
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Locale$LocalDocumentQNameFactory.class */
    private static final class LocalDocumentQNameFactory implements QNameFactory {
        private QNameCache _cache = new QNameCache(32);

        private LocalDocumentQNameFactory() {
        }

        @Override // org.apache.xmlbeans.impl.store.QNameFactory
        public QName getQName(String uri, String local) {
            return this._cache.getName(uri, local, "");
        }

        @Override // org.apache.xmlbeans.impl.store.QNameFactory
        public QName getQName(String uri, String local, String prefix) {
            return this._cache.getName(uri, local, prefix);
        }

        @Override // org.apache.xmlbeans.impl.store.QNameFactory
        public QName getQName(char[] uriSrc, int uriPos, int uriCch, char[] localSrc, int localPos, int localCch) {
            return this._cache.getName(new String(uriSrc, uriPos, uriCch), new String(localSrc, localPos, localCch), "");
        }

        @Override // org.apache.xmlbeans.impl.store.QNameFactory
        public QName getQName(char[] uriSrc, int uriPos, int uriCch, char[] localSrc, int localPos, int localCch, char[] prefixSrc, int prefixPos, int prefixCch) {
            return this._cache.getName(new String(uriSrc, uriPos, uriCch), new String(localSrc, localPos, localCch), new String(prefixSrc, prefixPos, prefixCch));
        }
    }
}
