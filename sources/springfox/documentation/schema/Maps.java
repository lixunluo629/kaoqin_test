package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/Maps.class */
public class Maps {
    private Maps() {
        throw new UnsupportedOperationException();
    }

    public static ResolvedType mapValueType(ResolvedType type) {
        if (Map.class.isAssignableFrom(type.getErasedType())) {
            return mapValueType(type, Map.class);
        }
        return new TypeResolver().resolve(Object.class, new Type[0]);
    }

    private static ResolvedType mapValueType(ResolvedType container, Class<Map> mapClass) {
        List<ResolvedType> resolvedTypes = container.typeParametersFor(mapClass);
        if (resolvedTypes.size() == 2) {
            return resolvedTypes.get(1);
        }
        return new TypeResolver().resolve(Object.class, new Type[0]);
    }

    public static boolean isMapType(ResolvedType type) {
        return Map.class.isAssignableFrom(type.getErasedType());
    }
}
