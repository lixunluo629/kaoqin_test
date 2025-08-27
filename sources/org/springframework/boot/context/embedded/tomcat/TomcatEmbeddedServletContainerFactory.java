package org.springframework.boot.context.embedded.tomcat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Manager;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.session.StandardManager;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.boot.context.embedded.AbstractEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.Compression;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.SslStoreProvider;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/TomcatEmbeddedServletContainerFactory.class */
public class TomcatEmbeddedServletContainerFactory extends AbstractEmbeddedServletContainerFactory implements ResourceLoaderAware {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Set<Class<?>> NO_CLASSES = Collections.emptySet();
    public static final String DEFAULT_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";
    private File baseDirectory;
    private List<Valve> engineValves;
    private List<Valve> contextValves;
    private List<LifecycleListener> contextLifecycleListeners;
    private List<TomcatContextCustomizer> tomcatContextCustomizers;
    private List<TomcatConnectorCustomizer> tomcatConnectorCustomizers;
    private List<Connector> additionalTomcatConnectors;
    private ResourceLoader resourceLoader;
    private String protocol;
    private Set<String> tldSkipPatterns;
    private Charset uriEncoding;
    private int backgroundProcessorDelay;

    public TomcatEmbeddedServletContainerFactory() {
        this.engineValves = new ArrayList();
        this.contextValves = new ArrayList();
        this.contextLifecycleListeners = new ArrayList();
        this.tomcatContextCustomizers = new ArrayList();
        this.tomcatConnectorCustomizers = new ArrayList();
        this.additionalTomcatConnectors = new ArrayList();
        this.protocol = DEFAULT_PROTOCOL;
        this.tldSkipPatterns = new LinkedHashSet(TldSkipPatterns.DEFAULT);
        this.uriEncoding = DEFAULT_CHARSET;
    }

    public TomcatEmbeddedServletContainerFactory(int port) {
        super(port);
        this.engineValves = new ArrayList();
        this.contextValves = new ArrayList();
        this.contextLifecycleListeners = new ArrayList();
        this.tomcatContextCustomizers = new ArrayList();
        this.tomcatConnectorCustomizers = new ArrayList();
        this.additionalTomcatConnectors = new ArrayList();
        this.protocol = DEFAULT_PROTOCOL;
        this.tldSkipPatterns = new LinkedHashSet(TldSkipPatterns.DEFAULT);
        this.uriEncoding = DEFAULT_CHARSET;
    }

    public TomcatEmbeddedServletContainerFactory(String contextPath, int port) {
        super(contextPath, port);
        this.engineValves = new ArrayList();
        this.contextValves = new ArrayList();
        this.contextLifecycleListeners = new ArrayList();
        this.tomcatContextCustomizers = new ArrayList();
        this.tomcatConnectorCustomizers = new ArrayList();
        this.additionalTomcatConnectors = new ArrayList();
        this.protocol = DEFAULT_PROTOCOL;
        this.tldSkipPatterns = new LinkedHashSet(TldSkipPatterns.DEFAULT);
        this.uriEncoding = DEFAULT_CHARSET;
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainerFactory
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers) {
        Tomcat tomcat = new Tomcat();
        File baseDir = this.baseDirectory != null ? this.baseDirectory : createTempDir("tomcat");
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        Connector connector = new Connector(this.protocol);
        tomcat.getService().addConnector(connector);
        customizeConnector(connector);
        tomcat.setConnector(connector);
        tomcat.getHost().setAutoDeploy(false);
        configureEngine(tomcat.getEngine());
        for (Connector additionalConnector : this.additionalTomcatConnectors) {
            tomcat.getService().addConnector(additionalConnector);
        }
        prepareContext(tomcat.getHost(), initializers);
        return getTomcatEmbeddedServletContainer(tomcat);
    }

    private void configureEngine(Engine engine) {
        engine.setBackgroundProcessorDelay(this.backgroundProcessorDelay);
        for (Valve valve : this.engineValves) {
            engine.getPipeline().addValve(valve);
        }
    }

