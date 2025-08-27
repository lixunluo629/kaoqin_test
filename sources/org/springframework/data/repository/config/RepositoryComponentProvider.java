package org.springframework.data.repository.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.util.ClassUtils;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryComponentProvider.class */
class RepositoryComponentProvider extends ClassPathScanningCandidateComponentProvider {
    private boolean considerNestedRepositoryInterfaces;
    private BeanDefinitionRegistry registry;

    public RepositoryComponentProvider(Iterable<? extends TypeFilter> includeFilters, BeanDefinitionRegistry registry) {
        super(false);
        Assert.notNull(includeFilters, "Include filters must not be null!");
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
        this.registry = registry;
        if (includeFilters.iterator().hasNext()) {
            for (TypeFilter filter : includeFilters) {
                addIncludeFilter(filter);
            }
        } else {
            super.addIncludeFilter(new InterfaceTypeFilter(Repository.class));
            super.addIncludeFilter(new AnnotationTypeFilter(RepositoryDefinition.class, true, true));
        }
        addExcludeFilter(new AnnotationTypeFilter(NoRepositoryBean.class));
    }

    @Override // org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
    public void addIncludeFilter(TypeFilter includeFilter) {
        List<TypeFilter> filterPlusInterface = new ArrayList<>(2);
        filterPlusInterface.add(includeFilter);
        filterPlusInterface.add(new InterfaceTypeFilter(Repository.class));
        super.addIncludeFilter(new AllTypeFilter(filterPlusInterface));
        List<TypeFilter> filterPlusAnnotation = new ArrayList<>(2);
        filterPlusAnnotation.add(includeFilter);
        filterPlusAnnotation.add(new AnnotationTypeFilter(RepositoryDefinition.class, true, true));
        super.addIncludeFilter(new AllTypeFilter(filterPlusAnnotation));
    }

    @Override // org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean isNonRepositoryInterface = !ClassUtils.isGenericRepositoryInterface(beanDefinition.getBeanClassName());
        boolean isTopLevelType = !beanDefinition.getMetadata().hasEnclosingClass();
        boolean isConsiderNestedRepositories = isConsiderNestedRepositoryInterfaces();
        return isNonRepositoryInterface && (isTopLevelType || isConsiderNestedRepositories);
    }

    @Override // org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = super.findCandidateComponents(basePackage);
        for (BeanDefinition candidate : candidates) {
            if (candidate instanceof AnnotatedBeanDefinition) {
                AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition) candidate);
            }
        }
        return candidates;
    }

    @Override // org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
    protected BeanDefinitionRegistry getRegistry() {
        return this.registry;
    }

    public boolean isConsiderNestedRepositoryInterfaces() {
        return this.considerNestedRepositoryInterfaces;
    }

    public void setConsiderNestedRepositoryInterfaces(boolean considerNestedRepositoryInterfaces) {
        this.considerNestedRepositoryInterfaces = considerNestedRepositoryInterfaces;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryComponentProvider$InterfaceTypeFilter.class */
    private static class InterfaceTypeFilter extends AssignableTypeFilter {
        public InterfaceTypeFilter(Class<?> targetType) {
            super(targetType);
        }

        @Override // org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter, org.springframework.core.type.filter.TypeFilter
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            return metadataReader.getClassMetadata().isInterface() && super.match(metadataReader, metadataReaderFactory);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryComponentProvider$AllTypeFilter.class */
    private static class AllTypeFilter implements TypeFilter {
        private final List<TypeFilter> delegates;

        public AllTypeFilter(List<TypeFilter> delegates) {
            Assert.notNull(delegates, "TypeFilter deleages must not be null!");
            this.delegates = delegates;
        }

        @Override // org.springframework.core.type.filter.TypeFilter
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            for (TypeFilter filter : this.delegates) {
                if (!filter.match(metadataReader, metadataReaderFactory)) {
                    return false;
                }
            }
            return true;
        }
    }
}
