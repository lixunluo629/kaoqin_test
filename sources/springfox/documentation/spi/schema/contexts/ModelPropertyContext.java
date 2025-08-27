package springfox.documentation.spi.schema.contexts;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;
import java.lang.reflect.AnnotatedElement;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.spi.DocumentationType;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/schema/contexts/ModelPropertyContext.class */
public class ModelPropertyContext {
    private final ModelPropertyBuilder builder;
    private final TypeResolver resolver;
    private final Optional<BeanPropertyDefinition> beanPropertyDefinition;
    private final Optional<AnnotatedElement> annotatedElement;
    private final DocumentationType documentationType;

    public ModelPropertyContext(ModelPropertyBuilder builder, AnnotatedElement annotatedElement, TypeResolver resolver, DocumentationType documentationType) {
        this.builder = builder;
        this.resolver = resolver;
        this.annotatedElement = Optional.fromNullable(annotatedElement);
        this.beanPropertyDefinition = Optional.absent();
        this.documentationType = documentationType;
    }

    public ModelPropertyContext(ModelPropertyBuilder builder, BeanPropertyDefinition beanPropertyDefinition, TypeResolver resolver, DocumentationType documentationType) {
        this.builder = builder;
        this.resolver = resolver;
        this.beanPropertyDefinition = Optional.fromNullable(beanPropertyDefinition);
        this.documentationType = documentationType;
        this.annotatedElement = Optional.absent();
    }

    public ModelPropertyBuilder getBuilder() {
        return this.builder;
    }

    public DocumentationType getDocumentationType() {
        return this.documentationType;
    }

    public Optional<AnnotatedElement> getAnnotatedElement() {
        return this.annotatedElement;
    }

    public Optional<BeanPropertyDefinition> getBeanPropertyDefinition() {
        return this.beanPropertyDefinition;
    }

    public TypeResolver getResolver() {
        return this.resolver;
    }
}
