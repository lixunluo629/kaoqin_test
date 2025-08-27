package org.springframework.data.repository.query;

import java.lang.reflect.Method;
import java.util.List;
import org.springframework.core.MethodParameter;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/DefaultParameters.class */
public final class DefaultParameters extends Parameters<DefaultParameters, Parameter> {
    @Override // org.springframework.data.repository.query.Parameters
    protected /* bridge */ /* synthetic */ Parameters createFrom(List list) {
        return createFrom((List<Parameter>) list);
    }

    public DefaultParameters(Method method) {
        super(method);
    }

    private DefaultParameters(List<Parameter> parameters) {
        super(parameters);
    }

    @Override // org.springframework.data.repository.query.Parameters
    protected Parameter createParameter(MethodParameter parameter) {
        return new Parameter(parameter);
    }

    @Override // org.springframework.data.repository.query.Parameters
    protected DefaultParameters createFrom(List<Parameter> parameters) {
        return new DefaultParameters(parameters);
    }
}
