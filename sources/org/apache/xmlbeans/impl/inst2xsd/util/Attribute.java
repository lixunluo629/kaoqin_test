package org.apache.xmlbeans.impl.inst2xsd.util;

import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/inst2xsd/util/Attribute.class */
public class Attribute {
    private QName _name;
    private Type _type;
    private Attribute _ref = null;
    private boolean _isGlobal = false;
    private boolean _isOptional = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Attribute.class.desiredAssertionStatus();
    }

    public QName getName() {
        return this._name;
    }

    public void setName(QName name) {
        this._name = name;
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

    public boolean isRef() {
        return this._ref != null;
    }

    public Attribute getRef() {
        return this._ref;
    }

    public void setRef(Attribute ref) {
        if (!$assertionsDisabled && isGlobal()) {
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
    }

    public boolean isOptional() {
        return this._isOptional;
    }

    public void setOptional(boolean isOptional) {
        if (!$assertionsDisabled && (!isOptional || isGlobal())) {
            throw new AssertionError("Global attributes cannot be optional.");
        }
        this._isOptional = isOptional;
    }

    public String toString() {
        return "\n    Attribute{_name=" + this._name + ", _type=" + this._type + ", _ref=" + (this._ref != null) + ", _isGlobal=" + this._isGlobal + ", _isOptional=" + this._isOptional + "}";
    }
}
