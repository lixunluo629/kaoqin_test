package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD, ElementType.TYPE})
@Mapping
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/annotation/RequestMapping.class */
public @interface RequestMapping {
    String name() default "";

    @AliasFor(Cookie2.PATH)
    String[] value() default {};

    @AliasFor("value")
    String[] path() default {};

    RequestMethod[] method() default {};

    String[] params() default {};

    String[] headers() default {};

    String[] consumes() default {};

    String[] produces() default {};
}
