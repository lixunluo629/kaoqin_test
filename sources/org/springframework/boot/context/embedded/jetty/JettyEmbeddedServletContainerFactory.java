package org.springframework.boot.context.embedded.jetty;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.util.TempFile;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ForwardedRequestCustomizer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.FileSessionDataStore;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.eclipse.jetty.util.resource.JarResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.AbstractConfiguration;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.boot.context.embedded.AbstractEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.Compression;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory.class */
public class JettyEmbeddedServletContainerFactory extends AbstractEmbeddedServletContainerFactory implements ResourceLoaderAware {
    private static final String GZIP_HANDLER_JETTY_9_2 = "org.eclipse.jetty.servlets.gzip.GzipHandler";
    private static final String GZIP_HANDLER_JETTY_8 = "org.eclipse.jetty.server.handler.GzipHandler";
    private static final String GZIP_HANDLER_JETTY_9_3 = "org.eclipse.jetty.server.handler.gzip.GzipHandler";
    private static final String CONNECTOR_JETTY_8 = "org.eclipse.jetty.server.nio.SelectChannelConnector";
    private static final String SESSION_JETTY_9_3 = "org.eclipse.jetty.server.session.HashSessionManager";
    private List<Configuration> configurations;
    private boolean useForwardHeaders;
    private int acceptors;
    private int selectors;
    private List<JettyServerCustomizer> jettyServerCustomizers;
    private ResourceLoader resourceLoader;
    private ThreadPool threadPool;

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$ConnectorFactory.class */
    private interface ConnectorFactory {
        AbstractConnector createConnector(Server server, InetSocketAddress inetSocketAddress, int i, int i2);
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$GzipHandlerFactory.class */
    private interface GzipHandlerFactory {
        HandlerWrapper createGzipHandler(Compression compression);
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$ServerFactory.class */
    private interface ServerFactory {
        Server createServer(ThreadPool threadPool);
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$SessionConfigurer.class */
    private interface SessionConfigurer {
        void configure(WebAppContext webAppContext, int i, boolean z, SessionDirectory sessionDirectory);
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$SessionDirectory.class */
    private interface SessionDirectory {
        File get();
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$SslServerConnectorFactory.class */
    private interface SslServerConnectorFactory {
        /* renamed from: createConnector */
        AbstractConnector mo7444createConnector(Server server, SslContextFactory sslContextFactory, InetSocketAddress inetSocketAddress);
    }

    public JettyEmbeddedServletContainerFactory() {
        this.configurations = new ArrayList();
        this.acceptors = -1;
        this.selectors = -1;
        this.jettyServerCustomizers = new ArrayList();
    }

    public JettyEmbeddedServletContainerFactory(int port) {
        super(port);
        this.configurations = new ArrayList();
        this.acceptors = -1;
        this.selectors = -1;
        this.jettyServerCustomizers = new ArrayList();
    }

    public JettyEmbeddedServletContainerFactory(String contextPath, int port) {
        super(contextPath, port);
        this.configurations = new ArrayList();
        this.acceptors = -1;
        this.selectors = -1;
        this.jettyServerCustomizers = new ArrayList();
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainerFactory
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JettyEmbeddedWebAppContext context = new JettyEmbeddedWebAppContext();
        int port = getPort() >= 0 ? getPort() : 0;
        InetSocketAddress address = new InetSocketAddress(getAddress(), port);
        Server server = createServer(address);
        configureWebAppContext(context, initializers);
        server.setHandler(addHandlerWrappers(context));
        this.logger.info("Server initialized with port: " + port);
        if (getSsl() != null && getSsl().isEnabled()) {
            SslContextFactory sslContextFactory = new SslContextFactory();
            configureSsl(sslContextFactory, getSsl());
            server.setConnectors(new Connector[]{getSslServerConnectorFactory().mo7444createConnector(server, sslContextFactory, address)});
        }
        for (JettyServerCustomizer customizer : getServerCustomizers()) {
            customizer.customize(server);
        }
        if (this.useForwardHeaders) {
            new ForwardHeadersCustomizer().customize(server);
        }
        return getJettyEmbeddedServletContainer(server);
    }

    private Server createServer(InetSocketAddress address) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Server server;
        if (ClassUtils.hasConstructor(Server.class, ThreadPool.class)) {
            server = new Jetty9ServerFactory().createServer(getThreadPool());
        } else {
            server = new Jetty8ServerFactory().createServer(getThreadPool());
        }
        server.setConnectors(new Connector[]{createConnector(address, server)});
        return server;
    }

