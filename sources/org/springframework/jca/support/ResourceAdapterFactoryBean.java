package org.springframework.jca.support;

import javax.resource.ResourceException;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.WorkManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/support/ResourceAdapterFactoryBean.class */
public class ResourceAdapterFactoryBean implements FactoryBean<ResourceAdapter>, InitializingBean, DisposableBean {
    private ResourceAdapter resourceAdapter;
    private BootstrapContext bootstrapContext;
    private WorkManager workManager;
    private XATerminator xaTerminator;

    public void setResourceAdapterClass(Class<? extends ResourceAdapter> resourceAdapterClass) {
        this.resourceAdapter = (ResourceAdapter) BeanUtils.instantiateClass(resourceAdapterClass);
    }

    public void setResourceAdapter(ResourceAdapter resourceAdapter) {
        this.resourceAdapter = resourceAdapter;
    }

    public void setBootstrapContext(BootstrapContext bootstrapContext) {
        this.bootstrapContext = bootstrapContext;
    }

    public void setWorkManager(WorkManager workManager) {
        this.workManager = workManager;
    }

    public void setXaTerminator(XATerminator xaTerminator) {
        this.xaTerminator = xaTerminator;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws ResourceException {
        if (this.resourceAdapter == null) {
            throw new IllegalArgumentException("'resourceAdapter' or 'resourceAdapterClass' is required");
        }
        if (this.bootstrapContext == null) {
            this.bootstrapContext = new SimpleBootstrapContext(this.workManager, this.xaTerminator);
        }
        this.resourceAdapter.start(this.bootstrapContext);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public ResourceAdapter getObject() {
        return this.resourceAdapter;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<? extends ResourceAdapter> getObjectType() {
        return this.resourceAdapter != null ? this.resourceAdapter.getClass() : ResourceAdapter.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        this.resourceAdapter.stop();
    }
}
