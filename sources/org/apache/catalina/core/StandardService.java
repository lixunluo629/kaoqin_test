package org.apache.catalina.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import javax.management.ObjectName;
import org.apache.catalina.Container;
import org.apache.catalina.Engine;
import org.apache.catalina.Executor;
import org.apache.catalina.JmxEnabled;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.mapper.Mapper;
import org.apache.catalina.mapper.MapperListener;
import org.apache.catalina.util.LifecycleMBeanBase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/StandardService.class */
public class StandardService extends LifecycleMBeanBase implements Service {
    private String name = null;
    private Server server = null;
    protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
    protected Connector[] connectors = new Connector[0];
    private final Object connectorsLock = new Object();
    protected final ArrayList<Executor> executors = new ArrayList<>();
    private Engine engine = null;
    private ClassLoader parentClassLoader = null;
    protected final Mapper mapper = new Mapper();
    protected final MapperListener mapperListener = new MapperListener(this);
    private static final Log log = LogFactory.getLog((Class<?>) StandardService.class);
    private static final StringManager sm = StringManager.getManager(Constants.Package);

    @Override // org.apache.catalina.Service
    public Mapper getMapper() {
        return this.mapper;
    }

    @Override // org.apache.catalina.Service
    public Engine getContainer() {
        return this.engine;
    }

    @Override // org.apache.catalina.Service
    public void setContainer(Engine engine) {
        Engine oldEngine = this.engine;
        if (oldEngine != null) {
            oldEngine.setService(null);
        }
        this.engine = engine;
        if (this.engine != null) {
            this.engine.setService(this);
        }
        if (getState().isAvailable()) {
            if (this.engine != null) {
                try {
                    this.engine.start();
                } catch (LifecycleException e) {
                    log.error(sm.getString("standardService.engine.startFailed"), e);
                }
            }
            try {
                this.mapperListener.stop();
            } catch (LifecycleException e2) {
                log.error(sm.getString("standardService.mapperListener.stopFailed"), e2);
            }
            try {
                this.mapperListener.start();
            } catch (LifecycleException e3) {
                log.error(sm.getString("standardService.mapperListener.startFailed"), e3);
            }
            if (oldEngine != null) {
                try {
                    oldEngine.stop();
                } catch (LifecycleException e4) {
                    log.error(sm.getString("standardService.engine.stopFailed"), e4);
                }
            }
        }
        this.support.firePropertyChange("container", oldEngine, this.engine);
    }

    @Override // org.apache.catalina.Service
    public String getName() {
        return this.name;
    }

    @Override // org.apache.catalina.Service
    public void setName(String name) {
        this.name = name;
    }

    @Override // org.apache.catalina.Service
    public Server getServer() {
        return this.server;
    }

    @Override // org.apache.catalina.Service
    public void setServer(Server server) {
        this.server = server;
    }

    @Override // org.apache.catalina.Service
    public void addConnector(Connector connector) {
        synchronized (this.connectorsLock) {
            connector.setService(this);
            Connector[] results = new Connector[this.connectors.length + 1];
            System.arraycopy(this.connectors, 0, results, 0, this.connectors.length);
            results[this.connectors.length] = connector;
            this.connectors = results;
            if (getState().isAvailable()) {
                try {
                    connector.start();
                } catch (LifecycleException e) {
                    log.error(sm.getString("standardService.connector.startFailed", connector), e);
                }
                this.support.firePropertyChange("connector", (Object) null, connector);
            } else {
                this.support.firePropertyChange("connector", (Object) null, connector);
            }
        }
    }

