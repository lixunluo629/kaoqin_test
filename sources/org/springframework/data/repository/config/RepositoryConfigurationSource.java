package org.springframework.data.repository.config;

import java.util.Collection;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.TypeFilter;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryConfigurationSource.class */
public interface RepositoryConfigurationSource {
    Object getSource();

    Iterable<String> getBasePackages();

    Object getQueryLookupStrategyKey();

    String getRepositoryImplementationPostfix();

    String getNamedQueryLocation();

    @Deprecated
    String getRepositoryFactoryBeanName();

    String getRepositoryBaseClassName();

    Collection<BeanDefinition> getCandidates(ResourceLoader resourceLoader);

    String getAttribute(String str);

    boolean usesExplicitFilters();

    Iterable<TypeFilter> getExcludeFilters();
}
