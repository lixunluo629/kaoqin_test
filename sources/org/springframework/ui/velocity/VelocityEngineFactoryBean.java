package org.springframework.ui.velocity;

import java.io.IOException;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;

@Deprecated
/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/ui/velocity/VelocityEngineFactoryBean.class */
public class VelocityEngineFactoryBean extends VelocityEngineFactory implements FactoryBean<VelocityEngine>, InitializingBean, ResourceLoaderAware {
    private VelocityEngine velocityEngine;

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws IOException, VelocityException {
        this.velocityEngine = createVelocityEngine();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public VelocityEngine getObject() {
        return this.velocityEngine;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<? extends VelocityEngine> getObjectType() {
        return VelocityEngine.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
