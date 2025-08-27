package org.apache.commons.httpclient.methods;

import org.apache.commons.httpclient.HttpMethodBase;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/TraceMethod.class */
public class TraceMethod extends HttpMethodBase {
    public TraceMethod(String uri) {
        super(uri);
        setFollowRedirects(false);
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public String getName() {
        return "TRACE";
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public void recycle() {
        super.recycle();
        setFollowRedirects(false);
    }
}