    private AbstractConnector createConnector(InetSocketAddress address, Server server) {
        if (ClassUtils.isPresent(CONNECTOR_JETTY_8, getClass().getClassLoader())) {
            return new Jetty8ConnectorFactory().createConnector(server, address, this.acceptors, this.selectors);
        }
        return new Jetty9ConnectorFactory().createConnector(server, address, this.acceptors, this.selectors);
    }

    private Handler addHandlerWrappers(Handler handler) {
        if (getCompression() != null && getCompression().getEnabled()) {
            handler = applyWrapper(handler, createGzipHandler());
        }
        if (StringUtils.hasText(getServerHeader())) {
            handler = applyWrapper(handler, new ServerHeaderHandler(getServerHeader()));
        }
        return handler;
    }

    private Handler applyWrapper(Handler handler, HandlerWrapper wrapper) {
        wrapper.setHandler(handler);
        return wrapper;
    }

    private HandlerWrapper createGzipHandler() {
        ClassLoader classLoader = getClass().getClassLoader();
        if (ClassUtils.isPresent(GZIP_HANDLER_JETTY_9_2, classLoader)) {
            return new Jetty92GzipHandlerFactory().createGzipHandler(getCompression());
        }
        if (ClassUtils.isPresent(GZIP_HANDLER_JETTY_8, getClass().getClassLoader())) {
            return new Jetty8GzipHandlerFactory().createGzipHandler(getCompression());
        }
        if (ClassUtils.isPresent(GZIP_HANDLER_JETTY_9_3, getClass().getClassLoader())) {
            return new Jetty93GzipHandlerFactory().createGzipHandler(getCompression());
        }
        throw new IllegalStateException("Compression is enabled, but GzipHandler is not on the classpath");
    }

    private SslServerConnectorFactory getSslServerConnectorFactory() {
        if (ClassUtils.isPresent("org.eclipse.jetty.server.ssl.SslSocketConnector", null)) {
            return new Jetty8SslServerConnectorFactory();
        }
        return new Jetty9SslServerConnectorFactory();
    }

    protected void configureSsl(SslContextFactory factory, Ssl ssl) {
        factory.setProtocol(ssl.getProtocol());
        configureSslClientAuth(factory, ssl);
        configureSslPasswords(factory, ssl);
        factory.setCertAlias(ssl.getKeyAlias());
        if (!ObjectUtils.isEmpty((Object[]) ssl.getCiphers())) {
            factory.setIncludeCipherSuites(ssl.getCiphers());
            factory.setExcludeCipherSuites(new String[0]);
        }
        if (ssl.getEnabledProtocols() != null) {
            factory.setIncludeProtocols(ssl.getEnabledProtocols());
        }
        if (getSslStoreProvider() != null) {
            try {
                factory.setKeyStore(getSslStoreProvider().getKeyStore());
                factory.setTrustStore(getSslStoreProvider().getTrustStore());
                return;
            } catch (Exception ex) {
                throw new IllegalStateException("Unable to set SSL store", ex);
            }
        }
        configureSslKeyStore(factory, ssl);
        configureSslTrustStore(factory, ssl);
    }

    private void configureSslClientAuth(SslContextFactory factory, Ssl ssl) {
        if (ssl.getClientAuth() == Ssl.ClientAuth.NEED) {
            factory.setNeedClientAuth(true);
            factory.setWantClientAuth(true);
        } else if (ssl.getClientAuth() == Ssl.ClientAuth.WANT) {
            factory.setWantClientAuth(true);
        }
    }

    private void configureSslPasswords(SslContextFactory factory, Ssl ssl) {
        if (ssl.getKeyStorePassword() != null) {
            factory.setKeyStorePassword(ssl.getKeyStorePassword());
        }
        if (ssl.getKeyPassword() != null) {
            factory.setKeyManagerPassword(ssl.getKeyPassword());
        }
    }

