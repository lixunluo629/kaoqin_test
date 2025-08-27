package org.springframework.data.auditing.config;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.config.ParsingUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/config/AuditingHandlerBeanDefinitionParser.class */
public class AuditingHandlerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final String AUDITOR_AWARE_REF = "auditor-aware-ref";
    private final String mappingContextBeanName;
    private String resolvedBeanName;

    public AuditingHandlerBeanDefinitionParser(String mappingContextBeanName) {
        Assert.hasText(mappingContextBeanName, "MappingContext bean name must not be null!");
        this.mappingContextBeanName = mappingContextBeanName;
    }

    public String getResolvedBeanName() {
        return this.resolvedBeanName;
    }

    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected Class<?> getBeanClass(Element element) {
        return AuditingHandler.class;
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected boolean shouldGenerateId() {
        return true;
    }

    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        builder.addConstructorArgReference(this.mappingContextBeanName);
        String auditorAwareRef = element.getAttribute(AUDITOR_AWARE_REF);
        if (StringUtils.hasText(auditorAwareRef)) {
            builder.addPropertyValue("auditorAware", createLazyInitTargetSourceBeanDefinition(auditorAwareRef));
        }
        ParsingUtils.setPropertyValue(builder, element, "set-dates", "dateTimeForNow");
        ParsingUtils.setPropertyReference(builder, element, "date-time-provider-ref", "dateTimeProvider");
        ParsingUtils.setPropertyValue(builder, element, "modify-on-creation", "modifyOnCreation");
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
        this.resolvedBeanName = super.resolveId(element, definition, parserContext);
        return this.resolvedBeanName;
    }

    private BeanDefinition createLazyInitTargetSourceBeanDefinition(String auditorAwareRef) {
        BeanDefinitionBuilder targetSourceBuilder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) LazyInitTargetSource.class);
        targetSourceBuilder.addPropertyValue("targetBeanName", auditorAwareRef);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) ProxyFactoryBean.class);
        builder.addPropertyValue("targetSource", targetSourceBuilder.getBeanDefinition());
        return builder.getBeanDefinition();
    }
}
