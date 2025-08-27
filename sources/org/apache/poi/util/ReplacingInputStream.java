package org.apache.poi.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/util/ReplacingInputStream.class */
public class ReplacingInputStream extends FilterInputStream {
    final int[] buf;
    private int matchedIndex;
    private int unbufferIndex;
    private int replacedIndex;
    private final byte[] pattern;
    private final byte[] replacement;
    private State state;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    /* loaded from: poi-3.17.jar:org/apache/poi/util/ReplacingInputStream$State.class */
    private enum State {
        NOT_MATCHED,
        MATCHING,
        REPLACING,
        UNBUFFER
    }

    public ReplacingInputStream(InputStream in, String pattern, String replacement) {
        this(in, pattern.getBytes(UTF8), replacement == null ? null : replacement.getBytes(UTF8));
    }

    public ReplacingInputStream(InputStream in, byte[] pattern, byte[] replacement) {
        super(in);
        this.matchedIndex = 0;
        this.unbufferIndex = 0;
        this.replacedIndex = 0;
        this.state = State.NOT_MATCHED;
        if (pattern == null || pattern.length == 0) {
            throw new IllegalArgumentException("pattern length should be > 0");
        }
        this.pattern = pattern;
        this.replacement = replacement;
        this.buf = new int[pattern.length];
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        int c;
        if (b == null) {
            throw new NullPointerException();
        }
        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        int c2 = read();
        if (c2 == -1) {
            return -1;
        }
        b[off] = (byte) c2;
        int i = 1;
        while (i < len && (c = read()) != -1) {
            b[off + i] = (byte) c;
            i++;
        }
        return i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        switch (this.state) {
            case NOT_MATCHED:
            default:
                int next = super.read();
                if (this.pattern[0] != next) {
                    return next;
                }
                Arrays.fill(this.buf, 0);
                this.matchedIndex = 0;
                int[] iArr = this.buf;
                int i = this.matchedIndex;
                this.matchedIndex = i + 1;
                iArr[i] = next;
                if (this.pattern.length == 1) {
                    this.state = State.REPLACING;
                    this.replacedIndex = 0;
                } else {
                    this.state = State.MATCHING;
                }
                return read();
            case MATCHING:
                int next2 = super.read();
                if (this.pattern[this.matchedIndex] == next2) {
                    int[] iArr2 = this.buf;
                    int i2 = this.matchedIndex;
                    this.matchedIndex = i2 + 1;
                    iArr2[i2] = next2;
                    if (this.matchedIndex == this.pattern.length) {
                        if (this.replacement == null || this.replacement.length == 0) {
                            this.state = State.NOT_MATCHED;
                            this.matchedIndex = 0;
                        } else {
                            this.state = State.REPLACING;
                            this.replacedIndex = 0;
                        }
                    }
                } else {
                    int[] iArr3 = this.buf;
                    int i3 = this.matchedIndex;
                    this.matchedIndex = i3 + 1;
                    iArr3[i3] = next2;
                    this.state = State.UNBUFFER;
                    this.unbufferIndex = 0;
                }
                return read();
            case REPLACING:
                byte[] bArr = this.replacement;
                int i4 = this.replacedIndex;
                this.replacedIndex = i4 + 1;
                byte b = bArr[i4];
                if (this.replacedIndex == this.replacement.length) {
                    this.state = State.NOT_MATCHED;
                    this.replacedIndex = 0;
                }
                return b;
            case UNBUFFER:
                int[] iArr4 = this.buf;
                int i5 = this.unbufferIndex;
                this.unbufferIndex = i5 + 1;
                int next3 = iArr4[i5];
                if (this.unbufferIndex == this.matchedIndex) {
                    this.state = State.NOT_MATCHED;
                    this.matchedIndex = 0;
                }
                return next3;
        }
    }

    public String toString() {
        return this.state.name() + SymbolConstants.SPACE_SYMBOL + this.matchedIndex + SymbolConstants.SPACE_SYMBOL + this.replacedIndex + SymbolConstants.SPACE_SYMBOL + this.unbufferIndex;
    }
}
