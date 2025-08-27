package org.springframework.web.method.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.springframework.beans.PropertyAccessor;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/support/InvocableHandlerMethod.class */
public class InvocableHandlerMethod extends HandlerMethod {
    private WebDataBinderFactory dataBinderFactory;
    private HandlerMethodArgumentResolverComposite argumentResolvers;
    private ParameterNameDiscoverer parameterNameDiscoverer;

    public InvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
        this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    public InvocableHandlerMethod(Object bean, Method method) {
        super(bean, method);
        this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    public InvocableHandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        super(bean, methodName, parameterTypes);
        this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory) {
        this.dataBinderFactory = dataBinderFactory;
    }

    public void setHandlerMethodArgumentResolvers(HandlerMethodArgumentResolverComposite argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    public Object invokeForRequest(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
        Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Invoking '" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) + "' with arguments " + Arrays.toString(args));
        }
        Object returnValue = doInvoke(args);
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Method [" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) + "] returned [" + returnValue + "]");
        }
        return returnValue;
    }

    private Object[] getMethodArgumentValues(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
        MethodParameter[] parameters = getMethodParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];
            parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            args[i] = resolveProvidedArgument(parameter, providedArgs);
            if (args[i] == null) {
                if (this.argumentResolvers.supportsParameter(parameter)) {
                    try {
                        args[i] = this.argumentResolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
                    } catch (Exception ex) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug(getArgumentResolutionErrorMessage("Failed to resolve", i), ex);
                        }
                        throw ex;
                    }
                } else if (args[i] == null) {
                    throw new IllegalStateException("Could not resolve method parameter at index " + parameter.getParameterIndex() + " in " + parameter.getMethod().toGenericString() + ": " + getArgumentResolutionErrorMessage("No suitable resolver for", i));
                }
            }
        }
        return args;
    }

    private String getArgumentResolutionErrorMessage(String text, int index) {
        Class<?> paramType = getMethodParameters()[index].getParameterType();
        return text + " argument " + index + " of type '" + paramType.getName() + "'";
    }

    private Object resolveProvidedArgument(MethodParameter parameter, Object... providedArgs) {
        if (providedArgs == null) {
            return null;
        }
        for (Object providedArg : providedArgs) {
            if (parameter.getParameterType().isInstance(providedArg)) {
                return providedArg;
            }
        }
        return null;
    }

    protected Object doInvoke(Object... args) throws Exception {
        ReflectionUtils.makeAccessible(getBridgedMethod());
        try {
            return getBridgedMethod().invoke(getBean(), args);
        } catch (IllegalArgumentException ex) {
            assertTargetBean(getBridgedMethod(), getBean(), args);
            String text = ex.getMessage() != null ? ex.getMessage() : "Illegal argument";
            throw new IllegalStateException(getInvocationErrorMessage(text, args), ex);
        } catch (InvocationTargetException ex2) {
            Throwable targetException = ex2.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            if (targetException instanceof Error) {
                throw ((Error) targetException);
            }
            if (targetException instanceof Exception) {
                throw ((Exception) targetException);
            }
            String text2 = getInvocationErrorMessage("Failed to invoke handler method", args);
            throw new IllegalStateException(text2, targetException);
        }
    }

    private void assertTargetBean(Method method, Object targetBean, Object[] args) {
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        Class<?> targetBeanClass = targetBean.getClass();
        if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
            String text = "The mapped handler method class '" + methodDeclaringClass.getName() + "' is not an instance of the actual controller bean class '" + targetBeanClass.getName() + "'. If the controller requires proxying (e.g. due to @Transactional), please use class-based proxying.";
            throw new IllegalStateException(getInvocationErrorMessage(text, args));
        }
    }

    private String getInvocationErrorMessage(String text, Object[] resolvedArgs) {
        StringBuilder sb = new StringBuilder(getDetailedErrorMessage(text));
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

    protected String getDetailedErrorMessage(String text) {
        StringBuilder sb = new StringBuilder(text).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("HandlerMethod details: \n");
        sb.append("Controller [").append(getBeanType().getName()).append("]\n");
        sb.append("Method [").append(getBridgedMethod().toGenericString()).append("]\n");
        return sb.toString();
    }
}
