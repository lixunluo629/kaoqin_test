package com.sun.jna;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.springframework.beans.PropertyAccessor;
import org.springframework.hateoas.Link;

/* loaded from: jna-3.0.9.jar:com/sun/jna/Structure.class */
public abstract class Structure {
    private static final boolean REVERSE_FIELDS;
    static boolean REQUIRES_FIELD_ORDER;
    static final boolean isPPC;
    static final boolean isSPARC;
    public static final int ALIGN_DEFAULT = 0;
    public static final int ALIGN_NONE = 1;
    public static final int ALIGN_GNUC = 2;
    public static final int ALIGN_MSVC = 3;
    private static final int MAX_GNUC_ALIGNMENT;
    protected static final int CALCULATE_SIZE = -1;
    private Pointer memory;
    private int size;
    private int alignType;
    private int structAlignment;
    private final Map structFields;
    private final Map nativeStrings;
    private TypeMapper typeMapper;
    private long typeInfo;
    private List fieldOrder;
    private boolean autoRead;
    private boolean autoWrite;
    private static Set reading;
    private static Set writing;
    static Class class$com$sun$jna$Structure$MemberOrder;
    static Class class$com$sun$jna$Structure;
    static Class class$com$sun$jna$Callback;
    static Class class$java$nio$Buffer;
    static Class class$com$sun$jna$Pointer;
    static Class class$com$sun$jna$Structure$ByReference;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Short;
    static Class class$java$lang$Character;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;
    static Class class$java$lang$String;
    static Class class$com$sun$jna$WString;
    static Class class$com$sun$jna$NativeMapped;
    static Class class$com$sun$jna$Memory;
    static Class class$java$lang$Void;
    static Class class$com$sun$jna$Union;

    /* loaded from: jna-3.0.9.jar:com/sun/jna/Structure$ByReference.class */
    public interface ByReference {
    }

    /* loaded from: jna-3.0.9.jar:com/sun/jna/Structure$ByValue.class */
    public interface ByValue {
    }

    /* loaded from: jna-3.0.9.jar:com/sun/jna/Structure$MemberOrder.class */
    private static class MemberOrder {
        public int first;
        public int middle;
        public int last;

