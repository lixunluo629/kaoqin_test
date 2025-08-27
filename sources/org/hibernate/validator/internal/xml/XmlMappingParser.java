package org.hibernate.validator.internal.xml;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.validation.ConstraintValidator;
import javax.validation.ParameterNameProvider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.validation.Schema;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.hibernate.validator.internal.util.privilegedactions.NewJaxbContext;
import org.hibernate.validator.internal.util.privilegedactions.SetContextClassLoader;
import org.hibernate.validator.internal.util.privilegedactions.Unmarshal;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/XmlMappingParser.class */
public class XmlMappingParser {
    private final ConstraintHelper constraintHelper;
    private final ParameterNameProvider parameterNameProvider;
    private final ClassLoadingHelper classLoadingHelper;
    private static final Log log = LoggerFactory.make();
    private static final ConcurrentMap<String, String> SCHEMAS_BY_VERSION = new ConcurrentHashMap(2, 0.75f, 1);
    private final Set<Class<?>> processedClasses = CollectionHelper.newHashSet();
    private final AnnotationProcessingOptionsImpl annotationProcessingOptions = new AnnotationProcessingOptionsImpl();
    private final Map<Class<?>, List<Class<?>>> defaultSequences = CollectionHelper.newHashMap();
    private final Map<Class<?>, Set<ConstrainedElement>> constrainedElements = CollectionHelper.newHashMap();
    private final XmlParserHelper xmlParserHelper = new XmlParserHelper();

    static {
        SCHEMAS_BY_VERSION.put("1.0", "META-INF/validation-mapping-1.0.xsd");
        SCHEMAS_BY_VERSION.put("1.1", "META-INF/validation-mapping-1.1.xsd");
    }

    public XmlMappingParser(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, ClassLoader externalClassLoader) {
        this.constraintHelper = constraintHelper;
        this.parameterNameProvider = parameterNameProvider;
        this.classLoadingHelper = new ClassLoadingHelper(externalClassLoader);
    }

    public final void parse(Set<InputStream> mappingStreams) {
        try {
            JAXBContext jc = (JAXBContext) run(NewJaxbContext.action(ConstraintMappingsType.class));
            MetaConstraintBuilder metaConstraintBuilder = new MetaConstraintBuilder(this.classLoadingHelper, this.constraintHelper);
            GroupConversionBuilder groupConversionBuilder = new GroupConversionBuilder(this.classLoadingHelper);
            ConstrainedTypeBuilder constrainedTypeBuilder = new ConstrainedTypeBuilder(this.classLoadingHelper, metaConstraintBuilder, this.annotationProcessingOptions, this.defaultSequences);
            ConstrainedFieldBuilder constrainedFieldBuilder = new ConstrainedFieldBuilder(metaConstraintBuilder, groupConversionBuilder, this.annotationProcessingOptions);
            ConstrainedExecutableBuilder constrainedExecutableBuilder = new ConstrainedExecutableBuilder(this.classLoadingHelper, this.parameterNameProvider, metaConstraintBuilder, groupConversionBuilder, this.annotationProcessingOptions);
            ConstrainedGetterBuilder constrainedGetterBuilder = new ConstrainedGetterBuilder(metaConstraintBuilder, groupConversionBuilder, this.annotationProcessingOptions);
            Set<String> alreadyProcessedConstraintDefinitions = CollectionHelper.newHashSet();
            for (InputStream in : mappingStreams) {
                boolean markSupported = in.markSupported();
                if (markSupported) {
                    in.mark(Integer.MAX_VALUE);
                }
                ConstraintMappingsType mapping = unmarshal(jc, in);
                String defaultPackage = mapping.getDefaultPackage();
                parseConstraintDefinitions(mapping.getConstraintDefinition(), defaultPackage, alreadyProcessedConstraintDefinitions);
                for (BeanType bean : mapping.getBean()) {
                    processBeanType(constrainedTypeBuilder, constrainedFieldBuilder, constrainedExecutableBuilder, constrainedGetterBuilder, defaultPackage, bean);
                }
                if (markSupported) {
                    try {
                        in.reset();
                    } catch (IOException e) {
                        log.debug("Unable to reset input stream.");
                    }
                }
            }
        } catch (JAXBException e2) {
            throw log.getErrorParsingMappingFileException(e2);
        }
    }

