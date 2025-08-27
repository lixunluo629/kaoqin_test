package springfox.documentation.spi.schema;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/schema/GenericTypeNamingStrategy.class */
public interface GenericTypeNamingStrategy {
    String getOpenGeneric();

    String getCloseGeneric();

    String getTypeListDelimiter();
}
