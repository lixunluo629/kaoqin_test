package javax.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: tomcat-annotations-api-8.5.43.jar:javax/annotation/Resource.class */
public @interface Resource {

    /* loaded from: tomcat-annotations-api-8.5.43.jar:javax/annotation/Resource$AuthenticationType.class */
    public enum AuthenticationType {
        CONTAINER,
        APPLICATION
    }

    String name() default "";

    Class<?> type() default Object.class;

    AuthenticationType authenticationType() default AuthenticationType.CONTAINER;

    boolean shareable() default true;

    String description() default "";

    String mappedName() default "";

    String lookup() default "";
}
