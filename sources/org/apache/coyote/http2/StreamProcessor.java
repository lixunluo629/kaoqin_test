package org.apache.coyote.http2;

import java.io.IOException;
import java.security.PrivilegedActionException;
import java.util.Iterator;
import org.apache.coyote.AbstractProcessor;
import org.apache.coyote.ActionCode;
import org.apache.coyote.Adapter;
import org.apache.coyote.ContainerThreadMarker;
import org.apache.coyote.ErrorState;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.apache.coyote.http11.filters.GzipOutputFilter;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.http.FastHttpDateFormat;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.DispatchType;
import org.apache.tomcat.util.net.SocketEvent;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/StreamProcessor.class */
class StreamProcessor extends AbstractProcessor {
    private static final Log log = LogFactory.getLog((Class<?>) StreamProcessor.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) StreamProcessor.class);
    private final Http2UpgradeHandler handler;
    private final Stream stream;

    StreamProcessor(Http2UpgradeHandler handler, Stream stream, Adapter adapter, SocketWrapperBase<?> socketWrapper) {
        super(socketWrapper.getEndpoint(), stream.getCoyoteRequest(), stream.getCoyoteResponse());
        this.handler = handler;
        this.stream = stream;
        setAdapter(adapter);
        setSocketWrapper(socketWrapper);
    }

    final void process(SocketEvent event) {
        try {
            synchronized (this) {
                ContainerThreadMarker.set();
                AbstractEndpoint.Handler.SocketState socketState = AbstractEndpoint.Handler.SocketState.CLOSED;
                try {
                    try {
                        AbstractEndpoint.Handler.SocketState state = process(this.socketWrapper, event);
                        if (state == AbstractEndpoint.Handler.SocketState.CLOSED) {
                            if (!getErrorState().isConnectionIoAllowed()) {
                                this.stream.close(new ConnectionException(sm.getString("streamProcessor.error.connection", this.stream.getConnectionId(), this.stream.getIdentifier()), Http2Error.INTERNAL_ERROR));
                            } else if (!getErrorState().isIoAllowed()) {
                                StreamException se = this.stream.getResetException();
                                if (se == null) {
                                    se = new StreamException(sm.getString("streamProcessor.error.stream", this.stream.getConnectionId(), this.stream.getIdentifier()), Http2Error.INTERNAL_ERROR, this.stream.getIdAsInt());
                                }
                                this.stream.close(se);
                            }
                        }
                        ContainerThreadMarker.clear();
                    } catch (Exception e) {
                        String msg = sm.getString("streamProcessor.error.connection", this.stream.getConnectionId(), this.stream.getIdentifier());
                        if (log.isDebugEnabled()) {
                            log.debug(msg, e);
                        }
                        ConnectionException ce = new ConnectionException(msg, Http2Error.INTERNAL_ERROR);
                        ce.initCause(e);
                        this.stream.close(ce);
                        ContainerThreadMarker.clear();
                    }
                } catch (Throwable th) {
                    ContainerThreadMarker.clear();
                    throw th;
                }
            }
        } finally {
            this.handler.executeQueuedStream();
        }
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void prepareResponse() throws IOException {
        this.response.setCommitted(true);
        prepareHeaders(this.request, this.response, this.handler.getProtocol(), this.stream);
        this.stream.writeHeaders();
    }

    static void prepareHeaders(Request coyoteRequest, Response coyoteResponse, Http2Protocol protocol, Stream stream) {
        MimeHeaders headers = coyoteResponse.getMimeHeaders();
        int statusCode = coyoteResponse.getStatus();
        headers.addValue(":status").setString(Integer.toString(statusCode));
        if (statusCode >= 200 && statusCode != 205 && statusCode != 304) {
            String contentType = coyoteResponse.getContentType();
            if (contentType != null) {
                headers.setValue("content-type").setString(contentType);
            }
            String contentLanguage = coyoteResponse.getContentLanguage();
            if (contentLanguage != null) {
                headers.setValue("content-language").setString(contentLanguage);
            }
        }
        long contentLength = coyoteResponse.getContentLengthLong();
        if (contentLength != -1 && headers.getValue("content-length") == null) {
            headers.addValue("content-length").setLong(contentLength);
        }
        if (statusCode >= 200 && headers.getValue("date") == null) {
            headers.addValue("date").setString(FastHttpDateFormat.getCurrentDate());
        }
        if (protocol != null && protocol.useCompression(coyoteRequest, coyoteResponse)) {
            stream.addOutputFilter(new GzipOutputFilter());
        }
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void finishResponse() throws IOException {
        this.stream.getOutputBuffer().end();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void ack() {
        if (!this.response.isCommitted() && this.request.hasExpectation()) {
            try {
                this.stream.writeAck();
            } catch (IOException ioe) {
                setErrorState(ErrorState.CLOSE_CONNECTION_NOW, ioe);
            }
        }
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void flush() throws IOException {
        this.stream.getOutputBuffer().flush();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final int available(boolean doRead) {
        return this.stream.getInputBuffer().available();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void setRequestBody(ByteChunk body) {
        this.stream.getInputBuffer().insertReplayedBody(body);
        try {
            this.stream.receivedEndOfStream();
        } catch (ConnectionException e) {
        }
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void setSwallowResponse() {
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void disableSwallowRequest() {
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected void processSocketEvent(SocketEvent event, boolean dispatch) {
        if (dispatch) {
            this.handler.processStreamOnContainerThread(this, event);
        } else {
            process(event);
        }
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final boolean isReadyForRead() {
        return this.stream.getInputBuffer().isReadyForRead();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final boolean isRequestBodyFullyRead() {
        return this.stream.getInputBuffer().isRequestBodyFullyRead();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void registerReadInterest() {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final boolean isReadyForWrite() {
        return this.stream.isReadyForWrite();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void executeDispatches() {
        Iterator<DispatchType> dispatches = getIteratorAndClearDispatches();
        while (dispatches != null && dispatches.hasNext()) {
            DispatchType dispatchType = dispatches.next();
            processSocketEvent(dispatchType.getSocketStatus(), true);
        }
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final boolean isPushSupported() {
        return this.stream.isPushSupported();
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final void doPush(Request pushTarget) throws PrivilegedActionException {
        try {
            this.stream.push(pushTarget);
        } catch (IOException ioe) {
            setErrorState(ErrorState.CLOSE_CONNECTION_NOW, ioe);
            this.response.setErrorException(ioe);
        }
    }

    @Override // org.apache.coyote.AbstractProcessor, org.apache.coyote.Processor
    public void recycle() {
        setSocketWrapper(null);
        setAdapter(null);
    }

    @Override // org.apache.coyote.AbstractProcessorLight
    protected Log getLog() {
        return log;
    }

    @Override // org.apache.coyote.Processor
    public void pause() {
    }

    @Override // org.apache.coyote.AbstractProcessorLight
    public AbstractEndpoint.Handler.SocketState service(SocketWrapperBase<?> socket) throws IOException {
        try {
            this.adapter.service(this.request, this.response);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("streamProcessor.service.error"), e);
            }
            this.response.setStatus(500);
            setErrorState(ErrorState.CLOSE_NOW, e);
        }
        if (!isAsync()) {
            endRequest();
        }
        if (getErrorState().isError()) {
            action(ActionCode.CLOSE, null);
            this.request.updateCounters();
            return AbstractEndpoint.Handler.SocketState.CLOSED;
        }
        if (isAsync()) {
            return AbstractEndpoint.Handler.SocketState.LONG;
        }
        action(ActionCode.CLOSE, null);
        this.request.updateCounters();
        return AbstractEndpoint.Handler.SocketState.CLOSED;
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected boolean flushBufferedWrite() throws IOException {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("streamProcessor.flushBufferedWrite.entry", this.stream.getConnectionId(), this.stream.getIdentifier()));
        }
        if (this.stream.flush(false)) {
            if (this.stream.isReadyForWrite()) {
                throw new IllegalStateException();
            }
            return true;
        }
        return false;
    }

    @Override // org.apache.coyote.AbstractProcessor
    protected final AbstractEndpoint.Handler.SocketState dispatchEndRequest() throws IOException {
        endRequest();
        return AbstractEndpoint.Handler.SocketState.CLOSED;
    }

    private void endRequest() throws IOException {
        if (!this.stream.isInputFinished() && getErrorState().isIoAllowed()) {
            StreamException se = new StreamException(sm.getString("streamProcessor.cancel", this.stream.getConnectionId(), this.stream.getIdentifier()), Http2Error.CANCEL, this.stream.getIdAsInt());
            this.handler.sendStreamReset(se);
        }
    }
}
