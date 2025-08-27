package org.apache.ibatis.jdbc;

import ch.qos.logback.classic.net.SyslogAppender;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/jdbc/ScriptRunner.class */
public class ScriptRunner {
    private static final String DEFAULT_DELIMITER = ";";
    private final Connection connection;
    private boolean stopOnError;
    private boolean throwWarning;
    private boolean autoCommit;
    private boolean sendFullScript;
    private boolean removeCRs;
    private boolean escapeProcessing = true;
    private PrintWriter logWriter = new PrintWriter(System.out);
    private PrintWriter errorLogWriter = new PrintWriter(System.err);
    private String delimiter = ";";
    private boolean fullLineDelimiter;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
    private static final Pattern DELIMITER_PATTERN = Pattern.compile("^\\s*((--)|(//))?\\s*(//)?\\s*@DELIMITER\\s+([^\\s]+)", 2);

    public ScriptRunner(Connection connection) {
        this.connection = connection;
    }

    public void setStopOnError(boolean stopOnError) {
        this.stopOnError = stopOnError;
    }

    public void setThrowWarning(boolean throwWarning) {
        this.throwWarning = throwWarning;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public void setSendFullScript(boolean sendFullScript) {
        this.sendFullScript = sendFullScript;
    }

    public void setRemoveCRs(boolean removeCRs) {
        this.removeCRs = removeCRs;
    }

    public void setEscapeProcessing(boolean escapeProcessing) {
        this.escapeProcessing = escapeProcessing;
    }

    public void setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    public void setErrorLogWriter(PrintWriter errorLogWriter) {
        this.errorLogWriter = errorLogWriter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setFullLineDelimiter(boolean fullLineDelimiter) {
        this.fullLineDelimiter = fullLineDelimiter;
    }

    public void runScript(Reader reader) {
        setAutoCommit();
        try {
            if (this.sendFullScript) {
                executeFullScript(reader);
            } else {
                executeLineByLine(reader);
            }
        } finally {
            rollbackConnection();
        }
    }

    private void executeFullScript(Reader reader) throws IOException {
        StringBuilder script = new StringBuilder();
        try {
            BufferedReader lineReader = new BufferedReader(reader);
            while (true) {
                String line = lineReader.readLine();
                if (line != null) {
                    script.append(line);
                    script.append(LINE_SEPARATOR);
                } else {
                    String command = script.toString();
                    println(command);
                    executeStatement(command);
                    commitConnection();
                    return;
                }
            }
        } catch (Exception e) {
            String message = "Error executing: " + ((Object) script) + ".  Cause: " + e;
            printlnError(message);
            throw new RuntimeSqlException(message, e);
        }
    }

    private void executeLineByLine(Reader reader) throws IOException {
        StringBuilder command = new StringBuilder();
        try {
            BufferedReader lineReader = new BufferedReader(reader);
            while (true) {
                String line = lineReader.readLine();
                if (line != null) {
                    handleLine(command, line);
                } else {
                    commitConnection();
                    checkForMissingLineTerminator(command);
                    return;
                }
            }
        } catch (Exception e) {
            String message = "Error executing: " + ((Object) command) + ".  Cause: " + e;
            printlnError(message);
            throw new RuntimeSqlException(message, e);
        }
    }

    public void closeConnection() throws SQLException {
        try {
            this.connection.close();
        } catch (Exception e) {
        }
    }

    private void setAutoCommit() {
        try {
            if (this.autoCommit != this.connection.getAutoCommit()) {
                this.connection.setAutoCommit(this.autoCommit);
            }
        } catch (Throwable t) {
            throw new RuntimeSqlException("Could not set AutoCommit to " + this.autoCommit + ". Cause: " + t, t);
        }
    }

    private void commitConnection() {
        try {
            if (!this.connection.getAutoCommit()) {
                this.connection.commit();
            }
        } catch (Throwable t) {
            throw new RuntimeSqlException("Could not commit transaction. Cause: " + t, t);
        }
    }

    private void rollbackConnection() {
        try {
            if (!this.connection.getAutoCommit()) {
                this.connection.rollback();
            }
        } catch (Throwable th) {
        }
    }

    private void checkForMissingLineTerminator(StringBuilder command) {
        if (command != null && command.toString().trim().length() > 0) {
            throw new RuntimeSqlException("Line missing end-of-line terminator (" + this.delimiter + ") => " + ((Object) command));
        }
    }

    private void handleLine(StringBuilder command, String line) throws SQLException {
        String trimmedLine = line.trim();
        if (lineIsComment(trimmedLine)) {
            Matcher matcher = DELIMITER_PATTERN.matcher(trimmedLine);
            if (matcher.find()) {
                this.delimiter = matcher.group(5);
            }
            println(trimmedLine);
            return;
        }
        if (commandReadyToExecute(trimmedLine)) {
            command.append(line.substring(0, line.lastIndexOf(this.delimiter)));
            command.append(LINE_SEPARATOR);
            println(command);
            executeStatement(command.toString());
            command.setLength(0);
            return;
        }
        if (trimmedLine.length() > 0) {
            command.append(line);
            command.append(LINE_SEPARATOR);
        }
    }

    private boolean lineIsComment(String trimmedLine) {
        return trimmedLine.startsWith("//") || trimmedLine.startsWith(ScriptUtils.DEFAULT_COMMENT_PREFIX);
    }

    private boolean commandReadyToExecute(String trimmedLine) {
        return (!this.fullLineDelimiter && trimmedLine.contains(this.delimiter)) || (this.fullLineDelimiter && trimmedLine.equals(this.delimiter));
    }

    private void executeStatement(String command) throws SQLException {
        SQLWarning warning;
        boolean hasResults = false;
        Statement statement = this.connection.createStatement();
        statement.setEscapeProcessing(this.escapeProcessing);
        String sql = command;
        if (this.removeCRs) {
            sql = sql.replaceAll("\r\n", ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (this.stopOnError) {
            hasResults = statement.execute(sql);
            if (this.throwWarning && (warning = statement.getWarnings()) != null) {
                throw warning;
            }
        } else {
            try {
                hasResults = statement.execute(sql);
            } catch (SQLException e) {
                String message = "Error executing: " + command + ".  Cause: " + e;
                printlnError(message);
            }
        }
        printResults(statement, hasResults);
        try {
            statement.close();
        } catch (Exception e2) {
        }
    }

    private void printResults(Statement statement, boolean hasResults) throws SQLException {
        if (hasResults) {
            try {
                ResultSet rs = statement.getResultSet();
                if (rs != null) {
                    ResultSetMetaData md = rs.getMetaData();
                    int cols = md.getColumnCount();
                    for (int i = 0; i < cols; i++) {
                        String name = md.getColumnLabel(i + 1);
                        print(name + SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
                    }
                    println("");
                    while (rs.next()) {
                        for (int i2 = 0; i2 < cols; i2++) {
                            String value = rs.getString(i2 + 1);
                            print(value + SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
                        }
                        println("");
                    }
                }
            } catch (SQLException e) {
                printlnError("Error printing results: " + e.getMessage());
            }
        }
    }

    private void print(Object o) {
        if (this.logWriter != null) {
            this.logWriter.print(o);
            this.logWriter.flush();
        }
    }

    private void println(Object o) {
        if (this.logWriter != null) {
            this.logWriter.println(o);
            this.logWriter.flush();
        }
    }

    private void printlnError(Object o) {
        if (this.errorLogWriter != null) {
            this.errorLogWriter.println(o);
            this.errorLogWriter.flush();
        }
    }
}
