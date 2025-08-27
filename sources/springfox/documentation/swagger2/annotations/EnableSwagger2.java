package springfox.documentation.swagger2.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({Swagger2DocumentationConfiguration.class})
/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/annotations/EnableSwagger2.class */
public @interface EnableSwagger2 {
}
