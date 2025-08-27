package com.itextpdf.layout.hyphenation;

import java.io.Serializable;
import java.util.Enumeration;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/TernaryTree.class */
public class TernaryTree implements Serializable {
    private static final long serialVersionUID = 3175412271203716160L;
    protected char[] lo;
    protected char[] hi;
    protected char[] eq;
    protected char[] sc;
    protected CharVector kv;
    protected char root;
    protected char freenode;
    protected int length;
    protected static final int BLOCK_SIZE = 2048;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TernaryTree.class.desiredAssertionStatus();
    }

    TernaryTree() {
        init();
    }

    TernaryTree(TernaryTree tt) {
        this.root = tt.root;
        this.freenode = tt.freenode;
        this.length = tt.length;
        this.lo = (char[]) tt.lo.clone();
        this.hi = (char[]) tt.hi.clone();
        this.eq = (char[]) tt.eq.clone();
        this.sc = (char[]) tt.sc.clone();
        this.kv = new CharVector(tt.kv);
    }

    protected void init() {
        this.root = (char) 0;
        this.freenode = (char) 1;
        this.length = 0;
        this.lo = new char[2048];
        this.hi = new char[2048];
        this.eq = new char[2048];
        this.sc = new char[2048];
        this.kv = new CharVector();
    }

    public void insert(String key, char val) {
        int len = key.length() + 1;
        if (this.freenode + len > this.eq.length) {
            redimNodeArrays(this.eq.length + 2048);
        }
        int len2 = len - 1;
        char[] strkey = new char[len];
        key.getChars(0, len2, strkey, 0);
        strkey[len2] = 0;
        this.root = insert(new TreeInsertionParams(this.root, strkey, 0, val));
    }

    public void insert(char[] key, int start, char val) {
        int len = strlen(key) + 1;
        if (this.freenode + len > this.eq.length) {
            redimNodeArrays(this.eq.length + 2048);
        }
        this.root = insert(new TreeInsertionParams(this.root, key, start, val));
    }

    private Character insertNewBranchIfNeeded(TreeInsertionParams params) {
        char p = params.p;
        char[] key = params.key;
        int start = params.start;
        char val = params.val;
        int len = strlen(key, start);
        if (p == 0) {
            char p2 = this.freenode;
            this.freenode = (char) (p2 + 1);
            this.eq[p2] = val;
            this.length++;
            this.hi[p2] = 0;
            if (len > 0) {
                this.sc[p2] = 65535;
                this.lo[p2] = (char) this.kv.alloc(len + 1);
                strcpy(this.kv.getArray(), this.lo[p2], key, start);
            } else {
                this.sc[p2] = 0;
                this.lo[p2] = 0;
            }
            return Character.valueOf(p2);
        }
        return null;
    }

    private char insertIntoExistingBranch(TreeInsertionParams params) {
        char initialP = params.p;
        TreeInsertionParams paramsToInsertNext = params;
        while (true) {
            if (paramsToInsertNext == null) {
                break;
            }
            char p = paramsToInsertNext.p;
            if (!$assertionsDisabled && p == 0) {
                throw new AssertionError();
            }
            char[] key = paramsToInsertNext.key;
            int start = paramsToInsertNext.start;
            char val = paramsToInsertNext.val;
            int len = strlen(key, start);
            paramsToInsertNext = null;
            if (this.sc[p] == 65535) {
                char pp = this.freenode;
                this.freenode = (char) (pp + 1);
                this.lo[pp] = this.lo[p];
                this.eq[pp] = this.eq[p];
                this.lo[p] = 0;
                if (len > 0) {
                    this.sc[p] = this.kv.get(this.lo[pp]);
                    this.eq[p] = pp;
                    char[] cArr = this.lo;
                    cArr[pp] = (char) (cArr[pp] + 1);
                    if (this.kv.get(this.lo[pp]) == 0) {
                        this.lo[pp] = 0;
                        this.sc[pp] = 0;
                        this.hi[pp] = 0;
                    } else {
                        this.sc[pp] = 65535;
                    }
                } else {
                    this.sc[pp] = 65535;
                    this.hi[p] = pp;
                    this.sc[p] = 0;
                    this.eq[p] = val;
                    this.length++;
                    break;
                }
            }
            char s = key[start];
            if (s < this.sc[p]) {
                TreeInsertionParams branchParams = new TreeInsertionParams(this.lo[p], key, start, val);
                Character insertNew = insertNewBranchIfNeeded(branchParams);
                if (insertNew == null) {
                    paramsToInsertNext = branchParams;
                } else {
                    this.lo[p] = insertNew.charValue();
                }
            } else if (s == this.sc[p]) {
                if (s != 0) {
                    TreeInsertionParams branchParams2 = new TreeInsertionParams(this.eq[p], key, start + 1, val);
                    Character insertNew2 = insertNewBranchIfNeeded(branchParams2);
                    if (insertNew2 == null) {
                        paramsToInsertNext = branchParams2;
                    } else {
                        this.eq[p] = insertNew2.charValue();
                    }
                } else {
                    this.eq[p] = val;
                }
            } else {
                TreeInsertionParams branchParams3 = new TreeInsertionParams(this.hi[p], key, start, val);
                Character insertNew3 = insertNewBranchIfNeeded(branchParams3);
                if (insertNew3 == null) {
                    paramsToInsertNext = branchParams3;
                } else {
                    this.hi[p] = insertNew3.charValue();
                }
            }
        }
        return initialP;
    }

    private char insert(TreeInsertionParams params) {
        Character newBranch = insertNewBranchIfNeeded(params);
        if (newBranch == null) {
            return insertIntoExistingBranch(params);
        }
        return newBranch.charValue();
    }

    public static int strcmp(char[] a, int startA, char[] b, int startB) {
        while (a[startA] == b[startB]) {
            if (a[startA] != 0) {
                startA++;
                startB++;
            } else {
                return 0;
            }
        }
        return a[startA] - b[startB];
    }

    public static int strcmp(String str, char[] a, int start) {
        int len = str.length();
        int i = 0;
        while (i < len) {
            int d = str.charAt(i) - a[start + i];
            if (d != 0) {
                return d;
            }
            if (a[start + i] != 0) {
                i++;
            } else {
                return d;
            }
        }
        if (a[start + i] != 0) {
            return -a[start + i];
        }
        return 0;
    }

    public static void strcpy(char[] dst, int di, char[] src, int si) {
        while (src[si] != 0) {
            int i = di;
            di++;
            int i2 = si;
            si++;
            dst[i] = src[i2];
        }
        dst[di] = 0;
    }

    public static int strlen(char[] a, int start) {
        int len = 0;
        for (int i = start; i < a.length && a[i] != 0; i++) {
            len++;
        }
        return len;
    }

    public static int strlen(char[] a) {
        return strlen(a, 0);
    }

    public int find(String key) {
        int len = key.length();
        char[] strkey = new char[len + 1];
        key.getChars(0, len, strkey, 0);
        strkey[len] = 0;
        return find(strkey, 0);
    }

    public int find(char[] key, int start) {
        char p = this.root;
        int i = start;
        while (p != 0) {
            if (this.sc[p] == 65535) {
                if (strcmp(key, i, this.kv.getArray(), this.lo[p]) == 0) {
                    return this.eq[p];
                }
                return -1;
            }
            char c = key[i];
            int d = c - this.sc[p];
            if (d == 0) {
                if (c == 0) {
                    return this.eq[p];
                }
                i++;
                p = this.eq[p];
            } else if (d < 0) {
                p = this.lo[p];
            } else {
                p = this.hi[p];
            }
        }
        return -1;
    }

    public boolean knows(String key) {
        return find(key) >= 0;
    }

    private void redimNodeArrays(int newsize) {
        int len = newsize < this.lo.length ? newsize : this.lo.length;
        char[] na = new char[newsize];
        System.arraycopy(this.lo, 0, na, 0, len);
        this.lo = na;
        char[] na2 = new char[newsize];
        System.arraycopy(this.hi, 0, na2, 0, len);
        this.hi = na2;
        char[] na3 = new char[newsize];
        System.arraycopy(this.eq, 0, na3, 0, len);
        this.eq = na3;
        char[] na4 = new char[newsize];
        System.arraycopy(this.sc, 0, na4, 0, len);
        this.sc = na4;
    }

    public int size() {
        return this.length;
    }

    protected void insertBalanced(String[] k, char[] v, int offset, int n) {
        if (n < 1) {
            return;
        }
        int m = n >> 1;
        insert(k[m + offset], v[m + offset]);
        insertBalanced(k, v, offset, m);
        insertBalanced(k, v, offset + m + 1, (n - m) - 1);
    }

    public void balance() {
        int i = 0;
        int n = this.length;
        String[] k = new String[n];
        char[] v = new char[n];
        TernaryTreeIterator iter = new TernaryTreeIterator(this);
        while (iter.hasMoreElements()) {
            v[i] = iter.getValue();
            int i2 = i;
            i++;
            k[i2] = (String) iter.nextElement();
        }
        init();
        insertBalanced(k, v, 0, n);
    }

    public void trimToSize() {
        balance();
        redimNodeArrays(this.freenode);
        CharVector kx = new CharVector();
        kx.alloc(1);
        TernaryTree map = new TernaryTree();
        compact(kx, map, this.root);
        this.kv = kx;
        this.kv.trimToSize();
    }

    private void compact(CharVector kx, TernaryTree map, char p) {
        if (p == 0) {
            return;
        }
        if (this.sc[p] == 65535) {
            int k = map.find(this.kv.getArray(), this.lo[p]);
            if (k < 0) {
                k = kx.alloc(strlen(this.kv.getArray(), this.lo[p]) + 1);
                strcpy(kx.getArray(), k, this.kv.getArray(), this.lo[p]);
                map.insert(kx.getArray(), k, (char) k);
            }
            this.lo[p] = (char) k;
            return;
        }
        compact(kx, map, this.lo[p]);
        if (this.sc[p] != 0) {
            compact(kx, map, this.eq[p]);
        }
        compact(kx, map, this.hi[p]);
    }

    public Enumeration keys() {
        return new TernaryTreeIterator(this);
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/TernaryTree$TreeInsertionParams.class */
    private static class TreeInsertionParams {
        char p;
        char[] key;
        int start;
        char val;

        public TreeInsertionParams(char p, char[] key, int start, char val) {
            this.p = p;
            this.key = key;
            this.start = start;
            this.val = val;
        }
    }
}
