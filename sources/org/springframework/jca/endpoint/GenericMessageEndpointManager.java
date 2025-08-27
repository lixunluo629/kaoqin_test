package org.springframework.jca.endpoint;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/endpoint/GenericMessageEndpointManager.class */
public class GenericMessageEndpointManager implements SmartLifecycle, InitializingBean, DisposableBean {
    private ResourceAdapter resourceAdapter;
    private MessageEndpointFactory messageEndpointFactory;
    private ActivationSpec activationSpec;
    private boolean autoStartup = true;
    private int phase = Integer.MAX_VALUE;
    private volatile boolean running = false;
    private final Object lifecycleMonitor = new Object();

    public void setResourceAdapter(ResourceAdapter resourceAdapter) {
        this.resourceAdapter = resourceAdapter;
    }

    public ResourceAdapter getResourceAdapter() {
        return this.resourceAdapter;
    }

    public void setMessageEndpointFactory(MessageEndpointFactory messageEndpointFactory) {
        this.messageEndpointFactory = messageEndpointFactory;
    }

    public MessageEndpointFactory getMessageEndpointFactory() {
        return this.messageEndpointFactory;
    }

    public void setActivationSpec(ActivationSpec activationSpec) {
        this.activationSpec = activationSpec;
    }

    public ActivationSpec getActivationSpec() {
        return this.activationSpec;
    }

    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    @Override // org.springframework.context.SmartLifecycle
    public boolean isAutoStartup() {
        return this.autoStartup;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    @Override // org.springframework.context.Phased
    public int getPhase() {
        return this.phase;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws ResourceException {
        if (getResourceAdapter() == null) {
            throw new IllegalArgumentException("Property 'resourceAdapter' is required");
        }
        if (getMessageEndpointFactory() == null) {
            throw new IllegalArgumentException("Property 'messageEndpointFactory' is required");
        }
        ActivationSpec activationSpec = getActivationSpec();
        if (activationSpec == null) {
            throw new IllegalArgumentException("Property 'activationSpec' is required");
        }
        if (activationSpec.getResourceAdapter() == null) {
            activationSpec.setResourceAdapter(getResourceAdapter());
        } else if (activationSpec.getResourceAdapter() != getResourceAdapter()) {
            throw new IllegalArgumentException("ActivationSpec [" + activationSpec + "] is associated with a different ResourceAdapter: " + activationSpec.getResourceAdapter());
        }
    }

    @Override // org.springframework.context.Lifecycle
    public void start() {
        synchronized (this.lifecycleMonitor) {
            if (!this.running) {
                try {
                    getResourceAdapter().endpointActivation(getMessageEndpointFactory(), getActivationSpec());
                    this.running = true;
                } catch (ResourceException ex) {
                    throw new IllegalStateException("Could not activate message endpoint", ex);
                }
            }
        }
    }

    @Override // org.springframework.context.Lifecycle
    public void stop() {
        synchronized (this.lifecycleMonitor) {
            if (this.running) {
                getResourceAdapter().endpointDeactivation(getMessageEndpointFactory(), getActivationSpec());
                this.running = false;
            }
        }
    }

    @Override // org.springframework.context.SmartLifecycle
    public void stop(Runnable callback) {
        synchronized (this.lifecycleMonitor) {
            stop();
            callback.run();
        }
    }

    @Override // org.springframework.context.Lifecycle
    public boolean isRunning() {
        return this.running;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        stop();
    }
}
