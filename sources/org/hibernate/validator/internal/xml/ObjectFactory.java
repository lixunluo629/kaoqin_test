package org.hibernate.validator.internal.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

@XmlRegistry
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ObjectFactory.class */
public class ObjectFactory {
    private static final QName _ValidationConfig_QNAME = new QName("http://jboss.org/xml/ns/javax/validation/configuration", "validation-config");
    private static final QName _ConstraintMappings_QNAME = new QName("http://jboss.org/xml/ns/javax/validation/mapping", "constraint-mappings");
    private static final QName _ElementTypeValue_QNAME = new QName("http://jboss.org/xml/ns/javax/validation/mapping", "value");
    private static final QName _ElementTypeAnnotation_QNAME = new QName("http://jboss.org/xml/ns/javax/validation/mapping", JamXmlElements.ANNOTATION);

    public ValidationConfigType createValidationConfigType() {
        return new ValidationConfigType();
    }

    public ExecutableValidationType createExecutableValidationType() {
        return new ExecutableValidationType();
    }

    public DefaultValidatedExecutableTypesType createDefaultValidatedExecutableTypesType() {
        return new DefaultValidatedExecutableTypesType();
    }

    public PropertyType createPropertyType() {
        return new PropertyType();
    }

    public ConstraintMappingsType createConstraintMappingsType() {
        return new ConstraintMappingsType();
    }

    public PayloadType createPayloadType() {
        return new PayloadType();
    }

    public GroupsType createGroupsType() {
        return new GroupsType();
    }

    public GroupSequenceType createGroupSequenceType() {
        return new GroupSequenceType();
    }

    public GroupConversionType createGroupConversionType() {
        return new GroupConversionType();
    }

    public ValidatedByType createValidatedByType() {
        return new ValidatedByType();
    }

    public ConstraintType createConstraintType() {
        return new ConstraintType();
    }

    public ElementType createElementType() {
        return new ElementType();
    }

    public ClassType createClassType() {
        return new ClassType();
    }

    public BeanType createBeanType() {
        return new BeanType();
    }

    public AnnotationType createAnnotationType() {
        return new AnnotationType();
    }

    public GetterType createGetterType() {
        return new GetterType();
    }

    public MethodType createMethodType() {
        return new MethodType();
    }

    public ConstructorType createConstructorType() {
        return new ConstructorType();
    }

    public ParameterType createParameterType() {
        return new ParameterType();
    }

    public ReturnValueType createReturnValueType() {
        return new ReturnValueType();
    }

    public CrossParameterType createCrossParameterType() {
        return new CrossParameterType();
    }

    public ConstraintDefinitionType createConstraintDefinitionType() {
        return new ConstraintDefinitionType();
    }

    public FieldType createFieldType() {
        return new FieldType();
    }

    @XmlElementDecl(namespace = "http://jboss.org/xml/ns/javax/validation/configuration", name = "validation-config")
    public JAXBElement<ValidationConfigType> createValidationConfig(ValidationConfigType value) {
        return new JAXBElement<>(_ValidationConfig_QNAME, ValidationConfigType.class, (Class) null, value);
    }

    @XmlElementDecl(namespace = "http://jboss.org/xml/ns/javax/validation/mapping", name = "constraint-mappings")
    public JAXBElement<ConstraintMappingsType> createConstraintMappings(ConstraintMappingsType value) {
        return new JAXBElement<>(_ConstraintMappings_QNAME, ConstraintMappingsType.class, (Class) null, value);
    }

    @XmlElementDecl(namespace = "http://jboss.org/xml/ns/javax/validation/mapping", name = "value", scope = ElementType.class)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createElementTypeValue(String value) {
        return new JAXBElement<>(_ElementTypeValue_QNAME, String.class, ElementType.class, value);
    }

    @XmlElementDecl(namespace = "http://jboss.org/xml/ns/javax/validation/mapping", name = JamXmlElements.ANNOTATION, scope = ElementType.class)
    public JAXBElement<AnnotationType> createElementTypeAnnotation(AnnotationType value) {
        return new JAXBElement<>(_ElementTypeAnnotation_QNAME, AnnotationType.class, ElementType.class, value);
    }
}
