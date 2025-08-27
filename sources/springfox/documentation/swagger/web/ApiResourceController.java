package springfox.documentation.swagger.web;

import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.aspectj.weaver.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;

@ApiIgnore
@Controller
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/web/ApiResourceController.class */
public class ApiResourceController {

    @Value("${springfox.documentation.swagger.v1.path:/api-docs}")
    private String swagger1Url;

    @Value("${springfox.documentation.swagger.v2.path:/v2/api-docs}")
    private String swagger2Url;

    @Autowired
    private DocumentationCache documentationCache;

    @Autowired(required = false)
    private SecurityConfiguration securityConfiguration;

    @Autowired(required = false)
    private UiConfiguration uiConfiguration;
    private boolean swagger1Available = classByName("springfox.documentation.swagger1.web.Swagger1Controller").isPresent();
    private boolean swagger2Available = classByName("springfox.documentation.swagger2.web.Swagger2Controller").isPresent();

    @RequestMapping({"/configuration/security"})
    @ResponseBody
    ResponseEntity<SecurityConfiguration> securityConfiguration() {
        return new ResponseEntity<>(Optional.fromNullable(this.securityConfiguration).or((Optional) SecurityConfiguration.DEFAULT), HttpStatus.OK);
    }

    @RequestMapping({"/configuration/ui"})
    @ResponseBody
    ResponseEntity<UiConfiguration> uiConfiguration() {
        return new ResponseEntity<>(Optional.fromNullable(this.uiConfiguration).or((Optional) UiConfiguration.DEFAULT), HttpStatus.OK);
    }

    @RequestMapping({"/swagger-resources"})
    @ResponseBody
    ResponseEntity<List<SwaggerResource>> swaggerResources() {
        List<SwaggerResource> resources = new ArrayList<>();
        for (Map.Entry<String, Documentation> entry : this.documentationCache.all().entrySet()) {
            String swaggerGroup = entry.getKey();
            if (this.swagger1Available) {
                SwaggerResource swaggerResource = resource(swaggerGroup, this.swagger1Url);
                swaggerResource.setSwaggerVersion(Constants.RUNTIME_LEVEL_12);
                resources.add(swaggerResource);
            }
            if (this.swagger2Available) {
                SwaggerResource swaggerResource2 = resource(swaggerGroup, this.swagger2Url);
                swaggerResource2.setSwaggerVersion("2.0");
                resources.add(swaggerResource2);
            }
        }
        Collections.sort(resources);
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    private SwaggerResource resource(String swaggerGroup, String baseUrl) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(swaggerGroup);
        swaggerResource.setLocation(swaggerLocation(baseUrl, swaggerGroup));
        return swaggerResource;
    }

    private String swaggerLocation(String swaggerUrl, String swaggerGroup) {
        String base = (String) Optional.of(swaggerUrl).get();
        if ("default".equals(swaggerGroup)) {
            return base;
        }
        return base + "?group=" + swaggerGroup;
    }

    private Optional<? extends Class> classByName(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.absent();
        }
    }
}
