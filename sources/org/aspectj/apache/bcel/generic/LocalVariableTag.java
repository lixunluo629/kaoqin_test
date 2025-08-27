package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/LocalVariableTag.class */
public final class LocalVariableTag extends Tag {
    private final String signature;
    private String name;
    private int slot;
    private final int startPosition;
    private boolean remapped = false;
    private int hashCode = 0;
    private Type type;

    public LocalVariableTag(String str, String str2, int i, int i2) {
        this.signature = str;
        this.name = str2;
        this.slot = i;
        this.startPosition = i2;
    }

    public LocalVariableTag(Type type, String str, String str2, int i, int i2) {
        this.type = type;
        this.signature = str;
        this.name = str2;
        this.slot = i;
        this.startPosition = i2;
    }

    public String getName() {
        return this.name;
    }

    public int getSlot() {
        return this.slot;
    }

    public String getType() {
        return this.signature;
    }

    public Type getRealType() {
        return this.type;
    }

    public void updateSlot(int i) {
        this.slot = i;
        this.remapped = true;
        this.hashCode = 0;
    }

    public void setName(String str) {
        this.name = str;
        this.hashCode = 0;
    }

    public boolean isRemapped() {
        return this.remapped;
    }

    public String toString() {
        return "local " + this.slot + ": " + this.signature + SymbolConstants.SPACE_SYMBOL + this.name;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LocalVariableTag)) {
            return false;
        }
        LocalVariableTag localVariableTag = (LocalVariableTag) obj;
        return localVariableTag.slot == this.slot && localVariableTag.startPosition == this.startPosition && localVariableTag.signature.equals(this.signature) && localVariableTag.name.equals(this.name);
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = (37 * ((37 * ((37 * this.signature.hashCode()) + this.name.hashCode())) + this.slot)) + this.startPosition;
        }
        return this.hashCode;
    }
}
