package org.springframework.context.event;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/EventExpressionEvaluator.class */
class EventExpressionEvaluator extends CachedExpressionEvaluator {
    private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> conditionCache = new ConcurrentHashMap(64);
    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap(64);

    EventExpressionEvaluator() {
    }

    public EvaluationContext createEvaluationContext(ApplicationEvent event, Class<?> targetClass, Method method, Object[] args, BeanFactory beanFactory) {
        Method targetMethod = getTargetMethod(targetClass, method);
        EventExpressionRootObject root = new EventExpressionRootObject(event, args);
        MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(root, targetMethod, args, getParameterNameDiscoverer());
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }

    public boolean condition(String conditionExpression, AnnotatedElementKey elementKey, EvaluationContext evalContext) {
        return ((Boolean) getExpression(this.conditionCache, elementKey, conditionExpression).getValue(evalContext, Boolean.TYPE)).booleanValue();
    }

    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }
}
