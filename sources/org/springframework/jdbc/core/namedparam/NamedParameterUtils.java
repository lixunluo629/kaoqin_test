package org.springframework.jdbc.core.namedparam;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/NamedParameterUtils.class */
public abstract class NamedParameterUtils {
    private static final String[] START_SKIP = {"'", SymbolConstants.QUOTES_SYMBOL, ScriptUtils.DEFAULT_COMMENT_PREFIX, ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER};
    private static final String[] STOP_SKIP = {"'", SymbolConstants.QUOTES_SYMBOL, ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "*/"};
    private static final char[] PARAMETER_SEPARATORS = {'\"', '\'', ':', '&', ',', ';', '(', ')', '|', '=', '+', '-', '*', '%', '/', '\\', '<', '>', '^'};

    public static ParsedSql parseSqlStatement(String sql) {
        int j;
        int skipToPosition;
        Assert.notNull(sql, "SQL must not be null");
        Set<String> namedParameters = new HashSet<>();
        String sqlToUse = sql;
        List<ParameterHolder> parameterList = new ArrayList<>();
        char[] statement = sql.toCharArray();
        int namedParameterCount = 0;
        int unnamedParameterCount = 0;
        int totalParameterCount = 0;
        int escapes = 0;
        int i = 0;
        while (i < statement.length) {
            while (i < statement.length && i != (skipToPosition = skipCommentsAndQuotes(statement, i))) {
                i = skipToPosition;
            }
            if (i >= statement.length) {
                break;
            }
            char c = statement[i];
            if (c == ':' || c == '&') {
                int j2 = i + 1;
                if (c == ':' && j2 < statement.length && statement[j2] == ':') {
                    i += 2;
                } else {
                    if (c == ':' && j2 < statement.length && statement[j2] == '{') {
                        while (statement[j2] != '}') {
                            j2++;
                            if (j2 >= statement.length) {
                                throw new InvalidDataAccessApiUsageException("Non-terminated named parameter declaration at position " + i + " in statement: " + sql);
                            }
                            if (statement[j2] == ':' || statement[j2] == '{') {
                                throw new InvalidDataAccessApiUsageException("Parameter name contains invalid character '" + statement[j2] + "' at position " + i + " in statement: " + sql);
                            }
                        }
                        if (j2 - i > 2) {
                            String parameter = sql.substring(i + 2, j2);
                            namedParameterCount = addNewNamedParameter(namedParameters, namedParameterCount, parameter);
                            totalParameterCount = addNamedParameter(parameterList, totalParameterCount, escapes, i, j2 + 1, parameter);
                        }
                        j2++;
                    } else {
                        while (j2 < statement.length && !isParameterSeparator(statement[j2])) {
                            j2++;
                        }
                        if (j2 - i > 1) {
                            String parameter2 = sql.substring(i + 1, j2);
                            namedParameterCount = addNewNamedParameter(namedParameters, namedParameterCount, parameter2);
                            totalParameterCount = addNamedParameter(parameterList, totalParameterCount, escapes, i, j2, parameter2);
                        }
                    }
                    i = j2 - 1;
                    i++;
                }
            } else if (c == '\\' && (j = i + 1) < statement.length && statement[j] == ':') {
                sqlToUse = sqlToUse.substring(0, i - escapes) + sqlToUse.substring((i - escapes) + 1);
                escapes++;
                i += 2;
            } else {
                if (c == '?') {
                    int j3 = i + 1;
                    if (j3 < statement.length && (statement[j3] == '?' || statement[j3] == '|' || statement[j3] == '&')) {
                        i += 2;
                    } else {
                        unnamedParameterCount++;
                        totalParameterCount++;
                    }
                }
                i++;
            }
        }
        ParsedSql parsedSql = new ParsedSql(sqlToUse);
        for (ParameterHolder ph : parameterList) {
            parsedSql.addNamedParameter(ph.getParameterName(), ph.getStartIndex(), ph.getEndIndex());
        }
        parsedSql.setNamedParameterCount(namedParameterCount);
        parsedSql.setUnnamedParameterCount(unnamedParameterCount);
        parsedSql.setTotalParameterCount(totalParameterCount);
        return parsedSql;
    }