    private ConstraintMappingsType unmarshal(JAXBContext jc, InputStream in) throws JAXBException {
        ClassLoader previousTccl = (ClassLoader) run(GetClassLoader.fromContext());
        try {
            run(SetContextClassLoader.action(XmlMappingParser.class.getClassLoader()));
            XMLEventReader xmlEventReader = this.xmlParserHelper.createXmlEventReader("constraint mapping file", new CloseIgnoringInputStream(in));
            String schemaVersion = this.xmlParserHelper.getSchemaVersion("constraint mapping file", xmlEventReader);
            String schemaResourceName = getSchemaResourceName(schemaVersion);
            Schema schema = this.xmlParserHelper.getSchema(schemaResourceName);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setSchema(schema);
            ConstraintMappingsType validationConfig = getValidationConfig(xmlEventReader, unmarshaller);
            run(SetContextClassLoader.action(previousTccl));
            return validationConfig;
        } catch (Throwable th) {
            run(SetContextClassLoader.action(previousTccl));
            throw th;
        }
    }

    public final Set<Class<?>> getXmlConfiguredClasses() {
        return this.processedClasses;
    }

    public final AnnotationProcessingOptions getAnnotationProcessingOptions() {
        return this.annotationProcessingOptions;
    }

    public final Set<ConstrainedElement> getConstrainedElementsForClass(Class<?> beanClass) {
        if (this.constrainedElements.containsKey(beanClass)) {
            return this.constrainedElements.get(beanClass);
        }
        return Collections.emptySet();
    }

    public final List<Class<?>> getDefaultSequenceForClass(Class<?> beanClass) {
        return this.defaultSequences.get(beanClass);
    }

    private void processBeanType(ConstrainedTypeBuilder constrainedTypeBuilder, ConstrainedFieldBuilder constrainedFieldBuilder, ConstrainedExecutableBuilder constrainedExecutableBuilder, ConstrainedGetterBuilder constrainedGetterBuilder, String defaultPackage, BeanType bean) {
        Class<?> beanClass = this.classLoadingHelper.loadClass(bean.getClazz(), defaultPackage);
        checkClassHasNotBeenProcessed(this.processedClasses, beanClass);
        this.annotationProcessingOptions.ignoreAnnotationConstraintForClass(beanClass, bean.getIgnoreAnnotations());
        ConstrainedType constrainedType = constrainedTypeBuilder.buildConstrainedType(bean.getClassType(), beanClass, defaultPackage);
        if (constrainedType != null) {
            addConstrainedElement(beanClass, constrainedType);
        }
        Set<ConstrainedField> constrainedFields = constrainedFieldBuilder.buildConstrainedFields(bean.getField(), beanClass, defaultPackage);
        addConstrainedElements(beanClass, constrainedFields);
        Set<ConstrainedExecutable> constrainedGetters = constrainedGetterBuilder.buildConstrainedGetters(bean.getGetter(), beanClass, defaultPackage);
        addConstrainedElements(beanClass, constrainedGetters);
        Set<ConstrainedExecutable> constrainedConstructors = constrainedExecutableBuilder.buildConstructorConstrainedExecutable(bean.getConstructor(), beanClass, defaultPackage);
        addConstrainedElements(beanClass, constrainedConstructors);
        Set<ConstrainedExecutable> constrainedMethods = constrainedExecutableBuilder.buildMethodConstrainedExecutable(bean.getMethod(), beanClass, defaultPackage);
        addConstrainedElements(beanClass, constrainedMethods);
        this.processedClasses.add(beanClass);
    }

    private void parseConstraintDefinitions(List<ConstraintDefinitionType> constraintDefinitionList, String defaultPackage, Set<String> alreadyProcessedConstraintDefinitions) {
        for (ConstraintDefinitionType constraintDefinition : constraintDefinitionList) {
            String annotationClassName = constraintDefinition.getAnnotation();
            if (alreadyProcessedConstraintDefinitions.contains(annotationClassName)) {
                throw log.getOverridingConstraintDefinitionsInMultipleMappingFilesException(annotationClassName);
            }
            alreadyProcessedConstraintDefinitions.add(annotationClassName);
            Class<?> clazz = this.classLoadingHelper.loadClass(annotationClassName, defaultPackage);
            if (!clazz.isAnnotation()) {
                throw log.getIsNotAnAnnotationException(annotationClassName);
            }
            addValidatorDefinitions(clazz, defaultPackage, constraintDefinition.getValidatedBy());
        }
    }

