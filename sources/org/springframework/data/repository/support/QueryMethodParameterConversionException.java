package org.springframework.data.repository.support;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionException;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/QueryMethodParameterConversionException.class */
public class QueryMethodParameterConversionException extends RuntimeException {
    private static final long serialVersionUID = -5818002272039533066L;
    private final Object source;
    private final MethodParameter parameter;

    public QueryMethodParameterConversionException(Object source, MethodParameter parameter, ConversionException cause) {
        super(String.format("Failed to convert %s into %s!", source, parameter.getParameterType().getName()), cause);
        Assert.notNull(parameter, "Method parameter must not be null!");
        Assert.notNull(cause, "ConversionException must not be null!");
        this.parameter = parameter;
        this.source = source;
    }

    public Object getSource() {
        return this.source;
    }

    public MethodParameter getParameter() {
        return this.parameter;
    }
}
