package org.hibernate.validator.internal.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validation-configType", namespace = "http://jboss.org/xml/ns/javax/validation/configuration", propOrder = {"defaultProvider", "messageInterpolator", "traversableResolver", "constraintValidatorFactory", "parameterNameProvider", "executableValidation", "constraintMapping", BeanDefinitionParserDelegate.PROPERTY_ELEMENT})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ValidationConfigType.class */
public class ValidationConfigType {

    @XmlElement(name = "default-provider")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String defaultProvider;

    @XmlElement(name = "message-interpolator")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String messageInterpolator;

    @XmlElement(name = "traversable-resolver")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String traversableResolver;

    @XmlElement(name = "constraint-validator-factory")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String constraintValidatorFactory;

    @XmlElement(name = "parameter-name-provider")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String parameterNameProvider;

    @XmlElement(name = "executable-validation")
    protected ExecutableValidationType executableValidation;

    @XmlElement(name = "constraint-mapping")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected List<String> constraintMapping;
    protected List<PropertyType> property;

    @XmlAttribute(name = "version", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String version;

    public String getDefaultProvider() {
        return this.defaultProvider;
    }

    public void setDefaultProvider(String value) {
        this.defaultProvider = value;
    }

    public String getMessageInterpolator() {
        return this.messageInterpolator;
    }

    public void setMessageInterpolator(String value) {
        this.messageInterpolator = value;
    }

    public String getTraversableResolver() {
        return this.traversableResolver;
    }

    public void setTraversableResolver(String value) {
        this.traversableResolver = value;
    }

    public String getConstraintValidatorFactory() {
        return this.constraintValidatorFactory;
    }

    public void setConstraintValidatorFactory(String value) {
        this.constraintValidatorFactory = value;
    }

    public String getParameterNameProvider() {
        return this.parameterNameProvider;
    }

    public void setParameterNameProvider(String value) {
        this.parameterNameProvider = value;
    }

    public ExecutableValidationType getExecutableValidation() {
        return this.executableValidation;
    }

    public void setExecutableValidation(ExecutableValidationType value) {
        this.executableValidation = value;
    }

    public List<String> getConstraintMapping() {
        if (this.constraintMapping == null) {
            this.constraintMapping = new ArrayList();
        }
        return this.constraintMapping;
    }

    public List<PropertyType> getProperty() {
        if (this.property == null) {
            this.property = new ArrayList();
        }
        return this.property;
    }

    public String getVersion() {
        if (this.version == null) {
            return "1.1";
        }
        return this.version;
    }

    public void setVersion(String value) {
        this.version = value;
    }
}
