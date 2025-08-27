package org.apache.ibatis.type;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/ByteArrayUtils.class */
class ByteArrayUtils {
    private ByteArrayUtils() {
    }

    static byte[] convertToPrimitiveArray(Byte[] objects) {
        byte[] bytes = new byte[objects.length];
        for (int i = 0; i < objects.length; i++) {
            bytes[i] = objects[i].byteValue();
        }
        return bytes;
    }

    static Byte[] convertToObjectArray(byte[] bytes) {
        Byte[] objects = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            objects[i] = Byte.valueOf(bytes[i]);
        }
        return objects;
    }
}
