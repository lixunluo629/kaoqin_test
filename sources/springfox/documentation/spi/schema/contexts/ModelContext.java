package springfox.documentation.spi.schema.contexts;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import java.lang.reflect.Type;
import java.util.Set;
import springfox.documentation.builders.ModelBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.AlternateTypeProvider;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/schema/contexts/ModelContext.class */
public class ModelContext {
    private final Type type;
    private final boolean returnType;
    private final DocumentationType documentationType;
    private final ModelContext parentContext;
    private final Set<ResolvedType> seenTypes;
    private final ModelBuilder modelBuilder;
    private final AlternateTypeProvider alternateTypeProvider;
    private GenericTypeNamingStrategy genericNamingStrategy;

    ModelContext(Type type, boolean returnType, DocumentationType documentationType, AlternateTypeProvider alternateTypeProvider, GenericTypeNamingStrategy genericNamingStrategy) {
        this.seenTypes = Sets.newHashSet();
        this.documentationType = documentationType;
        this.alternateTypeProvider = alternateTypeProvider;
        this.genericNamingStrategy = genericNamingStrategy;
        this.parentContext = null;
        this.type = type;
        this.returnType = returnType;
        this.modelBuilder = new ModelBuilder();
    }

    ModelContext(ModelContext parentContext, ResolvedType input) {
        this.seenTypes = Sets.newHashSet();
        this.parentContext = parentContext;
        this.type = input;
        this.returnType = parentContext.isReturnType();
        this.documentationType = parentContext.getDocumentationType();
        this.modelBuilder = new ModelBuilder();
        this.alternateTypeProvider = parentContext.alternateTypeProvider;
    }

    public Type getType() {
        return this.type;
    }

    public ResolvedType resolvedType(TypeResolver resolver) {
        return resolver.resolve(getType(), new Type[0]);
    }

    public boolean isReturnType() {
        return this.returnType;
    }

    public AlternateTypeProvider getAlternateTypeProvider() {
        return this.alternateTypeProvider;
    }

    public ResolvedType alternateFor(ResolvedType resolved) {
        return this.alternateTypeProvider.alternateFor(resolved);
    }

    public static ModelContext inputParam(Type type, DocumentationType documentationType, AlternateTypeProvider alternateTypeProvider, GenericTypeNamingStrategy genericNamingStrategy) {
        return new ModelContext(type, false, documentationType, alternateTypeProvider, genericNamingStrategy);
    }

    public static ModelContext returnValue(Type type, DocumentationType documentationType, AlternateTypeProvider alternateTypeProvider, GenericTypeNamingStrategy genericNamingStrategy) {
        return new ModelContext(type, true, documentationType, alternateTypeProvider, genericNamingStrategy);
    }

    public static ModelContext fromParent(ModelContext context, ResolvedType input) {
        return new ModelContext(context, input);
    }

    public boolean hasSeenBefore(ResolvedType resolvedType) {
        return this.seenTypes.contains(resolvedType) || this.seenTypes.contains(new TypeResolver().resolve(resolvedType.getErasedType(), new Type[0])) || parentHasSeenBefore(resolvedType);
    }

    public DocumentationType getDocumentationType() {
        return this.documentationType;
    }

    private boolean parentHasSeenBefore(ResolvedType resolvedType) {
        if (this.parentContext == null) {
            return false;
        }
        return this.parentContext.hasSeenBefore(resolvedType);
    }

    public GenericTypeNamingStrategy getGenericNamingStrategy() {
        if (this.parentContext == null) {
            return this.genericNamingStrategy;
        }
        return this.parentContext.getGenericNamingStrategy();
    }

    public ModelBuilder getBuilder() {
        return this.modelBuilder;
    }

    public void seen(ResolvedType resolvedType) {
        this.seenTypes.add(resolvedType);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModelContext that = (ModelContext) o;
        return Objects.equal(this.type, that.type) && Objects.equal(this.documentationType, that.documentationType) && Objects.equal(Boolean.valueOf(this.returnType), Boolean.valueOf(that.returnType)) && Objects.equal(this.genericNamingStrategy.getClass().getName(), that.genericNamingStrategy.getClass().getName());
    }

    public int hashCode() {
        return Objects.hashCode(this.type, this.documentationType, Boolean.valueOf(this.returnType), this.genericNamingStrategy.getClass().getName());
    }
}