        private MemberOrder() {
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    static {
        Class clsClass$;
        if (class$com$sun$jna$Structure$MemberOrder == null) {
            clsClass$ = class$("com.sun.jna.Structure$MemberOrder");
            class$com$sun$jna$Structure$MemberOrder = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Structure$MemberOrder;
        }
        Field[] fields = clsClass$.getFields();
        REVERSE_FIELDS = Link.REL_LAST.equals(fields[0].getName());
        REQUIRES_FIELD_ORDER = !"middle".equals(fields[1].getName());
        String arch = System.getProperty("os.arch").toLowerCase();
        isPPC = "ppc".equals(arch) || "powerpc".equals(arch);
        isSPARC = "sparc".equals(arch);
        MAX_GNUC_ALIGNMENT = isSPARC ? 8 : Native.LONG_SIZE;
        reading = new HashSet();
        writing = new HashSet();
    }

    protected Structure() {
        this(-1);
    }

    protected Structure(int size) {
        this(size, 0);
    }

    protected Structure(int size, int alignment) {
        this(size, alignment, null);
    }

    protected Structure(TypeMapper mapper) {
        this(-1, 0, mapper);
    }

    protected Structure(int size, int alignment, TypeMapper mapper) throws Throwable {
        this.size = -1;
        this.structFields = new LinkedHashMap();
        this.nativeStrings = new HashMap();
        this.autoRead = true;
        this.autoWrite = true;
        setAlignType(alignment);
        setTypeMapper(mapper);
        allocateMemory(size);
    }

    Map fields() {
        return this.structFields;
    }

    protected void setTypeMapper(TypeMapper mapper) {
        Class declaring;
        if (mapper == null && (declaring = getClass().getDeclaringClass()) != null) {
            mapper = Native.getTypeMapper(declaring);
        }
        this.typeMapper = mapper;
        this.size = -1;
        this.memory = null;
    }

    protected void setAlignType(int alignType) {
        if (alignType == 0) {
            Class declaring = getClass().getDeclaringClass();
            if (declaring != null) {
                alignType = Native.getStructureAlignment(declaring);
            }
            if (alignType == 0) {
                if (Platform.isWindows()) {
                    alignType = 3;
                } else {
                    alignType = 2;
                }
            }
        }
        this.alignType = alignType;
        this.size = -1;
        this.memory = null;
    }

    protected void useMemory(Pointer m) {
        useMemory(m, 0);
    }

    protected void useMemory(Pointer m, int offset) {
        this.memory = m.share(offset, size());
    }

    protected void ensureAllocated() throws Throwable {
        if (this.size == -1) {
            allocateMemory();
        }
    }

    protected void allocateMemory() throws Throwable {
        allocateMemory(calculateSize(true));
    }

    protected void allocateMemory(int size) throws Throwable {
        if (size == -1) {
            size = calculateSize(false);
        } else if (size <= 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Structure size must be greater than zero: ").append(size).toString());
        }
        if (size != -1) {
            this.memory = new Memory(size);
            this.memory.clear(size);
            this.size = size;
            if (this instanceof ByValue) {
                getTypeInfo();
            }
        }
    }

    public int size() {
        ensureAllocated();
        return this.size;
    }

    public void clear() {
        this.memory.clear(size());
    }

    public Pointer getPointer() {
        ensureAllocated();
        return this.memory;
    }

    public void read() {
        ensureAllocated();
        synchronized (reading) {
            if (reading.contains(this)) {
                return;
            }
            reading.add(this);
            try {
                Iterator i = this.structFields.values().iterator();
                while (i.hasNext()) {
                    readField((StructField) i.next());
                }
                synchronized (reading) {
                    reading.remove(this);
                }
            } catch (Throwable th) {
                synchronized (reading) {
                    reading.remove(this);
                    throw th;
                }
            }
        }
    }

    public Object readField(String name) throws Throwable {
        ensureAllocated();
        StructField f = (StructField) this.structFields.get(name);
        if (f == null) {
            throw new IllegalArgumentException(new StringBuffer().append("No such field: ").append(name).toString());
        }
        return readField(f);
    }

    Object getField(StructField structField) {
        try {
            return structField.field.get(this);
        } catch (Exception e) {
            throw new Error(new StringBuffer().append("Exception reading field '").append(structField.name).append("' in ").append(getClass()).append(": ").append(e).toString());
        }
    }

    void setField(StructField structField, Object value) throws IllegalAccessException, IllegalArgumentException {
        try {
            structField.field.set(this, value);
        } catch (IllegalAccessException e) {
            throw new Error(new StringBuffer().append("Unexpectedly unable to write to field '").append(structField.name).append("' within ").append(getClass()).append(": ").append(e).toString());
        }
    }

    static Structure updateStructureByReference(Class type, Structure s, Pointer address) throws IllegalArgumentException {
        if (address == null) {
            s = null;
        } else {
            if (s == null || !address.equals(s.getPointer())) {
                s = newInstance(type);
                s.useMemory(address);
            }
            if (s.getAutoRead()) {
                s.read();
            }
        }
        return s;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0094  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.Object readField(com.sun.jna.Structure.StructField r6) throws java.lang.Throwable {
        /*
            r5 = this;
            r0 = r6
            int r0 = r0.offset
            r7 = r0
            r0 = r6
            java.lang.Class r0 = r0.type
            r8 = r0
            r0 = r6
            com.sun.jna.FromNativeConverter r0 = r0.readConverter
            r9 = r0
            r0 = r9
            if (r0 == 0) goto L1d
            r0 = r9
            java.lang.Class r0 = r0.nativeType()
            r8 = r0
        L1d:
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Structure
            if (r0 != 0) goto L2f
            java.lang.String r0 = "com.sun.jna.Structure"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            com.sun.jna.Structure.class$com$sun$jna$Structure = r1
            goto L32
        L2f:
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Structure
        L32:
            r1 = r8
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 != 0) goto L94
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Callback
            if (r0 != 0) goto L4b
            java.lang.String r0 = "com.sun.jna.Callback"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            com.sun.jna.Structure.class$com$sun$jna$Callback = r1
            goto L4e
        L4b:
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Callback
        L4e:
            r1 = r8
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 != 0) goto L94
            java.lang.Class r0 = com.sun.jna.Structure.class$java$nio$Buffer
            if (r0 != 0) goto L67
            java.lang.String r0 = "java.nio.Buffer"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            com.sun.jna.Structure.class$java$nio$Buffer = r1
            goto L6a
        L67:
            java.lang.Class r0 = com.sun.jna.Structure.class$java$nio$Buffer
        L6a:
            r1 = r8
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 != 0) goto L94
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Pointer
            if (r0 != 0) goto L83
            java.lang.String r0 = "com.sun.jna.Pointer"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            com.sun.jna.Structure.class$com$sun$jna$Pointer = r1
            goto L86
        L83:
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Pointer
        L86:
            r1 = r8
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 != 0) goto L94
            r0 = r8
            boolean r0 = r0.isArray()
            if (r0 == 0) goto L9c
        L94:
            r0 = r5
            r1 = r6
            java.lang.Object r0 = r0.getField(r1)
            goto L9d
        L9c:
            r0 = 0
        L9d:
            r10 = r0
            r0 = r5
            r1 = r7
            r2 = r8
            r3 = r10
            java.lang.Object r0 = r0.readValue(r1, r2, r3)
            r11 = r0
            r0 = r9
            if (r0 == 0) goto Lbd
            r0 = r9
            r1 = r11
            r2 = r6
            com.sun.jna.FromNativeContext r2 = r2.context
            java.lang.Object r0 = r0.fromNative(r1, r2)
            r11 = r0
        Lbd:
            r0 = r5
            r1 = r6
            r2 = r11
            r0.setField(r1, r2)
            r0 = r11
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Structure.readField(com.sun.jna.Structure$StructField):java.lang.Object");
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x012e  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0163  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0198  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0202  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Object readValue(int r8, java.lang.Class r9, java.lang.Object r10) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 908
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Structure.readValue(int, java.lang.Class, java.lang.Object):java.lang.Object");
    }

    private void readArrayValue(int offset, Object o, Class cls) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        int length = Array.getLength(o);
        if (cls == Byte.TYPE) {
            this.memory.read(offset, (byte[]) o, 0, length);
            return;
        }
        if (cls == Short.TYPE) {
            this.memory.read(offset, (short[]) o, 0, length);
            return;
        }
        if (cls == Character.TYPE) {
            this.memory.read(offset, (char[]) o, 0, length);
            return;
        }
        if (cls == Integer.TYPE) {
            this.memory.read(offset, (int[]) o, 0, length);
            return;
        }
        if (cls == Long.TYPE) {
            this.memory.read(offset, (long[]) o, 0, length);
            return;
        }
        if (cls == Float.TYPE) {
            this.memory.read(offset, (float[]) o, 0, length);
            return;
        }
        if (cls == Double.TYPE) {
            this.memory.read(offset, (double[]) o, 0, length);
            return;
        }
        if (class$com$sun$jna$Pointer == null) {
            clsClass$ = class$("com.sun.jna.Pointer");
            class$com$sun$jna$Pointer = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Pointer;
        }
        if (clsClass$.isAssignableFrom(cls)) {
            this.memory.read(offset, (Pointer[]) o, 0, length);
            return;
        }
        if (class$com$sun$jna$Structure == null) {
            clsClass$2 = class$("com.sun.jna.Structure");
            class$com$sun$jna$Structure = clsClass$2;
        } else {
            clsClass$2 = class$com$sun$jna$Structure;
        }
        if (clsClass$2.isAssignableFrom(cls)) {
            Structure[] sarray = (Structure[]) o;
            if (class$com$sun$jna$Structure$ByReference == null) {
                clsClass$4 = class$("com.sun.jna.Structure$ByReference");
                class$com$sun$jna$Structure$ByReference = clsClass$4;
            } else {
                clsClass$4 = class$com$sun$jna$Structure$ByReference;
            }
            if (clsClass$4.isAssignableFrom(cls)) {
                Pointer[] parray = this.memory.getPointerArray(offset, sarray.length);
                for (int i = 0; i < sarray.length; i++) {
                    sarray[i] = updateStructureByReference(cls, sarray[i], parray[i]);
                }
                return;
            }
            for (int i2 = 0; i2 < sarray.length; i2++) {
                if (sarray[i2] == null) {
                    sarray[i2] = newInstance(cls);
                }
                sarray[i2].useMemory(this.memory, offset + (i2 * sarray[i2].size()));
                sarray[i2].read();
            }
            return;
        }
        if (class$com$sun$jna$NativeMapped == null) {
            clsClass$3 = class$("com.sun.jna.NativeMapped");
            class$com$sun$jna$NativeMapped = clsClass$3;
        } else {
            clsClass$3 = class$com$sun$jna$NativeMapped;
        }
        if (clsClass$3.isAssignableFrom(cls)) {
            NativeMapped[] array = (NativeMapped[]) o;
            NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
            int size = getNativeSize(o.getClass(), o) / array.length;
            for (int i3 = 0; i3 < array.length; i3++) {
                Object value = readValue(offset + (size * i3), tc.nativeType(), array[i3]);
                array[i3] = (NativeMapped) tc.fromNative(value, new FromNativeContext(cls));
            }
            return;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Array of ").append(cls).append(" not supported").toString());
    }

    public void write() {
        ensureAllocated();
        if (this instanceof ByValue) {
            getTypeInfo();
        }
        synchronized (writing) {
            if (writing.contains(this)) {
                return;
            }
            writing.add(this);
            try {
                for (StructField sf : this.structFields.values()) {
                    if (!sf.isVolatile) {
                        writeField(sf);
                    }
                }
                synchronized (writing) {
                    writing.remove(this);
                }
            } catch (Throwable th) {
                synchronized (writing) {
                    writing.remove(this);
                    throw th;
                }
            }
        }
    }

    public void writeField(String name) throws Throwable {
        ensureAllocated();
        StructField f = (StructField) this.structFields.get(name);
        if (f == null) {
            throw new IllegalArgumentException(new StringBuffer().append("No such field: ").append(name).toString());
        }
        writeField(f);
    }

    public void writeField(String name, Object value) throws Throwable {
        ensureAllocated();
        StructField f = (StructField) this.structFields.get(name);
        if (f == null) {
            throw new IllegalArgumentException(new StringBuffer().append("No such field: ").append(name).toString());
        }
        setField(f, value);
        writeField(f);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x006e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void writeField(com.sun.jna.Structure.StructField r8) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 308
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Structure.writeField(com.sun.jna.Structure$StructField):void");
    }

    private boolean writeValue(int offset, Object value, Class nativeType) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class clsClass$10;
        Class clsClass$11;
        Class clsClass$12;
        Class clsClass$13;
        Class clsClass$14;
        if (nativeType != Boolean.TYPE) {
            if (class$java$lang$Boolean == null) {
                clsClass$ = class$("java.lang.Boolean");
                class$java$lang$Boolean = clsClass$;
            } else {
                clsClass$ = class$java$lang$Boolean;
            }
            if (nativeType != clsClass$) {
                if (nativeType != Byte.TYPE) {
                    if (class$java$lang$Byte == null) {
                        clsClass$2 = class$("java.lang.Byte");
                        class$java$lang$Byte = clsClass$2;
                    } else {
                        clsClass$2 = class$java$lang$Byte;
                    }
                    if (nativeType != clsClass$2) {
                        if (nativeType != Short.TYPE) {
                            if (class$java$lang$Short == null) {
                                clsClass$3 = class$("java.lang.Short");
                                class$java$lang$Short = clsClass$3;
                            } else {
                                clsClass$3 = class$java$lang$Short;
                            }
                            if (nativeType != clsClass$3) {
                                if (nativeType != Character.TYPE) {
                                    if (class$java$lang$Character == null) {
                                        clsClass$4 = class$("java.lang.Character");
                                        class$java$lang$Character = clsClass$4;
                                    } else {
                                        clsClass$4 = class$java$lang$Character;
                                    }
                                    if (nativeType != clsClass$4) {
                                        if (nativeType != Integer.TYPE) {
                                            if (class$java$lang$Integer == null) {
                                                clsClass$5 = class$("java.lang.Integer");
                                                class$java$lang$Integer = clsClass$5;
                                            } else {
                                                clsClass$5 = class$java$lang$Integer;
                                            }
                                            if (nativeType != clsClass$5) {
                                                if (nativeType != Long.TYPE) {
                                                    if (class$java$lang$Long == null) {
                                                        clsClass$6 = class$("java.lang.Long");
                                                        class$java$lang$Long = clsClass$6;
                                                    } else {
                                                        clsClass$6 = class$java$lang$Long;
                                                    }
                                                    if (nativeType != clsClass$6) {
                                                        if (nativeType != Float.TYPE) {
                                                            if (class$java$lang$Float == null) {
                                                                clsClass$7 = class$("java.lang.Float");
                                                                class$java$lang$Float = clsClass$7;
                                                            } else {
                                                                clsClass$7 = class$java$lang$Float;
                                                            }
                                                            if (nativeType != clsClass$7) {
                                                                if (nativeType != Double.TYPE) {
                                                                    if (class$java$lang$Double == null) {
                                                                        clsClass$8 = class$("java.lang.Double");
                                                                        class$java$lang$Double = clsClass$8;
                                                                    } else {
                                                                        clsClass$8 = class$java$lang$Double;
                                                                    }
                                                                    if (nativeType != clsClass$8) {
                                                                        if (class$com$sun$jna$Pointer == null) {
                                                                            clsClass$9 = class$("com.sun.jna.Pointer");
                                                                            class$com$sun$jna$Pointer = clsClass$9;
                                                                        } else {
                                                                            clsClass$9 = class$com$sun$jna$Pointer;
                                                                        }
                                                                        if (nativeType == clsClass$9) {
                                                                            this.memory.setPointer(offset, (Pointer) value);
                                                                            return true;
                                                                        }
                                                                        if (class$java$lang$String == null) {
                                                                            clsClass$10 = class$("java.lang.String");
                                                                            class$java$lang$String = clsClass$10;
                                                                        } else {
                                                                            clsClass$10 = class$java$lang$String;
                                                                        }
                                                                        if (nativeType == clsClass$10) {
                                                                            this.memory.setPointer(offset, (Pointer) value);
                                                                            return true;
                                                                        }
                                                                        if (class$com$sun$jna$WString == null) {
                                                                            clsClass$11 = class$("com.sun.jna.WString");
                                                                            class$com$sun$jna$WString = clsClass$11;
                                                                        } else {
                                                                            clsClass$11 = class$com$sun$jna$WString;
                                                                        }
                                                                        if (nativeType == clsClass$11) {
                                                                            this.memory.setPointer(offset, (Pointer) value);
                                                                            return true;
                                                                        }
                                                                        if (class$com$sun$jna$Structure == null) {
                                                                            clsClass$12 = class$("com.sun.jna.Structure");
                                                                            class$com$sun$jna$Structure = clsClass$12;
                                                                        } else {
                                                                            clsClass$12 = class$com$sun$jna$Structure;
                                                                        }
                                                                        if (clsClass$12.isAssignableFrom(nativeType)) {
                                                                            Structure s = (Structure) value;
                                                                            if (class$com$sun$jna$Structure$ByReference == null) {
                                                                                clsClass$14 = class$("com.sun.jna.Structure$ByReference");
                                                                                class$com$sun$jna$Structure$ByReference = clsClass$14;
                                                                            } else {
                                                                                clsClass$14 = class$com$sun$jna$Structure$ByReference;
                                                                            }
                                                                            if (clsClass$14.isAssignableFrom(nativeType)) {
                                                                                this.memory.setPointer(offset, s == null ? null : s.getPointer());
                                                                                if (s != null) {
                                                                                    s.write();
                                                                                    return true;
                                                                                }
                                                                                return true;
                                                                            }
                                                                            s.useMemory(this.memory, offset);
                                                                            s.write();
                                                                            return true;
                                                                        }
                                                                        if (class$com$sun$jna$Callback == null) {
                                                                            clsClass$13 = class$("com.sun.jna.Callback");
                                                                            class$com$sun$jna$Callback = clsClass$13;
                                                                        } else {
                                                                            clsClass$13 = class$com$sun$jna$Callback;
                                                                        }
                                                                        if (clsClass$13.isAssignableFrom(nativeType)) {
                                                                            this.memory.setPointer(offset, CallbackReference.getFunctionPointer((Callback) value));
                                                                            return true;
                                                                        }
                                                                        if (nativeType.isArray()) {
                                                                            return writeArrayValue(offset, value, nativeType.getComponentType());
                                                                        }
                                                                        return false;
                                                                    }
                                                                }
                                                                this.memory.setDouble(offset, value == null ? 0.0d : ((Double) value).doubleValue());
                                                                return true;
                                                            }
                                                        }
                                                        this.memory.setFloat(offset, value == null ? 0.0f : ((Float) value).floatValue());
                                                        return true;
                                                    }
                                                }
                                                this.memory.setLong(offset, value == null ? 0L : ((Long) value).longValue());
                                                return true;
                                            }
                                        }
                                        this.memory.setInt(offset, value == null ? 0 : ((Integer) value).intValue());
                                        return true;
                                    }
                                }
                                this.memory.setChar(offset, value == null ? (char) 0 : ((Character) value).charValue());
                                return true;
                            }
                        }
                        this.memory.setShort(offset, value == null ? (short) 0 : ((Short) value).shortValue());
                        return true;
                    }
                }
                this.memory.setByte(offset, value == null ? (byte) 0 : ((Byte) value).byteValue());
                return true;
            }
        }
        this.memory.setInt(offset, Boolean.TRUE.equals(value) ? -1 : 0);
        return true;
    }

    private boolean writeArrayValue(int offset, Object value, Class cls) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        if (cls == Byte.TYPE) {
            byte[] buf = (byte[]) value;
            this.memory.write(offset, buf, 0, buf.length);
            return true;
        }
        if (cls == Short.TYPE) {
            short[] buf2 = (short[]) value;
            this.memory.write(offset, buf2, 0, buf2.length);
            return true;
        }
        if (cls == Character.TYPE) {
            char[] buf3 = (char[]) value;
            this.memory.write(offset, buf3, 0, buf3.length);
            return true;
        }
        if (cls == Integer.TYPE) {
            int[] buf4 = (int[]) value;
            this.memory.write(offset, buf4, 0, buf4.length);
            return true;
        }
        if (cls == Long.TYPE) {
            long[] buf5 = (long[]) value;
            this.memory.write(offset, buf5, 0, buf5.length);
            return true;
        }
        if (cls == Float.TYPE) {
            float[] buf6 = (float[]) value;
            this.memory.write(offset, buf6, 0, buf6.length);
            return true;
        }
        if (cls == Double.TYPE) {
            double[] buf7 = (double[]) value;
            this.memory.write(offset, buf7, 0, buf7.length);
            return true;
        }
        if (class$com$sun$jna$Pointer == null) {
            clsClass$ = class$("com.sun.jna.Pointer");
            class$com$sun$jna$Pointer = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Pointer;
        }
        if (clsClass$.isAssignableFrom(cls)) {
            Pointer[] buf8 = (Pointer[]) value;
            this.memory.write(offset, buf8, 0, buf8.length);
            return true;
        }
        if (class$com$sun$jna$Structure == null) {
            clsClass$2 = class$("com.sun.jna.Structure");
            class$com$sun$jna$Structure = clsClass$2;
        } else {
            clsClass$2 = class$com$sun$jna$Structure;
        }
        if (clsClass$2.isAssignableFrom(cls)) {
            Structure[] sbuf = (Structure[]) value;
            if (class$com$sun$jna$Structure$ByReference == null) {
                clsClass$4 = class$("com.sun.jna.Structure$ByReference");
                class$com$sun$jna$Structure$ByReference = clsClass$4;
            } else {
                clsClass$4 = class$com$sun$jna$Structure$ByReference;
            }
            if (clsClass$4.isAssignableFrom(cls)) {
                Pointer[] buf9 = new Pointer[sbuf.length];
                for (int i = 0; i < sbuf.length; i++) {
                    buf9[i] = sbuf[i] == null ? null : sbuf[i].getPointer();
                }
                this.memory.write(offset, buf9, 0, buf9.length);
                return true;
            }
            for (int i2 = 0; i2 < sbuf.length; i2++) {
                if (sbuf[i2] == null) {
                    sbuf[i2] = newInstance(cls);
                }
                sbuf[i2].useMemory(this.memory, offset + (i2 * sbuf[i2].size()));
                sbuf[i2].write();
            }
            return true;
        }
        if (class$com$sun$jna$NativeMapped == null) {
            clsClass$3 = class$("com.sun.jna.NativeMapped");
            class$com$sun$jna$NativeMapped = clsClass$3;
        } else {
            clsClass$3 = class$com$sun$jna$NativeMapped;
        }
        if (clsClass$3.isAssignableFrom(cls)) {
            NativeMapped[] buf10 = (NativeMapped[]) value;
            NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
            Class nativeType = tc.nativeType();
            int size = getNativeSize(value.getClass(), value) / buf10.length;
            for (int i3 = 0; i3 < buf10.length; i3++) {
                Object element = tc.toNative(buf10[i3], new ToNativeContext());
                if (!writeValue(offset + (i3 * size), element, nativeType)) {
                    return false;
                }
            }
            return true;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Inline array of ").append(cls).append(" not supported").toString());
    }

    protected List getFieldOrder() {
        List list;
        synchronized (this) {
            if (this.fieldOrder == null) {
                this.fieldOrder = new ArrayList();
            }
            list = this.fieldOrder;
        }
        return list;
    }

    protected void setFieldOrder(String[] fields) {
        getFieldOrder().addAll(Arrays.asList(fields));
    }

    protected void sortFields(Field[] fields, String[] names) {
        for (int i = 0; i < names.length; i++) {
            int f = i;
            while (true) {
                if (f >= fields.length) {
                    break;
                }
                if (!names[i].equals(fields[f].getName())) {
                    f++;
                } else {
                    Field tmp = fields[f];
                    fields[f] = fields[i];
                    fields[i] = tmp;
                    break;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:77:0x0274  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    int calculateSize(boolean r8) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 1103
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Structure.calculateSize(boolean):int");
    }

    int calculateAlignedSize(int calculatedSize) {
        if (this.alignType != 1 && calculatedSize % this.structAlignment != 0) {
            calculatedSize += this.structAlignment - (calculatedSize % this.structAlignment);
        }
        return calculatedSize;
    }

    protected int getStructAlignment() throws Throwable {
        if (this.size == -1) {
            calculateSize(true);
        }
        return this.structAlignment;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:59:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0199  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected int getNativeAlignment(java.lang.Class r6, java.lang.Object r7, boolean r8) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 635
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Structure.getNativeAlignment(java.lang.Class, java.lang.Object, boolean):int");
    }

    private static int getNativeSize(Class type, Object value) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (type.isArray()) {
            int len = Array.getLength(value);
            if (len > 0) {
                Object o = Array.get(value, 0);
                return len * getNativeSize(type.getComponentType(), o);
            }
            throw new IllegalArgumentException(new StringBuffer().append("Arrays of length zero not allowed in structure: ").append(type).toString());
        }
        if (class$java$nio$Buffer == null) {
            clsClass$ = class$("java.nio.Buffer");
            class$java$nio$Buffer = clsClass$;
        } else {
            clsClass$ = class$java$nio$Buffer;
        }
        if (clsClass$.isAssignableFrom(type)) {
            throw new IllegalArgumentException(new StringBuffer().append("the type \"").append(type.getName()).append("\" is not supported as a structure field: ").toString());
        }
        if (class$com$sun$jna$Structure == null) {
            clsClass$2 = class$("com.sun.jna.Structure");
            class$com$sun$jna$Structure = clsClass$2;
        } else {
            clsClass$2 = class$com$sun$jna$Structure;
        }
        if (clsClass$2.isAssignableFrom(type)) {
            if (class$com$sun$jna$Structure$ByReference == null) {
                clsClass$3 = class$("com.sun.jna.Structure$ByReference");
                class$com$sun$jna$Structure$ByReference = clsClass$3;
            } else {
                clsClass$3 = class$com$sun$jna$Structure$ByReference;
            }
            if (!clsClass$3.isAssignableFrom(type)) {
                if (value == null) {
                    value = newInstance(type);
                }
                return ((Structure) value).size();
            }
        }
        try {
            return Native.getNativeSize(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(new StringBuffer().append("The type \"").append(type.getName()).append("\" is not supported as a structure field: ").append(e.getMessage()).toString());
        }
    }

    public String toString() {
        return toString(0);
    }

    private String format(Class type) {
        String s = type.getName();
        int dot = s.lastIndexOf(".");
        return s.substring(dot + 1);
    }

    private String toString(int indent) {
        String LS = System.getProperty("line.separator");
        String name = new StringBuffer().append(format(getClass())).append("(").append(getPointer()).append(")").toString();
        if (!(getPointer() instanceof Memory)) {
            name = new StringBuffer().append(name).append(" (").append(size()).append(" bytes)").toString();
        }
        String prefix = "";
        for (int idx = 0; idx < indent; idx++) {
            prefix = new StringBuffer().append(prefix).append("  ").toString();
        }
        String contents = "";
        Iterator i = this.structFields.values().iterator();
        while (i.hasNext()) {
            StructField sf = (StructField) i.next();
            Object value = getField(sf);
            String type = format(sf.type);
            String index = "";
            String contents2 = new StringBuffer().append(contents).append(prefix).toString();
            if (sf.type.isArray() && value != null) {
                type = format(sf.type.getComponentType());
                index = new StringBuffer().append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(Array.getLength(value)).append("]").toString();
            }
            String contents3 = new StringBuffer().append(contents2).append("  ").append(type).append(SymbolConstants.SPACE_SYMBOL).append(sf.name).append(index).append("@").append(Integer.toHexString(sf.offset)).toString();
            if (value instanceof Structure) {
                if (value instanceof ByReference) {
                    String v = value.toString();
                    if (v.indexOf(LS) != -1) {
                        v = v.substring(0, v.indexOf(LS));
                    }
                    value = new StringBuffer().append(v).append("...}").toString();
                } else {
                    value = ((Structure) value).toString(indent + 1);
                }
            }
            contents = new StringBuffer().append(new StringBuffer().append(contents3).append(SymbolConstants.EQUAL_SYMBOL).append(String.valueOf(value).trim()).toString()).append(LS).toString();
            if (!i.hasNext()) {
                contents = new StringBuffer().append(contents).append(prefix).append("}").toString();
            }
        }
        if (indent == 0 && Boolean.getBoolean("jna.dump_memory")) {
            byte[] buf = getPointer().getByteArray(0L, size());
            String contents4 = new StringBuffer().append(contents).append(LS).append("memory dump").append(LS).toString();
            for (int i2 = 0; i2 < buf.length; i2++) {
                if (i2 % 4 == 0) {
                    contents4 = new StringBuffer().append(contents4).append(PropertyAccessor.PROPERTY_KEY_PREFIX).toString();
                }
                if (buf[i2] >= 0 && buf[i2] < 16) {
                    contents4 = new StringBuffer().append(contents4).append("0").toString();
                }
                contents4 = new StringBuffer().append(contents4).append(Integer.toHexString(buf[i2] & 255)).toString();
                if (i2 % 4 == 3 && i2 < buf.length - 1) {
                    contents4 = new StringBuffer().append(contents4).append("]").append(LS).toString();
                }
            }
            contents = new StringBuffer().append(contents4).append("]").toString();
        }
        return new StringBuffer().append(name).append(" {").append(LS).append(contents).toString();
    }

    public Structure[] toArray(Structure[] array) throws Throwable {
        Class clsClass$;
        if (this.size == -1) {
            allocateMemory();
        }
        if (class$com$sun$jna$Memory == null) {
            clsClass$ = class$("com.sun.jna.Memory");
            class$com$sun$jna$Memory = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Memory;
        }
        if (clsClass$.equals(this.memory.getClass())) {
            Memory m = (Memory) this.memory;
            int requiredSize = array.length * size();
            if (m.getSize() < requiredSize) {
                Memory m2 = new Memory(requiredSize);
                m2.clear();
                useMemory(m2);
            }
        }
        array[0] = this;
        int size = size();
        for (int i = 1; i < array.length; i++) {
            array[i] = newInstance(getClass());
            array[i].useMemory(this.memory.share(i * size, size));
            array[i].read();
        }
        return array;
    }

    public Structure[] toArray(int size) {
        return toArray((Structure[]) Array.newInstance(getClass(), size));
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if ((o instanceof Structure) && ((Structure) o).size() == size()) {
            if (o.getClass().isAssignableFrom(getClass()) || getClass().isAssignableFrom(o.getClass())) {
                Structure s = (Structure) o;
                Pointer p1 = getPointer();
                Pointer p2 = s.getPointer();
                for (int i = 0; i < size(); i++) {
                    if (p1.getByte(i) != p2.getByte(i)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        Pointer p = getPointer();
        if (p != null) {
            return p.hashCode();
        }
        return 0;
    }

    Pointer getTypeInfo() {
        Pointer p = getTypeInfo(this);
        this.typeInfo = p.peer;
        return p;
    }

    public void setAutoSynch(boolean auto) {
        setAutoRead(auto);
        setAutoWrite(auto);
    }

    public void setAutoRead(boolean auto) {
        this.autoRead = auto;
    }

    public boolean getAutoRead() {
        return this.autoRead;
    }

    public void setAutoWrite(boolean auto) {
        this.autoWrite = auto;
    }

    public boolean getAutoWrite() {
        return this.autoWrite;
    }

    static Pointer getTypeInfo(Object obj) {
        return FFIType.get(obj);
    }

    public static Structure newInstance(Class type) throws IllegalArgumentException {
        try {
            Structure s = (Structure) type.newInstance();
            if (s instanceof ByValue) {
                s.allocateMemory();
            }
            return s;
        } catch (IllegalAccessException e) {
            String msg = new StringBuffer().append("Instantiation of ").append(type).append(" not allowed, is it public? (").append(e).append(")").toString();
            throw new IllegalArgumentException(msg);
        } catch (InstantiationException e2) {
            String msg2 = new StringBuffer().append("Can't instantiate ").append(type).append(" (").append(e2).append(")").toString();
            throw new IllegalArgumentException(msg2);
        }
    }

    /* loaded from: jna-3.0.9.jar:com/sun/jna/Structure$StructField.class */
    class StructField {
        public String name;
        public Class type;
        public Field field;
        public int size = -1;
        public int offset = -1;
        public boolean isVolatile;
        public FromNativeConverter readConverter;
        public ToNativeConverter writeConverter;
        public FromNativeContext context;
        private final Structure this$0;

        StructField(Structure structure) {
            this.this$0 = structure;
        }
    }

    /* loaded from: jna-3.0.9.jar:com/sun/jna/Structure$FFIType.class */
    private static class FFIType extends Structure {
        private static Map typeInfoMap = new WeakHashMap();
        private static final int FFI_TYPE_STRUCT = 13;
        public size_t size;
        public short alignment;
        public short type = 13;
        public Pointer elements;

        /* loaded from: jna-3.0.9.jar:com/sun/jna/Structure$FFIType$size_t.class */
        public static class size_t extends IntegerType {
            public size_t() {
                this(0L);
            }

            public size_t(long value) {
                super(Native.POINTER_SIZE, value);
            }
        }

        static {
            Class clsClass$;
            Class clsClass$2;
            Class clsClass$3;
            Class clsClass$4;
            Class clsClass$5;
            Class clsClass$6;
            Class clsClass$7;
            Class clsClass$8;
            Class clsClass$9;
            Class clsClass$10;
            Class clsClass$11;
            if (Native.POINTER_SIZE != 0) {
                if (FFITypes.ffi_type_void != null) {
                    typeInfoMap.put(Void.TYPE, FFITypes.ffi_type_void);
                    Map map = typeInfoMap;
                    if (Structure.class$java$lang$Void == null) {
                        clsClass$ = Structure.class$("java.lang.Void");
                        Structure.class$java$lang$Void = clsClass$;
                    } else {
                        clsClass$ = Structure.class$java$lang$Void;
                    }
                    map.put(clsClass$, FFITypes.ffi_type_void);
                    typeInfoMap.put(Float.TYPE, FFITypes.ffi_type_float);
                    Map map2 = typeInfoMap;
                    if (Structure.class$java$lang$Float == null) {
                        clsClass$2 = Structure.class$("java.lang.Float");
                        Structure.class$java$lang$Float = clsClass$2;
                    } else {
                        clsClass$2 = Structure.class$java$lang$Float;
                    }
                    map2.put(clsClass$2, FFITypes.ffi_type_float);
                    typeInfoMap.put(Double.TYPE, FFITypes.ffi_type_double);
                    Map map3 = typeInfoMap;
                    if (Structure.class$java$lang$Double == null) {
                        clsClass$3 = Structure.class$("java.lang.Double");
                        Structure.class$java$lang$Double = clsClass$3;
                    } else {
                        clsClass$3 = Structure.class$java$lang$Double;
                    }
                    map3.put(clsClass$3, FFITypes.ffi_type_double);
                    typeInfoMap.put(Long.TYPE, FFITypes.ffi_type_sint64);
                    Map map4 = typeInfoMap;
                    if (Structure.class$java$lang$Long == null) {
                        clsClass$4 = Structure.class$("java.lang.Long");
                        Structure.class$java$lang$Long = clsClass$4;
                    } else {
                        clsClass$4 = Structure.class$java$lang$Long;
                    }
                    map4.put(clsClass$4, FFITypes.ffi_type_sint64);
                    typeInfoMap.put(Integer.TYPE, FFITypes.ffi_type_sint32);
                    Map map5 = typeInfoMap;
                    if (Structure.class$java$lang$Integer == null) {
                        clsClass$5 = Structure.class$("java.lang.Integer");
                        Structure.class$java$lang$Integer = clsClass$5;
                    } else {
                        clsClass$5 = Structure.class$java$lang$Integer;
                    }
                    map5.put(clsClass$5, FFITypes.ffi_type_sint32);
                    typeInfoMap.put(Short.TYPE, FFITypes.ffi_type_sint16);
                    Map map6 = typeInfoMap;
                    if (Structure.class$java$lang$Short == null) {
                        clsClass$6 = Structure.class$("java.lang.Short");
                        Structure.class$java$lang$Short = clsClass$6;
                    } else {
                        clsClass$6 = Structure.class$java$lang$Short;
                    }
                    map6.put(clsClass$6, FFITypes.ffi_type_sint16);
                    Pointer ctype = Native.WCHAR_SIZE == 2 ? FFITypes.ffi_type_uint16 : FFITypes.ffi_type_uint32;
                    typeInfoMap.put(Character.TYPE, ctype);
                    Map map7 = typeInfoMap;
                    if (Structure.class$java$lang$Character == null) {
                        clsClass$7 = Structure.class$("java.lang.Character");
                        Structure.class$java$lang$Character = clsClass$7;
                    } else {
                        clsClass$7 = Structure.class$java$lang$Character;
                    }
                    map7.put(clsClass$7, ctype);
                    typeInfoMap.put(Byte.TYPE, FFITypes.ffi_type_sint8);
                    Map map8 = typeInfoMap;
                    if (Structure.class$java$lang$Byte == null) {
                        clsClass$8 = Structure.class$("java.lang.Byte");
                        Structure.class$java$lang$Byte = clsClass$8;
                    } else {
                        clsClass$8 = Structure.class$java$lang$Byte;
                    }
                    map8.put(clsClass$8, FFITypes.ffi_type_sint8);
                    Map map9 = typeInfoMap;
                    if (Structure.class$com$sun$jna$Pointer == null) {
                        clsClass$9 = Structure.class$("com.sun.jna.Pointer");
                        Structure.class$com$sun$jna$Pointer = clsClass$9;
                    } else {
                        clsClass$9 = Structure.class$com$sun$jna$Pointer;
                    }
                    map9.put(clsClass$9, FFITypes.ffi_type_pointer);
                    Map map10 = typeInfoMap;
                    if (Structure.class$java$lang$String == null) {
                        clsClass$10 = Structure.class$("java.lang.String");
                        Structure.class$java$lang$String = clsClass$10;
                    } else {
                        clsClass$10 = Structure.class$java$lang$String;
                    }
                    map10.put(clsClass$10, FFITypes.ffi_type_pointer);
                    Map map11 = typeInfoMap;
                    if (Structure.class$com$sun$jna$WString == null) {
                        clsClass$11 = Structure.class$("com.sun.jna.WString");
                        Structure.class$com$sun$jna$WString = clsClass$11;
                    } else {
                        clsClass$11 = Structure.class$com$sun$jna$WString;
                    }
                    map11.put(clsClass$11, FFITypes.ffi_type_pointer);
                    return;
                }
                throw new Error("FFI types not initialized");
            }
            throw new Error("Native library not initialized");
        }

        /* loaded from: jna-3.0.9.jar:com/sun/jna/Structure$FFIType$FFITypes.class */
        private static class FFITypes {
            private static Pointer ffi_type_void;
            private static Pointer ffi_type_float;
            private static Pointer ffi_type_double;
            private static Pointer ffi_type_longdouble;
            private static Pointer ffi_type_uint8;
            private static Pointer ffi_type_sint8;
            private static Pointer ffi_type_uint16;
            private static Pointer ffi_type_sint16;
            private static Pointer ffi_type_uint32;
            private static Pointer ffi_type_sint32;
            private static Pointer ffi_type_uint64;
            private static Pointer ffi_type_sint64;
            private static Pointer ffi_type_pointer;

            private FFITypes() {
            }
        }

        private FFIType(Structure ref) {
            Pointer[] els = new Pointer[ref.fields().size() + 1];
            int idx = 0;
            for (StructField sf : ref.fields().values()) {
                int i = idx;
                idx++;
                els[i] = get(ref.getField(sf), sf.type);
            }
            init(els);
        }

        private FFIType(Object array, Class type) {
            int length = Array.getLength(array);
            Pointer[] els = new Pointer[length + 1];
            Pointer p = get(null, type.getComponentType());
            for (int i = 0; i < length; i++) {
                els[i] = p;
            }
            init(els);
        }

        private void init(Pointer[] els) {
            this.elements = new Memory(Pointer.SIZE * els.length);
            this.elements.write(0L, els, 0, els.length);
            write();
        }

        static Pointer get(Object obj) {
            if (obj == null) {
                return FFITypes.ffi_type_pointer;
            }
            return get(obj, obj.getClass());
        }

        private static Pointer get(Object obj, Class cls) {
            Class clsClass$;
            Class clsClass$2;
            Class clsClass$3;
            Class clsClass$4;
            Class clsClass$5;
            Class clsClass$6;
            synchronized (typeInfoMap) {
                Object o = typeInfoMap.get(cls);
                if (o instanceof Pointer) {
                    return (Pointer) o;
                }
                if (o instanceof FFIType) {
                    return ((FFIType) o).getPointer();
                }
                if (Structure.class$java$nio$Buffer == null) {
                    clsClass$ = Structure.class$("java.nio.Buffer");
                    Structure.class$java$nio$Buffer = clsClass$;
                } else {
                    clsClass$ = Structure.class$java$nio$Buffer;
                }
                if (!clsClass$.isAssignableFrom(cls)) {
                    if (Structure.class$com$sun$jna$Callback == null) {
                        clsClass$2 = Structure.class$("com.sun.jna.Callback");
                        Structure.class$com$sun$jna$Callback = clsClass$2;
                    } else {
                        clsClass$2 = Structure.class$com$sun$jna$Callback;
                    }
                    if (!clsClass$2.isAssignableFrom(cls)) {
                        if (Structure.class$com$sun$jna$Structure == null) {
                            clsClass$3 = Structure.class$("com.sun.jna.Structure");
                            Structure.class$com$sun$jna$Structure = clsClass$3;
                        } else {
                            clsClass$3 = Structure.class$com$sun$jna$Structure;
                        }
                        if (clsClass$3.isAssignableFrom(cls)) {
                            if (obj == null) {
                                obj = newInstance(cls);
                            }
                            if (Structure.class$com$sun$jna$Structure$ByReference == null) {
                                clsClass$5 = Structure.class$("com.sun.jna.Structure$ByReference");
                                Structure.class$com$sun$jna$Structure$ByReference = clsClass$5;
                            } else {
                                clsClass$5 = Structure.class$com$sun$jna$Structure$ByReference;
                            }
                            if (clsClass$5.isAssignableFrom(cls)) {
                                typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
                                return FFITypes.ffi_type_pointer;
                            }
                            if (Structure.class$com$sun$jna$Union == null) {
                                clsClass$6 = Structure.class$("com.sun.jna.Union");
                                Structure.class$com$sun$jna$Union = clsClass$6;
                            } else {
                                clsClass$6 = Structure.class$com$sun$jna$Union;
                            }
                            if (clsClass$6.isAssignableFrom(cls)) {
                                return ((Union) obj).getTypeInfo();
                            }
                            FFIType type = new FFIType((Structure) obj);
                            typeInfoMap.put(cls, type);
                            return type.getPointer();
                        }
                        if (Structure.class$com$sun$jna$NativeMapped == null) {
                            clsClass$4 = Structure.class$("com.sun.jna.NativeMapped");
                            Structure.class$com$sun$jna$NativeMapped = clsClass$4;
                        } else {
                            clsClass$4 = Structure.class$com$sun$jna$NativeMapped;
                        }
                        if (clsClass$4.isAssignableFrom(cls)) {
                            NativeMappedConverter c = NativeMappedConverter.getInstance(cls);
                            return get(c.toNative(obj, new ToNativeContext()), c.nativeType());
                        }
                        if (cls.isArray()) {
                            FFIType type2 = new FFIType(obj, cls);
                            typeInfoMap.put(obj, type2);
                            return type2.getPointer();
                        }
                        throw new IllegalArgumentException(new StringBuffer().append("Unsupported structure field type ").append(cls).toString());
                    }
                }
                typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
                return FFITypes.ffi_type_pointer;
            }
        }
    }
}
