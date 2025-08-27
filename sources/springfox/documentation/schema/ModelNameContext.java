package springfox.documentation.schema;

import springfox.documentation.spi.DocumentationType;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/ModelNameContext.class */
public class ModelNameContext {
    private final Class<?> type;
    private final DocumentationType documentationType;

    public ModelNameContext(Class<?> type, DocumentationType documentationType) {
        this.type = type;
        this.documentationType = documentationType;
    }

    public Class<?> getType() {
        return this.type;
    }

    public DocumentationType getDocumentationType() {
        return this.documentationType;
    }
}
