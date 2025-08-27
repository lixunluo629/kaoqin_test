package org.springframework.context.expression;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Map;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/expression/CachedExpressionEvaluator.class */
public abstract class CachedExpressionEvaluator {
    private final SpelExpressionParser parser;
    private final ParameterNameDiscoverer parameterNameDiscoverer;

    protected CachedExpressionEvaluator(SpelExpressionParser parser) {
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        Assert.notNull(parser, "SpelExpressionParser must not be null");
        this.parser = parser;
    }

    protected CachedExpressionEvaluator() {
        this(new SpelExpressionParser());
    }

    protected SpelExpressionParser getParser() {
        return this.parser;
    }

    protected ParameterNameDiscoverer getParameterNameDiscoverer() {
        return this.parameterNameDiscoverer;
    }

    protected Expression getExpression(Map<ExpressionKey, Expression> cache, AnnotatedElementKey elementKey, String expression) {
        ExpressionKey expressionKey = createKey(elementKey, expression);
        Expression expr = cache.get(expressionKey);
        if (expr == null) {
            expr = getParser().parseExpression(expression);
            cache.put(expressionKey, expr);
        }
        return expr;
    }

    private ExpressionKey createKey(AnnotatedElementKey elementKey, String expression) {
        return new ExpressionKey(elementKey, expression);
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/expression/CachedExpressionEvaluator$ExpressionKey.class */
    protected static class ExpressionKey implements Comparable<ExpressionKey> {
        private final AnnotatedElementKey element;
        private final String expression;

        protected ExpressionKey(AnnotatedElementKey element, String expression) {
            this.element = element;
            this.expression = expression;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ExpressionKey)) {
                return false;
            }
            ExpressionKey otherKey = (ExpressionKey) other;
            return this.element.equals(otherKey.element) && ObjectUtils.nullSafeEquals(this.expression, otherKey.expression);
        }

        public int hashCode() {
            return this.element.hashCode() + (this.expression != null ? this.expression.hashCode() * 29 : 0);
        }

        public String toString() {
            return this.element + (this.expression != null ? " with expression \"" + this.expression : SymbolConstants.QUOTES_SYMBOL);
        }

        @Override // java.lang.Comparable
        public int compareTo(ExpressionKey other) {
            int result = this.element.toString().compareTo(other.element.toString());
            if (result == 0 && this.expression != null) {
                result = this.expression.compareTo(other.expression);
            }
            return result;
        }
    }
}
