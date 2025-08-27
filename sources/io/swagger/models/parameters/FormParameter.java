package io.swagger.models.parameters;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/parameters/FormParameter.class */
public class FormParameter extends AbstractSerializableParameter<FormParameter> {
    public FormParameter() {
        super.setIn("formData");
    }

    @Override // io.swagger.models.parameters.AbstractSerializableParameter
    protected String getDefaultCollectionFormat() {
        return "multi";
    }
}
