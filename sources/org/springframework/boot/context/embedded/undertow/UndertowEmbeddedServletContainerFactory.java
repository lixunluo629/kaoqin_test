package org.springframework.boot.context.embedded.undertow;

import ch.qos.logback.core.net.ssl.SSL;
import io.undertow.Undertow;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;
import io.undertow.server.handlers.accesslog.DefaultAccessLogReceiver;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.server.handlers.resource.URLResource;
import io.undertow.server.session.SessionManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import io.undertow.servlet.api.MimeMapping;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.servlet.api.ServletStackTraces;
import io.undertow.servlet.handlers.DefaultServlet;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import org.apache.catalina.valves.Constants;
import org.springframework.boot.context.embedded.AbstractEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.Sequence;
import org.xnio.SslClientAuthMode;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowEmbeddedServletContainerFactory.class */
public class UndertowEmbeddedServletContainerFactory extends AbstractEmbeddedServletContainerFactory implements ResourceLoaderAware {
    private static final Set<Class<?>> NO_CLASSES = Collections.emptySet();
    private List<UndertowBuilderCustomizer> builderCustomizers;
    private List<UndertowDeploymentInfoCustomizer> deploymentInfoCustomizers;
    private ResourceLoader resourceLoader;
    private Integer bufferSize;
    private Integer ioThreads;
    private Integer workerThreads;
    private Boolean directBuffers;
    private File accessLogDirectory;
    private String accessLogPattern;
    private String accessLogPrefix;
    private String accessLogSuffix;
    private boolean accessLogEnabled;
    private boolean accessLogRotate;
    private boolean useForwardHeaders;

    public UndertowEmbeddedServletContainerFactory() {
        this.builderCustomizers = new ArrayList();
        this.deploymentInfoCustomizers = new ArrayList();
        this.accessLogEnabled = false;
        this.accessLogRotate = true;
        getJspServlet().setRegistered(false);
    }

    public UndertowEmbeddedServletContainerFactory(int port) {
        super(port);
        this.builderCustomizers = new ArrayList();
        this.deploymentInfoCustomizers = new ArrayList();
        this.accessLogEnabled = false;
        this.accessLogRotate = true;
        getJspServlet().setRegistered(false);
    }

    public UndertowEmbeddedServletContainerFactory(String contextPath, int port) {
        super(contextPath, port);
        this.builderCustomizers = new ArrayList();
        this.deploymentInfoCustomizers = new ArrayList();
        this.accessLogEnabled = false;
        this.accessLogRotate = true;
        getJspServlet().setRegistered(false);
    }

    public void setBuilderCustomizers(Collection<? extends UndertowBuilderCustomizer> customizers) {
        Assert.notNull(customizers, "Customizers must not be null");
        this.builderCustomizers = new ArrayList(customizers);
    }

    public Collection<UndertowBuilderCustomizer> getBuilderCustomizers() {
        return this.builderCustomizers;
    }

    public void addBuilderCustomizers(UndertowBuilderCustomizer... customizers) {
        Assert.notNull(customizers, "Customizers must not be null");
        this.builderCustomizers.addAll(Arrays.asList(customizers));
    }

    public void setDeploymentInfoCustomizers(Collection<? extends UndertowDeploymentInfoCustomizer> customizers) {
        Assert.notNull(customizers, "Customizers must not be null");
        this.deploymentInfoCustomizers = new ArrayList(customizers);
    }

    public Collection<UndertowDeploymentInfoCustomizer> getDeploymentInfoCustomizers() {
        return this.deploymentInfoCustomizers;
    }

