package springfox.documentation.builders;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.lang.annotation.Annotation;
import org.springframework.core.annotation.AnnotationUtils;
import springfox.documentation.RequestHandler;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/RequestHandlerSelectors.class */
public class RequestHandlerSelectors {
    private RequestHandlerSelectors() {
        throw new UnsupportedOperationException();
    }

    public static Predicate<RequestHandler> any() {
        return Predicates.alwaysTrue();
    }

    public static Predicate<RequestHandler> none() {
        return Predicates.alwaysFalse();
    }

    public static Predicate<RequestHandler> withMethodAnnotation(final Class<? extends Annotation> annotation) {
        return new Predicate<RequestHandler>() { // from class: springfox.documentation.builders.RequestHandlerSelectors.1
            @Override // com.google.common.base.Predicate
            public boolean apply(RequestHandler input) {
                return null != AnnotationUtils.findAnnotation(input.getHandlerMethod().getMethod(), annotation);
            }
        };
    }

    public static Predicate<RequestHandler> withClassAnnotation(final Class<? extends Annotation> annotation) {
        return new Predicate<RequestHandler>() { // from class: springfox.documentation.builders.RequestHandlerSelectors.2
            @Override // com.google.common.base.Predicate
            public boolean apply(RequestHandler input) {
                return RequestHandlerSelectors.declaringClass(input).isAnnotationPresent(annotation);
            }
        };
    }

    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return new Predicate<RequestHandler>() { // from class: springfox.documentation.builders.RequestHandlerSelectors.3
            @Override // com.google.common.base.Predicate
            public boolean apply(RequestHandler input) {
                return RequestHandlerSelectors.declaringClass(input).getPackage().getName().startsWith(basePackage);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Class<?> declaringClass(RequestHandler input) {
        return input.getHandlerMethod().getMethod().getDeclaringClass();
    }
}
