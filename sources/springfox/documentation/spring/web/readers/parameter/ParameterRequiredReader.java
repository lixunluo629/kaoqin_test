package springfox.documentation.spring.web.readers.parameter;

import com.google.common.base.Strings;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ValueConstants;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/parameter/ParameterRequiredReader.class */
public class ParameterRequiredReader implements ParameterBuilderPlugin {
    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        MethodParameter methodParameter = context.methodParameter();
        context.parameterBuilder().required(getAnnotatedRequired(methodParameter).booleanValue());
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private Boolean getAnnotatedRequired(MethodParameter methodParameter) {
        Set<Boolean> requiredSet = new HashSet<>();
        Annotation[] methodAnnotations = methodParameter.getParameterAnnotations();
        boolean optional = isOptional(methodParameter);
        for (Annotation annotation : methodAnnotations) {
            if (annotation instanceof RequestParam) {
                requiredSet.add(Boolean.valueOf(!optional && isRequired((RequestParam) annotation)));
            } else if (annotation instanceof RequestHeader) {
                requiredSet.add(Boolean.valueOf(!optional && ((RequestHeader) annotation).required()));
            } else if (annotation instanceof PathVariable) {
                requiredSet.add(true);
            } else if (annotation instanceof RequestBody) {
                requiredSet.add(Boolean.valueOf(!optional && ((RequestBody) annotation).required()));
            } else if (annotation instanceof RequestPart) {
                requiredSet.add(Boolean.valueOf(!optional && ((RequestPart) annotation).required()));
            }
        }
        return Boolean.valueOf(requiredSet.contains(true));
    }

    private boolean isOptional(MethodParameter methodParameter) {
        return methodParameter.getParameterType().getName().equals("java.util.Optional");
    }

    private boolean isRequired(RequestParam annotation) {
        String defaultValue = annotation.defaultValue();
        boolean missingDefaultValue = ValueConstants.DEFAULT_NONE.equals(defaultValue) || Strings.isNullOrEmpty(defaultValue);
        return annotation.required() && missingDefaultValue;
    }
}