    public ObjectName[] getConnectorNames() {
        ObjectName[] results = new ObjectName[this.connectors.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = this.connectors[i].getObjectName();
        }
        return results;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

    @Override // org.apache.catalina.Service
    public Connector[] findConnectors() {
        return this.connectors;
    }

    @Override // org.apache.catalina.Service
    public void removeConnector(Connector connector) {
        synchronized (this.connectorsLock) {
            int j = -1;
            int i = 0;
            while (true) {
                if (i >= this.connectors.length) {
                    break;
                }
                if (connector != this.connectors[i]) {
                    i++;
                } else {
                    j = i;
                    break;
                }
            }
            if (j < 0) {
                return;
            }
            if (this.connectors[j].getState().isAvailable()) {
                try {
                    this.connectors[j].stop();
                } catch (LifecycleException e) {
                    log.error(sm.getString("standardService.connector.stopFailed", this.connectors[j]), e);
                }
            }
            connector.setService(null);
            int k = 0;
            Connector[] results = new Connector[this.connectors.length - 1];
            for (int i2 = 0; i2 < this.connectors.length; i2++) {
                if (i2 != j) {
                    int i3 = k;
                    k++;
                    results[i3] = this.connectors[i2];
                }
            }
            this.connectors = results;
            this.support.firePropertyChange("connector", connector, (Object) null);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.support.removePropertyChangeListener(listener);
    }

    public String toString() {
        return "StandardService[" + getName() + "]";
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003a  */
    @Override // org.apache.catalina.Service
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void addExecutor(org.apache.catalina.Executor r5) {
        /*
            r4 = this;
            r0 = r4
            java.util.ArrayList<org.apache.catalina.Executor> r0 = r0.executors
            r1 = r0
            r6 = r1
            monitor-enter(r0)
            r0 = r4
            java.util.ArrayList<org.apache.catalina.Executor> r0 = r0.executors     // Catch: java.lang.Throwable -> L3f
            r1 = r5
            boolean r0 = r0.contains(r1)     // Catch: java.lang.Throwable -> L3f
            if (r0 != 0) goto L3a
            r0 = r4
            java.util.ArrayList<org.apache.catalina.Executor> r0 = r0.executors     // Catch: java.lang.Throwable -> L3f
            r1 = r5
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L3f
            r0 = r4
            org.apache.catalina.LifecycleState r0 = r0.getState()     // Catch: java.lang.Throwable -> L3f
            boolean r0 = r0.isAvailable()     // Catch: java.lang.Throwable -> L3f
            if (r0 == 0) goto L3a
            r0 = r5
            r0.start()     // Catch: org.apache.catalina.LifecycleException -> L2e java.lang.Throwable -> L3f
            goto L3a
        L2e:
            r7 = move-exception
            org.apache.juli.logging.Log r0 = org.apache.catalina.core.StandardService.log     // Catch: java.lang.Throwable -> L3f
            java.lang.String r1 = "Executor.start"
            r2 = r7
            r0.error(r1, r2)     // Catch: java.lang.Throwable -> L3f
        L3a:
            r0 = r6
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L3f
            goto L46
        L3f:
            r8 = move-exception
            r0 = r6
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L3f
            r0 = r8
            throw r0
        L46:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.catalina.core.StandardService.addExecutor(org.apache.catalina.Executor):void");
    }

    @Override // org.apache.catalina.Service
    public Executor[] findExecutors() {
        Executor[] arr;
        synchronized (this.executors) {
            arr = new Executor[this.executors.size()];
            this.executors.toArray(arr);
        }
        return arr;
    }

    @Override // org.apache.catalina.Service
    public Executor getExecutor(String executorName) {
        synchronized (this.executors) {
            Iterator i$ = this.executors.iterator();
            while (i$.hasNext()) {
                Executor executor = i$.next();
                if (executorName.equals(executor.getName())) {
                    return executor;
                }
            }
            return null;
        }
    }

    @Override // org.apache.catalina.Service
    public void removeExecutor(Executor ex) {
        synchronized (this.executors) {
            if (this.executors.remove(ex) && getState().isAvailable()) {
                try {
                    ex.stop();
                } catch (LifecycleException e) {
                    log.error("Executor.stop", e);
                }
            }
        }
    }

    @Override // org.apache.catalina.util.LifecycleBase
    protected void startInternal() throws LifecycleException {
        if (log.isInfoEnabled()) {
            log.info(sm.getString("standardService.start.name", this.name));
        }
        setState(LifecycleState.STARTING);
        if (this.engine != null) {
            synchronized (this.engine) {
                this.engine.start();
            }
        }
        synchronized (this.executors) {
            Iterator i$ = this.executors.iterator();
            while (i$.hasNext()) {
                Executor executor = i$.next();
                executor.start();
            }
        }
        this.mapperListener.start();
        synchronized (this.connectorsLock) {
            Connector[] arr$ = this.connectors;
            for (Connector connector : arr$) {
                try {
                    if (connector.getState() != LifecycleState.FAILED) {
                        connector.start();
                    }
                } catch (Exception e) {
                    log.error(sm.getString("standardService.connector.startFailed", connector), e);
                }
            }
        }
    }

    @Override // org.apache.catalina.util.LifecycleBase
    protected void stopInternal() throws LifecycleException {
        synchronized (this.connectorsLock) {
            Connector[] arr$ = this.connectors;
            for (Connector connector : arr$) {
                try {
                    connector.pause();
                } catch (Exception e) {
                    log.error(sm.getString("standardService.connector.pauseFailed", connector), e);
                }
                connector.getProtocolHandler().closeServerSocketGraceful();
            }
        }
        if (log.isInfoEnabled()) {
            log.info(sm.getString("standardService.stop.name", this.name));
        }
        setState(LifecycleState.STOPPING);
        if (this.engine != null) {
            synchronized (this.engine) {
                this.engine.stop();
            }
        }
        synchronized (this.connectorsLock) {
            Connector[] arr$2 = this.connectors;
            for (Connector connector2 : arr$2) {
                if (LifecycleState.STARTED.equals(connector2.getState())) {
                    try {
                        connector2.stop();
                    } catch (Exception e2) {
                        log.error(sm.getString("standardService.connector.stopFailed", connector2), e2);
                    }
                }
            }
        }
        if (this.mapperListener.getState() != LifecycleState.INITIALIZED) {
            this.mapperListener.stop();
        }
        synchronized (this.executors) {
            Iterator i$ = this.executors.iterator();
            while (i$.hasNext()) {
                Executor executor = i$.next();
                executor.stop();
            }
        }
    }

    @Override // org.apache.catalina.util.LifecycleMBeanBase, org.apache.catalina.util.LifecycleBase
    protected void initInternal() throws LifecycleException {
        super.initInternal();
        if (this.engine != null) {
            this.engine.init();
        }
        Executor[] arr$ = findExecutors();
        for (Executor executor : arr$) {
            if (executor instanceof JmxEnabled) {
                ((JmxEnabled) executor).setDomain(getDomain());
            }
            executor.init();
        }
        this.mapperListener.init();
        synchronized (this.connectorsLock) {
            Connector[] arr$2 = this.connectors;
            for (Connector connector : arr$2) {
                try {
                    connector.init();
                } catch (Exception e) {
                    String message = sm.getString("standardService.connector.initFailed", connector);
                    log.error(message, e);
                    if (Boolean.getBoolean("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE")) {
                        throw new LifecycleException(message);
                    }
                }
            }
        }
    }

    @Override // org.apache.catalina.util.LifecycleMBeanBase, org.apache.catalina.util.LifecycleBase
    protected void destroyInternal() throws LifecycleException {
        this.mapperListener.destroy();
        synchronized (this.connectorsLock) {
            Connector[] arr$ = this.connectors;
            for (Connector connector : arr$) {
                try {
                    connector.destroy();
                } catch (Exception e) {
                    log.error(sm.getString("standardService.connector.destroyFailed", connector), e);
                }
            }
        }
        Executor[] arr$2 = findExecutors();
        for (Executor executor : arr$2) {
            executor.destroy();
        }
        if (this.engine != null) {
            this.engine.destroy();
        }
        super.destroyInternal();
    }

    @Override // org.apache.catalina.Service
    public ClassLoader getParentClassLoader() {
        if (this.parentClassLoader != null) {
            return this.parentClassLoader;
        }
        if (this.server != null) {
            return this.server.getParentClassLoader();
        }
        return ClassLoader.getSystemClassLoader();
    }

    @Override // org.apache.catalina.Service
    public void setParentClassLoader(ClassLoader parent) {
        ClassLoader oldParentClassLoader = this.parentClassLoader;
        this.parentClassLoader = parent;
        this.support.firePropertyChange("parentClassLoader", oldParentClassLoader, this.parentClassLoader);
    }

    @Override // org.apache.catalina.util.LifecycleMBeanBase
    protected String getDomainInternal() {
        String domain = null;
        Container engine = getContainer();
        if (engine != null) {
            domain = engine.getName();
        }
        if (domain == null) {
            domain = getName();
        }
        return domain;
    }

    @Override // org.apache.catalina.util.LifecycleMBeanBase
    public final String getObjectNameKeyProperties() {
        return "type=Service";
    }
}
