package springfox.documentation.schema.property.bean;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.members.ResolvedParameterizedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import springfox.documentation.schema.property.BaseModelProperty;
import springfox.documentation.spi.schema.AlternateTypeProvider;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/bean/ParameterModelProperty.class */
public class ParameterModelProperty extends BaseModelProperty {
    private final AnnotatedParameter parameter;
    private final ResolvedParameterizedMember constructor;

    public ParameterModelProperty(String name, AnnotatedParameter parameter, ResolvedParameterizedMember constructor, AlternateTypeProvider alternateTypeProvider) {
        super(name, alternateTypeProvider);
        this.parameter = parameter;
        this.constructor = constructor;
    }

    @Override // springfox.documentation.schema.property.BaseModelProperty, springfox.documentation.schema.property.ModelProperty
    public String qualifiedTypeName() {
        return getType().toString();
    }

    @Override // springfox.documentation.schema.property.BaseModelProperty, springfox.documentation.schema.property.ModelProperty
    public boolean isRequired() {
        return true;
    }

    @Override // springfox.documentation.schema.property.BaseModelProperty, springfox.documentation.schema.property.ModelProperty
    public boolean isReadOnly() {
        return false;
    }

    @Override // springfox.documentation.schema.property.BaseModelProperty, springfox.documentation.schema.property.ModelProperty
    public int position() {
        return this.parameter.getIndex();
    }

    @Override // springfox.documentation.schema.property.BaseModelProperty
    protected ResolvedType realType() {
        return this.constructor.getArgumentType(this.parameter.getIndex());
    }
}
