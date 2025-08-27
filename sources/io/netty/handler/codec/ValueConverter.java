package io.netty.handler.codec;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/ValueConverter.class */
public interface ValueConverter<T> {
    T convertObject(Object obj);

    T convertBoolean(boolean z);

    boolean convertToBoolean(T t);

    T convertByte(byte b);

    byte convertToByte(T t);

    T convertChar(char c);

    char convertToChar(T t);

    T convertShort(short s);

    short convertToShort(T t);

    T convertInt(int i);

    int convertToInt(T t);

    T convertLong(long j);

    long convertToLong(T t);

    T convertTimeMillis(long j);

    long convertToTimeMillis(T t);

    T convertFloat(float f);

    float convertToFloat(T t);

    T convertDouble(double d);

    double convertToDouble(T t);
}
