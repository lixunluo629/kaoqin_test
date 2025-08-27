package org.apache.commons.httpclient.methods;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.ChunkedOutputStream;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.ProtocolException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/EntityEnclosingMethod.class */
public abstract class EntityEnclosingMethod extends ExpectContinueMethod {
    public static final long CONTENT_LENGTH_AUTO = -2;
    public static final long CONTENT_LENGTH_CHUNKED = -1;
    private static final Log LOG;
    private InputStream requestStream;
    private String requestString;
    private RequestEntity requestEntity;
    private int repeatCount;
    private long requestContentLength;
    private boolean chunked;
    static Class class$org$apache$commons$httpclient$methods$EntityEnclosingMethod;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$methods$EntityEnclosingMethod == null) {
            clsClass$ = class$("org.apache.commons.httpclient.methods.EntityEnclosingMethod");
            class$org$apache$commons$httpclient$methods$EntityEnclosingMethod = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$methods$EntityEnclosingMethod;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public EntityEnclosingMethod() {
        this.requestStream = null;
        this.requestString = null;
        this.repeatCount = 0;
        this.requestContentLength = -2L;
        this.chunked = false;
        setFollowRedirects(false);
    }

    public EntityEnclosingMethod(String uri) {
        super(uri);
        this.requestStream = null;
        this.requestString = null;
        this.repeatCount = 0;
        this.requestContentLength = -2L;
        this.chunked = false;
        setFollowRedirects(false);
    }

    @Override // org.apache.commons.httpclient.methods.ExpectContinueMethod
    protected boolean hasRequestContent() {
        LOG.trace("enter EntityEnclosingMethod.hasRequestContent()");
        return (this.requestEntity == null && this.requestStream == null && this.requestString == null) ? false : true;
    }

    protected void clearRequestBody() {
        LOG.trace("enter EntityEnclosingMethod.clearRequestBody()");
        this.requestStream = null;
        this.requestString = null;
        this.requestEntity = null;
    }

    protected byte[] generateRequestBody() {
        LOG.trace("enter EntityEnclosingMethod.renerateRequestBody()");
        return null;
    }

    protected RequestEntity generateRequestEntity() {
        byte[] requestBody = generateRequestBody();
        if (requestBody != null) {
            this.requestEntity = new ByteArrayRequestEntity(requestBody);
        } else if (this.requestStream != null) {
            this.requestEntity = new InputStreamRequestEntity(this.requestStream, this.requestContentLength);
            this.requestStream = null;
        } else if (this.requestString != null) {
            String charset = getRequestCharSet();
            try {
                this.requestEntity = new StringRequestEntity(this.requestString, null, charset);
            } catch (UnsupportedEncodingException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn(new StringBuffer().append(charset).append(" not supported").toString());
                }
                try {
                    this.requestEntity = new StringRequestEntity(this.requestString, null, null);
                } catch (UnsupportedEncodingException e2) {
                }
            }
        }
        return this.requestEntity;
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public boolean getFollowRedirects() {
        return false;
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public void setFollowRedirects(boolean followRedirects) {
        if (followRedirects) {
            throw new IllegalArgumentException("Entity enclosing requests cannot be redirected without user intervention");
        }
        super.setFollowRedirects(false);
    }

    public void setRequestContentLength(int length) {
        LOG.trace("enter EntityEnclosingMethod.setRequestContentLength(int)");
        this.requestContentLength = length;
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase
    public String getRequestCharSet() {
        if (getRequestHeader("Content-Type") == null) {
            if (this.requestEntity != null) {
                return getContentCharSet(new Header("Content-Type", this.requestEntity.getContentType()));
            }
            return super.getRequestCharSet();
        }
        return super.getRequestCharSet();
    }

    public void setRequestContentLength(long length) {
        LOG.trace("enter EntityEnclosingMethod.setRequestContentLength(int)");
        this.requestContentLength = length;
    }

    public void setContentChunked(boolean chunked) {
        this.chunked = chunked;
    }

    protected long getRequestContentLength() {
        LOG.trace("enter EntityEnclosingMethod.getRequestContentLength()");
        if (!hasRequestContent()) {
            return 0L;
        }
        if (this.chunked) {
            return -1L;
        }
        if (this.requestEntity == null) {
            this.requestEntity = generateRequestEntity();
        }
        if (this.requestEntity == null) {
            return 0L;
        }
        return this.requestEntity.getContentLength();
    }

    @Override // org.apache.commons.httpclient.methods.ExpectContinueMethod, org.apache.commons.httpclient.HttpMethodBase
    protected void addRequestHeaders(HttpState state, HttpConnection conn) throws IOException, IllegalArgumentException {
        RequestEntity requestEntity;
        LOG.trace("enter EntityEnclosingMethod.addRequestHeaders(HttpState, HttpConnection)");
        super.addRequestHeaders(state, conn);
        addContentLengthRequestHeader(state, conn);
        if (getRequestHeader("Content-Type") == null && (requestEntity = getRequestEntity()) != null && requestEntity.getContentType() != null) {
            setRequestHeader("Content-Type", requestEntity.getContentType());
        }
    }

    protected void addContentLengthRequestHeader(HttpState state, HttpConnection conn) throws IOException {
        LOG.trace("enter EntityEnclosingMethod.addContentLengthRequestHeader(HttpState, HttpConnection)");
        if (getRequestHeader("content-length") == null && getRequestHeader("Transfer-Encoding") == null) {
            long len = getRequestContentLength();
            if (len < 0) {
                if (getEffectiveVersion().greaterEquals(HttpVersion.HTTP_1_1)) {
                    addRequestHeader("Transfer-Encoding", "chunked");
                    return;
                }
                throw new ProtocolException(new StringBuffer().append(getEffectiveVersion()).append(" does not support chunk encoding").toString());
            }
            addRequestHeader("Content-Length", String.valueOf(len));
        }
    }

    public void setRequestBody(InputStream body) {
        LOG.trace("enter EntityEnclosingMethod.setRequestBody(InputStream)");
        clearRequestBody();
        this.requestStream = body;
    }

    public void setRequestBody(String body) {
        LOG.trace("enter EntityEnclosingMethod.setRequestBody(String)");
        clearRequestBody();
        this.requestString = body;
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase
    protected boolean writeRequestBody(HttpState state, HttpConnection conn) throws IllegalStateException, IOException {
        LOG.trace("enter EntityEnclosingMethod.writeRequestBody(HttpState, HttpConnection)");
        if (!hasRequestContent()) {
            LOG.debug("Request body has not been specified");
            return true;
        }
        if (this.requestEntity == null) {
            this.requestEntity = generateRequestEntity();
        }
        if (this.requestEntity == null) {
            LOG.debug("Request body is empty");
            return true;
        }
        long contentLength = getRequestContentLength();
        if (this.repeatCount > 0 && !this.requestEntity.isRepeatable()) {
            throw new ProtocolException("Unbuffered entity enclosing request can not be repeated.");
        }
        this.repeatCount++;
        OutputStream outstream = conn.getRequestOutputStream();
        if (contentLength < 0) {
            outstream = new ChunkedOutputStream(outstream);
        }
        this.requestEntity.writeRequest(outstream);
        if (outstream instanceof ChunkedOutputStream) {
            ((ChunkedOutputStream) outstream).finish();
        }
        outstream.flush();
        LOG.debug("Request body sent");
        return true;
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public void recycle() {
        LOG.trace("enter EntityEnclosingMethod.recycle()");
        clearRequestBody();
        this.requestContentLength = -2L;
        this.repeatCount = 0;
        this.chunked = false;
        super.recycle();
    }

    public RequestEntity getRequestEntity() {
        return generateRequestEntity();
    }

    public void setRequestEntity(RequestEntity requestEntity) {
        clearRequestBody();
        this.requestEntity = requestEntity;
    }
}
