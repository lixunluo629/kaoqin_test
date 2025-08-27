package org.springframework.boot.autoconfigure.web;

import com.google.common.net.HttpHeaders;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.catalina.valves.Constants;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.catalina.valves.RemoteIpValve;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.context.embedded.Compression;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.InitParameterConfiguringServletContextInitializer;
import org.springframework.boot.context.embedded.JspServlet;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties.class */
public class ServerProperties implements EmbeddedServletContainerCustomizer, EnvironmentAware, Ordered {
    private Integer port;
    private InetAddress address;
    private String contextPath;
    private Boolean useForwardHeaders;
    private String serverHeader;
    private Integer connectionTimeout;

    @NestedConfigurationProperty
    private Ssl ssl;

    @NestedConfigurationProperty
    private JspServlet jspServlet;
    private Environment environment;
    private String displayName = "application";

    @NestedConfigurationProperty
    private ErrorProperties error = new ErrorProperties();
    private String servletPath = "/";
    private final Map<String, String> contextParameters = new HashMap();
    private int maxHttpHeaderSize = 0;
    private int maxHttpPostSize = 0;
    private Session session = new Session();

    @NestedConfigurationProperty
    private Compression compression = new Compression();
    private final Tomcat tomcat = new Tomcat();
    private final Jetty jetty = new Jetty();
    private final Undertow undertow = new Undertow();

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return 0;
    }

    @Override // org.springframework.context.EnvironmentAware
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer
    public void customize(ConfigurableEmbeddedServletContainer container) {
        if (getPort() != null) {
            container.setPort(getPort().intValue());
        }
        if (getAddress() != null) {
            container.setAddress(getAddress());
        }
        if (getContextPath() != null) {
            container.setContextPath(getContextPath());
        }
        if (getDisplayName() != null) {
            container.setDisplayName(getDisplayName());
        }
        if (getSession().getTimeout() != null) {
            container.setSessionTimeout(getSession().getTimeout().intValue());
        }
        container.setPersistSession(getSession().isPersistent());
        container.setSessionStoreDir(getSession().getStoreDir());
        if (getSsl() != null) {
            container.setSsl(getSsl());
        }
        if (getJspServlet() != null) {
            container.setJspServlet(getJspServlet());
        }
        if (getCompression() != null) {
            container.setCompression(getCompression());
        }
        container.setServerHeader(getServerHeader());
        if (container instanceof TomcatEmbeddedServletContainerFactory) {
            getTomcat().customizeTomcat(this, (TomcatEmbeddedServletContainerFactory) container);
        }
        if (container instanceof JettyEmbeddedServletContainerFactory) {
            getJetty().customizeJetty(this, (JettyEmbeddedServletContainerFactory) container);
        }
        if (container instanceof UndertowEmbeddedServletContainerFactory) {
            getUndertow().customizeUndertow(this, (UndertowEmbeddedServletContainerFactory) container);
        }
        container.addInitializers(new SessionConfiguringInitializer(this.session));
        container.addInitializers(new InitParameterConfiguringServletContextInitializer(getContextParameters()));
    }

    public String getServletMapping() {
        if (this.servletPath.equals("") || this.servletPath.equals("/")) {
            return "/";
        }
        if (this.servletPath.contains("*")) {
            return this.servletPath;
        }
        if (this.servletPath.endsWith("/")) {
            return this.servletPath + "*";
        }
        return this.servletPath + ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER;
    }

    public String getPath(String path) {
        String prefix = getServletPrefix();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return prefix + path;
    }

    public String getServletPrefix() {
        String result = this.servletPath;
        if (result.contains("*")) {
            result = result.substring(0, result.indexOf("*"));
        }
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public String[] getPathsArray(Collection<String> paths) {
        String[] result = new String[paths.size()];
        int i = 0;
        for (String path : paths) {
            int i2 = i;
            i++;
            result[i2] = getPath(path);
        }
        return result;
    }

    public String[] getPathsArray(String[] paths) {
        String[] result = new String[paths.length];
        int i = 0;
        for (String path : paths) {
            int i2 = i;
            i++;
            result[i2] = getPath(path);
        }
        return result;
    }

    public void setLoader(String value) {
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public String getContextPath() {
        return this.contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = cleanContextPath(contextPath);
    }

    private String cleanContextPath(String contextPath) {
        if (StringUtils.hasText(contextPath) && contextPath.endsWith("/")) {
            return contextPath.substring(0, contextPath.length() - 1);
        }
        return contextPath;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getServletPath() {
        return this.servletPath;
    }

    public void setServletPath(String servletPath) {
        Assert.notNull(servletPath, "ServletPath must not be null");
        this.servletPath = servletPath;
    }

    public Map<String, String> getContextParameters() {
        return this.contextParameters;
    }

    public Boolean isUseForwardHeaders() {
        return this.useForwardHeaders;
    }

    public void setUseForwardHeaders(Boolean useForwardHeaders) {
        this.useForwardHeaders = useForwardHeaders;
    }

    public String getServerHeader() {
        return this.serverHeader;
    }

    public void setServerHeader(String serverHeader) {
        this.serverHeader = serverHeader;
    }

    public int getMaxHttpHeaderSize() {
        return this.maxHttpHeaderSize;
    }

    public void setMaxHttpHeaderSize(int maxHttpHeaderSize) {
        this.maxHttpHeaderSize = maxHttpHeaderSize;
    }

    @DeprecatedConfigurationProperty(reason = "Use dedicated property for each container.")
    @Deprecated
    public int getMaxHttpPostSize() {
        return this.maxHttpPostSize;
    }

    @Deprecated
    public void setMaxHttpPostSize(int maxHttpPostSize) {
        this.maxHttpPostSize = maxHttpPostSize;
        this.jetty.setMaxHttpPostSize(maxHttpPostSize);
        this.tomcat.setMaxHttpPostSize(maxHttpPostSize);
        this.undertow.setMaxHttpPostSize(maxHttpPostSize);
    }

    protected final boolean getOrDeduceUseForwardHeaders() {
        if (this.useForwardHeaders != null) {
            return this.useForwardHeaders.booleanValue();
        }
        CloudPlatform platform = CloudPlatform.getActive(this.environment);
        if (platform != null) {
            return platform.isUsingForwardHeaders();
        }
        return false;
    }

    public Integer getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public ErrorProperties getError() {
        return this.error;
    }

    public Session getSession() {
        return this.session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Ssl getSsl() {
        return this.ssl;
    }

    public void setSsl(Ssl ssl) {
        this.ssl = ssl;
    }

    public Compression getCompression() {
        return this.compression;
    }

    public JspServlet getJspServlet() {
        return this.jspServlet;
    }

    public void setJspServlet(JspServlet jspServlet) {
        this.jspServlet = jspServlet;
    }

    public Tomcat getTomcat() {
        return this.tomcat;
    }

    public Jetty getJetty() {
        return this.jetty;
    }

    public Undertow getUndertow() {
        return this.undertow;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Session.class */
    public static class Session {
        private Integer timeout;
        private Set<SessionTrackingMode> trackingModes;
        private boolean persistent;
        private File storeDir;
        private Cookie cookie = new Cookie();

        public Cookie getCookie() {
            return this.cookie;
        }

        public Integer getTimeout() {
            return this.timeout;
        }

        public void setTimeout(Integer sessionTimeout) {
            this.timeout = sessionTimeout;
        }

        public Set<SessionTrackingMode> getTrackingModes() {
            return this.trackingModes;
        }

        public void setTrackingModes(Set<SessionTrackingMode> trackingModes) {
            this.trackingModes = trackingModes;
        }

        public boolean isPersistent() {
            return this.persistent;
        }

        public void setPersistent(boolean persistent) {
            this.persistent = persistent;
        }

        public File getStoreDir() {
            return this.storeDir;
        }

        public void setStoreDir(File storeDir) {
            this.storeDir = storeDir;
        }

        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Session$Cookie.class */
        public static class Cookie {
            private String name;
            private String domain;
            private String path;
            private String comment;
            private Boolean httpOnly;
            private Boolean secure;
            private Integer maxAge;

            public String getName() {
                return this.name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDomain() {
                return this.domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public String getPath() {
                return this.path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getComment() {
                return this.comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public Boolean getHttpOnly() {
                return this.httpOnly;
            }

            public void setHttpOnly(Boolean httpOnly) {
                this.httpOnly = httpOnly;
            }

            public Boolean getSecure() {
                return this.secure;
            }

            public void setSecure(Boolean secure) {
                this.secure = secure;
            }

            public Integer getMaxAge() {
                return this.maxAge;
            }

            public void setMaxAge(Integer maxAge) {
                this.maxAge = maxAge;
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Tomcat.class */
    public static class Tomcat {
        private String protocolHeader;
        private String remoteIpHeader;
        private File basedir;
        private final Accesslog accesslog = new Accesslog();
        private String internalProxies = "10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|192\\.168\\.\\d{1,3}\\.\\d{1,3}|169\\.254\\.\\d{1,3}\\.\\d{1,3}|127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}|0:0:0:0:0:0:0:1|::1";
        private String protocolHeaderHttpsValue = "https";
        private String portHeader = "X-Forwarded-Port";
        private int backgroundProcessorDelay = 10;
        private int maxThreads = 200;
        private int minSpareThreads = 10;
        private int maxHttpPostSize = 2097152;
        private int maxHttpHeaderSize = 0;
        private Boolean redirectContextRoot = true;
        private Charset uriEncoding = Charset.forName("UTF-8");
        private int maxConnections = 10000;
        private int acceptCount = 100;
        private List<String> additionalTldSkipPatterns = new ArrayList();

        public int getMaxThreads() {
            return this.maxThreads;
        }

        public void setMaxThreads(int maxThreads) {
            this.maxThreads = maxThreads;
        }

        public int getMinSpareThreads() {
            return this.minSpareThreads;
        }

        public void setMinSpareThreads(int minSpareThreads) {
            this.minSpareThreads = minSpareThreads;
        }

        public int getMaxHttpPostSize() {
            return this.maxHttpPostSize;
        }

        public void setMaxHttpPostSize(int maxHttpPostSize) {
            this.maxHttpPostSize = maxHttpPostSize;
        }

        public Accesslog getAccesslog() {
            return this.accesslog;
        }

        public int getBackgroundProcessorDelay() {
            return this.backgroundProcessorDelay;
        }

        public void setBackgroundProcessorDelay(int backgroundProcessorDelay) {
            this.backgroundProcessorDelay = backgroundProcessorDelay;
        }

        public File getBasedir() {
            return this.basedir;
        }

        public void setBasedir(File basedir) {
            this.basedir = basedir;
        }

        public String getInternalProxies() {
            return this.internalProxies;
        }

        public void setInternalProxies(String internalProxies) {
            this.internalProxies = internalProxies;
        }

        public String getProtocolHeader() {
            return this.protocolHeader;
        }

        public void setProtocolHeader(String protocolHeader) {
            this.protocolHeader = protocolHeader;
        }

        public String getProtocolHeaderHttpsValue() {
            return this.protocolHeaderHttpsValue;
        }

        public void setProtocolHeaderHttpsValue(String protocolHeaderHttpsValue) {
            this.protocolHeaderHttpsValue = protocolHeaderHttpsValue;
        }

        public String getPortHeader() {
            return this.portHeader;
        }

        public void setPortHeader(String portHeader) {
            this.portHeader = portHeader;
        }

        public Boolean getRedirectContextRoot() {
            return this.redirectContextRoot;
        }

        public void setRedirectContextRoot(Boolean redirectContextRoot) {
            this.redirectContextRoot = redirectContextRoot;
        }

        public String getRemoteIpHeader() {
            return this.remoteIpHeader;
        }

        public void setRemoteIpHeader(String remoteIpHeader) {
            this.remoteIpHeader = remoteIpHeader;
        }

        public Charset getUriEncoding() {
            return this.uriEncoding;
        }

        public void setUriEncoding(Charset uriEncoding) {
            this.uriEncoding = uriEncoding;
        }

        public int getMaxConnections() {
            return this.maxConnections;
        }

        public void setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
        }

        public int getAcceptCount() {
            return this.acceptCount;
        }

        public void setAcceptCount(int acceptCount) {
            this.acceptCount = acceptCount;
        }

        public List<String> getAdditionalTldSkipPatterns() {
            return this.additionalTldSkipPatterns;
        }

        public void setAdditionalTldSkipPatterns(List<String> additionalTldSkipPatterns) {
            this.additionalTldSkipPatterns = additionalTldSkipPatterns;
        }

        void customizeTomcat(ServerProperties serverProperties, TomcatEmbeddedServletContainerFactory factory) {
            if (getBasedir() != null) {
                factory.setBaseDirectory(getBasedir());
            }
            factory.setBackgroundProcessorDelay(this.backgroundProcessorDelay);
            customizeRemoteIpValve(serverProperties, factory);
            if (this.maxThreads > 0) {
                customizeMaxThreads(factory);
            }
            if (this.minSpareThreads > 0) {
                customizeMinThreads(factory);
            }
            int maxHttpHeaderSize = serverProperties.getMaxHttpHeaderSize() > 0 ? serverProperties.getMaxHttpHeaderSize() : this.maxHttpHeaderSize;
            if (maxHttpHeaderSize > 0) {
                customizeMaxHttpHeaderSize(factory, maxHttpHeaderSize);
            }
            if (this.maxHttpPostSize != 0) {
                customizeMaxHttpPostSize(factory, this.maxHttpPostSize);
            }
            if (this.accesslog.enabled) {
                customizeAccessLog(factory);
            }
            if (getUriEncoding() != null) {
                factory.setUriEncoding(getUriEncoding());
            }
            if (serverProperties.getConnectionTimeout() != null) {
                customizeConnectionTimeout(factory, serverProperties.getConnectionTimeout().intValue());
            }
            if (this.redirectContextRoot != null) {
                customizeRedirectContextRoot(factory, this.redirectContextRoot.booleanValue());
            }
            if (this.maxConnections > 0) {
                customizeMaxConnections(factory);
            }
            if (this.acceptCount > 0) {
                customizeAcceptCount(factory);
            }
            if (!ObjectUtils.isEmpty(this.additionalTldSkipPatterns)) {
                factory.getTldSkipPatterns().addAll(this.additionalTldSkipPatterns);
            }
            if (serverProperties.getError().getIncludeStacktrace() == ErrorProperties.IncludeStacktrace.NEVER) {
                customizeErrorReportValve(factory);
            }
            final Session.Cookie cookie = serverProperties.getSession().getCookie();
            if (cookie.getHttpOnly() != null) {
                factory.addContextCustomizers(new TomcatContextCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.1
                    @Override // org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer
                    public void customize(Context context) {
                        context.setUseHttpOnly(cookie.getHttpOnly().booleanValue());
                    }
                });
            }
        }

        private void customizeErrorReportValve(TomcatEmbeddedServletContainerFactory factory) {
            factory.addContextCustomizers(new TomcatContextCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.2
                @Override // org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer
                public void customize(Context context) {
                    ErrorReportValve valve = new ErrorReportValve();
                    valve.setShowServerInfo(false);
                    valve.setShowReport(false);
                    context.getParent().getPipeline().addValve(valve);
                }
            });
        }

        private void customizeAcceptCount(TomcatEmbeddedServletContainerFactory factory) {
            factory.addConnectorCustomizers(new TomcatConnectorCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.3
                @Override // org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer
                public void customize(Connector connector) {
                    ProtocolHandler handler = connector.getProtocolHandler();
                    if (handler instanceof AbstractProtocol) {
                        AbstractProtocol<?> protocol = (AbstractProtocol) handler;
                        protocol.setBacklog(Tomcat.this.acceptCount);
                    }
                }
            });
        }

        private void customizeMaxConnections(TomcatEmbeddedServletContainerFactory factory) {
            factory.addConnectorCustomizers(new TomcatConnectorCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.4
                @Override // org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer
                public void customize(Connector connector) {
                    ProtocolHandler handler = connector.getProtocolHandler();
                    if (handler instanceof AbstractProtocol) {
                        AbstractProtocol<?> protocol = (AbstractProtocol) handler;
                        protocol.setMaxConnections(Tomcat.this.maxConnections);
                    }
                }
            });
        }

        private void customizeConnectionTimeout(TomcatEmbeddedServletContainerFactory factory, final int connectionTimeout) {
            factory.addConnectorCustomizers(new TomcatConnectorCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.5
                @Override // org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer
                public void customize(Connector connector) {
                    ProtocolHandler handler = connector.getProtocolHandler();
                    if (handler instanceof AbstractProtocol) {
                        AbstractProtocol<?> protocol = (AbstractProtocol) handler;
                        protocol.setConnectionTimeout(connectionTimeout);
                    }
                }
            });
        }

        private void customizeRemoteIpValve(ServerProperties properties, TomcatEmbeddedServletContainerFactory factory) {
            String protocolHeader = getProtocolHeader();
            String remoteIpHeader = getRemoteIpHeader();
            if (StringUtils.hasText(protocolHeader) || StringUtils.hasText(remoteIpHeader) || properties.getOrDeduceUseForwardHeaders()) {
                RemoteIpValve valve = new RemoteIpValve();
                valve.setProtocolHeader(StringUtils.hasLength(protocolHeader) ? protocolHeader : HttpHeaders.X_FORWARDED_PROTO);
                if (StringUtils.hasLength(remoteIpHeader)) {
                    valve.setRemoteIpHeader(remoteIpHeader);
                }
                valve.setInternalProxies(getInternalProxies());
                valve.setPortHeader(getPortHeader());
                valve.setProtocolHeaderHttpsValue(getProtocolHeaderHttpsValue());
                factory.addEngineValves(valve);
            }
        }

        private void customizeMaxThreads(TomcatEmbeddedServletContainerFactory factory) {
            factory.addConnectorCustomizers(new TomcatConnectorCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.6
                @Override // org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer
                public void customize(Connector connector) {
                    ProtocolHandler handler = connector.getProtocolHandler();
                    if (handler instanceof AbstractProtocol) {
                        AbstractProtocol protocol = (AbstractProtocol) handler;
                        protocol.setMaxThreads(Tomcat.this.maxThreads);
                    }
                }
            });
        }

        private void customizeMinThreads(TomcatEmbeddedServletContainerFactory factory) {
            factory.addConnectorCustomizers(new TomcatConnectorCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.7
                @Override // org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer
                public void customize(Connector connector) {
                    ProtocolHandler handler = connector.getProtocolHandler();
                    if (handler instanceof AbstractProtocol) {
                        AbstractProtocol protocol = (AbstractProtocol) handler;
                        protocol.setMinSpareThreads(Tomcat.this.minSpareThreads);
                    }
                }
            });
        }

        private void customizeMaxHttpHeaderSize(TomcatEmbeddedServletContainerFactory factory, final int maxHttpHeaderSize) {
            factory.addConnectorCustomizers(new TomcatConnectorCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.8
                @Override // org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer
                public void customize(Connector connector) {
                    ProtocolHandler handler = connector.getProtocolHandler();
                    if (handler instanceof AbstractHttp11Protocol) {
                        AbstractHttp11Protocol protocol = (AbstractHttp11Protocol) handler;
                        protocol.setMaxHttpHeaderSize(maxHttpHeaderSize);
                    }
                }
            });
        }

        private void customizeMaxHttpPostSize(TomcatEmbeddedServletContainerFactory factory, final int maxHttpPostSize) {
            factory.addConnectorCustomizers(new TomcatConnectorCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.9
                @Override // org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer
                public void customize(Connector connector) {
                    connector.setMaxPostSize(maxHttpPostSize);
                }
            });
        }

        private void customizeAccessLog(TomcatEmbeddedServletContainerFactory factory) {
            AccessLogValve valve = new AccessLogValve();
            valve.setPattern(this.accesslog.getPattern());
            valve.setDirectory(this.accesslog.getDirectory());
            valve.setPrefix(this.accesslog.getPrefix());
            valve.setSuffix(this.accesslog.getSuffix());
            valve.setRenameOnRotate(this.accesslog.isRenameOnRotate());
            valve.setRequestAttributesEnabled(this.accesslog.isRequestAttributesEnabled());
            valve.setRotatable(this.accesslog.isRotate());
            valve.setBuffered(this.accesslog.isBuffered());
            valve.setFileDateFormat(this.accesslog.getFileDateFormat());
            factory.addEngineValves(valve);
        }

        private void customizeRedirectContextRoot(TomcatEmbeddedServletContainerFactory factory, final boolean redirectContextRoot) {
            factory.addContextCustomizers(new TomcatContextCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.10
                @Override // org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer
                public void customize(Context context) {
                    try {
                        context.setMapperContextRootRedirectEnabled(redirectContextRoot);
                    } catch (NoSuchMethodError e) {
                    }
                }
            });
        }

        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Tomcat$Accesslog.class */
        public static class Accesslog {
            private boolean enabled = false;
            private String pattern = Constants.AccessLog.COMMON_ALIAS;
            private String directory = "logs";
            protected String prefix = "access_log";
            private String suffix = ".log";
            private boolean rotate = true;
            private boolean renameOnRotate = false;
            private String fileDateFormat = ".yyyy-MM-dd";
            private boolean requestAttributesEnabled = false;
            private boolean buffered = true;

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getPattern() {
                return this.pattern;
            }

            public void setPattern(String pattern) {
                this.pattern = pattern;
            }

            public String getDirectory() {
                return this.directory;
            }

            public void setDirectory(String directory) {
                this.directory = directory;
            }

            public String getPrefix() {
                return this.prefix;
            }

            public void setPrefix(String prefix) {
                this.prefix = prefix;
            }

            public String getSuffix() {
                return this.suffix;
            }

            public void setSuffix(String suffix) {
                this.suffix = suffix;
            }

            public boolean isRotate() {
                return this.rotate;
            }

            public void setRotate(boolean rotate) {
                this.rotate = rotate;
            }

            public boolean isRenameOnRotate() {
                return this.renameOnRotate;
            }

            public void setRenameOnRotate(boolean renameOnRotate) {
                this.renameOnRotate = renameOnRotate;
            }

            public String getFileDateFormat() {
                return this.fileDateFormat;
            }

            public void setFileDateFormat(String fileDateFormat) {
                this.fileDateFormat = fileDateFormat;
            }

            public boolean isRequestAttributesEnabled() {
                return this.requestAttributesEnabled;
            }

            public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {
                this.requestAttributesEnabled = requestAttributesEnabled;
            }

            public boolean isBuffered() {
                return this.buffered;
            }

            public void setBuffered(boolean buffered) {
                this.buffered = buffered;
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Jetty.class */
    public static class Jetty {
        private int maxHttpPostSize = 200000;
        private Integer acceptors = -1;
        private Integer selectors = -1;

        public int getMaxHttpPostSize() {
            return this.maxHttpPostSize;
        }

        public void setMaxHttpPostSize(int maxHttpPostSize) {
            this.maxHttpPostSize = maxHttpPostSize;
        }

        public Integer getAcceptors() {
            return this.acceptors;
        }

        public void setAcceptors(Integer acceptors) {
            this.acceptors = acceptors;
        }

        public Integer getSelectors() {
            return this.selectors;
        }

        public void setSelectors(Integer selectors) {
            this.selectors = selectors;
        }

        void customizeJetty(ServerProperties serverProperties, JettyEmbeddedServletContainerFactory factory) {
            factory.setUseForwardHeaders(serverProperties.getOrDeduceUseForwardHeaders());
            if (this.acceptors != null) {
                factory.setAcceptors(this.acceptors.intValue());
            }
            if (this.selectors != null) {
                factory.setSelectors(this.selectors.intValue());
            }
            if (serverProperties.getMaxHttpHeaderSize() > 0) {
                customizeMaxHttpHeaderSize(factory, serverProperties.getMaxHttpHeaderSize());
            }
            if (this.maxHttpPostSize > 0) {
                customizeMaxHttpPostSize(factory, this.maxHttpPostSize);
            }
            if (serverProperties.getConnectionTimeout() != null) {
                customizeConnectionTimeout(factory, serverProperties.getConnectionTimeout().intValue());
            }
        }

        private void customizeConnectionTimeout(JettyEmbeddedServletContainerFactory factory, final int connectionTimeout) {
            factory.addServerCustomizers(new JettyServerCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Jetty.1
                @Override // org.springframework.boot.context.embedded.jetty.JettyServerCustomizer
                public void customize(Server server) {
                    for (AbstractConnector abstractConnector : server.getConnectors()) {
                        if (abstractConnector instanceof AbstractConnector) {
                            abstractConnector.setIdleTimeout(connectionTimeout);
                        }
                    }
                }
            });
        }

        private void customizeMaxHttpHeaderSize(JettyEmbeddedServletContainerFactory factory, final int maxHttpHeaderSize) {
            factory.addServerCustomizers(new JettyServerCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Jetty.2
                @Override // org.springframework.boot.context.embedded.jetty.JettyServerCustomizer
                public void customize(Server server) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    for (org.eclipse.jetty.server.Connector connector : server.getConnectors()) {
                        try {
                            for (ConnectionFactory connectionFactory : connector.getConnectionFactories()) {
                                if (connectionFactory instanceof HttpConfiguration.ConnectionFactory) {
                                    customize((HttpConfiguration.ConnectionFactory) connectionFactory);
                                }
                            }
                        } catch (NoSuchMethodError e) {
                            customizeOnJetty8(connector, maxHttpHeaderSize);
                        }
                    }
                }

                private void customize(HttpConfiguration.ConnectionFactory factory2) {
                    HttpConfiguration configuration = factory2.getHttpConfiguration();
                    configuration.setRequestHeaderSize(maxHttpHeaderSize);
                    configuration.setResponseHeaderSize(maxHttpHeaderSize);
                }

                private void customizeOnJetty8(org.eclipse.jetty.server.Connector connector, int maxHttpHeaderSize2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    try {
                        connector.getClass().getMethod("setRequestHeaderSize", Integer.TYPE).invoke(connector, Integer.valueOf(maxHttpHeaderSize2));
                        connector.getClass().getMethod("setResponseHeaderSize", Integer.TYPE).invoke(connector, Integer.valueOf(maxHttpHeaderSize2));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        private void customizeMaxHttpPostSize(JettyEmbeddedServletContainerFactory factory, final int maxHttpPostSize) {
            factory.addServerCustomizers(new JettyServerCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Jetty.3
                @Override // org.springframework.boot.context.embedded.jetty.JettyServerCustomizer
                public void customize(Server server) {
                    setHandlerMaxHttpPostSize(maxHttpPostSize, server.getHandlers());
                }

                private void setHandlerMaxHttpPostSize(int maxHttpPostSize2, Handler... handlers) {
                    for (Handler handler : handlers) {
                        if (handler instanceof ContextHandler) {
                            ((ContextHandler) handler).setMaxFormContentSize(maxHttpPostSize2);
                        } else if (handler instanceof HandlerWrapper) {
                            setHandlerMaxHttpPostSize(maxHttpPostSize2, ((HandlerWrapper) handler).getHandler());
                        } else if (handler instanceof HandlerCollection) {
                            setHandlerMaxHttpPostSize(maxHttpPostSize2, ((HandlerCollection) handler).getHandlers());
                        }
                    }
                }
            });
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Undertow.class */
    public static class Undertow {
        private Integer bufferSize;

        @Deprecated
        private Integer buffersPerRegion;
        private Integer ioThreads;
        private Integer workerThreads;
        private Boolean directBuffers;
        private long maxHttpPostSize = -1;
        private final Accesslog accesslog = new Accesslog();

        public long getMaxHttpPostSize() {
            return this.maxHttpPostSize;
        }

        public void setMaxHttpPostSize(long maxHttpPostSize) {
            this.maxHttpPostSize = maxHttpPostSize;
        }

        public Integer getBufferSize() {
            return this.bufferSize;
        }

        public void setBufferSize(Integer bufferSize) {
            this.bufferSize = bufferSize;
        }

        @DeprecatedConfigurationProperty(reason = "The property is not used by Undertow. See https://issues.jboss.org/browse/UNDERTOW-587 for details")
        public Integer getBuffersPerRegion() {
            return this.buffersPerRegion;
        }

        public void setBuffersPerRegion(Integer buffersPerRegion) {
            this.buffersPerRegion = buffersPerRegion;
        }

        public Integer getIoThreads() {
            return this.ioThreads;
        }

        public void setIoThreads(Integer ioThreads) {
            this.ioThreads = ioThreads;
        }

        public Integer getWorkerThreads() {
            return this.workerThreads;
        }

        public void setWorkerThreads(Integer workerThreads) {
            this.workerThreads = workerThreads;
        }

        public Boolean getDirectBuffers() {
            return this.directBuffers;
        }

        public void setDirectBuffers(Boolean directBuffers) {
            this.directBuffers = directBuffers;
        }

        public Accesslog getAccesslog() {
            return this.accesslog;
        }

        void customizeUndertow(ServerProperties serverProperties, UndertowEmbeddedServletContainerFactory factory) {
            if (this.bufferSize != null) {
                factory.setBufferSize(this.bufferSize);
            }
            if (this.ioThreads != null) {
                factory.setIoThreads(this.ioThreads);
            }
            if (this.workerThreads != null) {
                factory.setWorkerThreads(this.workerThreads);
            }
            if (this.directBuffers != null) {
                factory.setDirectBuffers(this.directBuffers);
            }
            if (this.accesslog.enabled != null) {
                factory.setAccessLogEnabled(this.accesslog.enabled.booleanValue());
            }
            factory.setAccessLogDirectory(this.accesslog.dir);
            factory.setAccessLogPattern(this.accesslog.pattern);
            factory.setAccessLogPrefix(this.accesslog.prefix);
            factory.setAccessLogSuffix(this.accesslog.suffix);
            factory.setAccessLogRotate(this.accesslog.rotate);
            factory.setUseForwardHeaders(serverProperties.getOrDeduceUseForwardHeaders());
            if (serverProperties.getMaxHttpHeaderSize() > 0) {
                customizeMaxHttpHeaderSize(factory, serverProperties.getMaxHttpHeaderSize());
            }
            if (this.maxHttpPostSize > 0) {
                customizeMaxHttpPostSize(factory, this.maxHttpPostSize);
            }
            if (serverProperties.getConnectionTimeout() != null) {
                customizeConnectionTimeout(factory, serverProperties.getConnectionTimeout().intValue());
            }
        }

        private void customizeConnectionTimeout(UndertowEmbeddedServletContainerFactory factory, final int connectionTimeout) {
            factory.addBuilderCustomizers(new UndertowBuilderCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Undertow.1
                @Override // org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer
                public void customize(Undertow.Builder builder) {
                    builder.setSocketOption(UndertowOptions.NO_REQUEST_TIMEOUT, Integer.valueOf(connectionTimeout));
                }
            });
        }

        private void customizeMaxHttpHeaderSize(UndertowEmbeddedServletContainerFactory factory, final int maxHttpHeaderSize) {
            factory.addBuilderCustomizers(new UndertowBuilderCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Undertow.2
                @Override // org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer
                public void customize(Undertow.Builder builder) {
                    builder.setServerOption(UndertowOptions.MAX_HEADER_SIZE, Integer.valueOf(maxHttpHeaderSize));
                }
            });
        }

        private void customizeMaxHttpPostSize(UndertowEmbeddedServletContainerFactory factory, final long maxHttpPostSize) {
            factory.addBuilderCustomizers(new UndertowBuilderCustomizer() { // from class: org.springframework.boot.autoconfigure.web.ServerProperties.Undertow.3
                @Override // org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer
                public void customize(Undertow.Builder builder) {
                    builder.setServerOption(UndertowOptions.MAX_ENTITY_SIZE, Long.valueOf(maxHttpPostSize));
                }
            });
        }

        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Undertow$Accesslog.class */
        public static class Accesslog {
            private Boolean enabled;
            private String pattern = Constants.AccessLog.COMMON_ALIAS;
            protected String prefix = "access_log.";
            private String suffix = "log";
            private File dir = new File("logs");
            private boolean rotate = true;

            public Boolean getEnabled() {
                return this.enabled;
            }

            public void setEnabled(Boolean enabled) {
                this.enabled = enabled;
            }

            public String getPattern() {
                return this.pattern;
            }

            public void setPattern(String pattern) {
                this.pattern = pattern;
            }

            public String getPrefix() {
                return this.prefix;
            }

            public void setPrefix(String prefix) {
                this.prefix = prefix;
            }

            public String getSuffix() {
                return this.suffix;
            }

            public void setSuffix(String suffix) {
                this.suffix = suffix;
            }

            public File getDir() {
                return this.dir;
            }

            public void setDir(File dir) {
                this.dir = dir;
            }

            public boolean isRotate() {
                return this.rotate;
            }

            public void setRotate(boolean rotate) {
                this.rotate = rotate;
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$SessionConfiguringInitializer.class */
    private static class SessionConfiguringInitializer implements ServletContextInitializer {
        private final Session session;

        SessionConfiguringInitializer(Session session) {
            this.session = session;
        }

        @Override // org.springframework.boot.web.servlet.ServletContextInitializer
        public void onStartup(ServletContext servletContext) throws ServletException {
            if (this.session.getTrackingModes() != null) {
                servletContext.setSessionTrackingModes(this.session.getTrackingModes());
            }
            configureSessionCookie(servletContext.getSessionCookieConfig());
        }

        private void configureSessionCookie(SessionCookieConfig config) {
            Session.Cookie cookie = this.session.getCookie();
            if (cookie.getName() != null) {
                config.setName(cookie.getName());
            }
            if (cookie.getDomain() != null) {
                config.setDomain(cookie.getDomain());
            }
            if (cookie.getPath() != null) {
                config.setPath(cookie.getPath());
            }
            if (cookie.getComment() != null) {
                config.setComment(cookie.getComment());
            }
            if (cookie.getHttpOnly() != null) {
                config.setHttpOnly(cookie.getHttpOnly().booleanValue());
            }
            if (cookie.getSecure() != null) {
                config.setSecure(cookie.getSecure().booleanValue());
            }
            if (cookie.getMaxAge() != null) {
                config.setMaxAge(cookie.getMaxAge().intValue());
            }
        }
    }
}
