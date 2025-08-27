package org.springframework.data.projection;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/SpelEvaluatingMethodInterceptor.class */
class SpelEvaluatingMethodInterceptor implements MethodInterceptor {
    private static final ParserContext PARSER_CONTEXT = new TemplateParserContext();
    private final EvaluationContext evaluationContext;
    private final MethodInterceptor delegate;
    private final Map<Integer, Expression> expressions;

    public SpelEvaluatingMethodInterceptor(MethodInterceptor delegate, Object target, BeanFactory beanFactory, SpelExpressionParser parser, Class<?> targetInterface) {
        Assert.notNull(delegate, "Delegate MethodInterceptor must not be null!");
        Assert.notNull(target, "Target object must not be null!");
        Assert.notNull(parser, "SpelExpressionParser must not be null!");
        Assert.notNull(targetInterface, "Target interface must not be null!");
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(new TargetWrapper(target));
        if (target instanceof Map) {
            evaluationContext.addPropertyAccessor(new MapAccessor());
        }
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        this.expressions = potentiallyCreateExpressionsForMethodsOnTargetInterface(parser, targetInterface);
        this.evaluationContext = evaluationContext;
        this.delegate = delegate;
    }

    private static Map<Integer, Expression> potentiallyCreateExpressionsForMethodsOnTargetInterface(SpelExpressionParser parser, Class<?> targetInterface) throws SecurityException {
        Map<Integer, Expression> expressions = new HashMap<>();
        for (Method method : targetInterface.getMethods()) {
            if (method.isAnnotationPresent(Value.class)) {
                Value value = (Value) method.getAnnotation(Value.class);
                if (!StringUtils.hasText(value.value())) {
                    throw new IllegalStateException(String.format("@Value annotation on %s contains empty expression!", method));
                }
                expressions.put(Integer.valueOf(method.hashCode()), parser.parseExpression(value.value(), PARSER_CONTEXT));
            }
        }
        return Collections.unmodifiableMap(expressions);
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Expression expression = this.expressions.get(Integer.valueOf(invocation.getMethod().hashCode()));
        if (expression == null) {
            return this.delegate.invoke(invocation);
        }
        return expression.getValue(this.evaluationContext);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/SpelEvaluatingMethodInterceptor$TargetWrapper.class */
    static class TargetWrapper {
        private final Object target;

        public TargetWrapper(Object target) {
            this.target = target;
        }

        public Object getTarget() {
            return this.target;
        }
    }
}
