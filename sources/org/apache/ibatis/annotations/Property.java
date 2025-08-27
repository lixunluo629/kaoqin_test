package org.apache.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/annotations/Property.class */
public @interface Property {
    String name();

    String value();
}
