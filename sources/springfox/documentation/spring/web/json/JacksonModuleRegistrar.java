package springfox.documentation.spring.web.json;

import com.fasterxml.jackson.databind.ObjectMapper;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/json/JacksonModuleRegistrar.class */
public interface JacksonModuleRegistrar {
    void maybeRegisterModule(ObjectMapper objectMapper);
}
