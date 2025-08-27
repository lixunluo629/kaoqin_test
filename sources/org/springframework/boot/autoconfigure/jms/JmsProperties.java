package org.springframework.boot.autoconfigure.jms;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.jms")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/JmsProperties.class */
public class JmsProperties {
    private String jndiName;
    private boolean pubSubDomain = false;
    private final Listener listener = new Listener();
    private final Template template = new Template();

    public boolean isPubSubDomain() {
        return this.pubSubDomain;
    }

    public void setPubSubDomain(boolean pubSubDomain) {
        this.pubSubDomain = pubSubDomain;
    }

    public String getJndiName() {
        return this.jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public Listener getListener() {
        return this.listener;
    }

    public Template getTemplate() {
        return this.template;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/JmsProperties$Listener.class */
    public static class Listener {
        private boolean autoStartup = true;
        private AcknowledgeMode acknowledgeMode;
        private Integer concurrency;
        private Integer maxConcurrency;

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

        public String formatConcurrency() {
            if (this.concurrency != null) {
                return this.maxConcurrency != null ? this.concurrency + "-" + this.maxConcurrency : String.valueOf(this.concurrency);
            }
            if (this.maxConcurrency != null) {
                return "1-" + this.maxConcurrency;
            }
            return null;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/JmsProperties$Template.class */
    public static class Template {
        private String defaultDestination;
        private Long deliveryDelay;
        private DeliveryMode deliveryMode;
        private Integer priority;
        private Long timeToLive;
        private Boolean qosEnabled;
        private Long receiveTimeout;

        public String getDefaultDestination() {
            return this.defaultDestination;
        }

        public void setDefaultDestination(String defaultDestination) {
            this.defaultDestination = defaultDestination;
        }

        public Long getDeliveryDelay() {
            return this.deliveryDelay;
        }

        public void setDeliveryDelay(Long deliveryDelay) {
            this.deliveryDelay = deliveryDelay;
        }

        public DeliveryMode getDeliveryMode() {
            return this.deliveryMode;
        }

        public void setDeliveryMode(DeliveryMode deliveryMode) {
            this.deliveryMode = deliveryMode;
        }

        public Integer getPriority() {
            return this.priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public Long getTimeToLive() {
            return this.timeToLive;
        }

        public void setTimeToLive(Long timeToLive) {
            this.timeToLive = timeToLive;
        }

        public boolean determineQosEnabled() {
            if (this.qosEnabled != null) {
                return this.qosEnabled.booleanValue();
            }
            return (getDeliveryMode() == null && getPriority() == null && getTimeToLive() == null) ? false : true;
        }

        public Boolean getQosEnabled() {
            return this.qosEnabled;
        }

        public void setQosEnabled(Boolean qosEnabled) {
            this.qosEnabled = qosEnabled;
        }

        public Long getReceiveTimeout() {
            return this.receiveTimeout;
        }

        public void setReceiveTimeout(Long receiveTimeout) {
            this.receiveTimeout = receiveTimeout;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/JmsProperties$AcknowledgeMode.class */
    public enum AcknowledgeMode {
        AUTO(1),
        CLIENT(2),
        DUPS_OK(3);

        private final int mode;

        AcknowledgeMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return this.mode;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/JmsProperties$DeliveryMode.class */
    public enum DeliveryMode {
        NON_PERSISTENT(1),
        PERSISTENT(2);

        private final int value;

        DeliveryMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
