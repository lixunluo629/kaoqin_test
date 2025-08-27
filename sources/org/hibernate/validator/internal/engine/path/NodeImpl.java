package org.hibernate.validator.internal.engine.path;

import com.mysql.jdbc.MysqlErrorNumbers;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.validation.ElementKind;
import javax.validation.Path;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.path.PropertyNode;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/path/NodeImpl.class */
public class NodeImpl implements Path.PropertyNode, Path.MethodNode, Path.ConstructorNode, Path.BeanNode, Path.ParameterNode, Path.ReturnValueNode, Path.CrossParameterNode, PropertyNode, Serializable {
    private static final long serialVersionUID = 2075466571633860499L;
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    private static final Log log = LoggerFactory.make();
    private static final String INDEX_OPEN = "[";
    private static final String INDEX_CLOSE = "]";
    private static final String RETURN_VALUE_NODE_NAME = "<return value>";
    private static final String CROSS_PARAMETER_NODE_NAME = "<cross-parameter>";
    private static final String COLLECTION_ELEMENT_NODE_NAME = "<collection element>";
    private final String name;
    private final NodeImpl parent;
    private final boolean isIterable;
    private final Integer index;
    private final Object key;
    private final ElementKind kind;
    private final int hashCode = buildHashCode();
    private final Class<?>[] parameterTypes;
    private final Integer parameterIndex;
    private final Object value;
    private String asString;

    private NodeImpl(String name, NodeImpl parent, boolean indexable, Integer index, Object key, ElementKind kind, Class<?>[] parameterTypes, Integer parameterIndex, Object value) {
        this.name = name;
        this.parent = parent;
        this.index = index;
        this.key = key;
        this.value = value;
        this.isIterable = indexable;
        this.kind = kind;
        this.parameterTypes = parameterTypes;
        this.parameterIndex = parameterIndex;
    }

    public static NodeImpl createPropertyNode(String name, NodeImpl parent) {
        return new NodeImpl(name, parent, false, null, null, ElementKind.PROPERTY, EMPTY_CLASS_ARRAY, null, null);
    }

    public static NodeImpl createCollectionElementNode(NodeImpl parent) {
        return new NodeImpl(COLLECTION_ELEMENT_NODE_NAME, parent, false, null, null, ElementKind.PROPERTY, EMPTY_CLASS_ARRAY, null, null);
    }

    public static NodeImpl createParameterNode(String name, NodeImpl parent, int parameterIndex) {
        return new NodeImpl(name, parent, false, null, null, ElementKind.PARAMETER, EMPTY_CLASS_ARRAY, Integer.valueOf(parameterIndex), null);
    }

    public static NodeImpl createCrossParameterNode(NodeImpl parent) {
        return new NodeImpl(CROSS_PARAMETER_NODE_NAME, parent, false, null, null, ElementKind.CROSS_PARAMETER, EMPTY_CLASS_ARRAY, null, null);
    }

    public static NodeImpl createMethodNode(String name, NodeImpl parent, Class<?>[] parameterTypes) {
        return new NodeImpl(name, parent, false, null, null, ElementKind.METHOD, parameterTypes, null, null);
    }

    public static NodeImpl createConstructorNode(String name, NodeImpl parent, Class<?>[] parameterTypes) {
        return new NodeImpl(name, parent, false, null, null, ElementKind.CONSTRUCTOR, parameterTypes, null, null);
    }

    public static NodeImpl createBeanNode(NodeImpl parent) {
        return new NodeImpl(null, parent, false, null, null, ElementKind.BEAN, EMPTY_CLASS_ARRAY, null, null);
    }

    public static NodeImpl createReturnValue(NodeImpl parent) {
        return new NodeImpl(RETURN_VALUE_NODE_NAME, parent, false, null, null, ElementKind.RETURN_VALUE, EMPTY_CLASS_ARRAY, null, null);
    }

    public static NodeImpl makeIterable(NodeImpl node) {
        return new NodeImpl(node.name, node.parent, true, null, null, node.kind, node.parameterTypes, node.parameterIndex, node.value);
    }

    public static NodeImpl setIndex(NodeImpl node, Integer index) {
        return new NodeImpl(node.name, node.parent, true, index, null, node.kind, node.parameterTypes, node.parameterIndex, node.value);
    }

    public static NodeImpl setMapKey(NodeImpl node, Object key) {
        return new NodeImpl(node.name, node.parent, true, null, key, node.kind, node.parameterTypes, node.parameterIndex, node.value);
    }

    public static NodeImpl setPropertyValue(NodeImpl node, Object value) {
        return new NodeImpl(node.name, node.parent, node.isIterable, node.index, node.key, node.kind, node.parameterTypes, node.parameterIndex, value);
    }