    private void configureSslKeyStore(SslContextFactory factory, Ssl ssl) {
        try {
            URL url = ResourceUtils.getURL(ssl.getKeyStore());
            factory.setKeyStoreResource(Resource.newResource(url));
            if (ssl.getKeyStoreType() != null) {
                factory.setKeyStoreType(ssl.getKeyStoreType());
            }
            if (ssl.getKeyStoreProvider() != null) {
                factory.setKeyStoreProvider(ssl.getKeyStoreProvider());
            }
        } catch (IOException ex) {
            throw new EmbeddedServletContainerException("Could not find key store '" + ssl.getKeyStore() + "'", ex);
        }
    }

    private void configureSslTrustStore(SslContextFactory factory, Ssl ssl) {
        if (ssl.getTrustStorePassword() != null) {
            factory.setTrustStorePassword(ssl.getTrustStorePassword());
        }
        if (ssl.getTrustStore() != null) {
            try {
                URL url = ResourceUtils.getURL(ssl.getTrustStore());
                factory.setTrustStoreResource(Resource.newResource(url));
            } catch (IOException ex) {
                throw new EmbeddedServletContainerException("Could not find trust store '" + ssl.getTrustStore() + "'", ex);
            }
        }
        if (ssl.getTrustStoreType() != null) {
            factory.setTrustStoreType(ssl.getTrustStoreType());
        }
        if (ssl.getTrustStoreProvider() != null) {
            factory.setTrustStoreProvider(ssl.getTrustStoreProvider());
        }
    }

    protected final void configureWebAppContext(WebAppContext context, ServletContextInitializer... initializers) {
        Assert.notNull(context, "Context must not be null");
        context.setTempDirectory(getTempDirectory());
        if (this.resourceLoader != null) {
            context.setClassLoader(this.resourceLoader.getClassLoader());
        }
        String contextPath = getContextPath();
        context.setContextPath(StringUtils.hasLength(contextPath) ? contextPath : "/");
        context.setDisplayName(getDisplayName());
        configureDocumentRoot(context);
        if (isRegisterDefaultServlet()) {
            addDefaultServlet(context);
        }
        if (shouldRegisterJspServlet()) {
            addJspServlet(context);
            context.addBean(new JasperInitializer(context), true);
        }
        addLocaleMappings(context);
        ServletContextInitializer[] initializersToUse = mergeInitializers(initializers);
        Configuration[] configurations = getWebAppContextConfigurations(context, initializersToUse);
        context.setConfigurations(configurations);
        context.setThrowUnavailableOnStartupException(true);
        configureSession(context);
        postProcessWebAppContext(context);
    }

    private void configureSession(WebAppContext context) {
        SessionConfigurer configurer = getSessionConfigurer();
        configurer.configure(context, getSessionTimeout(), isPersistSession(), new SessionDirectory() { // from class: org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.1
            @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.SessionDirectory
            public File get() {
                return JettyEmbeddedServletContainerFactory.this.getValidSessionStoreDir();
            }
        });
    }

    private void addLocaleMappings(WebAppContext context) {
        for (Map.Entry<Locale, Charset> entry : getLocaleCharsetMappings().entrySet()) {
            Locale locale = entry.getKey();
            Charset charset = entry.getValue();
            context.addLocaleEncoding(locale.toString(), charset.toString());
        }
    }

    private SessionConfigurer getSessionConfigurer() {
        if (ClassUtils.isPresent(SESSION_JETTY_9_3, getClass().getClassLoader())) {
            return new Jetty93SessionConfigurer();
        }
        return new Jetty94SessionConfigurer();
    }

    private File getTempDirectory() {
        String temp = System.getProperty(TempFile.JAVA_IO_TMPDIR);
        if (temp != null) {
            return new File(temp);
        }
        return null;
    }

