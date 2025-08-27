package org.apache.poi.util;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/util/ShortField.class */
public class ShortField implements FixedField {
    private short _value;
    private final int _offset;

    public ShortField(int offset) throws ArrayIndexOutOfBoundsException {
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Illegal offset: " + offset);
        }
        this._offset = offset;
    }

    public ShortField(int offset, short value) throws ArrayIndexOutOfBoundsException {
        this(offset);
        set(value);
    }

    public ShortField(int offset, byte[] data) throws ArrayIndexOutOfBoundsException {
        this(offset);
        readFromBytes(data);
    }

    public ShortField(int offset, short value, byte[] data) throws ArrayIndexOutOfBoundsException {
        this(offset);
        set(value, data);
    }

    public short get() {
        return this._value;
    }

    public void set(short value) {
        this._value = value;
    }

    public void set(short value, byte[] data) throws ArrayIndexOutOfBoundsException {
        this._value = value;
        writeToBytes(data);
    }

    @Override // org.apache.poi.util.FixedField
    public void readFromBytes(byte[] data) throws ArrayIndexOutOfBoundsException {
        this._value = LittleEndian.getShort(data, this._offset);
    }

    @Override // org.apache.poi.util.FixedField
    public void readFromStream(InputStream stream) throws IOException {
        this._value = LittleEndian.readShort(stream);
    }

    @Override // org.apache.poi.util.FixedField
    public void writeToBytes(byte[] data) throws ArrayIndexOutOfBoundsException {
        LittleEndian.putShort(data, this._offset, this._value);
    }

    @Override // org.apache.poi.util.FixedField
    public String toString() {
        return String.valueOf((int) this._value);
    }
}
