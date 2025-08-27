package com.mysql.fabric.xmlrpc.base;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/xmlrpc/base/Param.class */
public class Param {
    protected Value value;

    public Param() {
    }

    public Param(Value value) {
        this.value = value;
    }

    public Value getValue() {
        return this.value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String toString() {
        return "<param>" + this.value.toString() + "</param>";
    }
}
