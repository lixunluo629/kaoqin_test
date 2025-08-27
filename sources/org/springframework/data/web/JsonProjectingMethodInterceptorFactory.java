package org.springframework.data.web;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.projection.Accessor;
import org.springframework.data.projection.MethodInterceptorFactory;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/JsonProjectingMethodInterceptorFactory.class */
public class JsonProjectingMethodInterceptorFactory implements MethodInterceptorFactory {
    private final ParseContext context;

    public JsonProjectingMethodInterceptorFactory(MappingProvider mappingProvider) {
        Assert.notNull(mappingProvider, "MappingProvider must not be null!");
        Configuration build = Configuration.builder().options(new Option[]{Option.ALWAYS_RETURN_LIST}).mappingProvider(mappingProvider).build();
        this.context = com.jayway.jsonpath.JsonPath.using(build);
    }

    @Override // org.springframework.data.projection.MethodInterceptorFactory
    public MethodInterceptor createMethodInterceptor(Object source, Class<?> targetType) {
        DocumentContext context = InputStream.class.isInstance(source) ? this.context.parse((InputStream) source) : this.context.parse(source);
        return new InputMessageProjecting(context);
    }

    @Override // org.springframework.data.projection.MethodInterceptorFactory
    public boolean supports(Object source, Class<?> targetType) {
        if (InputStream.class.isInstance(source) || JSONObject.class.isInstance(source) || JSONArray.class.isInstance(source)) {
            return true;
        }
        return Map.class.isInstance(source) && hasJsonPathAnnotation(targetType);
    }

    private static boolean hasJsonPathAnnotation(Class<?> type) throws SecurityException {
        for (Method method : type.getMethods()) {
            if (AnnotationUtils.findAnnotation(method, JsonPath.class) != null) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/JsonProjectingMethodInterceptorFactory$InputMessageProjecting.class */
    private static class InputMessageProjecting implements MethodInterceptor {
        private final DocumentContext context;

        @Generated
        public InputMessageProjecting(DocumentContext context) {
            this.context = context;
        }

        @Override // org.aopalliance.intercept.MethodInterceptor
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();
            TypeInformation<Object> returnType = ClassTypeInformation.fromReturnTypeOf(method);
            ResolvableType type = ResolvableType.forMethodReturnType(method);
            String jsonPath = getJsonPath(method);
            try {
                if (returnType.getActualType().getType().isInterface()) {
                    List<?> result = (List) this.context.read(jsonPath, new Predicate[0]);
                    if (result.isEmpty()) {
                        return null;
                    }
                    return result.get(0);
                }
                boolean isCollectionResult = Collection.class.isAssignableFrom(type.getRawClass());
                ResolvableType type2 = isCollectionResult ? type : ResolvableType.forClassWithGenerics((Class<?>) List.class, type);
                List<?> result2 = (List) this.context.read(jsonPath, new ResolvableTypeRef((isCollectionResult && com.jayway.jsonpath.JsonPath.isPathDefinite(jsonPath)) ? ResolvableType.forClassWithGenerics((Class<?>) List.class, type2) : type2));
                if (isCollectionResult && com.jayway.jsonpath.JsonPath.isPathDefinite(jsonPath)) {
                    result2 = (List) result2.get(0);
                }
                if (isCollectionResult) {
                    return result2;
                }
                if (result2.isEmpty()) {
                    return null;
                }
                return result2.get(0);
            } catch (PathNotFoundException e) {
                return null;
            }
        }

        private static String getJsonPath(Method method) {
            JsonPath annotation = (JsonPath) AnnotationUtils.findAnnotation(method, JsonPath.class);
            return annotation != null ? annotation.value() : "$.".concat(new Accessor(method).getPropertyName());
        }

        /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/JsonProjectingMethodInterceptorFactory$InputMessageProjecting$ResolvableTypeRef.class */
        private static class ResolvableTypeRef extends TypeRef<Object> {
            private final ResolvableType type;

            @Generated
            public ResolvableTypeRef(ResolvableType type) {
                this.type = type;
            }

            public Type getType() {
                return this.type.getType();
            }
        }
    }
}
