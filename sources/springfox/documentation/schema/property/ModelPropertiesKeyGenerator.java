package springfox.documentation.schema.property;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import springfox.documentation.spi.schema.contexts.ModelContext;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/ModelPropertiesKeyGenerator.class */
public class ModelPropertiesKeyGenerator implements KeyGenerator {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) ModelPropertiesKeyGenerator.class);

    @Override // org.springframework.cache.interceptor.KeyGenerator
    public Object generate(Object target, Method method, Object... params) {
        Optional<ResolvedType> type = FluentIterable.from(Lists.newArrayList(params)).filter(ResolvedType.class).first();
        Optional<ModelContext> context = FluentIterable.from(Lists.newArrayList(params)).filter(ModelContext.class).first();
        if (!type.isPresent()) {
            throw new IllegalArgumentException("Key generator can only be used where atleast one parameter is of type ResolvedType");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(type.get().toString());
        sb.append((String) context.transform(returnTypeComponent()).or((Optional) ""));
        LOG.info("Cache key generated: {}", sb.toString());
        return sb.toString();
    }

    private Function<ModelContext, String> returnTypeComponent() {
        return new Function<ModelContext, String>() { // from class: springfox.documentation.schema.property.ModelPropertiesKeyGenerator.1
            @Override // com.google.common.base.Function
            public String apply(ModelContext input) {
                return String.format("(%s)", Boolean.valueOf(input.isReturnType()));
            }
        };
    }
}
