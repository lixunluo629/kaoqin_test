package javax.annotation.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: tomcat-annotations-api-8.5.43.jar:javax/annotation/security/DeclareRoles.class */
public @interface DeclareRoles {
    String[] value();
}
