package org.hibernate.validator.internal.xml;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.validation.BootstrapConfiguration;
import javax.validation.executable.ExecutableType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.validation.Schema;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.hibernate.validator.internal.util.privilegedactions.NewJaxbContext;
import org.hibernate.validator.internal.util.privilegedactions.SetContextClassLoader;
import org.hibernate.validator.internal.util.privilegedactions.Unmarshal;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ValidationXmlParser.class */
public class ValidationXmlParser {
    private static final String VALIDATION_XML_FILE = "META-INF/validation.xml";
    private final ClassLoader externalClassLoader;
    private static final Log log = LoggerFactory.make();
    private static final ConcurrentMap<String, String> SCHEMAS_BY_VERSION = new ConcurrentHashMap(2, 0.75f, 1);

    static {
        SCHEMAS_BY_VERSION.put("1.0", "META-INF/validation-configuration-1.0.xsd");
        SCHEMAS_BY_VERSION.put("1.1", "META-INF/validation-configuration-1.1.xsd");
    }

    public ValidationXmlParser(ClassLoader externalClassLoader) {
        this.externalClassLoader = externalClassLoader;
    }

    public final BootstrapConfiguration parseValidationXml() throws IOException {
        InputStream inputStream = getValidationXmlInputStream();
        if (inputStream == null) {
            return BootstrapConfigurationImpl.getDefaultBootstrapConfiguration();
        }
        ClassLoader previousTccl = (ClassLoader) run(GetClassLoader.fromContext());
        try {
            run(SetContextClassLoader.action(ValidationXmlParser.class.getClassLoader()));
            XmlParserHelper xmlParserHelper = new XmlParserHelper();
            XMLEventReader xmlEventReader = xmlParserHelper.createXmlEventReader(VALIDATION_XML_FILE, inputStream);
            String schemaVersion = xmlParserHelper.getSchemaVersion(VALIDATION_XML_FILE, xmlEventReader);
            Schema schema = getSchema(xmlParserHelper, schemaVersion);
            ValidationConfigType validationConfig = unmarshal(xmlEventReader, schema);
            BootstrapConfiguration bootstrapConfigurationCreateBootstrapConfiguration = createBootstrapConfiguration(validationConfig);
            run(SetContextClassLoader.action(previousTccl));
            closeStream(inputStream);
            return bootstrapConfigurationCreateBootstrapConfiguration;
        } catch (Throwable th) {
            run(SetContextClassLoader.action(previousTccl));
            closeStream(inputStream);
            throw th;
        }
    }

    private InputStream getValidationXmlInputStream() {
        log.debugf("Trying to load %s for XML based Validator configuration.", VALIDATION_XML_FILE);
        InputStream inputStream = ResourceLoaderHelper.getResettableInputStreamForPath(VALIDATION_XML_FILE, this.externalClassLoader);
        if (inputStream != null) {
            return inputStream;
        }
        log.debugf("No %s found. Using annotation based configuration only.", VALIDATION_XML_FILE);
        return null;
    }

    private Schema getSchema(XmlParserHelper xmlParserHelper, String schemaVersion) {
        String schemaResource = SCHEMAS_BY_VERSION.get(schemaVersion);
        if (schemaResource == null) {
            throw log.getUnsupportedSchemaVersionException(VALIDATION_XML_FILE, schemaVersion);
        }
        return xmlParserHelper.getSchema(schemaResource);
    }

    private ValidationConfigType unmarshal(XMLEventReader xmlEventReader, Schema schema) {
        log.parsingXMLFile(VALIDATION_XML_FILE);
        try {
            JAXBContext jc = (JAXBContext) run(NewJaxbContext.action(ValidationConfigType.class));
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setSchema(schema);
            JAXBElement<ValidationConfigType> root = (JAXBElement) run(Unmarshal.action(unmarshaller, xmlEventReader, ValidationConfigType.class));
            return (ValidationConfigType) root.getValue();
        } catch (Exception e) {
            throw log.getUnableToParseValidationXmlFileException(VALIDATION_XML_FILE, e);
        }
    }

    private void closeStream(InputStream inputStream) throws IOException {
        try {
            inputStream.close();
        } catch (IOException e) {
            log.unableToCloseXMLFileInputStream(VALIDATION_XML_FILE);
        }
    }

    private BootstrapConfiguration createBootstrapConfiguration(ValidationConfigType config) {
        EnumSet<ExecutableType> validatedExecutableTypes;
        Map<String, String> properties = new HashMap<>();
        for (PropertyType property : config.getProperty()) {
            if (log.isDebugEnabled()) {
                log.debugf("Found property '%s' with value '%s' in validation.xml.", property.getName(), property.getValue());
            }
            properties.put(property.getName(), property.getValue());
        }
        ExecutableValidationType executableValidationType = config.getExecutableValidation();
        if (executableValidationType == null) {
            validatedExecutableTypes = getValidatedExecutableTypes(null);
        } else {
            validatedExecutableTypes = getValidatedExecutableTypes(executableValidationType.getDefaultValidatedExecutableTypes());
        }
        EnumSet<ExecutableType> defaultValidatedExecutableTypes = validatedExecutableTypes;
        boolean executableValidationEnabled = executableValidationType == null || executableValidationType.getEnabled().booleanValue();
        return new BootstrapConfigurationImpl(config.getDefaultProvider(), config.getConstraintValidatorFactory(), config.getMessageInterpolator(), config.getTraversableResolver(), config.getParameterNameProvider(), defaultValidatedExecutableTypes, executableValidationEnabled, new HashSet(config.getConstraintMapping()), properties);
    }

    private EnumSet<ExecutableType> getValidatedExecutableTypes(DefaultValidatedExecutableTypesType validatedExecutables) {
        if (validatedExecutables == null) {
            return null;
        }
        EnumSet<ExecutableType> executableTypes = EnumSet.noneOf(ExecutableType.class);
        executableTypes.addAll(validatedExecutables.getExecutableType());
        return executableTypes;
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }

    private <T> T run(PrivilegedExceptionAction<T> privilegedExceptionAction) throws Exception {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedExceptionAction) : privilegedExceptionAction.run();
    }
}
