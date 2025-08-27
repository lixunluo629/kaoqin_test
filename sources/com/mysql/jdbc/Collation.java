package com.mysql.jdbc;

import org.springframework.beans.PropertyAccessor;

/* compiled from: CharsetMapping.java */
/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/Collation.class */
class Collation {
    public final int index;
    public final String collationName;
    public final int priority;
    public final MysqlCharset mysqlCharset;

    public Collation(int index, String collationName, int priority, String charsetName) {
        this.index = index;
        this.collationName = collationName;
        this.priority = priority;
        this.mysqlCharset = CharsetMapping.CHARSET_NAME_TO_CHARSET.get(charsetName);
    }

    public String toString() {
        return PropertyAccessor.PROPERTY_KEY_PREFIX + "index=" + this.index + ",collationName=" + this.collationName + ",charsetName=" + this.mysqlCharset.charsetName + ",javaCharsetName=" + this.mysqlCharset.getMatchingJavaEncoding(null) + "]";
    }
}
