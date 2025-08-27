package springfox.documentation.spring.web.readers.parameter;

import com.fasterxml.classmate.ResolvedType;
import java.lang.annotation.Annotation;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/parameter/ParameterTypeReader.class */
public class ParameterTypeReader implements ParameterBuilderPlugin {
    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        context.parameterBuilder().parameterType(findParameterType(context));
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    public static String findParameterType(ParameterContext parameterContext) {
        MethodParameter methodParameter = parameterContext.methodParameter();
        ResolvedMethodParameter resolvedMethodParameter = parameterContext.resolvedMethodParameter();
        ResolvedType parameterType = resolvedMethodParameter.getResolvedParameterType();
        if (MultipartFile.class.isAssignableFrom(parameterContext.alternateFor(parameterType).getErasedType())) {
            return "form";
        }
        Annotation[] methodAnnotations = methodParameter.getParameterAnnotations();
        for (Annotation annotation : methodAnnotations) {
            if (annotation instanceof PathVariable) {
                return Cookie2.PATH;
            }
            if ((annotation instanceof ModelAttribute) || (annotation instanceof RequestBody)) {
                return "body";
            }
            if (annotation instanceof RequestParam) {
                return queryOrForm(parameterContext.getOperationContext());
            }
            if (annotation instanceof RequestHeader) {
                return "header";
            }
            if (annotation instanceof RequestPart) {
                return "form";
            }
        }
        return "body";
    }

    private static String queryOrForm(OperationContext context) {
        if (context.consumes().contains(MediaType.APPLICATION_FORM_URLENCODED) && context.httpMethod() == HttpMethod.POST) {
            return "form";
        }
        return "query";
    }
}
