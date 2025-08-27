package org.springframework.aop.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.Ordered;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.ClassUtils;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/interceptor/AsyncExecutionInterceptor.class */
public class AsyncExecutionInterceptor extends AsyncExecutionAspectSupport implements MethodInterceptor, Ordered {
    public AsyncExecutionInterceptor(Executor defaultExecutor) {
        super(defaultExecutor);
    }

    public AsyncExecutionInterceptor(Executor defaultExecutor, AsyncUncaughtExceptionHandler exceptionHandler) {
        super(defaultExecutor, exceptionHandler);
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null;
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        AsyncTaskExecutor executor = determineAsyncExecutor(userDeclaredMethod);
        if (executor == null) {
            throw new IllegalStateException("No executor specified and no default executor set on AsyncExecutionInterceptor either");
        }
        Callable<Object> task = new Callable<Object>() { // from class: org.springframework.aop.interceptor.AsyncExecutionInterceptor.1
            @Override // java.util.concurrent.Callable
            public Object call() throws Exception {
                try {
                    Object result = invocation.proceed();
                    if (result instanceof Future) {
                        return ((Future) result).get();
                    }
                    return null;
                } catch (ExecutionException ex) {
                    AsyncExecutionInterceptor.this.handleError(ex.getCause(), userDeclaredMethod, invocation.getArguments());
                    return null;
                } catch (Throwable ex2) {
                    AsyncExecutionInterceptor.this.handleError(ex2, userDeclaredMethod, invocation.getArguments());
                    return null;
                }
            }
        };
        return doSubmit(task, executor, invocation.getMethod().getReturnType());
    }

    @Override // org.springframework.aop.interceptor.AsyncExecutionAspectSupport
    protected String getExecutorQualifier(Method method) {
        return null;
    }

    @Override // org.springframework.aop.interceptor.AsyncExecutionAspectSupport
    protected Executor getDefaultExecutor(BeanFactory beanFactory) {
        Executor defaultExecutor = super.getDefaultExecutor(beanFactory);
        return defaultExecutor != null ? defaultExecutor : new SimpleAsyncTaskExecutor();
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
