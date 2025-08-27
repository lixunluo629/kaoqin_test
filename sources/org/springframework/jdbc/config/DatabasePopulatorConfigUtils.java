package org.springframework.jdbc.config;

import java.util.List;
import org.apache.commons.codec.language.bm.Rule;
import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.jdbc.datasource.init.CompositeDatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/config/DatabasePopulatorConfigUtils.class */
class DatabasePopulatorConfigUtils {
    DatabasePopulatorConfigUtils() {
    }

    public static void setDatabasePopulator(Element element, BeanDefinitionBuilder builder) {
        List<Element> scripts = DomUtils.getChildElementsByTagName(element, "script");
        if (scripts.size() > 0) {
            builder.addPropertyValue("databasePopulator", createDatabasePopulator(element, scripts, "INIT"));
            builder.addPropertyValue("databaseCleaner", createDatabasePopulator(element, scripts, "DESTROY"));
        }
    }

    private static BeanDefinition createDatabasePopulator(Element element, List<Element> scripts, String execution) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition((Class<?>) CompositeDatabasePopulator.class);
        boolean ignoreFailedDrops = element.getAttribute("ignore-failures").equals("DROPS");
        boolean continueOnError = element.getAttribute("ignore-failures").equals(Rule.ALL);
        ManagedList<BeanMetadataElement> delegates = new ManagedList<>();
        for (Element scriptElement : scripts) {
            String executionAttr = scriptElement.getAttribute("execution");
            if (!StringUtils.hasText(executionAttr)) {
                executionAttr = "INIT";
            }
            if (execution.equals(executionAttr)) {
                BeanDefinitionBuilder delegate = BeanDefinitionBuilder.genericBeanDefinition((Class<?>) ResourceDatabasePopulator.class);
                delegate.addPropertyValue("ignoreFailedDrops", Boolean.valueOf(ignoreFailedDrops));
                delegate.addPropertyValue("continueOnError", Boolean.valueOf(continueOnError));
                BeanDefinitionBuilder resourcesFactory = BeanDefinitionBuilder.genericBeanDefinition((Class<?>) SortedResourcesFactoryBean.class);
                resourcesFactory.addConstructorArgValue(new TypedStringValue(scriptElement.getAttribute("location")));
                delegate.addPropertyValue("scripts", resourcesFactory.getBeanDefinition());
                if (StringUtils.hasLength(scriptElement.getAttribute("encoding"))) {
                    delegate.addPropertyValue("sqlScriptEncoding", new TypedStringValue(scriptElement.getAttribute("encoding")));
                }
                String separator = getSeparator(element, scriptElement);
                if (separator != null) {
                    delegate.addPropertyValue("separator", new TypedStringValue(separator));
                }
                delegates.add(delegate.getBeanDefinition());
            }
        }
        builder.addPropertyValue("populators", delegates);
        return builder.getBeanDefinition();
    }

    private static String getSeparator(Element element, Element scriptElement) {
        String scriptSeparator = scriptElement.getAttribute("separator");
        if (StringUtils.hasLength(scriptSeparator)) {
            return scriptSeparator;
        }
        String elementSeparator = element.getAttribute("separator");
        if (StringUtils.hasLength(elementSeparator)) {
            return elementSeparator;
        }
        return null;
    }
}
