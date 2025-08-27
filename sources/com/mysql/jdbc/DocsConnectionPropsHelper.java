package com.mysql.jdbc;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DocsConnectionPropsHelper.class */
public class DocsConnectionPropsHelper extends ConnectionPropertiesImpl {
    static final long serialVersionUID = -1580779062220390294L;

    public static void main(String[] args) throws Exception {
        System.out.println(new DocsConnectionPropsHelper().exposeAsXml());
    }
}
