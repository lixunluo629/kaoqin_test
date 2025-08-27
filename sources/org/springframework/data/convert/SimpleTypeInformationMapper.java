package org.springframework.data.convert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/SimpleTypeInformationMapper.class */
public class SimpleTypeInformationMapper implements TypeInformationMapper {
    private final Map<String, CachedTypeInformation> CACHE = new ConcurrentHashMap();

    @Override // org.springframework.data.convert.TypeInformationMapper
    public /* bridge */ /* synthetic */ Object createAliasFor(TypeInformation typeInformation) {
        return createAliasFor((TypeInformation<?>) typeInformation);
    }

    @Override // org.springframework.data.convert.TypeInformationMapper
    public ClassTypeInformation<?> resolveTypeFrom(Object alias) {
        if (!(alias instanceof String)) {
            return null;
        }
        String value = (String) alias;
        if (!StringUtils.hasText(value)) {
            return null;
        }
        CachedTypeInformation cachedValue = this.CACHE.get(value);
        if (cachedValue != null) {
            return cachedValue.getType();
        }
        try {
            return cacheAndReturn(value, ClassTypeInformation.from(ClassUtils.forName(value, null)));
        } catch (ClassNotFoundException e) {
            return cacheAndReturn(value, null);
        }
    }

    private ClassTypeInformation<?> cacheAndReturn(String value, ClassTypeInformation<?> type) {
        this.CACHE.put(value, CachedTypeInformation.of(type));
        return type;
    }

    @Override // org.springframework.data.convert.TypeInformationMapper
    public String createAliasFor(TypeInformation<?> type) {
        return type.getType().getName();
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/SimpleTypeInformationMapper$CachedTypeInformation.class */
    static final class CachedTypeInformation {
        private final ClassTypeInformation<?> type;

        @Generated
        private CachedTypeInformation(ClassTypeInformation<?> type) {
            this.type = type;
        }

        @Generated
        public static CachedTypeInformation of(ClassTypeInformation<?> type) {
            return new CachedTypeInformation(type);
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof CachedTypeInformation)) {
                return false;
            }
            CachedTypeInformation other = (CachedTypeInformation) o;
            Object this$type = getType();
            Object other$type = other.getType();
            return this$type == null ? other$type == null : this$type.equals(other$type);
        }

        @Generated
        public int hashCode() {
            Object $type = getType();
            int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "SimpleTypeInformationMapper.CachedTypeInformation(type=" + getType() + ")";
        }

        @Generated
        public ClassTypeInformation<?> getType() {
            return this.type;
        }
    }
}
