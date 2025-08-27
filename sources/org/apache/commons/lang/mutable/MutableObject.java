package org.apache.commons.lang.mutable;

import java.io.Serializable;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/mutable/MutableObject.class */
public class MutableObject implements Mutable, Serializable {
    private static final long serialVersionUID = 86241875189L;
    private Object value;

    public MutableObject() {
    }

    public MutableObject(Object value) {
        this.value = value;
    }

    @Override // org.apache.commons.lang.mutable.Mutable
    public Object getValue() {
        return this.value;
    }

    @Override // org.apache.commons.lang.mutable.Mutable
    public void setValue(Object value) {
        this.value = value;
    }

    public boolean equals(Object obj) {
        if (obj instanceof MutableObject) {
            Object other = ((MutableObject) obj).value;
            return this.value == other || (this.value != null && this.value.equals(other));
        }
        return false;
    }

    public int hashCode() {
        if (this.value == null) {
            return 0;
        }
        return this.value.hashCode();
    }

    public String toString() {
        return this.value == null ? "null" : this.value.toString();
    }
}
