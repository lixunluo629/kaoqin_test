package org.hibernate.validator.internal.engine.resolver;

import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Path;
import javax.validation.TraversableResolver;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/resolver/CachingTraversableResolverForSingleValidation.class */
public class CachingTraversableResolverForSingleValidation implements TraversableResolver {
    private final TraversableResolver delegate;
    private final Map<TraversableHolder, TraversableHolder> traversables = new HashMap();

    public CachingTraversableResolverForSingleValidation(TraversableResolver delegate) {
        this.delegate = delegate;
    }

    @Override // javax.validation.TraversableResolver
    public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
        TraversableHolder currentLH = new TraversableHolder(traversableObject, traversableProperty);
        TraversableHolder cachedLH = this.traversables.get(currentLH);
        if (cachedLH == null) {
            currentLH.isReachable = Boolean.valueOf(this.delegate.isReachable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType));
            this.traversables.put(currentLH, currentLH);
            cachedLH = currentLH;
        } else if (cachedLH.isReachable == null) {
            cachedLH.isReachable = Boolean.valueOf(this.delegate.isReachable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType));
        }
        return cachedLH.isReachable.booleanValue();
    }

    @Override // javax.validation.TraversableResolver
    public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
        TraversableHolder currentLH = new TraversableHolder(traversableObject, traversableProperty);
        TraversableHolder cachedLH = this.traversables.get(currentLH);
        if (cachedLH == null) {
            currentLH.isCascadable = Boolean.valueOf(this.delegate.isCascadable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType));
            this.traversables.put(currentLH, currentLH);
            cachedLH = currentLH;
        } else if (cachedLH.isCascadable == null) {
            cachedLH.isCascadable = Boolean.valueOf(this.delegate.isCascadable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType));
        }
        return cachedLH.isCascadable.booleanValue();
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/resolver/CachingTraversableResolverForSingleValidation$TraversableHolder.class */
    private static final class TraversableHolder {
        private final Object traversableObject;
        private final Path.Node traversableProperty;
        private final int hashCode;
        private Boolean isReachable;
        private Boolean isCascadable;

        private TraversableHolder(Object traversableObject, Path.Node traversableProperty) {
            this.traversableObject = traversableObject;
            this.traversableProperty = traversableProperty;
            this.hashCode = buildHashCode();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TraversableHolder that = (TraversableHolder) o;
            if (this.traversableObject != null) {
                if (!this.traversableObject.equals(that.traversableObject)) {
                    return false;
                }
            } else if (that.traversableObject != null) {
                return false;
            }
            if (!this.traversableProperty.equals(that.traversableProperty)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.hashCode;
        }

        public int buildHashCode() {
            int result = this.traversableObject != null ? System.identityHashCode(this.traversableObject) : 0;
            return (31 * result) + this.traversableProperty.hashCode();
        }
    }
}
