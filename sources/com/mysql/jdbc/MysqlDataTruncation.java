package com.mysql.jdbc;

import java.sql.DataTruncation;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/MysqlDataTruncation.class */
public class MysqlDataTruncation extends DataTruncation {
    static final long serialVersionUID = 3263928195256986226L;
    private String message;
    private int vendorErrorCode;

    public MysqlDataTruncation(String message, int index, boolean parameter, boolean read, int dataSize, int transferSize, int vendorErrorCode) {
        super(index, parameter, read, dataSize, transferSize);
        this.message = message;
        this.vendorErrorCode = vendorErrorCode;
    }

    @Override // java.sql.SQLException
    public int getErrorCode() {
        return this.vendorErrorCode;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return super.getMessage() + ": " + this.message;
    }
}