    protected void prepareContext(Host host, ServletContextInitializer[] initializers) {
        File docBase = getValidDocumentRoot();
        File docBase2 = docBase != null ? docBase : createTempDir("tomcat-docbase");
        final TomcatEmbeddedContext context = new TomcatEmbeddedContext();
        context.setName(getContextPath());
        context.setDisplayName(getDisplayName());
        context.setPath(getContextPath());
        context.setDocBase(docBase2.getAbsolutePath());
        context.addLifecycleListener(new Tomcat.FixContextListener());
        context.setParentClassLoader(this.resourceLoader != null ? this.resourceLoader.getClassLoader() : ClassUtils.getDefaultClassLoader());
        resetDefaultLocaleMapping(context);
        addLocaleMappings(context);
        try {
            context.setUseRelativeRedirects(false);
        } catch (NoSuchMethodError e) {
        }
        try {
            context.setCreateUploadTargets(true);
        } catch (NoSuchMethodError e2) {
        }
        SkipPatternJarScanner.apply(context, this.tldSkipPatterns);
        WebappLoader loader = new WebappLoader(context.getParentClassLoader());
        loader.setLoaderClass(TomcatEmbeddedWebappClassLoader.class.getName());
        loader.setDelegate(true);
        context.setLoader(loader);
        if (isRegisterDefaultServlet()) {
            addDefaultServlet(context);
        }
        if (shouldRegisterJspServlet()) {
            addJspServlet(context);
            addJasperInitializer(context);
            context.addLifecycleListener(new StoreMergedWebXmlListener());
        }
        context.addLifecycleListener(new LifecycleListener() { // from class: org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory.1
            @Override // org.apache.catalina.LifecycleListener
            public void lifecycleEvent(LifecycleEvent event) {
                if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
                    TomcatResources.get(context).addResourceJars(TomcatEmbeddedServletContainerFactory.this.getUrlsOfJarsWithMetaInfResources());
                }
            }
        });
        ServletContextInitializer[] initializersToUse = mergeInitializers(initializers);
        host.addChild(context);
        configureContext(context, initializersToUse);
        postProcessContext(context);
    }

    private void resetDefaultLocaleMapping(TomcatEmbeddedContext context) {
        context.addLocaleEncodingMappingParameter(Locale.ENGLISH.toString(), DEFAULT_CHARSET.displayName());
        context.addLocaleEncodingMappingParameter(Locale.FRENCH.toString(), DEFAULT_CHARSET.displayName());
    }

    private void addLocaleMappings(TomcatEmbeddedContext context) {
        for (Map.Entry<Locale, Charset> entry : getLocaleCharsetMappings().entrySet()) {
            Locale locale = entry.getKey();
            Charset charset = entry.getValue();
            context.addLocaleEncodingMappingParameter(locale.toString(), charset.toString());
        }
    }

    private void addDefaultServlet(Context context) {
        Wrapper defaultServlet = context.createWrapper();
        defaultServlet.setName("default");
        defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
        defaultServlet.addInitParameter("debug", "0");
        defaultServlet.addInitParameter("listings", "false");
        defaultServlet.setLoadOnStartup(1);
        defaultServlet.setOverridable(true);
        context.addChild(defaultServlet);
        addServletMapping(context, "/", "default");
    }

    private void addJspServlet(Context context) {
        Wrapper jspServlet = context.createWrapper();
        jspServlet.setName("jsp");
        jspServlet.setServletClass(getJspServlet().getClassName());
        jspServlet.addInitParameter("fork", "false");
        for (Map.Entry<String, String> initParameter : getJspServlet().getInitParameters().entrySet()) {
            jspServlet.addInitParameter(initParameter.getKey(), initParameter.getValue());
        }
        jspServlet.setLoadOnStartup(3);
        context.addChild(jspServlet);
        addServletMapping(context, "*.jsp", "jsp");
        addServletMapping(context, "*.jspx", "jsp");
    }

    private void addServletMapping(Context context, String pattern, String name) {
        context.addServletMapping(pattern, name);
    }

    private void addJasperInitializer(TomcatEmbeddedContext context) {
        try {
            ServletContainerInitializer initializer = (ServletContainerInitializer) ClassUtils.forName("org.apache.jasper.servlet.JasperInitializer", null).newInstance();
            context.addServletContainerInitializer(initializer, null);
        } catch (Exception e) {
        }
    }

    protected void customizeConnector(Connector connector) {
        int port = getPort() >= 0 ? getPort() : 0;
        connector.setPort(port);
        if (StringUtils.hasText(getServerHeader())) {
            connector.setAttribute("server", getServerHeader());
        }
        if (connector.getProtocolHandler() instanceof AbstractProtocol) {
            customizeProtocol((AbstractProtocol) connector.getProtocolHandler());
        }
        if (getUriEncoding() != null) {
            connector.setURIEncoding(getUriEncoding().name());
        }
        connector.setProperty("bindOnInit", "false");
        if (getSsl() != null && getSsl().isEnabled()) {
            customizeSsl(connector);
        }
        if (getCompression() != null && getCompression().getEnabled()) {
            customizeCompression(connector);
        }
        for (TomcatConnectorCustomizer customizer : this.tomcatConnectorCustomizers) {
            customizer.customize(connector);
        }
    }

    private void customizeProtocol(AbstractProtocol<?> protocol) {
        if (getAddress() != null) {
            protocol.setAddress(getAddress());
        }
    }

    private void customizeSsl(Connector connector) {
        ProtocolHandler handler = connector.getProtocolHandler();
        Assert.state(handler instanceof AbstractHttp11JsseProtocol, "To use SSL, the connector's protocol handler must be an AbstractHttp11JsseProtocol subclass");
        configureSsl((AbstractHttp11JsseProtocol) handler, getSsl());
        connector.setScheme("https");
        connector.setSecure(true);
    }

    private void customizeCompression(Connector connector) {
        ProtocolHandler handler = connector.getProtocolHandler();
        if (handler instanceof AbstractHttp11Protocol) {
            AbstractHttp11Protocol<?> protocol = (AbstractHttp11Protocol) handler;
            Compression compression = getCompression();
            protocol.setCompression(CustomBooleanEditor.VALUE_ON);
            protocol.setCompressionMinSize(compression.getMinResponseSize());
            configureCompressibleMimeTypes(protocol, compression);
            if (getCompression().getExcludedUserAgents() != null) {
                protocol.setNoCompressionUserAgents(StringUtils.arrayToCommaDelimitedString(getCompression().getExcludedUserAgents()));
            }
        }
    }

    private void configureCompressibleMimeTypes(AbstractHttp11Protocol<?> protocol, Compression compression) {
        protocol.setCompressableMimeType(StringUtils.arrayToCommaDelimitedString(compression.getMimeTypes()));
    }

    protected void configureSsl(AbstractHttp11JsseProtocol<?> protocol, Ssl ssl) {
        protocol.setSSLEnabled(true);
        protocol.setSslProtocol(ssl.getProtocol());
        configureSslClientAuth(protocol, ssl);
        protocol.setKeystorePass(ssl.getKeyStorePassword());
        protocol.setKeyPass(ssl.getKeyPassword());
        protocol.setKeyAlias(ssl.getKeyAlias());
        String ciphers = StringUtils.arrayToCommaDelimitedString(ssl.getCiphers());
        protocol.setCiphers(StringUtils.hasText(ciphers) ? ciphers : null);
        if (ssl.getEnabledProtocols() != null) {
            try {
                for (SSLHostConfig sslHostConfig : protocol.findSslHostConfigs()) {
                    sslHostConfig.setProtocols(StringUtils.arrayToCommaDelimitedString(ssl.getEnabledProtocols()));
                }
            } catch (NoSuchMethodError e) {
                Assert.isTrue(protocol.setProperty("sslEnabledProtocols", StringUtils.arrayToCommaDelimitedString(ssl.getEnabledProtocols())), "Failed to set sslEnabledProtocols");
            }
        }
        if (getSslStoreProvider() != null) {
            TomcatURLStreamHandlerFactory instance = TomcatURLStreamHandlerFactory.getInstance();
            instance.addUserFactory(new SslStoreProviderUrlStreamHandlerFactory(getSslStoreProvider()));
            protocol.setKeystoreFile("springbootssl:keyStore");
            protocol.setTruststoreFile("springbootssl:trustStore");
            return;
        }
        configureSslKeyStore(protocol, ssl);
        configureSslTrustStore(protocol, ssl);
    }

    private void configureSslClientAuth(AbstractHttp11JsseProtocol<?> protocol, Ssl ssl) {
        if (ssl.getClientAuth() == Ssl.ClientAuth.NEED) {
            protocol.setClientAuth(Boolean.TRUE.toString());
        } else if (ssl.getClientAuth() == Ssl.ClientAuth.WANT) {
            protocol.setClientAuth("want");
        }
    }

    protected void configureSslStoreProvider(AbstractHttp11JsseProtocol<?> protocol, SslStoreProvider sslStoreProvider) {
        Assert.isInstanceOf(Http11NioProtocol.class, protocol, "SslStoreProvider can only be used with Http11NioProtocol");
    }

    private void configureSslKeyStore(AbstractHttp11JsseProtocol<?> protocol, Ssl ssl) {
        try {
            protocol.setKeystoreFile(ResourceUtils.getURL(ssl.getKeyStore()).toString());
            if (ssl.getKeyStoreType() != null) {
                protocol.setKeystoreType(ssl.getKeyStoreType());
            }
            if (ssl.getKeyStoreProvider() != null) {
                protocol.setKeystoreProvider(ssl.getKeyStoreProvider());
            }
        } catch (FileNotFoundException ex) {
            throw new EmbeddedServletContainerException("Could not load key store: " + ex.getMessage(), ex);
        }
    }

    private void configureSslTrustStore(AbstractHttp11JsseProtocol<?> protocol, Ssl ssl) {
        if (ssl.getTrustStore() != null) {
            try {
                protocol.setTruststoreFile(ResourceUtils.getURL(ssl.getTrustStore()).toString());
            } catch (FileNotFoundException ex) {
                throw new EmbeddedServletContainerException("Could not load trust store: " + ex.getMessage(), ex);
            }
        }
        protocol.setTruststorePass(ssl.getTrustStorePassword());
        if (ssl.getTrustStoreType() != null) {
            protocol.setTruststoreType(ssl.getTrustStoreType());
        }
        if (ssl.getTrustStoreProvider() != null) {
            protocol.setTruststoreProvider(ssl.getTrustStoreProvider());
        }
    }

    protected void configureContext(Context context, ServletContextInitializer[] initializers) {
        TomcatStarter starter = new TomcatStarter(initializers);
        if (context instanceof TomcatEmbeddedContext) {
            ((TomcatEmbeddedContext) context).setStarter(starter);
        }
        context.addServletContainerInitializer(starter, NO_CLASSES);
        for (LifecycleListener lifecycleListener : this.contextLifecycleListeners) {
            context.addLifecycleListener(lifecycleListener);
        }
        for (Valve valve : this.contextValves) {
            context.getPipeline().addValve(valve);
        }
        for (ErrorPage errorPage : getErrorPages()) {
            new TomcatErrorPage(errorPage).addToContext(context);
        }
        Iterator<MimeMappings.Mapping> it = getMimeMappings().iterator();
        while (it.hasNext()) {
            MimeMappings.Mapping mapping = it.next();
            context.addMimeMapping(mapping.getExtension(), mapping.getMimeType());
        }
        configureSession(context);
        for (TomcatContextCustomizer customizer : this.tomcatContextCustomizers) {
            customizer.customize(context);
        }
    }

    private void configureSession(Context context) {
        long sessionTimeout = getSessionTimeoutInMinutes();
        context.setSessionTimeout((int) sessionTimeout);
        if (isPersistSession()) {
            Manager manager = context.getManager();
            if (manager == null) {
                manager = new StandardManager();
                context.setManager(manager);
            }
            configurePersistSession(manager);
            return;
        }
        context.addLifecycleListener(new DisablePersistSessionListener());
    }

    private void configurePersistSession(Manager manager) {
        Assert.state(manager instanceof StandardManager, "Unable to persist HTTP session state using manager type " + manager.getClass().getName());
        File dir = getValidSessionStoreDir();
        File file = new File(dir, "SESSIONS.ser");
        ((StandardManager) manager).setPathname(file.getAbsolutePath());
    }

    private long getSessionTimeoutInMinutes() {
        long sessionTimeout = getSessionTimeout();
        if (sessionTimeout > 0) {
            sessionTimeout = Math.max(TimeUnit.SECONDS.toMinutes(sessionTimeout), 1L);
        }
        return sessionTimeout;
    }

    protected void postProcessContext(Context context) {
    }

    protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(Tomcat tomcat) {
        return new TomcatEmbeddedServletContainer(tomcat, getPort() >= 0);
    }

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Deprecated
    public void setTldSkip(String tldSkip) {
        Assert.notNull(tldSkip, "TldSkip must not be null");
        setTldSkipPatterns(StringUtils.commaDelimitedListToSet(tldSkip));
    }

    public Set<String> getTldSkipPatterns() {
        return this.tldSkipPatterns;
    }

    public void setTldSkipPatterns(Collection<String> patterns) {
        Assert.notNull(patterns, "Patterns must not be null");
        this.tldSkipPatterns = new LinkedHashSet(patterns);
    }

    public void addTldSkipPatterns(String... patterns) {
        Assert.notNull(patterns, "Patterns must not be null");
        this.tldSkipPatterns.addAll(Arrays.asList(patterns));
    }

    public void setProtocol(String protocol) {
        Assert.hasLength(protocol, "Protocol must not be empty");
        this.protocol = protocol;
    }

    public void setEngineValves(Collection<? extends Valve> engineValves) {
        Assert.notNull(engineValves, "Valves must not be null");
        this.engineValves = new ArrayList(engineValves);
    }

    public Collection<Valve> getEngineValves() {
        return this.engineValves;
    }

    public void addEngineValves(Valve... engineValves) {
        Assert.notNull(engineValves, "Valves must not be null");
        this.engineValves.addAll(Arrays.asList(engineValves));
    }

    public void setContextValves(Collection<? extends Valve> contextValves) {
        Assert.notNull(contextValves, "Valves must not be null");
        this.contextValves = new ArrayList(contextValves);
    }

    public Collection<Valve> getContextValves() {
        return this.contextValves;
    }

    public void addContextValves(Valve... contextValves) {
        Assert.notNull(contextValves, "Valves must not be null");
        this.contextValves.addAll(Arrays.asList(contextValves));
    }

    public void setContextLifecycleListeners(Collection<? extends LifecycleListener> contextLifecycleListeners) {
        Assert.notNull(contextLifecycleListeners, "ContextLifecycleListeners must not be null");
        this.contextLifecycleListeners = new ArrayList(contextLifecycleListeners);
    }

    public Collection<LifecycleListener> getContextLifecycleListeners() {
        return this.contextLifecycleListeners;
    }

    public void addContextLifecycleListeners(LifecycleListener... contextLifecycleListeners) {
        Assert.notNull(contextLifecycleListeners, "ContextLifecycleListeners must not be null");
        this.contextLifecycleListeners.addAll(Arrays.asList(contextLifecycleListeners));
    }

    public void setTomcatContextCustomizers(Collection<? extends TomcatContextCustomizer> tomcatContextCustomizers) {
        Assert.notNull(tomcatContextCustomizers, "TomcatContextCustomizers must not be null");
        this.tomcatContextCustomizers = new ArrayList(tomcatContextCustomizers);
    }

    public Collection<TomcatContextCustomizer> getTomcatContextCustomizers() {
        return this.tomcatContextCustomizers;
    }

    public void addContextCustomizers(TomcatContextCustomizer... tomcatContextCustomizers) {
        Assert.notNull(tomcatContextCustomizers, "TomcatContextCustomizers must not be null");
        this.tomcatContextCustomizers.addAll(Arrays.asList(tomcatContextCustomizers));
    }

    public void setTomcatConnectorCustomizers(Collection<? extends TomcatConnectorCustomizer> tomcatConnectorCustomizers) {
        Assert.notNull(tomcatConnectorCustomizers, "TomcatConnectorCustomizers must not be null");
        this.tomcatConnectorCustomizers = new ArrayList(tomcatConnectorCustomizers);
    }

    public void addConnectorCustomizers(TomcatConnectorCustomizer... tomcatConnectorCustomizers) {
        Assert.notNull(tomcatConnectorCustomizers, "TomcatConnectorCustomizers must not be null");
        this.tomcatConnectorCustomizers.addAll(Arrays.asList(tomcatConnectorCustomizers));
    }

    public Collection<TomcatConnectorCustomizer> getTomcatConnectorCustomizers() {
        return this.tomcatConnectorCustomizers;
    }

    public void addAdditionalTomcatConnectors(Connector... connectors) {
        Assert.notNull(connectors, "Connectors must not be null");
        this.additionalTomcatConnectors.addAll(Arrays.asList(connectors));
    }

    public List<Connector> getAdditionalTomcatConnectors() {
        return this.additionalTomcatConnectors;
    }

    public void setUriEncoding(Charset uriEncoding) {
        this.uriEncoding = uriEncoding;
    }

    public Charset getUriEncoding() {
        return this.uriEncoding;
    }

    public void setBackgroundProcessorDelay(int delay) {
        this.backgroundProcessorDelay = delay;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/TomcatEmbeddedServletContainerFactory$StoreMergedWebXmlListener.class */
    private static class StoreMergedWebXmlListener implements LifecycleListener {
        private static final String MERGED_WEB_XML = "org.apache.tomcat.util.scan.MergedWebXml";

        private StoreMergedWebXmlListener() {
        }

        @Override // org.apache.catalina.LifecycleListener
        public void lifecycleEvent(LifecycleEvent event) {
            if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
                onStart((Context) event.getLifecycle());
            }
        }

        private void onStart(Context context) {
            ServletContext servletContext = context.getServletContext();
            if (servletContext.getAttribute(MERGED_WEB_XML) == null) {
                servletContext.setAttribute(MERGED_WEB_XML, getEmptyWebXml());
            }
        }

        private String getEmptyWebXml() throws IOException {
            InputStream stream = TomcatEmbeddedServletContainerFactory.class.getResourceAsStream("empty-web.xml");
            Assert.state(stream != null, "Unable to read empty web.xml");
            try {
                try {
                    return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
                } finally {
                    stream.close();
                }
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/TomcatEmbeddedServletContainerFactory$DisablePersistSessionListener.class */
    private static class DisablePersistSessionListener implements LifecycleListener {
        private DisablePersistSessionListener() {
        }

        @Override // org.apache.catalina.LifecycleListener
        public void lifecycleEvent(LifecycleEvent event) {
            if (event.getType().equals(Lifecycle.START_EVENT)) {
                Context context = (Context) event.getLifecycle();
                Manager manager = context.getManager();
                if (manager != null && (manager instanceof StandardManager)) {
                    ((StandardManager) manager).setPathname(null);
                }
            }
        }
    }
}
