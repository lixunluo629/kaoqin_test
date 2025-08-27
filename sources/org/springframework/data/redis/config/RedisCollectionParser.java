package org.springframework.data.redis.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.data.redis.support.collections.RedisCollectionFactoryBean;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/config/RedisCollectionParser.class */
class RedisCollectionParser extends AbstractSimpleBeanDefinitionParser {
    RedisCollectionParser() {
    }

    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected Class<?> getBeanClass(Element element) {
        return RedisCollectionFactoryBean.class;
    }

    @Override // org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser
    protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {
        String template = element.getAttribute("template");
        if (StringUtils.hasText(template)) {
            beanDefinition.addPropertyReference("template", template);
        }
    }

    @Override // org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser
    protected boolean isEligibleAttribute(String attributeName) {
        return super.isEligibleAttribute(attributeName) && !"template".equals(attributeName);
    }
}
