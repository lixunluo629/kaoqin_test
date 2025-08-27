package net.dongliu.apk.parser.parser;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/StringPoolEntry.class */
public class StringPoolEntry {
    private int idx;
    private long offset;

    public StringPoolEntry(int idx, long offset) {
        this.idx = idx;
        this.offset = offset;
    }

    public int getIdx() {
        return this.idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public long getOffset() {
        return this.offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
