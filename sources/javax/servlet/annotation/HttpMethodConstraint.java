package javax.servlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.servlet.annotation.ServletSecurity;

@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/annotation/HttpMethodConstraint.class */
public @interface HttpMethodConstraint {
    String value();

    ServletSecurity.EmptyRoleSemantic emptyRoleSemantic() default ServletSecurity.EmptyRoleSemantic.PERMIT;

    ServletSecurity.TransportGuarantee transportGuarantee() default ServletSecurity.TransportGuarantee.NONE;

    String[] rolesAllowed() default {};
}
