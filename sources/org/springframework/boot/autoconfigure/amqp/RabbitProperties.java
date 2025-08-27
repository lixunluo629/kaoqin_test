package org.springframework.boot.autoconfigure.amqp;

import java.util.ArrayList;
import java.util.List;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "spring.rabbitmq")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties.class */
public class RabbitProperties {
    private String username;
    private String password;
    private String virtualHost;
    private String addresses;
    private Integer requestedHeartbeat;
    private boolean publisherConfirms;
    private boolean publisherReturns;
    private Integer connectionTimeout;
    private List<Address> parsedAddresses;
    private String host = "localhost";
    private int port = 5672;
    private final Ssl ssl = new Ssl();
    private final Cache cache = new Cache();
    private final Listener listener = new Listener();
    private final Template template = new Template();

    public String getHost() {
        return this.host;
    }

    public String determineHost() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return getHost();
        }
        return this.parsedAddresses.get(0).host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public int determinePort() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return getPort();
        }
        Address address = this.parsedAddresses.get(0);
        return address.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddresses() {
        return this.addresses;
    }

    public String determineAddresses() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return this.host + ":" + this.port;
        }
        List<String> addressStrings = new ArrayList<>();
        for (Address parsedAddress : this.parsedAddresses) {
            addressStrings.add(parsedAddress.host + ":" + parsedAddress.port);
        }
        return StringUtils.collectionToCommaDelimitedString(addressStrings);
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
        this.parsedAddresses = parseAddresses(addresses);
    }

    private List<Address> parseAddresses(String addresses) {
        List<Address> parsedAddresses = new ArrayList<>();
        for (String address : StringUtils.commaDelimitedListToStringArray(addresses)) {
            parsedAddresses.add(new Address(address));
        }
        return parsedAddresses;
    }

    public String getUsername() {
        return this.username;
    }

    public String determineUsername() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return this.username;
        }
        Address address = this.parsedAddresses.get(0);
        return address.username != null ? address.username : this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public String determinePassword() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return getPassword();
        }
        Address address = this.parsedAddresses.get(0);
        return address.password != null ? address.password : getPassword();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Ssl getSsl() {
        return this.ssl;
    }

    public String getVirtualHost() {
        return this.virtualHost;
    }

    public String determineVirtualHost() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return getVirtualHost();
        }
        Address address = this.parsedAddresses.get(0);
        return address.virtualHost != null ? address.virtualHost : getVirtualHost();
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = "".equals(virtualHost) ? "/" : virtualHost;
    }

    public Integer getRequestedHeartbeat() {
        return this.requestedHeartbeat;
    }

    public void setRequestedHeartbeat(Integer requestedHeartbeat) {
        this.requestedHeartbeat = requestedHeartbeat;
    }

    public boolean isPublisherConfirms() {
        return this.publisherConfirms;
    }

    public void setPublisherConfirms(boolean publisherConfirms) {
        this.publisherConfirms = publisherConfirms;
    }

    public boolean isPublisherReturns() {
        return this.publisherReturns;
    }

    public void setPublisherReturns(boolean publisherReturns) {
        this.publisherReturns = publisherReturns;
    }

    public Integer getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Cache getCache() {
        return this.cache;
    }

    public Listener getListener() {
        return this.listener;
    }

    public Template getTemplate() {
        return this.template;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties$Ssl.class */
    public static class Ssl {
        private boolean enabled;
        private String keyStore;
        private String keyStorePassword;
        private String trustStore;
        private String trustStorePassword;
        private String algorithm;
        private boolean validateServerCertificate = true;
        private Boolean verifyHostname;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getKeyStore() {
            return this.keyStore;
        }

        public void setKeyStore(String keyStore) {
            this.keyStore = keyStore;
        }

        public String getKeyStorePassword() {
            return this.keyStorePassword;
        }

        public void setKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
        }

        public String getTrustStore() {
            return this.trustStore;
        }

        public void setTrustStore(String trustStore) {
            this.trustStore = trustStore;
        }

        public String getTrustStorePassword() {
            return this.trustStorePassword;
        }

        public void setTrustStorePassword(String trustStorePassword) {
            this.trustStorePassword = trustStorePassword;
        }

        public String getAlgorithm() {
            return this.algorithm;
        }

        public void setAlgorithm(String sslAlgorithm) {
            this.algorithm = sslAlgorithm;
        }

        public boolean isValidateServerCertificate() {
            return this.validateServerCertificate;
        }

        public void setValidateServerCertificate(boolean validateServerCertificate) {
            this.validateServerCertificate = validateServerCertificate;
        }

        public Boolean getVerifyHostname() {
            return this.verifyHostname;
        }

        public void setVerifyHostname(Boolean verifyHostname) {
            this.verifyHostname = verifyHostname;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties$Cache.class */
    public static class Cache {
        private final Channel channel = new Channel();
        private final Connection connection = new Connection();

        public Channel getChannel() {
            return this.channel;
        }

        public Connection getConnection() {
            return this.connection;
        }

        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties$Cache$Channel.class */
        public static class Channel {
            private Integer size;
            private Long checkoutTimeout;

            public Integer getSize() {
                return this.size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Long getCheckoutTimeout() {
                return this.checkoutTimeout;
            }

            public void setCheckoutTimeout(Long checkoutTimeout) {
                this.checkoutTimeout = checkoutTimeout;
            }
        }

        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties$Cache$Connection.class */
        public static class Connection {
            private CachingConnectionFactory.CacheMode mode = CachingConnectionFactory.CacheMode.CHANNEL;
            private Integer size;

            public CachingConnectionFactory.CacheMode getMode() {
                return this.mode;
            }

            public void setMode(CachingConnectionFactory.CacheMode mode) {
                this.mode = mode;
            }

            public Integer getSize() {
                return this.size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties$Listener.class */
    public static class Listener {

        @NestedConfigurationProperty
        private final AmqpContainer simple = new AmqpContainer();

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.auto-startup")
        @Deprecated
        public boolean isAutoStartup() {
            return getSimple().isAutoStartup();
        }

        @Deprecated
        public void setAutoStartup(boolean autoStartup) {
            getSimple().setAutoStartup(autoStartup);
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.acknowledge-mode")
        @Deprecated
        public AcknowledgeMode getAcknowledgeMode() {
            return getSimple().getAcknowledgeMode();
        }

        @Deprecated
        public void setAcknowledgeMode(AcknowledgeMode acknowledgeMode) {
            getSimple().setAcknowledgeMode(acknowledgeMode);
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.concurrency")
        @Deprecated
        public Integer getConcurrency() {
            return getSimple().getConcurrency();
        }

        @Deprecated
        public void setConcurrency(Integer concurrency) {
            getSimple().setConcurrency(concurrency);
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.max-concurrency")
        @Deprecated
        public Integer getMaxConcurrency() {
            return getSimple().getMaxConcurrency();
        }

        @Deprecated
        public void setMaxConcurrency(Integer maxConcurrency) {
            getSimple().setMaxConcurrency(maxConcurrency);
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.prefetch")
        @Deprecated
        public Integer getPrefetch() {
            return getSimple().getPrefetch();
        }

        @Deprecated
        public void setPrefetch(Integer prefetch) {
            getSimple().setPrefetch(prefetch);
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.transaction-size")
        @Deprecated
        public Integer getTransactionSize() {
            return getSimple().getTransactionSize();
        }

        @Deprecated
        public void setTransactionSize(Integer transactionSize) {
            getSimple().setTransactionSize(transactionSize);
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.default-requeue-rejected")
        @Deprecated
        public Boolean getDefaultRequeueRejected() {
            return getSimple().getDefaultRequeueRejected();
        }

        @Deprecated
        public void setDefaultRequeueRejected(Boolean defaultRequeueRejected) {
            getSimple().setDefaultRequeueRejected(defaultRequeueRejected);
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.idle-event-interval")
        @Deprecated
        public Long getIdleEventInterval() {
            return getSimple().getIdleEventInterval();
        }

        @Deprecated
        public void setIdleEventInterval(Long idleEventInterval) {
            getSimple().setIdleEventInterval(idleEventInterval);
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.retry")
        @Deprecated
        public ListenerRetry getRetry() {
            return getSimple().getRetry();
        }

        public AmqpContainer getSimple() {
            return this.simple;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties$AmqpContainer.class */
    public static class AmqpContainer {
        private AcknowledgeMode acknowledgeMode;
        private Integer concurrency;
        private Integer maxConcurrency;
        private Integer prefetch;
        private Integer transactionSize;
        private Boolean defaultRequeueRejected;
        private Long idleEventInterval;
        private boolean autoStartup = true;

        @NestedConfigurationProperty
        private final ListenerRetry retry = new ListenerRetry();

        public boolean isAutoStartup() {
            return this.autoStartup;
        }

        public void setAutoStartup(boolean autoStartup) {
            this.autoStartup = autoStartup;
        }

        public AcknowledgeMode getAcknowledgeMode() {
            return this.acknowledgeMode;
        }

        public void setAcknowledgeMode(AcknowledgeMode acknowledgeMode) {
            this.acknowledgeMode = acknowledgeMode;
        }

        public Integer getConcurrency() {
            return this.concurrency;
        }

        public void setConcurrency(Integer concurrency) {
            this.concurrency = concurrency;
        }

        public Integer getMaxConcurrency() {
            return this.maxConcurrency;
        }

        public void setMaxConcurrency(Integer maxConcurrency) {
            this.maxConcurrency = maxConcurrency;
        }

        public Integer getPrefetch() {
            return this.prefetch;
        }

        public void setPrefetch(Integer prefetch) {
            this.prefetch = prefetch;
        }

        public Integer getTransactionSize() {
            return this.transactionSize;
        }

        public void setTransactionSize(Integer transactionSize) {
            this.transactionSize = transactionSize;
        }

        public Boolean getDefaultRequeueRejected() {
            return this.defaultRequeueRejected;
        }

        public void setDefaultRequeueRejected(Boolean defaultRequeueRejected) {
            this.defaultRequeueRejected = defaultRequeueRejected;
        }

        public Long getIdleEventInterval() {
            return this.idleEventInterval;
        }

        public void setIdleEventInterval(Long idleEventInterval) {
            this.idleEventInterval = idleEventInterval;
        }

        public ListenerRetry getRetry() {
            return this.retry;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties$Template.class */
    public static class Template {

        @NestedConfigurationProperty
        private final Retry retry = new Retry();
        private Boolean mandatory;
        private Long receiveTimeout;
        private Long replyTimeout;

        public Retry getRetry() {
            return this.retry;
        }

        public Boolean getMandatory() {
            return this.mandatory;
        }

        public void setMandatory(Boolean mandatory) {
            this.mandatory = mandatory;
        }

        public Long getReceiveTimeout() {
            return this.receiveTimeout;
        }

        public void setReceiveTimeout(Long receiveTimeout) {
            this.receiveTimeout = receiveTimeout;
        }

        public Long getReplyTimeout() {
            return this.replyTimeout;
        }

        public void setReplyTimeout(Long replyTimeout) {
            this.replyTimeout = replyTimeout;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties$Retry.class */
    public static class Retry {
        private boolean enabled;
        private int maxAttempts = 3;
        private long initialInterval = 1000;
        private double multiplier = 1.0d;
        private long maxInterval = 10000;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMaxAttempts() {
            return this.maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public long getInitialInterval() {
            return this.initialInterval;
        }

        public void setInitialInterval(long initialInterval) {
            this.initialInterval = initialInterval;
        }

        public double getMultiplier() {
            return this.multiplier;
        }

        public void setMultiplier(double multiplier) {
            this.multiplier = multiplier;
        }

        public long getMaxInterval() {
            return this.maxInterval;
        }

        public void setMaxInterval(long maxInterval) {
            this.maxInterval = maxInterval;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties$ListenerRetry.class */
    public static class ListenerRetry extends Retry {
        private boolean stateless = true;

        public boolean isStateless() {
            return this.stateless;
        }

        public void setStateless(boolean stateless) {
            this.stateless = stateless;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitProperties$Address.class */
    private static final class Address {
        private static final String PREFIX_AMQP = "amqp://";
        private static final int DEFAULT_PORT = 5672;
        private String host;
        private int port;
        private String username;
        private String password;
        private String virtualHost;

        private Address(String input) {
            parseHostAndPort(parseVirtualHost(parseUsernameAndPassword(trimPrefix(input.trim()))));
        }

        private String trimPrefix(String input) {
            if (input.startsWith(PREFIX_AMQP)) {
                input = input.substring(PREFIX_AMQP.length());
            }
            return input;
        }

        private String parseUsernameAndPassword(String input) {
            if (input.contains("@")) {
                String[] split = StringUtils.split(input, "@");
                String creds = split[0];
                input = split[1];
                String[] split2 = StringUtils.split(creds, ":");
                this.username = split2[0];
                if (split2.length > 0) {
                    this.password = split2[1];
                }
            }
            return input;
        }

        private String parseVirtualHost(String input) {
            int hostIndex = input.indexOf("/");
            if (hostIndex >= 0) {
                this.virtualHost = input.substring(hostIndex + 1);
                if (this.virtualHost.isEmpty()) {
                    this.virtualHost = "/";
                }
                input = input.substring(0, hostIndex);
            }
            return input;
        }

        private void parseHostAndPort(String input) {
            int portIndex = input.indexOf(58);
            if (portIndex == -1) {
                this.host = input;
                this.port = DEFAULT_PORT;
            } else {
                this.host = input.substring(0, portIndex);
                this.port = Integer.valueOf(input.substring(portIndex + 1)).intValue();
            }
        }
    }
}
