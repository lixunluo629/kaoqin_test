package org.springframework.web.servlet.mvc.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/support/AbstractControllerUrlHandlerMapping.class */
public abstract class AbstractControllerUrlHandlerMapping extends AbstractDetectingUrlHandlerMapping {
    private ControllerTypePredicate predicate = new AnnotationControllerTypePredicate();
    private Set<String> excludedPackages = Collections.singleton("org.springframework.web.servlet.mvc");
    private Set<Class<?>> excludedClasses = Collections.emptySet();

    protected abstract String[] buildUrlsForHandler(String str, Class<?> cls);

    public void setIncludeAnnotatedControllers(boolean includeAnnotatedControllers) {
        this.predicate = includeAnnotatedControllers ? new AnnotationControllerTypePredicate() : new ControllerTypePredicate();
    }

    public void setExcludedPackages(String... excludedPackages) {
        this.excludedPackages = excludedPackages != null ? new HashSet(Arrays.asList(excludedPackages)) : new HashSet();
    }

    public void setExcludedClasses(Class<?>... excludedClasses) {
        this.excludedClasses = excludedClasses != null ? new HashSet(Arrays.asList(excludedClasses)) : new HashSet();
    }

    @Override // org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping
    protected String[] determineUrlsForHandler(String beanName) {
        Class<?> beanClass = getApplicationContext().getType(beanName);
        if (isEligibleForMapping(beanName, beanClass)) {
            return buildUrlsForHandler(beanName, beanClass);
        }
        return null;
    }

    protected boolean isEligibleForMapping(String beanName, Class<?> beanClass) {
        if (beanClass == null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Excluding controller bean '" + beanName + "' from class name mapping because its bean type could not be determined");
                return false;
            }
            return false;
        }
        if (this.excludedClasses.contains(beanClass)) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Excluding controller bean '" + beanName + "' from class name mapping because its bean class is explicitly excluded: " + beanClass.getName());
                return false;
            }
            return false;
        }
        String beanClassName = beanClass.getName();
        for (String packageName : this.excludedPackages) {
            if (beanClassName.startsWith(packageName)) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Excluding controller bean '" + beanName + "' from class name mapping because its bean class is defined in an excluded package: " + beanClass.getName());
                    return false;
                }
                return false;
            }
        }
        return isControllerType(beanClass);
    }

    protected boolean isControllerType(Class<?> beanClass) {
        return this.predicate.isControllerType(beanClass);
    }

    protected boolean isMultiActionControllerType(Class<?> beanClass) {
        return this.predicate.isMultiActionControllerType(beanClass);
    }
}
