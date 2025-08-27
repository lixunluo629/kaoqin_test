package com.sun.jna;

import java.nio.CharBuffer;

/* loaded from: jna-3.0.9.jar:com/sun/jna/NativeString.class */
class NativeString implements CharSequence, Comparable {
    private String value;
    private Pointer pointer;

    public NativeString(String string) {
        this(string, false);
    }

    public NativeString(String string, boolean wide) {
        this.value = string;
        if (string == null) {
            throw new NullPointerException("String must not be null");
        }
        if (wide) {
            int len = (string.length() + 1) * Native.WCHAR_SIZE;
            this.pointer = new Memory(len);
            this.pointer.setString(0L, string, true);
        } else {
            byte[] data = Native.getBytes(string);
            this.pointer = new Memory(data.length + 1);
            this.pointer.setString(0L, string);
        }
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public boolean equals(Object other) {
        return (other instanceof CharSequence) && compareTo(other) == 0;
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.value;
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return this.value.charAt(index);
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.value.length();
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return CharBuffer.wrap(this.value).subSequence(start, end);
    }

    @Override // java.lang.Comparable
    public int compareTo(Object other) {
        if (other == null) {
            return 1;
        }
        return this.value.compareTo(other.toString());
    }
}
