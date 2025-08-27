package org.mybatis.spring.config;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/config/MapperScannerBeanDefinitionParser.class */
public class MapperScannerBeanDefinitionParser implements BeanDefinitionParser {
    private static final String ATTRIBUTE_BASE_PACKAGE = "base-package";
    private static final String ATTRIBUTE_ANNOTATION = "annotation";
    private static final String ATTRIBUTE_MARKER_INTERFACE = "marker-interface";
    private static final String ATTRIBUTE_NAME_GENERATOR = "name-generator";
    private static final String ATTRIBUTE_TEMPLATE_REF = "template-ref";
    private static final String ATTRIBUTE_FACTORY_REF = "factory-ref";

    @Override // org.springframework.beans.factory.xml.BeanDefinitionParser
    public synchronized BeanDefinition parse(Element element, ParserContext parserContext) throws ClassNotFoundException {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(parserContext.getRegistry());
        ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
        XmlReaderContext readerContext = parserContext.getReaderContext();
        scanner.setResourceLoader(readerContext.getResourceLoader());
        try {
            String annotationClassName = element.getAttribute("annotation");
            if (StringUtils.hasText(annotationClassName)) {
                scanner.setAnnotationClass(classLoader.loadClass(annotationClassName));
            }
            String markerInterfaceClassName = element.getAttribute(ATTRIBUTE_MARKER_INTERFACE);
            if (StringUtils.hasText(markerInterfaceClassName)) {
                Class<?> markerInterface = classLoader.loadClass(markerInterfaceClassName);
                scanner.setMarkerInterface(markerInterface);
            }
            String nameGeneratorClassName = element.getAttribute(ATTRIBUTE_NAME_GENERATOR);
            if (StringUtils.hasText(nameGeneratorClassName)) {
                Class<?> nameGeneratorClass = classLoader.loadClass(nameGeneratorClassName);
                BeanNameGenerator nameGenerator = (BeanNameGenerator) BeanUtils.instantiateClass(nameGeneratorClass, BeanNameGenerator.class);
                scanner.setBeanNameGenerator(nameGenerator);
            }
        } catch (Exception ex) {
            readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
        }
        String sqlSessionTemplateBeanName = element.getAttribute(ATTRIBUTE_TEMPLATE_REF);
        scanner.setSqlSessionTemplateBeanName(sqlSessionTemplateBeanName);
        String sqlSessionFactoryBeanName = element.getAttribute(ATTRIBUTE_FACTORY_REF);
        scanner.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);
        scanner.registerFilters();
        String basePackage = element.getAttribute(ATTRIBUTE_BASE_PACKAGE);
        scanner.scan(StringUtils.tokenizeToStringArray(basePackage, ",; \t\n"));
        return null;
    }
}
