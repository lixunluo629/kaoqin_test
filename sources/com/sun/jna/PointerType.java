package com.sun.jna;

/* loaded from: jna-3.0.9.jar:com/sun/jna/PointerType.class */
public abstract class PointerType implements NativeMapped {
    private Pointer pointer;
    static Class class$com$sun$jna$Pointer;

    protected PointerType() {
        this.pointer = Pointer.NULL;
    }

    protected PointerType(Pointer p) {
        this.pointer = p;
    }

    @Override // com.sun.jna.NativeMapped
    public Class nativeType() throws Throwable {
        if (class$com$sun$jna$Pointer != null) {
            return class$com$sun$jna$Pointer;
        }
        Class clsClass$ = class$("com.sun.jna.Pointer");
        class$com$sun$jna$Pointer = clsClass$;
        return clsClass$;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    @Override // com.sun.jna.NativeMapped
    public Object toNative() {
        return getPointer();
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    public void setPointer(Pointer p) {
        this.pointer = p;
    }

    @Override // com.sun.jna.NativeMapped
    public Object fromNative(Object nativeValue, FromNativeContext context) {
        if (nativeValue == null) {
            return null;
        }
        try {
            PointerType pt = (PointerType) getClass().newInstance();
            pt.pointer = (Pointer) nativeValue;
            return pt;
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(new StringBuffer().append("Not allowed to instantiate ").append(getClass()).toString());
        } catch (InstantiationException e2) {
            throw new IllegalArgumentException(new StringBuffer().append("Can't instantiate ").append(getClass()).toString());
        }
    }

    public int hashCode() {
        if (this.pointer != null) {
            return this.pointer.hashCode();
        }
        return 0;
    }

    public boolean equals(Object o) {
        if (o instanceof PointerType) {
            Pointer p = ((PointerType) o).getPointer();
            if (this.pointer == null) {
                return p == null;
            }
            return this.pointer.equals(p);
        }
        return false;
    }
}
