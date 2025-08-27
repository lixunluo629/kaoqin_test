package org.apache.coyote.http2;

import java.lang.Throwable;
import java.util.HashMap;
import java.util.Map;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/ConnectionSettingsBase.class */
public abstract class ConnectionSettingsBase<T extends Throwable> {
    private final String connectionId;
    protected static final int MAX_WINDOW_SIZE = Integer.MAX_VALUE;
    protected static final int MIN_MAX_FRAME_SIZE = 16384;
    protected static final int MAX_MAX_FRAME_SIZE = 16777215;
    protected static final long UNLIMITED = 4294967296L;
    protected static final int MAX_HEADER_TABLE_SIZE = 65536;
    protected static final int DEFAULT_HEADER_TABLE_SIZE = 4096;
    protected static final boolean DEFAULT_ENABLE_PUSH = true;
    protected static final long DEFAULT_MAX_CONCURRENT_STREAMS = 4294967296L;
    protected static final int DEFAULT_INITIAL_WINDOW_SIZE = 65535;
    protected static final int DEFAULT_MAX_FRAME_SIZE = 16384;
    protected static final long DEFAULT_MAX_HEADER_LIST_SIZE = 32768;
    private final Log log = LogFactory.getLog((Class<?>) ConnectionSettingsBase.class);
    private final StringManager sm = StringManager.getManager((Class<?>) ConnectionSettingsBase.class);
    protected Map<Setting, Long> current = new HashMap();
    protected Map<Setting, Long> pending = new HashMap();

    abstract void throwException(String str, Http2Error http2Error) throws Throwable;

    public ConnectionSettingsBase(String connectionId) {
        this.connectionId = connectionId;
        this.current.put(Setting.HEADER_TABLE_SIZE, Long.valueOf(org.aspectj.apache.bcel.Constants.NEGATABLE));
        this.current.put(Setting.ENABLE_PUSH, 1L);
        this.current.put(Setting.MAX_CONCURRENT_STREAMS, 4294967296L);
        this.current.put(Setting.INITIAL_WINDOW_SIZE, 65535L);
        this.current.put(Setting.MAX_FRAME_SIZE, 16384L);
        this.current.put(Setting.MAX_HEADER_LIST_SIZE, 32768L);
    }

    public void set(Setting setting, long value) throws Throwable {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.sm.getString("connectionSettings.debug", this.connectionId, setting, Long.toString(value)));
        }
        switch (setting) {
            case HEADER_TABLE_SIZE:
                validateHeaderTableSize(value);
                break;
            case ENABLE_PUSH:
                validateEnablePush(value);
                break;
            case INITIAL_WINDOW_SIZE:
                validateInitialWindowSize(value);
                break;
            case MAX_FRAME_SIZE:
                validateMaxFrameSize(value);
                break;
            case UNKNOWN:
                this.log.warn(this.sm.getString("connectionSettings.unknown", this.connectionId, setting, Long.toString(value)));
                return;
        }
        set(setting, Long.valueOf(value));
    }

    synchronized void set(Setting setting, Long value) {
        this.current.put(setting, value);
    }

    public int getHeaderTableSize() {
        return getMinInt(Setting.HEADER_TABLE_SIZE);
    }

    public boolean getEnablePush() {
        long result = getMin(Setting.ENABLE_PUSH);
        return result != 0;
    }

    public long getMaxConcurrentStreams() {
        return getMax(Setting.MAX_CONCURRENT_STREAMS);
    }

    public int getInitialWindowSize() {
        return getMaxInt(Setting.INITIAL_WINDOW_SIZE);
    }

    public int getMaxFrameSize() {
        return getMaxInt(Setting.MAX_FRAME_SIZE);
    }

    public long getMaxHeaderListSize() {
        return getMax(Setting.MAX_HEADER_LIST_SIZE);
    }

    private synchronized long getMin(Setting setting) {
        Long pendingValue = this.pending.get(setting);
        long currentValue = this.current.get(setting).longValue();
        if (pendingValue == null) {
            return currentValue;
        }
        return Math.min(pendingValue.longValue(), currentValue);
    }

    private synchronized int getMinInt(Setting setting) {
        long result = getMin(setting);
        if (result > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) result;
    }

    private synchronized long getMax(Setting setting) {
        Long pendingValue = this.pending.get(setting);
        long currentValue = this.current.get(setting).longValue();
        if (pendingValue == null) {
            return currentValue;
        }
        return Math.max(pendingValue.longValue(), currentValue);
    }

    private synchronized int getMaxInt(Setting setting) {
        long result = getMax(setting);
        if (result > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) result;
    }

    private void validateHeaderTableSize(long headerTableSize) throws Throwable {
        if (headerTableSize > org.aspectj.apache.bcel.Constants.EXCEPTION_THROWER) {
            String msg = this.sm.getString("connectionSettings.headerTableSizeLimit", this.connectionId, Long.toString(headerTableSize));
            throwException(msg, Http2Error.PROTOCOL_ERROR);
        }
    }

    private void validateEnablePush(long enablePush) throws Throwable {
        if (enablePush > 1) {
            String msg = this.sm.getString("connectionSettings.enablePushInvalid", this.connectionId, Long.toString(enablePush));
            throwException(msg, Http2Error.PROTOCOL_ERROR);
        }
    }

    private void validateInitialWindowSize(long initialWindowSize) throws Throwable {
        if (initialWindowSize > 2147483647L) {
            String msg = this.sm.getString("connectionSettings.windowSizeTooBig", this.connectionId, Long.toString(initialWindowSize), Long.toString(2147483647L));
            throwException(msg, Http2Error.FLOW_CONTROL_ERROR);
        }
    }

    private void validateMaxFrameSize(long maxFrameSize) throws Throwable {
        if (maxFrameSize < 16384 || maxFrameSize > 16777215) {
            String msg = this.sm.getString("connectionSettings.maxFrameSizeInvalid", this.connectionId, Long.toString(maxFrameSize), Integer.toString(16384), Integer.toString(16777215));
            throwException(msg, Http2Error.PROTOCOL_ERROR);
        }
    }
}
