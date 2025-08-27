package com.moredian.onpremise.frontend.config;

import io.swagger.annotations.Api;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/config/SwaggerConfig.class */
public class SwaggerConfig {

    @Value("${onpremise.current.version}")
    private String currentVersion;

    @Value("${onpremise.swagger.enable}")
    private String swaggerEnable;

    @Bean
    public Docket createRestApi() {
        Boolean enableFlag = BooleanUtils.toBooleanObject(this.swaggerEnable);
        Docket doc = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.withClassAnnotation(Api.class)).paths(PathSelectors.any()).build().enable(enableFlag == null ? true : enableFlag.booleanValue());
        return doc;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("on-premise restful APIs").description("on-premise API文档").contact("moredian").version(this.currentVersion).build();
    }
}
