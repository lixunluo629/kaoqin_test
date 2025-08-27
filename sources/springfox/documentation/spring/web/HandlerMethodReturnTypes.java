package springfox.documentation.spring.web;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import java.lang.reflect.Proxy;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/HandlerMethodReturnTypes.class */
public final class HandlerMethodReturnTypes {
    private HandlerMethodReturnTypes() {
        throw new UnsupportedOperationException();
    }

    public static ResolvedType handlerReturnType(TypeResolver resolver, HandlerMethod handlerMethod) {
        Class hostClass = useType(handlerMethod.getBeanType()).or((Optional<Class>) handlerMethod.getMethod().getDeclaringClass());
        return new HandlerMethodResolver(resolver).methodReturnType(handlerMethod.getMethod(), hostClass);
    }

    public static Optional<Class> useType(Class beanType) {
        if (Proxy.class.isAssignableFrom(beanType)) {
            return Optional.absent();
        }
        if (Class.class.getName().equals(beanType.getName())) {
            return Optional.absent();
        }
        return Optional.fromNullable(beanType);
    }
}
