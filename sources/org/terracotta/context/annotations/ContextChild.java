package org.terracotta.context.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/annotations/ContextChild.class */
public @interface ContextChild {
}
