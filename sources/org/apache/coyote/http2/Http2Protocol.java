package org.apache.coyote.http2;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.apache.coyote.Adapter;
import org.apache.coyote.CompressionConfig;
import org.apache.coyote.Processor;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.apache.coyote.UpgradeProtocol;
import org.apache.coyote.UpgradeToken;
import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
import org.apache.coyote.http11.upgrade.UpgradeProcessorInternal;
import org.apache.tomcat.util.buf.StringUtils;
import org.apache.tomcat.util.net.SocketWrapperBase;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Http2Protocol.class */
public class Http2Protocol implements UpgradeProtocol {
    static final long DEFAULT_READ_TIMEOUT = 5000;
    static final long DEFAULT_WRITE_TIMEOUT = 5000;
    static final long DEFAULT_KEEP_ALIVE_TIMEOUT = 20000;
    static final long DEFAULT_STREAM_READ_TIMEOUT = 20000;
    static final long DEFAULT_STREAM_WRITE_TIMEOUT = 20000;
    static final long DEFAULT_MAX_CONCURRENT_STREAMS = 100;
    static final int DEFAULT_MAX_CONCURRENT_STREAM_EXECUTION = 20;
    static final int DEFAULT_INITIAL_WINDOW_SIZE = 65535;
    static final int DEFAULT_OVERHEAD_COUNT_FACTOR = 1;
    private static final String HTTP_UPGRADE_NAME = "h2c";
    private static final String ALPN_NAME = "h2";
    private static final byte[] ALPN_IDENTIFIER = "h2".getBytes(StandardCharsets.UTF_8);
    private long readTimeout = 5000;
    private long writeTimeout = 5000;
    private long keepAliveTimeout = org.apache.tomcat.websocket.Constants.DEFAULT_BLOCKING_SEND_TIMEOUT;
    private long streamReadTimeout = org.apache.tomcat.websocket.Constants.DEFAULT_BLOCKING_SEND_TIMEOUT;
    private long streamWriteTimeout = org.apache.tomcat.websocket.Constants.DEFAULT_BLOCKING_SEND_TIMEOUT;
    private long maxConcurrentStreams = DEFAULT_MAX_CONCURRENT_STREAMS;
    private int maxConcurrentStreamExecution = 20;
    private int initialWindowSize = 65535;
    private Set<String> allowedTrailerHeaders = Collections.newSetFromMap(new ConcurrentHashMap());
    private int maxHeaderCount = 100;
    private int maxHeaderSize = 8192;
    private int maxTrailerCount = 100;
    private int maxTrailerSize = 8192;
    private int overheadCountFactor = 1;
    private boolean initiatePingDisabled = false;
    private final CompressionConfig compressionConfig = new CompressionConfig();

    @Override // org.apache.coyote.UpgradeProtocol
    public String getHttpUpgradeName(boolean isSSLEnabled) {
        if (isSSLEnabled) {
            return null;
        }
        return HTTP_UPGRADE_NAME;
    }

    @Override // org.apache.coyote.UpgradeProtocol
    public byte[] getAlpnIdentifier() {
        return ALPN_IDENTIFIER;
    }

    @Override // org.apache.coyote.UpgradeProtocol
    public String getAlpnName() {
        return "h2";
    }

    @Override // org.apache.coyote.UpgradeProtocol
    public Processor getProcessor(SocketWrapperBase<?> socketWrapper, Adapter adapter) {
        UpgradeProcessorInternal processor = new UpgradeProcessorInternal(socketWrapper, new UpgradeToken(getInternalUpgradeHandler(adapter, null), null, null));
        return processor;
    }

    @Override // org.apache.coyote.UpgradeProtocol
    public InternalHttpUpgradeHandler getInternalUpgradeHandler(Adapter adapter, Request coyoteRequest) {
        Http2UpgradeHandler result = new Http2UpgradeHandler(this, adapter, coyoteRequest);
        result.setReadTimeout(getReadTimeout());
        result.setKeepAliveTimeout(getKeepAliveTimeout());
        result.setWriteTimeout(getWriteTimeout());
        result.setMaxConcurrentStreams(getMaxConcurrentStreams());
        result.setMaxConcurrentStreamExecution(getMaxConcurrentStreamExecution());
        result.setInitialWindowSize(getInitialWindowSize());
        result.setAllowedTrailerHeaders(this.allowedTrailerHeaders);
        result.setMaxHeaderCount(getMaxHeaderCount());
        result.setMaxHeaderSize(getMaxHeaderSize());
        result.setMaxTrailerCount(getMaxTrailerCount());
        result.setMaxTrailerSize(getMaxTrailerSize());
        result.setInitiatePingDisabled(this.initiatePingDisabled);
        return result;
    }

    @Override // org.apache.coyote.UpgradeProtocol
    public boolean accept(Request request) {
        boolean found;
        Enumeration<String> settings = request.getMimeHeaders().values("HTTP2-Settings");
        int count = 0;
        while (settings.hasMoreElements()) {
            count++;
            settings.nextElement();
        }
        if (count != 1) {
            return false;
        }
        Enumeration<String> connection = request.getMimeHeaders().values("Connection");
        boolean zContains = false;
        while (true) {
            found = zContains;
            if (!connection.hasMoreElements() || found) {
                break;
            }
            zContains = connection.nextElement().contains("HTTP2-Settings");
        }
        return found;
    }

