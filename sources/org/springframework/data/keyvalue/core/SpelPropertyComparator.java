package org.springframework.data.keyvalue.core;

import java.util.Comparator;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/SpelPropertyComparator.class */
public class SpelPropertyComparator<T> implements Comparator<T> {
    private final String path;
    private final SpelExpressionParser parser;
    private boolean asc = true;
    private boolean nullsFirst = true;
    private SpelExpression expression;

    public SpelPropertyComparator(String path, SpelExpressionParser parser) {
        this.path = path;
        this.parser = parser;
    }

    public SpelPropertyComparator<T> asc() {
        this.asc = true;
        return this;
    }

    public SpelPropertyComparator<T> desc() {
        this.asc = false;
        return this;
    }

    public SpelPropertyComparator<T> nullsFirst() {
        this.nullsFirst = true;
        return this;
    }

    public SpelPropertyComparator<T> nullsLast() {
        this.nullsFirst = false;
        return this;
    }

    protected SpelExpression getExpression() {
        if (this.expression == null) {
            this.expression = this.parser.parseRaw(buildExpressionForPath());
        }
        return this.expression;
    }

    protected String buildExpressionForPath() {
        StringBuilder rawExpression = new StringBuilder("new org.springframework.util.comparator.NullSafeComparator(new org.springframework.util.comparator.ComparableComparator(), " + Boolean.toString(this.nullsFirst) + ").compare(");
        rawExpression.append("#arg1?.");
        rawExpression.append(this.path != null ? this.path.replace(".", "?.") : "");
        rawExpression.append(",");
        rawExpression.append("#arg2?.");
        rawExpression.append(this.path != null ? this.path.replace(".", "?.") : "");
        rawExpression.append(")");
        return rawExpression.toString();
    }

    @Override // java.util.Comparator
    public int compare(T arg1, T arg2) {
        SpelExpression expressionToUse = getExpression();
        expressionToUse.getEvaluationContext().setVariable("arg1", arg1);
        expressionToUse.getEvaluationContext().setVariable("arg2", arg2);
        return ((Integer) expressionToUse.getValue((Class) Integer.class)).intValue() * (this.asc ? 1 : -1);
    }

    public String getPath() {
        return this.path;
    }
}
