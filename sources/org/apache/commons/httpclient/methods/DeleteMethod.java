package org.apache.commons.httpclient.methods;

import org.apache.commons.httpclient.HttpMethodBase;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/DeleteMethod.class */
public class DeleteMethod extends HttpMethodBase {
    public DeleteMethod() {
    }

    public DeleteMethod(String uri) {
        super(uri);
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public String getName() {
        return "DELETE";
    }
}
