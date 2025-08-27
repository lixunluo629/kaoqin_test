package com.mysql.jdbc.util;

import com.mysql.jdbc.SQLError;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/util/ErrorMappingsDocGenerator.class */
public class ErrorMappingsDocGenerator {
    public static void main(String[] args) throws Exception {
        SQLError.dumpSqlStatesMappingsAsXml();
    }
}
