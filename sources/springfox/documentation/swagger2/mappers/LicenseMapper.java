package springfox.documentation.swagger2.mappers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;
import springfox.documentation.service.ApiInfo;

@Mapper
/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/LicenseMapper.class */
public class LicenseMapper {

    @Target({ElementType.METHOD})
    @Qualifier
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/LicenseMapper$License.class */
    public @interface License {
    }

    @Target({ElementType.TYPE})
    @Qualifier
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/LicenseMapper$LicenseTranslator.class */
    public @interface LicenseTranslator {
    }

    public io.swagger.models.License apiInfoToLicense(ApiInfo from) {
        return new io.swagger.models.License().name(from.getLicense()).url(from.getLicenseUrl());
    }
}
