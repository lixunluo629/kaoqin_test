package javax.validation;

import java.lang.annotation.ElementType;
import javax.validation.Path;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/TraversableResolver.class */
public interface TraversableResolver {
    boolean isReachable(Object obj, Path.Node node, Class<?> cls, Path path, ElementType elementType);

    boolean isCascadable(Object obj, Path.Node node, Class<?> cls, Path path, ElementType elementType);
}
