package org.springframework.data.auditing.config;

import org.springframework.data.auditing.IsNewAwareAuditingHandler;
import org.w3c.dom.Element;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/config/IsNewAwareAuditingHandlerBeanDefinitionParser.class */
public class IsNewAwareAuditingHandlerBeanDefinitionParser extends AuditingHandlerBeanDefinitionParser {
    public IsNewAwareAuditingHandlerBeanDefinitionParser(String mappingContextBeanName) {
        super(mappingContextBeanName);
    }

    @Override // org.springframework.data.auditing.config.AuditingHandlerBeanDefinitionParser, org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected Class<?> getBeanClass(Element element) {
        return IsNewAwareAuditingHandler.class;
    }
}
