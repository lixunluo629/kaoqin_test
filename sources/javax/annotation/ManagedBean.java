package javax.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: tomcat-annotations-api-8.5.43.jar:javax/annotation/ManagedBean.class */
public @interface ManagedBean {
    String value() default "";
}
