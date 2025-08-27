package springfox.documentation.swagger2.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.models.Contact;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.Xml;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;

/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/configuration/Swagger2JacksonModule.class */
public class Swagger2JacksonModule extends SimpleModule implements JacksonModuleRegistrar {
    @Override // springfox.documentation.spring.web.json.JacksonModuleRegistrar
    public void maybeRegisterModule(ObjectMapper objectMapper) {
        if (objectMapper.findMixInClassFor(Swagger.class) == null) {
            objectMapper.registerModule(new Swagger2JacksonModule());
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
    }

    @Override // com.fasterxml.jackson.databind.module.SimpleModule, com.fasterxml.jackson.databind.Module
    public void setupModule(Module.SetupContext context) {
        super.setupModule(context);
        context.setMixInAnnotations(Swagger.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Info.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(License.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Scheme.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(SecurityRequirement.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(SecuritySchemeDefinition.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Model.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Property.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Operation.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Path.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Response.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Parameter.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(ExternalDocs.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Xml.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Tag.class, CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(Contact.class, CustomizedSwaggerSerializer.class);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonAutoDetect
    /* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/configuration/Swagger2JacksonModule$CustomizedSwaggerSerializer.class */
    private class CustomizedSwaggerSerializer {
        private CustomizedSwaggerSerializer() {
        }
    }
}
