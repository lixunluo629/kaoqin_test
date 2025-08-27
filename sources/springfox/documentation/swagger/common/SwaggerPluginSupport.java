package springfox.documentation.swagger.common;

import springfox.documentation.spi.DocumentationType;

/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/common/SwaggerPluginSupport.class */
public class SwaggerPluginSupport {
    public static final int SWAGGER_PLUGIN_ORDER = -2147482648;

    private SwaggerPluginSupport() {
        throw new UnsupportedOperationException();
    }

    public static boolean pluginDoesApply(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_12.equals(documentationType) || DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
