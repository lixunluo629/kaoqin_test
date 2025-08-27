package org.springframework.web.servlet.mvc.method.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.EmptyTargetSource;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.Factory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/MvcUriComponentsBuilder.class */
public class MvcUriComponentsBuilder {
    public static final String MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME = "mvcUriComponentsContributor";
    private static final Log logger = LogFactory.getLog(MvcUriComponentsBuilder.class);
    private static final SpringObjenesis objenesis = new SpringObjenesis();
    private static final PathMatcher pathMatcher = new AntPathMatcher();
    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private static final CompositeUriComponentsContributor defaultUriComponentsContributor = new CompositeUriComponentsContributor(new PathVariableMethodArgumentResolver(), new RequestParamMethodArgumentResolver(false));
    private final UriComponentsBuilder baseUrl;

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/MvcUriComponentsBuilder$MethodInvocationInfo.class */
    public interface MethodInvocationInfo {
        Class<?> getControllerType();

        Method getControllerMethod();

        Object[] getArgumentValues();
    }

    protected MvcUriComponentsBuilder(UriComponentsBuilder baseUrl) {
        Assert.notNull(baseUrl, "'baseUrl' is required");
        this.baseUrl = baseUrl;
    }

    public static MvcUriComponentsBuilder relativeTo(UriComponentsBuilder baseUrl) {
        return new MvcUriComponentsBuilder(baseUrl);
    }

    public static UriComponentsBuilder fromController(Class<?> controllerType) {
        return fromController(null, controllerType);
    }

    public static UriComponentsBuilder fromController(UriComponentsBuilder builder, Class<?> controllerType) {
        UriComponentsBuilder builder2 = getBaseUrlToUse(builder);
        String mapping = getTypeRequestMapping(controllerType);
        return builder2.path(mapping);
    }

    public static UriComponentsBuilder fromMethodName(Class<?> controllerType, String methodName, Object... args) {
        Method method = getMethod(controllerType, methodName, args);
        return fromMethodInternal(null, controllerType, method, args);
    }

    public static UriComponentsBuilder fromMethodName(UriComponentsBuilder builder, Class<?> controllerType, String methodName, Object... args) {
        Method method = getMethod(controllerType, methodName, args);
        return fromMethodInternal(builder, controllerType, method, args);
    }

    public static UriComponentsBuilder fromMethodCall(Object info) {
        Assert.isInstanceOf(MethodInvocationInfo.class, info, "MethodInvocationInfo required");
        MethodInvocationInfo invocationInfo = (MethodInvocationInfo) info;
        Class<?> controllerType = invocationInfo.getControllerType();
        Method method = invocationInfo.getControllerMethod();
        Object[] arguments = invocationInfo.getArgumentValues();
        return fromMethodInternal(null, controllerType, method, arguments);
    }

    public static UriComponentsBuilder fromMethodCall(UriComponentsBuilder builder, Object info) {
        Assert.isInstanceOf(MethodInvocationInfo.class, info, "MethodInvocationInfo required");
        MethodInvocationInfo invocationInfo = (MethodInvocationInfo) info;
        Class<?> controllerType = invocationInfo.getControllerType();
        Method method = invocationInfo.getControllerMethod();
        Object[] arguments = invocationInfo.getArgumentValues();
        return fromMethodInternal(builder, controllerType, method, arguments);
    }

    public static MethodArgumentBuilder fromMappingName(String mappingName) {
        return fromMappingName(null, mappingName);
    }

    public static MethodArgumentBuilder fromMappingName(UriComponentsBuilder builder, String name) {
        RequestMappingInfoHandlerMapping handlerMapping = getRequestMappingInfoHandlerMapping();
        List<HandlerMethod> handlerMethods = handlerMapping.getHandlerMethodsForMappingName(name);
        if (handlerMethods == null) {
            throw new IllegalArgumentException("Mapping mappingName not found: " + name);
        }
        if (handlerMethods.size() != 1) {
            throw new IllegalArgumentException("No unique match for mapping mappingName " + name + ": " + handlerMethods);
        }
        HandlerMethod handlerMethod = handlerMethods.get(0);
        Class<?> controllerType = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();
        return new MethodArgumentBuilder(builder, controllerType, method);
    }

