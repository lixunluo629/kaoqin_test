package springfox.documentation.spring.web.readers.parameter;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import java.lang.annotation.Annotation;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/parameter/ParameterNameReader.class */
public class ParameterNameReader implements ParameterBuilderPlugin {
    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        MethodParameter methodParameter = context.methodParameter();
        String name = findParameterNameFromAnnotations(methodParameter);
        String parameterName = methodParameter.getParameterName();
        if (Strings.isNullOrEmpty(name)) {
            name = Strings.isNullOrEmpty(parameterName) ? String.format("param%s", Integer.valueOf(methodParameter.getParameterIndex())) : parameterName;
        }
        context.parameterBuilder().name(name);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private String findParameterNameFromAnnotations(MethodParameter methodParameter) {
        List<Annotation> methodAnnotations = Lists.newArrayList(methodParameter.getParameterAnnotations());
        return (String) FluentIterable.from(methodAnnotations).filter(PathVariable.class).first().transform(pathVariableValue()).or(first(methodAnnotations, ModelAttribute.class).transform(modelAttributeValue())).or(first(methodAnnotations, RequestParam.class).transform(requestParamValue())).or(first(methodAnnotations, RequestHeader.class).transform(requestHeaderValue())).orNull();
    }

    private <T> Optional<T> first(List<Annotation> methodAnnotations, Class<T> ofType) {
        return FluentIterable.from(methodAnnotations).filter(ofType).first();
    }

    private Function<RequestHeader, String> requestHeaderValue() {
        return new Function<RequestHeader, String>() { // from class: springfox.documentation.spring.web.readers.parameter.ParameterNameReader.1
            @Override // com.google.common.base.Function
            public String apply(RequestHeader input) {
                return input.value();
            }
        };
    }

    private Function<RequestParam, String> requestParamValue() {
        return new Function<RequestParam, String>() { // from class: springfox.documentation.spring.web.readers.parameter.ParameterNameReader.2
            @Override // com.google.common.base.Function
            public String apply(RequestParam input) {
                return input.value();
            }
        };
    }

    private Function<ModelAttribute, String> modelAttributeValue() {
        return new Function<ModelAttribute, String>() { // from class: springfox.documentation.spring.web.readers.parameter.ParameterNameReader.3
            @Override // com.google.common.base.Function
            public String apply(ModelAttribute input) {
                return input.value();
            }
        };
    }

    private Function<PathVariable, String> pathVariableValue() {
        return new Function<PathVariable, String>() { // from class: springfox.documentation.spring.web.readers.parameter.ParameterNameReader.4
            @Override // com.google.common.base.Function
            public String apply(PathVariable input) {
                return input.value();
            }
        };
    }
}
