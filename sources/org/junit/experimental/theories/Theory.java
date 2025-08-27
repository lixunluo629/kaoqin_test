package org.junit.experimental.theories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: junit-4.12.jar:org/junit/experimental/theories/Theory.class */
public @interface Theory {
    boolean nullsAccepted() default true;
}
