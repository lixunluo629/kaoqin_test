package org.springframework.data.repository.config;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.data.config.TypeFilterParser;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.util.ParsingUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/XmlRepositoryConfigurationSource.class */
public class XmlRepositoryConfigurationSource extends RepositoryConfigurationSourceSupport {
    private static final String QUERY_LOOKUP_STRATEGY = "query-lookup-strategy";
    private static final String BASE_PACKAGE = "base-package";
    private static final String NAMED_QUERIES_LOCATION = "named-queries-location";
    private static final String REPOSITORY_IMPL_POSTFIX = "repository-impl-postfix";
    private static final String REPOSITORY_FACTORY_BEAN_CLASS_NAME = "factory-class";
    private static final String REPOSITORY_BASE_CLASS_NAME = "base-class";
    private static final String CONSIDER_NESTED_REPOSITORIES = "consider-nested-repositories";
    private final Element element;
    private final ParserContext context;
    private final Collection<TypeFilter> includeFilters;
    private final Collection<TypeFilter> excludeFilters;

    public XmlRepositoryConfigurationSource(Element element, ParserContext context, Environment environment) {
        super(environment, context.getRegistry());
        Assert.notNull(element, "Element must not be null!");
        this.element = element;
        this.context = context;
        TypeFilterParser parser = new TypeFilterParser(context.getReaderContext());
        this.includeFilters = parser.parseTypeFilters(element, TypeFilterParser.Type.INCLUDE);
        this.excludeFilters = parser.parseTypeFilters(element, TypeFilterParser.Type.EXCLUDE);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public Object getSource() {
        return this.context.extractSource(this.element);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public Iterable<String> getBasePackages() {
        String attribute = this.element.getAttribute(BASE_PACKAGE);
        return Arrays.asList(StringUtils.delimitedListToStringArray(attribute, ",", SymbolConstants.SPACE_SYMBOL));
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public Object getQueryLookupStrategyKey() {
        return QueryLookupStrategy.Key.create(getNullDefaultedAttribute(this.element, QUERY_LOOKUP_STRATEGY));
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public String getNamedQueryLocation() {
        return getNullDefaultedAttribute(this.element, NAMED_QUERIES_LOCATION);
    }

    public Element getElement() {
        return this.element;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSourceSupport, org.springframework.data.repository.config.RepositoryConfigurationSource
    public Iterable<TypeFilter> getExcludeFilters() {
        return this.excludeFilters;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSourceSupport
    protected Iterable<TypeFilter> getIncludeFilters() {
        return this.includeFilters;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public String getRepositoryImplementationPostfix() {
        return getNullDefaultedAttribute(this.element, REPOSITORY_IMPL_POSTFIX);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public String getRepositoryFactoryBeanName() {
        return getNullDefaultedAttribute(this.element, REPOSITORY_FACTORY_BEAN_CLASS_NAME);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public String getRepositoryBaseClassName() {
        return getNullDefaultedAttribute(this.element, REPOSITORY_BASE_CLASS_NAME);
    }

    private String getNullDefaultedAttribute(Element element, String attributeName) {
        String attribute = element.getAttribute(attributeName);
        if (StringUtils.hasText(attribute)) {
            return attribute;
        }
        return null;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSourceSupport
    public boolean shouldConsiderNestedRepositories() {
        String attribute = getNullDefaultedAttribute(this.element, CONSIDER_NESTED_REPOSITORIES);
        return attribute != null && Boolean.parseBoolean(attribute);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public String getAttribute(String name) {
        String xmlAttributeName = ParsingUtils.reconcatenateCamelCase(name, "-");
        String attribute = this.element.getAttribute(xmlAttributeName);
        if (StringUtils.hasText(attribute)) {
            return attribute;
        }
        return null;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationSource
    public boolean usesExplicitFilters() {
        return (this.includeFilters.isEmpty() && this.excludeFilters.isEmpty()) ? false : true;
    }
}
