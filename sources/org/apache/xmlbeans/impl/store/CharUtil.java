package org.apache.xmlbeans.impl.store;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.PrintStream;
import java.lang.ref.SoftReference;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/CharUtil.class */
public final class CharUtil {
    private static int CHARUTIL_INITIAL_BUFSIZE;
    private static ThreadLocal tl_charUtil;
    private CharIterator _charIter = new CharIterator();
    private static final int MAX_COPY = 64;
    private int _charBufSize;
    private int _currentOffset;
    private char[] _currentBuffer;
    public int _offSrc;
    public int _cchSrc;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CharUtil.class.desiredAssertionStatus();
        CHARUTIL_INITIAL_BUFSIZE = 32768;
        tl_charUtil = new ThreadLocal() { // from class: org.apache.xmlbeans.impl.store.CharUtil.1
            @Override // java.lang.ThreadLocal
            protected Object initialValue() {
                return new SoftReference(new CharUtil(CharUtil.CHARUTIL_INITIAL_BUFSIZE));
            }
        };
    }

    public CharUtil(int charBufSize) {
        this._charBufSize = charBufSize;
    }

    public CharIterator getCharIterator(Object src, int off, int cch) {
        this._charIter.init(src, off, cch);
        return this._charIter;
    }

    public CharIterator getCharIterator(Object src, int off, int cch, int start) {
        this._charIter.init(src, off, cch, start);
        return this._charIter;
    }

    public static CharUtil getThreadLocalCharUtil() {
        SoftReference softRef = (SoftReference) tl_charUtil.get();
        CharUtil charUtil = (CharUtil) softRef.get();
        if (charUtil == null) {
            charUtil = new CharUtil(CHARUTIL_INITIAL_BUFSIZE);
            tl_charUtil.set(new SoftReference(charUtil));
        }
        return charUtil;
    }

    public static void getString(StringBuffer sb, Object src, int off, int cch) {
        if (!$assertionsDisabled && !isValid(src, off, cch)) {
            throw new AssertionError();
        }
        if (cch == 0) {
            return;
        }
        if (src instanceof char[]) {
            sb.append((char[]) src, off, cch);
            return;
        }
        if (src instanceof String) {
            String s = (String) src;
            if (off == 0 && cch == s.length()) {
                sb.append((String) src);
                return;
            } else {
                sb.append(s.substring(off, off + cch));
                return;
            }
        }
        ((CharJoin) src).getString(sb, off, cch);
    }

    public static void getChars(char[] chars, int start, Object src, int off, int cch) {
        if (!$assertionsDisabled && !isValid(src, off, cch)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (chars == null || start < 0 || start > chars.length)) {
            throw new AssertionError();
        }
        if (cch == 0) {
            return;
        }
        if (src instanceof char[]) {
            System.arraycopy((char[]) src, off, chars, start, cch);
        } else if (src instanceof String) {
            ((String) src).getChars(off, off + cch, chars, start);
        } else {
            ((CharJoin) src).getChars(chars, start, off, cch);
        }
    }

    public static String getString(Object src, int off, int cch) {
        if (!$assertionsDisabled && !isValid(src, off, cch)) {
            throw new AssertionError();
        }
        if (cch == 0) {
            return "";
        }
        if (src instanceof char[]) {
            return new String((char[]) src, off, cch);
        }
        if (src instanceof String) {
            String s = (String) src;
            if (off == 0 && cch == s.length()) {
                return s;
            }
            return s.substring(off, off + cch);
        }
        StringBuffer sb = new StringBuffer();
        ((CharJoin) src).getString(sb, off, cch);
        return sb.toString();
    }

    public static final boolean isWhiteSpace(char ch2) {
        switch (ch2) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                return true;
            default:
                return false;
        }
    }

    public final boolean isWhiteSpace(Object src, int off, int cch) {
        if (!$assertionsDisabled && !isValid(src, off, cch)) {
            throw new AssertionError();
        }
        if (cch <= 0) {
            return true;
        }
        if (src instanceof char[]) {
            char[] chars = (char[]) src;
            while (cch > 0) {
                int i = off;
                off++;
                if (isWhiteSpace(chars[i])) {
                    cch--;
                } else {
                    return false;
                }
            }
            return true;
        }
        if (src instanceof String) {
            String s = (String) src;
            while (cch > 0) {
                int i2 = off;
                off++;
                if (isWhiteSpace(s.charAt(i2))) {
                    cch--;
                } else {
                    return false;
                }
            }
            return true;
        }
        boolean isWhite = true;
        this._charIter.init(src, off, cch);
        while (true) {
            if (!this._charIter.hasNext()) {
                break;
            }
            if (!isWhiteSpace(this._charIter.next())) {
                isWhite = false;
                break;
            }
        }
        this._charIter.release();
        return isWhite;
    }

    public Object stripLeft(Object src, int off, int cch) {
        if (!$assertionsDisabled && !isValid(src, off, cch)) {
            throw new AssertionError();
        }
        if (cch > 0) {
            if (src instanceof char[]) {
                char[] chars = (char[]) src;
                while (cch > 0 && isWhiteSpace(chars[off])) {
                    cch--;
                    off++;
                }
            } else if (src instanceof String) {
                String s = (String) src;
                while (cch > 0 && isWhiteSpace(s.charAt(off))) {
                    cch--;
                    off++;
                }
            } else {
                int count = 0;
                this._charIter.init(src, off, cch);
                while (this._charIter.hasNext() && isWhiteSpace(this._charIter.next())) {
                    count++;
                }
                this._charIter.release();
                off += count;
            }
        }
        if (cch == 0) {
            this._offSrc = 0;
            this._cchSrc = 0;
            return null;
        }
        this._offSrc = off;
        this._cchSrc = cch;
        return src;
    }

    public Object stripRight(Object src, int off, int cch) {
        if (!$assertionsDisabled && !isValid(src, off, cch)) {
            throw new AssertionError();
        }
        if (cch > 0) {
            this._charIter.init(src, off, cch, cch);
            while (this._charIter.hasPrev() && isWhiteSpace(this._charIter.prev())) {
                cch--;
            }
            this._charIter.release();
        }
        if (cch == 0) {
            this._offSrc = 0;
            this._cchSrc = 0;
            return null;
        }
        this._offSrc = off;
        this._cchSrc = cch;
        return src;
    }

    public Object insertChars(int posInsert, Object src, int off, int cch, Object srcInsert, int offInsert, int cchInsert) {
        CharJoin newJoin;
        Object newSrc;
        if (!$assertionsDisabled && !isValid(src, off, cch)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !isValid(srcInsert, offInsert, cchInsert)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (posInsert < 0 || posInsert > cch)) {
            throw new AssertionError();
        }
        if (cchInsert == 0) {
            this._cchSrc = cch;
            this._offSrc = off;
            return src;
        }
        if (cch == 0) {
            this._cchSrc = cchInsert;
            this._offSrc = offInsert;
            return srcInsert;
        }
        this._cchSrc = cch + cchInsert;
        if (this._cchSrc <= 64 && canAllocate(this._cchSrc)) {
            char[] c = allocate(this._cchSrc);
            getChars(c, this._offSrc, src, off, posInsert);
            getChars(c, this._offSrc + posInsert, srcInsert, offInsert, cchInsert);
            getChars(c, this._offSrc + posInsert + cchInsert, src, off + posInsert, cch - posInsert);
            newSrc = c;
        } else {
            this._offSrc = 0;
            if (posInsert == 0) {
                newJoin = new CharJoin(srcInsert, offInsert, cchInsert, src, off);
            } else if (posInsert == cch) {
                newJoin = new CharJoin(src, off, cch, srcInsert, offInsert);
            } else {
                CharJoin j = new CharJoin(src, off, posInsert, srcInsert, offInsert);
                newJoin = new CharJoin(j, 0, posInsert + cchInsert, src, off + posInsert);
            }
            if (newJoin._depth > 64) {
                newSrc = saveChars(newJoin, this._offSrc, this._cchSrc);
            } else {
                newSrc = newJoin;
            }
        }
        if ($assertionsDisabled || isValid(newSrc, this._offSrc, this._cchSrc)) {
            return newSrc;
        }
        throw new AssertionError();
    }

    public Object removeChars(int posRemove, int cchRemove, Object src, int off, int cch) {
        Object newSrc;
        if (!$assertionsDisabled && !isValid(src, off, cch)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (posRemove < 0 || posRemove > cch)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (cchRemove < 0 || posRemove + cchRemove > cch)) {
            throw new AssertionError();
        }
        this._cchSrc = cch - cchRemove;
        if (this._cchSrc == 0) {
            newSrc = null;
            this._offSrc = 0;
        } else if (posRemove == 0) {
            newSrc = src;
            this._offSrc = off + cchRemove;
        } else if (posRemove + cchRemove == cch) {
            newSrc = src;
            this._offSrc = off;
        } else {
            int cchAfter = cch - cchRemove;
            if (cchAfter <= 64 && canAllocate(cchAfter)) {
                char[] chars = allocate(cchAfter);
                getChars(chars, this._offSrc, src, off, posRemove);
                getChars(chars, this._offSrc + posRemove, src, off + posRemove + cchRemove, (cch - posRemove) - cchRemove);
                newSrc = chars;
                this._offSrc = this._offSrc;
            } else {
                CharJoin j = new CharJoin(src, off, posRemove, src, off + posRemove + cchRemove);
                if (j._depth > 64) {
                    newSrc = saveChars(j, 0, this._cchSrc);
                } else {
                    newSrc = j;
                    this._offSrc = 0;
                }
            }
        }
        if ($assertionsDisabled || isValid(newSrc, this._offSrc, this._cchSrc)) {
            return newSrc;
        }
        throw new AssertionError();
    }

    private static int sizeof(Object src) {
        if (!$assertionsDisabled && src != null && !(src instanceof String) && !(src instanceof char[])) {
            throw new AssertionError();
        }
        if (src instanceof char[]) {
            return ((char[]) src).length;
        }
        if (src == null) {
            return 0;
        }
        return ((String) src).length();
    }

    private boolean canAllocate(int cch) {
        return this._currentBuffer == null || this._currentBuffer.length - this._currentOffset >= cch;
    }

    private char[] allocate(int cch) {
        if (!$assertionsDisabled && this._currentBuffer != null && this._currentBuffer.length - this._currentOffset <= 0) {
            throw new AssertionError();
        }
        if (this._currentBuffer == null) {
            this._currentBuffer = new char[Math.max(cch, this._charBufSize)];
            this._currentOffset = 0;
        }
        this._offSrc = this._currentOffset;
        this._cchSrc = Math.min(this._currentBuffer.length - this._currentOffset, cch);
        char[] retBuf = this._currentBuffer;
        if (!$assertionsDisabled && this._currentOffset + this._cchSrc > this._currentBuffer.length) {
            throw new AssertionError();
        }
        int i = this._currentOffset + this._cchSrc;
        this._currentOffset = i;
        if (i == this._currentBuffer.length) {
            this._currentBuffer = null;
            this._currentOffset = 0;
        }
        return retBuf;
    }

    public Object saveChars(Object srcSave, int offSave, int cchSave) {
        return saveChars(srcSave, offSave, cchSave, null, 0, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00fe  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object saveChars(java.lang.Object r9, int r10, int r11, java.lang.Object r12, int r13, int r14) {
        /*
            Method dump skipped, instructions count: 493
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.CharUtil.saveChars(java.lang.Object, int, int, java.lang.Object, int, int):java.lang.Object");
    }

    private static void dumpText(PrintStream o, String s) {
        o.print(SymbolConstants.QUOTES_SYMBOL);
        int i = 0;
        while (true) {
            if (i >= s.length()) {
                break;
            }
            char ch2 = s.charAt(i);
            if (i == 36) {
                o.print("...");
                break;
            }
            if (ch2 == '\n') {
                o.print("\\n");
            } else if (ch2 == '\r') {
                o.print("\\r");
            } else if (ch2 == '\t') {
                o.print("\\t");
            } else if (ch2 == '\f') {
                o.print("\\f");
            } else if (ch2 == '\f') {
                o.print("\\f");
            } else if (ch2 == '\"') {
                o.print("\\\"");
            } else {
                o.print(ch2);
            }
            i++;
        }
        o.print(SymbolConstants.QUOTES_SYMBOL);
    }

    public static void dump(Object src, int off, int cch) {
        dumpChars(System.out, src, off, cch);
        System.out.println();
    }

    public static void dumpChars(PrintStream p, Object src, int off, int cch) {
        p.print("off=" + off + ", cch=" + cch + ", ");
        if (src == null) {
            p.print("<null-src>");
            return;
        }
        if (src instanceof String) {
            String s = (String) src;
            p.print("String");
            if ((off != 0 || cch != s.length()) && (off < 0 || off > s.length() || off + cch < 0 || off + cch > s.length())) {
                p.print(" (Error)");
                return;
            } else {
                dumpText(p, s.substring(off, off + cch));
                return;
            }
        }
        if (src instanceof char[]) {
            char[] chars = (char[]) src;
            p.print("char[]");
            if ((off != 0 || cch != chars.length) && (off < 0 || off > chars.length || off + cch < 0 || off + cch > chars.length)) {
                p.print(" (Error)");
                return;
            } else {
                dumpText(p, new String(chars, off, cch));
                return;
            }
        }
        if (src instanceof CharJoin) {
            p.print("CharJoin");
            ((CharJoin) src).dumpChars(p, off, cch);
        } else {
            p.print("Unknown text source");
        }
    }

    public static boolean isValid(Object src, int off, int cch) {
        if (cch < 0 || off < 0) {
            return false;
        }
        if (src == null) {
            return off == 0 && cch == 0;
        }
        if (src instanceof char[]) {
            char[] c = (char[]) src;
            return off <= c.length && off + cch <= c.length;
        }
        if (src instanceof String) {
            String s = (String) src;
            return off <= s.length() && off + cch <= s.length();
        }
        if (src instanceof CharJoin) {
            return ((CharJoin) src).isValid(off, cch);
        }
        return false;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/CharUtil$CharJoin.class */
    public static final class CharJoin {
        public final Object _srcLeft;
        public final int _offLeft;
        public final int _cchLeft;
        public final Object _srcRight;
        public final int _offRight;
        public final int _depth;
        static final int MAX_DEPTH = 64;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !CharUtil.class.desiredAssertionStatus();
        }

        public CharJoin(Object srcLeft, int offLeft, int cchLeft, Object srcRight, int offRight) {
            int rightDepth;
            this._srcLeft = srcLeft;
            this._offLeft = offLeft;
            this._cchLeft = cchLeft;
            this._srcRight = srcRight;
            this._offRight = offRight;
            int depth = 0;
            depth = srcLeft instanceof CharJoin ? ((CharJoin) srcLeft)._depth : depth;
            if ((srcRight instanceof CharJoin) && (rightDepth = ((CharJoin) srcRight)._depth) > depth) {
                depth = rightDepth;
            }
            this._depth = depth + 1;
            if (!$assertionsDisabled && this._depth > 66) {
                throw new AssertionError();
            }
        }

        private int cchRight(int off, int cch) {
            return Math.max(0, (cch - this._cchLeft) - off);
        }

        public int depth() {
            int depth = 0;
            if (this._srcLeft instanceof CharJoin) {
                depth = ((CharJoin) this._srcLeft).depth();
            }
            if (this._srcRight instanceof CharJoin) {
                depth = Math.max(((CharJoin) this._srcRight).depth(), depth);
            }
            return depth + 1;
        }

        public boolean isValid(int off, int cch) {
            if (this._depth > 2) {
                return true;
            }
            if (!$assertionsDisabled && this._depth != depth()) {
                throw new AssertionError();
            }
            if (off < 0 || cch < 0 || !CharUtil.isValid(this._srcLeft, this._offLeft, this._cchLeft) || !CharUtil.isValid(this._srcRight, this._offRight, cchRight(off, cch))) {
                return false;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getString(StringBuffer sb, int off, int cch) {
            if (!$assertionsDisabled && cch <= 0) {
                throw new AssertionError();
            }
            if (off < this._cchLeft) {
                int cchL = Math.min(this._cchLeft - off, cch);
                CharUtil.getString(sb, this._srcLeft, this._offLeft + off, cchL);
                if (cch > cchL) {
                    CharUtil.getString(sb, this._srcRight, this._offRight, cch - cchL);
                    return;
                }
                return;
            }
            CharUtil.getString(sb, this._srcRight, (this._offRight + off) - this._cchLeft, cch);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getChars(char[] chars, int start, int off, int cch) {
            if (!$assertionsDisabled && cch <= 0) {
                throw new AssertionError();
            }
            if (off < this._cchLeft) {
                int cchL = Math.min(this._cchLeft - off, cch);
                CharUtil.getChars(chars, start, this._srcLeft, this._offLeft + off, cchL);
                if (cch > cchL) {
                    CharUtil.getChars(chars, start + cchL, this._srcRight, this._offRight, cch - cchL);
                    return;
                }
                return;
            }
            CharUtil.getChars(chars, start, this._srcRight, (this._offRight + off) - this._cchLeft, cch);
        }

        private void dumpChars(int off, int cch) {
            dumpChars(System.out, off, cch);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpChars(PrintStream p, int off, int cch) {
            p.print("( ");
            CharUtil.dumpChars(p, this._srcLeft, this._offLeft, this._cchLeft);
            p.print(", ");
            CharUtil.dumpChars(p, this._srcRight, this._offRight, cchRight(off, cch));
            p.print(" )");
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/CharUtil$CharIterator.class */
    public static final class CharIterator {
        private Object _srcRoot;
        private int _offRoot;
        private int _cchRoot;
        private int _pos;
        private int _minPos;
        private int _maxPos;
        private int _offLeaf;
        private String _srcLeafString;
        private char[] _srcLeafChars;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !CharUtil.class.desiredAssertionStatus();
        }

        public void init(Object src, int off, int cch) {
            init(src, off, cch, 0);
        }

        public void init(Object src, int off, int cch, int startPos) {
            if (!$assertionsDisabled && !CharUtil.isValid(src, off, cch)) {
                throw new AssertionError();
            }
            release();
            this._srcRoot = src;
            this._offRoot = off;
            this._cchRoot = cch;
            this._maxPos = -1;
            this._minPos = -1;
            movePos(startPos);
        }

        public void release() {
            this._srcRoot = null;
            this._srcLeafString = null;
            this._srcLeafChars = null;
        }

        public boolean hasNext() {
            return this._pos < this._cchRoot;
        }

        public boolean hasPrev() {
            return this._pos > 0;
        }

        public char next() {
            if (!$assertionsDisabled && !hasNext()) {
                throw new AssertionError();
            }
            char ch2 = currentChar();
            movePos(this._pos + 1);
            return ch2;
        }

        public char prev() {
            if (!$assertionsDisabled && !hasPrev()) {
                throw new AssertionError();
            }
            movePos(this._pos - 1);
            return currentChar();
        }

        public void movePos(int newPos) {
            if (!$assertionsDisabled && (newPos < 0 || newPos > this._cchRoot)) {
                throw new AssertionError();
            }
            if (newPos < this._minPos || newPos > this._maxPos) {
                Object src = this._srcRoot;
                int off = this._offRoot + newPos;
                int cch = this._cchRoot;
                this._offLeaf = this._offRoot;
                while (src instanceof CharJoin) {
                    CharJoin j = (CharJoin) src;
                    if (off < j._cchLeft) {
                        src = j._srcLeft;
                        this._offLeaf = j._offLeft;
                        off += j._offLeft;
                        cch = j._cchLeft;
                    } else {
                        src = j._srcRight;
                        this._offLeaf = j._offRight;
                        off -= j._cchLeft - j._offRight;
                        cch -= j._cchLeft;
                    }
                }
                this._minPos = newPos - (off - this._offLeaf);
                this._maxPos = this._minPos + cch;
                if (newPos < this._cchRoot) {
                    this._maxPos--;
                }
                this._srcLeafChars = null;
                this._srcLeafString = null;
                if (src instanceof char[]) {
                    this._srcLeafChars = (char[]) src;
                } else {
                    this._srcLeafString = (String) src;
                }
                if (!$assertionsDisabled && (newPos < this._minPos || newPos > this._maxPos)) {
                    throw new AssertionError();
                }
            }
            this._pos = newPos;
        }

        private char currentChar() {
            int i = (this._offLeaf + this._pos) - this._minPos;
            return this._srcLeafChars == null ? this._srcLeafString.charAt(i) : this._srcLeafChars[i];
        }
    }

    public static void clearThreadLocals() {
        tl_charUtil.remove();
    }
}
