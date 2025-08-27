package org.hibernate.validator.internal.engine.resolver;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.validation.Path;
import javax.validation.TraversableResolver;
import javax.validation.ValidationException;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/resolver/DefaultTraversableResolver.class */
public class DefaultTraversableResolver implements TraversableResolver {
    private static final Log log = LoggerFactory.make();
    private static final String PERSISTENCE_CLASS_NAME = "javax.persistence.Persistence";
    private static final String PERSISTENCE_UTIL_METHOD = "getPersistenceUtil";
    private static final String JPA_AWARE_TRAVERSABLE_RESOLVER_CLASS_NAME = "org.hibernate.validator.internal.engine.resolver.JPATraversableResolver";
    private TraversableResolver jpaTraversableResolver;

    public DefaultTraversableResolver() {
        detectJPA();
    }

    private void detectJPA() {
        try {
            Class<?> persistenceClass = (Class) run(LoadClass.action(PERSISTENCE_CLASS_NAME, getClass().getClassLoader()));
            Method persistenceUtilGetter = (Method) run(GetMethod.action(persistenceClass, PERSISTENCE_UTIL_METHOD));
            if (persistenceUtilGetter == null) {
                log.debugf("Found %s on classpath, but no method '%s'. Assuming JPA 1 environment. All properties will per default be traversable.", PERSISTENCE_CLASS_NAME, PERSISTENCE_UTIL_METHOD);
                return;
            }
            try {
                Object persistence = run(NewInstance.action(persistenceClass, "persistence provider"));
                ReflectionHelper.getValue(persistenceUtilGetter, persistence);
                log.debugf("Found %s on classpath containing '%s'. Assuming JPA 2 environment. Trying to instantiate JPA aware TraversableResolver", PERSISTENCE_CLASS_NAME, PERSISTENCE_UTIL_METHOD);
                try {
                    Class<? extends TraversableResolver> jpaAwareResolverClass = (Class) run(LoadClass.action(JPA_AWARE_TRAVERSABLE_RESOLVER_CLASS_NAME, getClass().getClassLoader()));
                    this.jpaTraversableResolver = (TraversableResolver) run(NewInstance.action(jpaAwareResolverClass, ""));
                    log.debugf("Instantiated JPA aware TraversableResolver of type %s.", JPA_AWARE_TRAVERSABLE_RESOLVER_CLASS_NAME);
                } catch (ValidationException e) {
                    log.debugf("Unable to load or instantiate JPA aware resolver %s. All properties will per default be traversable.", JPA_AWARE_TRAVERSABLE_RESOLVER_CLASS_NAME);
                }
            } catch (Exception e2) {
                log.debugf("Unable to invoke %s.%s. Inconsistent JPA environment. All properties will per default be traversable.", PERSISTENCE_CLASS_NAME, PERSISTENCE_UTIL_METHOD);
            }
        } catch (ValidationException e3) {
            log.debugf("Cannot find %s on classpath. Assuming non JPA 2 environment. All properties will per default be traversable.", PERSISTENCE_CLASS_NAME);
        }
    }

    @Override // javax.validation.TraversableResolver
    public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
        return this.jpaTraversableResolver == null || this.jpaTraversableResolver.isReachable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType);
    }

    @Override // javax.validation.TraversableResolver
    public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
        return this.jpaTraversableResolver == null || this.jpaTraversableResolver.isCascadable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType);
    }

    private <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
