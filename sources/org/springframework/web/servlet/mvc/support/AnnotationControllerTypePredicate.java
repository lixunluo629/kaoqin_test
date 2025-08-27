package org.springframework.web.servlet.mvc.support;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/support/AnnotationControllerTypePredicate.class */
class AnnotationControllerTypePredicate extends ControllerTypePredicate {
    AnnotationControllerTypePredicate() {
    }

    @Override // org.springframework.web.servlet.mvc.support.ControllerTypePredicate
    public boolean isControllerType(Class<?> beanClass) {
        return super.isControllerType(beanClass) || AnnotationUtils.findAnnotation(beanClass, Controller.class) != null;
    }

    @Override // org.springframework.web.servlet.mvc.support.ControllerTypePredicate
    public boolean isMultiActionControllerType(Class<?> beanClass) {
        return super.isMultiActionControllerType(beanClass) || AnnotationUtils.findAnnotation(beanClass, Controller.class) != null;
    }
}
