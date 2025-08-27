package org.springframework.web.servlet.mvc.multiaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.LastModified;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/multiaction/MultiActionController.class */
public class MultiActionController extends AbstractController implements LastModified {
    public static final String LAST_MODIFIED_METHOD_SUFFIX = "LastModified";
    public static final String DEFAULT_COMMAND_NAME = "command";
    public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
    protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
    private Object delegate;
    private MethodNameResolver methodNameResolver;
    private Validator[] validators;
    private WebBindingInitializer webBindingInitializer;
    private final Map<String, Method> handlerMethodMap;
    private final Map<String, Method> lastModifiedMethodMap;
    private final Map<Class<?>, Method> exceptionHandlerMap;

    public MultiActionController() throws NoSuchMethodException, SecurityException {
        this.methodNameResolver = new InternalPathMethodNameResolver();
        this.handlerMethodMap = new HashMap();
        this.lastModifiedMethodMap = new HashMap();
        this.exceptionHandlerMap = new HashMap();
        this.delegate = this;
        registerHandlerMethods(this.delegate);
    }

    public MultiActionController(Object delegate) throws NoSuchMethodException, SecurityException {
        this.methodNameResolver = new InternalPathMethodNameResolver();
        this.handlerMethodMap = new HashMap();
        this.lastModifiedMethodMap = new HashMap();
        this.exceptionHandlerMap = new HashMap();
        setDelegate(delegate);
    }

    public final void setDelegate(Object delegate) throws NoSuchMethodException, SecurityException {
        Assert.notNull(delegate, "Delegate must not be null");
        this.delegate = delegate;
        registerHandlerMethods(this.delegate);
        if (this.handlerMethodMap.isEmpty()) {
            throw new IllegalStateException("No handler methods in class [" + this.delegate.getClass() + "]");
        }
    }

    public final void setMethodNameResolver(MethodNameResolver methodNameResolver) {
        this.methodNameResolver = methodNameResolver;
    }

    public final MethodNameResolver getMethodNameResolver() {
        return this.methodNameResolver;
    }

    public final void setValidators(Validator[] validators) {
        this.validators = validators;
    }

    public final Validator[] getValidators() {
        return this.validators;
    }

