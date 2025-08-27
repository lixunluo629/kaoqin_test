package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.w3c.dom.Element;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/xml/BeanDefinitionParser.class */
public interface BeanDefinitionParser {
    BeanDefinition parse(Element element, ParserContext parserContext);
}
