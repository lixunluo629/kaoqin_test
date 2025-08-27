package com.zaxxer.hikari;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.mysql.jdbc.NonRegisteringDriver;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.util.PropertyElf;
import com.zaxxer.hikari.util.UtilityElf;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.security.AccessControlException;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/HikariConfig.class */
public class HikariConfig implements HikariConfigMXBean {
    private static final int DEFAULT_POOL_SIZE = 10;
    private volatile String catalog;
    private volatile long connectionTimeout;
    private volatile long validationTimeout;
    private volatile long idleTimeout;
    private volatile long leakDetectionThreshold;
    private volatile long maxLifetime;
    private volatile int maxPoolSize;
    private volatile int minIdle;
    private volatile String username;
    private volatile String password;
    private long initializationFailTimeout;
    private String connectionInitSql;
    private String connectionTestQuery;
    private String dataSourceClassName;
    private String dataSourceJndiName;
    private String driverClassName;
    private String exceptionOverrideClassName;
    private String jdbcUrl;
    private String poolName;
    private String schema;
    private String transactionIsolationName;
    private boolean isAutoCommit;
    private boolean isReadOnly;
    private boolean isIsolateInternalQueries;
    private boolean isRegisterMbeans;
    private boolean isAllowPoolSuspension;
    private DataSource dataSource;
    private Properties dataSourceProperties;
    private ThreadFactory threadFactory;
    private ScheduledExecutorService scheduledExecutor;
    private MetricsTrackerFactory metricsTrackerFactory;
    private Object metricRegistry;
    private Object healthCheckRegistry;
    private Properties healthCheckProperties;
    private volatile boolean sealed;
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) HikariConfig.class);
    private static final char[] ID_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final long CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30);
    private static final long VALIDATION_TIMEOUT = TimeUnit.SECONDS.toMillis(5);
    private static final long IDLE_TIMEOUT = TimeUnit.MINUTES.toMillis(10);
    private static final long MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30);
    private static boolean unitTest = false;

    public HikariConfig() throws IOException {
        this.dataSourceProperties = new Properties();
        this.healthCheckProperties = new Properties();
        this.minIdle = -1;
        this.maxPoolSize = -1;
        this.maxLifetime = MAX_LIFETIME;
        this.connectionTimeout = CONNECTION_TIMEOUT;
        this.validationTimeout = VALIDATION_TIMEOUT;
        this.idleTimeout = IDLE_TIMEOUT;
        this.initializationFailTimeout = 1L;
        this.isAutoCommit = true;
        String systemProp = System.getProperty("hikaricp.configurationFile");
        if (systemProp != null) {
            loadProperties(systemProp);
        }
    }

    public HikariConfig(Properties properties) {
        this();
        PropertyElf.setTargetFromProperties(this, properties);
    }

    public HikariConfig(String propertyFileName) throws IOException {
        this();
        loadProperties(propertyFileName);
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public String getCatalog() {
        return this.catalog;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public long getConnectionTimeout() {
        return this.connectionTimeout;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public void setConnectionTimeout(long connectionTimeoutMs) {
        if (connectionTimeoutMs == 0) {
            this.connectionTimeout = 2147483647L;
        } else {
            if (connectionTimeoutMs < 250) {
                throw new IllegalArgumentException("connectionTimeout cannot be less than 250ms");
            }
            this.connectionTimeout = connectionTimeoutMs;
        }
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public long getIdleTimeout() {
        return this.idleTimeout;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public void setIdleTimeout(long idleTimeoutMs) {
        if (idleTimeoutMs < 0) {
            throw new IllegalArgumentException("idleTimeout cannot be negative");
        }
        this.idleTimeout = idleTimeoutMs;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public long getLeakDetectionThreshold() {
        return this.leakDetectionThreshold;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public void setLeakDetectionThreshold(long leakDetectionThresholdMs) {
        this.leakDetectionThreshold = leakDetectionThresholdMs;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public long getMaxLifetime() {
        return this.maxLifetime;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public void setMaxLifetime(long maxLifetimeMs) {
        this.maxLifetime = maxLifetimeMs;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public int getMaximumPoolSize() {
        return this.maxPoolSize;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public void setMaximumPoolSize(int maxPoolSize) {
        if (maxPoolSize < 1) {
            throw new IllegalArgumentException("maxPoolSize cannot be less than 1");
        }
        this.maxPoolSize = maxPoolSize;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public int getMinimumIdle() {
        return this.minIdle;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public void setMinimumIdle(int minIdle) {
        if (minIdle < 0) {
            throw new IllegalArgumentException("minimumIdle cannot be negative");
        }
        this.minIdle = minIdle;
    }

    public String getPassword() {
        return this.password;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public void setUsername(String username) {
        this.username = username;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public long getValidationTimeout() {
        return this.validationTimeout;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public void setValidationTimeout(long validationTimeoutMs) {
        if (validationTimeoutMs < 250) {
            throw new IllegalArgumentException("validationTimeout cannot be less than 250ms");
        }
        this.validationTimeout = validationTimeoutMs;
    }

    public String getConnectionTestQuery() {
        return this.connectionTestQuery;
    }

    public void setConnectionTestQuery(String connectionTestQuery) {
        checkIfSealed();
        this.connectionTestQuery = connectionTestQuery;
    }

    public String getConnectionInitSql() {
        return this.connectionInitSql;
    }

    public void setConnectionInitSql(String connectionInitSql) {
        checkIfSealed();
        this.connectionInitSql = connectionInitSql;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        checkIfSealed();
        this.dataSource = dataSource;
    }

    public String getDataSourceClassName() {
        return this.dataSourceClassName;
    }

    public void setDataSourceClassName(String className) {
        checkIfSealed();
        this.dataSourceClassName = className;
    }

    public void addDataSourceProperty(String propertyName, Object value) {
        checkIfSealed();
        this.dataSourceProperties.put(propertyName, value);
    }

    public String getDataSourceJNDI() {
        return this.dataSourceJndiName;
    }

    public void setDataSourceJNDI(String jndiDataSource) {
        checkIfSealed();
        this.dataSourceJndiName = jndiDataSource;
    }

    public Properties getDataSourceProperties() {
        return this.dataSourceProperties;
    }

    public void setDataSourceProperties(Properties dsProperties) {
        checkIfSealed();
        this.dataSourceProperties.putAll(dsProperties);
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public void setDriverClassName(String driverClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        checkIfSealed();
        Class<?> driverClass = attemptFromContextLoader(driverClassName);
        if (driverClass == null) {
            try {
                driverClass = getClass().getClassLoader().loadClass(driverClassName);
                LOGGER.debug("Driver class {} found in the HikariConfig class classloader {}", driverClassName, getClass().getClassLoader());
            } catch (ClassNotFoundException e) {
                LOGGER.error("Failed to load driver class {} from HikariConfig class classloader {}", driverClassName, getClass().getClassLoader());
            }
        }
        if (driverClass == null) {
            throw new RuntimeException("Failed to load driver class " + driverClassName + " in either of HikariConfig class loader or Thread context classloader");
        }
        try {
            driverClass.getConstructor(new Class[0]).newInstance(new Object[0]);
            this.driverClassName = driverClassName;
        } catch (Exception e2) {
            throw new RuntimeException("Failed to instantiate class " + driverClassName, e2);
        }
    }

    public String getJdbcUrl() {
        return this.jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        checkIfSealed();
        this.jdbcUrl = jdbcUrl;
    }

    public boolean isAutoCommit() {
        return this.isAutoCommit;
    }

    public void setAutoCommit(boolean isAutoCommit) {
        checkIfSealed();
        this.isAutoCommit = isAutoCommit;
    }

    public boolean isAllowPoolSuspension() {
        return this.isAllowPoolSuspension;
    }

    public void setAllowPoolSuspension(boolean isAllowPoolSuspension) {
        checkIfSealed();
        this.isAllowPoolSuspension = isAllowPoolSuspension;
    }

    public long getInitializationFailTimeout() {
        return this.initializationFailTimeout;
    }

    public void setInitializationFailTimeout(long initializationFailTimeout) {
        checkIfSealed();
        this.initializationFailTimeout = initializationFailTimeout;
    }

    public boolean isIsolateInternalQueries() {
        return this.isIsolateInternalQueries;
    }

    public void setIsolateInternalQueries(boolean isolate) {
        checkIfSealed();
        this.isIsolateInternalQueries = isolate;
    }

    public MetricsTrackerFactory getMetricsTrackerFactory() {
        return this.metricsTrackerFactory;
    }

    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
        if (this.metricRegistry != null) {
            throw new IllegalStateException("cannot use setMetricsTrackerFactory() and setMetricRegistry() together");
        }
        this.metricsTrackerFactory = metricsTrackerFactory;
    }

    public Object getMetricRegistry() {
        return this.metricRegistry;
    }

    public void setMetricRegistry(Object metricRegistry) {
        if (this.metricsTrackerFactory != null) {
            throw new IllegalStateException("cannot use setMetricRegistry() and setMetricsTrackerFactory() together");
        }
        if (metricRegistry != null) {
            metricRegistry = getObjectOrPerformJndiLookup(metricRegistry);
            if (!UtilityElf.safeIsAssignableFrom(metricRegistry, "com.codahale.metrics.MetricRegistry") && !UtilityElf.safeIsAssignableFrom(metricRegistry, "io.micrometer.core.instrument.MeterRegistry")) {
                throw new IllegalArgumentException("Class must be instance of com.codahale.metrics.MetricRegistry or io.micrometer.core.instrument.MeterRegistry");
            }
        }
        this.metricRegistry = metricRegistry;
    }

    public Object getHealthCheckRegistry() {
        return this.healthCheckRegistry;
    }

    public void setHealthCheckRegistry(Object healthCheckRegistry) {
        checkIfSealed();
        if (healthCheckRegistry != null) {
            healthCheckRegistry = getObjectOrPerformJndiLookup(healthCheckRegistry);
            if (!(healthCheckRegistry instanceof HealthCheckRegistry)) {
                throw new IllegalArgumentException("Class must be an instance of com.codahale.metrics.health.HealthCheckRegistry");
            }
        }
        this.healthCheckRegistry = healthCheckRegistry;
    }

    public Properties getHealthCheckProperties() {
        return this.healthCheckProperties;
    }

    public void setHealthCheckProperties(Properties healthCheckProperties) {
        checkIfSealed();
        this.healthCheckProperties.putAll(healthCheckProperties);
    }

    public void addHealthCheckProperty(String key, String value) {
        checkIfSealed();
        this.healthCheckProperties.setProperty(key, value);
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        checkIfSealed();
        this.isReadOnly = readOnly;
    }

    public boolean isRegisterMbeans() {
        return this.isRegisterMbeans;
    }

    public void setRegisterMbeans(boolean register) {
        checkIfSealed();
        this.isRegisterMbeans = register;
    }

    @Override // com.zaxxer.hikari.HikariConfigMXBean
    public String getPoolName() {
        return this.poolName;
    }

    public void setPoolName(String poolName) {
        checkIfSealed();
        this.poolName = poolName;
    }

    public ScheduledExecutorService getScheduledExecutor() {
        return this.scheduledExecutor;
    }

    public void setScheduledExecutor(ScheduledExecutorService executor) {
        checkIfSealed();
        this.scheduledExecutor = executor;
    }

    public String getTransactionIsolation() {
        return this.transactionIsolationName;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        checkIfSealed();
        this.schema = schema;
    }

    public String getExceptionOverrideClassName() {
        return this.exceptionOverrideClassName;
    }

    public void setExceptionOverrideClassName(String exceptionOverrideClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        checkIfSealed();
        Class<?> overrideClass = attemptFromContextLoader(exceptionOverrideClassName);
        if (overrideClass == null) {
            try {
                overrideClass = getClass().getClassLoader().loadClass(exceptionOverrideClassName);
                LOGGER.debug("SQLExceptionOverride class {} found in the HikariConfig class classloader {}", exceptionOverrideClassName, getClass().getClassLoader());
            } catch (ClassNotFoundException e) {
                LOGGER.error("Failed to load SQLExceptionOverride class {} from HikariConfig class classloader {}", exceptionOverrideClassName, getClass().getClassLoader());
            }
        }
        if (overrideClass == null) {
            throw new RuntimeException("Failed to load SQLExceptionOverride class " + exceptionOverrideClassName + " in either of HikariConfig class loader or Thread context classloader");
        }
        try {
            overrideClass.getConstructor(new Class[0]).newInstance(new Object[0]);
            this.exceptionOverrideClassName = exceptionOverrideClassName;
        } catch (Exception e2) {
            throw new RuntimeException("Failed to instantiate class " + exceptionOverrideClassName, e2);
        }
    }

    public void setTransactionIsolation(String isolationLevel) {
        checkIfSealed();
        this.transactionIsolationName = isolationLevel;
    }

    public ThreadFactory getThreadFactory() {
        return this.threadFactory;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        checkIfSealed();
        this.threadFactory = threadFactory;
    }

    void seal() {
        this.sealed = true;
    }

    public void copyStateTo(HikariConfig other) throws IllegalAccessException, IllegalArgumentException {
        for (Field field : HikariConfig.class.getDeclaredFields()) {
            if (!Modifier.isFinal(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    field.set(other, field.get(this));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to copy HikariConfig state: " + e.getMessage(), e);
                }
            }
        }
        other.sealed = false;
    }

    private Class<?> attemptFromContextLoader(String driverClassName) throws ClassNotFoundException {
        ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
        if (threadContextClassLoader != null) {
            try {
                Class<?> driverClass = threadContextClassLoader.loadClass(driverClassName);
                LOGGER.debug("Driver class {} found in Thread context class loader {}", driverClassName, threadContextClassLoader);
                return driverClass;
            } catch (ClassNotFoundException e) {
                LOGGER.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}", driverClassName, threadContextClassLoader, getClass().getClassLoader());
                return null;
            }
        }
        return null;
    }

    public void validate() {
        if (this.poolName == null) {
            this.poolName = generatePoolName();
        } else if (this.isRegisterMbeans && this.poolName.contains(":")) {
            throw new IllegalArgumentException("poolName cannot contain ':' when used with JMX");
        }
        this.catalog = UtilityElf.getNullIfEmpty(this.catalog);
        this.connectionInitSql = UtilityElf.getNullIfEmpty(this.connectionInitSql);
        this.connectionTestQuery = UtilityElf.getNullIfEmpty(this.connectionTestQuery);
        this.transactionIsolationName = UtilityElf.getNullIfEmpty(this.transactionIsolationName);
        this.dataSourceClassName = UtilityElf.getNullIfEmpty(this.dataSourceClassName);
        this.dataSourceJndiName = UtilityElf.getNullIfEmpty(this.dataSourceJndiName);
        this.driverClassName = UtilityElf.getNullIfEmpty(this.driverClassName);
        this.jdbcUrl = UtilityElf.getNullIfEmpty(this.jdbcUrl);
        if (this.dataSource != null) {
            if (this.dataSourceClassName != null) {
                LOGGER.warn("{} - using dataSource and ignoring dataSourceClassName.", this.poolName);
            }
        } else if (this.dataSourceClassName != null) {
            if (this.driverClassName != null) {
                LOGGER.error("{} - cannot use driverClassName and dataSourceClassName together.", this.poolName);
                throw new IllegalStateException("cannot use driverClassName and dataSourceClassName together.");
            }
            if (this.jdbcUrl != null) {
                LOGGER.warn("{} - using dataSourceClassName and ignoring jdbcUrl.", this.poolName);
            }
        } else if (this.jdbcUrl == null && this.dataSourceJndiName == null) {
            if (this.driverClassName != null) {
                LOGGER.error("{} - jdbcUrl is required with driverClassName.", this.poolName);
                throw new IllegalArgumentException("jdbcUrl is required with driverClassName.");
            }
            LOGGER.error("{} - dataSource or dataSourceClassName or jdbcUrl is required.", this.poolName);
            throw new IllegalArgumentException("dataSource or dataSourceClassName or jdbcUrl is required.");
        }
        validateNumerics();
        if (LOGGER.isDebugEnabled() || unitTest) {
            logConfiguration();
        }
    }

    private void validateNumerics() {
        if (this.maxLifetime != 0 && this.maxLifetime < TimeUnit.SECONDS.toMillis(30L)) {
            LOGGER.warn("{} - maxLifetime is less than 30000ms, setting to default {}ms.", this.poolName, Long.valueOf(MAX_LIFETIME));
            this.maxLifetime = MAX_LIFETIME;
        }
        if (this.leakDetectionThreshold > 0 && !unitTest && (this.leakDetectionThreshold < TimeUnit.SECONDS.toMillis(2L) || (this.leakDetectionThreshold > this.maxLifetime && this.maxLifetime > 0))) {
            LOGGER.warn("{} - leakDetectionThreshold is less than 2000ms or more than maxLifetime, disabling it.", this.poolName);
            this.leakDetectionThreshold = 0L;
        }
        if (this.connectionTimeout < 250) {
            LOGGER.warn("{} - connectionTimeout is less than 250ms, setting to {}ms.", this.poolName, Long.valueOf(CONNECTION_TIMEOUT));
            this.connectionTimeout = CONNECTION_TIMEOUT;
        }
        if (this.validationTimeout < 250) {
            LOGGER.warn("{} - validationTimeout is less than 250ms, setting to {}ms.", this.poolName, Long.valueOf(VALIDATION_TIMEOUT));
            this.validationTimeout = VALIDATION_TIMEOUT;
        }
        if (this.maxPoolSize < 1) {
            this.maxPoolSize = 10;
        }
        if (this.minIdle < 0 || this.minIdle > this.maxPoolSize) {
            this.minIdle = this.maxPoolSize;
        }
        if (this.idleTimeout + TimeUnit.SECONDS.toMillis(1L) > this.maxLifetime && this.maxLifetime > 0 && this.minIdle < this.maxPoolSize) {
            LOGGER.warn("{} - idleTimeout is close to or more than maxLifetime, disabling it.", this.poolName);
            this.idleTimeout = 0L;
            return;
        }
        if (this.idleTimeout != 0 && this.idleTimeout < TimeUnit.SECONDS.toMillis(10L) && this.minIdle < this.maxPoolSize) {
            LOGGER.warn("{} - idleTimeout is less than 10000ms, setting to default {}ms.", this.poolName, Long.valueOf(IDLE_TIMEOUT));
            this.idleTimeout = IDLE_TIMEOUT;
        } else if (this.idleTimeout != IDLE_TIMEOUT && this.idleTimeout != 0 && this.minIdle == this.maxPoolSize) {
            LOGGER.warn("{} - idleTimeout has been set but has no effect because the pool is operating as a fixed size pool.", this.poolName);
        }
    }

    private void checkIfSealed() {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started. Use HikariConfigMXBean for runtime changes.");
        }
    }

    private void logConfiguration() {
        LOGGER.debug("{} - configuration:", this.poolName);
        Set<String> propertyNames = new TreeSet<>(PropertyElf.getPropertyNames(HikariConfig.class));
        for (String prop : propertyNames) {
            try {
                Object value = PropertyElf.getProperty(prop, this);
                if ("dataSourceProperties".equals(prop)) {
                    Properties dsProps = PropertyElf.copyProperties(this.dataSourceProperties);
                    dsProps.setProperty(NonRegisteringDriver.PASSWORD_PROPERTY_KEY, "<masked>");
                    value = dsProps;
                }
                if ("initializationFailTimeout".equals(prop) && this.initializationFailTimeout == Long.MAX_VALUE) {
                    value = "infinite";
                } else if ("transactionIsolation".equals(prop) && this.transactionIsolationName == null) {
                    value = "default";
                } else if (prop.matches("scheduledExecutorService|threadFactory") && value == null) {
                    value = "internal";
                } else if (prop.contains("jdbcUrl") && (value instanceof String)) {
                    value = ((String) value).replaceAll("([?&;]password=)[^&#;]*(.*)", "$1<masked>$2");
                } else if (prop.contains(NonRegisteringDriver.PASSWORD_PROPERTY_KEY)) {
                    value = "<masked>";
                } else if (value instanceof String) {
                    value = SymbolConstants.QUOTES_SYMBOL + value + SymbolConstants.QUOTES_SYMBOL;
                } else if (value == null) {
                    value = "none";
                }
                LOGGER.debug((prop + "................................................").substring(0, 32) + value);
            } catch (Exception e) {
            }
        }
    }

    private void loadProperties(String propertyFileName) throws IOException {
        File propFile = new File(propertyFileName);
        try {
            InputStream is = propFile.isFile() ? new FileInputStream(propFile) : getClass().getResourceAsStream(propertyFileName);
            try {
                if (is != null) {
                    Properties props = new Properties();
                    props.load(is);
                    PropertyElf.setTargetFromProperties(this, props);
                    if (is != null) {
                        is.close();
                    }
                    return;
                }
                throw new IllegalArgumentException("Cannot find property file: " + propertyFileName);
            } finally {
            }
        } catch (IOException io2) {
            throw new RuntimeException("Failed to read property file", io2);
        }
    }

    private String generatePoolName() {
        String str;
        try {
            synchronized (System.getProperties()) {
                String next = String.valueOf(Integer.getInteger("com.zaxxer.hikari.pool_number", 0).intValue() + 1);
                System.setProperty("com.zaxxer.hikari.pool_number", next);
                str = "HikariPool-" + next;
            }
            return str;
        } catch (AccessControlException e) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            StringBuilder buf = new StringBuilder("HikariPool-");
            for (int i = 0; i < 4; i++) {
                buf.append(ID_CHARACTERS[random.nextInt(62)]);
            }
            LOGGER.info("assigned random pool name '{}' (security manager prevented access to system properties)", buf);
            return buf.toString();
        }
    }

    private Object getObjectOrPerformJndiLookup(Object object) {
        if (object instanceof String) {
            try {
                InitialContext initCtx = new InitialContext();
                return initCtx.lookup((String) object);
            } catch (NamingException e) {
                throw new IllegalArgumentException((Throwable) e);
            }
        }
        return object;
    }
}