    public final void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }

    public final WebBindingInitializer getWebBindingInitializer() {
        return this.webBindingInitializer;
    }

    private void registerHandlerMethods(Object delegate) throws NoSuchMethodException, SecurityException {
        this.handlerMethodMap.clear();
        this.lastModifiedMethodMap.clear();
        this.exceptionHandlerMap.clear();
        Method[] methods = delegate.getClass().getMethods();
        for (Method method : methods) {
            if (isExceptionHandlerMethod(method)) {
                registerExceptionHandlerMethod(method);
            } else if (isHandlerMethod(method)) {
                registerHandlerMethod(method);
                registerLastModifiedMethodIfExists(delegate, method);
            }
        }
    }

    private boolean isHandlerMethod(Method method) {
        Class<?> returnType = method.getReturnType();
        if (ModelAndView.class == returnType || Map.class == returnType || String.class == returnType || Void.TYPE == returnType) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            return parameterTypes.length >= 2 && HttpServletRequest.class == parameterTypes[0] && HttpServletResponse.class == parameterTypes[1] && !("handleRequest".equals(method.getName()) && parameterTypes.length == 2);
        }
        return false;
    }

    private boolean isExceptionHandlerMethod(Method method) {
        return isHandlerMethod(method) && method.getParameterTypes().length == 3 && Throwable.class.isAssignableFrom(method.getParameterTypes()[2]);
    }

    private void registerHandlerMethod(Method method) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Found action method [" + method + "]");
        }
        this.handlerMethodMap.put(method.getName(), method);
    }

    private void registerLastModifiedMethodIfExists(Object delegate, Method method) throws NoSuchMethodException, SecurityException {
        try {
            Method lastModifiedMethod = delegate.getClass().getMethod(method.getName() + LAST_MODIFIED_METHOD_SUFFIX, HttpServletRequest.class);
            Class<?> returnType = lastModifiedMethod.getReturnType();
            if (Long.TYPE != returnType && Long.class != returnType) {
                throw new IllegalStateException("last-modified method [" + lastModifiedMethod + "] declares an invalid return type - needs to be 'long' or 'Long'");
            }
            this.lastModifiedMethodMap.put(method.getName(), lastModifiedMethod);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Found last-modified method for handler method [" + method + "]");
            }
        } catch (NoSuchMethodException e) {
        }
    }

    private void registerExceptionHandlerMethod(Method method) {
        this.exceptionHandlerMap.put(method.getParameterTypes()[2], method);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Found exception handler method [" + method + "]");
        }
    }

    @Override // org.springframework.web.servlet.mvc.LastModified
    public long getLastModified(HttpServletRequest request) {
        try {
            String handlerMethodName = this.methodNameResolver.getHandlerMethodName(request);
            Method lastModifiedMethod = this.lastModifiedMethodMap.get(handlerMethodName);
            if (lastModifiedMethod != null) {
                try {
                    Long wrappedLong = (Long) lastModifiedMethod.invoke(this.delegate, request);
                    if (wrappedLong != null) {
                        return wrappedLong.longValue();
                    }
                    return -1L;
                } catch (Exception ex) {
                    this.logger.error("Failed to invoke last-modified method", ex);
                }
            }
            return -1L;
        } catch (NoSuchRequestHandlingMethodException e) {
            return -1L;
        }
    }

    @Override // org.springframework.web.servlet.mvc.AbstractController
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String methodName = this.methodNameResolver.getHandlerMethodName(request);
            return invokeNamedMethod(methodName, request, response);
        } catch (NoSuchRequestHandlingMethodException ex) {
            return handleNoSuchRequestHandlingMethod(ex, request, response);
        }
    }

    protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        pageNotFoundLogger.warn(ex.getMessage());
        response.sendError(404);
        return null;
    }

    protected final ModelAndView invokeNamedMethod(String methodName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method method = this.handlerMethodMap.get(methodName);
        if (method == null) {
            throw new NoSuchRequestHandlingMethodException(methodName, getClass());
        }
        try {
            Class<?>[] paramTypes = method.getParameterTypes();
            List<Object> params = new ArrayList<>(4);
            params.add(request);
            params.add(response);
            if (paramTypes.length >= 3 && HttpSession.class == paramTypes[2]) {
                HttpSession session = request.getSession(false);
                if (session == null) {
                    throw new HttpSessionRequiredException("Pre-existing session required for handler method '" + methodName + "'");
                }
                params.add(session);
            }
            if (paramTypes.length >= 3 && HttpSession.class != paramTypes[paramTypes.length - 1]) {
                Object command = newCommandObject(paramTypes[paramTypes.length - 1]);
                params.add(command);
                bind(request, command);
            }
            Object returnValue = method.invoke(this.delegate, params.toArray(new Object[params.size()]));
            return massageReturnValueIfNecessary(returnValue);
        } catch (InvocationTargetException ex) {
            return handleException(request, response, ex.getTargetException());
        } catch (Exception ex2) {
            return handleException(request, response, ex2);
        }
    }

    private ModelAndView massageReturnValueIfNecessary(Object returnValue) {
        if (returnValue instanceof ModelAndView) {
            return (ModelAndView) returnValue;
        }
        if (returnValue instanceof Map) {
            return new ModelAndView().addAllObjects((Map) returnValue);
        }
        if (returnValue instanceof String) {
            return new ModelAndView((String) returnValue);
        }
        return null;
    }

    protected Object newCommandObject(Class<?> clazz) throws Exception {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Creating new command of class [" + clazz.getName() + "]");
        }
        return BeanUtils.instantiateClass(clazz);
    }

    protected void bind(HttpServletRequest request, Object command) throws Exception {
        this.logger.debug("Binding request parameters onto MultiActionController command");
        ServletRequestDataBinder binder = createBinder(request, command);
        binder.bind(request);
        if (this.validators != null) {
            for (Validator validator : this.validators) {
                if (validator.supports(command.getClass())) {
                    ValidationUtils.invokeValidator(validator, command, binder.getBindingResult());
                }
            }
        }
        binder.closeNoCatch();
    }

    protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command, getCommandName(command));
        initBinder(request, binder);
        return binder;
    }

    protected String getCommandName(Object command) {
        return "command";
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        if (this.webBindingInitializer != null) {
            this.webBindingInitializer.initBinder(binder, new ServletWebRequest(request));
        }
    }

    protected Method getExceptionHandler(Throwable exception) {
        Method handler;
        Class<?> exceptionClass = exception.getClass();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Trying to find handler for exception class [" + exceptionClass.getName() + "]");
        }
        Method method = this.exceptionHandlerMap.get(exceptionClass);
        while (true) {
            handler = method;
            if (handler != null || exceptionClass == Throwable.class) {
                break;
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Trying to find handler for exception superclass [" + exceptionClass.getName() + "]");
            }
            exceptionClass = exceptionClass.getSuperclass();
            method = this.exceptionHandlerMap.get(exceptionClass);
        }
        return handler;
    }

    private ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws Exception {
        Method handler = getExceptionHandler(ex);
        if (handler != null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Invoking exception handler [" + handler + "] for exception: " + ex);
            }
            try {
                Object returnValue = handler.invoke(this.delegate, request, response, ex);
                return massageReturnValueIfNecessary(returnValue);
            } catch (InvocationTargetException ex2) {
                this.logger.error("Original exception overridden by exception handling failure", ex);
                ReflectionUtils.rethrowException(ex2.getTargetException());
            } catch (Exception ex22) {
                this.logger.error("Failed to invoke exception handler method", ex22);
            }
        } else {
            ReflectionUtils.rethrowException(ex);
        }
        throw new IllegalStateException("Should never get here");
    }
}
