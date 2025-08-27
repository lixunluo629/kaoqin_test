package org.apache.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.StatementType;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/annotations/Options.class */
public @interface Options {

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/annotations/Options$FlushCachePolicy.class */
    public enum FlushCachePolicy {
        DEFAULT,
        TRUE,
        FALSE
    }

    boolean useCache() default true;

    FlushCachePolicy flushCache() default FlushCachePolicy.DEFAULT;

    ResultSetType resultSetType() default ResultSetType.FORWARD_ONLY;

    StatementType statementType() default StatementType.PREPARED;

    int fetchSize() default -1;

    int timeout() default -1;

    boolean useGeneratedKeys() default false;

    String keyProperty() default "id";

    String keyColumn() default "";

    String resultSets() default "";
}
