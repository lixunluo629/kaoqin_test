package org.springframework.jdbc.datasource.init;

import org.springframework.core.io.support.EncodedResource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/init/ScriptStatementFailedException.class */
public class ScriptStatementFailedException extends ScriptException {
    public ScriptStatementFailedException(String stmt, int stmtNumber, EncodedResource encodedResource, Throwable cause) {
        super(buildErrorMessage(stmt, stmtNumber, encodedResource), cause);
    }

    public static String buildErrorMessage(String stmt, int stmtNumber, EncodedResource encodedResource) {
        return String.format("Failed to execute SQL script statement #%s of %s: %s", Integer.valueOf(stmtNumber), encodedResource, stmt);
    }
}
