package org.apache.xmlbeans.impl.values;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.DelegateXmlObject;
import org.apache.xmlbeans.GDate;
import org.apache.xmlbeans.GDateSpecification;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.GDurationSpecification;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlRuntimeException;
import org.apache.xmlbeans.impl.common.GlobalLock;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.common.XmlErrorWatcher;
import org.apache.xmlbeans.impl.common.XmlLocale;
import org.apache.xmlbeans.impl.common.XmlWhitespace;
import org.apache.xmlbeans.impl.schema.SchemaTypeImpl;
import org.apache.xmlbeans.impl.schema.SchemaTypeVisitorImpl;
import org.apache.xmlbeans.impl.validator.Validator;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.bouncycastle.pqc.crypto.qteslarnd1.Polynomial;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlObjectBase.class */
public abstract class XmlObjectBase implements TypeStoreUser, Serializable, XmlObject, SimpleValue {
    public static final short MAJOR_VERSION_NUMBER = 1;
    public static final short MINOR_VERSION_NUMBER = 1;
    public static final short KIND_SETTERHELPER_SINGLETON = 1;
    public static final short KIND_SETTERHELPER_ARRAYITEM = 2;
    public static final ValidationContext _voorVc;
    private int _flags = 65;
    private Object _textsource;
    private static final int FLAG_NILLABLE = 1;
    private static final int FLAG_HASDEFAULT = 2;
    private static final int FLAG_FIXED = 4;
    private static final int FLAG_ATTRIBUTE = 8;
    private static final int FLAG_STORE = 16;
    private static final int FLAG_VALUE_DATED = 32;
    private static final int FLAG_NIL = 64;
    private static final int FLAG_NIL_DATED = 128;
    private static final int FLAG_ISDEFAULT = 256;
    private static final int FLAG_ELEMENT_DATED = 512;
    private static final int FLAG_SETTINGDEFAULT = 1024;
    private static final int FLAG_ORPHANED = 2048;
    private static final int FLAG_IMMUTABLE = 4096;
    private static final int FLAG_COMPLEXTYPE = 8192;
    private static final int FLAG_COMPLEXCONTENT = 16384;
    private static final int FLAG_NOT_VARIABLE = 32768;
    private static final int FLAG_VALIDATE_ON_SET = 65536;
    private static final int FLAGS_DATED = 672;
    private static final int FLAGS_ELEMENT = 7;
    private static final BigInteger _max;
    private static final BigInteger _min;
    private static final XmlOptions _toStringOptions;
    private static final XmlObject[] EMPTY_RESULT;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract SchemaType schemaType();

    protected abstract void set_text(String str);

    protected abstract void set_nil();

    protected abstract String compute_text(NamespaceManager namespaceManager);

    protected abstract boolean equal_to(XmlObject xmlObject);

    protected abstract int value_hash_code();

    static {
        $assertionsDisabled = !XmlObjectBase.class.desiredAssertionStatus();
        _voorVc = new ValueOutOfRangeValidationContext();
        _max = BigInteger.valueOf(Long.MAX_VALUE);
        _min = BigInteger.valueOf(Long.MIN_VALUE);
        _toStringOptions = buildInnerPrettyOptions();
        EMPTY_RESULT = new XmlObject[0];
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public final Object monitor() {
        if (has_store()) {
            return get_store().get_locale();
        }
        return this;
    }

    private static XmlObjectBase underlying(XmlObject obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof XmlObjectBase) {
            return (XmlObjectBase) obj;
        }
        while (obj instanceof DelegateXmlObject) {
            obj = ((DelegateXmlObject) obj).underlyingXmlObject();
        }
        if (obj instanceof XmlObjectBase) {
            return (XmlObjectBase) obj;
        }
        throw new IllegalStateException("Non-native implementations of XmlObject should extend FilterXmlObject or implement DelegateXmlObject");
    }

    @Override // org.apache.xmlbeans.XmlObject
    public final XmlObject copy() {
        XmlObject xmlObject_copy;
        if (preCheck()) {
            return _copy();
        }
        synchronized (monitor()) {
            xmlObject_copy = _copy();
        }
        return xmlObject_copy;
    }

    @Override // org.apache.xmlbeans.XmlObject
    public final XmlObject copy(XmlOptions options) {
        XmlObject xmlObject_copy;
        if (preCheck()) {
            return _copy(options);
        }
        synchronized (monitor()) {
            xmlObject_copy = _copy(options);
        }
        return xmlObject_copy;
    }

    private boolean preCheck() {
        if (has_store()) {
            return get_store().get_locale().noSync();
        }
        return false;
    }

    public final XmlObject _copy() {
        return _copy(null);
    }

