package org.apache.ibatis.mapping;

import com.mysql.jdbc.MysqlErrorNumbers;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/ResultSetType.class */
public enum ResultSetType {
    FORWARD_ONLY(1003),
    SCROLL_INSENSITIVE(MysqlErrorNumbers.ER_CANT_CREATE_FILE),
    SCROLL_SENSITIVE(1005);

    private final int value;

    ResultSetType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
