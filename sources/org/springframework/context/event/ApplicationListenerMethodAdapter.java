package org.springframework.context.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/ApplicationListenerMethodAdapter.class */
public class ApplicationListenerMethodAdapter implements GenericApplicationListener {
    protected final Log logger = LogFactory.getLog(getClass());
    private final String beanName;
    private final Method method;
    private final Class<?> targetClass;
    private final Method bridgedMethod;
    private final List<ResolvableType> declaredEventTypes;
    private final String condition;
    private final int order;
    private final AnnotatedElementKey methodKey;
    private ApplicationContext applicationContext;
    private EventExpressionEvaluator evaluator;

    public ApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method) {
        this.beanName = beanName;
        this.method = method;
        this.targetClass = targetClass;
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
        Method targetMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        EventListener ann = (EventListener) AnnotatedElementUtils.findMergedAnnotation(targetMethod, EventListener.class);
        this.declaredEventTypes = resolveDeclaredEventTypes(method, ann);
        this.condition = ann != null ? ann.condition() : null;
        this.order = resolveOrder(targetMethod);
        this.methodKey = new AnnotatedElementKey(method, targetClass);
    }

    private List<ResolvableType> resolveDeclaredEventTypes(Method method, EventListener ann) {
        int count = method.getParameterTypes().length;
        if (count > 1) {
            throw new IllegalStateException("Maximum one parameter is allowed for event listener method: " + method);
        }
        if (ann != null && ann.classes().length > 0) {
            List<ResolvableType> types = new ArrayList<>(ann.classes().length);
            for (Class<?> eventType : ann.classes()) {
                types.add(ResolvableType.forClass(eventType));
            }
            return types;
        }
        if (count == 0) {
            throw new IllegalStateException("Event parameter is mandatory for event listener method: " + method);
        }
        return Collections.singletonList(ResolvableType.forMethodParameter(method, 0));
    }

    private int resolveOrder(Method method) {
        Order ann = (Order) AnnotatedElementUtils.findMergedAnnotation(method, Order.class);
        if (ann != null) {
            return ann.value();
        }
        return 0;
    }

    void init(ApplicationContext applicationContext, EventExpressionEvaluator evaluator) {
        this.applicationContext = applicationContext;
        this.evaluator = evaluator;
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ApplicationEvent event) {
        processEvent(event);
    }

    @Override // org.springframework.context.event.GenericApplicationListener
    public boolean supportsEventType(ResolvableType eventType) {
        for (ResolvableType declaredEventType : this.declaredEventTypes) {
            if (declaredEventType.isAssignableFrom(eventType)) {
                return true;
            }
            if (PayloadApplicationEvent.class.isAssignableFrom(eventType.getRawClass())) {
                ResolvableType payloadType = eventType.as(PayloadApplicationEvent.class).getGeneric(new int[0]);
                if (declaredEventType.isAssignableFrom(payloadType)) {
                    return true;
                }
            }
        }
        return eventType.hasUnresolvableGenerics();
    }

    @Override // org.springframework.context.event.GenericApplicationListener
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void processEvent(ApplicationEvent event) {
        Object[] args = resolveArguments(event);
        if (shouldHandle(event, args)) {
            Object result = doInvoke(args);
            if (result != null) {
                handleResult(result);
            } else {
                this.logger.trace("No result object given - no result to handle");
            }
        }
    }

    protected Object[] resolveArguments(ApplicationEvent event) {
        ResolvableType declaredEventType = getResolvableType(event);
        if (declaredEventType == null) {
            return null;
        }
        if (this.method.getParameterTypes().length == 0) {
            return new Object[0];
        }
        Class<?> eventClass = declaredEventType.getRawClass();
        if ((eventClass == null || !ApplicationEvent.class.isAssignableFrom(eventClass)) && (event instanceof PayloadApplicationEvent)) {
            Object payload = ((PayloadApplicationEvent) event).getPayload();
            if (eventClass == null || eventClass.isInstance(payload)) {
                return new Object[]{payload};
            }
        }
        return new Object[]{event};
    }

    protected void handleResult(Object result) {
        if (result.getClass().isArray()) {
            Object[] events = ObjectUtils.toObjectArray(result);
            for (Object event : events) {
                publishEvent(event);
            }
            return;
        }
        if (result instanceof Collection) {
            for (Object event2 : (Collection) result) {
                publishEvent(event2);
            }
            return;
        }
        publishEvent(result);
    }

    private void publishEvent(Object event) {
        if (event != null) {
            Assert.notNull(this.applicationContext, "ApplicationContext must not be null");
            this.applicationContext.publishEvent(event);
        }
    }

    private boolean shouldHandle(ApplicationEvent event, Object[] args) {
        if (args == null) {
            return false;
        }
        String condition = getCondition();
        if (StringUtils.hasText(condition)) {
            Assert.notNull(this.evaluator, "EventExpressionEvaluator must no be null");
            EvaluationContext evaluationContext = this.evaluator.createEvaluationContext(event, this.targetClass, this.method, args, this.applicationContext);
            return this.evaluator.condition(condition, this.methodKey, evaluationContext);
        }
        return true;
    }

    protected Object doInvoke(Object... args) {
        Object bean = getTargetBean();
        ReflectionUtils.makeAccessible(this.bridgedMethod);
        try {
            return this.bridgedMethod.invoke(bean, args);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(getInvocationErrorMessage(bean, ex.getMessage(), args), ex);
        } catch (IllegalArgumentException ex2) {
            assertTargetBean(this.bridgedMethod, bean, args);
            throw new IllegalStateException(getInvocationErrorMessage(bean, ex2.getMessage(), args), ex2);
        } catch (InvocationTargetException ex3) {
            Throwable targetException = ex3.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            String msg = getInvocationErrorMessage(bean, "Failed to invoke event listener method", args);
            throw new UndeclaredThrowableException(targetException, msg);
        }
    }

    protected Object getTargetBean() {
        Assert.notNull(this.applicationContext, "ApplicationContext must no be null");
        return this.applicationContext.getBean(this.beanName);
    }

    protected String getCondition() {
        return this.condition;
    }

    protected String getDetailedErrorMessage(Object bean, String message) {
        StringBuilder sb = new StringBuilder(message).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("HandlerMethod details: \n");
        sb.append("Bean [").append(bean.getClass().getName()).append("]\n");
        sb.append("Method [").append(this.bridgedMethod.toGenericString()).append("]\n");
        return sb.toString();
    }

    private void assertTargetBean(Method method, Object targetBean, Object[] args) {
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        Class<?> targetBeanClass = targetBean.getClass();
        if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
            String msg = "The event listener method class '" + methodDeclaringClass.getName() + "' is not an instance of the actual bean class '" + targetBeanClass.getName() + "'. If the bean requires proxying (e.g. due to @Transactional), please use class-based proxying.";
            throw new IllegalStateException(getInvocationErrorMessage(targetBean, msg, args));
        }
    }

    private String getInvocationErrorMessage(Object bean, String message, Object[] resolvedArgs) {
        StringBuilder sb = new StringBuilder(getDetailedErrorMessage(bean, message));
        sb.append("Resolved arguments: \n");
        for (int i = 0; i < resolvedArgs.length; i++) {
            sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i).append("] ");
            if (resolvedArgs[i] == null) {
                sb.append("[null] \n");
            } else {
                sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
                sb.append("[value=").append(resolvedArgs[i]).append("]\n");
            }
        }
        return sb.toString();
    }

    private ResolvableType getResolvableType(ApplicationEvent event) {
        ResolvableType payloadType = null;
        if (event instanceof PayloadApplicationEvent) {
            PayloadApplicationEvent<?> payloadEvent = (PayloadApplicationEvent) event;
            payloadType = payloadEvent.getResolvableType().as(PayloadApplicationEvent.class).getGeneric(new int[0]);
        }
        for (ResolvableType declaredEventType : this.declaredEventTypes) {
            if (!ApplicationEvent.class.isAssignableFrom(declaredEventType.getRawClass()) && payloadType != null && declaredEventType.isAssignableFrom(payloadType)) {
                return declaredEventType;
            }
            if (declaredEventType.getRawClass().isInstance(event)) {
                return declaredEventType;
            }
        }
        return null;
    }

    public String toString() {
        return this.method.toGenericString();
    }
}
