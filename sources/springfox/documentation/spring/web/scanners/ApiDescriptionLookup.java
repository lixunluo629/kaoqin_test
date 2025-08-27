package springfox.documentation.spring.web.scanners;

import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.stereotype.Component;
import springfox.documentation.annotations.Incubating;
import springfox.documentation.service.ApiDescription;

@Incubating("2.2.0")
@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/scanners/ApiDescriptionLookup.class */
public class ApiDescriptionLookup {
    private Map<Method, ApiDescription> cache = Maps.newHashMap();

    public void add(Method key, ApiDescription value) {
        this.cache.put(key, value);
    }

    public ApiDescription description(Method key) {
        return this.cache.get(key);
    }
}
