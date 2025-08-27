package org.apache.xmlbeans.impl.schema;

import java.math.BigInteger;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaPropertyImpl.class */
public class SchemaPropertyImpl implements SchemaProperty {
    private QName _name;
    private SchemaType.Ref _typeref;
    private boolean _isAttribute;
    private SchemaType.Ref _containerTypeRef;
    private String _javaPropertyName;
    private BigInteger _minOccurs;
    private BigInteger _maxOccurs;
    private int _hasNillable;
    private int _hasDefault;
    private int _hasFixed;
    private String _defaultText;
    private boolean _isImmutable;
    private SchemaType.Ref _javaBasedOnTypeRef;
    private boolean _extendsSingleton;
    private boolean _extendsArray;
    private boolean _extendsOption;
    private int _javaTypeCode;
    private QNameSet _javaSetterDelimiter;
    private XmlValueRef _defaultValue;
    private Set _acceptedNames;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SchemaPropertyImpl.class.desiredAssertionStatus();
    }

    private void mutate() {
        if (this._isImmutable) {
            throw new IllegalStateException();
        }
    }

    public void setImmutable() {
        mutate();
        this._isImmutable = true;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public SchemaType getContainerType() {
        return this._containerTypeRef.get();
    }

    public void setContainerTypeRef(SchemaType.Ref typeref) {
        mutate();
        this._containerTypeRef = typeref;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public QName getName() {
        return this._name;
    }

    public void setName(QName name) {
        mutate();
        this._name = name;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public String getJavaPropertyName() {
        return this._javaPropertyName;
    }

    public void setJavaPropertyName(String name) {
        mutate();
        this._javaPropertyName = name;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public boolean isAttribute() {
        return this._isAttribute;
    }

    public void setAttribute(boolean isAttribute) {
        mutate();
        this._isAttribute = isAttribute;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public boolean isReadOnly() {
        return false;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public SchemaType getType() {
        return this._typeref.get();
    }

    public void setTypeRef(SchemaType.Ref typeref) {
        mutate();
        this._typeref = typeref;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public SchemaType javaBasedOnType() {
        if (this._javaBasedOnTypeRef == null) {
            return null;
        }
        return this._javaBasedOnTypeRef.get();
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public boolean extendsJavaSingleton() {
        return this._extendsSingleton;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public boolean extendsJavaArray() {
        return this._extendsArray;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public boolean extendsJavaOption() {
        return this._extendsOption;
    }

    public void setExtendsJava(SchemaType.Ref javaBasedOnTypeRef, boolean singleton, boolean option, boolean array) {
        mutate();
        this._javaBasedOnTypeRef = javaBasedOnTypeRef;
        this._extendsSingleton = singleton;
        this._extendsOption = option;
        this._extendsArray = array;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public QNameSet getJavaSetterDelimiter() {
        if (this._isAttribute) {
            return QNameSet.EMPTY;
        }
        if (this._javaSetterDelimiter == null) {
            ((SchemaTypeImpl) getContainerType()).assignJavaElementSetterModel();
        }
        if ($assertionsDisabled || this._javaSetterDelimiter != null) {
            return this._javaSetterDelimiter;
        }
        throw new AssertionError();
    }

    void setJavaSetterDelimiter(QNameSet set) {
        this._javaSetterDelimiter = set;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public QName[] acceptedNames() {
        if (this._acceptedNames == null) {
            return new QName[]{this._name};
        }
        return (QName[]) this._acceptedNames.toArray(new QName[this._acceptedNames.size()]);
    }

    public void setAcceptedNames(Set set) {
        mutate();
        this._acceptedNames = set;
    }

    public void setAcceptedNames(QNameSet set) {
        mutate();
        this._acceptedNames = set.includedQNamesInExcludedURIs();
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public BigInteger getMinOccurs() {
        return this._minOccurs;
    }

    public void setMinOccurs(BigInteger min) {
        mutate();
        this._minOccurs = min;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public BigInteger getMaxOccurs() {
        return this._maxOccurs;
    }

    public void setMaxOccurs(BigInteger max) {
        mutate();
        this._maxOccurs = max;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public int hasNillable() {
        return this._hasNillable;
    }

    public void setNillable(int when) {
        mutate();
        this._hasNillable = when;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public int hasDefault() {
        return this._hasDefault;
    }

    public void setDefault(int when) {
        mutate();
        this._hasDefault = when;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public int hasFixed() {
        return this._hasFixed;
    }

    public void setFixed(int when) {
        mutate();
        this._hasFixed = when;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public String getDefaultText() {
        return this._defaultText;
    }

    public void setDefaultText(String val) {
        mutate();
        this._defaultText = val;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public XmlAnySimpleType getDefaultValue() {
        if (this._defaultValue != null) {
            return this._defaultValue.get();
        }
        return null;
    }

    public void setDefaultValue(XmlValueRef defaultRef) {
        mutate();
        this._defaultValue = defaultRef;
    }

    @Override // org.apache.xmlbeans.SchemaProperty
    public int getJavaTypeCode() {
        return this._javaTypeCode;
    }

    public void setJavaTypeCode(int code) {
        mutate();
        this._javaTypeCode = code;
    }
}
