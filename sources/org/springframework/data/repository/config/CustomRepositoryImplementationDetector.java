package org.springframework.data.repository.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/CustomRepositoryImplementationDetector.class */
public class CustomRepositoryImplementationDetector {
    private static final String CUSTOM_IMPLEMENTATION_RESOURCE_PATTERN = "**/%s.class";
    private final MetadataReaderFactory metadataReaderFactory;
    private final Environment environment;
    private final ResourceLoader resourceLoader;

    public CustomRepositoryImplementationDetector(MetadataReaderFactory metadataReaderFactory, Environment environment, ResourceLoader resourceLoader) {
        Assert.notNull(metadataReaderFactory, "MetadataReaderFactory must not be null!");
        Assert.notNull(resourceLoader, "ResourceLoader must not be null!");
        Assert.notNull(environment, "Environment must not be null!");
        this.metadataReaderFactory = metadataReaderFactory;
        this.environment = environment;
        this.resourceLoader = resourceLoader;
    }

    public AbstractBeanDefinition detectCustomImplementation(RepositoryConfiguration<?> configuration) {
        return detectCustomImplementation(configuration.getImplementationClassName(), configuration.getImplementationBasePackages(), configuration.getExcludeFilters());
    }

    public AbstractBeanDefinition detectCustomImplementation(String className, Iterable<String> basePackages, Iterable<TypeFilter> excludeFilters) {
        Assert.notNull(className, "ClassName must not be null!");
        Assert.notNull(basePackages, "BasePackages must not be null!");
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false, this.environment);
        provider.setResourceLoader(this.resourceLoader);
        provider.setResourcePattern(String.format(CUSTOM_IMPLEMENTATION_RESOURCE_PATTERN, className));
        provider.setMetadataReaderFactory(this.metadataReaderFactory);
        provider.addIncludeFilter(AlwaysIncludeFilter.INSTANCE);
        for (TypeFilter excludeFilter : excludeFilters) {
            provider.addExcludeFilter(excludeFilter);
        }
        Set<BeanDefinition> definitions = new HashSet<>();
        for (String basePackage : basePackages) {
            definitions.addAll(provider.findCandidateComponents(basePackage));
        }
        if (definitions.isEmpty()) {
            return null;
        }
        if (definitions.size() == 1) {
            return (AbstractBeanDefinition) definitions.iterator().next();
        }
        List<String> implementationClassNames = new ArrayList<>();
        for (BeanDefinition bean : definitions) {
            implementationClassNames.add(bean.getBeanClassName());
        }
        throw new IllegalStateException(String.format("Ambiguous custom implementations detected! Found %s but expected a single implementation!", StringUtils.collectionToCommaDelimitedString(implementationClassNames)));
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/CustomRepositoryImplementationDetector$AlwaysIncludeFilter.class */
    private enum AlwaysIncludeFilter implements TypeFilter {
        INSTANCE;

        @Override // org.springframework.core.type.filter.TypeFilter
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            return true;
        }
    }
}
