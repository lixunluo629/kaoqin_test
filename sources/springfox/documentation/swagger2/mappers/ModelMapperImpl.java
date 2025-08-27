package springfox.documentation.swagger2.mappers;

import io.swagger.models.properties.Property;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelProperty;

@Component
/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/ModelMapperImpl.class */
public class ModelMapperImpl extends ModelMapper {
    @Override // springfox.documentation.swagger2.mappers.ModelMapper
    protected Map<String, Property> mapProperties(Map<String, ModelProperty> properties) {
        if (properties == null) {
            return null;
        }
        Map<String, Property> map = new HashMap<>();
        for (Map.Entry<String, ModelProperty> entry : properties.entrySet()) {
            String key = entry.getKey();
            Property value = mapProperty(entry.getValue());
            map.put(key, value);
        }
        return map;
    }
}
