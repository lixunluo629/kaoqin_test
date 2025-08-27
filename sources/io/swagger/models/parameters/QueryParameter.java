package io.swagger.models.parameters;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/parameters/QueryParameter.class */
public class QueryParameter extends AbstractSerializableParameter<QueryParameter> {
    public QueryParameter() {
        super.setIn("query");
    }

    @Override // io.swagger.models.parameters.AbstractSerializableParameter
    protected String getDefaultCollectionFormat() {
        return "multi";
    }
}
