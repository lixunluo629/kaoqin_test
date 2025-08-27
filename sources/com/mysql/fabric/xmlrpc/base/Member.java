package com.mysql.fabric.xmlrpc.base;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/xmlrpc/base/Member.class */
public class Member {
    protected String name;
    protected Value value;

    public Member() {
    }

    public Member(String name, Value value) {
        setName(name);
        setValue(value);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Value getValue() {
        return this.value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<member>");
        sb.append("<name>" + this.name + "</name>");
        sb.append(this.value.toString());
        sb.append("</member>");
        return sb.toString();
    }
}
