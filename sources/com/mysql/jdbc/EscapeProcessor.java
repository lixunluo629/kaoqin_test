package com.mysql.jdbc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/EscapeProcessor.class */
class EscapeProcessor {
    private static Map<String, String> JDBC_CONVERT_TO_MYSQL_TYPE_MAP;
    private static Map<String, String> JDBC_NO_CONVERT_TO_MYSQL_EXPRESSION_MAP;

    EscapeProcessor() {
    }

    static {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("BIGINT", "0 + ?");
        tempMap.put("BINARY", "BINARY");
        tempMap.put("BIT", "0 + ?");
        tempMap.put("CHAR", "CHAR");
        tempMap.put("DATE", "DATE");
        tempMap.put("DECIMAL", "0.0 + ?");
        tempMap.put("DOUBLE", "0.0 + ?");
        tempMap.put("FLOAT", "0.0 + ?");
        tempMap.put("INTEGER", "0 + ?");
        tempMap.put("LONGVARBINARY", "BINARY");
        tempMap.put("LONGVARCHAR", "CONCAT(?)");
        tempMap.put("REAL", "0.0 + ?");
        tempMap.put("SMALLINT", "CONCAT(?)");
        tempMap.put("TIME", "TIME");
        tempMap.put("TIMESTAMP", "DATETIME");
        tempMap.put("TINYINT", "CONCAT(?)");
        tempMap.put("VARBINARY", "BINARY");
        tempMap.put("VARCHAR", "CONCAT(?)");
        JDBC_CONVERT_TO_MYSQL_TYPE_MAP = Collections.unmodifiableMap(tempMap);
        Map<String, String> tempMap2 = new HashMap<>(JDBC_CONVERT_TO_MYSQL_TYPE_MAP);
        tempMap2.put("BINARY", "CONCAT(?)");
        tempMap2.put("CHAR", "CONCAT(?)");
        tempMap2.remove("DATE");
        tempMap2.put("LONGVARBINARY", "CONCAT(?)");
        tempMap2.remove("TIME");
        tempMap2.remove("TIMESTAMP");
        tempMap2.put("VARBINARY", "CONCAT(?)");
        JDBC_NO_CONVERT_TO_MYSQL_EXPRESSION_MAP = Collections.unmodifiableMap(tempMap2);
    }

