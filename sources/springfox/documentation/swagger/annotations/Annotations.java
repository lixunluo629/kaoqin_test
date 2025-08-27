package springfox.documentation.swagger.annotations;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import org.springframework.core.annotation.AnnotationUtils;

/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/annotations/Annotations.class */
public class Annotations {
    private Annotations() {
        throw new UnsupportedOperationException();
    }

    public static Optional<ApiParam> findApiParamAnnotation(AnnotatedElement annotated) {
        return Optional.fromNullable(AnnotationUtils.getAnnotation(annotated, ApiParam.class));
    }

    public static Optional<ApiOperation> findApiOperationAnnotation(Method annotated) {
        return Optional.fromNullable(AnnotationUtils.findAnnotation(annotated, ApiOperation.class));
    }

    public static Optional<ApiResponses> findApiResponsesAnnotations(Method annotated) {
        return Optional.fromNullable(AnnotationUtils.findAnnotation(annotated, ApiResponses.class));
    }

    public static Function<ApiOperation, ResolvedType> resolvedTypeFromOperation(final TypeResolver typeResolver, final ResolvedType defaultType) {
        return new Function<ApiOperation, ResolvedType>() { // from class: springfox.documentation.swagger.annotations.Annotations.1
            @Override // com.google.common.base.Function
            public ResolvedType apply(ApiOperation annotation) {
                return Annotations.getResolvedType(annotation, typeResolver, defaultType);
            }
        };
    }

    public static Function<ApiResponse, ResolvedType> resolvedTypeFromResponse(final TypeResolver typeResolver, final ResolvedType defaultType) {
        return new Function<ApiResponse, ResolvedType>() { // from class: springfox.documentation.swagger.annotations.Annotations.2
            @Override // com.google.common.base.Function
            public ResolvedType apply(ApiResponse annotation) {
                return Annotations.getResolvedType(annotation, typeResolver, defaultType);
            }
        };
    }

    @VisibleForTesting
    static ResolvedType getResolvedType(ApiOperation annotation, TypeResolver typeResolver, ResolvedType defaultType) {
        if (null != annotation && Void.class != annotation.response()) {
            if ("List".compareToIgnoreCase(annotation.responseContainer()) == 0) {
                return typeResolver.resolve(List.class, annotation.response());
            }
            if ("Set".compareToIgnoreCase(annotation.responseContainer()) == 0) {
                return typeResolver.resolve(Set.class, annotation.response());
            }
            return typeResolver.resolve(annotation.response(), new Type[0]);
        }
        return defaultType;
    }

    @VisibleForTesting
    static ResolvedType getResolvedType(ApiResponse annotation, TypeResolver typeResolver, ResolvedType defaultType) {
        if (null != annotation && Void.class != annotation.response()) {
            if ("List".compareToIgnoreCase(annotation.responseContainer()) == 0) {
                return typeResolver.resolve(List.class, annotation.response());
            }
            if ("Set".compareToIgnoreCase(annotation.responseContainer()) == 0) {
                return typeResolver.resolve(Set.class, annotation.response());
            }
            return typeResolver.resolve(annotation.response(), new Type[0]);
        }
        return defaultType;
    }
}
