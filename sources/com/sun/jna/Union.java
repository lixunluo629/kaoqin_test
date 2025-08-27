package com.sun.jna;

import com.sun.jna.Structure;

/* loaded from: jna-3.0.9.jar:com/sun/jna/Union.class */
public abstract class Union extends Structure {
    private Structure.StructField activeField;
    private Structure.StructField biggestField;
    static Class class$com$sun$jna$Structure;
    static Class class$java$lang$String;
    static Class class$com$sun$jna$WString;

    protected Union() {
    }

    protected Union(int size) {
        super(size);
    }

    protected Union(int size, int alignType) {
        super(size, alignType);
    }

    protected Union(TypeMapper mapper) {
        super(mapper);
    }

    protected Union(int size, int alignType, TypeMapper mapper) {
        super(size, alignType, mapper);
    }

    public void setType(Class type) {
        ensureAllocated();
        for (Structure.StructField f : fields().values()) {
            if (f.type == type) {
                this.activeField = f;
                return;
            }
        }
        throw new IllegalArgumentException(new StringBuffer().append("No field of type ").append(type).append(" in ").append(this).toString());
    }

    @Override // com.sun.jna.Structure
    public Object readField(String name) {
        ensureAllocated();
        Structure.StructField f = (Structure.StructField) fields().get(name);
        if (f != null) {
            setType(f.type);
        }
        return super.readField(name);
    }

    @Override // com.sun.jna.Structure
    public void writeField(String name) throws Throwable {
        ensureAllocated();
        Structure.StructField f = (Structure.StructField) fields().get(name);
        if (f != null) {
            setType(f.type);
        }
        super.writeField(name);
    }

    @Override // com.sun.jna.Structure
    public void writeField(String name, Object value) throws Throwable {
        ensureAllocated();
        Structure.StructField f = (Structure.StructField) fields().get(name);
        if (f != null) {
            setType(f.type);
        }
        super.writeField(name, value);
    }

    public Object getTypedValue(Class type) {
        ensureAllocated();
        for (Structure.StructField f : fields().values()) {
            if (f.type == type) {
                this.activeField = f;
                read();
                return getField(this.activeField);
            }
        }
        throw new IllegalArgumentException(new StringBuffer().append("No field of type ").append(type).append(" in ").append(this).toString());
    }

    public Object setTypedValue(Object object) {
        ensureAllocated();
        Structure.StructField f = findField(object.getClass());
        if (f != null) {
            this.activeField = f;
            setField(f, object);
            return this;
        }
        throw new IllegalArgumentException(new StringBuffer().append("No field of type ").append(object.getClass()).append(" in ").append(this).toString());
    }

    private Structure.StructField findField(Class type) {
        for (Structure.StructField f : fields().values()) {
            if (f.type.isAssignableFrom(type)) {
                return f;
            }
        }
        return null;
    }

    @Override // com.sun.jna.Structure
    void writeField(Structure.StructField field) throws Throwable {
        if (field == this.activeField) {
            super.writeField(field);
        }
    }

    @Override // com.sun.jna.Structure
    Object readField(Structure.StructField field) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (field != this.activeField) {
            if (class$com$sun$jna$Structure == null) {
                clsClass$ = class$("com.sun.jna.Structure");
                class$com$sun$jna$Structure = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$Structure;
            }
            if (clsClass$.isAssignableFrom(field.type)) {
                return null;
            }
            if (class$java$lang$String == null) {
                clsClass$2 = class$("java.lang.String");
                class$java$lang$String = clsClass$2;
            } else {
                clsClass$2 = class$java$lang$String;
            }
            if (clsClass$2.isAssignableFrom(field.type)) {
                return null;
            }
            if (class$com$sun$jna$WString == null) {
                clsClass$3 = class$("com.sun.jna.WString");
                class$com$sun$jna$WString = clsClass$3;
            } else {
                clsClass$3 = class$com$sun$jna$WString;
            }
            if (clsClass$3.isAssignableFrom(field.type)) {
                return null;
            }
        }
        return super.readField(field);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    @Override // com.sun.jna.Structure
    int calculateSize(boolean force) throws Throwable {
        int size = super.calculateSize(force);
        if (size != -1) {
            int fsize = 0;
            for (Structure.StructField f : fields().values()) {
                f.offset = 0;
                if (f.size > fsize) {
                    fsize = f.size;
                    this.biggestField = f;
                }
            }
            size = calculateAlignedSize(fsize);
        }
        return size;
    }

    @Override // com.sun.jna.Structure
    protected int getNativeAlignment(Class type, Object value, boolean isFirstElement) {
        return super.getNativeAlignment(type, value, true);
    }

    @Override // com.sun.jna.Structure
    Pointer getTypeInfo() {
        return getTypeInfo(getField(this.biggestField));
    }
}
