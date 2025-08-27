package org.springframework.data.repository.config;

import java.util.Arrays;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.data.repository.init.UnmarshallerRepositoryPopulatorFactoryBean;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/ResourceReaderRepositoryPopulatorBeanDefinitionParser.class */
public class ResourceReaderRepositoryPopulatorBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected String getBeanClassName(Element element) {
        String name = element.getLocalName();
        if ("unmarshaller-populator".equals(name)) {
            return UnmarshallerRepositoryPopulatorFactoryBean.class.getName();
        }
        if ("jackson2-populator".equals(name)) {
            return Jackson2RepositoryPopulatorFactoryBean.class.getName();
        }
        throw new IllegalStateException("Unsupported populator type " + name + "!");
    }

    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String localName = element.getLocalName();
        builder.addPropertyValue("resources", element.getAttribute("locations"));
        if ("unmarshaller-populator".equals(localName)) {
            parseXmlPopulator(element, builder);
        } else if (Arrays.asList("jackson-populator", "jackson2-populator").contains(localName)) {
            parseJsonPopulator(element, builder);
        }
    }

    private static void parseJsonPopulator(Element element, BeanDefinitionBuilder builder) {
        String objectMapperRef = element.getAttribute("object-mapper-ref");
        if (StringUtils.hasText(objectMapperRef)) {
            builder.addPropertyReference("mapper", objectMapperRef);
        }
    }

    private static void parseXmlPopulator(Element element, BeanDefinitionBuilder builder) {
        String unmarshallerRefName = element.getAttribute("unmarshaller-ref");
        if (StringUtils.hasText(unmarshallerRefName)) {
            builder.addPropertyReference("unmarshaller", unmarshallerRefName);
        }
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }
}
