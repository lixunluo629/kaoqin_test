package org.apache.coyote.http2;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Setting.class */
public enum Setting {
    HEADER_TABLE_SIZE(1),
    ENABLE_PUSH(2),
    MAX_CONCURRENT_STREAMS(3),
    INITIAL_WINDOW_SIZE(4),
    MAX_FRAME_SIZE(5),
    MAX_HEADER_LIST_SIZE(6),
    UNKNOWN(Integer.MAX_VALUE);

    private final int id;

    Setting(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override // java.lang.Enum
    public String toString() {
        return Integer.toString(this.id);
    }

    public static Setting valueOf(int i) {
        switch (i) {
            case 1:
                return HEADER_TABLE_SIZE;
            case 2:
                return ENABLE_PUSH;
            case 3:
                return MAX_CONCURRENT_STREAMS;
            case 4:
                return INITIAL_WINDOW_SIZE;
            case 5:
                return MAX_FRAME_SIZE;
            case 6:
                return MAX_HEADER_LIST_SIZE;
            default:
                return UNKNOWN;
        }
    }
}
