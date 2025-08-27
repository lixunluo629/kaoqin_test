package io.netty.handler.codec.http2;

import io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder;
import io.netty.handler.codec.http2.Http2ConnectionHandler;
import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.util.internal.ObjectUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/AbstractHttp2ConnectionHandlerBuilder.class */
public abstract class AbstractHttp2ConnectionHandlerBuilder<T extends Http2ConnectionHandler, B extends AbstractHttp2ConnectionHandlerBuilder<T, B>> {
    private static final Http2HeadersEncoder.SensitivityDetector DEFAULT_HEADER_SENSITIVITY_DETECTOR;
    private Http2FrameListener frameListener;
    private boolean decoupleCloseAndGoAway;
    private Boolean isServer;
    private Integer maxReservedStreams;
    private Http2Connection connection;
    private Http2ConnectionDecoder decoder;
    private Http2ConnectionEncoder encoder;
    private Boolean validateHeaders;
    private Http2FrameLogger frameLogger;
    private Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector;
    private Boolean encoderEnforceMaxConcurrentStreams;
    private Boolean encoderIgnoreMaxHeaderListSize;
    static final /* synthetic */ boolean $assertionsDisabled;
    private Http2Settings initialSettings = Http2Settings.defaultSettings();
    private long gracefulShutdownTimeoutMillis = Http2CodecUtil.DEFAULT_GRACEFUL_SHUTDOWN_TIMEOUT_MILLIS;
    private Http2PromisedRequestVerifier promisedRequestVerifier = Http2PromisedRequestVerifier.ALWAYS_VERIFY;
    private boolean autoAckSettingsFrame = true;
    private boolean autoAckPingFrame = true;
    private int maxQueuedControlFrames = 10000;
    private int maxConsecutiveEmptyFrames = 2;

    protected abstract T build(Http2ConnectionDecoder http2ConnectionDecoder, Http2ConnectionEncoder http2ConnectionEncoder, Http2Settings http2Settings) throws Exception;

    static {
        $assertionsDisabled = !AbstractHttp2ConnectionHandlerBuilder.class.desiredAssertionStatus();
        DEFAULT_HEADER_SENSITIVITY_DETECTOR = Http2HeadersEncoder.NEVER_SENSITIVE;
    }

    protected Http2Settings initialSettings() {
        return this.initialSettings;
    }

    protected B initialSettings(Http2Settings http2Settings) {
        this.initialSettings = (Http2Settings) ObjectUtil.checkNotNull(http2Settings, "settings");
        return (B) self();
    }

    protected Http2FrameListener frameListener() {
        return this.frameListener;
    }

    protected B frameListener(Http2FrameListener http2FrameListener) {
        this.frameListener = (Http2FrameListener) ObjectUtil.checkNotNull(http2FrameListener, "frameListener");
        return (B) self();
    }

    protected long gracefulShutdownTimeoutMillis() {
        return this.gracefulShutdownTimeoutMillis;
    }

    protected B gracefulShutdownTimeoutMillis(long j) {
        if (j < -1) {
            throw new IllegalArgumentException("gracefulShutdownTimeoutMillis: " + j + " (expected: -1 for indefinite or >= 0)");
        }
        this.gracefulShutdownTimeoutMillis = j;
        return (B) self();
    }

    protected boolean isServer() {
        if (this.isServer != null) {
            return this.isServer.booleanValue();
        }
        return true;
    }

    protected B server(boolean z) {
        enforceConstraint("server", "connection", this.connection);
        enforceConstraint("server", "codec", this.decoder);
        enforceConstraint("server", "codec", this.encoder);
        this.isServer = Boolean.valueOf(z);
        return (B) self();
    }

    protected int maxReservedStreams() {
        if (this.maxReservedStreams != null) {
            return this.maxReservedStreams.intValue();
        }
        return 100;
    }

    protected B maxReservedStreams(int i) {
        enforceConstraint("server", "connection", this.connection);
        enforceConstraint("server", "codec", this.decoder);
        enforceConstraint("server", "codec", this.encoder);
        this.maxReservedStreams = Integer.valueOf(ObjectUtil.checkPositiveOrZero(i, "maxReservedStreams"));
        return (B) self();
    }

    protected Http2Connection connection() {
        return this.connection;
    }

    protected B connection(Http2Connection http2Connection) {
        enforceConstraint("connection", "maxReservedStreams", this.maxReservedStreams);
        enforceConstraint("connection", "server", this.isServer);
        enforceConstraint("connection", "codec", this.decoder);
        enforceConstraint("connection", "codec", this.encoder);
        this.connection = (Http2Connection) ObjectUtil.checkNotNull(http2Connection, "connection");
        return (B) self();
    }

    protected Http2ConnectionDecoder decoder() {
        return this.decoder;
    }

    protected Http2ConnectionEncoder encoder() {
        return this.encoder;
    }