    @Override // javax.validation.Path.Node
    public final String getName() {
        return this.name;
    }

    @Override // javax.validation.Path.Node
    public final boolean isInIterable() {
        return this.parent != null && this.parent.isIterable();
    }

    public final boolean isIterable() {
        return this.isIterable;
    }

    @Override // javax.validation.Path.Node
    public final Integer getIndex() {
        if (this.parent == null) {
            return null;
        }
        return this.parent.index;
    }

    @Override // javax.validation.Path.Node
    public final Object getKey() {
        if (this.parent == null) {
            return null;
        }
        return this.parent.key;
    }

    public final NodeImpl getParent() {
        return this.parent;
    }

    @Override // javax.validation.Path.Node
    public ElementKind getKind() {
        return this.kind;
    }

    @Override // javax.validation.Path.Node
    public <T extends Path.Node> T as(Class<T> nodeType) {
        if ((this.kind == ElementKind.BEAN && nodeType == Path.BeanNode.class) || ((this.kind == ElementKind.CONSTRUCTOR && nodeType == Path.ConstructorNode.class) || ((this.kind == ElementKind.CROSS_PARAMETER && nodeType == Path.CrossParameterNode.class) || ((this.kind == ElementKind.METHOD && nodeType == Path.MethodNode.class) || ((this.kind == ElementKind.PARAMETER && nodeType == Path.ParameterNode.class) || ((this.kind == ElementKind.PROPERTY && (nodeType == Path.PropertyNode.class || nodeType == PropertyNode.class)) || (this.kind == ElementKind.RETURN_VALUE && nodeType == Path.ReturnValueNode.class))))))) {
            return nodeType.cast(this);
        }
        throw log.getUnableToNarrowNodeTypeException(getClass().getName(), this.kind, nodeType.getName());
    }

    @Override // javax.validation.Path.MethodNode, javax.validation.Path.ConstructorNode
    public List<Class<?>> getParameterTypes() {
        return Arrays.asList(this.parameterTypes);
    }

    @Override // javax.validation.Path.ParameterNode
    public int getParameterIndex() {
        Contracts.assertTrue(this.kind == ElementKind.PARAMETER, "getParameterIndex() may only be invoked for nodes of ElementKind.PARAMETER.");
        return this.parameterIndex.intValue();
    }

    @Override // org.hibernate.validator.path.PropertyNode
    public Object getValue() {
        return this.value;
    }

    public String toString() {
        return asString();
    }

    public final String asString() {
        if (this.asString == null) {
            this.asString = buildToString();
        }
        return this.asString;
    }

    private String buildToString() {
        StringBuilder builder = new StringBuilder();
        if (ElementKind.BEAN.equals(getKind())) {
            builder.append("");
        } else {
            builder.append(getName());
        }
        if (isIterable()) {
            builder.append("[");
            if (this.index != null) {
                builder.append(this.index);
            } else if (this.key != null) {
                builder.append(this.key);
            }
            builder.append("]");
        }
        return builder.toString();
    }

    public final int buildHashCode() {
        int result = (31 * 1) + (this.index == null ? 0 : this.index.hashCode());
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.isIterable ? MysqlErrorNumbers.ER_WRONG_VALUE_FOR_VAR : MysqlErrorNumbers.ER_SLAVE_IGNORED_TABLE))) + (this.key == null ? 0 : this.key.hashCode()))) + (this.kind == null ? 0 : this.kind.hashCode()))) + (this.name == null ? 0 : this.name.hashCode()))) + (this.parameterIndex == null ? 0 : this.parameterIndex.hashCode()))) + (this.parameterTypes == null ? 0 : this.parameterTypes.hashCode()))) + (this.parent == null ? 0 : this.parent.hashCode());
    }

    public int hashCode() {
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NodeImpl other = (NodeImpl) obj;
        if (this.index == null) {
            if (other.index != null) {
                return false;
            }
        } else if (!this.index.equals(other.index)) {
            return false;
        }
        if (this.isIterable != other.isIterable) {
            return false;
        }
        if (this.key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!this.key.equals(other.key)) {
            return false;
        }
        if (this.kind != other.kind) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.parameterIndex == null) {
            if (other.parameterIndex != null) {
                return false;
            }
        } else if (!this.parameterIndex.equals(other.parameterIndex)) {
            return false;
        }
        if (this.parameterTypes == null) {
            if (other.parameterTypes != null) {
                return false;
            }
        } else if (!this.parameterTypes.equals(other.parameterTypes)) {
            return false;
        }
        if (this.parent == null) {
            if (other.parent != null) {
                return false;
            }
            return true;
        }
        if (!this.parent.equals(other.parent)) {
            return false;
        }
        return true;
    }
}
