package io.netty.handler.codec.spdy;

import java.util.Set;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdySettingsFrame.class */
public interface SpdySettingsFrame extends SpdyFrame {
    public static final int SETTINGS_MINOR_VERSION = 0;
    public static final int SETTINGS_UPLOAD_BANDWIDTH = 1;
    public static final int SETTINGS_DOWNLOAD_BANDWIDTH = 2;
    public static final int SETTINGS_ROUND_TRIP_TIME = 3;
    public static final int SETTINGS_MAX_CONCURRENT_STREAMS = 4;
    public static final int SETTINGS_CURRENT_CWND = 5;
    public static final int SETTINGS_DOWNLOAD_RETRANS_RATE = 6;
    public static final int SETTINGS_INITIAL_WINDOW_SIZE = 7;
    public static final int SETTINGS_CLIENT_CERTIFICATE_VECTOR_SIZE = 8;

    Set<Integer> ids();

    boolean isSet(int i);

    int getValue(int i);

    SpdySettingsFrame setValue(int i, int i2);

    SpdySettingsFrame setValue(int i, int i2, boolean z, boolean z2);

    SpdySettingsFrame removeValue(int i);

    boolean isPersistValue(int i);

    SpdySettingsFrame setPersistValue(int i, boolean z);

    boolean isPersisted(int i);

    SpdySettingsFrame setPersisted(int i, boolean z);

    boolean clearPreviouslyPersistedSettings();

    SpdySettingsFrame setClearPreviouslyPersistedSettings(boolean z);
}
