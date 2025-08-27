package springfox.documentation.swagger2.web;

import com.google.common.base.Optional;
import io.swagger.models.Swagger;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

@ApiIgnore
@Controller
/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/web/Swagger2Controller.class */
public class Swagger2Controller {
    public static final String DEFAULT_URL = "/v2/api-docs";

    @Value("${springfox.documentation.swagger.v2.host:DEFAULT}")
    private String hostNameOverride;

    @Autowired
    private DocumentationCache documentationCache;

    @Autowired
    private ServiceModelToSwagger2Mapper mapper;

    @Autowired
    private JsonSerializer jsonSerializer;

    @RequestMapping(value = {"${springfox.documentation.swagger.v2.path:/v2/api-docs}"}, method = {RequestMethod.GET})
    @ApiIgnore
    @ResponseBody
    public ResponseEntity<Json> getDocumentation(@RequestParam(value = "group", required = false) String swaggerGroup) {
        String groupName = (String) Optional.fromNullable(swaggerGroup).or((Optional) "default");
        Documentation documentation = this.documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Swagger swagger = this.mapper.mapDocumentation(documentation);
        swagger.host(hostName());
        return new ResponseEntity<>(this.jsonSerializer.toJson(swagger), HttpStatus.OK);
    }

    private String hostName() {
        if ("DEFAULT".equals(this.hostNameOverride)) {
            URI uri = ControllerLinkBuilder.linkTo((Class<?>) Swagger2Controller.class).toUri();
            String host = uri.getHost();
            int port = uri.getPort();
            if (port > -1) {
                return String.format("%s:%d", host, Integer.valueOf(port));
            }
            return host;
        }
        return this.hostNameOverride;
    }
}
