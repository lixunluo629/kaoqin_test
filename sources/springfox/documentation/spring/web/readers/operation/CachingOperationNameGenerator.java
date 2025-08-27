package springfox.documentation.spring.web.readers.operation;

import com.google.common.collect.Maps;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import springfox.documentation.OperationNameGenerator;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/CachingOperationNameGenerator.class */
public class CachingOperationNameGenerator implements OperationNameGenerator {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) CachingOperationNameGenerator.class);
    private Map<String, Integer> generated = Maps.newHashMap();

    @Override // springfox.documentation.OperationNameGenerator
    public String startingWith(String prefix) {
        if (this.generated.containsKey(prefix)) {
            this.generated.put(prefix, Integer.valueOf(this.generated.get(prefix).intValue() + 1));
            String nextUniqueOperationName = String.format("%s_%s", prefix, this.generated.get(prefix));
            LOG.info("Generating unique operation named: {}", nextUniqueOperationName);
            return nextUniqueOperationName;
        }
        this.generated.put(prefix, 0);
        return prefix;
    }
}
