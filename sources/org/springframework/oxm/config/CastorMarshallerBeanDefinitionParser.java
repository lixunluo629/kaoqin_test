package org.springframework.oxm.config;

import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

@Deprecated
/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/config/CastorMarshallerBeanDefinitionParser.class */
class CastorMarshallerBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
    CastorMarshallerBeanDefinitionParser() {
    }

    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected String getBeanClassName(Element element) {
        return "org.springframework.oxm.castor.CastorMarshaller";
    }
}
