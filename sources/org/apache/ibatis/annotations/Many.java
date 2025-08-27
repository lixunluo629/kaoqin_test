package org.apache.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.ibatis.mapping.FetchType;

@Target({})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/annotations/Many.class */
public @interface Many {
    String select() default "";

    FetchType fetchType() default FetchType.DEFAULT;
}
