package springfox.documentation.schema;

import springfox.documentation.spi.schema.GenericTypeNamingStrategy;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/CodeGenGenericTypeNamingStrategy.class */
public class CodeGenGenericTypeNamingStrategy implements GenericTypeNamingStrategy {
    private static final String OPEN = "Of";
    private static final String CLOSE = "";
    private static final String DELIM = "And";

    @Override // springfox.documentation.spi.schema.GenericTypeNamingStrategy
    public String getOpenGeneric() {
        return OPEN;
    }

    @Override // springfox.documentation.spi.schema.GenericTypeNamingStrategy
    public String getCloseGeneric() {
        return "";
    }

    @Override // springfox.documentation.spi.schema.GenericTypeNamingStrategy
    public String getTypeListDelimiter() {
        return DELIM;
    }
}
