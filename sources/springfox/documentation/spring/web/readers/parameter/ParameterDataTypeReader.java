package springfox.documentation.spring.web.readers.parameter;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import java.io.File;
import java.lang.reflect.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.schema.Collections;
import springfox.documentation.schema.Maps;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/parameter/ParameterDataTypeReader.class */
public class ParameterDataTypeReader implements ParameterBuilderPlugin {
    private final TypeNameExtractor nameExtractor;
    private final TypeResolver resolver;

    @Autowired
    public ParameterDataTypeReader(TypeNameExtractor nameExtractor, TypeResolver resolver) {
        this.nameExtractor = nameExtractor;
        this.resolver = resolver;
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        ResolvedMethodParameter methodParameter = context.resolvedMethodParameter();
        ResolvedType parameterType = context.alternateFor(methodParameter.getResolvedParameterType());
        if (MultipartFile.class.isAssignableFrom(parameterType.getErasedType())) {
            context.parameterBuilder().type(this.resolver.resolve(File.class, new Type[0])).modelRef(new ModelRef("File"));
        } else {
            ModelContext modelContext = ModelContext.inputParam(parameterType, context.getDocumentationType(), context.getAlternateTypeProvider(), context.getGenericNamingStrategy());
            context.parameterBuilder().type(parameterType).modelRef(modelRef(parameterType, modelContext));
        }
    }

    private ModelRef modelRef(ResolvedType type, ModelContext modelContext) {
        if (Collections.isContainerType(type)) {
            ResolvedType collectionElementType = Collections.collectionElementType(type);
            if (collectionElementType != null && MultipartFile.class.isAssignableFrom(collectionElementType.getErasedType())) {
                return new ModelRef(Collections.containerType(type), "File");
            }
            String elementTypeName = this.nameExtractor.typeName(ModelContext.fromParent(modelContext, collectionElementType));
            return new ModelRef(Collections.containerType(type), elementTypeName);
        }
        if (Maps.isMapType(type)) {
            String elementTypeName2 = this.nameExtractor.typeName(ModelContext.fromParent(modelContext, Maps.mapValueType(type)));
            return new ModelRef("Map", elementTypeName2, true);
        }
        String typeName = this.nameExtractor.typeName(ModelContext.fromParent(modelContext, type));
        return new ModelRef(typeName);
    }
}
