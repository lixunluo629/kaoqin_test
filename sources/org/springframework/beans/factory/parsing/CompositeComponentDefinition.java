package org.springframework.beans.factory.parsing;

import java.util.LinkedList;
import java.util.List;
import org.springframework.util.Assert;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/CompositeComponentDefinition.class */
public class CompositeComponentDefinition extends AbstractComponentDefinition {
    private final String name;
    private final Object source;
    private final List<ComponentDefinition> nestedComponents = new LinkedList();

    public CompositeComponentDefinition(String name, Object source) {
        Assert.notNull(name, "Name must not be null");
        this.name = name;
        this.source = source;
    }

    @Override // org.springframework.beans.factory.parsing.ComponentDefinition
    public String getName() {
        return this.name;
    }

    @Override // org.springframework.beans.BeanMetadataElement
    public Object getSource() {
        return this.source;
    }

    public void addNestedComponent(ComponentDefinition component) {
        Assert.notNull(component, "ComponentDefinition must not be null");
        this.nestedComponents.add(component);
    }

    public ComponentDefinition[] getNestedComponents() {
        return (ComponentDefinition[]) this.nestedComponents.toArray(new ComponentDefinition[this.nestedComponents.size()]);
    }
}
