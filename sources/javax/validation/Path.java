package javax.validation;

import java.util.List;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/Path.class */
public interface Path extends Iterable<Node> {

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/Path$BeanNode.class */
    public interface BeanNode extends Node {
    }

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/Path$ConstructorNode.class */
    public interface ConstructorNode extends Node {
        List<Class<?>> getParameterTypes();
    }

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/Path$CrossParameterNode.class */
    public interface CrossParameterNode extends Node {
    }

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/Path$MethodNode.class */
    public interface MethodNode extends Node {
        List<Class<?>> getParameterTypes();
    }

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/Path$Node.class */
    public interface Node {
        String getName();

        boolean isInIterable();

        Integer getIndex();

        Object getKey();

        ElementKind getKind();

        <T extends Node> T as(Class<T> cls);
    }

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/Path$ParameterNode.class */
    public interface ParameterNode extends Node {
        int getParameterIndex();
    }

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/Path$PropertyNode.class */
    public interface PropertyNode extends Node {
    }

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/Path$ReturnValueNode.class */
    public interface ReturnValueNode extends Node {
    }
}
