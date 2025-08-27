package org.springframework.boot.context.embedded.tomcat;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.naming.NamingException;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.naming.ContextBindings;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/TomcatEmbeddedServletContainer.class */
public class TomcatEmbeddedServletContainer implements EmbeddedServletContainer {
    private static final Log logger = LogFactory.getLog(TomcatEmbeddedServletContainer.class);
    private static final AtomicInteger containerCounter = new AtomicInteger(-1);
    private final Object monitor;
    private final Map<Service, Connector[]> serviceConnectors;
    private final Tomcat tomcat;
    private final boolean autoStart;
    private volatile boolean started;

    public TomcatEmbeddedServletContainer(Tomcat tomcat) {
        this(tomcat, true);
    }

    public TomcatEmbeddedServletContainer(Tomcat tomcat, boolean autoStart) throws EmbeddedServletContainerException {
        this.monitor = new Object();
        this.serviceConnectors = new HashMap();
        Assert.notNull(tomcat, "Tomcat Server must not be null");
        this.tomcat = tomcat;
        this.autoStart = autoStart;
        initialize();
    }

    private void initialize() throws EmbeddedServletContainerException {
        logger.info("Tomcat initialized with port(s): " + getPortsDescription(false));
        synchronized (this.monitor) {
            try {
                addInstanceIdToEngineName();
                try {
                    final Context context = findContext();
                    context.addLifecycleListener(new LifecycleListener() { // from class: org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer.1
                        @Override // org.apache.catalina.LifecycleListener
                        public void lifecycleEvent(LifecycleEvent event) {
                            if (context.equals(event.getSource()) && Lifecycle.START_EVENT.equals(event.getType())) {
                                TomcatEmbeddedServletContainer.this.removeServiceConnectors();
                            }
                        }
                    });
                    this.tomcat.start();
                    rethrowDeferredStartupExceptions();
                    try {
                        ContextBindings.bindClassLoader(context, getNamingToken(context), getClass().getClassLoader());
                    } catch (NamingException e) {
                    }
                    startDaemonAwaitThread();
                } catch (Exception ex) {
                    containerCounter.decrementAndGet();
                    throw ex;
                }
            } catch (Exception ex2) {
                stopSilently();
                throw new EmbeddedServletContainerException("Unable to start embedded Tomcat", ex2);
            }
        }
    }

    private Context findContext() {
        for (Container child : this.tomcat.getHost().findChildren()) {
            if (child instanceof Context) {
                return (Context) child;
            }
        }
        throw new IllegalStateException("The host does not contain a Context");
    }

