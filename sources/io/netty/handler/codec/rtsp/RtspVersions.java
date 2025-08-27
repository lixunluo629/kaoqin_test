package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.internal.ObjectUtil;
import org.bouncycastle.i18n.TextBundle;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/rtsp/RtspVersions.class */
public final class RtspVersions {
    public static final HttpVersion RTSP_1_0 = new HttpVersion("RTSP", 1, 0, true);

    public static HttpVersion valueOf(String text) {
        ObjectUtil.checkNotNull(text, TextBundle.TEXT_ENTRY);
        String text2 = text.trim().toUpperCase();
        if ("RTSP/1.0".equals(text2)) {
            return RTSP_1_0;
        }
        return new HttpVersion(text2, true);
    }

    private RtspVersions() {
    }
}
