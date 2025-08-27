package org.springframework.objenesis.instantiator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/objenesis/instantiator/annotations/Instantiator.class */
public @interface Instantiator {
    Typology value();
}
