package org.apache.xmlbeans.impl.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.store.DomImpl;
import org.apache.xmlbeans.impl.store.Saver;
import org.apache.xmlbeans.impl.values.NamespaceManager;
import org.apache.xmlbeans.impl.values.TypeStore;
import org.apache.xmlbeans.impl.values.TypeStoreUser;
import org.apache.xmlbeans.impl.values.TypeStoreVisitor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Public2.class */
public final class Public2 {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Public2.class.desiredAssertionStatus();
    }

    private static Locale newLocale(Saaj saaj) {
        XmlOptions options = null;
        if (saaj != null) {
            options = new XmlOptions();
            options.put(Saaj.SAAJ_IMPL, saaj);
        }
        return Locale.getLocale(null, options);
    }

    private static Locale newLocale() {
        return Locale.getLocale(null, null);
    }

    public static void setSync(Document doc, boolean sync) {
        if (!$assertionsDisabled && !(doc instanceof DomImpl.Dom)) {
            throw new AssertionError();
        }
        Locale l = ((DomImpl.Dom) doc).locale();
        l._noSync = !sync;
    }

    public static String compilePath(String path, XmlOptions options) {
        return Path.compilePath(path, options);
    }

    public static DOMImplementation getDomImplementation() {
        return newLocale();
    }

    public static DOMImplementation getDomImplementation(Saaj saaj) {
        return newLocale(saaj);
    }

    public static Document parse(String s) throws XmlException {
        DomImpl.Dom d;
        Locale l = newLocale();
        if (l.noSync()) {
            l.enter();
            try {
                d = l.load(s);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    d = l.load(s);
                    l.exit();
                } finally {
                }
            }
        }
        return (Document) d;
    }

    public static Document parse(String s, XmlOptions options) throws XmlException {
        DomImpl.Dom d;
        Locale l = newLocale();
        if (l.noSync()) {
            l.enter();
            try {
                d = l.load(s, options);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    d = l.load(s, options);
                    l.exit();
                } finally {
                }
            }
        }
        return (Document) d;
    }

    public static Document parse(String s, Saaj saaj) throws XmlException {
        DomImpl.Dom d;
        Locale l = newLocale(saaj);
        if (l.noSync()) {
            l.enter();
            try {
                d = l.load(s);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    d = l.load(s);
                    l.exit();
                } finally {
                }
            }
        }
        return (Document) d;
    }

    public static Document parse(InputStream is, XmlOptions options) throws XmlException, IOException {
        DomImpl.Dom d;
        Locale l = newLocale();
        if (l.noSync()) {
            l.enter();
            try {
                d = l.load(is, options);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    d = l.load(is, options);
                    l.exit();
                } finally {
                }
            }
        }
        return (Document) d;
    }

    public static Document parse(InputStream is, Saaj saaj) throws XmlException, IOException {
        DomImpl.Dom d;
        Locale l = newLocale(saaj);
        if (l.noSync()) {
            l.enter();
            try {
                d = l.load(is);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    d = l.load(is);
                    l.exit();
                } finally {
                }
            }
        }
        return (Document) d;
    }

    public static Node getNode(XMLStreamReader s) {
        return Jsr173.nodeFromStream(s);
    }

    public static XMLStreamReader getStream(Node n) {
        XMLStreamReader xmlStreamReader;
        if (!$assertionsDisabled && !(n instanceof DomImpl.Dom)) {
            throw new AssertionError();
        }
        DomImpl.Dom d = (DomImpl.Dom) n;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                XMLStreamReader xmlStreamReader2 = DomImpl.getXmlStreamReader(d);
                l.exit();
                return xmlStreamReader2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                xmlStreamReader = DomImpl.getXmlStreamReader(d);
                l.exit();
            } finally {
            }
        }
        return xmlStreamReader;
    }

    public static String save(Node n) {
        return save(n, (XmlOptions) null);
    }

    public static void save(Node n, OutputStream os, XmlOptions options) throws IOException {
        XmlCursor c = getCursor(n);
        c.save(os, options);
        c.dispose();
    }

    public static String save(Node n, XmlOptions options) {
        String strSaveImpl;
        if (!$assertionsDisabled && !(n instanceof DomImpl.Dom)) {
            throw new AssertionError();
        }
        DomImpl.Dom d = (DomImpl.Dom) n;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                String strSaveImpl2 = saveImpl(d, options);
                l.exit();
                return strSaveImpl2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strSaveImpl = saveImpl(d, options);
                l.exit();
            } finally {
            }
        }
        return strSaveImpl;
    }

    private static String saveImpl(DomImpl.Dom d, XmlOptions options) {
        Cur c = d.tempCur();
        String s = new Saver.TextSaver(c, options, null).saveToString();
        c.release();
        return s;
    }

    public static String save(XmlCursor c) {
        return save(c, (XmlOptions) null);
    }

    public static String save(XmlCursor xc, XmlOptions options) {
        String strSaveImpl;
        Cursor cursor = (Cursor) xc;
        Locale l = cursor.locale();
        if (l.noSync()) {
            l.enter();
            try {
                String strSaveImpl2 = saveImpl(cursor, options);
                l.exit();
                return strSaveImpl2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strSaveImpl = saveImpl(cursor, options);
                l.exit();
            } finally {
            }
        }
        return strSaveImpl;
    }

    private static String saveImpl(Cursor cursor, XmlOptions options) {
        Cur c = cursor.tempCur();
        String s = new Saver.TextSaver(c, options, null).saveToString();
        c.release();
        return s;
    }

    public static XmlCursor newStore() {
        return newStore(null);
    }

    public static XmlCursor newStore(Saaj saaj) {
        XmlCursor xmlCursor_newStore;
        Locale l = newLocale(saaj);
        if (l.noSync()) {
            l.enter();
            try {
                XmlCursor xmlCursor_newStore2 = _newStore(l);
                l.exit();
                return xmlCursor_newStore2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                xmlCursor_newStore = _newStore(l);
                l.exit();
            } finally {
            }
        }
        return xmlCursor_newStore;
    }

    public static XmlCursor _newStore(Locale l) {
        Cur c = l.tempCur();
        c.createRoot();
        Cursor cursor = new Cursor(c);
        c.release();
        return cursor;
    }

    public static XmlCursor getCursor(Node n) {
        XmlCursor xmlCursor;
        if (!$assertionsDisabled && !(n instanceof DomImpl.Dom)) {
            throw new AssertionError();
        }
        DomImpl.Dom d = (DomImpl.Dom) n;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                XmlCursor xmlCursor2 = DomImpl.getXmlCursor(d);
                l.exit();
                return xmlCursor2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                xmlCursor = DomImpl.getXmlCursor(d);
                l.exit();
            } finally {
            }
        }
        return xmlCursor;
    }

    public static void dump(PrintStream o, DomImpl.Dom d) {
        d.dump(o);
    }

    public static void dump(PrintStream o, Node n) {
        dump(o, (DomImpl.Dom) n);
    }

    public static void dump(PrintStream o, XmlCursor c) {
        ((Cursor) c).dump(o);
    }

    public static void dump(PrintStream o, XmlObject x) {
        XmlCursor xc = x.newCursor();
        Node n = xc.getDomNode();
        DomImpl.Dom d = (DomImpl.Dom) n;
        xc.dispose();
        dump(o, d);
    }

    public static void dump(DomImpl.Dom d) {
        dump(System.out, d);
    }

    public static void dump(Node n) {
        dump(System.out, n);
    }

    public static void dump(XmlCursor c) {
        dump(System.out, c);
    }

    public static void dump(XmlObject x) {
        dump(System.out, x);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Public2$TestTypeStoreUser.class */
    private static class TestTypeStoreUser implements TypeStoreUser {
        private String _value;

        TestTypeStoreUser(String value) {
            this._value = value;
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public void attach_store(TypeStore store) {
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public SchemaType get_schema_type() {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public TypeStore get_store() {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public void invalidate_value() {
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public boolean uses_invalidate_value() {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public String build_text(NamespaceManager nsm) {
            return this._value;
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public boolean build_nil() {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public void invalidate_nilvalue() {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public void invalidate_element_order() {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public void validate_now() {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public void disconnect_store() {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public TypeStoreUser create_element_user(QName eltName, QName xsiType) {
            return new TestTypeStoreUser("ELEM");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public TypeStoreUser create_attribute_user(QName attrName) {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public String get_default_element_text(QName eltName) {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public String get_default_attribute_text(QName attrName) {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public SchemaType get_element_type(QName eltName, QName xsiType) {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public SchemaType get_attribute_type(QName attrName) {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public int get_elementflags(QName eltName) {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public int get_attributeflags(QName attrName) {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public SchemaField get_attribute_field(QName attrName) {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public boolean is_child_element_order_sensitive() {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public QNameSet get_element_ending_delimiters(QName eltname) {
            throw new RuntimeException("Not impl");
        }

        @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
        public TypeStoreVisitor new_visitor() {
            throw new RuntimeException("Not impl");
        }
    }

    public static void test() throws Exception {
        Xobj x = (Xobj) parse("<a>XY</a>");
        Locale l = x._locale;
        l.enter();
        try {
            try {
                Cur c = x.tempCur();
                c.next();
                Cur c2 = c.tempCur();
                c2.next();
                Cur c3 = c2.tempCur();
                c3.nextChars(1);
                Cur c4 = c3.tempCur();
                c4.nextChars(1);
                c.dump();
                c.moveNodeContents(c, true);
                c.dump();
                l.exit();
            } catch (Throwable e) {
                e.printStackTrace();
                l.exit();
            }
        } catch (Throwable th) {
            l.exit();
            throw th;
        }
    }
}
