package org.springframework.web.servlet.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/VelocityConfigurerBeanDefinitionParser.class */
public class VelocityConfigurerBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
    public static final String BEAN_NAME = "mvcVelocityConfigurer";

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
        return BEAN_NAME;
    }

    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected String getBeanClassName(Element element) {
        return "org.springframework.web.servlet.view.velocity.VelocityConfigurer";
    }

    @Override // org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser
    protected boolean isEligibleAttribute(String attributeName) {
        return attributeName.equals("resource-loader-path");
    }

    @Override // org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser
    protected void postProcess(BeanDefinitionBuilder builder, Element element) {
        if (!builder.getBeanDefinition().hasAttribute("resourceLoaderPath")) {
            builder.getBeanDefinition().setAttribute("resourceLoaderPath", "/WEB-INF/");
        }
    }
}
