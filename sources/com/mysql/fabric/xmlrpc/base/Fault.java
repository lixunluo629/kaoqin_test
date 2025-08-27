package com.mysql.fabric.xmlrpc.base;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/xmlrpc/base/Fault.class */
public class Fault {
    protected Value value;

    public Value getValue() {
        return this.value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.value != null) {
            sb.append("<fault>");
            sb.append(this.value.toString());
            sb.append("</fault>");
        }
        return sb.toString();
    }
}
