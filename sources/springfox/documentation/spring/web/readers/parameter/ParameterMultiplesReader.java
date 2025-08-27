package springfox.documentation.spring.web.readers.parameter;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/parameter/ParameterMultiplesReader.class */
public class ParameterMultiplesReader implements ParameterBuilderPlugin {
    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        MethodParameter methodParameter = context.methodParameter();
        Class<?> parameterType = methodParameter.getParameterType();
        if (parameterType != null) {
            Boolean allowMultiple = Boolean.valueOf(parameterType.isArray() || Iterable.class.isAssignableFrom(parameterType));
            context.parameterBuilder().allowMultiple(allowMultiple.booleanValue());
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
