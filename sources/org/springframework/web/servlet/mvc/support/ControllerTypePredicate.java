package org.springframework.web.servlet.mvc.support;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/support/ControllerTypePredicate.class */
class ControllerTypePredicate {
    ControllerTypePredicate() {
    }

    public boolean isControllerType(Class<?> beanClass) {
        return Controller.class.isAssignableFrom(beanClass);
    }

    public boolean isMultiActionControllerType(Class<?> beanClass) {
        return MultiActionController.class.isAssignableFrom(beanClass);
    }
}
