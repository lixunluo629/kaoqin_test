package javax.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/annotation/MultipartConfig.class */
public @interface MultipartConfig {
    String location() default "";

    long maxFileSize() default -1;

    long maxRequestSize() default -1;

    int fileSizeThreshold() default 0;
}
