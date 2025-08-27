package org.apache.xmlbeans.impl.inst2xsd.util;

import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/inst2xsd/util/Element.class */
public class Element {
    public static final int UNBOUNDED = -1;
    static final /* synthetic */ boolean $assertionsDisabled;
    private QName _name = null;
    private Element _ref = null;
    private boolean _isGlobal = false;
    private int _minOccurs = 1;
    private int _maxOccurs = 1;
    private boolean _isNillable = false;
    private Type _type = null;
    private String _comment = null;

    static {
        $assertionsDisabled = !Element.class.desiredAssertionStatus();
    }

    public QName getName() {
        return this._name;
    }

    public void setName(QName name) {
        this._name = name;
    }

    public boolean isRef() {
        return this._ref != null;
    }

    public Element getRef() {
        return this._ref;
    }

    public void setRef(Element ref) {
        if (!$assertionsDisabled && this._isGlobal) {
            throw new AssertionError();
        }
        this._ref = ref;
        this._type = null;
    }

    public boolean isGlobal() {
        return this._isGlobal;
    }

    public void setGlobal(boolean isGlobal) {
        this._isGlobal = isGlobal;
        this._minOccurs = 1;
        this._maxOccurs = 1;
    }

    public int getMinOccurs() {
        return this._minOccurs;
    }

    public void setMinOccurs(int minOccurs) {
        this._minOccurs = minOccurs;
    }

    public int getMaxOccurs() {
        return this._maxOccurs;
    }

    public void setMaxOccurs(int maxOccurs) {
        this._maxOccurs = maxOccurs;
    }

    public boolean isNillable() {
        return this._isNillable;
    }

    public void setNillable(boolean isNillable) {
        this._isNillable = isNillable;
    }

    public Type getType() {
        return isRef() ? getRef().getType() : this._type;
    }

    public void setType(Type type) {
        if (!$assertionsDisabled && isRef()) {
            throw new AssertionError();
        }
        this._type = type;
    }

    public String getComment() {
        return this._comment;
    }

    public void setComment(String comment) {
        this._comment = comment;
    }

    public String toString() {
        return "\n  Element{ _name = " + this._name + ", _ref = " + (this._ref != null) + ", _isGlobal = " + this._isGlobal + ", _minOccurs = " + this._minOccurs + ", _maxOccurs = " + this._maxOccurs + ", _isNillable = " + this._isNillable + ", _comment = " + this._comment + ",\n    _type = " + (this._type == null ? "null" : this._type.isGlobal() ? this._type.getName().toString() : this._type.toString()) + "\n  }";
    }
}
