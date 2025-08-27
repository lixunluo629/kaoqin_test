package org.springframework.data.repository.init;

import com.fasterxml.jackson.databind.ObjectMapper;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/init/Jackson2RepositoryPopulatorFactoryBean.class */
public class Jackson2RepositoryPopulatorFactoryBean extends AbstractRepositoryPopulatorFactoryBean {
    private ObjectMapper mapper;

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override // org.springframework.data.repository.init.AbstractRepositoryPopulatorFactoryBean
    protected ResourceReader getResourceReader() {
        return new Jackson2ResourceReader(this.mapper);
    }
}
