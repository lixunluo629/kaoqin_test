package org.springframework.boot.context.embedded;

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/AbstractConfigurableEmbeddedServletContainer.class */
public abstract class AbstractConfigurableEmbeddedServletContainer implements ConfigurableEmbeddedServletContainer {
    private static final int DEFAULT_SESSION_TIMEOUT = (int) TimeUnit.MINUTES.toSeconds(30);
    private String contextPath;
    private String displayName;
    private boolean registerDefaultServlet;
    private int port;
    private List<ServletContextInitializer> initializers;
    private File documentRoot;
    private Set<ErrorPage> errorPages;
    private MimeMappings mimeMappings;
    private InetAddress address;
    private int sessionTimeout;
    private boolean persistSession;
    private File sessionStoreDir;
    private Ssl ssl;
    private SslStoreProvider sslStoreProvider;
    private JspServlet jspServlet;
    private Compression compression;
    private String serverHeader;
    private Map<Locale, Charset> localeCharsetMappings;

    public AbstractConfigurableEmbeddedServletContainer() {
        this.contextPath = "";
        this.registerDefaultServlet = true;
        this.port = 8080;
        this.initializers = new ArrayList();
        this.errorPages = new LinkedHashSet();
        this.mimeMappings = new MimeMappings(MimeMappings.DEFAULT);
        this.sessionTimeout = DEFAULT_SESSION_TIMEOUT;
        this.jspServlet = new JspServlet();
        this.localeCharsetMappings = new HashMap();
    }

    public AbstractConfigurableEmbeddedServletContainer(int port) {
        this.contextPath = "";
        this.registerDefaultServlet = true;
        this.port = 8080;
        this.initializers = new ArrayList();
        this.errorPages = new LinkedHashSet();
        this.mimeMappings = new MimeMappings(MimeMappings.DEFAULT);
        this.sessionTimeout = DEFAULT_SESSION_TIMEOUT;
        this.jspServlet = new JspServlet();
        this.localeCharsetMappings = new HashMap();
        this.port = port;
    }

    public AbstractConfigurableEmbeddedServletContainer(String contextPath, int port) {
        this.contextPath = "";
        this.registerDefaultServlet = true;
        this.port = 8080;
        this.initializers = new ArrayList();
        this.errorPages = new LinkedHashSet();
        this.mimeMappings = new MimeMappings(MimeMappings.DEFAULT);
        this.sessionTimeout = DEFAULT_SESSION_TIMEOUT;
        this.jspServlet = new JspServlet();
        this.localeCharsetMappings = new HashMap();
        checkContextPath(contextPath);
        this.contextPath = contextPath;
        this.port = port;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setContextPath(String contextPath) {
        checkContextPath(contextPath);
        this.contextPath = contextPath;
    }

    private void checkContextPath(String contextPath) {
        Assert.notNull(contextPath, "ContextPath must not be null");
        if (contextPath.length() > 0) {
            if ("/".equals(contextPath)) {
                throw new IllegalArgumentException("Root ContextPath must be specified using an empty string");
            }
            if (!contextPath.startsWith("/") || contextPath.endsWith("/")) {
                throw new IllegalArgumentException("ContextPath must start with '/' and not end with '/'");
            }
        }
    }

    public String getContextPath() {
        return this.contextPath;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setSessionTimeout(int sessionTimeout, TimeUnit timeUnit) {
        Assert.notNull(timeUnit, "TimeUnit must not be null");
        this.sessionTimeout = (int) timeUnit.toSeconds(sessionTimeout);
    }

    public int getSessionTimeout() {
        return this.sessionTimeout;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setPersistSession(boolean persistSession) {
        this.persistSession = persistSession;
    }

    public boolean isPersistSession() {
        return this.persistSession;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setSessionStoreDir(File sessionStoreDir) {
        this.sessionStoreDir = sessionStoreDir;
    }

    public File getSessionStoreDir() {
        return this.sessionStoreDir;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setInitializers(List<? extends ServletContextInitializer> initializers) {
        Assert.notNull(initializers, "Initializers must not be null");
        this.initializers = new ArrayList(initializers);
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void addInitializers(ServletContextInitializer... initializers) {
        Assert.notNull(initializers, "Initializers must not be null");
        this.initializers.addAll(Arrays.asList(initializers));
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setDocumentRoot(File documentRoot) {
        this.documentRoot = documentRoot;
    }

    public File getDocumentRoot() {
        return this.documentRoot;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setErrorPages(Set<? extends ErrorPage> errorPages) {
        Assert.notNull(errorPages, "ErrorPages must not be null");
        this.errorPages = new LinkedHashSet(errorPages);
    }

    @Override // org.springframework.boot.web.servlet.ErrorPageRegistry
    public void addErrorPages(ErrorPage... errorPages) {
        Assert.notNull(errorPages, "ErrorPages must not be null");
        this.errorPages.addAll(Arrays.asList(errorPages));
    }

    public Set<ErrorPage> getErrorPages() {
        return this.errorPages;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setMimeMappings(MimeMappings mimeMappings) {
        this.mimeMappings = new MimeMappings(mimeMappings);
    }

    public MimeMappings getMimeMappings() {
        return this.mimeMappings;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setRegisterDefaultServlet(boolean registerDefaultServlet) {
        this.registerDefaultServlet = registerDefaultServlet;
    }

    public boolean isRegisterDefaultServlet() {
        return this.registerDefaultServlet;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setSsl(Ssl ssl) {
        this.ssl = ssl;
    }

    public Ssl getSsl() {
        return this.ssl;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setSslStoreProvider(SslStoreProvider sslStoreProvider) {
        this.sslStoreProvider = sslStoreProvider;
    }

    public SslStoreProvider getSslStoreProvider() {
        return this.sslStoreProvider;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setJspServlet(JspServlet jspServlet) {
        this.jspServlet = jspServlet;
    }

    public JspServlet getJspServlet() {
        return this.jspServlet;
    }

    public Compression getCompression() {
        return this.compression;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setCompression(Compression compression) {
        this.compression = compression;
    }

    public String getServerHeader() {
        return this.serverHeader;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setServerHeader(String serverHeader) {
        this.serverHeader = serverHeader;
    }

    public Map<Locale, Charset> getLocaleCharsetMappings() {
        return this.localeCharsetMappings;
    }

    @Override // org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
    public void setLocaleCharsetMappings(Map<Locale, Charset> localeCharsetMappings) {
        Assert.notNull(localeCharsetMappings, "localeCharsetMappings must not be null");
        this.localeCharsetMappings = localeCharsetMappings;
    }

    protected final ServletContextInitializer[] mergeInitializers(ServletContextInitializer... initializers) {
        List<ServletContextInitializer> mergedInitializers = new ArrayList<>();
        mergedInitializers.addAll(Arrays.asList(initializers));
        mergedInitializers.addAll(this.initializers);
        return (ServletContextInitializer[]) mergedInitializers.toArray(new ServletContextInitializer[mergedInitializers.size()]);
    }

    protected boolean shouldRegisterJspServlet() {
        return this.jspServlet != null && this.jspServlet.getRegistered() && ClassUtils.isPresent(this.jspServlet.getClassName(), getClass().getClassLoader());
    }
}
