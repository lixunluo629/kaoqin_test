package org.springframework.data.convert;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ReadingConverter.class */
public @interface ReadingConverter {
}
