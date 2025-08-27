package org.apache.commons.httpclient.methods;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/OptionsMethod.class */
public class OptionsMethod extends HttpMethodBase {
    private static final Log LOG;
    private Vector methodsAllowed;
    static Class class$org$apache$commons$httpclient$methods$OptionsMethod;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$methods$OptionsMethod == null) {
            clsClass$ = class$("org.apache.commons.httpclient.methods.OptionsMethod");
            class$org$apache$commons$httpclient$methods$OptionsMethod = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$methods$OptionsMethod;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public OptionsMethod() {
        this.methodsAllowed = new Vector();
    }

    public OptionsMethod(String uri) {
        super(uri);
        this.methodsAllowed = new Vector();
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public String getName() {
        return "OPTIONS";
    }

    public boolean isAllowed(String method) {
        checkUsed();
        return this.methodsAllowed.contains(method);
    }

    public Enumeration getAllowedMethods() {
        checkUsed();
        return this.methodsAllowed.elements();
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase
    protected void processResponseHeaders(HttpState state, HttpConnection conn) {
        LOG.trace("enter OptionsMethod.processResponseHeaders(HttpState, HttpConnection)");
        Header allowHeader = getResponseHeader("allow");
        if (allowHeader != null) {
            String allowHeaderValue = allowHeader.getValue();
            StringTokenizer tokenizer = new StringTokenizer(allowHeaderValue, ",");
            while (tokenizer.hasMoreElements()) {
                String methodAllowed = tokenizer.nextToken().trim().toUpperCase();
                this.methodsAllowed.addElement(methodAllowed);
            }
        }
    }

    public boolean needContentLength() {
        return false;
    }
}
