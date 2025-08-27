package springfox.documentation.schema;

import springfox.documentation.spi.schema.GenericTypeNamingStrategy;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/DefaultGenericTypeNamingStrategy.class */
public class DefaultGenericTypeNamingStrategy implements GenericTypeNamingStrategy {
    private static final String OPEN = "«";
    private static final String CLOSE = "»";
    private static final String DELIM = ",";

    @Override // springfox.documentation.spi.schema.GenericTypeNamingStrategy
    public String getOpenGeneric() {
        return OPEN;
    }

    @Override // springfox.documentation.spi.schema.GenericTypeNamingStrategy
    public String getCloseGeneric() {
        return CLOSE;
    }

    @Override // springfox.documentation.spi.schema.GenericTypeNamingStrategy
    public String getTypeListDelimiter() {
        return ",";
    }
}
