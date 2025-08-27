package org.springframework.data.redis.listener.adapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/adapter/MessageListenerAdapter.class */
public class MessageListenerAdapter implements InitializingBean, MessageListener {
    public static final String ORIGINAL_DEFAULT_LISTENER_METHOD = "handleMessage";
    protected final Log logger;
    private volatile Object delegate;
    private volatile MethodInvoker invoker;
    private String defaultListenerMethod;
    private RedisSerializer<?> serializer;
    private RedisSerializer<String> stringSerializer;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/adapter/MessageListenerAdapter$MethodInvoker.class */
    private class MethodInvoker {
        private final Object delegate;
        private String methodName;
        private Set<Method> methods = new HashSet();
        private boolean lenient;

        MethodInvoker(Object delegate, String methodName) throws SecurityException, IllegalArgumentException {
            this.delegate = delegate;
            this.methodName = methodName;
            this.lenient = delegate instanceof MessageListener;
            Class<?> c = delegate.getClass();
            ReflectionUtils.doWithMethods(c, new ReflectionUtils.MethodCallback() { // from class: org.springframework.data.redis.listener.adapter.MessageListenerAdapter.MethodInvoker.1
                @Override // org.springframework.util.ReflectionUtils.MethodCallback
                public void doWith(Method method) throws IllegalAccessException, IllegalArgumentException {
                    ReflectionUtils.makeAccessible(method);
                    MethodInvoker.this.methods.add(method);
                }
            }, new MostSpecificMethodFilter(methodName, c));
            Assert.isTrue(this.lenient || !this.methods.isEmpty(), "Cannot find a suitable method named [" + c.getName() + "#" + methodName + "] - is the method public and has the proper arguments?");
        }

        void invoke(Object[] arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Object[] message = {arguments[0]};
            for (Method m : this.methods) {
                Class<?>[] types = m.getParameterTypes();
                Object[] args = (types.length == 2 && types[0].isInstance(arguments[0]) && types[1].isInstance(arguments[1])) ? arguments : message;
                if (types[0].isInstance(args[0])) {
                    m.invoke(this.delegate, args);
                    return;
                }
            }
        }

        public String getMethodName() {
            return this.methodName;
        }
    }

    public MessageListenerAdapter() {
        this.logger = LogFactory.getLog(getClass());
        this.defaultListenerMethod = ORIGINAL_DEFAULT_LISTENER_METHOD;
        initDefaultStrategies();
        this.delegate = this;
    }

    public MessageListenerAdapter(Object delegate) {
        this.logger = LogFactory.getLog(getClass());
        this.defaultListenerMethod = ORIGINAL_DEFAULT_LISTENER_METHOD;
        initDefaultStrategies();
        setDelegate(delegate);
    }

    public MessageListenerAdapter(Object delegate, String defaultListenerMethod) {
        this(delegate);
        setDefaultListenerMethod(defaultListenerMethod);
    }

    public void setDelegate(Object delegate) {
        Assert.notNull(delegate, "Delegate must not be null");
        this.delegate = delegate;
    }

    public Object getDelegate() {
        return this.delegate;
    }

    public void setDefaultListenerMethod(String defaultListenerMethod) {
        this.defaultListenerMethod = defaultListenerMethod;
    }

    protected String getDefaultListenerMethod() {
        return this.defaultListenerMethod;
    }

    public void setSerializer(RedisSerializer<?> serializer) {
        this.serializer = serializer;
    }

    public void setStringSerializer(RedisSerializer<String> serializer) {
        this.stringSerializer = serializer;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        String methodName = getDefaultListenerMethod();
        if (!StringUtils.hasText(methodName)) {
            throw new InvalidDataAccessApiUsageException("No default listener method specified: Either specify a non-null value for the 'defaultListenerMethod' property or override the 'getListenerMethodName' method.");
        }
        this.invoker = new MethodInvoker(this.delegate, methodName);
    }

    @Override // org.springframework.data.redis.connection.MessageListener
    public void onMessage(Message message, byte[] pattern) {
        try {
            if (this.delegate != this && (this.delegate instanceof MessageListener)) {
                ((MessageListener) this.delegate).onMessage(message, pattern);
                return;
            }
            Object convertedMessage = extractMessage(message);
            String convertedChannel = this.stringSerializer.deserialize(pattern);
            Object[] listenerArguments = {convertedMessage, convertedChannel};
            invokeListenerMethod(this.invoker.getMethodName(), listenerArguments);
        } catch (Throwable th) {
            handleListenerException(th);
        }
    }

    protected void initDefaultStrategies() {
        RedisSerializer<String> serializer = new StringRedisSerializer();
        setSerializer(serializer);
        setStringSerializer(serializer);
    }

    protected void handleListenerException(Throwable ex) {
        this.logger.error("Listener execution failed", ex);
    }

    protected Object extractMessage(Message message) {
        if (this.serializer != null) {
            return this.serializer.deserialize(message.getBody());
        }
        return message.getBody();
    }

    protected String getListenerMethodName(Message originalMessage, Object extractedMessage) {
        return getDefaultListenerMethod();
    }

    protected void invokeListenerMethod(String methodName, Object[] arguments) {
        try {
            this.invoker.invoke(arguments);
        } catch (InvocationTargetException ex) {
            Throwable targetEx = ex.getTargetException();
            if (targetEx instanceof DataAccessException) {
                throw ((DataAccessException) targetEx);
            }
            throw new RedisListenerExecutionFailedException("Listener method '" + methodName + "' threw exception", targetEx);
        } catch (Throwable ex2) {
            throw new RedisListenerExecutionFailedException("Failed to invoke target method '" + methodName + "' with arguments " + ObjectUtils.nullSafeToString(arguments), ex2);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/adapter/MessageListenerAdapter$MostSpecificMethodFilter.class */
    static final class MostSpecificMethodFilter implements ReflectionUtils.MethodFilter {
        private final String methodName;
        private final Class<?> c;

        MostSpecificMethodFilter(String methodName, Class<?> c) {
            this.methodName = methodName;
            this.c = c;
        }

        @Override // org.springframework.util.ReflectionUtils.MethodFilter
        public boolean matches(Method method) {
            if (Modifier.isPublic(method.getModifiers()) && this.methodName.equals(method.getName()) && method.equals(ClassUtils.getMostSpecificMethod(method, this.c))) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                return (parameterTypes.length == 2 && String.class.equals(parameterTypes[1])) || parameterTypes.length == 1;
            }
            return false;
        }
    }
}
