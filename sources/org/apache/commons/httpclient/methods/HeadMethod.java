package org.apache.commons.httpclient.methods;

import java.io.IOException;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.ProtocolException;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.support.WebContentGenerator;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/HeadMethod.class */
public class HeadMethod extends HttpMethodBase {
    private static final Log LOG;
    static Class class$org$apache$commons$httpclient$methods$HeadMethod;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$methods$HeadMethod == null) {
            clsClass$ = class$("org.apache.commons.httpclient.methods.HeadMethod");
            class$org$apache$commons$httpclient$methods$HeadMethod = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$methods$HeadMethod;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public HeadMethod() {
        setFollowRedirects(true);
    }

    public HeadMethod(String uri) {
        super(uri);
        setFollowRedirects(true);
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public String getName() {
        return WebContentGenerator.METHOD_HEAD;
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public void recycle() {
        super.recycle();
        setFollowRedirects(true);
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase
    protected void readResponseBody(HttpState state, HttpConnection conn) throws IllegalStateException, IOException {
        boolean responseAvailable;
        LOG.trace("enter HeadMethod.readResponseBody(HttpState, HttpConnection)");
        int bodyCheckTimeout = getParams().getIntParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, -1);
        if (bodyCheckTimeout < 0) {
            responseBodyConsumed();
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Check for non-compliant response body. Timeout in ").append(bodyCheckTimeout).append(" ms").toString());
        }
        try {
            responseAvailable = conn.isResponseAvailable(bodyCheckTimeout);
        } catch (IOException e) {
            LOG.debug("An IOException occurred while testing if a response was available, we will assume one is not.", e);
            responseAvailable = false;
        }
        if (responseAvailable) {
            if (getParams().isParameterTrue(HttpMethodParams.REJECT_HEAD_BODY)) {
                throw new ProtocolException("Body content may not be sent in response to HTTP HEAD request");
            }
            LOG.warn("Body content returned in response to HTTP HEAD");
            super.readResponseBody(state, conn);
        }
    }

    public int getBodyCheckTimeout() {
        return getParams().getIntParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, -1);
    }

    public void setBodyCheckTimeout(int timeout) {
        getParams().setIntParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, timeout);
    }
}
