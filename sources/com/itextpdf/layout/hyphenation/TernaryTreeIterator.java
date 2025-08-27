package com.itextpdf.layout.hyphenation;

import java.util.Enumeration;
import java.util.Stack;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/TernaryTreeIterator.class */
class TernaryTreeIterator implements Enumeration {
    String curkey;
    TernaryTree tt;
    int cur = -1;
    Stack ns = new Stack();
    StringBuffer ks = new StringBuffer();

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/TernaryTreeIterator$Item.class */
    private class Item {
        char parent;
        char child;

        public Item() {
            this.parent = (char) 0;
            this.child = (char) 0;
        }

        public Item(char p, char c) {
            this.parent = p;
            this.child = c;
        }

        public Item(Item i) {
            this.parent = i.parent;
            this.child = i.child;
        }
    }

    public TernaryTreeIterator(TernaryTree tt) {
        this.tt = tt;
        reset();
    }

    public void reset() {
        this.ns.removeAllElements();
        this.ks.setLength(0);
        this.cur = this.tt.root;
        run();
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        String res = this.curkey;
        this.cur = up();
        run();
        return res;
    }

    public char getValue() {
        if (this.cur >= 0) {
            return this.tt.eq[this.cur];
        }
        return (char) 0;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return this.cur != -1;
    }

    /* JADX WARN: Incorrect condition in loop: B:14:0x003d */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int up() {
        /*
            Method dump skipped, instructions count: 310
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.layout.hyphenation.TernaryTreeIterator.up():int");
    }

    private int run() {
        if (this.cur == -1) {
            return -1;
        }
        boolean leaf = false;
        while (true) {
            if (this.cur != 0) {
                if (this.tt.sc[this.cur] == 65535) {
                    leaf = true;
                } else {
                    this.ns.push(new Item((char) this.cur, (char) 0));
                    if (this.tt.sc[this.cur] == 0) {
                        leaf = true;
                    } else {
                        this.cur = this.tt.lo[this.cur];
                    }
                }
            }
            if (!leaf) {
                this.cur = up();
                if (this.cur == -1) {
                    return -1;
                }
            } else {
                StringBuffer buf = new StringBuffer(this.ks.toString());
                if (this.tt.sc[this.cur] == 65535) {
                    int p = this.tt.lo[this.cur];
                    while (this.tt.kv.get(p) != 0) {
                        int i = p;
                        p++;
                        buf.append(this.tt.kv.get(i));
                    }
                }
                this.curkey = buf.toString();
                return 0;
            }
        }
    }
}
