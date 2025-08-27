package org.apache.coyote.http11;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.coyote.AbstractProcessor;
import org.apache.coyote.ActionCode;
import org.apache.coyote.ErrorState;
import org.apache.coyote.Request;
import org.apache.coyote.RequestInfo;
import org.apache.coyote.UpgradeProtocol;
import org.apache.coyote.UpgradeToken;
import org.apache.coyote.http11.filters.BufferedInputFilter;
import org.apache.coyote.http11.filters.ChunkedInputFilter;
import org.apache.coyote.http11.filters.ChunkedOutputFilter;
import org.apache.coyote.http11.filters.GzipOutputFilter;
import org.apache.coyote.http11.filters.IdentityInputFilter;
import org.apache.coyote.http11.filters.IdentityOutputFilter;
import org.apache.coyote.http11.filters.SavedRequestInputFilter;
import org.apache.coyote.http11.filters.VoidInputFilter;
import org.apache.coyote.http11.filters.VoidOutputFilter;
import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.FastHttpDateFormat;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.apache.tomcat.util.log.UserDataHelper;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.SendfileDataBase;
import org.apache.tomcat.util.net.SendfileKeepAliveState;
import org.apache.tomcat.util.net.SendfileState;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.res.StringManager;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.web.servlet.support.WebContentGenerator;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/Http11Processor.class */
public class Http11Processor extends AbstractProcessor {
    private static final Log log = LogFactory.getLog((Class<?>) Http11Processor.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) Http11Processor.class);
    protected final Http11InputBuffer inputBuffer;
    protected final Http11OutputBuffer outputBuffer;
    private final HttpParser httpParser;
    private int pluggableFilterIndex;
    protected volatile boolean keepAlive;
    protected boolean openSocket;
    protected boolean readComplete;
    protected boolean http11;
    protected boolean http09;
    protected boolean contentDelimitation;
    protected Pattern restrictedUserAgents;
    protected int maxKeepAliveRequests;
    protected int connectionUploadTimeout;
    protected boolean disableUploadTimeout;
    protected int compressionLevel;
    protected int compressionMinSize;
    protected int maxSavePostSize;
    protected Pattern noCompressionUserAgents;
    protected String[] compressableMimeTypes;
    private String server;
    private boolean serverRemoveAppProvidedValues;
    protected UpgradeToken upgradeToken;
    protected SendfileDataBase sendfileData;
    private final Map<String, UpgradeProtocol> httpUpgradeProtocols;
    private final boolean allowHostHeaderMismatch;

    public Http11Processor(int maxHttpHeaderSize, boolean allowHostHeaderMismatch, boolean rejectIllegalHeaderName, AbstractEndpoint<?> endpoint, int maxTrailerSize, Set<String> allowedTrailerHeaders, int maxExtensionSize, int maxSwallowSize, Map<String, UpgradeProtocol> httpUpgradeProtocols, boolean sendReasonPhrase, String relaxedPathChars, String relaxedQueryChars) {
        super(endpoint);
        this.pluggableFilterIndex = Integer.MAX_VALUE;
        this.keepAlive = true;
        this.openSocket = false;
        this.readComplete = true;
        this.http11 = true;
        this.http09 = false;
        this.contentDelimitation = true;
        this.restrictedUserAgents = null;
        this.maxKeepAliveRequests = -1;
        this.connectionUploadTimeout = 300000;
        this.disableUploadTimeout = false;
        this.compressionLevel = 0;
        this.compressionMinSize = 2048;
        this.maxSavePostSize = 4096;
        this.noCompressionUserAgents = null;
        this.server = null;
        this.serverRemoveAppProvidedValues = false;
        this.upgradeToken = null;
        this.sendfileData = null;
        this.httpParser = new HttpParser(relaxedPathChars, relaxedQueryChars);
        this.inputBuffer = new Http11InputBuffer(this.request, maxHttpHeaderSize, rejectIllegalHeaderName, this.httpParser);
        this.request.setInputBuffer(this.inputBuffer);
        this.outputBuffer = new Http11OutputBuffer(this.response, maxHttpHeaderSize, sendReasonPhrase);
        this.response.setOutputBuffer(this.outputBuffer);
        this.inputBuffer.addFilter(new IdentityInputFilter(maxSwallowSize));
        this.outputBuffer.addFilter(new IdentityOutputFilter());
        this.inputBuffer.addFilter(new ChunkedInputFilter(maxTrailerSize, allowedTrailerHeaders, maxExtensionSize, maxSwallowSize));
        this.outputBuffer.addFilter(new ChunkedOutputFilter());
        this.inputBuffer.addFilter(new VoidInputFilter());
        this.outputBuffer.addFilter(new VoidOutputFilter());
        this.inputBuffer.addFilter(new BufferedInputFilter());
        this.outputBuffer.addFilter(new GzipOutputFilter());
        this.pluggableFilterIndex = this.inputBuffer.getFilters().length;
        this.httpUpgradeProtocols = httpUpgradeProtocols;
        this.allowHostHeaderMismatch = allowHostHeaderMismatch;
    }

    public void setCompression(String compression) {
        if (compression.equals(CustomBooleanEditor.VALUE_ON)) {
            this.compressionLevel = 1;
            return;
        }
        if (compression.equals("force")) {
            this.compressionLevel = 2;
            return;
        }
        if (compression.equals(CustomBooleanEditor.VALUE_OFF)) {
            this.compressionLevel = 0;
            return;
        }
        try {
            this.compressionMinSize = Integer.parseInt(compression);
            this.compressionLevel = 1;
        } catch (Exception e) {
            this.compressionLevel = 0;
        }
    }

    public void setCompressionMinSize(int compressionMinSize) {
        this.compressionMinSize = compressionMinSize;
    }

    public void setNoCompressionUserAgents(String noCompressionUserAgents) {
        if (noCompressionUserAgents == null || noCompressionUserAgents.length() == 0) {
            this.noCompressionUserAgents = null;
        } else {
            this.noCompressionUserAgents = Pattern.compile(noCompressionUserAgents);
        }
    }

    @Deprecated
    public void setCompressableMimeTypes(String[] compressibleMimeTypes) {
        setCompressibleMimeTypes(compressibleMimeTypes);
    }

    public void setCompressibleMimeTypes(String[] compressibleMimeTypes) {
        this.compressableMimeTypes = compressibleMimeTypes;
    }

    public String getCompression() {
        switch (this.compressionLevel) {
            case 0:
                return CustomBooleanEditor.VALUE_OFF;
            case 1:
                return CustomBooleanEditor.VALUE_ON;
            case 2:
                return "force";
            default:
                return CustomBooleanEditor.VALUE_OFF;
        }
    }

    private static boolean startsWithStringArray(String[] sArray, String value) {
        if (value == null) {
            return false;
        }
        for (String str : sArray) {
            if (value.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    public void setRestrictedUserAgents(String restrictedUserAgents) {
        if (restrictedUserAgents == null || restrictedUserAgents.length() == 0) {
            this.restrictedUserAgents = null;
        } else {
            this.restrictedUserAgents = Pattern.compile(restrictedUserAgents);
        }
    }

    public void setMaxKeepAliveRequests(int mkar) {
        this.maxKeepAliveRequests = mkar;
    }

    public int getMaxKeepAliveRequests() {
        return this.maxKeepAliveRequests;
    }

    public void setMaxSavePostSize(int msps) {
        this.maxSavePostSize = msps;
    }

    public int getMaxSavePostSize() {
        return this.maxSavePostSize;
    }

    public void setDisableUploadTimeout(boolean isDisabled) {
        this.disableUploadTimeout = isDisabled;
    }

    public boolean getDisableUploadTimeout() {
        return this.disableUploadTimeout;
    }

    public void setConnectionUploadTimeout(int timeout) {
        this.connectionUploadTimeout = timeout;
    }

    public int getConnectionUploadTimeout() {
        return this.connectionUploadTimeout;
    }

    public void setServer(String server) {
        if (server == null || server.equals("")) {
            this.server = null;
        } else {
            this.server = server;
        }
    }

    public void setServerRemoveAppProvidedValues(boolean serverRemoveAppProvidedValues) {
        this.serverRemoveAppProvidedValues = serverRemoveAppProvidedValues;
    }

    private boolean isCompressible() {
        MessageBytes contentEncodingMB = this.response.getMimeHeaders().getValue("Content-Encoding");
        if (contentEncodingMB != null && contentEncodingMB.indexOf("gzip") != -1) {
            return false;
        }
        if (this.compressionLevel == 2) {
            return true;
        }
        long contentLength = this.response.getContentLengthLong();
        if ((contentLength == -1 || contentLength > this.compressionMinSize) && this.compressableMimeTypes != null) {
            return startsWithStringArray(this.compressableMimeTypes, this.response.getContentType());
        }
        return false;
    }

    private boolean useCompression() {
        MessageBytes userAgentValueMB;
        MessageBytes acceptEncodingMB = this.request.getMimeHeaders().getValue("accept-encoding");
        if (acceptEncodingMB == null || acceptEncodingMB.indexOf("gzip") == -1) {
            return false;
        }
        if (this.compressionLevel != 2 && this.noCompressionUserAgents != null && (userAgentValueMB = this.request.getMimeHeaders().getValue("user-agent")) != null) {
            String userAgentValue = userAgentValueMB.toString();
            if (this.noCompressionUserAgents.matcher(userAgentValue).matches()) {
                return false;
            }
            return true;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0069, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int findBytes(org.apache.tomcat.util.buf.ByteChunk r4, byte[] r5) {
        /*
            r0 = r5
            r1 = 0
            r0 = r0[r1]
            r6 = r0
            r0 = r4
            byte[] r0 = r0.getBuffer()
            r7 = r0
            r0 = r4
            int r0 = r0.getStart()
            r8 = r0
            r0 = r4
            int r0 = r0.getEnd()
            r9 = r0
            r0 = r5
            int r0 = r0.length
            r10 = r0
            r0 = r8
            r11 = r0
        L1d:
            r0 = r11
            r1 = r9
            r2 = r10
            int r1 = r1 - r2
            if (r0 > r1) goto L6f
            r0 = r7
            r1 = r11
            r0 = r0[r1]
            int r0 = org.apache.tomcat.util.buf.Ascii.toLower(r0)
            r1 = r6
            if (r0 == r1) goto L35
            goto L69
        L35:
            r0 = r11
            r1 = 1
            int r0 = r0 + r1
            r12 = r0
            r0 = 1
            r13 = r0
        L3e:
            r0 = r13
            r1 = r10
            if (r0 >= r1) goto L69
            r0 = r7
            r1 = r12
            int r12 = r12 + 1
            r0 = r0[r1]
            int r0 = org.apache.tomcat.util.buf.Ascii.toLower(r0)
            r1 = r5
            r2 = r13
            int r13 = r13 + 1
            r1 = r1[r2]
            if (r0 == r1) goto L5c
            goto L69
        L5c:
            r0 = r13
            r1 = r10
            if (r0 != r1) goto L3e
            r0 = r11
            r1 = r8
            int r0 = r0 - r1
            return r0
        L69:
            int r11 = r11 + 1
            goto L1d
        L6f:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.coyote.http11.Http11Processor.findBytes(org.apache.tomcat.util.buf.ByteChunk, byte[]):int");
    }

    private static boolean statusDropsConnection(int status) {
        return status == 400 || status == 408 || status == 411 || status == 413 || status == 414 || status == 500 || status == 503 || status == 501;
    }

    private void addInputFilter(InputFilter[] inputFilters, String encodingName) {
        String encodingName2 = encodingName.trim().toLowerCase(Locale.ENGLISH);
        if (!encodingName2.equals("identity")) {
            if (encodingName2.equals("chunked")) {
                this.inputBuffer.addActiveFilter(inputFilters[1]);
                this.contentDelimitation = true;
                return;
            }
            for (int i = this.pluggableFilterIndex; i < inputFilters.length; i++) {
                if (inputFilters[i].getEncodingName().toString().equals(encodingName2)) {
                    this.inputBuffer.addActiveFilter(inputFilters[i]);
                    return;
                }
            }
            this.response.setStatus(501);
            setErrorState(ErrorState.CLOSE_CLEAN, null);
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("http11processor.request.prepare") + " Unsupported transfer encoding [" + encodingName2 + "]");
            }
        }
    }

    @Override // org.apache.coyote.AbstractProcessorLight
    public AbstractEndpoint.Handler.SocketState service(SocketWrapperBase<?> socketWrapper) throws IllegalStateException, IOException {
        SendfileState sendfileState;
        boolean foundUpgrade;
        String requestedProtocol;
        UpgradeProtocol upgradeProtocol;
        RequestInfo rp = this.request.getRequestProcessor();
        rp.setStage(1);
        setSocketWrapper(socketWrapper);
        this.inputBuffer.init(socketWrapper);
        this.outputBuffer.init(socketWrapper);
        this.keepAlive = true;
        this.openSocket = false;
        this.readComplete = true;
        boolean keptAlive = false;
        SendfileState sendfileStateProcessSendfile = SendfileState.DONE;
        while (true) {
            sendfileState = sendfileStateProcessSendfile;
            if (!getErrorState().isError() && this.keepAlive && !isAsync() && this.upgradeToken == null && sendfileState == SendfileState.DONE && !this.endpoint.isPaused()) {
                try {
                    if (!this.inputBuffer.parseRequestLine(keptAlive)) {
                        if (this.inputBuffer.getParsingRequestLinePhase() == -1) {
                            return AbstractEndpoint.Handler.SocketState.UPGRADING;
                        }
                        if (handleIncompleteRequestLineRead()) {
                        }
                    }
                    if (this.endpoint.isPaused()) {
                        this.response.setStatus(503);
                        setErrorState(ErrorState.CLOSE_CLEAN, null);
                    } else {
                        keptAlive = true;
                        this.request.getMimeHeaders().setLimit(this.endpoint.getMaxHeaderCount());
                        if (!this.inputBuffer.parseHeaders()) {
                            this.openSocket = true;
                            this.readComplete = false;
                        } else if (!this.disableUploadTimeout) {
                            socketWrapper.setReadTimeout(this.connectionUploadTimeout);
                        }
                    }
                } catch (IOException e) {
                    if (log.isDebugEnabled()) {
                        log.debug(sm.getString("http11processor.header.parse"), e);
                    }
                    setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
                } catch (Throwable t) {
                    ExceptionUtils.handleThrowable(t);
                    UserDataHelper.Mode logMode = this.userDataHelper.getNextMode();
                    if (logMode != null) {
                        String message = sm.getString("http11processor.header.parse");
                        switch (logMode) {
                            case INFO_THEN_DEBUG:
                                message = message + sm.getString("http11processor.fallToDebug");
                            case INFO:
                                log.info(message, t);
                                break;
                            case DEBUG:
                                log.debug(message, t);
                                break;
                        }
                    }
                    this.response.setStatus(400);
                    setErrorState(ErrorState.CLOSE_CLEAN, t);
                }
                Enumeration<String> connectionValues = this.request.getMimeHeaders().values("Connection");
                boolean zContains = false;
                while (true) {
                    foundUpgrade = zContains;
                    if (connectionValues.hasMoreElements() && !foundUpgrade) {
                        zContains = connectionValues.nextElement().toLowerCase(Locale.ENGLISH).contains(org.apache.tomcat.websocket.Constants.CONNECTION_HEADER_VALUE);
                    }
                }
                if (foundUpgrade && (upgradeProtocol = this.httpUpgradeProtocols.get((requestedProtocol = this.request.getHeader("Upgrade")))) != null && upgradeProtocol.accept(this.request)) {
                    this.response.setStatus(101);
                    this.response.setHeader("Connection", "Upgrade");
                    this.response.setHeader("Upgrade", requestedProtocol);
                    action(ActionCode.CLOSE, null);
                    getAdapter().log(this.request, this.response, 0L);
                    InternalHttpUpgradeHandler upgradeHandler = upgradeProtocol.getInternalUpgradeHandler(getAdapter(), cloneRequest(this.request));
                    UpgradeToken upgradeToken = new UpgradeToken(upgradeHandler, null, null);
                    action(ActionCode.UPGRADE, upgradeToken);
                    return AbstractEndpoint.Handler.SocketState.UPGRADING;
                }
                if (getErrorState().isIoAllowed()) {
                    rp.setStage(2);
                    try {
                        prepareRequest();
                    } catch (Throwable t2) {
                        ExceptionUtils.handleThrowable(t2);
                        if (log.isDebugEnabled()) {
                            log.debug(sm.getString("http11processor.request.prepare"), t2);
                        }
                        this.response.setStatus(500);
                        setErrorState(ErrorState.CLOSE_CLEAN, t2);
                    }
                }
                if (this.maxKeepAliveRequests == 1) {
                    this.keepAlive = false;
                } else if (this.maxKeepAliveRequests > 0 && socketWrapper.decrementKeepAlive() <= 0) {
                    this.keepAlive = false;
                }
                if (getErrorState().isIoAllowed()) {
                    try {
                        rp.setStage(3);
                        getAdapter().service(this.request, this.response);
                        if (this.keepAlive && !getErrorState().isError() && !isAsync() && statusDropsConnection(this.response.getStatus())) {
                            setErrorState(ErrorState.CLOSE_CLEAN, null);
                        }
                    } catch (InterruptedIOException e2) {
                        setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e2);
                    } catch (HeadersTooLargeException e3) {
                        log.error(sm.getString("http11processor.request.process"), e3);
                        if (this.response.isCommitted()) {
                            setErrorState(ErrorState.CLOSE_NOW, e3);
                        } else {
                            this.response.reset();
                            this.response.setStatus(500);
                            setErrorState(ErrorState.CLOSE_CLEAN, e3);
                            this.response.setHeader("Connection", "close");
                        }
                    } catch (Throwable t3) {
                        ExceptionUtils.handleThrowable(t3);
                        log.error(sm.getString("http11processor.request.process"), t3);
                        this.response.setStatus(500);
                        setErrorState(ErrorState.CLOSE_CLEAN, t3);
                        getAdapter().log(this.request, this.response, 0L);
                    }
                }
                rp.setStage(4);
                if (!isAsync()) {
                    endRequest();
                }
                rp.setStage(5);
                if (getErrorState().isError()) {
                    this.response.setStatus(500);
                }
                if (!isAsync() || getErrorState().isError()) {
                    this.request.updateCounters();
                    if (getErrorState().isIoAllowed()) {
                        this.inputBuffer.nextRequest();
                        this.outputBuffer.nextRequest();
                    }
                }
                if (!this.disableUploadTimeout) {
                    int soTimeout = this.endpoint.getConnectionTimeout();
                    if (soTimeout > 0) {
                        socketWrapper.setReadTimeout(soTimeout);
                    } else {
                        socketWrapper.setReadTimeout(0L);
                    }
                }
                rp.setStage(6);
                sendfileStateProcessSendfile = processSendfile(socketWrapper);
            }
        }
        rp.setStage(7);
        if (getErrorState().isError() || this.endpoint.isPaused()) {
            return AbstractEndpoint.Handler.SocketState.CLOSED;
        }
        if (isAsync()) {
            return AbstractEndpoint.Handler.SocketState.LONG;
        }
        if (isUpgrade()) {
            return AbstractEndpoint.Handler.SocketState.UPGRADING;
        }
        if (sendfileState == SendfileState.PENDING) {
            return AbstractEndpoint.Handler.SocketState.SENDFILE;
        }
        if (this.openSocket) {
            if (this.readComplete) {
                return AbstractEndpoint.Handler.SocketState.OPEN;
            }
            return AbstractEndpoint.Handler.SocketState.LONG;
        }
        return AbstractEndpoint.Handler.SocketState.CLOSED;
    }

    private Request cloneRequest(Request source) throws IOException {
        Request dest = new Request();
        dest.decodedURI().duplicate(source.decodedURI());
        dest.method().duplicate(source.method());
        dest.getMimeHeaders().duplicate(source.getMimeHeaders());
        dest.requestURI().duplicate(source.requestURI());
        dest.queryString().duplicate(source.queryString());
        return dest;
    }

    private boolean handleIncompleteRequestLineRead() {
        this.openSocket = true;
        if (this.inputBuffer.getParsingRequestLinePhase() > 1) {
            if (this.endpoint.isPaused()) {
                this.response.setStatus(503);
                setErrorState(ErrorState.CLOSE_CLEAN, null);
                return false;
            }
            this.readComplete = false;
            return true;
        }
        return true;
    }

    private void checkExpectationAndResponseStatus() {
        if (this.request.hasExpectation()) {
            if (this.response.getStatus() < 200 || this.response.getStatus() > 299) {
                this.inputBuffer.setSwallowInput(false);
                this.keepAlive = false;
            }
        }
    }

    private void prepareRequest() {
        MessageBytes transferEncodingValueMB;
        MessageBytes userAgentValueMB;
        MessageBytes expectMB;
        this.http11 = true;
        this.http09 = false;
        this.contentDelimitation = false;
        if (this.endpoint.isSSLEnabled()) {
            this.request.scheme().setString("https");
        }
        MessageBytes protocolMB = this.request.protocol();
        if (protocolMB.equals(Constants.HTTP_11)) {
            this.http11 = true;
            protocolMB.setString(Constants.HTTP_11);
        } else if (protocolMB.equals(Constants.HTTP_10)) {
            this.http11 = false;
            this.keepAlive = false;
            protocolMB.setString(Constants.HTTP_10);
        } else if (protocolMB.equals("")) {
            this.http09 = true;
            this.http11 = false;
            this.keepAlive = false;
        } else {
            this.http11 = false;
            this.response.setStatus(505);
            setErrorState(ErrorState.CLOSE_CLEAN, null);
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("http11processor.request.prepare") + " Unsupported HTTP version \"" + protocolMB + SymbolConstants.QUOTES_SYMBOL);
            }
        }
        MimeHeaders headers = this.request.getMimeHeaders();
        MessageBytes connectionValueMB = headers.getValue("Connection");
        if (connectionValueMB != null) {
            ByteChunk connectionValueBC = connectionValueMB.getByteChunk();
            if (findBytes(connectionValueBC, Constants.CLOSE_BYTES) != -1) {
                this.keepAlive = false;
            } else if (findBytes(connectionValueBC, Constants.KEEPALIVE_BYTES) != -1) {
                this.keepAlive = true;
            }
        }
        if (this.http11 && (expectMB = headers.getValue("expect")) != null) {
            if (expectMB.indexOfIgnoreCase("100-continue", 0) != -1) {
                this.inputBuffer.setSwallowInput(false);
                this.request.setExpectation(true);
            } else {
                this.response.setStatus(417);
                setErrorState(ErrorState.CLOSE_CLEAN, null);
            }
        }
        if (this.restrictedUserAgents != null && ((this.http11 || this.keepAlive) && (userAgentValueMB = headers.getValue("user-agent")) != null)) {
            String userAgentValue = userAgentValueMB.toString();
            if (this.restrictedUserAgents != null && this.restrictedUserAgents.matcher(userAgentValue).matches()) {
                this.http11 = false;
                this.keepAlive = false;
            }
        }
        MessageBytes hostValueMB = null;
        try {
            hostValueMB = headers.getUniqueValue("host");
        } catch (IllegalArgumentException e) {
            this.response.setStatus(400);
            setErrorState(ErrorState.CLOSE_CLEAN, null);
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("http11processor.request.multipleHosts"));
            }
        }
        if (this.http11 && hostValueMB == null) {
            this.response.setStatus(400);
            setErrorState(ErrorState.CLOSE_CLEAN, null);
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("http11processor.request.noHostHeader"));
            }
        }
        ByteChunk uriBC = this.request.requestURI().getByteChunk();
        byte[] uriB = uriBC.getBytes();
        if (uriBC.startsWithIgnoreCase("http", 0)) {
            int pos = 4;
            if (uriBC.startsWithIgnoreCase(ExcelXmlConstants.CELL_DATA_FORMAT_TAG, 4)) {
                pos = 4 + 1;
            }
            if (uriBC.startsWith("://", pos)) {
                int pos2 = pos + 3;
                int uriBCStart = uriBC.getStart();
                int slashPos = uriBC.indexOf('/', pos2);
                int atPos = uriBC.indexOf('@', pos2);
                if (slashPos > -1 && atPos > slashPos) {
                    atPos = -1;
                }
                if (slashPos == -1) {
                    slashPos = uriBC.getLength();
                    this.request.requestURI().setBytes(uriB, uriBCStart + 6, 1);
                } else {
                    this.request.requestURI().setBytes(uriB, uriBCStart + slashPos, uriBC.getLength() - slashPos);
                }
                if (atPos != -1) {
                    while (true) {
                        if (pos2 >= atPos) {
                            break;
                        }
                        byte c = uriB[uriBCStart + pos2];
                        if (HttpParser.isUserInfo(c)) {
                            pos2++;
                        } else {
                            this.response.setStatus(400);
                            setErrorState(ErrorState.CLOSE_CLEAN, null);
                            if (log.isDebugEnabled()) {
                                log.debug(sm.getString("http11processor.request.invalidUserInfo"));
                            }
                        }
                    }
                    pos2 = atPos + 1;
                }
                if (this.http11) {
                    if (hostValueMB != null && !hostValueMB.getByteChunk().equals(uriB, uriBCStart + pos2, slashPos - pos2)) {
                        if (this.allowHostHeaderMismatch) {
                            hostValueMB = headers.setValue("host");
                            hostValueMB.setBytes(uriB, uriBCStart + pos2, slashPos - pos2);
                        } else {
                            this.response.setStatus(400);
                            setErrorState(ErrorState.CLOSE_CLEAN, null);
                            if (log.isDebugEnabled()) {
                                log.debug(sm.getString("http11processor.request.inconsistentHosts"));
                            }
                        }
                    }
                } else {
                    hostValueMB = headers.setValue("host");
                    hostValueMB.setBytes(uriB, uriBCStart + pos2, slashPos - pos2);
                }
            } else {
                this.response.setStatus(400);
                setErrorState(ErrorState.CLOSE_CLEAN, null);
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("http11processor.request.invalidScheme"));
                }
            }
        }
        int i = uriBC.getStart();
        while (true) {
            if (i >= uriBC.getEnd()) {
                break;
            }
            if (this.httpParser.isAbsolutePathRelaxed(uriB[i])) {
                i++;
            } else {
                this.response.setStatus(400);
                setErrorState(ErrorState.CLOSE_CLEAN, null);
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("http11processor.request.invalidUri"));
                }
            }
        }
        InputFilter[] inputFilters = this.inputBuffer.getFilters();
        if (this.http11 && (transferEncodingValueMB = headers.getValue("transfer-encoding")) != null) {
            String transferEncodingValue = transferEncodingValueMB.toString();
            int startPos = 0;
            int commaPos = transferEncodingValue.indexOf(44);
            while (commaPos != -1) {
                String encodingName = transferEncodingValue.substring(startPos, commaPos);
                addInputFilter(inputFilters, encodingName);
                startPos = commaPos + 1;
                commaPos = transferEncodingValue.indexOf(44, startPos);
            }
            String encodingName2 = transferEncodingValue.substring(startPos);
            addInputFilter(inputFilters, encodingName2);
        }
        long contentLength = this.request.getContentLengthLong();
        if (contentLength >= 0) {
            if (this.contentDelimitation) {
                headers.removeHeader("content-length");
                this.request.setContentLength(-1L);
            } else {
                this.inputBuffer.addActiveFilter(inputFilters[0]);
                this.contentDelimitation = true;
            }
        }
        parseHost(hostValueMB);
        if (!this.contentDelimitation) {
            this.inputBuffer.addActiveFilter(inputFilters[2]);
            this.contentDelimitation = true;
        }
        if (!getErrorState().isIoAllowed()) {
            getAdapter().log(this.request, this.response, 0L);
        }
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void prepareResponse() throws IOException {
        boolean entityBody = true;
        this.contentDelimitation = false;
        OutputFilter[] outputFilters = this.outputBuffer.getFilters();
        if (this.http09) {
            this.outputBuffer.addActiveFilter(outputFilters[0]);
            this.outputBuffer.commit();
            return;
        }
        int statusCode = this.response.getStatus();
        if (statusCode < 200 || statusCode == 204 || statusCode == 205 || statusCode == 304) {
            this.outputBuffer.addActiveFilter(outputFilters[2]);
            entityBody = false;
            this.contentDelimitation = true;
            if (statusCode == 205) {
                this.response.setContentLength(0L);
            } else {
                this.response.setContentLength(-1L);
            }
        }
        MessageBytes methodMB = this.request.method();
        if (methodMB.equals(WebContentGenerator.METHOD_HEAD)) {
            this.outputBuffer.addActiveFilter(outputFilters[2]);
            this.contentDelimitation = true;
        }
        if (this.endpoint.getUseSendfile()) {
            prepareSendfile(outputFilters);
        }
        boolean isCompressible = false;
        boolean useCompression = false;
        if (entityBody && this.compressionLevel > 0 && this.sendfileData == null) {
            isCompressible = isCompressible();
            if (isCompressible) {
                useCompression = useCompression();
            }
            if (useCompression) {
                this.response.setContentLength(-1L);
            }
        }
        MimeHeaders headers = this.response.getMimeHeaders();
        if (entityBody || statusCode == 204) {
            String contentType = this.response.getContentType();
            if (contentType != null) {
                headers.setValue("Content-Type").setString(contentType);
            }
            String contentLanguage = this.response.getContentLanguage();
            if (contentLanguage != null) {
                headers.setValue("Content-Language").setString(contentLanguage);
            }
        }
        long contentLength = this.response.getContentLengthLong();
        boolean connectionClosePresent = false;
        if (contentLength != -1) {
            headers.setValue("Content-Length").setLong(contentLength);
            this.outputBuffer.addActiveFilter(outputFilters[0]);
            this.contentDelimitation = true;
        } else {
            connectionClosePresent = isConnectionClose(headers);
            if (entityBody && this.http11 && !connectionClosePresent) {
                this.outputBuffer.addActiveFilter(outputFilters[1]);
                this.contentDelimitation = true;
                headers.addValue("Transfer-Encoding").setString("chunked");
            } else {
                this.outputBuffer.addActiveFilter(outputFilters[0]);
            }
        }
        if (useCompression) {
            this.outputBuffer.addActiveFilter(outputFilters[3]);
            headers.setValue("Content-Encoding").setString("gzip");
        }
        if (isCompressible) {
            MessageBytes vary = headers.getValue("Vary");
            if (vary == null) {
                headers.setValue("Vary").setString("Accept-Encoding");
            } else if (!vary.equals("*")) {
                headers.setValue("Vary").setString(vary.getString() + ",Accept-Encoding");
            }
        }
        if (headers.getValue("Date") == null) {
            headers.addValue("Date").setString(FastHttpDateFormat.getCurrentDate());
        }
        if (entityBody && !this.contentDelimitation) {
            this.keepAlive = false;
        }
        checkExpectationAndResponseStatus();
        if (this.keepAlive && statusDropsConnection(statusCode)) {
            this.keepAlive = false;
        }
        if (!this.keepAlive) {
            if (!connectionClosePresent) {
                headers.addValue("Connection").setString("close");
            }
        } else if (!this.http11 && !getErrorState().isError()) {
            headers.addValue("Connection").setString("keep-alive");
        }
        if (this.server == null) {
            if (this.serverRemoveAppProvidedValues) {
                headers.removeHeader("server");
            }
        } else {
            headers.setValue("Server").setString(this.server);
        }
        try {
            this.outputBuffer.sendStatus();
            int size = headers.size();
            for (int i = 0; i < size; i++) {
                this.outputBuffer.sendHeader(headers.getName(i), headers.getValue(i));
            }
            this.outputBuffer.endHeaders();
            this.outputBuffer.commit();
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            this.outputBuffer.resetHeaderBuffer();
            throw t;
        }
    }

    private static boolean isConnectionClose(MimeHeaders headers) {
        MessageBytes connection = headers.getValue("Connection");
        if (connection == null) {
            return false;
        }
        return connection.equals("close");
    }

    private void prepareSendfile(OutputFilter[] outputFilters) {
        String fileName = (String) this.request.getAttribute("org.apache.tomcat.sendfile.filename");
        if (fileName == null) {
            this.sendfileData = null;
            return;
        }
        this.outputBuffer.addActiveFilter(outputFilters[2]);
        this.contentDelimitation = true;
        long pos = ((Long) this.request.getAttribute("org.apache.tomcat.sendfile.start")).longValue();
        long end = ((Long) this.request.getAttribute("org.apache.tomcat.sendfile.end")).longValue();
        this.sendfileData = this.socketWrapper.createSendfileData(fileName, pos, end - pos);
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected void populatePort() {
        this.request.action(ActionCode.REQ_LOCALPORT_ATTRIBUTE, this.request);
        this.request.setServerPort(this.request.getLocalPort());
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected boolean flushBufferedWrite() throws IOException {
        if (this.outputBuffer.hasDataToWrite() && this.outputBuffer.flushBuffer(false)) {
            this.outputBuffer.registerWriteInterest();
            return true;
        }
        return false;
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected AbstractEndpoint.Handler.SocketState dispatchEndRequest() {
        if (!this.keepAlive) {
            return AbstractEndpoint.Handler.SocketState.CLOSED;
        }
        endRequest();
        this.inputBuffer.nextRequest();
        this.outputBuffer.nextRequest();
        if (this.socketWrapper.isReadPending()) {
            return AbstractEndpoint.Handler.SocketState.LONG;
        }
        return AbstractEndpoint.Handler.SocketState.OPEN;
    }

    @Override // org.apache.coyote.AbstractProcessorLight
    protected Log getLog() {
        return log;
    }

    private void endRequest() {
        if (getErrorState().isError()) {
            this.inputBuffer.setSwallowInput(false);
        } else {
            checkExpectationAndResponseStatus();
        }
        if (getErrorState().isIoAllowed()) {
            try {
                this.inputBuffer.endRequest();
            } catch (IOException e) {
                setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
            } catch (Throwable t) {
                ExceptionUtils.handleThrowable(t);
                this.response.setStatus(500);
                setErrorState(ErrorState.CLOSE_NOW, t);
                log.error(sm.getString("http11processor.request.finish"), t);
            }
        }
        if (getErrorState().isIoAllowed()) {
            try {
                action(ActionCode.COMMIT, null);
                this.outputBuffer.end();
            } catch (IOException e2) {
                setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e2);
            } catch (Throwable t2) {
                ExceptionUtils.handleThrowable(t2);
                setErrorState(ErrorState.CLOSE_NOW, t2);
                log.error(sm.getString("http11processor.response.finish"), t2);
            }
        }
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void finishResponse() throws IOException {
        this.outputBuffer.end();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void ack() {
        if (!this.response.isCommitted() && this.request.hasExpectation()) {
            this.inputBuffer.setSwallowInput(true);
            try {
                this.outputBuffer.sendAck();
            } catch (IOException e) {
                setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
            }
        }
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void flush() throws IOException {
        this.outputBuffer.flush();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final int available(boolean doRead) {
        return this.inputBuffer.available(doRead);
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void setRequestBody(ByteChunk body) {
        InputFilter savedBody = new SavedRequestInputFilter(body);
        Http11InputBuffer internalBuffer = (Http11InputBuffer) this.request.getInputBuffer();
        internalBuffer.addActiveFilter(savedBody);
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void setSwallowResponse() {
        this.outputBuffer.responseFinished = true;
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void disableSwallowRequest() {
        this.inputBuffer.setSwallowInput(false);
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void sslReHandShake() throws IOException {
        if (this.sslSupport != null) {
            InputFilter[] inputFilters = this.inputBuffer.getFilters();
            ((BufferedInputFilter) inputFilters[3]).setLimit(this.maxSavePostSize);
            this.inputBuffer.addActiveFilter(inputFilters[3]);
            this.socketWrapper.doClientAuth(this.sslSupport);
            try {
                Object sslO = this.sslSupport.getPeerCertificateChain();
                if (sslO != null) {
                    this.request.setAttribute("javax.servlet.request.X509Certificate", sslO);
                }
            } catch (IOException ioe) {
                log.warn(sm.getString("http11processor.socket.ssl"), ioe);
            }
        }
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final boolean isRequestBodyFullyRead() {
        return this.inputBuffer.isFinished();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void registerReadInterest() {
        this.socketWrapper.registerReadInterest();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final boolean isReadyForWrite() {
        return this.outputBuffer.isReady();
    }

    @Override // org.apache.coyote.AbstractProcessor, org.apache.coyote.Processor
    public UpgradeToken getUpgradeToken() {
        return this.upgradeToken;
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void doHttpUpgrade(UpgradeToken upgradeToken) {
        this.upgradeToken = upgradeToken;
        this.outputBuffer.responseFinished = true;
    }

    @Override // org.apache.coyote.AbstractProcessor, org.apache.coyote.Processor
    public ByteBuffer getLeftoverInput() {
        return this.inputBuffer.getLeftover();
    }

    @Override // org.apache.coyote.AbstractProcessor, org.apache.coyote.Processor
    public boolean isUpgrade() {
        return this.upgradeToken != null;
    }

    private SendfileState processSendfile(SocketWrapperBase<?> socketWrapper) {
        this.openSocket = this.keepAlive;
        SendfileState result = SendfileState.DONE;
        if (this.sendfileData != null && !getErrorState().isError()) {
            if (this.keepAlive) {
                if (available(false) == 0) {
                    this.sendfileData.keepAliveState = SendfileKeepAliveState.OPEN;
                } else {
                    this.sendfileData.keepAliveState = SendfileKeepAliveState.PIPELINED;
                }
            } else {
                this.sendfileData.keepAliveState = SendfileKeepAliveState.NONE;
            }
            result = socketWrapper.processSendfile(this.sendfileData);
            switch (result) {
                case ERROR:
                    if (log.isDebugEnabled()) {
                        log.debug(sm.getString("http11processor.sendfile.error"));
                    }
                    setErrorState(ErrorState.CLOSE_CONNECTION_NOW, null);
                    break;
            }
            this.sendfileData = null;
        }
        return result;
    }

    @Override // org.apache.coyote.AbstractProcessor, org.apache.coyote.Processor
    public final void recycle() {
        getAdapter().checkRecycled(this.request, this.response);
        super.recycle();
        this.inputBuffer.recycle();
        this.outputBuffer.recycle();
        this.upgradeToken = null;
        this.socketWrapper = null;
        this.sendfileData = null;
    }

    @Override // org.apache.coyote.Processor
    public void pause() {
    }
}
