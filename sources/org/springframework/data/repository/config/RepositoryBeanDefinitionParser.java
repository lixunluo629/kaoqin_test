package org.springframework.data.repository.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryBeanDefinitionParser.class */
public class RepositoryBeanDefinitionParser implements BeanDefinitionParser {
    private final RepositoryConfigurationExtension extension;

    public RepositoryBeanDefinitionParser(RepositoryConfigurationExtension extension) {
        Assert.notNull(extension, "Extension must not be null!");
        this.extension = extension;
    }

    @Override // org.springframework.beans.factory.xml.BeanDefinitionParser
    public BeanDefinition parse(Element element, ParserContext parser) {
        XmlReaderContext readerContext = parser.getReaderContext();
        try {
            Environment environment = readerContext.getEnvironment();
            ResourceLoader resourceLoader = readerContext.getResourceLoader();
            BeanDefinitionRegistry registry = parser.getRegistry();
            XmlRepositoryConfigurationSource configSource = new XmlRepositoryConfigurationSource(element, parser, environment);
            RepositoryConfigurationDelegate delegate = new RepositoryConfigurationDelegate(configSource, resourceLoader, environment);
            RepositoryConfigurationUtils.exposeRegistration(this.extension, registry, configSource);
            for (BeanComponentDefinition definition : delegate.registerRepositoriesIn(registry, this.extension)) {
                readerContext.fireComponentRegistered(definition);
            }
            return null;
        } catch (RuntimeException e) {
            handleError(e, element, readerContext);
            return null;
        }
    }

    private void handleError(Exception e, Element source, ReaderContext reader) {
        reader.error(e.getMessage(), reader.extractSource(source), e);
    }

    protected static boolean hasBean(Class<?> type, BeanDefinitionRegistry registry) {
        String name = String.format("%s%s0", type.getName(), "#");
        return registry.containsBeanDefinition(name);
    }
}