    public long getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public long getWriteTimeout() {
        return this.writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public long getKeepAliveTimeout() {
        return this.keepAliveTimeout;
    }

    public void setKeepAliveTimeout(long keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public long getStreamReadTimeout() {
        return this.streamReadTimeout;
    }

    public void setStreamReadTimeout(long streamReadTimeout) {
        this.streamReadTimeout = streamReadTimeout;
    }

    public long getStreamWriteTimeout() {
        return this.streamWriteTimeout;
    }

    public void setStreamWriteTimeout(long streamWriteTimeout) {
        this.streamWriteTimeout = streamWriteTimeout;
    }

    public long getMaxConcurrentStreams() {
        return this.maxConcurrentStreams;
    }

    public void setMaxConcurrentStreams(long maxConcurrentStreams) {
        this.maxConcurrentStreams = maxConcurrentStreams;
    }

    public int getMaxConcurrentStreamExecution() {
        return this.maxConcurrentStreamExecution;
    }

    public void setMaxConcurrentStreamExecution(int maxConcurrentStreamExecution) {
        this.maxConcurrentStreamExecution = maxConcurrentStreamExecution;
    }

    public int getInitialWindowSize() {
        return this.initialWindowSize;
    }

    public void setInitialWindowSize(int initialWindowSize) {
        this.initialWindowSize = initialWindowSize;
    }

    public void setAllowedTrailerHeaders(String commaSeparatedHeaders) {
        Set<String> toRemove = new HashSet<>();
        toRemove.addAll(this.allowedTrailerHeaders);
        if (commaSeparatedHeaders != null) {
            String[] headers = commaSeparatedHeaders.split(",");
            for (String header : headers) {
                String trimmedHeader = header.trim().toLowerCase(Locale.ENGLISH);
                if (toRemove.contains(trimmedHeader)) {
                    toRemove.remove(trimmedHeader);
                } else {
                    this.allowedTrailerHeaders.add(trimmedHeader);
                }
            }
            this.allowedTrailerHeaders.removeAll(toRemove);
        }
    }

    public String getAllowedTrailerHeaders() {
        List<String> copy = new ArrayList<>(this.allowedTrailerHeaders.size());
        copy.addAll(this.allowedTrailerHeaders);
        return StringUtils.join(copy);
    }

    public void setMaxHeaderCount(int maxHeaderCount) {
        this.maxHeaderCount = maxHeaderCount;
    }

    public int getMaxHeaderCount() {
        return this.maxHeaderCount;
    }

    public void setMaxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
    }

    public int getMaxHeaderSize() {
        return this.maxHeaderSize;
    }

    public void setMaxTrailerCount(int maxTrailerCount) {
        this.maxTrailerCount = maxTrailerCount;
    }

    public int getMaxTrailerCount() {
        return this.maxTrailerCount;
    }

    public void setMaxTrailerSize(int maxTrailerSize) {
        this.maxTrailerSize = maxTrailerSize;
    }

    public int getMaxTrailerSize() {
        return this.maxTrailerSize;
    }

    public int getOverheadCountFactor() {
        return this.overheadCountFactor;
    }

    public void setOverheadCountFactor(int overheadCountFactor) {
        this.overheadCountFactor = overheadCountFactor;
    }

    public void setInitiatePingDisabled(boolean initiatePingDisabled) {
        this.initiatePingDisabled = initiatePingDisabled;
    }

    public void setCompression(String compression) {
        this.compressionConfig.setCompression(compression);
    }

    public String getCompression() {
        return this.compressionConfig.getCompression();
    }

    protected int getCompressionLevel() {
        return this.compressionConfig.getCompressionLevel();
    }

    public String getNoCompressionUserAgents() {
        return this.compressionConfig.getNoCompressionUserAgents();
    }

    protected Pattern getNoCompressionUserAgentsPattern() {
        return this.compressionConfig.getNoCompressionUserAgentsPattern();
    }

    public void setNoCompressionUserAgents(String noCompressionUserAgents) {
        this.compressionConfig.setNoCompressionUserAgents(noCompressionUserAgents);
    }

    public String getCompressibleMimeType() {
        return this.compressionConfig.getCompressibleMimeType();
    }

    public void setCompressibleMimeType(String valueS) {
        this.compressionConfig.setCompressibleMimeType(valueS);
    }

    public String[] getCompressibleMimeTypes() {
        return this.compressionConfig.getCompressibleMimeTypes();
    }

    public int getCompressionMinSize() {
        return this.compressionConfig.getCompressionMinSize();
    }

    public void setCompressionMinSize(int compressionMinSize) {
        this.compressionConfig.setCompressionMinSize(compressionMinSize);
    }

    public boolean useCompression(Request request, Response response) {
        return this.compressionConfig.useCompression(request, response);
    }
}