    public static UriComponentsBuilder fromMethod(Class<?> controllerType, Method method, Object... args) {
        return fromMethodInternal(null, controllerType, method, args);
    }

    public static UriComponentsBuilder fromMethod(UriComponentsBuilder baseUrl, Class<?> controllerType, Method method, Object... args) {
        return fromMethodInternal(baseUrl, controllerType != null ? controllerType : method.getDeclaringClass(), method, args);
    }

    @Deprecated
    public static UriComponentsBuilder fromMethod(Method method, Object... args) {
        return fromMethodInternal(null, method.getDeclaringClass(), method, args);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static UriComponentsBuilder fromMethodInternal(UriComponentsBuilder baseUrl, Class<?> controllerType, Method method, Object... args) {
        UriComponentsBuilder baseUrl2 = getBaseUrlToUse(baseUrl);
        String typePath = getTypeRequestMapping(controllerType);
        String methodPath = getMethodRequestMapping(method);
        String path = pathMatcher.combine(typePath, methodPath);
        baseUrl2.path(path);
        UriComponents uriComponents = applyContributors(baseUrl2, method, args);
        return UriComponentsBuilder.newInstance().uriComponents(uriComponents);
    }

    private static UriComponentsBuilder getBaseUrlToUse(UriComponentsBuilder baseUrl) {
        if (baseUrl != null) {
            return baseUrl.cloneBuilder();
        }
        return ServletUriComponentsBuilder.fromCurrentServletMapping();
    }

    private static String getTypeRequestMapping(Class<?> controllerType) {
        Assert.notNull(controllerType, "'controllerType' must not be null");
        RequestMapping requestMapping = (RequestMapping) AnnotatedElementUtils.findMergedAnnotation(controllerType, RequestMapping.class);
        if (requestMapping == null) {
            return "/";
        }
        String[] paths = requestMapping.path();
        if (ObjectUtils.isEmpty((Object[]) paths) || StringUtils.isEmpty(paths[0])) {
            return "/";
        }
        if (paths.length > 1 && logger.isWarnEnabled()) {
            logger.warn("Multiple paths on controller " + controllerType.getName() + ", using first one");
        }
        return paths[0];
    }

    private static String getMethodRequestMapping(Method method) {
        Assert.notNull(method, "'method' must not be null");
        RequestMapping requestMapping = (RequestMapping) AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (requestMapping == null) {
            throw new IllegalArgumentException("No @RequestMapping on: " + method.toGenericString());
        }
        String[] paths = requestMapping.path();
        if (ObjectUtils.isEmpty((Object[]) paths) || StringUtils.isEmpty(paths[0])) {
            return "/";
        }
        if (paths.length > 1 && logger.isWarnEnabled()) {
            logger.warn("Multiple paths on method " + method.toGenericString() + ", using first one");
        }
        return paths[0];
    }

    private static Method getMethod(Class<?> controllerType, final String methodName, final Object... args) {
        ReflectionUtils.MethodFilter selector = new ReflectionUtils.MethodFilter() { // from class: org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.1
            @Override // org.springframework.util.ReflectionUtils.MethodFilter
            public boolean matches(Method method) {
                String name = method.getName();
                int argLength = method.getParameterTypes().length;
                return name.equals(methodName) && argLength == args.length;
            }
        };
        Set<Method> methods = MethodIntrospector.selectMethods(controllerType, selector);
        if (methods.size() == 1) {
            return methods.iterator().next();
        }
        if (methods.size() > 1) {
            throw new IllegalArgumentException(String.format("Found two methods named '%s' accepting arguments %s in controller %s: [%s]", methodName, Arrays.asList(args), controllerType.getName(), methods));
        }
        throw new IllegalArgumentException("No method named '" + methodName + "' with " + args.length + " arguments found in controller " + controllerType.getName());
    }

    private static UriComponents applyContributors(UriComponentsBuilder builder, Method method, Object... args) {
        CompositeUriComponentsContributor contributor = getConfiguredUriComponentsContributor();
        if (contributor == null) {
            logger.debug("Using default CompositeUriComponentsContributor");
            contributor = defaultUriComponentsContributor;
        }
        int paramCount = method.getParameterTypes().length;
        int argCount = args.length;
        if (paramCount != argCount) {
            throw new IllegalArgumentException("Number of method parameters " + paramCount + " does not match number of argument values " + argCount);
        }
        final Map<String, Object> uriVars = new HashMap<>();
        for (int i = 0; i < paramCount; i++) {
            MethodParameter param = new SynthesizingMethodParameter(method, i);
            param.initParameterNameDiscovery(parameterNameDiscoverer);
            contributor.contributeMethodArgument(param, args[i], builder, uriVars);
        }
        return builder.build().expand(new UriComponents.UriTemplateVariables() { // from class: org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.2
            @Override // org.springframework.web.util.UriComponents.UriTemplateVariables
            public Object getValue(String name) {
                return uriVars.containsKey(name) ? uriVars.get(name) : UriComponents.UriTemplateVariables.SKIP_VALUE;
            }
        });
    }

    private static CompositeUriComponentsContributor getConfiguredUriComponentsContributor() {
        WebApplicationContext wac = getWebApplicationContext();
        if (wac == null) {
            return null;
        }
        try {
            return (CompositeUriComponentsContributor) wac.getBean(MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME, CompositeUriComponentsContributor.class);
        } catch (NoSuchBeanDefinitionException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("No CompositeUriComponentsContributor bean with name 'mvcUriComponentsContributor'");
                return null;
            }
            return null;
        }
    }

