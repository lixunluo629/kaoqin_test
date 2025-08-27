package springfox.documentation.spi.service.contexts;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.AlternateTypeProvider;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spi.schema.contexts.ModelContext;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/OperationModelContextsBuilder.class */
public class OperationModelContextsBuilder {
    private final DocumentationType documentationType;
    private final AlternateTypeProvider alternateTypeProvider;
    private final GenericTypeNamingStrategy genericsNamingStrategy;
    private final Set<ModelContext> contexts = Sets.newHashSet();

    public OperationModelContextsBuilder(DocumentationType documentationType, AlternateTypeProvider alternateTypeProvider, GenericTypeNamingStrategy genericsNamingStrategy) {
        this.documentationType = documentationType;
        this.alternateTypeProvider = alternateTypeProvider;
        this.genericsNamingStrategy = genericsNamingStrategy;
    }

    public OperationModelContextsBuilder addReturn(Type type) {
        ModelContext returnValue = ModelContext.returnValue(type, this.documentationType, this.alternateTypeProvider, this.genericsNamingStrategy);
        this.contexts.add(returnValue);
        return this;
    }

    public OperationModelContextsBuilder addInputParam(Type type) {
        ModelContext inputParam = ModelContext.inputParam(type, this.documentationType, this.alternateTypeProvider, this.genericsNamingStrategy);
        this.contexts.add(inputParam);
        return this;
    }

    public Set<ModelContext> build() {
        return ImmutableSet.copyOf((Collection) this.contexts);
    }
}
