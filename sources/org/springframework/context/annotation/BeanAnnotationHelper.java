package org.springframework.context.annotation;

import java.lang.reflect.Method;
import org.springframework.core.annotation.AnnotatedElementUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/BeanAnnotationHelper.class */
class BeanAnnotationHelper {
    BeanAnnotationHelper() {
    }

    public static boolean isBeanAnnotated(Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, Bean.class);
    }

    public static String determineBeanNameFor(Method beanMethod) {
        String beanName = beanMethod.getName();
        Bean bean = (Bean) AnnotatedElementUtils.findMergedAnnotation(beanMethod, Bean.class);
        if (bean != null) {
            String[] names = bean.name();
            if (names.length > 0) {
                beanName = names[0];
            }
        }
        return beanName;
    }
}
