package springfox.documentation.builders;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.Parameter;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/ParameterBuilder.class */
public class ParameterBuilder {
    private String name;
    private String description;
    private String defaultValue;
    private boolean required;
    private boolean allowMultiple;
    private AllowableValues allowableValues;
    private String paramType;
    private String paramAccess;
    private ResolvedType type;
    private ModelRef modelRef;

    ParameterBuilder from(Parameter other) {
        return name(other.getName()).allowableValues(other.getAllowableValues()).allowMultiple(other.isAllowMultiple().booleanValue()).defaultValue(other.getDefaultValue()).description(other.getDescription()).modelRef(other.getModelRef()).parameterAccess(other.getParamAccess()).parameterType(other.getParamType()).required(other.isRequired().booleanValue()).type(other.getType().orNull());
    }

    public ParameterBuilder name(String name) {
        this.name = (String) BuilderDefaults.defaultIfAbsent(name, this.name);
        return this;
    }

    public ParameterBuilder description(String description) {
        this.description = (String) BuilderDefaults.defaultIfAbsent(description, this.description);
        return this;
    }

    public ParameterBuilder defaultValue(String defaultValue) {
        this.defaultValue = (String) BuilderDefaults.defaultIfAbsent(defaultValue, this.defaultValue);
        return this;
    }

    public ParameterBuilder required(boolean required) {
        this.required = required;
        return this;
    }

    public ParameterBuilder allowMultiple(boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
        return this;
    }

    public ParameterBuilder allowableValues(AllowableValues allowableValues) {
        this.allowableValues = (AllowableValues) BuilderDefaults.defaultIfAbsent(allowableValues, this.allowableValues);
        return this;
    }

    public ParameterBuilder parameterType(String paramType) {
        this.paramType = (String) BuilderDefaults.defaultIfAbsent(paramType, this.paramType);
        return this;
    }

    public ParameterBuilder parameterAccess(String paramAccess) {
        this.paramAccess = (String) BuilderDefaults.defaultIfAbsent(paramAccess, this.paramAccess);
        return this;
    }

    public ParameterBuilder type(ResolvedType type) {
        this.type = (ResolvedType) BuilderDefaults.defaultIfAbsent(type, this.type);
        return this;
    }

    public ParameterBuilder modelRef(ModelRef modelRef) {
        this.modelRef = (ModelRef) BuilderDefaults.defaultIfAbsent(modelRef, this.modelRef);
        return this;
    }

    public Parameter build() {
        return new Parameter(this.name, this.description, this.defaultValue, this.required, this.allowMultiple, this.modelRef, Optional.fromNullable(this.type), this.allowableValues, this.paramType, this.paramAccess);
    }
}
