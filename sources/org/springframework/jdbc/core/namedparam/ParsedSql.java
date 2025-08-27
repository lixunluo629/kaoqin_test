package org.springframework.jdbc.core.namedparam;

import java.util.ArrayList;
import java.util.List;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/ParsedSql.class */
public class ParsedSql {
    private String originalSql;
    private List<String> parameterNames = new ArrayList();
    private List<int[]> parameterIndexes = new ArrayList();
    private int namedParameterCount;
    private int unnamedParameterCount;
    private int totalParameterCount;

    ParsedSql(String originalSql) {
        this.originalSql = originalSql;
    }

    String getOriginalSql() {
        return this.originalSql;
    }

    void addNamedParameter(String parameterName, int startIndex, int endIndex) {
        this.parameterNames.add(parameterName);
        this.parameterIndexes.add(new int[]{startIndex, endIndex});
    }

    List<String> getParameterNames() {
        return this.parameterNames;
    }

    int[] getParameterIndexes(int parameterPosition) {
        return this.parameterIndexes.get(parameterPosition);
    }

    void setNamedParameterCount(int namedParameterCount) {
        this.namedParameterCount = namedParameterCount;
    }

    int getNamedParameterCount() {
        return this.namedParameterCount;
    }

    void setUnnamedParameterCount(int unnamedParameterCount) {
        this.unnamedParameterCount = unnamedParameterCount;
    }

    int getUnnamedParameterCount() {
        return this.unnamedParameterCount;
    }

    void setTotalParameterCount(int totalParameterCount) {
        this.totalParameterCount = totalParameterCount;
    }

    int getTotalParameterCount() {
        return this.totalParameterCount;
    }

    public String toString() {
        return this.originalSql;
    }
}