    public static final Object escapeSQL(String sql, boolean serverSupportsConvertFn, MySQLConnection conn) throws SQLException {
        String currentSql;
        String remaining;
        boolean replaceEscapeSequence = false;
        String escapeSequence = null;
        if (sql == null) {
            return null;
        }
        int beginBrace = sql.indexOf(123);
        int nextEndBrace = beginBrace == -1 ? -1 : sql.indexOf(125, beginBrace);
        if (nextEndBrace == -1) {
            return sql;
        }
        StringBuilder newSql = new StringBuilder();
        EscapeTokenizer escapeTokenizer = new EscapeTokenizer(sql);
        byte usesVariables = 0;
        boolean callingStoredFunction = false;
        while (escapeTokenizer.hasMoreTokens()) {
            String token = escapeTokenizer.nextToken();
            if (token.length() != 0) {
                if (token.charAt(0) == '{') {
                    if (!token.endsWith("}")) {
                        throw SQLError.createSQLException("Not a valid escape sequence: " + token, conn.getExceptionInterceptor());
                    }
                    if (token.length() > 2) {
                        int nestedBrace = token.indexOf(123, 2);
                        if (nestedBrace != -1) {
                            StringBuilder buf = new StringBuilder(token.substring(0, 1));
                            Object remainingResults = escapeSQL(token.substring(1, token.length() - 1), serverSupportsConvertFn, conn);
                            if (remainingResults instanceof String) {
                                remaining = (String) remainingResults;
                            } else {
                                remaining = ((EscapeProcessorResult) remainingResults).escapedSql;
                                if (usesVariables != 1) {
                                    usesVariables = ((EscapeProcessorResult) remainingResults).usesVariables;
                                }
                            }
                            buf.append(remaining);
                            buf.append('}');
                            token = buf.toString();
                        }
                    }
                    String collapsedToken = removeWhitespace(token);
                    if (StringUtils.startsWithIgnoreCase(collapsedToken, "{escape")) {
                        try {
                            StringTokenizer st = new StringTokenizer(token, " '");
                            st.nextToken();
                            escapeSequence = st.nextToken();
                            if (escapeSequence.length() < 3) {
                                newSql.append(token);
                            } else {
                                escapeSequence = escapeSequence.substring(1, escapeSequence.length() - 1);
                                replaceEscapeSequence = true;
                            }
                        } catch (NoSuchElementException e) {
                            newSql.append(token);
                        }
                    } else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{fn")) {
                        String fnToken = token.substring(token.toLowerCase().indexOf("fn ") + 3, token.length() - 1);
                        if (StringUtils.startsWithIgnoreCaseAndWs(fnToken, "convert")) {
                            newSql.append(processConvertToken(fnToken, serverSupportsConvertFn, conn));
                        } else {
                            newSql.append(fnToken);
                        }
                    } else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{d")) {
                        int startPos = token.indexOf(39) + 1;
                        int endPos = token.lastIndexOf(39);
                        if (startPos == -1 || endPos == -1) {
                            newSql.append(token);
                        } else {
                            String argument = token.substring(startPos, endPos);
                            try {
                                StringTokenizer st2 = new StringTokenizer(argument, " -");
                                String year4 = st2.nextToken();
                                String month2 = st2.nextToken();
                                String day2 = st2.nextToken();
                                String dateString = "'" + year4 + "-" + month2 + "-" + day2 + "'";
                                newSql.append(dateString);
                            } catch (NoSuchElementException e2) {
                                throw SQLError.createSQLException("Syntax error for DATE escape sequence '" + argument + "'", SQLError.SQL_STATE_SYNTAX_ERROR, conn.getExceptionInterceptor());
                            }
                        }
                    } else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{ts")) {
                        processTimestampToken(conn, newSql, token);
                    } else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{t")) {
                        processTimeToken(conn, newSql, token);
                    } else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{call") || StringUtils.startsWithIgnoreCase(collapsedToken, "{?=call")) {
                        int startPos2 = StringUtils.indexOfIgnoreCase(token, "CALL") + 5;
                        int endPos2 = token.length() - 1;
                        if (StringUtils.startsWithIgnoreCase(collapsedToken, "{?=call")) {
                            callingStoredFunction = true;
                            newSql.append("SELECT ");
                            newSql.append(token.substring(startPos2, endPos2));
                        } else {
                            callingStoredFunction = false;
                            newSql.append("CALL ");
                            newSql.append(token.substring(startPos2, endPos2));
                        }
                        int i = endPos2 - 1;
                        while (true) {
                            if (i >= startPos2) {
                                char c = token.charAt(i);
                                if (Character.isWhitespace(c)) {
                                    i--;
                                } else if (c != ')') {
                                    newSql.append("()");
                                }
                            }
                        }
                    } else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{oj")) {
                        newSql.append(token);
                    } else {
                        newSql.append(token);
                    }
                } else {
                    newSql.append(token);
                }
            }
        }
        String escapedSql = newSql.toString();
        if (replaceEscapeSequence) {
            String str = escapedSql;
            while (true) {
                currentSql = str;
                if (currentSql.indexOf(escapeSequence) == -1) {
                    break;
                }
                int escapePos = currentSql.indexOf(escapeSequence);
                String lhs = currentSql.substring(0, escapePos);
                String rhs = currentSql.substring(escapePos + 1, currentSql.length());
                str = lhs + "\\" + rhs;
            }
            escapedSql = currentSql;
        }
        EscapeProcessorResult epr = new EscapeProcessorResult();
        epr.escapedSql = escapedSql;
        epr.callingStoredFunction = callingStoredFunction;
        if (usesVariables != 1) {
            if (escapeTokenizer.sawVariableUse()) {
                epr.usesVariables = (byte) 1;
            } else {
                epr.usesVariables = (byte) 0;
            }
        }
        return epr;
    }

    private static void processTimeToken(MySQLConnection conn, StringBuilder newSql, String token) throws SQLException, NumberFormatException {
        int startPos = token.indexOf(39) + 1;
        int endPos = token.lastIndexOf(39);
        if (startPos == -1 || endPos == -1) {
            newSql.append(token);
            return;
        }
        String argument = token.substring(startPos, endPos);
        try {
            StringTokenizer st = new StringTokenizer(argument, " :.");
            String hour = st.nextToken();
            String minute = st.nextToken();
            String second = st.nextToken();
            boolean serverSupportsFractionalSecond = false;
            String fractionalSecond = "";
            if (st.hasMoreTokens() && conn.versionMeetsMinimum(5, 6, 4)) {
                serverSupportsFractionalSecond = true;
                fractionalSecond = "." + st.nextToken();
            }
            if (!conn.getUseTimezone() || !conn.getUseLegacyDatetimeCode()) {
                newSql.append("'");
                newSql.append(hour);
                newSql.append(":");
                newSql.append(minute);
                newSql.append(":");
                newSql.append(second);
                newSql.append(fractionalSecond);
                newSql.append("'");
            } else {
                Calendar sessionCalendar = conn.getCalendarInstanceForSessionOrNew();
                try {
                    int hourInt = Integer.parseInt(hour);
                    int minuteInt = Integer.parseInt(minute);
                    int secondInt = Integer.parseInt(second);
                    Time toBeAdjusted = TimeUtil.fastTimeCreate(sessionCalendar, hourInt, minuteInt, secondInt, conn.getExceptionInterceptor());
                    Time inServerTimezone = TimeUtil.changeTimezone(conn, sessionCalendar, (Calendar) null, toBeAdjusted, sessionCalendar.getTimeZone(), conn.getServerTimezoneTZ(), false);
                    newSql.append("'");
                    newSql.append(inServerTimezone.toString());
                    if (serverSupportsFractionalSecond) {
                        newSql.append(fractionalSecond);
                    }
                    newSql.append("'");
                } catch (NumberFormatException e) {
                    throw SQLError.createSQLException("Syntax error in TIMESTAMP escape sequence '" + token + "'.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, conn.getExceptionInterceptor());
                }
            }
        } catch (NoSuchElementException e2) {
            throw SQLError.createSQLException("Syntax error for escape sequence '" + argument + "'", SQLError.SQL_STATE_SYNTAX_ERROR, conn.getExceptionInterceptor());
        }
    }

    private static void processTimestampToken(MySQLConnection conn, StringBuilder newSql, String token) throws SQLException, NumberFormatException {
        int startPos = token.indexOf(39) + 1;
        int endPos = token.lastIndexOf(39);
        if (startPos == -1 || endPos == -1) {
            newSql.append(token);
            return;
        }
        String argument = token.substring(startPos, endPos);
        try {
            if (!conn.getUseLegacyDatetimeCode()) {
                Timestamp ts = TimeUtil.adjustTimestampNanosPrecision(Timestamp.valueOf(argument), 6, !conn.isServerTruncatesFracSecs());
                SimpleDateFormat tsdf = TimeUtil.getSimpleDateFormat(null, "''yyyy-MM-dd HH:mm:ss", null, conn.getServerTimezoneTZ());
                newSql.append(tsdf.format((Date) ts));
                if (ts.getNanos() > 0 && conn.versionMeetsMinimum(5, 6, 4)) {
                    newSql.append('.');
                    newSql.append(TimeUtil.formatNanos(ts.getNanos(), true, 6));
                }
                newSql.append('\'');
            } else {
                StringTokenizer st = new StringTokenizer(argument, " .-:");
                try {
                    String year4 = st.nextToken();
                    String month2 = st.nextToken();
                    String day2 = st.nextToken();
                    String hour = st.nextToken();
                    String minute = st.nextToken();
                    String second = st.nextToken();
                    boolean serverSupportsFractionalSecond = false;
                    String fractionalSecond = "";
                    if (st.hasMoreTokens() && conn.versionMeetsMinimum(5, 6, 4)) {
                        serverSupportsFractionalSecond = true;
                        fractionalSecond = "." + st.nextToken();
                    }
                    if (!conn.getUseTimezone() && !conn.getUseJDBCCompliantTimezoneShift()) {
                        newSql.append("'").append(year4).append("-").append(month2).append("-").append(day2).append(SymbolConstants.SPACE_SYMBOL).append(hour).append(":").append(minute).append(":").append(second).append(fractionalSecond).append("'");
                    } else {
                        Calendar sessionCalendar = conn.getCalendarInstanceForSessionOrNew();
                        try {
                            int year4Int = Integer.parseInt(year4);
                            int month2Int = Integer.parseInt(month2);
                            int day2Int = Integer.parseInt(day2);
                            int hourInt = Integer.parseInt(hour);
                            int minuteInt = Integer.parseInt(minute);
                            int secondInt = Integer.parseInt(second);
                            boolean useGmtMillis = conn.getUseGmtMillisForDatetimes();
                            Timestamp toBeAdjusted = TimeUtil.fastTimestampCreate(useGmtMillis, useGmtMillis ? Calendar.getInstance(TimeZone.getTimeZone("GMT")) : null, sessionCalendar, year4Int, month2Int, day2Int, hourInt, minuteInt, secondInt, 0);
                            Timestamp inServerTimezone = TimeUtil.changeTimezone(conn, sessionCalendar, (Calendar) null, toBeAdjusted, sessionCalendar.getTimeZone(), conn.getServerTimezoneTZ(), false);
                            newSql.append("'");
                            String timezoneLiteral = inServerTimezone.toString();
                            int indexOfDot = timezoneLiteral.indexOf(".");
                            if (indexOfDot != -1) {
                                timezoneLiteral = timezoneLiteral.substring(0, indexOfDot);
                            }
                            newSql.append(timezoneLiteral);
                            if (serverSupportsFractionalSecond) {
                                newSql.append(fractionalSecond);
                            }
                            newSql.append("'");
                        } catch (NumberFormatException e) {
                            throw SQLError.createSQLException("Syntax error in TIMESTAMP escape sequence '" + token + "'.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, conn.getExceptionInterceptor());
                        }
                    }
                } catch (NoSuchElementException e2) {
                    throw SQLError.createSQLException("Syntax error for TIMESTAMP escape sequence '" + argument + "'", SQLError.SQL_STATE_SYNTAX_ERROR, conn.getExceptionInterceptor());
                }
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            SQLException sqlEx = SQLError.createSQLException("Syntax error for TIMESTAMP escape sequence '" + argument + "'", SQLError.SQL_STATE_SYNTAX_ERROR, conn.getExceptionInterceptor());
            sqlEx.initCause(illegalArgumentException);
            throw sqlEx;
        }
    }

    private static String processConvertToken(String functionToken, boolean serverSupportsConvertFn, MySQLConnection conn) throws SQLException {
        String newType;
        int firstIndexOfParen = functionToken.indexOf("(");
        if (firstIndexOfParen == -1) {
            throw SQLError.createSQLException("Syntax error while processing {fn convert (... , ...)} token, missing opening parenthesis in token '" + functionToken + "'.", SQLError.SQL_STATE_SYNTAX_ERROR, conn.getExceptionInterceptor());
        }
        int indexOfComma = functionToken.lastIndexOf(",");
        if (indexOfComma == -1) {
            throw SQLError.createSQLException("Syntax error while processing {fn convert (... , ...)} token, missing comma in token '" + functionToken + "'.", SQLError.SQL_STATE_SYNTAX_ERROR, conn.getExceptionInterceptor());
        }
        int indexOfCloseParen = functionToken.indexOf(41, indexOfComma);
        if (indexOfCloseParen == -1) {
            throw SQLError.createSQLException("Syntax error while processing {fn convert (... , ...)} token, missing closing parenthesis in token '" + functionToken + "'.", SQLError.SQL_STATE_SYNTAX_ERROR, conn.getExceptionInterceptor());
        }
        String expression = functionToken.substring(firstIndexOfParen + 1, indexOfComma);
        String type = functionToken.substring(indexOfComma + 1, indexOfCloseParen);
        String trimmedType = type.trim();
        if (StringUtils.startsWithIgnoreCase(trimmedType, "SQL_")) {
            trimmedType = trimmedType.substring(4, trimmedType.length());
        }
        if (serverSupportsConvertFn) {
            newType = JDBC_CONVERT_TO_MYSQL_TYPE_MAP.get(trimmedType.toUpperCase(Locale.ENGLISH));
        } else {
            newType = JDBC_NO_CONVERT_TO_MYSQL_EXPRESSION_MAP.get(trimmedType.toUpperCase(Locale.ENGLISH));
            if (newType == null) {
                throw SQLError.createSQLException("Can't find conversion re-write for type '" + type + "' that is applicable for this server version while processing escape tokens.", SQLError.SQL_STATE_GENERAL_ERROR, conn.getExceptionInterceptor());
            }
        }
        if (newType == null) {
            throw SQLError.createSQLException("Unsupported conversion type '" + type.trim() + "' found while processing escape token.", SQLError.SQL_STATE_GENERAL_ERROR, conn.getExceptionInterceptor());
        }
        int replaceIndex = newType.indexOf("?");
        if (replaceIndex != -1) {
            return newType.substring(0, replaceIndex) + expression + newType.substring(replaceIndex + 1, newType.length());
        }
        return "CAST(" + expression + " AS " + newType + ")";
    }

    private static String removeWhitespace(String toCollapse) {
        if (toCollapse == null) {
            return null;
        }
        int length = toCollapse.length();
        StringBuilder collapsed = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = toCollapse.charAt(i);
            if (!Character.isWhitespace(c)) {
                collapsed.append(c);
            }
        }
        return collapsed.toString();
    }
}