    public final XmlObject _copy(XmlOptions xmlOptions) {
        if (isImmutable()) {
            return this;
        }
        check_orphaned();
        SchemaTypeLoader stl = get_store().get_schematypeloader();
        XmlObject result = (XmlObject) get_store().copy(stl, schemaType(), xmlOptions);
        return result;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XmlDocumentProperties documentProperties() {
        XmlCursor cur = newCursorForce();
        try {
            XmlDocumentProperties xmlDocumentPropertiesDocumentProperties = cur.documentProperties();
            cur.dispose();
            return xmlDocumentPropertiesDocumentProperties;
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLInputStream newXMLInputStream() {
        return newXMLInputStream(null);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLInputStream newXMLInputStream(XmlOptions options) {
        XmlCursor cur = newCursorForce();
        try {
            XMLInputStream xMLInputStreamNewXMLInputStream = cur.newXMLInputStream(makeInnerOptions(options));
            cur.dispose();
            return xMLInputStreamNewXMLInputStream;
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLStreamReader newXMLStreamReader() {
        return newXMLStreamReader(null);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLStreamReader newXMLStreamReader(XmlOptions options) {
        XmlCursor cur = newCursorForce();
        try {
            XMLStreamReader xMLStreamReaderNewXMLStreamReader = cur.newXMLStreamReader(makeInnerOptions(options));
            cur.dispose();
            return xMLStreamReaderNewXMLStreamReader;
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public InputStream newInputStream() {
        return newInputStream(null);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public InputStream newInputStream(XmlOptions options) {
        XmlCursor cur = newCursorForce();
        try {
            InputStream inputStreamNewInputStream = cur.newInputStream(makeInnerOptions(options));
            cur.dispose();
            return inputStreamNewInputStream;
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Reader newReader() {
        return newReader(null);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Reader newReader(XmlOptions options) {
        XmlCursor cur = newCursorForce();
        try {
            Reader readerNewReader = cur.newReader(makeInnerOptions(options));
            cur.dispose();
            return readerNewReader;
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Node getDomNode() {
        XmlCursor cur = newCursorForce();
        try {
            Node domNode = cur.getDomNode();
            cur.dispose();
            return domNode;
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Node newDomNode() {
        return newDomNode(null);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Node newDomNode(XmlOptions options) {
        XmlCursor cur = newCursorForce();
        try {
            Node nodeNewDomNode = cur.newDomNode(makeInnerOptions(options));
            cur.dispose();
            return nodeNewDomNode;
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(ContentHandler ch2, LexicalHandler lh, XmlOptions options) throws SAXException {
        XmlCursor cur = newCursorForce();
        try {
            cur.save(ch2, lh, makeInnerOptions(options));
            cur.dispose();
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(File file, XmlOptions options) throws IOException {
        XmlCursor cur = newCursorForce();
        try {
            cur.save(file, makeInnerOptions(options));
            cur.dispose();
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(OutputStream os, XmlOptions options) throws IOException {
        XmlCursor cur = newCursorForce();
        try {
            cur.save(os, makeInnerOptions(options));
            cur.dispose();
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(Writer w, XmlOptions options) throws IOException {
        XmlCursor cur = newCursorForce();
        try {
            cur.save(w, makeInnerOptions(options));
            cur.dispose();
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(ContentHandler ch2, LexicalHandler lh) throws SAXException {
        save(ch2, lh, null);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(File file) throws IOException {
        save(file, (XmlOptions) null);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(OutputStream os) throws IOException {
        save(os, (XmlOptions) null);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(Writer w) throws IOException {
        save(w, (XmlOptions) null);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void dump() {
        XmlCursor cur = newCursorForce();
        try {
            cur.dump();
            cur.dispose();
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    public XmlCursor newCursorForce() {
        XmlCursor xmlCursorNewCursor;
        synchronized (monitor()) {
            xmlCursorNewCursor = ensureStore().newCursor();
        }
        return xmlCursorNewCursor;
    }

    private XmlObject ensureStore() {
        String strCompute_text;
        if ((this._flags & 16) != 0) {
            return this;
        }
        check_dated();
        if ((this._flags & 64) != 0) {
            strCompute_text = "";
        } else {
            strCompute_text = compute_text(has_store() ? get_store() : null);
        }
        String value = strCompute_text;
        XmlOptions options = new XmlOptions().setDocumentType(schemaType());
        XmlObject x = XmlObject.Factory.newInstance(options);
        XmlCursor c = x.newCursor();
        c.toNextToken();
        c.insertChars(value);
        return x;
    }

    private static XmlOptions makeInnerOptions(XmlOptions options) {
        XmlOptions innerOptions = new XmlOptions(options);
        innerOptions.put(XmlOptions.SAVE_INNER);
        return innerOptions;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XmlCursor newCursor() {
        XmlCursor xmlCursorNew_cursor;
        if ((this._flags & 16) == 0) {
            throw new IllegalStateException("XML Value Objects cannot create cursors");
        }
        check_orphaned();
        XmlLocale l = getXmlLocale();
        if (l.noSync()) {
            l.enter();
            try {
                XmlCursor xmlCursorNew_cursor2 = get_store().new_cursor();
                l.exit();
                return xmlCursorNew_cursor2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                xmlCursorNew_cursor = get_store().new_cursor();
                l.exit();
            } finally {
            }
        }
        return xmlCursorNew_cursor;
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public SchemaType instanceType() {
        SchemaType schemaType;
        synchronized (monitor()) {
            schemaType = isNil() ? null : schemaType();
        }
        return schemaType;
    }

    private SchemaField schemaField() {
        SchemaType st = schemaType();
        SchemaField field = st.getContainerField();
        if (field == null) {
            field = get_store().get_schema_field();
        }
        return field;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlObjectBase$ValueOutOfRangeValidationContext.class */
    private static final class ValueOutOfRangeValidationContext implements ValidationContext {
        private ValueOutOfRangeValidationContext() {
        }

        @Override // org.apache.xmlbeans.impl.common.ValidationContext
        public void invalid(String message) {
            throw new XmlValueOutOfRangeException(message);
        }

        @Override // org.apache.xmlbeans.impl.common.ValidationContext
        public void invalid(String code, Object[] args) {
            throw new XmlValueOutOfRangeException(code, args);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlObjectBase$ImmutableValueValidationContext.class */
    private static final class ImmutableValueValidationContext implements ValidationContext {
        private XmlObject _loc;
        private Collection _coll;

        ImmutableValueValidationContext(Collection coll, XmlObject loc) {
            this._coll = coll;
            this._loc = loc;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidationContext
        public void invalid(String message) {
            this._coll.add(XmlError.forObject(message, this._loc));
        }

        @Override // org.apache.xmlbeans.impl.common.ValidationContext
        public void invalid(String code, Object[] args) {
            this._coll.add(XmlError.forObject(code, args, this._loc));
        }
    }

    @Override // org.apache.xmlbeans.XmlObject
    public boolean validate() {
        return validate(null);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public boolean validate(XmlOptions options) {
        boolean zIsValid;
        if ((this._flags & 16) == 0) {
            if ((this._flags & 4096) != 0) {
                return validate_immutable(options);
            }
            throw new IllegalStateException("XML objects with no underlying store cannot be validated");
        }
        synchronized (monitor()) {
            if ((this._flags & 2048) != 0) {
                throw new XmlValueDisconnectedException();
            }
            SchemaField field = schemaField();
            SchemaType type = schemaType();
            TypeStore typeStore = get_store();
            Validator validator = new Validator(type, field, typeStore.get_schematypeloader(), options, null);
            typeStore.validate(validator);
            zIsValid = validator.isValid();
        }
        return zIsValid;
    }

    private boolean validate_immutable(XmlOptions options) {
        Collection errorListener = options == null ? null : (Collection) options.get(XmlOptions.ERROR_LISTENER);
        XmlErrorWatcher watcher = new XmlErrorWatcher(errorListener);
        if (!schemaType().isSimpleType() && (options == null || !options.hasOption(XmlOptions.VALIDATE_TEXT_ONLY))) {
            SchemaProperty[] properties = schemaType().getProperties();
            for (int i = 0; i < properties.length; i++) {
                if (properties[i].getMinOccurs().signum() > 0) {
                    if (properties[i].isAttribute()) {
                        watcher.add(XmlError.forObject(XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$MISSING_REQUIRED_ATTRIBUTE, new Object[]{QNameHelper.pretty(properties[i].getName())}, this));
                    } else {
                        watcher.add(XmlError.forObject(XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$MISSING_ELEMENT, new Object[]{properties[i].getMinOccurs(), QNameHelper.pretty(properties[i].getName())}, this));
                    }
                }
            }
            if (schemaType().getContentType() != 2) {
                return !watcher.hasError();
            }
        }
        String text = (String) this._textsource;
        if (text == null) {
            text = "";
        }
        validate_simpleval(text, new ImmutableValueValidationContext(watcher, this));
        return !watcher.hasError();
    }

    protected void validate_simpleval(String lexical, ValidationContext ctx) {
    }

    private static XmlObject[] _typedArray(XmlObject[] input) {
        if (input.length == 0) {
            return input;
        }
        SchemaType commonType = input[0].schemaType();
        if (commonType.equals(XmlObject.type) || commonType.isNoType()) {
            return input;
        }
        for (int i = 1; i < input.length; i++) {
            if (input[i].schemaType().isNoType()) {
                return input;
            }
            commonType = commonType.getCommonBaseType(input[i].schemaType());
            if (commonType.equals(XmlObject.type)) {
                return input;
            }
        }
        Class javaClass = commonType.getJavaClass();
        while (true) {
            Class desiredClass = javaClass;
            if (desiredClass == null) {
                commonType = commonType.getBaseType();
                if (XmlObject.type.equals(commonType)) {
                    return input;
                }
                javaClass = commonType.getJavaClass();
            } else {
                XmlObject[] result = (XmlObject[]) Array.newInstance((Class<?>) desiredClass, input.length);
                System.arraycopy(input, 0, result, 0, input.length);
                return result;
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectPath(String path) {
        return selectPath(path, null);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectPath(String path, XmlOptions options) {
        XmlObject[] selections;
        XmlCursor c = newCursor();
        if (c == null) {
            throw new XmlValueDisconnectedException();
        }
        try {
            c.selectPath(path, options);
            if (!c.hasNextSelection()) {
                selections = EMPTY_RESULT;
            } else {
                selections = new XmlObject[c.getSelectionCount()];
                int i = 0;
                while (c.toNextSelection()) {
                    XmlObject object = c.getObject();
                    selections[i] = object;
                    if (object == null) {
                        if (c.toParent()) {
                            XmlObject object2 = c.getObject();
                            selections[i] = object2;
                            if (object2 == null) {
                            }
                        }
                        throw new XmlRuntimeException("Path must select only elements and attributes");
                    }
                    i++;
                }
            }
            return _typedArray(selections);
        } finally {
            c.dispose();
        }
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] execQuery(String path) {
        return execQuery(path, null);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] execQuery(String queryExpr, XmlOptions options) {
        XmlObject[] xmlObjectArr_typedArray;
        synchronized (monitor()) {
            TypeStore typeStore = get_store();
            if (typeStore == null) {
                throw new XmlRuntimeException("Cannot do XQuery on XML Value Objects");
            }
            try {
                xmlObjectArr_typedArray = _typedArray(typeStore.exec_query(queryExpr, options));
            } catch (XmlException e) {
                throw new XmlRuntimeException(e);
            }
        }
        return xmlObjectArr_typedArray;
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject changeType(SchemaType type) {
        XmlObject xmlObject;
        if (type == null) {
            throw new IllegalArgumentException("Invalid type (null)");
        }
        if ((this._flags & 16) == 0) {
            throw new IllegalStateException("XML Value Objects cannot have thier type changed");
        }
        synchronized (monitor()) {
            check_orphaned();
            xmlObject = (XmlObject) get_store().change_type(type);
        }
        return xmlObject;
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject substitute(QName name, SchemaType type) {
        XmlObject xmlObject;
        if (name == null) {
            throw new IllegalArgumentException("Invalid name (null)");
        }
        if (type == null) {
            throw new IllegalArgumentException("Invalid type (null)");
        }
        if ((this._flags & 16) == 0) {
            throw new IllegalStateException("XML Value Objects cannot be used with substitution");
        }
        synchronized (monitor()) {
            check_orphaned();
            xmlObject = (XmlObject) get_store().substitute(name, type);
        }
        return xmlObject;
    }

    protected XmlObjectBase() {
    }

    public void init_flags(SchemaProperty prop) {
        if (prop == null || prop.hasDefault() == 1 || prop.hasFixed() == 1 || prop.hasNillable() == 1) {
            return;
        }
        this._flags &= -8;
        this._flags |= (prop.hasDefault() == 0 ? 0 : 2) | (prop.hasFixed() == 0 ? 0 : 4) | (prop.hasNillable() == 0 ? 0 : 1) | 32768;
    }

    protected void initComplexType(boolean complexType, boolean complexContent) {
        this._flags |= (complexType ? 8192 : 0) | (complexContent ? 16384 : 0);
    }

    protected boolean _isComplexType() {
        return (this._flags & 8192) != 0;
    }

    protected boolean _isComplexContent() {
        return (this._flags & 16384) != 0;
    }

    public void setValidateOnSet() {
        this._flags |= 65536;
    }

    protected boolean _validateOnSet() {
        return (this._flags & 65536) != 0;
    }

    @Override // org.apache.xmlbeans.XmlObject
    public final boolean isNil() {
        boolean z;
        synchronized (monitor()) {
            check_dated();
            z = (this._flags & 64) != 0;
        }
        return z;
    }

    public final boolean isFixed() {
        check_element_dated();
        return (this._flags & 4) != 0;
    }

    public final boolean isNillable() {
        check_element_dated();
        return (this._flags & 1) != 0;
    }

    public final boolean isDefaultable() {
        check_element_dated();
        return (this._flags & 2) != 0;
    }

    public final boolean isDefault() {
        check_dated();
        return (this._flags & 256) != 0;
    }

    @Override // org.apache.xmlbeans.XmlObject
    public final void setNil() {
        synchronized (monitor()) {
            set_prepare();
            if ((this._flags & 1) == 0 && (this._flags & 65536) != 0) {
                throw new XmlValueNotNillableException();
            }
            set_nil();
            this._flags |= 64;
            if ((this._flags & 16) != 0) {
                get_store().invalidate_text();
                this._flags &= -673;
                get_store().invalidate_nil();
            } else {
                this._textsource = null;
            }
        }
    }

    protected int elementFlags() {
        check_element_dated();
        return this._flags & 7;
    }

    public void setImmutable() {
        if ((this._flags & 4112) != 0) {
            throw new IllegalStateException();
        }
        this._flags |= 4096;
    }

    @Override // org.apache.xmlbeans.XmlObject
    public boolean isImmutable() {
        return (this._flags & 4096) != 0;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public final void attach_store(TypeStore store) {
        this._textsource = store;
        if ((this._flags & 4096) != 0) {
            throw new IllegalStateException();
        }
        this._flags |= 688;
        if (store.is_attribute()) {
            this._flags |= 8;
        }
        if (store.validate_on_set()) {
            this._flags |= 65536;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public final void invalidate_value() {
        if (!$assertionsDisabled && (this._flags & 16) == 0) {
            throw new AssertionError();
        }
        this._flags |= 32;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public final boolean uses_invalidate_value() {
        SchemaType type = schemaType();
        return type.isSimpleType() || type.getContentType() == 2;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public final void invalidate_nilvalue() {
        if (!$assertionsDisabled && (this._flags & 16) == 0) {
            throw new AssertionError();
        }
        this._flags |= 160;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public final void invalidate_element_order() {
        if (!$assertionsDisabled && (this._flags & 16) == 0) {
            throw new AssertionError();
        }
        this._flags |= FLAGS_DATED;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public final TypeStore get_store() {
        if ($assertionsDisabled || (this._flags & 16) != 0) {
            return (TypeStore) this._textsource;
        }
        throw new AssertionError();
    }

    public final XmlLocale getXmlLocale() {
        return get_store().get_locale();
    }

    protected final boolean has_store() {
        return (this._flags & 16) != 0;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public final String build_text(NamespaceManager nsm) {
        if (!$assertionsDisabled && (this._flags & 16) == 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (this._flags & 32) != 0) {
            throw new AssertionError();
        }
        if ((this._flags & 320) != 0) {
            return "";
        }
        return compute_text(nsm == null ? has_store() ? get_store() : null : nsm);
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public boolean build_nil() {
        if (!$assertionsDisabled && (this._flags & 16) == 0) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || (this._flags & 32) == 0) {
            return (this._flags & 64) != 0;
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public void validate_now() {
        check_dated();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public void disconnect_store() {
        if (!$assertionsDisabled && (this._flags & 16) == 0) {
            throw new AssertionError();
        }
        this._flags |= Polynomial.SIGNATURE_III_SIZE;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public TypeStoreUser create_element_user(QName eltName, QName xsiType) {
        return (TypeStoreUser) ((SchemaTypeImpl) schemaType()).createElementType(eltName, xsiType, get_store().get_schematypeloader());
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public TypeStoreUser create_attribute_user(QName attrName) {
        return (TypeStoreUser) ((SchemaTypeImpl) schemaType()).createAttributeType(attrName, get_store().get_schematypeloader());
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public SchemaType get_schema_type() {
        return schemaType();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public SchemaType get_element_type(QName eltName, QName xsiType) {
        return schemaType().getElementType(eltName, xsiType, get_store().get_schematypeloader());
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public SchemaType get_attribute_type(QName attrName) {
        return schemaType().getAttributeType(attrName, get_store().get_schematypeloader());
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public String get_default_element_text(QName eltName) {
        if (!$assertionsDisabled && !_isComplexContent()) {
            throw new AssertionError();
        }
        if (!_isComplexContent()) {
            throw new IllegalStateException();
        }
        SchemaProperty prop = schemaType().getElementProperty(eltName);
        if (prop == null) {
            return "";
        }
        return prop.getDefaultText();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public String get_default_attribute_text(QName attrName) {
        if (!$assertionsDisabled && !_isComplexType()) {
            throw new AssertionError();
        }
        if (!_isComplexType()) {
            throw new IllegalStateException();
        }
        SchemaProperty prop = schemaType().getAttributeProperty(attrName);
        if (prop == null) {
            return "";
        }
        return prop.getDefaultText();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public int get_elementflags(QName eltName) {
        SchemaProperty prop;
        if (!_isComplexContent() || (prop = schemaType().getElementProperty(eltName)) == null) {
            return 0;
        }
        if (prop.hasDefault() == 1 || prop.hasFixed() == 1 || prop.hasNillable() == 1) {
            return -1;
        }
        return (prop.hasDefault() == 0 ? 0 : 2) | (prop.hasFixed() == 0 ? 0 : 4) | (prop.hasNillable() == 0 ? 0 : 1);
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public int get_attributeflags(QName attrName) {
        SchemaProperty prop;
        if (_isComplexType() && (prop = schemaType().getAttributeProperty(attrName)) != null) {
            return (prop.hasDefault() == 0 ? 0 : 2) | (prop.hasFixed() == 0 ? 0 : 4);
        }
        return 0;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public boolean is_child_element_order_sensitive() {
        if (!_isComplexType()) {
            return false;
        }
        return schemaType().isOrderSensitive();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public final QNameSet get_element_ending_delimiters(QName eltname) {
        SchemaProperty prop = schemaType().getElementProperty(eltname);
        if (prop == null) {
            return null;
        }
        return prop.getJavaSetterDelimiter();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public TypeStoreVisitor new_visitor() {
        if (!_isComplexContent()) {
            return null;
        }
        return new SchemaTypeVisitorImpl(schemaType().getContentModel());
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStoreUser
    public SchemaField get_attribute_field(QName attrName) {
        SchemaAttributeModel model = schemaType().getAttributeModel();
        if (model == null) {
            return null;
        }
        return model.getAttribute(attrName);
    }

    protected void set_String(String v) {
        if ((this._flags & 4096) != 0) {
            throw new IllegalStateException();
        }
        boolean wasNilled = (this._flags & 64) != 0;
        String wscanon = apply_wscanon(v);
        update_from_wscanon_text(wscanon);
        if ((this._flags & 16) != 0) {
            this._flags &= -33;
            if ((this._flags & 1024) == 0) {
                get_store().store_text(v);
            }
            if (wasNilled) {
                get_store().invalidate_nil();
                return;
            }
            return;
        }
        this._textsource = v;
    }

    protected void update_from_complex_content() {
        throw new XmlValueNotSupportedException("Complex content");
    }

    private final void update_from_wscanon_text(String v) {
        if ((this._flags & 2) != 0 && (this._flags & 1024) == 0 && (this._flags & 8) == 0 && v.equals("")) {
            String def = get_store().compute_default_text();
            if (def == null) {
                throw new XmlValueOutOfRangeException();
            }
            this._flags |= 1024;
            try {
                setStringValue(def);
                this._flags &= -1025;
                this._flags &= -65;
                this._flags |= 256;
                return;
            } catch (Throwable th) {
                this._flags &= -1025;
                throw th;
            }
        }
        set_text(v);
        this._flags &= -321;
    }

    protected boolean is_defaultable_ws(String v) {
        return true;
    }

    protected int get_wscanon_rule() {
        return 3;
    }

    private final String apply_wscanon(String v) {
        return XmlWhitespace.collapse(v, get_wscanon_rule());
    }

    private final void check_element_dated() {
        if ((this._flags & 512) != 0 && (this._flags & 32768) == 0) {
            if ((this._flags & 2048) != 0) {
                throw new XmlValueDisconnectedException();
            }
            int eltflags = get_store().compute_flags();
            this._flags &= -520;
            this._flags |= eltflags;
        }
        if ((this._flags & 32768) != 0) {
            this._flags &= -513;
        }
    }

    protected final boolean is_orphaned() {
        return (this._flags & 2048) != 0;
    }

    protected final void check_orphaned() {
        if (is_orphaned()) {
            throw new XmlValueDisconnectedException();
        }
    }

    public final void check_dated() {
        String text;
        if ((this._flags & FLAGS_DATED) != 0) {
            if ((this._flags & 2048) != 0) {
                throw new XmlValueDisconnectedException();
            }
            if (!$assertionsDisabled && (this._flags & 16) == 0) {
                throw new AssertionError();
            }
            check_element_dated();
            if ((this._flags & 512) != 0) {
                int eltflags = get_store().compute_flags();
                this._flags &= -520;
                this._flags |= eltflags;
            }
            boolean nilled = false;
            if ((this._flags & 128) != 0) {
                if (get_store().find_nil()) {
                    if ((this._flags & 1) == 0 && (this._flags & 65536) != 0) {
                        throw new XmlValueOutOfRangeException();
                    }
                    set_nil();
                    this._flags |= 64;
                    nilled = true;
                } else {
                    this._flags &= -65;
                }
                this._flags &= -129;
            }
            if (!nilled) {
                if ((this._flags & 16384) != 0 || (text = get_wscanon_text()) == null) {
                    update_from_complex_content();
                } else {
                    NamespaceContext.push(new NamespaceContext(get_store()));
                    try {
                        update_from_wscanon_text(text);
                        NamespaceContext.pop();
                    } catch (Throwable th) {
                        NamespaceContext.pop();
                        throw th;
                    }
                }
            }
            this._flags &= -33;
        }
    }

    private final void set_prepare() {
        check_element_dated();
        if ((this._flags & 4096) != 0) {
            throw new IllegalStateException();
        }
    }

    private final void set_commit() {
        boolean wasNilled = (this._flags & 64) != 0;
        this._flags &= -321;
        if ((this._flags & 16) != 0) {
            this._flags &= -673;
            get_store().invalidate_text();
            if (wasNilled) {
                get_store().invalidate_nil();
                return;
            }
            return;
        }
        this._textsource = null;
    }

    public final String get_wscanon_text() {
        if ((this._flags & 16) == 0) {
            return apply_wscanon((String) this._textsource);
        }
        return get_store().fetch_text(get_wscanon_rule());
    }

    public float getFloatValue() {
        BigDecimal bd = getBigDecimalValue();
        if (bd == null) {
            return 0.0f;
        }
        return bd.floatValue();
    }

    public double getDoubleValue() {
        BigDecimal bd = getBigDecimalValue();
        if (bd == null) {
            return 0.0d;
        }
        return bd.doubleValue();
    }

    public BigDecimal getBigDecimalValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), "numeric"});
    }

    public BigInteger getBigIntegerValue() {
        BigDecimal bd = bigDecimalValue();
        if (bd == null) {
            return null;
        }
        return bd.toBigInteger();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public byte getByteValue() {
        long l = getIntValue();
        if (l > 127) {
            throw new XmlValueOutOfRangeException();
        }
        if (l < -128) {
            throw new XmlValueOutOfRangeException();
        }
        return (byte) l;
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public short getShortValue() {
        long l = getIntValue();
        if (l > 32767) {
            throw new XmlValueOutOfRangeException();
        }
        if (l < -32768) {
            throw new XmlValueOutOfRangeException();
        }
        return (short) l;
    }

    public int getIntValue() {
        long l = getLongValue();
        if (l > 2147483647L) {
            throw new XmlValueOutOfRangeException();
        }
        if (l < -2147483648L) {
            throw new XmlValueOutOfRangeException();
        }
        return (int) l;
    }

    public long getLongValue() {
        BigInteger b = getBigIntegerValue();
        if (b == null) {
            return 0L;
        }
        if (b.compareTo(_max) >= 0) {
            throw new XmlValueOutOfRangeException();
        }
        if (b.compareTo(_min) <= 0) {
            throw new XmlValueOutOfRangeException();
        }
        return b.longValue();
    }

    static final XmlOptions buildInnerPrettyOptions() {
        XmlOptions options = new XmlOptions();
        options.put(XmlOptions.SAVE_INNER);
        options.put(XmlOptions.SAVE_PRETTY_PRINT);
        options.put(XmlOptions.SAVE_AGGRESSIVE_NAMESPACES);
        options.put(XmlOptions.SAVE_USE_DEFAULT_NAMESPACE);
        return options;
    }

    @Override // org.apache.xmlbeans.XmlObject
    public final String toString() {
        String strXmlText;
        synchronized (monitor()) {
            strXmlText = ensureStore().xmlText(_toStringOptions);
        }
        return strXmlText;
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public String xmlText() {
        return xmlText(null);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public String xmlText(XmlOptions options) {
        XmlCursor cur = newCursorForce();
        try {
            String strXmlText = cur.xmlText(makeInnerOptions(options));
            cur.dispose();
            return strXmlText;
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    public StringEnumAbstractBase getEnumValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), "enum"});
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public String getStringValue() {
        if (isImmutable()) {
            if ((this._flags & 64) != 0) {
                return null;
            }
            return compute_text(null);
        }
        synchronized (monitor()) {
            if (_isComplexContent()) {
                return get_store().fetch_text(1);
            }
            check_dated();
            if ((this._flags & 64) != 0) {
                return null;
            }
            return compute_text(has_store() ? get_store() : null);
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public String stringValue() {
        return getStringValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public boolean booleanValue() {
        return getBooleanValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public byte byteValue() {
        return getByteValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public short shortValue() {
        return getShortValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public int intValue() {
        return getIntValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public long longValue() {
        return getLongValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public BigInteger bigIntegerValue() {
        return getBigIntegerValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public BigDecimal bigDecimalValue() {
        return getBigDecimalValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public float floatValue() {
        return getFloatValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public double doubleValue() {
        return getDoubleValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public byte[] byteArrayValue() {
        return getByteArrayValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public StringEnumAbstractBase enumValue() {
        return getEnumValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public Calendar calendarValue() {
        return getCalendarValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public Date dateValue() {
        return getDateValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public GDate gDateValue() {
        return getGDateValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public GDuration gDurationValue() {
        return getGDurationValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public QName qNameValue() {
        return getQNameValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public List xlistValue() {
        return xgetListValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public List listValue() {
        return getListValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public Object objectValue() {
        return getObjectValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(String obj) {
        setStringValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(boolean v) {
        setBooleanValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(byte v) {
        setByteValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(short v) {
        setShortValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(int v) {
        setIntValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(long v) {
        setLongValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(BigInteger obj) {
        setBigIntegerValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(BigDecimal obj) {
        setBigDecimalValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(float v) {
        setFloatValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(double v) {
        setDoubleValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(byte[] obj) {
        setByteArrayValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(StringEnumAbstractBase obj) {
        setEnumValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(Calendar obj) {
        setCalendarValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(Date obj) {
        setDateValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(GDateSpecification obj) {
        setGDateValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(GDurationSpecification obj) {
        setGDurationValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(QName obj) {
        setQNameValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(List obj) {
        setListValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void objectSet(Object obj) {
        setObjectValue(obj);
    }

    public byte[] getByteArrayValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), "byte[]"});
    }

    public boolean getBooleanValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), "boolean"});
    }

    public GDate getGDateValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), "Date"});
    }

    public Date getDateValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), "Date"});
    }

    public Calendar getCalendarValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), "Calendar"});
    }

    public GDuration getGDurationValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), "Duration"});
    }

    public QName getQNameValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), XmlErrorCodes.QNAME});
    }

    public List getListValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), "List"});
    }

    public List xgetListValue() {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_S2J, new Object[]{getPrimitiveTypeName(), "List"});
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public Object getObjectValue() {
        return java_value(this);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setBooleanValue(boolean v) {
        synchronized (monitor()) {
            set_prepare();
            set_boolean(v);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setByteValue(byte v) {
        synchronized (monitor()) {
            set_prepare();
            set_byte(v);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setShortValue(short v) {
        synchronized (monitor()) {
            set_prepare();
            set_short(v);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setIntValue(int v) {
        synchronized (monitor()) {
            set_prepare();
            set_int(v);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setLongValue(long v) {
        synchronized (monitor()) {
            set_prepare();
            set_long(v);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setFloatValue(float v) {
        synchronized (monitor()) {
            set_prepare();
            set_float(v);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setDoubleValue(double v) {
        synchronized (monitor()) {
            set_prepare();
            set_double(v);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setByteArrayValue(byte[] obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_ByteArray(obj);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setEnumValue(StringEnumAbstractBase obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_enum(obj);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setBigIntegerValue(BigInteger obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_BigInteger(obj);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setBigDecimalValue(BigDecimal obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_BigDecimal(obj);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setCalendarValue(Calendar obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_Calendar(obj);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setDateValue(Date obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_Date(obj);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setGDateValue(GDate obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_GDate(obj);
            set_commit();
        }
    }

    public final void setGDateValue(GDateSpecification obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_GDate(obj);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setGDurationValue(GDuration obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_GDuration(obj);
            set_commit();
        }
    }

    public final void setGDurationValue(GDurationSpecification obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_GDuration(obj);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setQNameValue(QName obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_QName(obj);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setListValue(List obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_list(obj);
            set_commit();
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public final void setStringValue(String obj) {
        if (obj == null) {
            setNil();
            return;
        }
        synchronized (monitor()) {
            set_prepare();
            set_String(obj);
        }
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setObjectValue(Object o) {
        if (o == null) {
            setNil();
            return;
        }
        if (o instanceof XmlObject) {
            set((XmlObject) o);
            return;
        }
        if (o instanceof String) {
            setStringValue((String) o);
            return;
        }
        if (o instanceof StringEnumAbstractBase) {
            setEnumValue((StringEnumAbstractBase) o);
            return;
        }
        if (o instanceof BigInteger) {
            setBigIntegerValue((BigInteger) o);
            return;
        }
        if (o instanceof BigDecimal) {
            setBigDecimalValue((BigDecimal) o);
            return;
        }
        if (o instanceof Byte) {
            setByteValue(((Byte) o).byteValue());
            return;
        }
        if (o instanceof Short) {
            setShortValue(((Short) o).shortValue());
            return;
        }
        if (o instanceof Integer) {
            setIntValue(((Integer) o).intValue());
            return;
        }
        if (o instanceof Long) {
            setLongValue(((Long) o).longValue());
            return;
        }
        if (o instanceof Boolean) {
            setBooleanValue(((Boolean) o).booleanValue());
            return;
        }
        if (o instanceof Float) {
            setFloatValue(((Float) o).floatValue());
            return;
        }
        if (o instanceof Double) {
            setDoubleValue(((Double) o).doubleValue());
            return;
        }
        if (o instanceof Calendar) {
            setCalendarValue((Calendar) o);
            return;
        }
        if (o instanceof Date) {
            setDateValue((Date) o);
            return;
        }
        if (o instanceof GDateSpecification) {
            setGDateValue((GDateSpecification) o);
            return;
        }
        if (o instanceof GDurationSpecification) {
            setGDurationValue((GDurationSpecification) o);
            return;
        }
        if (o instanceof QName) {
            setQNameValue((QName) o);
        } else if (o instanceof List) {
            setListValue((List) o);
        } else {
            if (o instanceof byte[]) {
                setByteArrayValue((byte[]) o);
                return;
            }
            throw new XmlValueNotSupportedException("Can't set union object of class : " + o.getClass().getName());
        }
    }

    public final void set_newValue(XmlObject obj) {
        if (obj == null || obj.isNil()) {
            setNil();
            return;
        }
        if (obj instanceof XmlAnySimpleType) {
            XmlAnySimpleType v = (XmlAnySimpleType) obj;
            SchemaType instanceType = ((SimpleValue) v).instanceType();
            if (!$assertionsDisabled && instanceType == null) {
                throw new AssertionError("Nil case should have been handled already");
            }
            if (instanceType.getSimpleVariety() == 3) {
                synchronized (monitor()) {
                    set_prepare();
                    set_list(((SimpleValue) v).xgetListValue());
                    set_commit();
                }
                return;
            }
            synchronized (monitor()) {
                if (!$assertionsDisabled && instanceType.getSimpleVariety() != 1) {
                    throw new AssertionError();
                }
                switch (instanceType.getPrimitiveType().getBuiltinTypeCode()) {
                    case 2:
                        boolean pushed = false;
                        if (!v.isImmutable()) {
                            pushed = true;
                            NamespaceContext.push(new NamespaceContext(v));
                        }
                        try {
                            set_prepare();
                            set_xmlanysimple(v);
                            if (pushed) {
                                NamespaceContext.pop();
                            }
                            set_commit();
                            return;
                        } catch (Throwable th) {
                            if (pushed) {
                                NamespaceContext.pop();
                            }
                            throw th;
                        }
                    case 3:
                        boolean bool = ((SimpleValue) v).getBooleanValue();
                        set_prepare();
                        set_boolean(bool);
                        set_commit();
                        return;
                    case 4:
                        byte[] byteArr = ((SimpleValue) v).getByteArrayValue();
                        set_prepare();
                        set_b64(byteArr);
                        set_commit();
                        return;
                    case 5:
                        byte[] byteArr2 = ((SimpleValue) v).getByteArrayValue();
                        set_prepare();
                        set_hex(byteArr2);
                        set_commit();
                        return;
                    case 6:
                        String uri = v.getStringValue();
                        set_prepare();
                        set_text(uri);
                        set_commit();
                        return;
                    case 7:
                        QName name = ((SimpleValue) v).getQNameValue();
                        set_prepare();
                        set_QName(name);
                        set_commit();
                        return;
                    case 8:
                        String s = v.getStringValue();
                        set_prepare();
                        set_notation(s);
                        set_commit();
                        return;
                    case 9:
                        float f = ((SimpleValue) v).getFloatValue();
                        set_prepare();
                        set_float(f);
                        set_commit();
                        return;
                    case 10:
                        double d = ((SimpleValue) v).getDoubleValue();
                        set_prepare();
                        set_double(d);
                        set_commit();
                        return;
                    case 11:
                        switch (instanceType.getDecimalSize()) {
                            case 8:
                                byte b = ((SimpleValue) v).getByteValue();
                                set_prepare();
                                set_byte(b);
                                set_commit();
                                return;
                            case 16:
                                short s2 = ((SimpleValue) v).getShortValue();
                                set_prepare();
                                set_short(s2);
                                set_commit();
                                return;
                            case 32:
                                int i = ((SimpleValue) v).getIntValue();
                                set_prepare();
                                set_int(i);
                                set_commit();
                                return;
                            case 64:
                                long l = ((SimpleValue) v).getLongValue();
                                set_prepare();
                                set_long(l);
                                set_commit();
                                return;
                            case SchemaType.SIZE_BIG_INTEGER /* 1000000 */:
                                BigInteger bi = ((SimpleValue) v).getBigIntegerValue();
                                set_prepare();
                                set_BigInteger(bi);
                                set_commit();
                                return;
                            default:
                                if (!$assertionsDisabled) {
                                    throw new AssertionError("invalid numeric bit count");
                                }
                            case 1000001:
                                BigDecimal bd = ((SimpleValue) v).getBigDecimalValue();
                                set_prepare();
                                set_BigDecimal(bd);
                                set_commit();
                                return;
                        }
                    case 12:
                        String s3 = v.getStringValue();
                        set_prepare();
                        set_String(s3);
                        set_commit();
                        return;
                    case 13:
                        GDuration gd = ((SimpleValue) v).getGDurationValue();
                        set_prepare();
                        set_GDuration(gd);
                        set_commit();
                        return;
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                        GDate gd2 = ((SimpleValue) v).getGDateValue();
                        set_prepare();
                        set_GDate(gd2);
                        set_commit();
                        return;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError("encountered nonprimitive type.");
                        }
                        break;
                }
            }
        }
        throw new IllegalStateException("Complex type unexpected");
    }

    private TypeStoreUser setterHelper(XmlObjectBase src) {
        check_orphaned();
        src.check_orphaned();
        return get_store().copy_contents_from(src.get_store()).get_store().change_type(src.schemaType());
    }

    @Override // org.apache.xmlbeans.XmlObject
    public final XmlObject set(XmlObject src) {
        if (isImmutable()) {
            throw new IllegalStateException("Cannot set the value of an immutable XmlObject");
        }
        XmlObjectBase obj = underlying(src);
        TypeStoreUser newObj = this;
        if (obj == null) {
            setNil();
            return this;
        }
        if (obj.isImmutable()) {
            setStringValue(obj.getStringValue());
        } else {
            boolean noSyncThis = preCheck();
            boolean noSyncObj = obj.preCheck();
            if (monitor() == obj.monitor()) {
                if (noSyncThis) {
                    newObj = setterHelper(obj);
                } else {
                    synchronized (monitor()) {
                        newObj = setterHelper(obj);
                    }
                }
            } else if (noSyncThis) {
                if (noSyncObj) {
                    newObj = setterHelper(obj);
                } else {
                    synchronized (obj.monitor()) {
                        newObj = setterHelper(obj);
                    }
                }
            } else if (noSyncObj) {
                synchronized (monitor()) {
                    newObj = setterHelper(obj);
                }
            } else {
                boolean acquired = false;
                try {
                    try {
                        GlobalLock.acquire();
                        synchronized (monitor()) {
                            synchronized (obj.monitor()) {
                                GlobalLock.release();
                                acquired = false;
                                newObj = setterHelper(obj);
                            }
                        }
                    } catch (InterruptedException e) {
                        throw new XmlRuntimeException(e);
                    }
                } finally {
                    if (acquired) {
                        GlobalLock.release();
                    }
                }
            }
        }
        return (XmlObject) newObj;
    }

    public final XmlObject generatedSetterHelperImpl(XmlObject src, QName propName, int index, short kindSetterHelper) {
        XmlObject xmlObject;
        XmlObject xmlObject2;
        XmlObject xmlObject3;
        XmlObject xmlObject4;
        XmlObjectBase target;
        XmlObjectBase target2;
        XmlObjectBase srcObj = underlying(src);
        if (srcObj == null) {
            synchronized (monitor()) {
                target2 = getTargetForSetter(propName, index, kindSetterHelper);
                target2.setNil();
            }
            return target2;
        }
        if (srcObj.isImmutable()) {
            synchronized (monitor()) {
                target = getTargetForSetter(propName, index, kindSetterHelper);
                target.setStringValue(srcObj.getStringValue());
            }
            return target;
        }
        boolean noSyncThis = preCheck();
        boolean noSyncObj = srcObj.preCheck();
        if (monitor() == srcObj.monitor()) {
            if (noSyncThis) {
                return (XmlObject) objSetterHelper(srcObj, propName, index, kindSetterHelper);
            }
            synchronized (monitor()) {
                xmlObject4 = (XmlObject) objSetterHelper(srcObj, propName, index, kindSetterHelper);
            }
            return xmlObject4;
        }
        if (noSyncThis) {
            if (noSyncObj) {
                return (XmlObject) objSetterHelper(srcObj, propName, index, kindSetterHelper);
            }
            synchronized (srcObj.monitor()) {
                xmlObject3 = (XmlObject) objSetterHelper(srcObj, propName, index, kindSetterHelper);
            }
            return xmlObject3;
        }
        if (noSyncObj) {
            synchronized (monitor()) {
                xmlObject2 = (XmlObject) objSetterHelper(srcObj, propName, index, kindSetterHelper);
            }
            return xmlObject2;
        }
        boolean acquired = false;
        try {
            try {
                GlobalLock.acquire();
                synchronized (monitor()) {
                    synchronized (srcObj.monitor()) {
                        GlobalLock.release();
                        acquired = false;
                        xmlObject = (XmlObject) objSetterHelper(srcObj, propName, index, kindSetterHelper);
                    }
                }
                return xmlObject;
            } catch (InterruptedException e) {
                throw new XmlRuntimeException(e);
            }
        } finally {
            if (acquired) {
                GlobalLock.release();
            }
        }
    }

    private TypeStoreUser objSetterHelper(XmlObjectBase srcObj, QName propName, int index, short kindSetterHelper) {
        XmlObjectBase target = getTargetForSetter(propName, index, kindSetterHelper);
        target.check_orphaned();
        srcObj.check_orphaned();
        return target.get_store().copy_contents_from(srcObj.get_store()).get_store().change_type(srcObj.schemaType());
    }

    private XmlObjectBase getTargetForSetter(QName propName, int index, short kindSetterHelper) {
        switch (kindSetterHelper) {
            case 1:
                check_orphaned();
                XmlObjectBase target = (XmlObjectBase) get_store().find_element_user(propName, index);
                if (target == null) {
                    target = (XmlObjectBase) get_store().add_element_user(propName);
                }
                if (target.isImmutable()) {
                    throw new IllegalStateException("Cannot set the value of an immutable XmlObject");
                }
                return target;
            case 2:
                check_orphaned();
                XmlObjectBase target2 = (XmlObjectBase) get_store().find_element_user(propName, index);
                if (target2 == null) {
                    throw new IndexOutOfBoundsException();
                }
                if (target2.isImmutable()) {
                    throw new IllegalStateException("Cannot set the value of an immutable XmlObject");
                }
                return target2;
            default:
                throw new IllegalArgumentException("Unknown kindSetterHelper: " + ((int) kindSetterHelper));
        }
    }

    public final XmlObject _set(XmlObject src) {
        if (isImmutable()) {
            throw new IllegalStateException("Cannot set the value of an immutable XmlObject");
        }
        XmlObjectBase obj = underlying(src);
        TypeStoreUser newObj = this;
        if (obj == null) {
            setNil();
            return this;
        }
        if (obj.isImmutable()) {
            set(obj.stringValue());
        } else {
            check_orphaned();
            obj.check_orphaned();
            newObj = get_store().copy_contents_from(obj.get_store()).get_store().change_type(obj.schemaType());
        }
        return (XmlObject) newObj;
    }

    protected void set_list(List list) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{"List", getPrimitiveTypeName()});
    }

    protected void set_boolean(boolean v) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{"boolean", getPrimitiveTypeName()});
    }

    protected void set_byte(byte v) {
        set_int(v);
    }

    protected void set_short(short v) {
        set_int(v);
    }

    protected void set_int(int v) {
        set_long(v);
    }

    protected void set_long(long v) {
        set_BigInteger(BigInteger.valueOf(v));
    }

    protected void set_char(char v) {
        set_String(Character.toString(v));
    }

    protected void set_float(float v) {
        set_BigDecimal(new BigDecimal(v));
    }

    protected void set_double(double v) {
        set_BigDecimal(new BigDecimal(v));
    }

    protected void set_enum(StringEnumAbstractBase e) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{"enum", getPrimitiveTypeName()});
    }

    protected void set_ByteArray(byte[] b) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{"byte[]", getPrimitiveTypeName()});
    }

    protected void set_b64(byte[] b) {
        set_ByteArray(b);
    }

    protected void set_hex(byte[] b) {
        set_ByteArray(b);
    }

    protected void set_BigInteger(BigInteger v) {
        set_BigDecimal(new BigDecimal(v));
    }

    protected void set_BigDecimal(BigDecimal v) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{"numeric", getPrimitiveTypeName()});
    }

    protected void set_Date(Date v) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{"Date", getPrimitiveTypeName()});
    }

    protected void set_Calendar(Calendar v) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{"Calendar", getPrimitiveTypeName()});
    }

    protected void set_GDate(GDateSpecification v) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{"Date", getPrimitiveTypeName()});
    }

    protected void set_GDuration(GDurationSpecification v) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{"Duration", getPrimitiveTypeName()});
    }

    protected void set_ComplexXml(XmlObject v) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{"complex content", getPrimitiveTypeName()});
    }

    protected void set_QName(QName v) {
        throw new XmlValueNotSupportedException(XmlErrorCodes.EXCEPTION_VALUE_NOT_SUPPORTED_J2S, new Object[]{XmlErrorCodes.QNAME, getPrimitiveTypeName()});
    }

    protected void set_notation(String v) {
        throw new XmlValueNotSupportedException();
    }

    protected void set_xmlanysimple(XmlAnySimpleType v) {
        set_String(v.getStringValue());
    }

    private final String getPrimitiveTypeName() {
        SchemaType type = schemaType();
        if (type.isNoType()) {
            return "unknown";
        }
        SchemaType t = type.getPrimitiveType();
        if (t == null) {
            return "complex";
        }
        return t.getName().getLocalPart();
    }

    private final boolean comparable_value_spaces(SchemaType t1, SchemaType t2) {
        if (!$assertionsDisabled && (t1.getSimpleVariety() == 2 || t2.getSimpleVariety() == 2)) {
            throw new AssertionError();
        }
        if (!t1.isSimpleType() && !t2.isSimpleType()) {
            return t1.getContentType() == t2.getContentType();
        }
        if (!t1.isSimpleType() || !t2.isSimpleType()) {
            return false;
        }
        if (t1.getSimpleVariety() == 3 && t2.getSimpleVariety() == 3) {
            return true;
        }
        if (t1.getSimpleVariety() == 3 || t2.getSimpleVariety() == 3) {
            return false;
        }
        return t1.getPrimitiveType().equals(t2.getPrimitiveType());
    }

    private final boolean valueEqualsImpl(XmlObject xmlobj) {
        check_dated();
        SchemaType typethis = instanceType();
        SchemaType typeother = ((SimpleValue) xmlobj).instanceType();
        if (typethis == null && typeother == null) {
            return true;
        }
        if (typethis == null || typeother == null || !comparable_value_spaces(typethis, typeother)) {
            return false;
        }
        if (xmlobj.schemaType().getSimpleVariety() == 2) {
            return underlying(xmlobj).equal_to(this);
        }
        return equal_to(xmlobj);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public final boolean valueEquals(XmlObject xmlobj) {
        boolean zValueEqualsImpl;
        boolean zValueEqualsImpl2;
        boolean zValueEqualsImpl3;
        try {
            try {
                if (isImmutable()) {
                    if (xmlobj.isImmutable()) {
                        boolean zValueEqualsImpl4 = valueEqualsImpl(xmlobj);
                        if (0 != 0) {
                            GlobalLock.release();
                        }
                        return zValueEqualsImpl4;
                    }
                    synchronized (xmlobj.monitor()) {
                        zValueEqualsImpl3 = valueEqualsImpl(xmlobj);
                    }
                    return zValueEqualsImpl3;
                }
                if (xmlobj.isImmutable() || monitor() == xmlobj.monitor()) {
                    synchronized (monitor()) {
                        zValueEqualsImpl = valueEqualsImpl(xmlobj);
                    }
                    if (0 != 0) {
                        GlobalLock.release();
                    }
                    return zValueEqualsImpl;
                }
                GlobalLock.acquire();
                synchronized (monitor()) {
                    synchronized (xmlobj.monitor()) {
                        GlobalLock.release();
                        zValueEqualsImpl2 = valueEqualsImpl(xmlobj);
                    }
                }
                if (0 != 0) {
                    GlobalLock.release();
                }
                return zValueEqualsImpl2;
            } catch (InterruptedException e) {
                throw new XmlRuntimeException(e);
            }
        } finally {
            if (0 != 0) {
                GlobalLock.release();
            }
        }
    }

    @Override // org.apache.xmlbeans.XmlObject
    public final int compareTo(Object obj) {
        int result = compareValue((XmlObject) obj);
        if (result == 2) {
            throw new ClassCastException();
        }
        return result;
    }

    private final int compareValueImpl(XmlObject xmlobj) {
        try {
            SchemaType type1 = instanceType();
            SchemaType type2 = ((SimpleValue) xmlobj).instanceType();
            if (type1 == null && type2 == null) {
                return 0;
            }
            if (type1 == null || type2 == null || !type1.isSimpleType() || type1.isURType() || !type2.isSimpleType() || type2.isURType()) {
                return 2;
            }
            if (type1.getPrimitiveType().getBuiltinTypeCode() != type2.getPrimitiveType().getBuiltinTypeCode()) {
                return 2;
            }
            return compare_to(xmlobj);
        } catch (XmlValueOutOfRangeException e) {
            return 2;
        }
    }

    @Override // org.apache.xmlbeans.XmlObject
    public final int compareValue(XmlObject xmlobj) {
        int iCompareValueImpl;
        int iCompareValueImpl2;
        int iCompareValueImpl3;
        if (xmlobj == null) {
            return 2;
        }
        try {
            try {
                if (isImmutable()) {
                    if (xmlobj.isImmutable()) {
                        int iCompareValueImpl4 = compareValueImpl(xmlobj);
                        if (0 != 0) {
                            GlobalLock.release();
                        }
                        return iCompareValueImpl4;
                    }
                    synchronized (xmlobj.monitor()) {
                        iCompareValueImpl3 = compareValueImpl(xmlobj);
                    }
                    return iCompareValueImpl3;
                }
                if (xmlobj.isImmutable() || monitor() == xmlobj.monitor()) {
                    synchronized (monitor()) {
                        iCompareValueImpl = compareValueImpl(xmlobj);
                    }
                    if (0 != 0) {
                        GlobalLock.release();
                    }
                    return iCompareValueImpl;
                }
                GlobalLock.acquire();
                synchronized (monitor()) {
                    synchronized (xmlobj.monitor()) {
                        GlobalLock.release();
                        iCompareValueImpl2 = compareValueImpl(xmlobj);
                    }
                }
                if (0 != 0) {
                    GlobalLock.release();
                }
                return iCompareValueImpl2;
            } catch (InterruptedException e) {
                throw new XmlRuntimeException(e);
            }
        } finally {
            if (0 != 0) {
                GlobalLock.release();
            }
        }
    }

    protected int compare_to(XmlObject xmlobj) {
        if (equal_to(xmlobj)) {
            return 0;
        }
        return 2;
    }

    @Override // org.apache.xmlbeans.XmlObject
    public int valueHashCode() {
        int iValue_hash_code;
        synchronized (monitor()) {
            iValue_hash_code = value_hash_code();
        }
        return iValue_hash_code;
    }

    public boolean isInstanceOf(SchemaType type) {
        if (type.getSimpleVariety() != 2) {
            SchemaType schemaTypeInstanceType = instanceType();
            while (true) {
                SchemaType myType = schemaTypeInstanceType;
                if (myType != null) {
                    if (type != myType) {
                        schemaTypeInstanceType = myType.getBaseType();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            Set ctypes = new HashSet(Arrays.asList(type.getUnionConstituentTypes()));
            SchemaType schemaTypeInstanceType2 = instanceType();
            while (true) {
                SchemaType myType2 = schemaTypeInstanceType2;
                if (myType2 != null) {
                    if (!ctypes.contains(myType2)) {
                        schemaTypeInstanceType2 = myType2.getBaseType();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    public final boolean equals(Object obj) {
        if (!isImmutable()) {
            return super.equals(obj);
        }
        if (!(obj instanceof XmlObject)) {
            return false;
        }
        XmlObject xmlobj = (XmlObject) obj;
        if (!xmlobj.isImmutable()) {
            return false;
        }
        return valueEquals(xmlobj);
    }

    public final int hashCode() {
        if (!isImmutable()) {
            return super.hashCode();
        }
        synchronized (monitor()) {
            if (isNil()) {
                return 0;
            }
            return value_hash_code();
        }
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectChildren(QName elementName) {
        XmlCursor xc = newCursor();
        try {
            if (!xc.isContainer()) {
                XmlObject[] xmlObjectArr = EMPTY_RESULT;
                xc.dispose();
                return xmlObjectArr;
            }
            List result = new ArrayList();
            if (xc.toChild(elementName)) {
                do {
                    result.add(xc.getObject());
                } while (xc.toNextSibling(elementName));
            }
            if (result.size() == 0) {
                XmlObject[] xmlObjectArr2 = EMPTY_RESULT;
                xc.dispose();
                return xmlObjectArr2;
            }
            XmlObject[] xmlObjectArr3 = (XmlObject[]) result.toArray(EMPTY_RESULT);
            xc.dispose();
            return xmlObjectArr3;
        } catch (Throwable th) {
            xc.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectChildren(String elementUri, String elementLocalName) {
        return selectChildren(new QName(elementUri, elementLocalName));
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectChildren(QNameSet elementNameSet) {
        if (elementNameSet == null) {
            throw new IllegalArgumentException();
        }
        XmlCursor xc = newCursor();
        try {
            if (!xc.isContainer()) {
                XmlObject[] xmlObjectArr = EMPTY_RESULT;
                xc.dispose();
                return xmlObjectArr;
            }
            List result = new ArrayList();
            if (xc.toFirstChild()) {
                do {
                    if (!$assertionsDisabled && !xc.isContainer()) {
                        throw new AssertionError();
                    }
                    if (elementNameSet.contains(xc.getName())) {
                        result.add(xc.getObject());
                    }
                } while (xc.toNextSibling());
            }
            if (result.size() == 0) {
                XmlObject[] xmlObjectArr2 = EMPTY_RESULT;
                xc.dispose();
                return xmlObjectArr2;
            }
            XmlObject[] xmlObjectArr3 = (XmlObject[]) result.toArray(EMPTY_RESULT);
            xc.dispose();
            return xmlObjectArr3;
        } catch (Throwable th) {
            xc.dispose();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject selectAttribute(QName attributeName) {
        XmlCursor xc = newCursor();
        try {
            if (!xc.isContainer()) {
                return null;
            }
            if (xc.toFirstAttribute()) {
                while (!xc.getName().equals(attributeName)) {
                    if (!xc.toNextAttribute()) {
                    }
                }
                XmlObject object = xc.getObject();
                xc.dispose();
                return object;
            }
            xc.dispose();
            return null;
        } finally {
            xc.dispose();
        }
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject selectAttribute(String attributeUri, String attributeLocalName) {
        return selectAttribute(new QName(attributeUri, attributeLocalName));
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectAttributes(QNameSet attributeNameSet) {
        if (attributeNameSet == null) {
            throw new IllegalArgumentException();
        }
        XmlCursor xc = newCursor();
        try {
            if (!xc.isContainer()) {
                XmlObject[] xmlObjectArr = EMPTY_RESULT;
                xc.dispose();
                return xmlObjectArr;
            }
            List result = new ArrayList();
            if (xc.toFirstAttribute()) {
                do {
                    if (attributeNameSet.contains(xc.getName())) {
                        result.add(xc.getObject());
                    }
                } while (xc.toNextAttribute());
            }
            if (result.size() == 0) {
                XmlObject[] xmlObjectArr2 = EMPTY_RESULT;
                xc.dispose();
                return xmlObjectArr2;
            }
            XmlObject[] xmlObjectArr3 = (XmlObject[]) result.toArray(EMPTY_RESULT);
            xc.dispose();
            return xmlObjectArr3;
        } catch (Throwable th) {
            xc.dispose();
            throw th;
        }
    }

    public Object writeReplace() {
        synchronized (monitor()) {
            if (isRootXmlObject()) {
                return new SerializedRootObject(this);
            }
            return new SerializedInteriorObject(this, getRootXmlObject());
        }
    }

    private boolean isRootXmlObject() {
        XmlCursor cur = newCursor();
        if (cur == null) {
            return false;
        }
        boolean result = !cur.toParent();
        cur.dispose();
        return result;
    }

    private XmlObject getRootXmlObject() {
        XmlCursor cur = newCursor();
        if (cur == null) {
            return this;
        }
        cur.toStartDoc();
        XmlObject result = cur.getObject();
        cur.dispose();
        return result;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlObjectBase$SerializedRootObject.class */
    private static class SerializedRootObject implements Serializable {
        private static final long serialVersionUID = 1;
        transient Class _xbeanClass;
        transient XmlObject _impl;

        private SerializedRootObject() {
        }

        private SerializedRootObject(XmlObject impl) {
            this._xbeanClass = impl.schemaType().getJavaClass();
            this._impl = impl;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(this._xbeanClass);
            out.writeShort(0);
            out.writeShort(1);
            out.writeShort(1);
            String xmlText = this._impl.xmlText();
            out.writeObject(xmlText);
            out.writeBoolean(false);
        }

        private void readObject(ObjectInputStream in) throws IOException {
            String xmlText;
            try {
                this._xbeanClass = (Class) in.readObject();
                int utfBytes = in.readUnsignedShort();
                int majorVersionNum = 0;
                int minorVersionNum = 0;
                if (utfBytes == 0) {
                    majorVersionNum = in.readUnsignedShort();
                    minorVersionNum = in.readUnsignedShort();
                }
                switch (majorVersionNum) {
                    case 0:
                        xmlText = readObjectV0(in, utfBytes);
                        in.readBoolean();
                        break;
                    case 1:
                        switch (minorVersionNum) {
                            case 1:
                                xmlText = (String) in.readObject();
                                in.readBoolean();
                                break;
                            default:
                                throw new IOException("Deserialization error: version number " + majorVersionNum + "." + minorVersionNum + " not supported.");
                        }
                    default:
                        throw new IOException("Deserialization error: version number " + majorVersionNum + "." + minorVersionNum + " not supported.");
                }
                XmlOptions opts = new XmlOptions().setDocumentType(XmlBeans.typeForClass(this._xbeanClass));
                this._impl = XmlBeans.getContextTypeLoader().parse(xmlText, (SchemaType) null, opts);
            } catch (Exception e) {
                throw ((IOException) new IOException(e.getMessage()).initCause(e));
            }
        }

        private String readObjectV0(ObjectInputStream in, int utfBytes) throws IOException {
            int totalBytesRead;
            int numRead;
            byte[] bArray = new byte[utfBytes + 2];
            bArray[0] = (byte) (255 & (utfBytes >> 8));
            bArray[1] = (byte) (255 & utfBytes);
            int i = 0;
            while (true) {
                totalBytesRead = i;
                if (totalBytesRead >= utfBytes || (numRead = in.read(bArray, 2 + totalBytesRead, utfBytes - totalBytesRead)) == -1) {
                    break;
                }
                i = totalBytesRead + numRead;
            }
            if (totalBytesRead != utfBytes) {
                throw new IOException("Error reading backwards compatible XmlObject: number of bytes read (" + totalBytesRead + ") != number expected (" + utfBytes + ")");
            }
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new ByteArrayInputStream(bArray));
                String str = dis.readUTF();
                if (dis != null) {
                    dis.close();
                }
                return str;
            } catch (Throwable th) {
                if (dis != null) {
                    dis.close();
                }
                throw th;
            }
        }

        private Object readResolve() throws ObjectStreamException {
            return this._impl;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlObjectBase$SerializedInteriorObject.class */
    private static class SerializedInteriorObject implements Serializable {
        private static final long serialVersionUID = 1;
        transient XmlObject _impl;
        transient XmlObject _root;

        private SerializedInteriorObject() {
        }

        private SerializedInteriorObject(XmlObject impl, XmlObject root) {
            this._impl = impl;
            this._root = root;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(this._root);
            out.writeBoolean(false);
            out.writeInt(distanceToRoot());
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            this._root = (XmlObject) in.readObject();
            in.readBoolean();
            this._impl = objectAtDistance(in.readInt());
        }

        private Object readResolve() throws ObjectStreamException {
            return this._impl;
        }

        private int distanceToRoot() {
            XmlCursor cur = this._impl.newCursor();
            int count = 0;
            while (!cur.toPrevToken().isNone()) {
                if (!cur.currentTokenType().isNamespace()) {
                    count++;
                }
            }
            cur.dispose();
            return count;
        }

        private XmlObject objectAtDistance(int count) {
            XmlCursor cur = this._root.newCursor();
            while (count > 0) {
                cur.toNextToken();
                if (!cur.currentTokenType().isNamespace()) {
                    count--;
                }
            }
            XmlObject result = cur.getObject();
            cur.dispose();
            return result;
        }
    }

    protected static Object java_value(XmlObject obj) {
        if (obj.isNil()) {
            return null;
        }
        if (!(obj instanceof XmlAnySimpleType)) {
            return obj;
        }
        SchemaType instanceType = ((SimpleValue) obj).instanceType();
        if (!$assertionsDisabled && instanceType == null) {
            throw new AssertionError("Nil case should have been handled above");
        }
        if (instanceType.getSimpleVariety() == 3) {
            return ((SimpleValue) obj).getListValue();
        }
        SimpleValue base = (SimpleValue) obj;
        switch (instanceType.getPrimitiveType().getBuiltinTypeCode()) {
            case 2:
            case 8:
            case 12:
                break;
            case 3:
                return base.getBooleanValue() ? Boolean.TRUE : Boolean.FALSE;
            case 4:
            case 5:
                return base.getByteArrayValue();
            case 6:
                return base.getStringValue();
            case 7:
                return base.getQNameValue();
            case 9:
                return new Float(base.getFloatValue());
            case 10:
                return new Double(base.getDoubleValue());
            case 11:
                switch (instanceType.getDecimalSize()) {
                    case 8:
                        return new Byte(base.getByteValue());
                    case 16:
                        return new Short(base.getShortValue());
                    case 32:
                        return new Integer(base.getIntValue());
                    case 64:
                        return new Long(base.getLongValue());
                    case SchemaType.SIZE_BIG_INTEGER /* 1000000 */:
                        return base.getBigIntegerValue();
                    case 1000001:
                        break;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError("invalid numeric bit count");
                        }
                        break;
                }
                return base.getBigDecimalValue();
            case 13:
                return base.getGDurationValue();
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                return base.getCalendarValue();
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError("encountered nonprimitive type.");
                }
                break;
        }
        return base.getStringValue();
    }

    protected XmlAnySimpleType get_default_attribute_value(QName name) {
        SchemaLocalAttribute sAttr;
        SchemaType sType = schemaType();
        SchemaAttributeModel aModel = sType.getAttributeModel();
        if (aModel == null || (sAttr = aModel.getAttribute(name)) == null) {
            return null;
        }
        return sAttr.getDefaultValue();
    }
}
