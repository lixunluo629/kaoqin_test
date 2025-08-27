package org.springframework.data.repository.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryConfigurationSourceSupport.class */
public abstract class RepositoryConfigurationSourceSupport implements RepositoryConfigurationSource {
    protected static final String DEFAULT_REPOSITORY_IMPL_POSTFIX = "Impl";
    private final Environment environment;
    private final BeanDefinitionRegistry registry;

    public RepositoryConfigurationSourceSupport(Environment environment, BeanDefinitionRegistry registry) {
        Assert.notNull(environment, "Environment must not be null!");
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
        this.environment = environment;
        this.registry = registry;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public Collection<BeanDefinition> getCandidates(ResourceLoader loader) {
        RepositoryComponentProvider scanner = new RepositoryComponentProvider(getIncludeFilters(), this.registry);
        scanner.setConsiderNestedRepositoryInterfaces(shouldConsiderNestedRepositories());
        scanner.setEnvironment(this.environment);
        scanner.setResourceLoader(loader);
        for (TypeFilter filter : getExcludeFilters()) {
            scanner.addExcludeFilter(filter);
        }
        Set<BeanDefinition> result = new HashSet<>();
        for (String basePackage : getBasePackages()) {
            Set<BeanDefinition> candidate = scanner.findCandidateComponents(basePackage);
            result.addAll(candidate);
        }
        return result;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public Iterable<TypeFilter> getExcludeFilters() {
        return Collections.emptySet();
    }

    protected Iterable<TypeFilter> getIncludeFilters() {
        return Collections.emptySet();
    }

    public boolean shouldConsiderNestedRepositories() {
        return false;
    }
}
