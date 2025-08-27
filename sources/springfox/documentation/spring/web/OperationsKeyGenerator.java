package springfox.documentation.spring.web;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.readers.operation.ApiOperationReader;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/OperationsKeyGenerator.class */
public class OperationsKeyGenerator implements KeyGenerator {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) OperationsKeyGenerator.class);
    public static final String OPERATION_KEY_SPEL = "T(springfox.documentation.spring.web.OperationsKeyGenerator).operationKey(#outerContext)";

    public static String operationKey(RequestMappingContext context) {
        return new OperationsKeyGenerator().generate(ApiOperationReader.class, null, context).toString();
    }

    @Override // org.springframework.cache.interceptor.KeyGenerator
    public Object generate(Object target, Method method, Object... params) {
        Optional<RequestMappingContext> context = FluentIterable.from(Lists.newArrayList(nullToEmptyArray(params))).filter(RequestMappingContext.class).first();
        if (context.isPresent()) {
            String key = String.format("%s.%s.%s.%s", context.get().getRequestMappingPattern(), context.get().getHandlerMethod().getMethod().getDeclaringClass().getName(), context.get().getHandlerMethod().getMethod().getName(), context.get().getDocumentationContext().getGenericsNamingStrategy().getClass().getSimpleName());
            LOG.info("Cache key generated: {}", key);
            return key;
        }
        throw new IllegalArgumentException("Key generator can only be used where the first Parameter is of type RequestMappingContext");
    }

    private Object[] nullToEmptyArray(Object[] params) {
        if (params == null) {
            return new Object[0];
        }
        return params;
    }
}
