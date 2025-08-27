package springfox.documentation.schema.property;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.schema.AlternateTypeProvider;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/BaseModelProperty.class */
public abstract class BaseModelProperty implements ModelProperty {
    private final String name;
    protected final AlternateTypeProvider alternateTypeProvider;

    protected abstract ResolvedType realType();

    public BaseModelProperty(String name, AlternateTypeProvider alternateTypeProvider) {
        this.name = name;
        this.alternateTypeProvider = alternateTypeProvider;
    }

    @Override // springfox.documentation.schema.property.ModelProperty
    public ResolvedType getType() {
        return this.alternateTypeProvider.alternateFor(realType());
    }

    @Override // springfox.documentation.schema.property.ModelProperty
    public String getName() {
        return this.name;
    }

    @Override // springfox.documentation.schema.property.ModelProperty
    public String qualifiedTypeName() {
        if (getType().getTypeParameters().size() > 0) {
            return getType().toString();
        }
        return ResolvedTypes.simpleQualifiedTypeName(getType());
    }

    @Override // springfox.documentation.schema.property.ModelProperty
    public AllowableValues allowableValues() {
        Optional<AllowableValues> allowableValues = Optional.fromNullable(ResolvedTypes.allowableValues(getType()));
        if (allowableValues.isPresent()) {
            return allowableValues.get();
        }
        return null;
    }

    @Override // springfox.documentation.schema.property.ModelProperty
    public boolean isRequired() {
        return false;
    }

    @Override // springfox.documentation.schema.property.ModelProperty
    public boolean isReadOnly() {
        return false;
    }

    @Override // springfox.documentation.schema.property.ModelProperty
    public String propertyDescription() {
        return null;
    }

    @Override // springfox.documentation.schema.property.ModelProperty
    public int position() {
        return 0;
    }
}
