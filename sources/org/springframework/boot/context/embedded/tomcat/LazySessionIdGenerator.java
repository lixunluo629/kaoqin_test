package org.springframework.boot.context.embedded.tomcat;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.util.StandardSessionIdGenerator;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/LazySessionIdGenerator.class */
class LazySessionIdGenerator extends StandardSessionIdGenerator {
    LazySessionIdGenerator() {
    }

    @Override // org.apache.catalina.util.SessionIdGeneratorBase, org.apache.catalina.util.LifecycleBase
    protected void startInternal() throws LifecycleException {
        setState(LifecycleState.STARTING);
    }
}
