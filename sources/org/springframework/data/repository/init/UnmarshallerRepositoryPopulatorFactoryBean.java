package org.springframework.data.repository.init;

import org.springframework.oxm.Unmarshaller;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/init/UnmarshallerRepositoryPopulatorFactoryBean.class */
public class UnmarshallerRepositoryPopulatorFactoryBean extends AbstractRepositoryPopulatorFactoryBean {
    private Unmarshaller unmarshaller;

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Override // org.springframework.data.repository.init.AbstractRepositoryPopulatorFactoryBean
    protected ResourceReader getResourceReader() {
        return new UnmarshallingResourceReader(this.unmarshaller);
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.unmarshaller, "No Unmarshaller configured!");
        super.afterPropertiesSet();
    }
}
