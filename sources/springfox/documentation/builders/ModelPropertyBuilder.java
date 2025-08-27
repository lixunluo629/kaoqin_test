package springfox.documentation.builders;

import com.fasterxml.classmate.ResolvedType;
import springfox.documentation.schema.Enums;
import springfox.documentation.schema.ModelProperty;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/ModelPropertyBuilder.class */
public class ModelPropertyBuilder {
    private ResolvedType type;
    private String qualifiedType;
    private int position;
    private Boolean required;
    private Boolean readOnly;
    private String description;
    private AllowableValues allowableValues;
    private String name;
    private boolean isHidden;

    public ModelPropertyBuilder name(String name) {
        this.name = (String) BuilderDefaults.defaultIfAbsent(name, this.name);
        return this;
    }

    public ModelPropertyBuilder type(ResolvedType type) {
        this.type = BuilderDefaults.replaceIfMoreSpecific(type, this.type);
        return this;
    }

    public ModelPropertyBuilder qualifiedType(String qualifiedType) {
        this.qualifiedType = (String) BuilderDefaults.defaultIfAbsent(qualifiedType, this.qualifiedType);
        return this;
    }

    public ModelPropertyBuilder position(int position) {
        this.position = position;
        return this;
    }

    public ModelPropertyBuilder required(Boolean required) {
        this.required = required;
        return this;
    }

    public ModelPropertyBuilder readOnly(Boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public ModelPropertyBuilder description(String description) {
        this.description = (String) BuilderDefaults.defaultIfAbsent(description, this.description);
        return this;
    }

    public ModelPropertyBuilder allowableValues(AllowableValues allowableValues) {
        if (allowableValues != null) {
            if (allowableValues instanceof AllowableListValues) {
                this.allowableValues = (AllowableValues) BuilderDefaults.defaultIfAbsent(Enums.emptyListValuesToNull((AllowableListValues) allowableValues), this.allowableValues);
            } else {
                this.allowableValues = (AllowableValues) BuilderDefaults.defaultIfAbsent(allowableValues, this.allowableValues);
            }
        }
        return this;
    }

    public ModelPropertyBuilder isHidden(Boolean isHidden) {
        this.isHidden = isHidden.booleanValue();
        return this;
    }

    public ModelProperty build() {
        return new ModelProperty(this.name, this.type, this.qualifiedType, this.position, this.required, Boolean.valueOf(this.isHidden), this.readOnly, this.description, this.allowableValues);
    }
}
