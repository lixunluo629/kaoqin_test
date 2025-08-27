package org.apache.xmlbeans.impl.inst2xsd.util;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.impl.common.PrefixResolver;
import org.apache.xmlbeans.impl.values.XmlQNameImpl;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/inst2xsd/util/Type.class */
public class Type {
    private QName _name;
    public static final int SIMPLE_TYPE_SIMPLE_CONTENT = 1;
    public static final int COMPLEX_TYPE_SIMPLE_CONTENT = 2;
    public static final int COMPLEX_TYPE_COMPLEX_CONTENT = 3;
    public static final int COMPLEX_TYPE_MIXED_CONTENT = 4;
    public static final int COMPLEX_TYPE_EMPTY_CONTENT = 5;
    public static final int PARTICLE_SEQUENCE = 1;
    public static final int PARTICLE_CHOICE_UNBOUNDED = 2;
    private List _elements;
    private List _attributes;
    private Type _extensionType;
    private List _enumerationValues;
    private List _enumerationQNames;
    static final /* synthetic */ boolean $assertionsDisabled;
    private int _kind = 1;
    private int _topParticleForComplexOrMixedContent = 1;
    private boolean _isGlobal = false;
    private boolean _acceptsEnumerationValue = true;

    static {
        $assertionsDisabled = !Type.class.desiredAssertionStatus();
    }

    protected Type() {
    }

    public static Type createNamedType(QName name, int contentType) {
        if (!$assertionsDisabled && name == null) {
            throw new AssertionError();
        }
        Type type = new Type();
        type.setName(name);
        type.setContentType(contentType);
        return type;
    }

    public static Type createUnnamedType(int contentType) {
        if (!$assertionsDisabled && contentType != 1 && contentType != 2 && contentType != 3 && contentType != 4 && contentType != 5) {
            throw new AssertionError("Unknown contentType: " + contentType);
        }
        Type type = new Type();
        type.setContentType(contentType);
        return type;
    }

    public QName getName() {
        return this._name;
    }

    public void setName(QName name) {
        this._name = name;
    }

    public int getContentType() {
        return this._kind;
    }

    public void setContentType(int kind) {
        this._kind = kind;
    }

    public List getElements() {
        ensureElements();
        return this._elements;
    }

    public void addElement(Element element) {
        ensureElements();
        this._elements.add(element);
    }

    public void setElements(List elements) {
        ensureElements();
        this._elements.clear();
        this._elements.addAll(elements);
    }

    private void ensureElements() {
        if (this._elements == null) {
            this._elements = new ArrayList();
        }
    }

    public List getAttributes() {
        ensureAttributes();
        return this._attributes;
    }

    public void addAttribute(Attribute attribute) {
        ensureAttributes();
        this._attributes.add(attribute);
    }

    public Attribute getAttribute(QName name) {
        for (int i = 0; i < this._attributes.size(); i++) {
            Attribute attribute = (Attribute) this._attributes.get(i);
            if (attribute.getName().equals(name)) {
                return attribute;
            }
        }
        return null;
    }

    private void ensureAttributes() {
        if (this._attributes == null) {
            this._attributes = new ArrayList();
        }
    }

    public boolean isComplexType() {
        return this._kind == 3 || this._kind == 4 || this._kind == 2;
    }

    public boolean hasSimpleContent() {
        return this._kind == 1 || this._kind == 2;
    }

    public int getTopParticleForComplexOrMixedContent() {
        return this._topParticleForComplexOrMixedContent;
    }

    public void setTopParticleForComplexOrMixedContent(int topParticleForComplexOrMixedContent) {
        this._topParticleForComplexOrMixedContent = topParticleForComplexOrMixedContent;
    }

    public boolean isGlobal() {
        return this._isGlobal;
    }

    public void setGlobal(boolean isGlobal) {
        if (!$assertionsDisabled && (!isGlobal || getName() == null)) {
            throw new AssertionError();
        }
        this._isGlobal = isGlobal;
    }

