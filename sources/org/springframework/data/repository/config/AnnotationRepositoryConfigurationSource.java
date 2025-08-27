package org.springframework.data.repository.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/AnnotationRepositoryConfigurationSource.class */
public class AnnotationRepositoryConfigurationSource extends RepositoryConfigurationSourceSupport {
    private static final String REPOSITORY_IMPLEMENTATION_POSTFIX = "repositoryImplementationPostfix";
    private static final String BASE_PACKAGES = "basePackages";
    private static final String BASE_PACKAGE_CLASSES = "basePackageClasses";
    private static final String NAMED_QUERIES_LOCATION = "namedQueriesLocation";
    private static final String QUERY_LOOKUP_STRATEGY = "queryLookupStrategy";
    private static final String REPOSITORY_FACTORY_BEAN_CLASS = "repositoryFactoryBeanClass";
    private static final String REPOSITORY_BASE_CLASS = "repositoryBaseClass";
    private static final String CONSIDER_NESTED_REPOSITORIES = "considerNestedRepositories";
    private final AnnotationMetadata configMetadata;
    private final AnnotationMetadata enableAnnotationMetadata;
    private final AnnotationAttributes attributes;
    private final ResourceLoader resourceLoader;
    private final boolean hasExplicitFilters;

    public AnnotationRepositoryConfigurationSource(AnnotationMetadata metadata, Class<? extends Annotation> annotation, ResourceLoader resourceLoader, Environment environment, BeanDefinitionRegistry registry) {
        super(environment, registry);
        Assert.notNull(metadata, "Metadata must not be null!");
        Assert.notNull(annotation, "Annotation must not be null!");
        Assert.notNull(resourceLoader, "ResourceLoader must not be null!");
        this.attributes = new AnnotationAttributes(metadata.getAnnotationAttributes(annotation.getName()));
        this.enableAnnotationMetadata = new StandardAnnotationMetadata(annotation);
        this.configMetadata = metadata;
        this.resourceLoader = resourceLoader;
        this.hasExplicitFilters = hasExplicitFilters(this.attributes);
    }

    private static boolean hasExplicitFilters(AnnotationAttributes attributes) {
        for (String attribute : Arrays.asList("includeFilters", "excludeFilters")) {
            if (attributes.getAnnotationArray(attribute).length > 0) {
                return true;
            }
        }
        return false;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public Iterable<String> getBasePackages() {
        String[] value = this.attributes.getStringArray("value");
        String[] basePackages = this.attributes.getStringArray(BASE_PACKAGES);
        Class<?>[] basePackageClasses = this.attributes.getClassArray(BASE_PACKAGE_CLASSES);
        if (value.length == 0 && basePackages.length == 0 && basePackageClasses.length == 0) {
            String className = this.configMetadata.getClassName();
            return Collections.singleton(ClassUtils.getPackageName(className));
        }
        Set<String> packages = new HashSet<>();
        packages.addAll(Arrays.asList(value));
        packages.addAll(Arrays.asList(basePackages));
        for (Class<?> typeName : basePackageClasses) {
            packages.add(ClassUtils.getPackageName(typeName));
        }
        return packages;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public Object getQueryLookupStrategyKey() {
        return this.attributes.get(QUERY_LOOKUP_STRATEGY);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public String getNamedQueryLocation() {
        return getNullDefaultedAttribute(NAMED_QUERIES_LOCATION);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public String getRepositoryImplementationPostfix() {
        return getNullDefaultedAttribute(REPOSITORY_IMPLEMENTATION_POSTFIX);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public Object getSource() {
        return this.configMetadata;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSourceSupport
    protected Iterable<TypeFilter> getIncludeFilters() {
        return parseFilters("includeFilters");
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSourceSupport, org.springframework.data.repository.config.RepositoryConfigurationSource
    public Iterable<TypeFilter> getExcludeFilters() {
        return parseFilters("excludeFilters");
    }

    private Set<TypeFilter> parseFilters(String attributeName) {
        Set<TypeFilter> result = new HashSet<>();
        AnnotationAttributes[] filters = this.attributes.getAnnotationArray(attributeName);
        for (AnnotationAttributes filter : filters) {
            result.addAll(typeFiltersFor(filter));
        }
        return result;
    }

    private String getNullDefaultedAttribute(String attributeName) {
        String attribute = this.attributes.getString(attributeName);
        if (StringUtils.hasText(attribute)) {
            return attribute;
        }
        return null;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public String getRepositoryFactoryBeanName() {
        return this.attributes.getClass(REPOSITORY_FACTORY_BEAN_CLASS).getName();
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public String getRepositoryBaseClassName() {
        if (!this.attributes.containsKey(REPOSITORY_BASE_CLASS)) {
            return null;
        }
        Class<? extends Object> repositoryBaseClass = this.attributes.getClass(REPOSITORY_BASE_CLASS);
        if (DefaultRepositoryBaseClass.class.equals(repositoryBaseClass)) {
            return null;
        }
        return repositoryBaseClass.getName();
    }

    public AnnotationAttributes getAttributes() {
        return this.attributes;
    }

    public AnnotationMetadata getEnableAnnotationMetadata() {
        return this.enableAnnotationMetadata;
    }

    private List<TypeFilter> typeFiltersFor(AnnotationAttributes filterAttributes) {
        ArrayList arrayList = new ArrayList();
        FilterType filterType = (FilterType) filterAttributes.getEnum("type");
        for (Class<?> filterClass : filterAttributes.getClassArray("value")) {
            switch (filterType) {
                case ANNOTATION:
                    Assert.isAssignable(Annotation.class, filterClass, "An error occured when processing a @ComponentScan ANNOTATION type filter: ");
                    arrayList.add(new AnnotationTypeFilter(filterClass));
                    break;
                case ASSIGNABLE_TYPE:
                    arrayList.add(new AssignableTypeFilter(filterClass));
                    break;
                case CUSTOM:
                    Assert.isAssignable(TypeFilter.class, filterClass, "An error occured when processing a @ComponentScan CUSTOM type filter: ");
                    arrayList.add(BeanUtils.instantiateClass(filterClass, TypeFilter.class));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown filter type " + filterType);
            }
        }
        for (String expression : getPatterns(filterAttributes)) {
            String rawName = filterType.toString();
            if ("REGEX".equals(rawName)) {
                arrayList.add(new RegexPatternTypeFilter(Pattern.compile(expression)));
            } else if ("ASPECTJ".equals(rawName)) {
                arrayList.add(new AspectJTypeFilter(expression, this.resourceLoader.getClassLoader()));
            } else {
                throw new IllegalArgumentException("Unknown filter type " + filterType);
            }
        }
        return arrayList;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSourceSupport
    public boolean shouldConsiderNestedRepositories() {
        return this.attributes.containsKey(CONSIDER_NESTED_REPOSITORIES) && this.attributes.getBoolean(CONSIDER_NESTED_REPOSITORIES);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public String getAttribute(String name) {
        String attribute = this.attributes.getString(name);
        if (StringUtils.hasText(attribute)) {
            return attribute;
        }
        return null;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public boolean usesExplicitFilters() {
        return this.hasExplicitFilters;
    }

    private String[] getPatterns(AnnotationAttributes filterAttributes) {
        try {
            return filterAttributes.getStringArray("pattern");
        } catch (IllegalArgumentException e) {
            return new String[0];
        }
    }
}
