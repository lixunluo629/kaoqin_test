package org.springframework.boot.autoconfigure.kafka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.util.CollectionUtils;

@ConfigurationProperties(prefix = "spring.kafka")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/KafkaProperties.class */
public class KafkaProperties {
    private String clientId;
    private List<String> bootstrapServers = new ArrayList(Collections.singletonList("localhost:9092"));
    private Map<String, String> properties = new HashMap();
    private final Consumer consumer = new Consumer();
    private final Producer producer = new Producer();
    private final Listener listener = new Listener();
    private final Ssl ssl = new Ssl();
    private final Template template = new Template();

    public List<String> getBootstrapServers() {
        return this.bootstrapServers;
    }

    public void setBootstrapServers(List<String> bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Consumer getConsumer() {
        return this.consumer;
    }

    public Producer getProducer() {
        return this.producer;
    }

    public Listener getListener() {
        return this.listener;
    }

    public Ssl getSsl() {
        return this.ssl;
    }

    public Template getTemplate() {
        return this.template;
    }

    private Map<String, Object> buildCommonProperties() {
        Map<String, Object> properties = new HashMap<>();
        if (this.bootstrapServers != null) {
            properties.put("bootstrap.servers", this.bootstrapServers);
        }
        if (this.clientId != null) {
            properties.put("client.id", this.clientId);
        }
        if (this.ssl.getKeyPassword() != null) {
            properties.put("ssl.key.password", this.ssl.getKeyPassword());
        }
        if (this.ssl.getKeystoreLocation() != null) {
            properties.put("ssl.keystore.location", resourceToPath(this.ssl.getKeystoreLocation()));
        }
        if (this.ssl.getKeystorePassword() != null) {
            properties.put("ssl.keystore.password", this.ssl.getKeystorePassword());
        }
        if (this.ssl.getTruststoreLocation() != null) {
            properties.put("ssl.truststore.location", resourceToPath(this.ssl.getTruststoreLocation()));
        }
        if (this.ssl.getTruststorePassword() != null) {
            properties.put("ssl.truststore.password", this.ssl.getTruststorePassword());
        }
        if (!CollectionUtils.isEmpty(this.properties)) {
            properties.putAll(this.properties);
        }
        return properties;
    }

    public Map<String, Object> buildConsumerProperties() {
        Map<String, Object> properties = buildCommonProperties();
        properties.putAll(this.consumer.buildProperties());
        return properties;
    }

    public Map<String, Object> buildProducerProperties() {
        Map<String, Object> properties = buildCommonProperties();
        properties.putAll(this.producer.buildProperties());
        return properties;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String resourceToPath(Resource resource) {
        try {
            return resource.getFile().getAbsolutePath();
        } catch (IOException ex) {
            throw new IllegalStateException("Resource '" + resource + "' must be on a file system", ex);
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/KafkaProperties$Consumer.class */
    public static class Consumer {
        private Integer autoCommitInterval;
        private String autoOffsetReset;
        private List<String> bootstrapServers;
        private String clientId;
        private Boolean enableAutoCommit;
        private Integer fetchMaxWait;
        private Integer fetchMinSize;
        private String groupId;
        private Integer heartbeatInterval;
        private Integer maxPollRecords;
        private final Ssl ssl = new Ssl();
        private Class<?> keyDeserializer = StringDeserializer.class;
        private Class<?> valueDeserializer = StringDeserializer.class;

        public Ssl getSsl() {
            return this.ssl;
        }

        public Integer getAutoCommitInterval() {
            return this.autoCommitInterval;
        }

        public void setAutoCommitInterval(Integer autoCommitInterval) {
            this.autoCommitInterval = autoCommitInterval;
        }

        public String getAutoOffsetReset() {
            return this.autoOffsetReset;
        }

        public void setAutoOffsetReset(String autoOffsetReset) {
            this.autoOffsetReset = autoOffsetReset;
        }

        public List<String> getBootstrapServers() {
            return this.bootstrapServers;
        }

        public void setBootstrapServers(List<String> bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }

        public String getClientId() {
            return this.clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public Boolean getEnableAutoCommit() {
            return this.enableAutoCommit;
        }

        public void setEnableAutoCommit(Boolean enableAutoCommit) {
            this.enableAutoCommit = enableAutoCommit;
        }

        public Integer getFetchMaxWait() {
            return this.fetchMaxWait;
        }

        public void setFetchMaxWait(Integer fetchMaxWait) {
            this.fetchMaxWait = fetchMaxWait;
        }

        public Integer getFetchMinSize() {
            return this.fetchMinSize;
        }

        public void setFetchMinSize(Integer fetchMinSize) {
            this.fetchMinSize = fetchMinSize;
        }

        public String getGroupId() {
            return this.groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public Integer getHeartbeatInterval() {
            return this.heartbeatInterval;
        }

        public void setHeartbeatInterval(Integer heartbeatInterval) {
            this.heartbeatInterval = heartbeatInterval;
        }

        public Class<?> getKeyDeserializer() {
            return this.keyDeserializer;
        }

        public void setKeyDeserializer(Class<?> keyDeserializer) {
            this.keyDeserializer = keyDeserializer;
        }

        public Class<?> getValueDeserializer() {
            return this.valueDeserializer;
        }

        public void setValueDeserializer(Class<?> valueDeserializer) {
            this.valueDeserializer = valueDeserializer;
        }

        public Integer getMaxPollRecords() {
            return this.maxPollRecords;
        }

        public void setMaxPollRecords(Integer maxPollRecords) {
            this.maxPollRecords = maxPollRecords;
        }

        public Map<String, Object> buildProperties() {
            Map<String, Object> properties = new HashMap<>();
            if (this.autoCommitInterval != null) {
                properties.put("auto.commit.interval.ms", this.autoCommitInterval);
            }
            if (this.autoOffsetReset != null) {
                properties.put("auto.offset.reset", this.autoOffsetReset);
            }
            if (this.bootstrapServers != null) {
                properties.put("bootstrap.servers", this.bootstrapServers);
            }
            if (this.clientId != null) {
                properties.put("client.id", this.clientId);
            }
            if (this.enableAutoCommit != null) {
                properties.put("enable.auto.commit", this.enableAutoCommit);
            }
            if (this.fetchMaxWait != null) {
                properties.put("fetch.max.wait.ms", this.fetchMaxWait);
            }
            if (this.fetchMinSize != null) {
                properties.put("fetch.min.bytes", this.fetchMinSize);
            }
            if (this.groupId != null) {
                properties.put("group.id", this.groupId);
            }
            if (this.heartbeatInterval != null) {
                properties.put("heartbeat.interval.ms", this.heartbeatInterval);
            }
            if (this.keyDeserializer != null) {
                properties.put("key.deserializer", this.keyDeserializer);
            }
            if (this.ssl.getKeyPassword() != null) {
                properties.put("ssl.key.password", this.ssl.getKeyPassword());
            }
            if (this.ssl.getKeystoreLocation() != null) {
                properties.put("ssl.keystore.location", KafkaProperties.resourceToPath(this.ssl.getKeystoreLocation()));
            }
            if (this.ssl.getKeystorePassword() != null) {
                properties.put("ssl.keystore.password", this.ssl.getKeystorePassword());
            }
            if (this.ssl.getTruststoreLocation() != null) {
                properties.put("ssl.truststore.location", KafkaProperties.resourceToPath(this.ssl.getTruststoreLocation()));
            }
            if (this.ssl.getTruststorePassword() != null) {
                properties.put("ssl.truststore.password", this.ssl.getTruststorePassword());
            }
            if (this.valueDeserializer != null) {
                properties.put("value.deserializer", this.valueDeserializer);
            }
            if (this.maxPollRecords != null) {
                properties.put("max.poll.records", this.maxPollRecords);
            }
            return properties;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/KafkaProperties$Producer.class */
    public static class Producer {
        private String acks;
        private Integer batchSize;
        private List<String> bootstrapServers;
        private Long bufferMemory;
        private String clientId;
        private String compressionType;
        private Integer retries;
        private final Ssl ssl = new Ssl();
        private Class<?> keySerializer = StringSerializer.class;
        private Class<?> valueSerializer = StringSerializer.class;

        public Ssl getSsl() {
            return this.ssl;
        }

        public String getAcks() {
            return this.acks;
        }

        public void setAcks(String acks) {
            this.acks = acks;
        }

        public Integer getBatchSize() {
            return this.batchSize;
        }

        public void setBatchSize(Integer batchSize) {
            this.batchSize = batchSize;
        }

        public List<String> getBootstrapServers() {
            return this.bootstrapServers;
        }

        public void setBootstrapServers(List<String> bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }

        public Long getBufferMemory() {
            return this.bufferMemory;
        }

        public void setBufferMemory(Long bufferMemory) {
            this.bufferMemory = bufferMemory;
        }

        public String getClientId() {
            return this.clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getCompressionType() {
            return this.compressionType;
        }

        public void setCompressionType(String compressionType) {
            this.compressionType = compressionType;
        }

        public Class<?> getKeySerializer() {
            return this.keySerializer;
        }

        public void setKeySerializer(Class<?> keySerializer) {
            this.keySerializer = keySerializer;
        }

        public Class<?> getValueSerializer() {
            return this.valueSerializer;
        }

        public void setValueSerializer(Class<?> valueSerializer) {
            this.valueSerializer = valueSerializer;
        }

        public Integer getRetries() {
            return this.retries;
        }

        public void setRetries(Integer retries) {
            this.retries = retries;
        }

        public Map<String, Object> buildProperties() {
            Map<String, Object> properties = new HashMap<>();
            if (this.acks != null) {
                properties.put("acks", this.acks);
            }
            if (this.batchSize != null) {
                properties.put("batch.size", this.batchSize);
            }
            if (this.bootstrapServers != null) {
                properties.put("bootstrap.servers", this.bootstrapServers);
            }
            if (this.bufferMemory != null) {
                properties.put("buffer.memory", this.bufferMemory);
            }
            if (this.clientId != null) {
                properties.put("client.id", this.clientId);
            }
            if (this.compressionType != null) {
                properties.put("compression.type", this.compressionType);
            }
            if (this.keySerializer != null) {
                properties.put("key.serializer", this.keySerializer);
            }
            if (this.retries != null) {
                properties.put("retries", this.retries);
            }
            if (this.ssl.getKeyPassword() != null) {
                properties.put("ssl.key.password", this.ssl.getKeyPassword());
            }
            if (this.ssl.getKeystoreLocation() != null) {
                properties.put("ssl.keystore.location", KafkaProperties.resourceToPath(this.ssl.getKeystoreLocation()));
            }
            if (this.ssl.getKeystorePassword() != null) {
                properties.put("ssl.keystore.password", this.ssl.getKeystorePassword());
            }
            if (this.ssl.getTruststoreLocation() != null) {
                properties.put("ssl.truststore.location", KafkaProperties.resourceToPath(this.ssl.getTruststoreLocation()));
            }
            if (this.ssl.getTruststorePassword() != null) {
                properties.put("ssl.truststore.password", this.ssl.getTruststorePassword());
            }
            if (this.valueSerializer != null) {
                properties.put("value.serializer", this.valueSerializer);
            }
            return properties;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/KafkaProperties$Template.class */
    public static class Template {
        private String defaultTopic;

        public String getDefaultTopic() {
            return this.defaultTopic;
        }

        public void setDefaultTopic(String defaultTopic) {
            this.defaultTopic = defaultTopic;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/KafkaProperties$Listener.class */
    public static class Listener {
        private AbstractMessageListenerContainer.AckMode ackMode;
        private Integer concurrency;
        private Long pollTimeout;
        private Integer ackCount;
        private Long ackTime;

        public AbstractMessageListenerContainer.AckMode getAckMode() {
            return this.ackMode;
        }

        public void setAckMode(AbstractMessageListenerContainer.AckMode ackMode) {
            this.ackMode = ackMode;
        }

        public Integer getConcurrency() {
            return this.concurrency;
        }

        public void setConcurrency(Integer concurrency) {
            this.concurrency = concurrency;
        }

        public Long getPollTimeout() {
            return this.pollTimeout;
        }

        public void setPollTimeout(Long pollTimeout) {
            this.pollTimeout = pollTimeout;
        }

        public Integer getAckCount() {
            return this.ackCount;
        }

        public void setAckCount(Integer ackCount) {
            this.ackCount = ackCount;
        }

        public Long getAckTime() {
            return this.ackTime;
        }

        public void setAckTime(Long ackTime) {
            this.ackTime = ackTime;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/KafkaProperties$Ssl.class */
    public static class Ssl {
        private String keyPassword;
        private Resource keystoreLocation;
        private String keystorePassword;
        private Resource truststoreLocation;
        private String truststorePassword;

        public String getKeyPassword() {
            return this.keyPassword;
        }

        public void setKeyPassword(String keyPassword) {
            this.keyPassword = keyPassword;
        }

        public Resource getKeystoreLocation() {
            return this.keystoreLocation;
        }

        public void setKeystoreLocation(Resource keystoreLocation) {
            this.keystoreLocation = keystoreLocation;
        }

        public String getKeystorePassword() {
            return this.keystorePassword;
        }

        public void setKeystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
        }

        public Resource getTruststoreLocation() {
            return this.truststoreLocation;
        }

        public void setTruststoreLocation(Resource truststoreLocation) {
            this.truststoreLocation = truststoreLocation;
        }

        public String getTruststorePassword() {
            return this.truststorePassword;
        }

        public void setTruststorePassword(String truststorePassword) {
            this.truststorePassword = truststorePassword;
        }
    }
}
