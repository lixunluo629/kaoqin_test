package io.swagger.models.parameters;

import org.apache.commons.httpclient.cookie.Cookie2;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/parameters/PathParameter.class */
public class PathParameter extends AbstractSerializableParameter<PathParameter> {
    public PathParameter() {
        super.setIn(Cookie2.PATH);
        super.setRequired(true);
    }
}
