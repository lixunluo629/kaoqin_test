package org.apache.commons.httpclient.methods;

import java.io.IOException;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/ExpectContinueMethod.class */
public abstract class ExpectContinueMethod extends HttpMethodBase {
    private static final Log LOG;
    static Class class$org$apache$commons$httpclient$methods$ExpectContinueMethod;

    protected abstract boolean hasRequestContent();

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$methods$ExpectContinueMethod == null) {
            clsClass$ = class$("org.apache.commons.httpclient.methods.ExpectContinueMethod");
            class$org$apache$commons$httpclient$methods$ExpectContinueMethod = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$methods$ExpectContinueMethod;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public ExpectContinueMethod() {
    }

    public ExpectContinueMethod(String uri) {
        super(uri);
    }

    public boolean getUseExpectHeader() {
        return getParams().getBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, false);
    }

    public void setUseExpectHeader(boolean value) {
        getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, value);
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase
    protected void addRequestHeaders(HttpState state, HttpConnection conn) throws IOException, IllegalArgumentException {
        LOG.trace("enter ExpectContinueMethod.addRequestHeaders(HttpState, HttpConnection)");
        super.addRequestHeaders(state, conn);
        boolean headerPresent = getRequestHeader("Expect") != null;
        if (getParams().isParameterTrue(HttpMethodParams.USE_EXPECT_CONTINUE) && getEffectiveVersion().greaterEquals(HttpVersion.HTTP_1_1) && hasRequestContent()) {
            if (!headerPresent) {
                setRequestHeader("Expect", "100-continue");
            }
        } else if (headerPresent) {
            removeRequestHeader("Expect");
        }
    }
}
