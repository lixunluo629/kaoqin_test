package com.sun.jna;

import java.nio.CharBuffer;

/* loaded from: jna-3.0.9.jar:com/sun/jna/WString.class */
public final class WString implements CharSequence, Comparable {
    private String string;

    public WString(String s) {
        this.string = s;
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.string;
    }

    public boolean equals(Object o) {
        return (o instanceof WString) && toString().equals(o.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        return toString().compareTo(o.toString());
    }

    @Override // java.lang.CharSequence
    public int length() {
        return toString().length();
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return CharBuffer.wrap(toString()).subSequence(start, end);
    }
}
