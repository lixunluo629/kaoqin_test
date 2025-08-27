package org.springframework.boot.context.embedded.tomcat;

import org.apache.catalina.Container;
import org.apache.catalina.Manager;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.session.ManagerBase;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/TomcatEmbeddedContext.class */
class TomcatEmbeddedContext extends StandardContext {
    private TomcatStarter starter;
    private final boolean overrideLoadOnStart;

    TomcatEmbeddedContext() {
        this.overrideLoadOnStart = ReflectionUtils.findMethod(StandardContext.class, "loadOnStartup", Container[].class).getReturnType() == Boolean.TYPE;
    }

    @Override // org.apache.catalina.core.StandardContext
    public boolean loadOnStartup(Container[] children) {
        if (this.overrideLoadOnStart) {
            return true;
        }
        return super.loadOnStartup(children);
    }

    @Override // org.apache.catalina.core.StandardContext, org.apache.catalina.Context
    public void setManager(Manager manager) {
        if (manager instanceof ManagerBase) {
            ((ManagerBase) manager).setSessionIdGenerator(new LazySessionIdGenerator());
        }
        super.setManager(manager);
    }

    public void deferredLoadOnStartup() {
        ClassLoader classLoader = getLoader().getClassLoader();
        ClassLoader existingLoader = null;
        if (classLoader != null) {
            existingLoader = ClassUtils.overrideThreadContextClassLoader(classLoader);
        }
        if (this.overrideLoadOnStart) {
            super.loadOnStartup(findChildren());
        }
        if (existingLoader != null) {
            ClassUtils.overrideThreadContextClassLoader(existingLoader);
        }
    }

    public void setStarter(TomcatStarter starter) {
        this.starter = starter;
    }

    public TomcatStarter getStarter() {
        return this.starter;
    }
}
