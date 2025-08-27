package org.springframework.context.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.jmx.support.WebSphereMBeanServerFactoryBean;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/config/MBeanServerBeanDefinitionParser.class */
class MBeanServerBeanDefinitionParser extends AbstractBeanDefinitionParser {
    private static final String MBEAN_SERVER_BEAN_NAME = "mbeanServer";
    private static final String AGENT_ID_ATTRIBUTE = "agent-id";
    private static final boolean weblogicPresent = ClassUtils.isPresent("weblogic.management.Helper", MBeanServerBeanDefinitionParser.class.getClassLoader());
    private static final boolean webspherePresent = ClassUtils.isPresent("com.ibm.websphere.management.AdminServiceFactory", MBeanServerBeanDefinitionParser.class.getClassLoader());

    MBeanServerBeanDefinitionParser() {
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
        String id = element.getAttribute("id");
        return StringUtils.hasText(id) ? id : MBEAN_SERVER_BEAN_NAME;
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        String agentId = element.getAttribute(AGENT_ID_ATTRIBUTE);
        if (StringUtils.hasText(agentId)) {
            RootBeanDefinition bd = new RootBeanDefinition((Class<?>) MBeanServerFactoryBean.class);
            bd.getPropertyValues().add("agentId", agentId);
            return bd;
        }
        AbstractBeanDefinition specialServer = findServerForSpecialEnvironment();
        if (specialServer != null) {
            return specialServer;
        }
        RootBeanDefinition bd2 = new RootBeanDefinition((Class<?>) MBeanServerFactoryBean.class);
        bd2.getPropertyValues().add("locateExistingServerIfPossible", Boolean.TRUE);
        bd2.setRole(2);
        bd2.setSource(parserContext.extractSource(element));
        return bd2;
    }

    static AbstractBeanDefinition findServerForSpecialEnvironment() {
        if (weblogicPresent) {
            RootBeanDefinition bd = new RootBeanDefinition((Class<?>) JndiObjectFactoryBean.class);
            bd.getPropertyValues().add("jndiName", "java:comp/env/jmx/runtime");
            return bd;
        }
        if (webspherePresent) {
            return new RootBeanDefinition((Class<?>) WebSphereMBeanServerFactoryBean.class);
        }
        return null;
    }
}
