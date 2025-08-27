package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Function;
import springfox.documentation.service.AllowableValues;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/schema/ModelProperty.class */
public class ModelProperty {
    private final String name;
    private final ResolvedType type;
    private final String qualifiedType;
    private final int position;
    private final Boolean required;
    private final boolean isHidden;
    private final Boolean readOnly;
    private final String description;
    private final AllowableValues allowableValues;
    private ModelRef modelRef;

    public ModelProperty(String name, ResolvedType type, String qualifiedType, int position, Boolean required, Boolean isHidden, Boolean readOnly, String description, AllowableValues allowableValues) {
        this.name = name;
        this.type = type;
        this.qualifiedType = qualifiedType;
        this.position = position;
        this.required = required;
        this.isHidden = isHidden.booleanValue();
        this.readOnly = readOnly;
        this.description = description;
        this.allowableValues = allowableValues;
    }

    public String getName() {
        return this.name;
    }

    public ResolvedType getType() {
        return this.type;
    }

    public String getQualifiedType() {
        return this.qualifiedType;
    }

    public int getPosition() {
        return this.position;
    }

    public Boolean isRequired() {
        return this.required;
    }

    public Boolean isReadOnly() {
        return this.readOnly;
    }

    public String getDescription() {
        return this.description;
    }

    public AllowableValues getAllowableValues() {
        return this.allowableValues;
    }

    public ModelRef getModelRef() {
        return this.modelRef;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public ModelProperty updateModelRef(Function<? super ResolvedType, ModelRef> modelRefFactory) {
        this.modelRef = modelRefFactory.apply(this.type);
        return this;
    }
}
