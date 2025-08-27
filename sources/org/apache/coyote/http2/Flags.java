package org.apache.coyote.http2;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Flags.class */
public class Flags {
    private Flags() {
    }

    public static boolean isEndOfStream(int flags) {
        return (flags & 1) != 0;
    }

    public static boolean isAck(int flags) {
        return (flags & 1) != 0;
    }

    public static boolean isEndOfHeaders(int flags) {
        return (flags & 4) != 0;
    }

    public static boolean hasPadding(int flags) {
        return (flags & 8) != 0;
    }

    public static boolean hasPriority(int flags) {
        return (flags & 32) != 0;
    }
}
