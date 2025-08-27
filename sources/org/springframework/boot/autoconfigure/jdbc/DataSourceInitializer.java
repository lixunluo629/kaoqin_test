package org.springframework.boot.autoconfigure.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.config.SortedResourcesFactoryBean;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceInitializer.class */
class DataSourceInitializer implements ApplicationListener<DataSourceInitializedEvent> {
    private static final Log logger = LogFactory.getLog(DataSourceInitializer.class);
    private final DataSourceProperties properties;
    private final ApplicationContext applicationContext;
    private DataSource dataSource;
    private boolean initialized = false;

    DataSourceInitializer(DataSourceProperties properties, ApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() throws DataAccessException {
        if (!this.properties.isInitialize()) {
            logger.debug("Initialization disabled (not running DDL scripts)");
            return;
        }
        if (this.applicationContext.getBeanNamesForType(DataSource.class, false, false).length > 0) {
            this.dataSource = (DataSource) this.applicationContext.getBean(DataSource.class);
        }
        if (this.dataSource == null) {
            logger.debug("No DataSource found so not initializing");
        } else {
            runSchemaScripts();
        }
    }

    private void runSchemaScripts() throws DataAccessException {
        List<Resource> scripts = getScripts("spring.datasource.schema", this.properties.getSchema(), "schema");
        if (!scripts.isEmpty()) {
            String username = this.properties.getSchemaUsername();
            String password = this.properties.getSchemaPassword();
            runScripts(scripts, username, password);
            try {
                this.applicationContext.publishEvent((ApplicationEvent) new DataSourceInitializedEvent(this.dataSource));
                if (!this.initialized) {
                    runDataScripts();
                    this.initialized = true;
                }
            } catch (IllegalStateException ex) {
                logger.warn("Could not send event to complete DataSource initialization (" + ex.getMessage() + ")");
            }
        }
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(DataSourceInitializedEvent event) throws DataAccessException {
        if (!this.properties.isInitialize()) {
            logger.debug("Initialization disabled (not running data scripts)");
        } else if (!this.initialized) {
            runDataScripts();
            this.initialized = true;
        }
    }

    private void runDataScripts() throws DataAccessException {
        List<Resource> scripts = getScripts("spring.datasource.data", this.properties.getData(), "data");
        String username = this.properties.getDataUsername();
        String password = this.properties.getDataPassword();
        runScripts(scripts, username, password);
    }

    private List<Resource> getScripts(String propertyName, List<String> resources, String fallback) {
        if (resources != null) {
            return getResources(propertyName, resources, true);
        }
        String platform = this.properties.getPlatform();
        List<String> fallbackResources = new ArrayList<>();
        fallbackResources.add(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + fallback + "-" + platform + ".sql");
        fallbackResources.add(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + fallback + ".sql");
        return getResources(propertyName, fallbackResources, false);
    }

    private List<Resource> getResources(String propertyName, List<String> locations, boolean validate) {
        List<Resource> resources = new ArrayList<>();
        for (String location : locations) {
            for (Resource resource : doGetResources(location)) {
                if (resource.exists()) {
                    resources.add(resource);
                } else if (validate) {
                    throw new ResourceNotFoundException(propertyName, resource);
                }
            }
        }
        return resources;
    }

    private Resource[] doGetResources(String location) {
        try {
            SortedResourcesFactoryBean factory = new SortedResourcesFactoryBean(this.applicationContext, Collections.singletonList(location));
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to load resources from " + location, ex);
        }
    }

    private void runScripts(List<Resource> resources, String username, String password) throws DataAccessException {
        if (resources.isEmpty()) {
            return;
        }
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(this.properties.isContinueOnError());
        populator.setSeparator(this.properties.getSeparator());
        if (this.properties.getSqlScriptEncoding() != null) {
            populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
        }
        for (Resource resource : resources) {
            populator.addScript(resource);
        }
        DataSource dataSource = this.dataSource;
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            dataSource = DataSourceBuilder.create(this.properties.getClassLoader()).driverClassName(this.properties.determineDriverClassName()).url(this.properties.determineUrl()).username(username).password(password).build();
        }
        DatabasePopulatorUtils.execute(populator, dataSource);
    }
}
