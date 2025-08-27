package springfox.documentation.spring.web.readers.operation;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import springfox.documentation.schema.Collections;
import springfox.documentation.schema.Maps;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.schema.contexts.ModelContext;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/ModelRefs.class */
public class ModelRefs {
    private ModelRefs() {
        throw new UnsupportedOperationException();
    }

    public static Optional<ModelRef> modelRef(Optional<ResolvedType> type, ModelContext modelContext, TypeNameExtractor nameExtractor) {
        if (!type.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(modelRef(type.get(), modelContext, nameExtractor));
    }

    public static ModelRef modelRef(ResolvedType resolved, ModelContext modelContext, TypeNameExtractor nameExtractor) {
        if (Collections.isContainerType(resolved)) {
            ResolvedType collectionElementType = Collections.collectionElementType(resolved);
            String elementTypeName = nameExtractor.typeName(ModelContext.fromParent(modelContext, collectionElementType));
            AllowableValues allowableValues = ResolvedTypes.allowableValues(resolved);
            return new ModelRef(Collections.containerType(resolved), elementTypeName, allowableValues);
        }
        if (Maps.isMapType(resolved)) {
            String elementTypeName2 = nameExtractor.typeName(ModelContext.fromParent(modelContext, Maps.mapValueType(resolved)));
            return new ModelRef("Map", elementTypeName2, true);
        }
        if (Void.class.equals(resolved.getErasedType()) || Void.TYPE.equals(resolved.getErasedType())) {
            return new ModelRef("void");
        }
        AllowableValues allowableValues2 = ResolvedTypes.allowableValues(resolved);
        String typeName = nameExtractor.typeName(ModelContext.fromParent(modelContext, resolved));
        return new ModelRef(typeName, allowableValues2);
    }
}
