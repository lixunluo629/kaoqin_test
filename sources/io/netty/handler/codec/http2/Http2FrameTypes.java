package io.netty.handler.codec.http2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FrameTypes.class */
public final class Http2FrameTypes {
    public static final byte DATA = 0;
    public static final byte HEADERS = 1;
    public static final byte PRIORITY = 2;
    public static final byte RST_STREAM = 3;
    public static final byte SETTINGS = 4;
    public static final byte PUSH_PROMISE = 5;
    public static final byte PING = 6;
    public static final byte GO_AWAY = 7;
    public static final byte WINDOW_UPDATE = 8;
    public static final byte CONTINUATION = 9;

    private Http2FrameTypes() {
    }
}
