package springfox.documentation.schema;

import com.fasterxml.classmate.TypeResolver;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/schema/AlternateTypeRules.class */
public class AlternateTypeRules {
    private AlternateTypeRules() {
        throw new UnsupportedOperationException();
    }

    public static AlternateTypeRule newRule(Type original, Type alternate) {
        TypeResolver resolver = new TypeResolver();
        return new AlternateTypeRule(resolver.resolve(original, new Type[0]), resolver.resolve(alternate, new Type[0]));
    }

    public static AlternateTypeRule newMapRule(Class<?> key, Class<?> value) {
        TypeResolver resolver = new TypeResolver();
        return new AlternateTypeRule(resolver.resolve(Map.class, key, value), resolver.resolve(List.class, resolver.resolve(Entry.class, key, value)));
    }
}