    private void configureDocumentRoot(WebAppContext handler) {
        File root = getValidDocumentRoot();
        File root2 = root != null ? root : createTempDir("jetty-docbase");
        try {
            List<Resource> resources = new ArrayList<>();
            resources.add(root2.isDirectory() ? Resource.newResource(root2.getCanonicalFile()) : JarResource.newJarResource(Resource.newResource(root2)));
            for (URL resourceJarUrl : getUrlsOfJarsWithMetaInfResources()) {
                Resource resource = createResource(resourceJarUrl);
                if (resource.exists() && resource.isDirectory()) {
                    resources.add(resource);
                }
            }
            handler.setBaseResource(new ResourceCollection((Resource[]) resources.toArray(new Resource[resources.size()])));
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private Resource createResource(URL url) throws Exception {
        if ("file".equals(url.getProtocol())) {
            File file = new File(url.toURI());
            if (file.isFile()) {
                return Resource.newResource(ResourceUtils.JAR_URL_PREFIX + url + "!/META-INF/resources");
            }
        }
        return Resource.newResource(url + "META-INF/resources");
    }

    protected final void addDefaultServlet(WebAppContext context) {
        Assert.notNull(context, "Context must not be null");
        ServletHolder holder = new ServletHolder();
        holder.setName("default");
        holder.setClassName("org.eclipse.jetty.servlet.DefaultServlet");
        holder.setInitParameter("dirAllowed", "false");
        holder.setInitOrder(1);
        context.getServletHandler().addServletWithMapping(holder, "/");
        context.getServletHandler().getServletMapping("/").setDefault(true);
    }

    protected final void addJspServlet(WebAppContext context) {
        Assert.notNull(context, "Context must not be null");
        ServletHolder holder = new ServletHolder();
        holder.setName("jsp");
        holder.setClassName(getJspServlet().getClassName());
        holder.setInitParameter("fork", "false");
        holder.setInitParameters(getJspServlet().getInitParameters());
        holder.setInitOrder(3);
        context.getServletHandler().addServlet(holder);
        ServletMapping mapping = new ServletMapping();
        mapping.setServletName("jsp");
        mapping.setPathSpecs(new String[]{"*.jsp", "*.jspx"});
        context.getServletHandler().addServletMapping(mapping);
    }

    protected Configuration[] getWebAppContextConfigurations(WebAppContext webAppContext, ServletContextInitializer... initializers) {
        List<Configuration> configurations = new ArrayList<>();
        configurations.add(getServletContextInitializerConfiguration(webAppContext, initializers));
        configurations.addAll(getConfigurations());
        configurations.add(getErrorPageConfiguration());
        configurations.add(getMimeTypeConfiguration());
        return (Configuration[]) configurations.toArray(new Configuration[configurations.size()]);
    }

    private Configuration getErrorPageConfiguration() {
        return new AbstractConfiguration() { // from class: org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.2
            public void configure(WebAppContext context) throws Exception {
                ErrorHandler errorHandler = context.getErrorHandler();
                context.setErrorHandler(new JettyEmbeddedErrorHandler(errorHandler));
                JettyEmbeddedServletContainerFactory.this.addJettyErrorPages(errorHandler, JettyEmbeddedServletContainerFactory.this.getErrorPages());
            }
        };
    }

    private Configuration getMimeTypeConfiguration() {
        return new AbstractConfiguration() { // from class: org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.3
            public void configure(WebAppContext context) throws Exception {
                MimeTypes mimeTypes = context.getMimeTypes();
                Iterator<MimeMappings.Mapping> it = JettyEmbeddedServletContainerFactory.this.getMimeMappings().iterator();
                while (it.hasNext()) {
                    MimeMappings.Mapping mapping = it.next();
                    mimeTypes.addMimeMapping(mapping.getExtension(), mapping.getMimeType());
                }
            }
        };
    }

    protected Configuration getServletContextInitializerConfiguration(WebAppContext webAppContext, ServletContextInitializer... initializers) {
        return new ServletContextInitializerConfiguration(initializers);
    }

    protected void postProcessWebAppContext(WebAppContext webAppContext) {
    }

    protected JettyEmbeddedServletContainer getJettyEmbeddedServletContainer(Server server) {
        return new JettyEmbeddedServletContainer(server, getPort() >= 0);
    }

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setUseForwardHeaders(boolean useForwardHeaders) {
        this.useForwardHeaders = useForwardHeaders;
    }

    public void setAcceptors(int acceptors) {
        this.acceptors = acceptors;
    }

    public void setSelectors(int selectors) {
        this.selectors = selectors;
    }

    public void setServerCustomizers(Collection<? extends JettyServerCustomizer> customizers) {
        Assert.notNull(customizers, "Customizers must not be null");
        this.jettyServerCustomizers = new ArrayList(customizers);
    }

    public Collection<JettyServerCustomizer> getServerCustomizers() {
        return this.jettyServerCustomizers;
    }

    public void addServerCustomizers(JettyServerCustomizer... customizers) {
        Assert.notNull(customizers, "Customizers must not be null");
        this.jettyServerCustomizers.addAll(Arrays.asList(customizers));
    }

    public void setConfigurations(Collection<? extends Configuration> configurations) {
        Assert.notNull(configurations, "Configurations must not be null");
        this.configurations = new ArrayList(configurations);
    }

    public Collection<Configuration> getConfigurations() {
        return this.configurations;
    }

    public void addConfigurations(Configuration... configurations) {
        Assert.notNull(configurations, "Configurations must not be null");
        this.configurations.addAll(Arrays.asList(configurations));
    }

    public ThreadPool getThreadPool() {
        return this.threadPool;
    }

    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addJettyErrorPages(ErrorHandler errorHandler, Collection<ErrorPage> errorPages) {
        if (errorHandler instanceof ErrorPageErrorHandler) {
            ErrorPageErrorHandler handler = (ErrorPageErrorHandler) errorHandler;
            for (ErrorPage errorPage : errorPages) {
                if (errorPage.isGlobal()) {
                    handler.addErrorPage("org.eclipse.jetty.server.error_page.global", errorPage.getPath());
                } else if (errorPage.getExceptionName() != null) {
                    handler.addErrorPage(errorPage.getExceptionName(), errorPage.getPath());
                } else {
                    handler.addErrorPage(errorPage.getStatusCode(), errorPage.getPath());
                }
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty9SslServerConnectorFactory.class */
    private static class Jetty9SslServerConnectorFactory implements SslServerConnectorFactory {
        private Jetty9SslServerConnectorFactory() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.SslServerConnectorFactory
        /* renamed from: createConnector, reason: merged with bridge method [inline-methods] */
        public ServerConnector mo7444createConnector(Server server, SslContextFactory sslContextFactory, InetSocketAddress address) {
            HttpConfiguration config = new HttpConfiguration();
            config.setSendServerVersion(false);
            config.addCustomizer(new SecureRequestCustomizer());
            ServerConnector serverConnector = new ServerConnector(server, new ConnectionFactory[]{new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()), new HttpConnectionFactory(config)});
            serverConnector.setPort(address.getPort());
            serverConnector.setHost(address.getHostString());
            return serverConnector;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty8SslServerConnectorFactory.class */
    private static class Jetty8SslServerConnectorFactory implements SslServerConnectorFactory {
        private Jetty8SslServerConnectorFactory() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.SslServerConnectorFactory
        /* renamed from: createConnector */
        public AbstractConnector mo7444createConnector(Server server, SslContextFactory sslContextFactory, InetSocketAddress address) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
            try {
                Class<?> connectorClass = Class.forName("org.eclipse.jetty.server.ssl.SslSocketConnector");
                AbstractConnector connector = (AbstractConnector) connectorClass.getConstructor(SslContextFactory.class).newInstance(sslContextFactory);
                connector.getClass().getMethod("setPort", Integer.TYPE).invoke(connector, Integer.valueOf(address.getPort()));
                connector.getClass().getMethod("setHost", String.class).invoke(connector, address.getHostString());
                return connector;
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty8GzipHandlerFactory.class */
    private static class Jetty8GzipHandlerFactory implements GzipHandlerFactory {
        private Jetty8GzipHandlerFactory() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.GzipHandlerFactory
        public HandlerWrapper createGzipHandler(Compression compression) throws IllegalAccessException, LinkageError, IllegalArgumentException, InvocationTargetException {
            try {
                Class<?> handlerClass = ClassUtils.forName(JettyEmbeddedServletContainerFactory.GZIP_HANDLER_JETTY_8, getClass().getClassLoader());
                HandlerWrapper handler = (HandlerWrapper) handlerClass.newInstance();
                ReflectionUtils.findMethod(handlerClass, "setMinGzipSize", Integer.TYPE).invoke(handler, Integer.valueOf(compression.getMinResponseSize()));
                ReflectionUtils.findMethod(handlerClass, "setMimeTypes", Set.class).invoke(handler, new HashSet(Arrays.asList(compression.getMimeTypes())));
                if (compression.getExcludedUserAgents() != null) {
                    ReflectionUtils.findMethod(handlerClass, "setExcluded", Set.class).invoke(handler, new HashSet(Arrays.asList(compression.getExcludedUserAgents())));
                }
                return handler;
            } catch (Exception ex) {
                throw new RuntimeException("Failed to configure Jetty 8 gzip handler", ex);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty92GzipHandlerFactory.class */
    private static class Jetty92GzipHandlerFactory implements GzipHandlerFactory {
        private Jetty92GzipHandlerFactory() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.GzipHandlerFactory
        public HandlerWrapper createGzipHandler(Compression compression) throws IllegalAccessException, LinkageError, IllegalArgumentException, InvocationTargetException {
            try {
                Class<?> handlerClass = ClassUtils.forName(JettyEmbeddedServletContainerFactory.GZIP_HANDLER_JETTY_9_2, getClass().getClassLoader());
                HandlerWrapper gzipHandler = (HandlerWrapper) handlerClass.newInstance();
                ReflectionUtils.findMethod(handlerClass, "setMinGzipSize", Integer.TYPE).invoke(gzipHandler, Integer.valueOf(compression.getMinResponseSize()));
                ReflectionUtils.findMethod(handlerClass, "addIncludedMimeTypes", String[].class).invoke(gzipHandler, compression.getMimeTypes());
                if (compression.getExcludedUserAgents() != null) {
                    ReflectionUtils.findMethod(handlerClass, "setExcluded", Set.class).invoke(gzipHandler, new HashSet(Arrays.asList(compression.getExcludedUserAgents())));
                }
                return gzipHandler;
            } catch (Exception ex) {
                throw new RuntimeException("Failed to configure Jetty 9.2 gzip handler", ex);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty93GzipHandlerFactory.class */
    private static class Jetty93GzipHandlerFactory implements GzipHandlerFactory {
        private Jetty93GzipHandlerFactory() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.GzipHandlerFactory
        public HandlerWrapper createGzipHandler(Compression compression) {
            GzipHandler handler = new GzipHandler();
            handler.setMinGzipSize(compression.getMinResponseSize());
            handler.setIncludedMimeTypes(compression.getMimeTypes());
            if (compression.getExcludedUserAgents() != null) {
                handler.setExcludedAgentPatterns(compression.getExcludedUserAgents());
            }
            return handler;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$ForwardHeadersCustomizer.class */
    private static class ForwardHeadersCustomizer implements JettyServerCustomizer {
        private ForwardHeadersCustomizer() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyServerCustomizer
        public void customize(Server server) {
            ForwardedRequestCustomizer customizer = new ForwardedRequestCustomizer();
            for (Connector connector : server.getConnectors()) {
                for (HttpConfiguration.ConnectionFactory connectionFactory : connector.getConnectionFactories()) {
                    if (connectionFactory instanceof HttpConfiguration.ConnectionFactory) {
                        connectionFactory.getHttpConfiguration().addCustomizer(customizer);
                    }
                }
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$ServerHeaderHandler.class */
    private static class ServerHeaderHandler extends HandlerWrapper {
        private static final String SERVER_HEADER = "server";
        private final String value;

        ServerHeaderHandler(String value) {
            this.value = value;
        }

        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            if (!response.getHeaderNames().contains(SERVER_HEADER)) {
                response.setHeader(SERVER_HEADER, this.value);
            }
            super.handle(target, baseRequest, request, response);
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty8ConnectorFactory.class */
    private static class Jetty8ConnectorFactory implements ConnectorFactory {
        private Jetty8ConnectorFactory() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.ConnectorFactory
        public AbstractConnector createConnector(Server server, InetSocketAddress address, int acceptors, int selectors) throws IllegalAccessException, LinkageError, IllegalArgumentException, InvocationTargetException {
            try {
                Class<?> connectorClass = ClassUtils.forName(JettyEmbeddedServletContainerFactory.CONNECTOR_JETTY_8, getClass().getClassLoader());
                AbstractConnector connector = (AbstractConnector) connectorClass.newInstance();
                ReflectionUtils.findMethod(connectorClass, "setPort", Integer.TYPE).invoke(connector, Integer.valueOf(address.getPort()));
                ReflectionUtils.findMethod(connectorClass, "setHost", String.class).invoke(connector, address.getHostString());
                if (acceptors > 0) {
                    ReflectionUtils.findMethod(connectorClass, "setAcceptors", Integer.TYPE).invoke(connector, Integer.valueOf(acceptors));
                }
                if (selectors > 0) {
                    Object selectorManager = ReflectionUtils.findMethod(connectorClass, "getSelectorManager").invoke(connector, new Object[0]);
                    ReflectionUtils.findMethod(selectorManager.getClass(), "setSelectSets", Integer.TYPE).invoke(selectorManager, Integer.valueOf(selectors));
                }
                return connector;
            } catch (Exception ex) {
                throw new RuntimeException("Failed to configure Jetty 8 connector", ex);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty9ConnectorFactory.class */
    private static class Jetty9ConnectorFactory implements ConnectorFactory {
        private Jetty9ConnectorFactory() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.ConnectorFactory
        public AbstractConnector createConnector(Server server, InetSocketAddress address, int acceptors, int selectors) {
            ServerConnector connector = new ServerConnector(server, acceptors, selectors);
            connector.setHost(address.getHostString());
            connector.setPort(address.getPort());
            for (HttpConfiguration.ConnectionFactory connectionFactory : connector.getConnectionFactories()) {
                if (connectionFactory instanceof HttpConfiguration.ConnectionFactory) {
                    connectionFactory.getHttpConfiguration().setSendServerVersion(false);
                }
            }
            return connector;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty8ServerFactory.class */
    private static class Jetty8ServerFactory implements ServerFactory {
        private Jetty8ServerFactory() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.ServerFactory
        public Server createServer(ThreadPool threadPool) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Server server = new Server();
            try {
                ReflectionUtils.findMethod(Server.class, "setThreadPool", ThreadPool.class).invoke(server, threadPool);
                try {
                    ReflectionUtils.findMethod(Server.class, "setSendServerVersion", Boolean.TYPE).invoke(server, false);
                    return server;
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to disable Server header", ex);
                }
            } catch (Exception ex2) {
                throw new RuntimeException("Failed to configure Jetty 8 ThreadPool", ex2);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty9ServerFactory.class */
    private static class Jetty9ServerFactory implements ServerFactory {
        private Jetty9ServerFactory() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.ServerFactory
        public Server createServer(ThreadPool threadPool) {
            return new Server(threadPool);
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty93SessionConfigurer.class */
    private static class Jetty93SessionConfigurer implements SessionConfigurer {
        private Jetty93SessionConfigurer() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.SessionConfigurer
        public void configure(WebAppContext context, int timeout, boolean persist, SessionDirectory sessionDirectory) throws IllegalArgumentException {
            SessionHandler handler = context.getSessionHandler();
            Object manager = getSessionManager(handler);
            setMaxInactiveInterval(manager, timeout > 0 ? timeout : -1);
            if (persist) {
                Class<?> hashSessionManagerClass = ClassUtils.resolveClassName(JettyEmbeddedServletContainerFactory.SESSION_JETTY_9_3, handler.getClass().getClassLoader());
                Assert.isInstanceOf(hashSessionManagerClass, manager, "Unable to use persistent sessions");
                configurePersistSession(manager, sessionDirectory);
            }
        }

        private Object getSessionManager(SessionHandler handler) {
            Method method = ReflectionUtils.findMethod(SessionHandler.class, "getSessionManager");
            return ReflectionUtils.invokeMethod(method, handler);
        }

        private void setMaxInactiveInterval(Object manager, int interval) {
            Method method = ReflectionUtils.findMethod(manager.getClass(), "setMaxInactiveInterval", Integer.TYPE);
            ReflectionUtils.invokeMethod(method, manager, Integer.valueOf(interval));
        }

        private void configurePersistSession(Object manager, SessionDirectory sessionDirectory) {
            try {
                setStoreDirectory(manager, sessionDirectory.get());
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }

        private void setStoreDirectory(Object manager, File file) throws IOException {
            Method method = ReflectionUtils.findMethod(manager.getClass(), "setStoreDirectory", File.class);
            ReflectionUtils.invokeMethod(method, manager, file);
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainerFactory$Jetty94SessionConfigurer.class */
    private static class Jetty94SessionConfigurer implements SessionConfigurer {
        private Jetty94SessionConfigurer() {
        }

        @Override // org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory.SessionConfigurer
        public void configure(WebAppContext context, int timeout, boolean persist, SessionDirectory sessionDirectory) {
            SessionHandler handler = context.getSessionHandler();
            handler.setMaxInactiveInterval(timeout > 0 ? timeout : -1);
            if (persist) {
                DefaultSessionCache cache = new DefaultSessionCache(handler);
                FileSessionDataStore store = new FileSessionDataStore();
                store.setStoreDir(sessionDirectory.get());
                cache.setSessionDataStore(store);
                handler.setSessionCache(cache);
            }
        }
    }
}