    private static int addNamedParameter(List<ParameterHolder> parameterList, int totalParameterCount, int escapes, int i, int j, String parameter) {
        parameterList.add(new ParameterHolder(parameter, i - escapes, j - escapes));
        return totalParameterCount + 1;
    }

    private static int addNewNamedParameter(Set<String> namedParameters, int namedParameterCount, String parameter) {
        if (!namedParameters.contains(parameter)) {
            namedParameters.add(parameter);
            namedParameterCount++;
        }
        return namedParameterCount;
    }

    private static int skipCommentsAndQuotes(char[] statement, int position) {
        for (int i = 0; i < START_SKIP.length; i++) {
            if (statement[position] == START_SKIP[i].charAt(0)) {
                boolean match = true;
                int j = 1;
                while (true) {
                    if (j >= START_SKIP[i].length()) {
                        break;
                    }
                    if (statement[position + j] == START_SKIP[i].charAt(j)) {
                        j++;
                    } else {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    int offset = START_SKIP[i].length();
                    for (int m = position + offset; m < statement.length; m++) {
                        if (statement[m] == STOP_SKIP[i].charAt(0)) {
                            boolean endMatch = true;
                            int endPos = m;
                            int n = 1;
                            while (true) {
                                if (n >= STOP_SKIP[i].length()) {
                                    break;
                                }
                                if (m + n >= statement.length) {
                                    return statement.length;
                                }
                                if (statement[m + n] != STOP_SKIP[i].charAt(n)) {
                                    endMatch = false;
                                    break;
                                }
                                endPos = m + n;
                                n++;
                            }
                            if (endMatch) {
                                return endPos + 1;
                            }
                        }
                    }
                    return statement.length;
                }
            }
        }
        return position;
    }

    public static String substituteNamedParameters(ParsedSql parsedSql, SqlParameterSource paramSource) {
        String originalSql = parsedSql.getOriginalSql();
        List<String> paramNames = parsedSql.getParameterNames();
        if (paramNames.isEmpty()) {
            return originalSql;
        }
        StringBuilder actualSql = new StringBuilder(originalSql.length());
        int lastIndex = 0;
        for (int i = 0; i < paramNames.size(); i++) {
            String paramName = paramNames.get(i);
            int[] indexes = parsedSql.getParameterIndexes(i);
            int startIndex = indexes[0];
            int endIndex = indexes[1];
            actualSql.append((CharSequence) originalSql, lastIndex, startIndex);
            if (paramSource != null && paramSource.hasValue(paramName)) {
                Object value = paramSource.getValue(paramName);
                if (value instanceof SqlParameterValue) {
                    value = ((SqlParameterValue) value).getValue();
                }
                if (value instanceof Collection) {
                    int k = 0;
                    for (Object entryItem : (Collection) value) {
                        if (k > 0) {
                            actualSql.append(", ");
                        }
                        k++;
                        if (entryItem instanceof Object[]) {
                            Object[] expressionList = (Object[]) entryItem;
                            actualSql.append('(');
                            for (int m = 0; m < expressionList.length; m++) {
                                if (m > 0) {
                                    actualSql.append(", ");
                                }
                                actualSql.append('?');
                            }
                            actualSql.append(')');
                        } else {
                            actualSql.append('?');
                        }
                    }
                } else {
                    actualSql.append('?');
                }
            } else {
                actualSql.append('?');
            }
            lastIndex = endIndex;
        }
        actualSql.append((CharSequence) originalSql, lastIndex, originalSql.length());
        return actualSql.toString();
    }

    public static Object[] buildValueArray(ParsedSql parsedSql, SqlParameterSource paramSource, List<SqlParameter> declaredParams) {
        Object[] paramArray = new Object[parsedSql.getTotalParameterCount()];
        if (parsedSql.getNamedParameterCount() > 0 && parsedSql.getUnnamedParameterCount() > 0) {
            throw new InvalidDataAccessApiUsageException("Not allowed to mix named and traditional ? placeholders. You have " + parsedSql.getNamedParameterCount() + " named parameter(s) and " + parsedSql.getUnnamedParameterCount() + " traditional placeholder(s) in statement: " + parsedSql.getOriginalSql());
        }
        List<String> paramNames = parsedSql.getParameterNames();
        for (int i = 0; i < paramNames.size(); i++) {
            String paramName = paramNames.get(i);
            try {
                Object value = paramSource.getValue(paramName);
                SqlParameter param = findParameter(declaredParams, paramName, i);
                paramArray[i] = param != null ? new SqlParameterValue(param, value) : value;
            } catch (IllegalArgumentException ex) {
                throw new InvalidDataAccessApiUsageException("No value supplied for the SQL parameter '" + paramName + "': " + ex.getMessage());
            }
        }
        return paramArray;
    }

    private static SqlParameter findParameter(List<SqlParameter> declaredParams, String paramName, int paramIndex) {
        if (declaredParams != null) {
            for (SqlParameter declaredParam : declaredParams) {
                if (paramName.equals(declaredParam.getName())) {
                    return declaredParam;
                }
            }
            if (paramIndex < declaredParams.size()) {
                SqlParameter declaredParam2 = declaredParams.get(paramIndex);
                if (declaredParam2.getName() == null) {
                    return declaredParam2;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private static boolean isParameterSeparator(char c) {
        if (Character.isWhitespace(c)) {
            return true;
        }
        for (char separator : PARAMETER_SEPARATORS) {
            if (c == separator) {
                return true;
            }
        }
        return false;
    }

    public static int[] buildSqlTypeArray(ParsedSql parsedSql, SqlParameterSource paramSource) {
        int[] sqlTypes = new int[parsedSql.getTotalParameterCount()];
        List<String> paramNames = parsedSql.getParameterNames();
        for (int i = 0; i < paramNames.size(); i++) {
            String paramName = paramNames.get(i);
            sqlTypes[i] = paramSource.getSqlType(paramName);
        }
        return sqlTypes;
    }

    public static List<SqlParameter> buildSqlParameterList(ParsedSql parsedSql, SqlParameterSource paramSource) {
        List<String> paramNames = parsedSql.getParameterNames();
        List<SqlParameter> params = new ArrayList<>(paramNames.size());
        for (String paramName : paramNames) {
            params.add(new SqlParameter(paramName, paramSource.getSqlType(paramName), paramSource.getTypeName(paramName)));
        }
        return params;
    }

    public static String parseSqlStatementIntoString(String sql) {
        ParsedSql parsedSql = parseSqlStatement(sql);
        return substituteNamedParameters(parsedSql, (SqlParameterSource) null);
    }

    public static String substituteNamedParameters(String sql, SqlParameterSource paramSource) {
        ParsedSql parsedSql = parseSqlStatement(sql);
        return substituteNamedParameters(parsedSql, paramSource);
    }

    public static Object[] buildValueArray(String sql, Map<String, ?> paramMap) {
        ParsedSql parsedSql = parseSqlStatement(sql);
        return buildValueArray(parsedSql, new MapSqlParameterSource(paramMap), null);
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/NamedParameterUtils$ParameterHolder.class */
    private static class ParameterHolder {
        private final String parameterName;
        private final int startIndex;
        private final int endIndex;

        public ParameterHolder(String parameterName, int startIndex, int endIndex) {
            this.parameterName = parameterName;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public String getParameterName() {
            return this.parameterName;
        }

        public int getStartIndex() {
            return this.startIndex;
        }

        public int getEndIndex() {
            return this.endIndex;
        }
    }
}
