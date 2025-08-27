package org.springframework.web.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SourceFilteringListener;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.async.CallableProcessingInterceptorAdapter;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.context.support.ServletRequestHandledEvent;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/FrameworkServlet.class */
public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {
    public static final String DEFAULT_NAMESPACE_SUFFIX = "-servlet";
    private static final String INIT_PARAM_DELIMITERS = ",; \t\n";
    private String contextAttribute;
    private String contextId;
    private String namespace;
    private String contextConfigLocation;
    private String contextInitializerClasses;
    private WebApplicationContext webApplicationContext;
    public static final Class<?> DEFAULT_CONTEXT_CLASS = XmlWebApplicationContext.class;
    public static final String SERVLET_CONTEXT_PREFIX = FrameworkServlet.class.getName() + ".CONTEXT.";
    private static final boolean responseGetStatusAvailable = ClassUtils.hasMethod(HttpServletResponse.class, "getStatus", new Class[0]);
    private Class<?> contextClass = DEFAULT_CONTEXT_CLASS;
    private final List<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers = new ArrayList();
    private boolean publishContext = true;
    private boolean publishEvents = true;
    private boolean threadContextInheritable = false;
    private boolean dispatchOptionsRequest = false;
    private boolean dispatchTraceRequest = false;
    private boolean webApplicationContextInjected = false;
    private boolean refreshEventReceived = false;

    protected abstract void doService(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;

    public FrameworkServlet() {
    }

    public FrameworkServlet(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    public void setContextAttribute(String contextAttribute) {
        this.contextAttribute = contextAttribute;
    }

    public String getContextAttribute() {
        return this.contextAttribute;
    }

    public void setContextClass(Class<?> contextClass) {
        this.contextClass = contextClass;
    }

    public Class<?> getContextClass() {
        return this.contextClass;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getContextId() {
        return this.contextId;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return this.namespace != null ? this.namespace : getServletName() + DEFAULT_NAMESPACE_SUFFIX;
    }

    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    public String getContextConfigLocation() {
        return this.contextConfigLocation;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setContextInitializers(ApplicationContextInitializer<?>... initializers) {
        if (initializers != null) {
            for (ApplicationContextInitializer<?> initializer : initializers) {
                this.contextInitializers.add(initializer);
            }
        }
    }

    public void setContextInitializerClasses(String contextInitializerClasses) {
        this.contextInitializerClasses = contextInitializerClasses;
    }

    public void setPublishContext(boolean publishContext) {
        this.publishContext = publishContext;
    }

    public void setPublishEvents(boolean publishEvents) {
        this.publishEvents = publishEvents;
    }

    public void setThreadContextInheritable(boolean threadContextInheritable) {
        this.threadContextInheritable = threadContextInheritable;
    }

    public void setDispatchOptionsRequest(boolean dispatchOptionsRequest) {
        this.dispatchOptionsRequest = dispatchOptionsRequest;
    }

    public void setDispatchTraceRequest(boolean dispatchTraceRequest) {
        this.dispatchTraceRequest = dispatchTraceRequest;
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (this.webApplicationContext == null && (applicationContext instanceof WebApplicationContext)) {
            this.webApplicationContext = (WebApplicationContext) applicationContext;
            this.webApplicationContextInjected = true;
        }
    }

    @Override // org.springframework.web.servlet.HttpServletBean
    protected final void initServletBean() throws ServletException {
        getServletContext().log("Initializing Spring FrameworkServlet '" + getServletName() + "'");
        if (this.logger.isInfoEnabled()) {
            this.logger.info("FrameworkServlet '" + getServletName() + "': initialization started");
        }
        long startTime = System.currentTimeMillis();
        try {
            this.webApplicationContext = initWebApplicationContext();
            initFrameworkServlet();
            if (this.logger.isInfoEnabled()) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                this.logger.info("FrameworkServlet '" + getServletName() + "': initialization completed in " + elapsedTime + " ms");
            }
        } catch (RuntimeException ex) {
            this.logger.error("Context initialization failed", ex);
            throw ex;
        } catch (ServletException ex2) {
            this.logger.error("Context initialization failed", ex2);
            throw ex2;
        }
    }

    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext rootContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        WebApplicationContext wac = null;
        if (this.webApplicationContext != null) {
            wac = this.webApplicationContext;
            if (wac instanceof ConfigurableWebApplicationContext) {
                ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
                if (!cwac.isActive()) {
                    if (cwac.getParent() == null) {
                        cwac.setParent(rootContext);
                    }
                    configureAndRefreshWebApplicationContext(cwac);
                }
            }
        }
        if (wac == null) {
            wac = findWebApplicationContext();
        }
        if (wac == null) {
            wac = createWebApplicationContext(rootContext);
        }
        if (!this.refreshEventReceived) {
            onRefresh(wac);
        }
        if (this.publishContext) {
            String attrName = getServletContextAttributeName();
            getServletContext().setAttribute(attrName, wac);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Published WebApplicationContext of servlet '" + getServletName() + "' as ServletContext attribute with name [" + attrName + "]");
            }
        }
        return wac;
    }

    protected WebApplicationContext findWebApplicationContext() {
        String attrName = getContextAttribute();
        if (attrName == null) {
            return null;
        }
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
        if (wac == null) {
            throw new IllegalStateException("No WebApplicationContext found: initializer not registered?");
        }
        return wac;
    }

    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
        Class<?> contextClass = getContextClass();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Servlet with name '" + getServletName() + "' will try to create custom WebApplicationContext context of class '" + contextClass.getName() + "', using parent context [" + parent + "]");
        }
        if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
            throw new ApplicationContextException("Fatal initialization error in servlet with name '" + getServletName() + "': custom WebApplicationContext class [" + contextClass.getName() + "] is not of type ConfigurableWebApplicationContext");
        }
        ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
        wac.setEnvironment(getEnvironment());
        wac.setParent(parent);
        wac.setConfigLocation(getContextConfigLocation());
        configureAndRefreshWebApplicationContext(wac);
        return wac;
    }

    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {
        if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
            if (this.contextId != null) {
                wac.setId(this.contextId);
            } else {
                wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + ObjectUtils.getDisplayString(getServletContext().getContextPath()) + '/' + getServletName());
            }
        }
        wac.setServletContext(getServletContext());
        wac.setServletConfig(getServletConfig());
        wac.setNamespace(getNamespace());
        wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener()));
        ConfigurableEnvironment env = wac.getEnvironment();
        if (env instanceof ConfigurableWebEnvironment) {
            ((ConfigurableWebEnvironment) env).initPropertySources(getServletContext(), getServletConfig());
        }
        postProcessWebApplicationContext(wac);
        applyInitializers(wac);
        wac.refresh();
    }

    protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent) {
        return createWebApplicationContext((ApplicationContext) parent);
    }

    protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac) {
    }

    protected void applyInitializers(ConfigurableApplicationContext wac) {
        String globalClassNames = getServletContext().getInitParameter(ContextLoader.GLOBAL_INITIALIZER_CLASSES_PARAM);
        if (globalClassNames != null) {
            for (String className : StringUtils.tokenizeToStringArray(globalClassNames, ",; \t\n")) {
                this.contextInitializers.add(loadInitializer(className, wac));
            }
        }
        if (this.contextInitializerClasses != null) {
            for (String className2 : StringUtils.tokenizeToStringArray(this.contextInitializerClasses, ",; \t\n")) {
                this.contextInitializers.add(loadInitializer(className2, wac));
            }
        }
        AnnotationAwareOrderComparator.sort(this.contextInitializers);
        for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
            initializer.initialize(wac);
        }
    }

    private ApplicationContextInitializer<ConfigurableApplicationContext> loadInitializer(String className, ConfigurableApplicationContext wac) throws LinkageError {
        try {
            Class<?> initializerClass = ClassUtils.forName(className, wac.getClassLoader());
            Class<?> initializerContextClass = GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
            if (initializerContextClass != null && !initializerContextClass.isInstance(wac)) {
                throw new ApplicationContextException(String.format("Could not apply context initializer [%s] since its generic parameter [%s] is not assignable from the type of application context used by this framework servlet: [%s]", initializerClass.getName(), initializerContextClass.getName(), wac.getClass().getName()));
            }
            return (ApplicationContextInitializer) BeanUtils.instantiateClass(initializerClass, ApplicationContextInitializer.class);
        } catch (ClassNotFoundException ex) {
            throw new ApplicationContextException(String.format("Could not load class [%s] specified via 'contextInitializerClasses' init-param", className), ex);
        }
    }

    public String getServletContextAttributeName() {
        return SERVLET_CONTEXT_PREFIX + getServletName();
    }

    public final WebApplicationContext getWebApplicationContext() {
        return this.webApplicationContext;
    }

    protected void initFrameworkServlet() throws ServletException {
    }

    public void refresh() throws IllegalStateException, BeansException {
        WebApplicationContext wac = getWebApplicationContext();
        if (!(wac instanceof ConfigurableApplicationContext)) {
            throw new IllegalStateException("WebApplicationContext does not support refresh: " + wac);
        }
        ((ConfigurableApplicationContext) wac).refresh();
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.refreshEventReceived = true;
        onRefresh(event.getApplicationContext());
    }

    protected void onRefresh(ApplicationContext context) {
    }

    @Override // javax.servlet.GenericServlet, javax.servlet.Servlet
    public void destroy() {
        getServletContext().log("Destroying Spring FrameworkServlet '" + getServletName() + "'");
        if ((this.webApplicationContext instanceof ConfigurableApplicationContext) && !this.webApplicationContextInjected) {
            ((ConfigurableApplicationContext) this.webApplicationContext).close();
        }
    }

    @Override // javax.servlet.http.HttpServlet
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
        if (httpMethod == HttpMethod.PATCH || httpMethod == null) {
            processRequest(request, response);
        } else {
            super.service(request, response);
        }
    }

    @Override // javax.servlet.http.HttpServlet
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override // javax.servlet.http.HttpServlet
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override // javax.servlet.http.HttpServlet
    protected final void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override // javax.servlet.http.HttpServlet
    protected final void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override // javax.servlet.http.HttpServlet
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (this.dispatchOptionsRequest || CorsUtils.isPreFlightRequest(request)) {
            processRequest(request, response);
            if (response.containsHeader("Allow")) {
                return;
            }
        }
        super.doOptions(request, new HttpServletResponseWrapper(response) { // from class: org.springframework.web.servlet.FrameworkServlet.1
            @Override // javax.servlet.http.HttpServletResponseWrapper, javax.servlet.http.HttpServletResponse
            public void setHeader(String name, String value) {
                if ("Allow".equals(name)) {
                    value = (StringUtils.hasLength(value) ? value + ", " : "") + HttpMethod.PATCH.name();
                }
                super.setHeader(name, value);
            }
        });
    }

    @Override // javax.servlet.http.HttpServlet
    protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (this.dispatchTraceRequest) {
            processRequest(request, response);
            if ("message/http".equals(response.getContentType())) {
                return;
            }
        }
        super.doTrace(request, response);
    }

    protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
        LocaleContext localeContext = buildLocaleContext(request);
        RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);
        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
        asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor());
        initContextHolders(request, localeContext, requestAttributes);
        try {
            try {
                doService(request, response);
                resetContextHolders(request, previousLocaleContext, previousAttributes);
                if (requestAttributes != null) {
                    requestAttributes.requestCompleted();
                }
                if (this.logger.isDebugEnabled()) {
                    if (0 != 0) {
                        this.logger.debug("Could not complete request", null);
                    } else if (asyncManager.isConcurrentHandlingStarted()) {
                        this.logger.debug("Leaving response open for concurrent processing");
                    } else {
                        this.logger.debug("Successfully completed request");
                    }
                }
                publishRequestHandledEvent(request, response, startTime, null);
            } catch (IOException ex) {
                throw ex;
            } catch (ServletException ex2) {
                throw ex2;
            } catch (Throwable ex3) {
                throw new NestedServletException("Request processing failed", ex3);
            }
        } catch (Throwable th) {
            resetContextHolders(request, previousLocaleContext, previousAttributes);
            if (requestAttributes != null) {
                requestAttributes.requestCompleted();
            }
            if (this.logger.isDebugEnabled()) {
                if (0 != 0) {
                    this.logger.debug("Could not complete request", null);
                } else if (asyncManager.isConcurrentHandlingStarted()) {
                    this.logger.debug("Leaving response open for concurrent processing");
                } else {
                    this.logger.debug("Successfully completed request");
                }
            }
            publishRequestHandledEvent(request, response, startTime, null);
            throw th;
        }
    }

    protected LocaleContext buildLocaleContext(HttpServletRequest request) {
        return new SimpleLocaleContext(request.getLocale());
    }

    protected ServletRequestAttributes buildRequestAttributes(HttpServletRequest request, HttpServletResponse response, RequestAttributes previousAttributes) {
        if (previousAttributes == null || (previousAttributes instanceof ServletRequestAttributes)) {
            return new ServletRequestAttributes(request, response);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initContextHolders(HttpServletRequest request, LocaleContext localeContext, RequestAttributes requestAttributes) {
        if (localeContext != null) {
            LocaleContextHolder.setLocaleContext(localeContext, this.threadContextInheritable);
        }
        if (requestAttributes != null) {
            RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
        }
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Bound request context to thread: " + request);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetContextHolders(HttpServletRequest request, LocaleContext prevLocaleContext, RequestAttributes previousAttributes) {
        LocaleContextHolder.setLocaleContext(prevLocaleContext, this.threadContextInheritable);
        RequestContextHolder.setRequestAttributes(previousAttributes, this.threadContextInheritable);
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Cleared thread-bound request context: " + request);
        }
    }

    private void publishRequestHandledEvent(HttpServletRequest request, HttpServletResponse response, long startTime, Throwable failureCause) {
        if (this.publishEvents) {
            long processingTime = System.currentTimeMillis() - startTime;
            int statusCode = responseGetStatusAvailable ? response.getStatus() : -1;
            this.webApplicationContext.publishEvent((ApplicationEvent) new ServletRequestHandledEvent(this, request.getRequestURI(), request.getRemoteAddr(), request.getMethod(), getServletConfig().getServletName(), WebUtils.getSessionId(request), getUsernameForRequest(request), processingTime, failureCause, statusCode));
        }
    }

    protected String getUsernameForRequest(HttpServletRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal != null) {
            return userPrincipal.getName();
        }
        return null;
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/FrameworkServlet$ContextRefreshListener.class */
    private class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {
        private ContextRefreshListener() {
        }

        @Override // org.springframework.context.ApplicationListener
        public void onApplicationEvent(ContextRefreshedEvent event) {
            FrameworkServlet.this.onApplicationEvent(event);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/FrameworkServlet$RequestBindingInterceptor.class */
    private class RequestBindingInterceptor extends CallableProcessingInterceptorAdapter {
        private RequestBindingInterceptor() {
        }

        @Override // org.springframework.web.context.request.async.CallableProcessingInterceptorAdapter, org.springframework.web.context.request.async.CallableProcessingInterceptor
        public <T> void preProcess(NativeWebRequest webRequest, Callable<T> task) {
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
            if (request != null) {
                HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse(HttpServletResponse.class);
                FrameworkServlet.this.initContextHolders(request, FrameworkServlet.this.buildLocaleContext(request), FrameworkServlet.this.buildRequestAttributes(request, response, null));
            }
        }

        @Override // org.springframework.web.context.request.async.CallableProcessingInterceptorAdapter, org.springframework.web.context.request.async.CallableProcessingInterceptor
        public <T> void postProcess(NativeWebRequest webRequest, Callable<T> task, Object concurrentResult) {
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
            if (request != null) {
                FrameworkServlet.this.resetContextHolders(request, null, null);
            }
        }
    }
}