    protected B codec(Http2ConnectionDecoder http2ConnectionDecoder, Http2ConnectionEncoder http2ConnectionEncoder) {
        enforceConstraint("codec", "server", this.isServer);
        enforceConstraint("codec", "maxReservedStreams", this.maxReservedStreams);
        enforceConstraint("codec", "connection", this.connection);
        enforceConstraint("codec", "frameLogger", this.frameLogger);
        enforceConstraint("codec", "validateHeaders", this.validateHeaders);
        enforceConstraint("codec", "headerSensitivityDetector", this.headerSensitivityDetector);
        enforceConstraint("codec", "encoderEnforceMaxConcurrentStreams", this.encoderEnforceMaxConcurrentStreams);
        ObjectUtil.checkNotNull(http2ConnectionDecoder, "decoder");
        ObjectUtil.checkNotNull(http2ConnectionEncoder, "encoder");
        if (http2ConnectionDecoder.connection() != http2ConnectionEncoder.connection()) {
            throw new IllegalArgumentException("The specified encoder and decoder have different connections.");
        }
        this.decoder = http2ConnectionDecoder;
        this.encoder = http2ConnectionEncoder;
        return (B) self();
    }

    protected boolean isValidateHeaders() {
        if (this.validateHeaders != null) {
            return this.validateHeaders.booleanValue();
        }
        return true;
    }

    protected B validateHeaders(boolean z) {
        enforceNonCodecConstraints("validateHeaders");
        this.validateHeaders = Boolean.valueOf(z);
        return (B) self();
    }

    protected Http2FrameLogger frameLogger() {
        return this.frameLogger;
    }

    protected B frameLogger(Http2FrameLogger http2FrameLogger) {
        enforceNonCodecConstraints("frameLogger");
        this.frameLogger = (Http2FrameLogger) ObjectUtil.checkNotNull(http2FrameLogger, "frameLogger");
        return (B) self();
    }

    protected boolean encoderEnforceMaxConcurrentStreams() {
        if (this.encoderEnforceMaxConcurrentStreams != null) {
            return this.encoderEnforceMaxConcurrentStreams.booleanValue();
        }
        return false;
    }

    protected B encoderEnforceMaxConcurrentStreams(boolean z) {
        enforceNonCodecConstraints("encoderEnforceMaxConcurrentStreams");
        this.encoderEnforceMaxConcurrentStreams = Boolean.valueOf(z);
        return (B) self();
    }

    protected int encoderEnforceMaxQueuedControlFrames() {
        return this.maxQueuedControlFrames;
    }

    protected B encoderEnforceMaxQueuedControlFrames(int i) {
        enforceNonCodecConstraints("encoderEnforceMaxQueuedControlFrames");
        this.maxQueuedControlFrames = ObjectUtil.checkPositiveOrZero(i, "maxQueuedControlFrames");
        return (B) self();
    }

    protected Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector() {
        return this.headerSensitivityDetector != null ? this.headerSensitivityDetector : DEFAULT_HEADER_SENSITIVITY_DETECTOR;
    }

