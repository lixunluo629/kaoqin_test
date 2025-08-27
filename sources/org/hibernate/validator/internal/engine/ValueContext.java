package org.hibernate.validator.internal.engine;

import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import javax.validation.ElementKind;
import javax.validation.groups.Default;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.aggregated.ParameterMetaData;
import org.hibernate.validator.internal.metadata.facets.Cascadable;
import org.hibernate.validator.internal.metadata.facets.Validatable;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/ValueContext.class */
public class ValueContext<T, V> {
    private final T currentBean;
    private final Class<T> currentBeanType;
    private PathImpl propertyPath;
    private Class<?> currentGroup;
    private V currentValue;
    private final Validatable currentValidatable;
    private ElementType elementType;
    private Type declaredTypeOfValidatedElement;
    private ValidatedValueUnwrapper<V> validatedValueHandler;
    private UnwrapMode unwrapMode = UnwrapMode.AUTOMATIC;

    public static <T, V> ValueContext<T, V> getLocalExecutionContext(T value, Validatable validatable, PathImpl propertyPath) {
        return new ValueContext<>(value, value.getClass(), validatable, propertyPath);
    }

    public static <T, V> ValueContext<T, V> getLocalExecutionContext(Class<T> type, Validatable validatable, PathImpl propertyPath) {
        return new ValueContext<>(null, type, validatable, propertyPath);
    }

    private ValueContext(T currentBean, Class<T> currentBeanType, Validatable validatable, PathImpl propertyPath) {
        this.currentBean = currentBean;
        this.currentBeanType = currentBeanType;
        this.currentValidatable = validatable;
        this.propertyPath = propertyPath;
    }

    public final PathImpl getPropertyPath() {
        return this.propertyPath;
    }

    public final Class<?> getCurrentGroup() {
        return this.currentGroup;
    }

    public final T getCurrentBean() {
        return this.currentBean;
    }

    public final Class<T> getCurrentBeanType() {
        return this.currentBeanType;
    }

    public Validatable getCurrentValidatable() {
        return this.currentValidatable;
    }

    public final Object getCurrentValidatedValue() {
        return this.validatedValueHandler != null ? this.validatedValueHandler.handleValidatedValue(this.currentValue) : this.currentValue;
    }

    public final void setPropertyPath(PathImpl propertyPath) {
        this.propertyPath = propertyPath;
    }

    public final void appendNode(Cascadable node) {
        this.propertyPath = PathImpl.createCopy(this.propertyPath);
        if (node.getKind() == ElementKind.PROPERTY) {
            this.propertyPath.addPropertyNode(node.getName());
        } else if (node.getKind() == ElementKind.PARAMETER) {
            this.propertyPath.addParameterNode(node.getName(), ((ParameterMetaData) node).getIndex());
        } else if (node.getKind() == ElementKind.RETURN_VALUE) {
            this.propertyPath.addReturnValueNode();
        }
    }

    public final void appendCollectionElementNode() {
        this.propertyPath = PathImpl.createCopy(this.propertyPath);
        this.propertyPath.addCollectionElementNode();
    }

    public final void appendBeanNode() {
        this.propertyPath = PathImpl.createCopy(this.propertyPath);
        this.propertyPath.addBeanNode();
    }

    public final void appendCrossParameterNode() {
        this.propertyPath = PathImpl.createCopy(this.propertyPath);
        this.propertyPath.addCrossParameterNode();
    }

    public final void markCurrentPropertyAsIterable() {
        this.propertyPath.makeLeafNodeIterable();
    }

    public final void setKey(Object key) {
        this.propertyPath.setLeafNodeMapKey(key);
    }

    public final void setIndex(Integer index) {
        this.propertyPath.setLeafNodeIndex(index);
    }

    public final void setCurrentGroup(Class<?> currentGroup) {
        this.currentGroup = currentGroup;
    }

    public final void setCurrentValidatedValue(V currentValue) {
        this.propertyPath.setLeafNodeValue(currentValue);
        this.currentValue = currentValue;
    }

    public final boolean validatingDefault() {
        return getCurrentGroup() != null && getCurrentGroup().getName().equals(Default.class.getName());
    }

    public final ElementType getElementType() {
        return this.elementType;
    }

    public final void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public final Type getDeclaredTypeOfValidatedElement() {
        return this.declaredTypeOfValidatedElement;
    }

    public final void setDeclaredTypeOfValidatedElement(Type declaredTypeOfValidatedElement) {
        this.declaredTypeOfValidatedElement = declaredTypeOfValidatedElement;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ValueContext");
        sb.append("{currentBean=").append(this.currentBean);
        sb.append(", currentBeanType=").append(this.currentBeanType);
        sb.append(", propertyPath=").append(this.propertyPath);
        sb.append(", currentGroup=").append(this.currentGroup);
        sb.append(", currentValue=").append(this.currentValue);
        sb.append(", elementType=").append(this.elementType);
        sb.append(", typeOfValidatedValue=").append(this.declaredTypeOfValidatedElement);
        sb.append('}');
        return sb.toString();
    }

    public void setValidatedValueHandler(ValidatedValueUnwrapper<V> handler) {
        this.validatedValueHandler = handler;
    }

    public ValidatedValueUnwrapper<V> getValidatedValueHandler() {
        return this.validatedValueHandler;
    }

    public UnwrapMode getUnwrapMode() {
        return this.unwrapMode;
    }

    public void setUnwrapMode(UnwrapMode unwrapMode) {
        this.unwrapMode = unwrapMode;
    }
}