    private static RequestMappingInfoHandlerMapping getRequestMappingInfoHandlerMapping() {
        WebApplicationContext wac = getWebApplicationContext();
        Assert.notNull(wac, "Cannot lookup handler method mappings without WebApplicationContext");
        try {
            return (RequestMappingInfoHandlerMapping) wac.getBean(RequestMappingInfoHandlerMapping.class);
        } catch (NoUniqueBeanDefinitionException ex) {
            throw new IllegalStateException("More than one RequestMappingInfoHandlerMapping beans found", ex);
        } catch (NoSuchBeanDefinitionException ex2) {
            throw new IllegalStateException("No RequestMappingInfoHandlerMapping bean", ex2);
        }
    }

    private static WebApplicationContext getWebApplicationContext() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            logger.debug("No request bound to the current thread: not in a DispatcherServlet request?");
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        WebApplicationContext wac = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (wac == null) {
            logger.debug("No WebApplicationContext found: not in a DispatcherServlet request?");
            return null;
        }
        return wac;
    }

    public static <T> T on(Class<T> cls) {
        return (T) controller(cls);
    }

    public static <T> T controller(Class<T> cls) {
        Assert.notNull(cls, "'controllerType' must not be null");
        return (T) initProxy(cls, new ControllerMethodInvocationInterceptor(cls));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T initProxy(Class<?> cls, ControllerMethodInvocationInterceptor controllerMethodInvocationInterceptor) {
        if (cls.isInterface()) {
            ProxyFactory proxyFactory = new ProxyFactory(EmptyTargetSource.INSTANCE);
            proxyFactory.addInterface(cls);
            proxyFactory.addInterface(MethodInvocationInfo.class);
            proxyFactory.addAdvice(controllerMethodInvocationInterceptor);
            return (T) proxyFactory.getProxy();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setInterfaces(new Class[]{MethodInvocationInfo.class});
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setCallbackType(MethodInterceptor.class);
        Class<T> clsCreateClass = enhancer.createClass();
        T tNewInstance = null;
        if (objenesis.isWorthTrying()) {
            try {
                tNewInstance = objenesis.newInstance(clsCreateClass, enhancer.getUseCache());
            } catch (ObjenesisException e) {
                logger.debug("Unable to instantiate controller proxy using Objenesis, falling back to regular construction", e);
            }
        }
        if (tNewInstance == null) {
            try {
                tNewInstance = clsCreateClass.newInstance();
            } catch (Throwable th) {
                throw new IllegalStateException("Unable to instantiate controller proxy using Objenesis, and regular controller instantiation via default constructor fails as well", th);
            }
        }
        ((Factory) tNewInstance).setCallbacks(new Callback[]{controllerMethodInvocationInterceptor});
        return tNewInstance;
    }

    public UriComponentsBuilder withController(Class<?> controllerType) {
        return fromController(this.baseUrl, controllerType);
    }

    public UriComponentsBuilder withMethodName(Class<?> controllerType, String methodName, Object... args) {
        return fromMethodName(this.baseUrl, controllerType, methodName, args);
    }

    public UriComponentsBuilder withMethodCall(Object invocationInfo) {
        return fromMethodCall(this.baseUrl, invocationInfo);
    }

    public MethodArgumentBuilder withMappingName(String mappingName) {
        return fromMappingName(this.baseUrl, mappingName);
    }

    public UriComponentsBuilder withMethod(Class<?> controllerType, Method method, Object... args) {
        return fromMethod(this.baseUrl, controllerType, method, args);
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/MvcUriComponentsBuilder$ControllerMethodInvocationInterceptor.class */
    private static class ControllerMethodInvocationInterceptor implements MethodInterceptor, org.aopalliance.intercept.MethodInterceptor {
        private static final Method getControllerMethod = ReflectionUtils.findMethod(MethodInvocationInfo.class, "getControllerMethod");
        private static final Method getArgumentValues = ReflectionUtils.findMethod(MethodInvocationInfo.class, "getArgumentValues");
        private static final Method getControllerType = ReflectionUtils.findMethod(MethodInvocationInfo.class, "getControllerType");
        private final Class<?> controllerType;
        private Method controllerMethod;
        private Object[] argumentValues;

        ControllerMethodInvocationInterceptor(Class<?> controllerType) {
            this.controllerType = controllerType;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
            if (getControllerType.equals(method)) {
                return this.controllerType;
            }
            if (getControllerMethod.equals(method)) {
                return this.controllerMethod;
            }
            if (getArgumentValues.equals(method)) {
                return this.argumentValues;
            }
            if (ReflectionUtils.isObjectMethod(method)) {
                return ReflectionUtils.invokeMethod(method, obj, args);
            }
            this.controllerMethod = method;
            this.argumentValues = args;
            Class<?> returnType = method.getReturnType();
            try {
                if (returnType == Void.TYPE) {
                    return null;
                }
                return returnType.cast(MvcUriComponentsBuilder.initProxy(returnType, this));
            } catch (Throwable ex) {
                throw new IllegalStateException("Failed to create proxy for controller method return type: " + method, ex);
            }
        }

        @Override // org.aopalliance.intercept.MethodInterceptor
        public Object invoke(MethodInvocation inv) throws Throwable {
            return intercept(inv.getThis(), inv.getMethod(), inv.getArguments(), null);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/MvcUriComponentsBuilder$MethodArgumentBuilder.class */
    public static class MethodArgumentBuilder {
        private final Class<?> controllerType;
        private final Method method;
        private final Object[] argumentValues;
        private final UriComponentsBuilder baseUrl;

        public MethodArgumentBuilder(Class<?> controllerType, Method method) {
            this(null, controllerType, method);
        }

        public MethodArgumentBuilder(UriComponentsBuilder baseUrl, Class<?> controllerType, Method method) {
            Assert.notNull(controllerType, "'controllerType' is required");
            Assert.notNull(method, "'method' is required");
            this.baseUrl = baseUrl != null ? baseUrl : initBaseUrl();
            this.controllerType = controllerType;
            this.method = method;
            this.argumentValues = new Object[method.getParameterTypes().length];
            for (int i = 0; i < this.argumentValues.length; i++) {
                this.argumentValues[i] = null;
            }
        }

        @Deprecated
        public MethodArgumentBuilder(Method method) {
            this(method.getDeclaringClass(), method);
        }

        private static UriComponentsBuilder initBaseUrl() {
            UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
            return UriComponentsBuilder.fromPath(builder.build().getPath());
        }

        public MethodArgumentBuilder arg(int index, Object value) {
            this.argumentValues[index] = value;
            return this;
        }

        public String build() {
            return MvcUriComponentsBuilder.fromMethodInternal(this.baseUrl, this.controllerType, this.method, this.argumentValues).build(false).encode().toUriString();
        }

        public String buildAndExpand(Object... uriVars) {
            return MvcUriComponentsBuilder.fromMethodInternal(this.baseUrl, this.controllerType, this.method, this.argumentValues).build(false).expand(uriVars).encode().toString();
        }
    }
}
