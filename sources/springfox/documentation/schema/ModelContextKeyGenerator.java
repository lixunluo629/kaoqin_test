package springfox.documentation.schema;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import springfox.documentation.spi.schema.contexts.ModelContext;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/ModelContextKeyGenerator.class */
public class ModelContextKeyGenerator implements KeyGenerator {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) ModelContextKeyGenerator.class);
    private final TypeResolver resolver;

    public ModelContextKeyGenerator(TypeResolver resolver) {
        this.resolver = resolver;
    }

    @Override // org.springframework.cache.interceptor.KeyGenerator
    public Object generate(Object target, Method method, Object... params) {
        Optional<ModelContext> context = FluentIterable.from(Lists.newArrayList(params)).filter(ModelContext.class).first();
        if (context.isPresent()) {
            String key = String.format("%s(%s)", context.get().resolvedType(this.resolver).toString(), Boolean.valueOf(context.get().isReturnType()));
            LOG.info("Cache Key Generated: {}", key);
            return key;
        }
        throw new IllegalArgumentException("Key generator can only be used where at least one parameter is of type ModelContext");
    }
}
