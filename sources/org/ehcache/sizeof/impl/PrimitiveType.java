package org.ehcache.sizeof.impl;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/impl/PrimitiveType.class */
enum PrimitiveType {
    BOOLEAN(Boolean.TYPE, 1),
    BYTE(Byte.TYPE, 1),
    CHAR(Character.TYPE, 2),
    SHORT(Short.TYPE, 2),
    INT(Integer.TYPE, 4),
    FLOAT(Float.TYPE, 4),
    DOUBLE(Double.TYPE, 8),
    LONG(Long.TYPE, 8);

    private Class<?> type;
    private int size;

    PrimitiveType(Class cls, int size) {
        this.type = cls;
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public Class<?> getType() {
        return this.type;
    }

    public static int getReferenceSize() {
        return JvmInformation.CURRENT_JVM_INFORMATION.getJavaPointerSize();
    }

    public static long getArraySize() {
        return JvmInformation.CURRENT_JVM_INFORMATION.getObjectHeaderSize() + INT.getSize();
    }

    public static PrimitiveType forType(Class<?> type) {
        PrimitiveType[] arr$ = values();
        for (PrimitiveType primitiveType : arr$) {
            if (primitiveType.getType() == type) {
                return primitiveType;
            }
        }
        return null;
    }
}
