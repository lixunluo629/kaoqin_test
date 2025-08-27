package org.terracotta.statistics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/Statistic.class */
public @interface Statistic {
    String name();

    String[] tags() default {};
}
