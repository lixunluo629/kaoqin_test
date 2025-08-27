package org.springframework.boot.context.embedded.jetty;

import java.io.IOException;
import java.net.BindException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NetworkConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
import org.springframework.boot.context.embedded.PortInUseException;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedServletContainer.class */
public class JettyEmbeddedServletContainer implements EmbeddedServletContainer {
    private static final Log logger = LogFactory.getLog(JettyEmbeddedServletContainer.class);
    private final Object monitor;
    private final Server server;
    private final boolean autoStart;
    private Connector[] connectors;
    private volatile boolean started;

    public JettyEmbeddedServletContainer(Server server) {
        this(server, true);
    }

    public JettyEmbeddedServletContainer(Server server, boolean autoStart) {
        this.monitor = new Object();
        this.autoStart = autoStart;
        Assert.notNull(server, "Jetty Server must not be null");
        this.server = server;
        initialize();
    }

    private void initialize() {
        synchronized (this.monitor) {
            try {
                this.connectors = this.server.getConnectors();
                this.server.addBean(new AbstractLifeCycle() { // from class: org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainer.1
                    protected void doStart() throws Exception {
                        for (Connector connector : JettyEmbeddedServletContainer.this.connectors) {
                            Assert.state(connector.isStopped(), "Connector " + connector + " has been started prematurely");
                        }
                        JettyEmbeddedServletContainer.this.server.setConnectors((Connector[]) null);
                    }
                });
                this.server.start();
                this.server.setStopAtShutdown(false);
            } catch (Throwable ex) {
                stopSilently();
                throw new EmbeddedServletContainerException("Unable to start embedded Jetty servlet container", ex);
            }
        }
    }

    private void stopSilently() {
        try {
            this.server.stop();
        } catch (Exception e) {
        }
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainer
    public void start() throws EmbeddedServletContainerException {
        synchronized (this.monitor) {
            if (this.started) {
                return;
            }
            this.server.setConnectors(this.connectors);
            if (this.autoStart) {
                try {
                    try {
                        this.server.start();
                        for (Handler handler : this.server.getHandlers()) {
                            handleDeferredInitialize(handler);
                        }
                        for (NetworkConnector networkConnector : this.server.getConnectors()) {
                            try {
                                networkConnector.start();
                            } catch (IOException ex) {
                                if ((networkConnector instanceof NetworkConnector) && findBindException(ex) != null) {
                                    throw new PortInUseException(networkConnector.getPort());
                                }
                                throw ex;
                            }
                        }
                        this.started = true;
                        logger.info("Jetty started on port(s) " + getActualPortsDescription());
                    } catch (EmbeddedServletContainerException ex2) {
                        stopSilently();
                        throw ex2;
                    }
                } catch (Exception ex3) {
                    stopSilently();
                    throw new EmbeddedServletContainerException("Unable to start embedded Jetty servlet container", ex3);
                }
            }
        }
    }

    private BindException findBindException(Throwable ex) {
        if (ex == null) {
            return null;
        }
        if (ex instanceof BindException) {
            return (BindException) ex;
        }
        return findBindException(ex.getCause());
    }

    private String getActualPortsDescription() {
        StringBuilder ports = new StringBuilder();
        for (Connector connector : this.server.getConnectors()) {
            ports.append(ports.length() != 0 ? ", " : "");
            ports.append(getLocalPort(connector) + getProtocols(connector));
        }
        return ports.toString();
    }

    private Integer getLocalPort(Connector connector) {
        try {
            return (Integer) ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(connector.getClass(), "getLocalPort"), connector);
        } catch (Exception ex) {
            logger.info("could not determine port ( " + ex.getMessage() + ")");
            return 0;
        }
    }

    private String getProtocols(Connector connector) {
        try {
            List<String> protocols = connector.getProtocols();
            return " (" + StringUtils.collectionToDelimitedString(protocols, ", ") + ")";
        } catch (NoSuchMethodError e) {
            return "";
        }
    }

    private void handleDeferredInitialize(Handler... handlers) throws Exception {
        for (Handler handler : handlers) {
            if (handler instanceof JettyEmbeddedWebAppContext) {
                ((JettyEmbeddedWebAppContext) handler).deferredInitialize();
            } else if (handler instanceof HandlerWrapper) {
                handleDeferredInitialize(((HandlerWrapper) handler).getHandler());
            } else if (handler instanceof HandlerCollection) {
                handleDeferredInitialize(((HandlerCollection) handler).getHandlers());
            }
        }
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainer
    public void stop() {
        synchronized (this.monitor) {
            this.started = false;
            try {
                this.server.stop();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                throw new EmbeddedServletContainerException("Unable to stop embedded Jetty servlet container", ex);
            }
        }
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainer
    public int getPort() {
        Connector[] connectors = this.server.getConnectors();
        if (0 < connectors.length) {
            Connector connector = connectors[0];
            return getLocalPort(connector).intValue();
        }
        return 0;
    }

    public Server getServer() {
        return this.server;
    }
}
