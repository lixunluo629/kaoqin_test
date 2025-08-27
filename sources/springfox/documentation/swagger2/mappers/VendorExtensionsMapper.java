package springfox.documentation.swagger2.mappers;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import springfox.documentation.service.ObjectVendorExtension;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;

@Mapper
/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/VendorExtensionsMapper.class */
public class VendorExtensionsMapper {
    public Map<String, Object> mapExtensions(List<VendorExtension> from) {
        Map<String, Object> extensions = Maps.newHashMap();
        Iterable<Map<String, Object>> objectExtensions = FluentIterable.from(from).filter(ObjectVendorExtension.class).transform(toExtensionMap());
        Iterator<Map<String, Object>> it = objectExtensions.iterator();
        while (it.hasNext()) {
            extensions.putAll(it.next());
        }
        Iterable<StringVendorExtension> propertyExtensions = FluentIterable.from(from).filter(StringVendorExtension.class);
        for (StringVendorExtension each : propertyExtensions) {
            extensions.put(each.getName(), each.getValue());
        }
        return extensions;
    }

    private Function<ObjectVendorExtension, Map<String, Object>> toExtensionMap() {
        return new Function<ObjectVendorExtension, Map<String, Object>>() { // from class: springfox.documentation.swagger2.mappers.VendorExtensionsMapper.1
            @Override // com.google.common.base.Function
            public Map<String, Object> apply(ObjectVendorExtension input) {
                if (Strings.isNullOrEmpty(input.getName())) {
                    return VendorExtensionsMapper.this.propertiesAsMap(input);
                }
                Map<String, Object> map = Maps.newHashMap();
                map.put(input.getName(), VendorExtensionsMapper.this.mapExtensions(input.getValue()));
                return map;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, Object> propertiesAsMap(ObjectVendorExtension input) {
        Map<String, Object> properties = Maps.newHashMap();
        Iterable<StringVendorExtension> stringExtensions = FluentIterable.from(input.getValue()).filter(StringVendorExtension.class);
        for (StringVendorExtension property : stringExtensions) {
            properties.put(property.getName(), property.getValue());
        }
        Iterable<ObjectVendorExtension> objectExtensions = FluentIterable.from(input.getValue()).filter(ObjectVendorExtension.class);
        for (ObjectVendorExtension property2 : objectExtensions) {
            properties.put(property2.getName(), mapExtensions(property2.getValue()));
        }
        return properties;
    }
}
