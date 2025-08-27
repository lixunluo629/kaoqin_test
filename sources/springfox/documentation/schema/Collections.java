package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.xmlbeans.impl.schema.SoapEncSchemaTypeSystem;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/Collections.class */
public class Collections {
    private Collections() {
        throw new UnsupportedOperationException();
    }

    public static ResolvedType collectionElementType(ResolvedType type) {
        if (List.class.isAssignableFrom(type.getErasedType())) {
            return elementType(type, List.class);
        }
        if (Set.class.isAssignableFrom(type.getErasedType())) {
            return elementType(type, Set.class);
        }
        if (type.isArray()) {
            return type.getArrayElementType();
        }
        return null;
    }

    public static boolean isContainerType(ResolvedType type) {
        if (List.class.isAssignableFrom(type.getErasedType()) || Set.class.isAssignableFrom(type.getErasedType()) || type.isArray()) {
            return true;
        }
        return false;
    }

    public static String containerType(ResolvedType type) {
        if (List.class.isAssignableFrom(type.getErasedType())) {
            return "List";
        }
        if (Set.class.isAssignableFrom(type.getErasedType())) {
            return "Set";
        }
        if (type.isArray()) {
            return SoapEncSchemaTypeSystem.SOAP_ARRAY;
        }
        throw new UnsupportedOperationException(String.format("Type is not collection type %s", type));
    }

    private static <T extends Collection> ResolvedType elementType(ResolvedType container, Class<T> collectionType) {
        List<ResolvedType> resolvedTypes = container.typeParametersFor(collectionType);
        if (resolvedTypes.size() == 1) {
            return resolvedTypes.get(0);
        }
        return new TypeResolver().resolve(Object.class, new Type[0]);
    }
}
