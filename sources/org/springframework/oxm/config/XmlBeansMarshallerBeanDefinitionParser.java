package org.springframework.oxm.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

@Deprecated
/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/config/XmlBeansMarshallerBeanDefinitionParser.class */
class XmlBeansMarshallerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    XmlBeansMarshallerBeanDefinitionParser() {
    }

    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected String getBeanClassName(Element element) {
        return "org.springframework.oxm.xmlbeans.XmlBeansMarshaller";
    }

    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder beanDefinitionBuilder) {
        String optionsName = element.getAttribute("options");
        if (StringUtils.hasText(optionsName)) {
            beanDefinitionBuilder.addPropertyReference("xmlOptions", optionsName);
        }
    }
}
