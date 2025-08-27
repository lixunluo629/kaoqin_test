package com.mysql.jdbc;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/EscapeProcessorResult.class */
class EscapeProcessorResult {
    String escapedSql;
    boolean callingStoredFunction = false;
    byte usesVariables = 0;

    EscapeProcessorResult() {
    }
}
