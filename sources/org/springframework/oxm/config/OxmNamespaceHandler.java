package org.springframework.oxm.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/config/OxmNamespaceHandler.class */
public class OxmNamespaceHandler extends NamespaceHandlerSupport {
    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("jaxb2-marshaller", new Jaxb2MarshallerBeanDefinitionParser());
        registerBeanDefinitionParser("jibx-marshaller", new JibxMarshallerBeanDefinitionParser());
        registerBeanDefinitionParser("castor-marshaller", new CastorMarshallerBeanDefinitionParser());
        registerBeanDefinitionParser("xmlbeans-marshaller", new XmlBeansMarshallerBeanDefinitionParser());
    }
}
