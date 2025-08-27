package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.types.ResolvedArrayType;
import com.fasterxml.classmate.types.ResolvedObjectType;
import com.fasterxml.classmate.types.ResolvedPrimitiveType;
import com.google.common.base.Optional;
import io.swagger.models.properties.StringProperty;
import java.lang.reflect.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spi.schema.TypeNameProviderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;

@Component
/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/TypeNameExtractor.class */
public class TypeNameExtractor {
    private final TypeResolver typeResolver;
    private final PluginRegistry<TypeNameProviderPlugin, DocumentationType> typeNameProviders;

    @Autowired
    public TypeNameExtractor(TypeResolver typeResolver, @Qualifier("typeNameProviderPluginRegistry") PluginRegistry<TypeNameProviderPlugin, DocumentationType> typeNameProviders) {
        this.typeResolver = typeResolver;
        this.typeNameProviders = typeNameProviders;
    }

    public String typeName(ModelContext context) {
        ResolvedType type = asResolved(context.getType());
        if (Collections.isContainerType(type)) {
            return Collections.containerType(type);
        }
        return innerTypeName(type, context);
    }

    private ResolvedType asResolved(Type type) {
        return this.typeResolver.resolve(type, new Type[0]);
    }

    private String genericTypeName(ResolvedType resolvedType, ModelContext context) {
        Class<?> erasedType = resolvedType.getErasedType();
        GenericTypeNamingStrategy namingStrategy = context.getGenericNamingStrategy();
        ModelNameContext nameContext = new ModelNameContext(resolvedType.getErasedType(), context.getDocumentationType());
        String simpleName = (String) Optional.fromNullable(Types.typeNameFor(erasedType)).or((Optional) typeName(nameContext));
        StringBuilder sb = new StringBuilder(String.format("%s%s", simpleName, namingStrategy.getOpenGeneric()));
        boolean first = true;
        for (int index = 0; index < erasedType.getTypeParameters().length; index++) {
            ResolvedType typeParam = resolvedType.getTypeParameters().get(index);
            if (first) {
                sb.append(innerTypeName(typeParam, context));
                first = false;
            } else {
                sb.append(String.format("%s%s", namingStrategy.getTypeListDelimiter(), innerTypeName(typeParam, context)));
            }
        }
        sb.append(namingStrategy.getCloseGeneric());
        return sb.toString();
    }

    private String innerTypeName(ResolvedType type, ModelContext context) {
        if (type.getTypeParameters().size() > 0 && type.getErasedType().getTypeParameters().length > 0) {
            return genericTypeName(type, context);
        }
        return simpleTypeName(type, context);
    }

    private String simpleTypeName(ResolvedType type, ModelContext context) {
        String typeName;
        Class<?> erasedType = type.getErasedType();
        if (type instanceof ResolvedPrimitiveType) {
            return Types.typeNameFor(erasedType);
        }
        if (erasedType.isEnum()) {
            return StringProperty.TYPE;
        }
        if (type instanceof ResolvedArrayType) {
            GenericTypeNamingStrategy namingStrategy = context.getGenericNamingStrategy();
            return String.format("Array%s%s%s", namingStrategy.getOpenGeneric(), simpleTypeName(type.getArrayElementType(), context), namingStrategy.getCloseGeneric());
        }
        if ((type instanceof ResolvedObjectType) && (typeName = Types.typeNameFor(erasedType)) != null) {
            return typeName;
        }
        return typeName(new ModelNameContext(type.getErasedType(), context.getDocumentationType()));
    }

    private String typeName(ModelNameContext context) {
        TypeNameProviderPlugin selected = (TypeNameProviderPlugin) this.typeNameProviders.getPluginFor((PluginRegistry<TypeNameProviderPlugin, DocumentationType>) context.getDocumentationType(), (DocumentationType) new DefaultTypeNameProvider());
        return selected.nameFor(context.getType());
    }
}
