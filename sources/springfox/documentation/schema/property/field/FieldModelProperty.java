package springfox.documentation.schema.property.field;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.members.ResolvedField;
import springfox.documentation.schema.property.BaseModelProperty;
import springfox.documentation.spi.schema.AlternateTypeProvider;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/field/FieldModelProperty.class */
public class FieldModelProperty extends BaseModelProperty {
    private final ResolvedField childField;

    public FieldModelProperty(String fieldName, ResolvedField childField, AlternateTypeProvider alternateTypeProvider) {
        super(fieldName, alternateTypeProvider);
        this.childField = childField;
    }

    @Override // springfox.documentation.schema.property.BaseModelProperty
    protected ResolvedType realType() {
        return this.childField.getType();
    }
}