    protected B headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector sensitivityDetector) {
        enforceNonCodecConstraints("headerSensitivityDetector");
        this.headerSensitivityDetector = (Http2HeadersEncoder.SensitivityDetector) ObjectUtil.checkNotNull(sensitivityDetector, "headerSensitivityDetector");
        return (B) self();
    }

    protected B encoderIgnoreMaxHeaderListSize(boolean z) {
        enforceNonCodecConstraints("encoderIgnoreMaxHeaderListSize");
        this.encoderIgnoreMaxHeaderListSize = Boolean.valueOf(z);
        return (B) self();
    }

    @Deprecated
    protected B initialHuffmanDecodeCapacity(int i) {
        return (B) self();
    }

    protected B promisedRequestVerifier(Http2PromisedRequestVerifier http2PromisedRequestVerifier) {
        enforceNonCodecConstraints("promisedRequestVerifier");
        this.promisedRequestVerifier = (Http2PromisedRequestVerifier) ObjectUtil.checkNotNull(http2PromisedRequestVerifier, "promisedRequestVerifier");
        return (B) self();
    }

    protected Http2PromisedRequestVerifier promisedRequestVerifier() {
        return this.promisedRequestVerifier;
    }

    protected int decoderEnforceMaxConsecutiveEmptyDataFrames() {
        return this.maxConsecutiveEmptyFrames;
    }

    protected B decoderEnforceMaxConsecutiveEmptyDataFrames(int i) {
        enforceNonCodecConstraints("maxConsecutiveEmptyFrames");
        this.maxConsecutiveEmptyFrames = ObjectUtil.checkPositiveOrZero(i, "maxConsecutiveEmptyFrames");
        return (B) self();
    }

    protected B autoAckSettingsFrame(boolean z) {
        enforceNonCodecConstraints("autoAckSettingsFrame");
        this.autoAckSettingsFrame = z;
        return (B) self();
    }

    protected boolean isAutoAckSettingsFrame() {
        return this.autoAckSettingsFrame;
    }

    protected B autoAckPingFrame(boolean z) {
        enforceNonCodecConstraints("autoAckPingFrame");
        this.autoAckPingFrame = z;
        return (B) self();
    }

    protected boolean isAutoAckPingFrame() {
        return this.autoAckPingFrame;
    }

    protected B decoupleCloseAndGoAway(boolean z) {
        this.decoupleCloseAndGoAway = z;
        return (B) self();
    }

    protected boolean decoupleCloseAndGoAway() {
        return this.decoupleCloseAndGoAway;
    }

    protected T build() {
        if (this.encoder != null) {
            if ($assertionsDisabled || this.decoder != null) {
                return (T) buildFromCodec(this.decoder, this.encoder);
            }
            throw new AssertionError();
        }
        Http2Connection defaultHttp2Connection = this.connection;
        if (defaultHttp2Connection == null) {
            defaultHttp2Connection = new DefaultHttp2Connection(isServer(), maxReservedStreams());
        }
        return (T) buildFromConnection(defaultHttp2Connection);
    }

    private T buildFromConnection(Http2Connection http2Connection) {
        Http2FrameWriter defaultHttp2FrameWriter;
        Long lMaxHeaderListSize = this.initialSettings.maxHeaderListSize();
        Http2FrameReader defaultHttp2FrameReader = new DefaultHttp2FrameReader(new DefaultHttp2HeadersDecoder(isValidateHeaders(), lMaxHeaderListSize == null ? 8192L : lMaxHeaderListSize.longValue(), -1));
        if (this.encoderIgnoreMaxHeaderListSize == null) {
            defaultHttp2FrameWriter = new DefaultHttp2FrameWriter(headerSensitivityDetector());
        } else {
            defaultHttp2FrameWriter = new DefaultHttp2FrameWriter(headerSensitivityDetector(), this.encoderIgnoreMaxHeaderListSize.booleanValue());
        }
        Http2FrameWriter http2OutboundFrameLogger = defaultHttp2FrameWriter;
        if (this.frameLogger != null) {
            defaultHttp2FrameReader = new Http2InboundFrameLogger(defaultHttp2FrameReader, this.frameLogger);
            http2OutboundFrameLogger = new Http2OutboundFrameLogger(http2OutboundFrameLogger, this.frameLogger);
        }
        Http2ConnectionEncoder defaultHttp2ConnectionEncoder = new DefaultHttp2ConnectionEncoder(http2Connection, http2OutboundFrameLogger);
        boolean zEncoderEnforceMaxConcurrentStreams = encoderEnforceMaxConcurrentStreams();
        if (this.maxQueuedControlFrames != 0) {
            defaultHttp2ConnectionEncoder = new Http2ControlFrameLimitEncoder(defaultHttp2ConnectionEncoder, this.maxQueuedControlFrames);
        }
        if (zEncoderEnforceMaxConcurrentStreams) {
            if (http2Connection.isServer()) {
                defaultHttp2ConnectionEncoder.close();
                defaultHttp2FrameReader.close();
                throw new IllegalArgumentException("encoderEnforceMaxConcurrentStreams: " + zEncoderEnforceMaxConcurrentStreams + " not supported for server");
            }
            defaultHttp2ConnectionEncoder = new StreamBufferingEncoder(defaultHttp2ConnectionEncoder);
        }
        return (T) buildFromCodec(new DefaultHttp2ConnectionDecoder(http2Connection, defaultHttp2ConnectionEncoder, defaultHttp2FrameReader, promisedRequestVerifier(), isAutoAckSettingsFrame(), isAutoAckPingFrame()), defaultHttp2ConnectionEncoder);
    }

    private T buildFromCodec(Http2ConnectionDecoder http2ConnectionDecoder, Http2ConnectionEncoder http2ConnectionEncoder) {
        int iDecoderEnforceMaxConsecutiveEmptyDataFrames = decoderEnforceMaxConsecutiveEmptyDataFrames();
        if (iDecoderEnforceMaxConsecutiveEmptyDataFrames > 0) {
            http2ConnectionDecoder = new Http2EmptyDataFrameConnectionDecoder(http2ConnectionDecoder, iDecoderEnforceMaxConsecutiveEmptyDataFrames);
        }
        try {
            T t = (T) build(http2ConnectionDecoder, http2ConnectionEncoder, this.initialSettings);
            t.gracefulShutdownTimeoutMillis(this.gracefulShutdownTimeoutMillis);
            if (t.decoder().frameListener() == null) {
                t.decoder().frameListener(this.frameListener);
            }
            return t;
        } catch (Throwable th) {
            http2ConnectionEncoder.close();
            http2ConnectionDecoder.close();
            throw new IllegalStateException("failed to build an Http2ConnectionHandler", th);
        }
    }

    protected final B self() {
        return this;
    }

    private void enforceNonCodecConstraints(String rejected) {
        enforceConstraint(rejected, "server/connection", this.decoder);
        enforceConstraint(rejected, "server/connection", this.encoder);
    }

    private static void enforceConstraint(String methodName, String rejectorName, Object value) {
        if (value != null) {
            throw new IllegalStateException(methodName + "() cannot be called because " + rejectorName + "() has been called already.");
        }
    }
}
