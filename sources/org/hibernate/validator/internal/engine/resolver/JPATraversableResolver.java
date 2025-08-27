package org.hibernate.validator.internal.engine.resolver;

import java.lang.annotation.ElementType;
import javax.persistence.Persistence;
import javax.validation.Path;
import javax.validation.TraversableResolver;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/resolver/JPATraversableResolver.class */
public class JPATraversableResolver implements TraversableResolver {
    private static final Log log = LoggerFactory.make();

    @Override // javax.validation.TraversableResolver
    public final boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
        if (log.isTraceEnabled()) {
            log.tracef("Calling isReachable on object %s with node name %s.", traversableObject, traversableProperty.getName());
        }
        if (traversableObject == null) {
            return true;
        }
        return Persistence.getPersistenceUtil().isLoaded(traversableObject, traversableProperty.getName());
    }

    @Override // javax.validation.TraversableResolver
    public final boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
        return true;
    }
}
