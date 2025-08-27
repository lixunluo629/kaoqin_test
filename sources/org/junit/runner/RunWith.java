package org.junit.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: junit-4.12.jar:org/junit/runner/RunWith.class */
public @interface RunWith {
    Class<? extends Runner> value();
}
