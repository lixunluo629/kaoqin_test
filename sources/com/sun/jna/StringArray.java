package com.sun.jna;

import com.sun.jna.Function;
import java.util.ArrayList;
import java.util.List;

/* loaded from: jna-3.0.9.jar:com/sun/jna/StringArray.class */
public class StringArray extends Memory implements Function.PostCallRead {
    private boolean wide;
    private List natives;
    private Object[] original;

    public StringArray(String[] strings) {
        this(strings, false);
    }

    public StringArray(String[] strings, boolean wide) {
        this((Object[]) strings, wide);
    }

    public StringArray(WString[] strings) {
        this((Object[]) strings, true);
    }

    private StringArray(Object[] strings, boolean wide) {
        super((strings.length + 1) * Pointer.SIZE);
        this.natives = new ArrayList();
        this.original = strings;
        this.wide = wide;
        for (int i = 0; i < strings.length; i++) {
            Pointer p = null;
            if (strings[i] != null) {
                NativeString ns = new NativeString(strings[i].toString(), wide);
                this.natives.add(ns);
                p = ns.getPointer();
            }
            setPointer(Pointer.SIZE * i, p);
        }
        setPointer(Pointer.SIZE * strings.length, null);
    }

    @Override // com.sun.jna.Function.PostCallRead
    public void read() {
        boolean returnWide = this.original instanceof WString[];
        for (int si = 0; si < this.original.length; si++) {
            Pointer p = getPointer(si * Pointer.SIZE);
            Object s = null;
            if (p != null) {
                s = p.getString(0L, this.wide);
                if (returnWide) {
                    s = new WString((String) s);
                }
            }
            this.original[si] = s;
        }
    }
}
