package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Controller;

@Target({ElementType.TYPE})
@Controller
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseBody
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/annotation/RestController.class */
public @interface RestController {
    String value() default "";
}
