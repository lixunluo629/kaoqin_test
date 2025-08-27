package org.springframework.boot.jta.atomikos;

import java.util.Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.jta.atomikos.properties")
/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/atomikos/AtomikosProperties.class */
public class AtomikosProperties {
    private String service;
    private String transactionManagerUniqueName;
    private boolean forceShutdownOnVmExit;
    private String logBaseDir;
    private boolean threadedTwoPhaseCommit;
    private long maxTimeout = 300000;
    private long defaultJtaTimeout = 10000;
    private int maxActives = 50;
    private boolean enableLogging = true;
    private boolean serialJtaTransactions = true;
    private String logBaseName = "tmlog";
    private long checkpointInterval = 500;

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return this.service;
    }

    public void setMaxTimeout(long maxTimeout) {
        this.maxTimeout = maxTimeout;
    }

    public long getMaxTimeout() {
        return this.maxTimeout;
    }

    public void setDefaultJtaTimeout(long defaultJtaTimeout) {
        this.defaultJtaTimeout = defaultJtaTimeout;
    }

    public long getDefaultJtaTimeout() {
        return this.defaultJtaTimeout;
    }

    public void setMaxActives(int maxActives) {
        this.maxActives = maxActives;
    }

    public int getMaxActives() {
        return this.maxActives;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public boolean isEnableLogging() {
        return this.enableLogging;
    }

    public void setTransactionManagerUniqueName(String uniqueName) {
        this.transactionManagerUniqueName = uniqueName;
    }

    public String getTransactionManagerUniqueName() {
        return this.transactionManagerUniqueName;
    }

    public void setSerialJtaTransactions(boolean serialJtaTransactions) {
        this.serialJtaTransactions = serialJtaTransactions;
    }

    public boolean isSerialJtaTransactions() {
        return this.serialJtaTransactions;
    }

    public void setForceShutdownOnVmExit(boolean forceShutdownOnVmExit) {
        this.forceShutdownOnVmExit = forceShutdownOnVmExit;
    }

    public boolean isForceShutdownOnVmExit() {
        return this.forceShutdownOnVmExit;
    }

    public void setLogBaseName(String logBaseName) {
        this.logBaseName = logBaseName;
    }

    public String getLogBaseName() {
        return this.logBaseName;
    }

    public void setLogBaseDir(String logBaseDir) {
        this.logBaseDir = logBaseDir;
    }

    public String getLogBaseDir() {
        return this.logBaseDir;
    }

    public void setCheckpointInterval(long checkpointInterval) {
        this.checkpointInterval = checkpointInterval;
    }

    public long getCheckpointInterval() {
        return this.checkpointInterval;
    }

    public void setThreadedTwoPhaseCommit(boolean threadedTwoPhaseCommit) {
        this.threadedTwoPhaseCommit = threadedTwoPhaseCommit;
    }

    public boolean isThreadedTwoPhaseCommit() {
        return this.threadedTwoPhaseCommit;
    }

    public Properties asProperties() {
        Properties properties = new Properties();
        set(properties, "service", getService());
        set(properties, "max_timeout", Long.valueOf(getMaxTimeout()));
        set(properties, "default_jta_timeout", Long.valueOf(getDefaultJtaTimeout()));
        set(properties, "max_actives", Integer.valueOf(getMaxActives()));
        set(properties, "enable_logging", Boolean.valueOf(isEnableLogging()));
        set(properties, "tm_unique_name", getTransactionManagerUniqueName());
        set(properties, "serial_jta_transactions", Boolean.valueOf(isSerialJtaTransactions()));
        set(properties, "force_shutdown_on_vm_exit", Boolean.valueOf(isForceShutdownOnVmExit()));
        set(properties, "log_base_name", getLogBaseName());
        set(properties, "log_base_dir", getLogBaseDir());
        set(properties, "checkpoint_interval", Long.valueOf(getCheckpointInterval()));
        set(properties, "threaded_2pc", Boolean.valueOf(isThreadedTwoPhaseCommit()));
        return properties;
    }

    private void set(Properties properties, String key, Object value) {
        String id = "com.atomikos.icatch." + key;
        if (value != null && !properties.containsKey(id)) {
            properties.setProperty(id, value.toString());
        }
    }
}
