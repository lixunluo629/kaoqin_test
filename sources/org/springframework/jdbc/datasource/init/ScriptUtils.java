package org.springframework.jdbc.datasource.init;

import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/init/ScriptUtils.class */
public abstract class ScriptUtils {
    public static final String DEFAULT_STATEMENT_SEPARATOR = ";";
    public static final String FALLBACK_STATEMENT_SEPARATOR = "\n";
    public static final String EOF_STATEMENT_SEPARATOR = "^^^ END OF SCRIPT ^^^";
    public static final String DEFAULT_COMMENT_PREFIX = "--";
    public static final String DEFAULT_BLOCK_COMMENT_START_DELIMITER = "/*";
    public static final String DEFAULT_BLOCK_COMMENT_END_DELIMITER = "*/";
    private static final Log logger = LogFactory.getLog(ScriptUtils.class);

    public static void splitSqlScript(String script, char separator, List<String> statements) throws ScriptException {
        splitSqlScript(script, String.valueOf(separator), statements);
    }

    public static void splitSqlScript(String script, String separator, List<String> statements) throws ScriptException {
        splitSqlScript(null, script, separator, DEFAULT_COMMENT_PREFIX, DEFAULT_BLOCK_COMMENT_START_DELIMITER, "*/", statements);
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x0184 A[PHI: r17
  0x0184: PHI (r17v1 'c' char) = (r17v0 'c' char), (r17v0 'c' char), (r17v2 'c' char), (r17v0 'c' char) binds: [B:30:0x00a3, B:32:0x00a8, B:63:0x0180, B:58:0x0164] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void splitSqlScript(org.springframework.core.io.support.EncodedResource r5, java.lang.String r6, java.lang.String r7, java.lang.String r8, java.lang.String r9, java.lang.String r10, java.util.List<java.lang.String> r11) throws org.springframework.jdbc.datasource.init.ScriptException {
        /*
            Method dump skipped, instructions count: 424
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.jdbc.datasource.init.ScriptUtils.splitSqlScript(org.springframework.core.io.support.EncodedResource, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List):void");
    }

    static String readScript(EncodedResource resource) throws IOException {
        return readScript(resource, DEFAULT_COMMENT_PREFIX, DEFAULT_STATEMENT_SEPARATOR);
    }

    private static String readScript(EncodedResource resource, String commentPrefix, String separator) throws IOException {
        LineNumberReader lnr = new LineNumberReader(resource.getReader());
        try {
            String script = readScript(lnr, commentPrefix, separator);
            lnr.close();
            return script;
        } catch (Throwable th) {
            lnr.close();
            throw th;
        }
    }

    public static String readScript(LineNumberReader lineNumberReader, String commentPrefix, String separator) throws IOException {
        String currentStatement = lineNumberReader.readLine();
        StringBuilder scriptBuilder = new StringBuilder();
        while (currentStatement != null) {
            if (commentPrefix != null && !currentStatement.startsWith(commentPrefix)) {
                if (scriptBuilder.length() > 0) {
                    scriptBuilder.append('\n');
                }
                scriptBuilder.append(currentStatement);
            }
            currentStatement = lineNumberReader.readLine();
        }
        appendSeparatorToScriptIfNecessary(scriptBuilder, separator);
        return scriptBuilder.toString();
    }

    private static void appendSeparatorToScriptIfNecessary(StringBuilder scriptBuilder, String separator) {
        if (separator == null) {
            return;
        }
        String trimmed = separator.trim();
        if (trimmed.length() != separator.length() && scriptBuilder.lastIndexOf(trimmed) == scriptBuilder.length() - trimmed.length()) {
            scriptBuilder.append(separator.substring(trimmed.length()));
        }
    }

    public static boolean containsSqlScriptDelimiters(String script, String delim) {
        boolean inLiteral = false;
        for (int i = 0; i < script.length(); i++) {
            if (script.charAt(i) == '\'') {
                inLiteral = !inLiteral;
            }
            if (!inLiteral && script.startsWith(delim, i)) {
                return true;
            }
        }
        return false;
    }

    public static void executeSqlScript(Connection connection, Resource resource) throws ScriptException {
        executeSqlScript(connection, new EncodedResource(resource));
    }

    public static void executeSqlScript(Connection connection, EncodedResource resource) throws ScriptException {
        executeSqlScript(connection, resource, false, false, DEFAULT_COMMENT_PREFIX, DEFAULT_STATEMENT_SEPARATOR, DEFAULT_BLOCK_COMMENT_START_DELIMITER, "*/");
    }

    /* JADX WARN: Finally extract failed */
    public static void executeSqlScript(Connection connection, EncodedResource resource, boolean continueOnError, boolean ignoreFailedDrops, String commentPrefix, String separator, String blockCommentStartDelimiter, String blockCommentEndDelimiter) throws ScriptException {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("Executing SQL script from " + resource);
            }
            long startTime = System.currentTimeMillis();
            try {
                String script = readScript(resource, commentPrefix, separator);
                if (separator == null) {
                    separator = DEFAULT_STATEMENT_SEPARATOR;
                }
                if (!EOF_STATEMENT_SEPARATOR.equals(separator) && !containsSqlScriptDelimiters(script, separator)) {
                    separator = FALLBACK_STATEMENT_SEPARATOR;
                }
                List<String> statements = new LinkedList<>();
                splitSqlScript(resource, script, separator, commentPrefix, blockCommentStartDelimiter, blockCommentEndDelimiter, statements);
                int stmtNumber = 0;
                Statement stmt = connection.createStatement();
                try {
                    for (String statement : statements) {
                        stmtNumber++;
                        try {
                            stmt.execute(statement);
                            int rowsAffected = stmt.getUpdateCount();
                            if (logger.isDebugEnabled()) {
                                logger.debug(rowsAffected + " returned as update count for SQL: " + statement);
                                for (SQLWarning warningToLog = stmt.getWarnings(); warningToLog != null; warningToLog = warningToLog.getNextWarning()) {
                                    logger.debug("SQLWarning ignored: SQL state '" + warningToLog.getSQLState() + "', error code '" + warningToLog.getErrorCode() + "', message [" + warningToLog.getMessage() + "]");
                                }
                            }
                        } catch (SQLException ex) {
                            boolean dropStatement = StringUtils.startsWithIgnoreCase(statement.trim(), "drop");
                            if (continueOnError || (dropStatement && ignoreFailedDrops)) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug(ScriptStatementFailedException.buildErrorMessage(statement, stmtNumber, resource), ex);
                                }
                            } else {
                                throw new ScriptStatementFailedException(statement, stmtNumber, resource, ex);
                            }
                        }
                    }
                    try {
                        stmt.close();
                    } catch (Throwable ex2) {
                        logger.debug("Could not close JDBC Statement", ex2);
                    }
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    if (logger.isInfoEnabled()) {
                        logger.info("Executed SQL script from " + resource + " in " + elapsedTime + " ms.");
                    }
                } catch (Throwable th) {
                    try {
                        stmt.close();
                    } catch (Throwable ex3) {
                        logger.debug("Could not close JDBC Statement", ex3);
                    }
                    throw th;
                }
            } catch (IOException ex4) {
                throw new CannotReadScriptException(resource, ex4);
            }
        } catch (Exception ex5) {
            if (ex5 instanceof ScriptException) {
                throw ((ScriptException) ex5);
            }
            throw new UncategorizedScriptException("Failed to execute database script from resource [" + resource + "]", ex5);
        }
    }
}
