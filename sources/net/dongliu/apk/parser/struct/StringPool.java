package net.dongliu.apk.parser.struct;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/StringPool.class */
public class StringPool {
    private String[] pool;

    public StringPool(int poolSize) {
        this.pool = new String[poolSize];
    }

    public String get(int idx) {
        return this.pool[idx];
    }

    public void set(int idx, String value) {
        this.pool[idx] = value;
    }
}