    private void addInstanceIdToEngineName() {
        int instanceId = containerCounter.incrementAndGet();
        if (instanceId > 0) {
            Engine engine = this.tomcat.getEngine();
            engine.setName(engine.getName() + "-" + instanceId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeServiceConnectors() {
        for (Service service : this.tomcat.getServer().findServices()) {
            Connector[] connectors = (Connector[]) service.findConnectors().clone();
            this.serviceConnectors.put(service, connectors);
            for (Connector connector : connectors) {
                service.removeConnector(connector);
            }
        }
    }

    private void rethrowDeferredStartupExceptions() throws Exception {
        Exception exception;
        Container[] children = this.tomcat.getHost().findChildren();
        for (Container container : children) {
            if ((container instanceof TomcatEmbeddedContext) && (exception = ((TomcatEmbeddedContext) container).getStarter().getStartUpException()) != null) {
                throw exception;
            }
            if (!LifecycleState.STARTED.equals(container.getState())) {
                throw new IllegalStateException(container + " failed to start");
            }
        }
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread("container-" + containerCounter.get()) { // from class: org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                TomcatEmbeddedServletContainer.this.tomcat.getServer().await();
            }
        };
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainer
    public void start() throws EmbeddedServletContainerException {
        synchronized (this.monitor) {
            try {
                if (this.started) {
                    return;
                }
                try {
                    addPreviouslyRemovedConnectors();
                    Connector connector = this.tomcat.getConnector();
                    if (connector != null && this.autoStart) {
                        performDeferredLoadOnStartup();
                    }
                    checkThatConnectorsHaveStarted();
                    this.started = true;
                    logger.info("Tomcat started on port(s): " + getPortsDescription(true));
                    Context context = findContext();
                    ContextBindings.unbindClassLoader(context, getNamingToken(context), getClass().getClassLoader());
                } catch (ConnectorStartFailedException ex) {
                    stopSilently();
                    throw ex;
                } catch (Exception ex2) {
                    throw new EmbeddedServletContainerException("Unable to start embedded Tomcat servlet container", ex2);
                }
            } catch (Throwable th) {
                Context context2 = findContext();
                ContextBindings.unbindClassLoader(context2, getNamingToken(context2), getClass().getClassLoader());
                throw th;
            }
        }
    }

    private void checkThatConnectorsHaveStarted() {
        for (Connector connector : this.tomcat.getService().findConnectors()) {
            if (LifecycleState.FAILED.equals(connector.getState())) {
                throw new ConnectorStartFailedException(connector.getPort());
            }
        }
    }

    private void stopSilently() throws IOException {
        try {
            stopTomcat();
        } catch (LifecycleException e) {
        }
    }

    private void stopTomcat() throws LifecycleException, IOException {
        if (Thread.currentThread().getContextClassLoader() instanceof TomcatEmbeddedWebappClassLoader) {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        }
        this.tomcat.stop();
    }

    private void addPreviouslyRemovedConnectors() {
        Service[] services = this.tomcat.getServer().findServices();
        for (Service service : services) {
            Connector[] connectors = this.serviceConnectors.get(service);
            if (connectors != null) {
                for (Connector connector : connectors) {
                    service.addConnector(connector);
                    if (!this.autoStart) {
                        stopProtocolHandler(connector);
                    }
                }
                this.serviceConnectors.remove(service);
            }
        }
    }

    private void stopProtocolHandler(Connector connector) {
        try {
            connector.getProtocolHandler().stop();
        } catch (Exception ex) {
            logger.error("Cannot pause connector: ", ex);
        }
    }

    private void performDeferredLoadOnStartup() {
        try {
            for (Container child : this.tomcat.getHost().findChildren()) {
                if (child instanceof TomcatEmbeddedContext) {
                    ((TomcatEmbeddedContext) child).deferredLoadOnStartup();
                }
            }
        } catch (Exception ex) {
            logger.error("Cannot start connector: ", ex);
            throw new EmbeddedServletContainerException("Unable to start embedded Tomcat connectors", ex);
        }
    }

    Map<Service, Connector[]> getServiceConnectors() {
        return this.serviceConnectors;
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainer
    public void stop() throws EmbeddedServletContainerException {
        synchronized (this.monitor) {
            boolean wasStarted = this.started;
            try {
                try {
                    this.started = false;
                    try {
                        stopTomcat();
                        this.tomcat.destroy();
                    } catch (LifecycleException e) {
                    }
                    if (wasStarted) {
                        containerCounter.decrementAndGet();
                    }
                } catch (Throwable th) {
                    if (wasStarted) {
                        containerCounter.decrementAndGet();
                    }
                    throw th;
                }
            } catch (Exception ex) {
                throw new EmbeddedServletContainerException("Unable to stop embedded Tomcat", ex);
            }
        }
    }

    private String getPortsDescription(boolean localPort) {
        StringBuilder ports = new StringBuilder();
        for (Connector connector : this.tomcat.getService().findConnectors()) {
            ports.append(ports.length() != 0 ? SymbolConstants.SPACE_SYMBOL : "");
            int port = localPort ? connector.getLocalPort() : connector.getPort();
            ports.append(port + " (" + connector.getScheme() + ")");
        }
        return ports.toString();
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainer
    public int getPort() {
        Connector connector = this.tomcat.getConnector();
        if (connector != null) {
            return connector.getLocalPort();
        }
        return 0;
    }

    public Tomcat getTomcat() {
        return this.tomcat;
    }

    private Object getNamingToken(Context context) {
        try {
            return context.getNamingToken();
        } catch (NoSuchMethodError e) {
            return context;
        }
    }
}
