package org.springframework.data.convert;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ConfigurableTypeInformationMapper.class */
public class ConfigurableTypeInformationMapper implements TypeInformationMapper {
    private final Map<ClassTypeInformation<?>, Object> typeMap;

    public ConfigurableTypeInformationMapper(Map<? extends Class<?>, String> sourceTypeMap) {
        Assert.notNull(sourceTypeMap, "SourceTypeMap must not be null!");
        this.typeMap = new HashMap(sourceTypeMap.size());
        for (Map.Entry<? extends Class<?>, String> entry : sourceTypeMap.entrySet()) {
            ClassTypeInformation<?> key = ClassTypeInformation.from(entry.getKey());
            String value = entry.getValue();
            if (this.typeMap.containsValue(value)) {
                throw new IllegalArgumentException(String.format("Detected mapping ambiguity! String %s cannot be mapped to more than one type!", value));
            }
            this.typeMap.put(key, value);
        }
    }

    @Override // org.springframework.data.convert.TypeInformationMapper
    public Object createAliasFor(TypeInformation<?> type) {
        return this.typeMap.get(type);
    }

    @Override // org.springframework.data.convert.TypeInformationMapper
    public ClassTypeInformation<?> resolveTypeFrom(Object alias) {
        if (alias == null) {
            return null;
        }
        for (Map.Entry<ClassTypeInformation<?>, Object> entry : this.typeMap.entrySet()) {
            if (entry.getValue().equals(alias)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
