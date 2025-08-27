package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.types.ResolvedArrayType;
import com.fasterxml.classmate.types.ResolvedPrimitiveType;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import java.lang.reflect.Type;
import java.util.List;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.schema.contexts.ModelContext;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/ResolvedTypes.class */
public class ResolvedTypes {
    private ResolvedTypes() {
        throw new UnsupportedOperationException();
    }

    public static String simpleQualifiedTypeName(ResolvedType type) {
        if (type instanceof ResolvedPrimitiveType) {
            Type primitiveType = type.getErasedType();
            return Types.typeNameFor(primitiveType);
        }
        if (type instanceof ResolvedArrayType) {
            return Types.typeNameFor(type.getArrayElementType().getErasedType());
        }
        return type.getErasedType().getName();
    }

    public static AllowableValues allowableValues(ResolvedType resolvedType) {
        List<ResolvedType> typeParameters;
        if (Collections.isContainerType(resolvedType) && (typeParameters = resolvedType.getTypeParameters()) != null && typeParameters.size() == 1) {
            return Enums.allowableValues(typeParameters.get(0).getErasedType());
        }
        return Enums.allowableValues(resolvedType.getErasedType());
    }

    public static Optional<String> resolvedTypeSignature(ResolvedType resolvedType) {
        return Optional.fromNullable(resolvedType).transform(new Function<ResolvedType, String>() { // from class: springfox.documentation.schema.ResolvedTypes.1
            @Override // com.google.common.base.Function
            public String apply(ResolvedType input) {
                return input.getSignature();
            }
        });
    }

    public static Function<? super ResolvedType, ModelRef> modelRefFactory(final ModelContext parentContext, final TypeNameExtractor typeNameExtractor) {
        return new Function<ResolvedType, ModelRef>() { // from class: springfox.documentation.schema.ResolvedTypes.2
            @Override // com.google.common.base.Function
            public ModelRef apply(ResolvedType type) {
                if (Collections.isContainerType(type)) {
                    ResolvedType collectionElementType = Collections.collectionElementType(type);
                    String elementTypeName = typeNameExtractor.typeName(ModelContext.fromParent(parentContext, collectionElementType));
                    return new ModelRef(Collections.containerType(type), elementTypeName);
                }
                if (Maps.isMapType(type)) {
                    String elementTypeName2 = typeNameExtractor.typeName(ModelContext.fromParent(parentContext, Maps.mapValueType(type)));
                    return new ModelRef("Map", elementTypeName2, true);
                }
                String typeName = typeNameExtractor.typeName(ModelContext.fromParent(parentContext, type));
                return new ModelRef(typeName);
            }
        };
    }
}
