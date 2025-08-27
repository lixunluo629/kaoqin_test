package org.hibernate.validator.internal.metadata.provider;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.xml.XmlMappingParser;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/provider/XmlMetaDataProvider.class */
public class XmlMetaDataProvider extends MetaDataProviderKeyedByClassName {
    private final AnnotationProcessingOptions annotationProcessingOptions;

    public XmlMetaDataProvider(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, Set<InputStream> mappingStreams, ClassLoader externalClassLoader) {
        this(constraintHelper, createMappingParser(constraintHelper, parameterNameProvider, mappingStreams, externalClassLoader));
    }

    private XmlMetaDataProvider(ConstraintHelper constraintHelper, XmlMappingParser mappingParser) {
        super(constraintHelper, createBeanConfigurations(mappingParser));
        this.annotationProcessingOptions = mappingParser.getAnnotationProcessingOptions();
    }

    private static XmlMappingParser createMappingParser(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, Set<InputStream> mappingStreams, ClassLoader externalClassLoader) {
        XmlMappingParser mappingParser = new XmlMappingParser(constraintHelper, parameterNameProvider, externalClassLoader);
        mappingParser.parse(mappingStreams);
        return mappingParser;
    }

    private static Map<String, BeanConfiguration<?>> createBeanConfigurations(XmlMappingParser mappingParser) {
        Map<String, BeanConfiguration<?>> configuredBeans = new HashMap<>();
        for (Class<?> clazz : mappingParser.getXmlConfiguredClasses()) {
            Set<ConstrainedElement> constrainedElements = mappingParser.getConstrainedElementsForClass(clazz);
            BeanConfiguration<?> beanConfiguration = createBeanConfiguration(ConfigurationSource.XML, clazz, constrainedElements, mappingParser.getDefaultSequenceForClass(clazz), null);
            configuredBeans.put(clazz.getName(), beanConfiguration);
        }
        return configuredBeans;
    }

    @Override // org.hibernate.validator.internal.metadata.provider.MetaDataProvider
    public AnnotationProcessingOptions getAnnotationProcessingOptions() {
        return this.annotationProcessingOptions;
    }
}