    public Type getExtensionType() {
        return this._extensionType;
    }

    public void setExtensionType(Type extendedType) {
        if (!$assertionsDisabled && this._kind != 2) {
            throw new AssertionError("Extension used only for type which are COMPLEX_TYPE_SIMPLE_CONTENT");
        }
        if (!$assertionsDisabled && (extendedType == null || extendedType.getName() == null)) {
            throw new AssertionError("Extended type must be a named type.");
        }
        this._extensionType = extendedType;
    }

    public List getEnumerationValues() {
        ensureEnumerationValues();
        return this._enumerationValues;
    }

    public List getEnumerationQNames() {
        ensureEnumerationValues();
        return this._enumerationQNames;
    }

    public void addEnumerationValue(String enumerationValue, final XmlCursor xc) {
        if (!$assertionsDisabled && this._kind != 1 && this._kind != 2) {
            throw new AssertionError("Enumerations possible only on simple content");
        }
        ensureEnumerationValues();
        if (this._acceptsEnumerationValue && !this._enumerationValues.contains(enumerationValue)) {
            this._enumerationValues.add(enumerationValue);
            if (this._name.equals(XmlQName.type.getName())) {
                PrefixResolver prefixResolver = new PrefixResolver() { // from class: org.apache.xmlbeans.impl.inst2xsd.util.Type.1
                    @Override // org.apache.xmlbeans.impl.common.PrefixResolver
                    public String getNamespaceForPrefix(String prefix) {
                        return xc.namespaceForPrefix(prefix);
                    }
                };
                QName qname = XmlQNameImpl.validateLexical(enumerationValue, null, prefixResolver);
                if (!$assertionsDisabled && qname == null) {
                    throw new AssertionError("The check for QName should allready have happened.");
                }
                this._enumerationQNames.add(qname);
            }
        }
    }

    private void ensureEnumerationValues() {
        if (this._enumerationValues == null) {
            this._enumerationValues = new ArrayList();
            this._enumerationQNames = new ArrayList();
        }
    }

    public boolean isEnumeration() {
        return this._acceptsEnumerationValue && this._enumerationValues != null && this._enumerationValues.size() > 1;
    }

    public boolean isQNameEnumeration() {
        return isEnumeration() && this._name.equals(XmlQName.type.getName()) && this._enumerationQNames != null && this._enumerationQNames.size() > 1;
    }

    public void closeEnumeration() {
        this._acceptsEnumerationValue = false;
    }

    public String toString() {
        return "Type{_name = " + this._name + ", _extensionType = " + this._extensionType + ", _kind = " + this._kind + ", _elements = " + this._elements + ", _attributes = " + this._attributes + "}";
    }

    public void addAllEnumerationsFrom(Type from) {
        if (!$assertionsDisabled && this._kind != 1 && this._kind != 2) {
            throw new AssertionError("Enumerations possible only on simple content");
        }
        ensureEnumerationValues();
        if (this._name.equals(XmlQName.type.getName()) && from._name.equals(XmlQName.type.getName())) {
            for (int i = 0; i < from.getEnumerationValues().size(); i++) {
                String enumValue = (String) from.getEnumerationValues().get(i);
                QName enumQNameValue = (QName) from.getEnumerationQNames().get(i);
                if (this._acceptsEnumerationValue && !this._enumerationQNames.contains(enumQNameValue)) {
                    this._enumerationValues.add(enumValue);
                    this._enumerationQNames.add(enumQNameValue);
                }
            }
            return;
        }
        for (int i2 = 0; i2 < from.getEnumerationValues().size(); i2++) {
            String enumValue2 = (String) from.getEnumerationValues().get(i2);
            if (this._acceptsEnumerationValue && !this._enumerationValues.contains(enumValue2)) {
                this._enumerationValues.add(enumValue2);
            }
        }
    }
}
