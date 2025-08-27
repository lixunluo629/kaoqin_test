package springfox.documentation.service;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import springfox.documentation.schema.ModelRef;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/Parameter.class */
public class Parameter {
    private final String name;
    private final String description;
    private final String defaultValue;
    private final Boolean required;
    private final Boolean allowMultiple;
    private final ModelRef modelRef;
    private final Optional<ResolvedType> type;
    private final AllowableValues allowableValues;
    private final String paramType;
    private final String paramAccess;

    public Parameter(String name, String description, String defaultValue, boolean required, boolean allowMultiple, ModelRef modelRef, Optional<ResolvedType> type, AllowableValues allowableValues, String paramType, String paramAccess) {
        this.description = description;
        this.defaultValue = defaultValue;
        this.required = Boolean.valueOf(required);
        this.allowMultiple = Boolean.valueOf(allowMultiple);
        this.modelRef = modelRef;
        this.type = type;
        this.allowableValues = allowableValues;
        this.paramType = paramType;
        this.paramAccess = paramAccess;
        this.name = name;
    }

    public Optional<ResolvedType> getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public Boolean isRequired() {
        return this.required;
    }

    public Boolean isAllowMultiple() {
        return this.allowMultiple;
    }

    public AllowableValues getAllowableValues() {
        return this.allowableValues;
    }

    public String getParamType() {
        return this.paramType;
    }

    public String getParamAccess() {
        return this.paramAccess;
    }

    public ModelRef getModelRef() {
        return this.modelRef;
    }
}
