package org.springframework.ejb.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/ejb/config/JeeNamespaceHandler.class */
public class JeeNamespaceHandler extends NamespaceHandlerSupport {
    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("jndi-lookup", new JndiLookupBeanDefinitionParser());
        registerBeanDefinitionParser("local-slsb", new LocalStatelessSessionBeanDefinitionParser());
        registerBeanDefinitionParser("remote-slsb", new RemoteStatelessSessionBeanDefinitionParser());
    }
}