    public void addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer... customizers) {
        Assert.notNull(customizers, "UndertowDeploymentInfoCustomizers must not be null");
        this.deploymentInfoCustomizers.addAll(Arrays.asList(customizers));
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainerFactory
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers) throws NoSuchAlgorithmException, KeyManagementException {
        DeploymentManager manager = createDeploymentManager(initializers);
        int port = getPort();
        Undertow.Builder builder = createBuilder(port);
        return getUndertowEmbeddedServletContainer(builder, manager, port);
    }

    private Undertow.Builder createBuilder(int port) throws NoSuchAlgorithmException, KeyManagementException {
        Undertow.Builder builder = Undertow.builder();
        if (this.bufferSize != null) {
            builder.setBufferSize(this.bufferSize.intValue());
        }
        if (this.ioThreads != null) {
            builder.setIoThreads(this.ioThreads.intValue());
        }
        if (this.workerThreads != null) {
            builder.setWorkerThreads(this.workerThreads.intValue());
        }
        if (this.directBuffers != null) {
            builder.setDirectBuffers(this.directBuffers.booleanValue());
        }
        if (getSsl() != null && getSsl().isEnabled()) {
            configureSsl(getSsl(), port, builder);
        } else {
            builder.addHttpListener(port, getListenAddress());
        }
        for (UndertowBuilderCustomizer customizer : this.builderCustomizers) {
            customizer.customize(builder);
        }
        return builder;
    }

    private void configureSsl(Ssl ssl, int port, Undertow.Builder builder) throws NoSuchAlgorithmException, KeyManagementException {
        try {
            SSLContext sslContext = SSLContext.getInstance(ssl.getProtocol());
            sslContext.init(getKeyManagers(), getTrustManagers(), null);
            builder.addHttpsListener(port, getListenAddress(), sslContext);
            builder.setSocketOption(Options.SSL_CLIENT_AUTH_MODE, getSslClientAuthMode(ssl));
            if (ssl.getEnabledProtocols() != null) {
                builder.setSocketOption(Options.SSL_ENABLED_PROTOCOLS, Sequence.of(ssl.getEnabledProtocols()));
            }
            if (ssl.getCiphers() != null) {
                builder.setSocketOption(Options.SSL_ENABLED_CIPHER_SUITES, Sequence.of(ssl.getCiphers()));
            }
        } catch (KeyManagementException ex) {
            throw new IllegalStateException(ex);
        } catch (NoSuchAlgorithmException ex2) {
            throw new IllegalStateException(ex2);
        }
    }

    private String getListenAddress() {
        if (getAddress() == null) {
            return "0.0.0.0";
        }
        return getAddress().getHostAddress();
    }

    private SslClientAuthMode getSslClientAuthMode(Ssl ssl) {
        if (ssl.getClientAuth() == Ssl.ClientAuth.NEED) {
            return SslClientAuthMode.REQUIRED;
        }
        if (ssl.getClientAuth() == Ssl.ClientAuth.WANT) {
            return SslClientAuthMode.REQUESTED;
        }
        return SslClientAuthMode.NOT_REQUESTED;
    }

    private KeyManager[] getKeyManagers() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        try {
            KeyStore keyStore = getKeyStore();
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            Ssl ssl = getSsl();
            char[] keyPassword = ssl.getKeyPassword() != null ? ssl.getKeyPassword().toCharArray() : null;
            if (keyPassword == null && ssl.getKeyStorePassword() != null) {
                keyPassword = ssl.getKeyStorePassword().toCharArray();
            }
            keyManagerFactory.init(keyStore, keyPassword);
            if (ssl.getKeyAlias() != null) {
                return getConfigurableAliasKeyManagers(ssl, keyManagerFactory.getKeyManagers());
            }
            return keyManagerFactory.getKeyManagers();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private KeyManager[] getConfigurableAliasKeyManagers(Ssl ssl, KeyManager[] keyManagers) {
        for (int i = 0; i < keyManagers.length; i++) {
            if (keyManagers[i] instanceof X509ExtendedKeyManager) {
                keyManagers[i] = new ConfigurableAliasKeyManager((X509ExtendedKeyManager) keyManagers[i], ssl.getKeyAlias());
            }
        }
        return keyManagers;
    }

    private KeyStore getKeyStore() throws Exception {
        if (getSslStoreProvider() != null) {
            return getSslStoreProvider().getKeyStore();
        }
        Ssl ssl = getSsl();
        return loadKeyStore(ssl.getKeyStoreType(), ssl.getKeyStoreProvider(), ssl.getKeyStore(), ssl.getKeyStorePassword());
    }

    private TrustManager[] getTrustManagers() throws NoSuchAlgorithmException, KeyStoreException {
        try {
            KeyStore store = getTrustStore();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(store);
            return trustManagerFactory.getTrustManagers();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private KeyStore getTrustStore() throws Exception {
        if (getSslStoreProvider() != null) {
            return getSslStoreProvider().getTrustStore();
        }
        Ssl ssl = getSsl();
        return loadKeyStore(ssl.getTrustStoreType(), ssl.getTrustStoreProvider(), ssl.getTrustStore(), ssl.getTrustStorePassword());
    }

    private KeyStore loadKeyStore(String type, String provider, String resource, String password) throws Exception {
        String type2 = type != null ? type : SSL.DEFAULT_KEYSTORE_TYPE;
        if (resource == null) {
            return null;
        }
        KeyStore store = provider != null ? KeyStore.getInstance(type2, provider) : KeyStore.getInstance(type2);
        URL url = ResourceUtils.getURL(resource);
        store.load(url.openStream(), password != null ? password.toCharArray() : null);
        return store;
    }

    private DeploymentManager createDeploymentManager(ServletContextInitializer... initializers) {
        DeploymentInfo deployment = Servlets.deployment();
        registerServletContainerInitializerToDriveServletContextInitializers(deployment, initializers);
        deployment.setClassLoader(getServletClassLoader());
        deployment.setContextPath(getContextPath());
        deployment.setDisplayName(getDisplayName());
        deployment.setDeploymentName("spring-boot");
        if (isRegisterDefaultServlet()) {
            deployment.addServlet(Servlets.servlet("default", DefaultServlet.class));
        }
        configureErrorPages(deployment);
        deployment.setServletStackTraces(ServletStackTraces.NONE);
        deployment.setResourceManager(getDocumentRootResourceManager());
        configureMimeMappings(deployment);
        for (UndertowDeploymentInfoCustomizer customizer : this.deploymentInfoCustomizers) {
            customizer.customize(deployment);
        }
        if (isAccessLogEnabled()) {
            configureAccessLog(deployment);
        }
        if (isPersistSession()) {
            File dir = getValidSessionStoreDir();
            deployment.setSessionPersistenceManager(new FileSessionPersistence(dir));
        }
        addLocaleMappings(deployment);
        DeploymentManager manager = Servlets.newContainer().addDeployment(deployment);
        manager.deploy();
        SessionManager sessionManager = manager.getDeployment().getSessionManager();
        int sessionTimeout = getSessionTimeout() > 0 ? getSessionTimeout() : -1;
        sessionManager.setDefaultSessionTimeout(sessionTimeout);
        return manager;
    }

    private void configureAccessLog(DeploymentInfo deploymentInfo) {
        try {
            createAccessLogDirectoryIfNecessary();
            XnioWorker worker = createWorker();
            String prefix = this.accessLogPrefix != null ? this.accessLogPrefix : "access_log.";
            final DefaultAccessLogReceiver accessLogReceiver = new DefaultAccessLogReceiver(worker, this.accessLogDirectory, prefix, this.accessLogSuffix, this.accessLogRotate);
            EventListener listener = new AccessLogShutdownListener(worker, accessLogReceiver);
            deploymentInfo.addListener(new ListenerInfo(AccessLogShutdownListener.class, new ImmediateInstanceFactory(listener)));
            deploymentInfo.addInitialHandlerChainWrapper(new HandlerWrapper() { // from class: org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory.1
                public HttpHandler wrap(HttpHandler handler) {
                    return UndertowEmbeddedServletContainerFactory.this.createAccessLogHandler(handler, accessLogReceiver);
                }
            });
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to create AccessLogHandler", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AccessLogHandler createAccessLogHandler(HttpHandler handler, AccessLogReceiver accessLogReceiver) {
        createAccessLogDirectoryIfNecessary();
        String formatString = this.accessLogPattern != null ? this.accessLogPattern : Constants.AccessLog.COMMON_ALIAS;
        return new AccessLogHandler(handler, accessLogReceiver, formatString, Undertow.class.getClassLoader());
    }

    private void createAccessLogDirectoryIfNecessary() {
        Assert.state(this.accessLogDirectory != null, "Access log directory is not set");
        if (!this.accessLogDirectory.isDirectory() && !this.accessLogDirectory.mkdirs()) {
            throw new IllegalStateException("Failed to create access log directory '" + this.accessLogDirectory + "'");
        }
    }

    private XnioWorker createWorker() throws IOException {
        Xnio xnio = Xnio.getInstance(Undertow.class.getClassLoader());
        return xnio.createWorker(OptionMap.builder().set(Options.THREAD_DAEMON, true).getMap());
    }

    private void addLocaleMappings(DeploymentInfo deployment) {
        for (Map.Entry<Locale, Charset> entry : getLocaleCharsetMappings().entrySet()) {
            Locale locale = entry.getKey();
            Charset charset = entry.getValue();
            deployment.addLocaleCharsetMapping(locale.toString(), charset.toString());
        }
    }

    private void registerServletContainerInitializerToDriveServletContextInitializers(DeploymentInfo deployment, ServletContextInitializer... initializers) {
        ServletContextInitializer[] mergedInitializers = mergeInitializers(initializers);
        Initializer initializer = new Initializer(mergedInitializers);
        deployment.addServletContainerInitializer(new ServletContainerInitializerInfo(Initializer.class, new ImmediateInstanceFactory(initializer), NO_CLASSES));
    }

    private ClassLoader getServletClassLoader() {
        if (this.resourceLoader != null) {
            return this.resourceLoader.getClassLoader();
        }
        return getClass().getClassLoader();
    }

    private ResourceManager getDocumentRootResourceManager() {
        File root = getCanonicalDocumentRoot();
        List<URL> metaInfResourceUrls = getUrlsOfJarsWithMetaInfResources();
        List<URL> resourceJarUrls = new ArrayList<>();
        List<ResourceManager> resourceManagers = new ArrayList<>();
        resourceManagers.add(root.isDirectory() ? new FileResourceManager(root, 0L) : new JarResourceManager(root));
        for (URL url : metaInfResourceUrls) {
            if ("file".equals(url.getProtocol())) {
                try {
                    File file = new File(url.toURI());
                    if (file.isFile()) {
                        resourceJarUrls.add(new URL(ResourceUtils.JAR_URL_PREFIX + url + ResourceUtils.JAR_URL_SEPARATOR));
                    } else {
                        resourceManagers.add(new FileResourceManager(new File(file, "META-INF/resources"), 0L));
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                resourceJarUrls.add(url);
            }
        }
        resourceManagers.add(new MetaInfResourcesResourceManager(resourceJarUrls));
        return new CompositeResourceManager((ResourceManager[]) resourceManagers.toArray(new ResourceManager[resourceManagers.size()]));
    }

    private File getCanonicalDocumentRoot() {
        try {
            File root = getValidDocumentRoot();
            return (root != null ? root : createTempDir("undertow-docbase")).getCanonicalFile();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot get canonical document root", ex);
        }
    }

    private void configureErrorPages(DeploymentInfo servletBuilder) {
        for (ErrorPage errorPage : getErrorPages()) {
            servletBuilder.addErrorPage(getUndertowErrorPage(errorPage));
        }
    }

    private io.undertow.servlet.api.ErrorPage getUndertowErrorPage(ErrorPage errorPage) {
        if (errorPage.getStatus() != null) {
            return new io.undertow.servlet.api.ErrorPage(errorPage.getPath(), errorPage.getStatusCode());
        }
        if (errorPage.getException() != null) {
            return new io.undertow.servlet.api.ErrorPage(errorPage.getPath(), errorPage.getException());
        }
        return new io.undertow.servlet.api.ErrorPage(errorPage.getPath());
    }

    private void configureMimeMappings(DeploymentInfo servletBuilder) {
        Iterator<MimeMappings.Mapping> it = getMimeMappings().iterator();
        while (it.hasNext()) {
            MimeMappings.Mapping mimeMapping = it.next();
            servletBuilder.addMimeMapping(new MimeMapping(mimeMapping.getExtension(), mimeMapping.getMimeType()));
        }
    }

    protected UndertowEmbeddedServletContainer getUndertowEmbeddedServletContainer(Undertow.Builder builder, DeploymentManager manager, int port) {
        return new UndertowEmbeddedServletContainer(builder, manager, getContextPath(), isUseForwardHeaders(), port >= 0, getCompression(), getServerHeader());
    }

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Deprecated
    public void setBuffersPerRegion(Integer buffersPerRegion) {
    }

    public void setIoThreads(Integer ioThreads) {
        this.ioThreads = ioThreads;
    }

    public void setWorkerThreads(Integer workerThreads) {
        this.workerThreads = workerThreads;
    }

    public void setDirectBuffers(Boolean directBuffers) {
        this.directBuffers = directBuffers;
    }

    public void setAccessLogDirectory(File accessLogDirectory) {
        this.accessLogDirectory = accessLogDirectory;
    }

    public void setAccessLogPattern(String accessLogPattern) {
        this.accessLogPattern = accessLogPattern;
    }

    public String getAccessLogPrefix() {
        return this.accessLogPrefix;
    }

    public void setAccessLogPrefix(String accessLogPrefix) {
        this.accessLogPrefix = accessLogPrefix;
    }

    public void setAccessLogSuffix(String accessLogSuffix) {
        this.accessLogSuffix = accessLogSuffix;
    }

    public void setAccessLogEnabled(boolean accessLogEnabled) {
        this.accessLogEnabled = accessLogEnabled;
    }

    public boolean isAccessLogEnabled() {
        return this.accessLogEnabled;
    }

    public void setAccessLogRotate(boolean accessLogRotate) {
        this.accessLogRotate = accessLogRotate;
    }

    protected final boolean isUseForwardHeaders() {
        return this.useForwardHeaders;
    }

    public void setUseForwardHeaders(boolean useForwardHeaders) {
        this.useForwardHeaders = useForwardHeaders;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowEmbeddedServletContainerFactory$MetaInfResourcesResourceManager.class */
    private static final class MetaInfResourcesResourceManager implements ResourceManager {
        private final List<URL> metaInfResourceJarUrls;

        private MetaInfResourcesResourceManager(List<URL> metaInfResourceJarUrls) {
            this.metaInfResourceJarUrls = metaInfResourceJarUrls;
        }

        public void close() throws IOException {
        }

        public Resource getResource(String path) {
            for (URL url : this.metaInfResourceJarUrls) {
                URLResource resource = getMetaInfResource(url, path);
                if (resource != null) {
                    return resource;
                }
            }
            return null;
        }

        public boolean isResourceChangeListenerSupported() {
            return false;
        }

        public void registerResourceChangeListener(ResourceChangeListener listener) {
        }

        public void removeResourceChangeListener(ResourceChangeListener listener) {
        }

        private URLResource getMetaInfResource(URL resourceJar, String path) {
            try {
                URL resourceUrl = new URL(resourceJar + "META-INF/resources" + path);
                URLResource resource = new URLResource(resourceUrl, path);
                if (resource.getContentLength().longValue() < 0) {
                    return null;
                }
                return resource;
            } catch (MalformedURLException e) {
                return null;
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowEmbeddedServletContainerFactory$Initializer.class */
    private static class Initializer implements ServletContainerInitializer {
        private final ServletContextInitializer[] initializers;

        Initializer(ServletContextInitializer[] initializers) {
            this.initializers = initializers;
        }

        @Override // javax.servlet.ServletContainerInitializer
        public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
            for (ServletContextInitializer initializer : this.initializers) {
                initializer.onStartup(servletContext);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowEmbeddedServletContainerFactory$ConfigurableAliasKeyManager.class */
    private static class ConfigurableAliasKeyManager extends X509ExtendedKeyManager {
        private final X509ExtendedKeyManager keyManager;
        private final String alias;

        ConfigurableAliasKeyManager(X509ExtendedKeyManager keyManager, String alias) {
            this.keyManager = keyManager;
            this.alias = alias;
        }

        @Override // javax.net.ssl.X509ExtendedKeyManager
        public String chooseEngineClientAlias(String[] strings, Principal[] principals, SSLEngine sslEngine) {
            return this.keyManager.chooseEngineClientAlias(strings, principals, sslEngine);
        }

        @Override // javax.net.ssl.X509ExtendedKeyManager
        public String chooseEngineServerAlias(String s, Principal[] principals, SSLEngine sslEngine) {
            if (this.alias == null) {
                return this.keyManager.chooseEngineServerAlias(s, principals, sslEngine);
            }
            return this.alias;
        }

        @Override // javax.net.ssl.X509KeyManager
        public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
            return this.keyManager.chooseClientAlias(keyType, issuers, socket);
        }

        @Override // javax.net.ssl.X509KeyManager
        public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
            return this.keyManager.chooseServerAlias(keyType, issuers, socket);
        }

        @Override // javax.net.ssl.X509KeyManager
        public X509Certificate[] getCertificateChain(String alias) {
            return this.keyManager.getCertificateChain(alias);
        }

        @Override // javax.net.ssl.X509KeyManager
        public String[] getClientAliases(String keyType, Principal[] issuers) {
            return this.keyManager.getClientAliases(keyType, issuers);
        }

        @Override // javax.net.ssl.X509KeyManager
        public PrivateKey getPrivateKey(String alias) {
            return this.keyManager.getPrivateKey(alias);
        }

        @Override // javax.net.ssl.X509KeyManager
        public String[] getServerAliases(String keyType, Principal[] issuers) {
            return this.keyManager.getServerAliases(keyType, issuers);
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowEmbeddedServletContainerFactory$AccessLogShutdownListener.class */
    private static class AccessLogShutdownListener implements ServletContextListener {
        private final XnioWorker worker;
        private final DefaultAccessLogReceiver accessLogReceiver;

        AccessLogShutdownListener(XnioWorker worker, DefaultAccessLogReceiver accessLogReceiver) {
            this.worker = worker;
            this.accessLogReceiver = accessLogReceiver;
        }

        @Override // javax.servlet.ServletContextListener
        public void contextInitialized(ServletContextEvent sce) {
        }

        @Override // javax.servlet.ServletContextListener
        public void contextDestroyed(ServletContextEvent sce) {
            try {
                this.accessLogReceiver.close();
                this.worker.shutdown();
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
}
