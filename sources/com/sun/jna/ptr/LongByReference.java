package com.sun.jna.ptr;

/* loaded from: jna-3.0.9.jar:com/sun/jna/ptr/LongByReference.class */
public class LongByReference extends ByReference {
    public LongByReference() {
        this(0L);
    }

    public LongByReference(long value) {
        super(8);
        setValue(value);
    }

    public void setValue(long value) {
        getPointer().setLong(0L, value);
    }

    public long getValue() {
        return getPointer().getLong(0L);
    }
}
