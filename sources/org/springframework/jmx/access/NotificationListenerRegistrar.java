package org.springframework.jmx.access;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.JmxException;
import org.springframework.jmx.MBeanServerNotFoundException;
import org.springframework.jmx.support.NotificationListenerHolder;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/access/NotificationListenerRegistrar.class */
public class NotificationListenerRegistrar extends NotificationListenerHolder implements InitializingBean, DisposableBean {
    private MBeanServerConnection server;
    private JMXServiceURL serviceUrl;
    private Map<String, ?> environment;
    private String agentId;
    private ObjectName[] actualObjectNames;
    protected final Log logger = LogFactory.getLog(getClass());
    private final ConnectorDelegate connector = new ConnectorDelegate();

    public void setServer(MBeanServerConnection server) {
        this.server = server;
    }

    public void setEnvironment(Map<String, ?> environment) {
        this.environment = environment;
    }

    public Map<String, ?> getEnvironment() {
        return this.environment;
    }

    public void setServiceUrl(String url) throws MalformedURLException {
        this.serviceUrl = new JMXServiceURL(url);
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (getNotificationListener() == null) {
            throw new IllegalArgumentException("Property 'notificationListener' is required");
        }
        if (CollectionUtils.isEmpty(this.mappedObjectNames)) {
            throw new IllegalArgumentException("Property 'mappedObjectName' is required");
        }
        prepare();
    }

    public void prepare() {
        if (this.server == null) {
            this.server = this.connector.connect(this.serviceUrl, this.environment, this.agentId);
        }
        try {
            this.actualObjectNames = getResolvedObjectNames();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Registering NotificationListener for MBeans " + Arrays.asList(this.actualObjectNames));
            }
            for (ObjectName actualObjectName : this.actualObjectNames) {
                this.server.addNotificationListener(actualObjectName, getNotificationListener(), getNotificationFilter(), getHandback());
            }
        } catch (IOException ex) {
            throw new MBeanServerNotFoundException("Could not connect to remote MBeanServer at URL [" + this.serviceUrl + "]", ex);
        } catch (Exception ex2) {
            throw new JmxException("Unable to register NotificationListener", ex2);
        }
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        try {
            if (this.actualObjectNames != null) {
                for (ObjectName actualObjectName : this.actualObjectNames) {
                    try {
                        this.server.removeNotificationListener(actualObjectName, getNotificationListener(), getNotificationFilter(), getHandback());
                    } catch (Exception ex) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Unable to unregister NotificationListener", ex);
                        }
                    }
                }
            }
        } finally {
            this.connector.close();
        }
    }
}
