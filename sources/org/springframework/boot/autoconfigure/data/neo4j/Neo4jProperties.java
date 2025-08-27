package org.springframework.boot.autoconfigure.data.neo4j;

import java.net.URI;
import java.net.URISyntaxException;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.config.DriverConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

@ConfigurationProperties(prefix = "spring.data.neo4j")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/neo4j/Neo4jProperties.class */
public class Neo4jProperties implements ApplicationContextAware {
    static final String EMBEDDED_DRIVER = "org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver";
    static final String HTTP_DRIVER = "org.neo4j.ogm.drivers.http.driver.HttpDriver";
    static final String DEFAULT_HTTP_URI = "http://localhost:7474";
    static final String BOLT_DRIVER = "org.neo4j.ogm.drivers.bolt.driver.BoltDriver";
    private String uri;
    private String username;
    private String password;
    private String compiler;
    private final Embedded embedded = new Embedded();
    private ClassLoader classLoader = Neo4jProperties.class.getClassLoader();

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompiler() {
        return this.compiler;
    }

    public void setCompiler(String compiler) {
        this.compiler = compiler;
    }

    public Embedded getEmbedded() {
        return this.embedded;
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.classLoader = ctx.getClassLoader();
    }

    public Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configureDriver(configuration.driverConfiguration());
        if (this.compiler != null) {
            configuration.compilerConfiguration().setCompilerClassName(this.compiler);
        }
        return configuration;
    }

    private void configureDriver(DriverConfiguration driverConfiguration) {
        if (this.uri != null) {
            configureDriverFromUri(driverConfiguration, this.uri);
        } else {
            configureDriverWithDefaults(driverConfiguration);
        }
        if (this.username != null && this.password != null) {
            driverConfiguration.setCredentials(this.username, this.password);
        }
    }

    private void configureDriverFromUri(DriverConfiguration driverConfiguration, String uri) {
        driverConfiguration.setDriverClassName(deduceDriverFromUri());
        driverConfiguration.setURI(uri);
    }

    private String deduceDriverFromUri() {
        try {
            URI uri = new URI(this.uri);
            String scheme = uri.getScheme();
            if (scheme == null || scheme.equals("file")) {
                return EMBEDDED_DRIVER;
            }
            if ("http".equals(scheme) || "https".equals(scheme)) {
                return HTTP_DRIVER;
            }
            if ("bolt".equals(scheme)) {
                return BOLT_DRIVER;
            }
            throw new IllegalArgumentException("Could not deduce driver to use based on URI '" + uri + "'");
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid URI for spring.data.neo4j.uri '" + this.uri + "'", ex);
        }
    }

    private void configureDriverWithDefaults(DriverConfiguration driverConfiguration) {
        if (getEmbedded().isEnabled() && ClassUtils.isPresent(EMBEDDED_DRIVER, this.classLoader)) {
            driverConfiguration.setDriverClassName(EMBEDDED_DRIVER);
        } else {
            driverConfiguration.setDriverClassName(HTTP_DRIVER);
            driverConfiguration.setURI(DEFAULT_HTTP_URI);
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/neo4j/Neo4jProperties$Embedded.class */
    public static class Embedded {
        private boolean enabled = true;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
