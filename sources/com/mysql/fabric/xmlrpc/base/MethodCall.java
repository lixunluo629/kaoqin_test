package com.mysql.fabric.xmlrpc.base;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/xmlrpc/base/MethodCall.class */
public class MethodCall {
    protected String methodName;
    protected Params params;

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String value) {
        this.methodName = value;
    }

    public Params getParams() {
        return this.params;
    }

    public void setParams(Params value) {
        this.params = value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<methodCall>");
        sb.append("\t<methodName>" + this.methodName + "</methodName>");
        if (this.params != null) {
            sb.append(this.params.toString());
        }
        sb.append("</methodCall>");
        return sb.toString();
    }
}