    private <A extends Annotation> void addValidatorDefinitions(Class<A> annotationClass, String defaultPackage, ValidatedByType validatedByType) {
        ArrayList arrayListNewArrayList = CollectionHelper.newArrayList();
        for (String validatorClassName : validatedByType.getValue()) {
            Class<?> clsLoadClass = this.classLoadingHelper.loadClass(validatorClassName, defaultPackage);
            if (!ConstraintValidator.class.isAssignableFrom(clsLoadClass)) {
                throw log.getIsNotAConstraintValidatorClassException(clsLoadClass);
            }
            arrayListNewArrayList.add(clsLoadClass);
        }
        this.constraintHelper.putValidatorClasses(annotationClass, arrayListNewArrayList, Boolean.TRUE.equals(validatedByType.getIncludeExistingValidators()));
    }

    private void checkClassHasNotBeenProcessed(Set<Class<?>> processedClasses, Class<?> beanClass) {
        if (processedClasses.contains(beanClass)) {
            throw log.getBeanClassHasAlreadyBeConfiguredInXmlException(beanClass.getName());
        }
    }

    private void addConstrainedElement(Class<?> beanClass, ConstrainedElement constrainedElement) {
        if (this.constrainedElements.containsKey(beanClass)) {
            this.constrainedElements.get(beanClass).add(constrainedElement);
            return;
        }
        Set<ConstrainedElement> tmpList = CollectionHelper.newHashSet();
        tmpList.add(constrainedElement);
        this.constrainedElements.put(beanClass, tmpList);
    }

    private void addConstrainedElements(Class<?> beanClass, Set<? extends ConstrainedElement> newConstrainedElements) {
        if (this.constrainedElements.containsKey(beanClass)) {
            Set<ConstrainedElement> existingConstrainedElements = this.constrainedElements.get(beanClass);
            for (ConstrainedElement constrainedElement : newConstrainedElements) {
                for (ConstrainedElement existingConstrainedElement : existingConstrainedElements) {
                    if (existingConstrainedElement.getLocation().getMember() != null && existingConstrainedElement.getLocation().getMember().equals(constrainedElement.getLocation().getMember())) {
                        ConstraintLocation location = constrainedElement.getLocation();
                        throw log.getConstrainedElementConfiguredMultipleTimesException(location.getMember().toString());
                    }
                }
                existingConstrainedElements.add(constrainedElement);
            }
            return;
        }
        Set<ConstrainedElement> tmpSet = CollectionHelper.newHashSet();
        tmpSet.addAll(newConstrainedElements);
        this.constrainedElements.put(beanClass, tmpSet);
    }

    private ConstraintMappingsType getValidationConfig(XMLEventReader xmlEventReader, Unmarshaller unmarshaller) {
        try {
            JAXBElement<ConstraintMappingsType> root = (JAXBElement) run(Unmarshal.action(unmarshaller, xmlEventReader, ConstraintMappingsType.class));
            ConstraintMappingsType constraintMappings = (ConstraintMappingsType) root.getValue();
            return constraintMappings;
        } catch (Exception e) {
            throw log.getErrorParsingMappingFileException(e);
        }
    }

    private String getSchemaResourceName(String schemaVersion) {
        String schemaResource = SCHEMAS_BY_VERSION.get(schemaVersion);
        if (schemaResource == null) {
            throw log.getUnsupportedSchemaVersionException("constraint mapping file", schemaVersion);
        }
        return schemaResource;
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.xml.bind.JAXBException */
    private <T> T run(PrivilegedExceptionAction<T> privilegedExceptionAction) throws JAXBException {
        try {
            return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedExceptionAction) : privilegedExceptionAction.run();
        } catch (JAXBException e) {
            throw e;
        } catch (Exception e2) {
            throw log.getErrorParsingMappingFileException(e2);
        }
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/XmlMappingParser$CloseIgnoringInputStream.class */
    private static class CloseIgnoringInputStream extends FilterInputStream {
        public CloseIgnoringInputStream(InputStream in) {
            super(in);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }
    }
}
