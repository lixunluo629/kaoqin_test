package org.aspectj.util;

import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/TypeSafeEnum.class */
public class TypeSafeEnum {
    private byte key;
    private String name;

    public TypeSafeEnum(String name, int key) {
        this.name = name;
        if (key > 127 || key < -128) {
            throw new IllegalArgumentException("key doesn't fit into a byte: " + key);
        }
        this.key = (byte) key;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public byte getKey() {
        return this.key;
    }

    public void write(DataOutputStream s) throws IOException {
        s.writeByte(this.key);
    }
}
