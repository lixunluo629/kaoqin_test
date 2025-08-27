package org.hibernate.validator.internal.xml;

import java.util.ArrayList;
import java.util.List;
import javax.validation.executable.ExecutableType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "default-validated-executable-typesType", namespace = "http://jboss.org/xml/ns/javax/validation/configuration", propOrder = {"executableType"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/DefaultValidatedExecutableTypesType.class */
public class DefaultValidatedExecutableTypesType {

    @XmlElement(name = "executable-type", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    protected List<ExecutableType> executableType;

    public List<ExecutableType> getExecutableType() {
        if (this.executableType == null) {
            this.executableType = new ArrayList();
        }
        return this.executableType;
    }
}
