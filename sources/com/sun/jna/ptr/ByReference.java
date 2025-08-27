package com.sun.jna.ptr;

import com.sun.jna.Memory;
import com.sun.jna.PointerType;

/* loaded from: jna-3.0.9.jar:com/sun/jna/ptr/ByReference.class */
public abstract class ByReference extends PointerType {
    protected ByReference(int dataSize) {
        setPointer(new Memory(dataSize));
    }
}
