package org.apache.ibatis.builder.annotation;

import java.lang.reflect.Method;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/annotation/MethodResolver.class */
public class MethodResolver {
    private final MapperAnnotationBuilder annotationBuilder;
    private final Method method;

    public MethodResolver(MapperAnnotationBuilder annotationBuilder, Method method) {
        this.annotationBuilder = annotationBuilder;
        this.method = method;
    }

    public void resolve() {
        this.annotationBuilder.parseStatement(this.method);
    }
}
