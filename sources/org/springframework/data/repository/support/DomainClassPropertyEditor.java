package org.springframework.data.repository.support;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/DomainClassPropertyEditor.class */
public class DomainClassPropertyEditor<T, ID extends Serializable> extends PropertyEditorSupport {
    private final RepositoryInvoker invoker;
    private final EntityInformation<T, ID> information;
    private final PropertyEditorRegistry registry;

    public DomainClassPropertyEditor(RepositoryInvoker invoker, EntityInformation<T, ID> information, PropertyEditorRegistry registry) {
        Assert.notNull(invoker, "Invoker must not be null!");
        Assert.notNull(information, "Information must not be null!");
        Assert.notNull(registry, "Registry must not be null!");
        this.invoker = invoker;
        this.information = information;
        this.registry = registry;
    }

    public void setAsText(String idAsString) {
        if (!StringUtils.hasText(idAsString)) {
            setValue(null);
        } else {
            setValue(this.invoker.invokeFindOne(getId(idAsString)));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String getAsText() {
        Object id;
        Object value = getValue();
        if (null == value || (id = getId((DomainClassPropertyEditor<T, ID>) value)) == null) {
            return null;
        }
        return id.toString();
    }

    private ID getId(T t) {
        return (ID) this.information.getId(t);
    }

    private ID getId(String idAsString) {
        Class<ID> idClass = this.information.getIdType();
        PropertyEditor idEditor = this.registry.findCustomEditor(idClass, null);
        if (idEditor != null) {
            idEditor.setAsText(idAsString);
            return (ID) idEditor.getValue();
        }
        return (ID) new SimpleTypeConverter().convertIfNecessary(idAsString, idClass);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DomainClassPropertyEditor<?, ?> that = (DomainClassPropertyEditor) obj;
        return this.invoker.equals(that.invoker) && this.registry.equals(that.registry) && this.information.equals(that.information);
    }

    public int hashCode() {
        int hashCode = 17 + (this.invoker.hashCode() * 32);
        return hashCode + (this.information.hashCode() * 32) + (this.registry.hashCode() * 32);
    }
}
