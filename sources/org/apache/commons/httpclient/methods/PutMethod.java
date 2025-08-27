package org.apache.commons.httpclient.methods;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/PutMethod.class */
public class PutMethod extends EntityEnclosingMethod {
    public PutMethod() {
    }

    public PutMethod(String uri) {
        super(uri);
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public String getName() {
        return "PUT";
    }
}
