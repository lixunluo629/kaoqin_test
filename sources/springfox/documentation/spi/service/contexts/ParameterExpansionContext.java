package springfox.documentation.spi.service.contexts;

import java.lang.reflect.Field;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.spi.DocumentationType;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/ParameterExpansionContext.class */
public class ParameterExpansionContext {
    private final String dataTypeName;
    private final String parentName;
    private final Field field;
    private final DocumentationType documentationType;
    private ParameterBuilder parameterBuilder;

    public ParameterExpansionContext(String dataTypeName, String parentName, Field field, DocumentationType documentationType, ParameterBuilder parameterBuilder) {
        this.dataTypeName = dataTypeName;
        this.parentName = parentName;
        this.field = field;
        this.documentationType = documentationType;
        this.parameterBuilder = parameterBuilder;
    }

    public String getDataTypeName() {
        return this.dataTypeName;
    }

    public String getParentName() {
        return this.parentName;
    }

    public Field getField() {
        return this.field;
    }

    public DocumentationType getDocumentationType() {
        return this.documentationType;
    }

    public ParameterBuilder getParameterBuilder() {
        return this.parameterBuilder;
    }
}
