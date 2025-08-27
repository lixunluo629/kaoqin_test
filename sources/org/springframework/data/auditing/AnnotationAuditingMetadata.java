package org.springframework.data.auditing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.convert.ThreeTenBackPortConverters;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/AnnotationAuditingMetadata.class */
final class AnnotationAuditingMetadata {
    private static final ReflectionUtils.AnnotationFieldFilter CREATED_BY_FILTER = new ReflectionUtils.AnnotationFieldFilter(CreatedBy.class);
    private static final ReflectionUtils.AnnotationFieldFilter CREATED_DATE_FILTER = new ReflectionUtils.AnnotationFieldFilter(CreatedDate.class);
    private static final ReflectionUtils.AnnotationFieldFilter LAST_MODIFIED_BY_FILTER = new ReflectionUtils.AnnotationFieldFilter(LastModifiedBy.class);
    private static final ReflectionUtils.AnnotationFieldFilter LAST_MODIFIED_DATE_FILTER = new ReflectionUtils.AnnotationFieldFilter(LastModifiedDate.class);
    private static final Map<Class<?>, AnnotationAuditingMetadata> METADATA_CACHE = new ConcurrentHashMap();
    public static final boolean IS_JDK_8 = ClassUtils.isPresent("java.time.Clock", AnnotationAuditingMetadata.class.getClassLoader());
    static final List<String> SUPPORTED_DATE_TYPES;
    private final Field createdByField;
    private final Field createdDateField;
    private final Field lastModifiedByField;
    private final Field lastModifiedDateField;

    static {
        List<String> types = new ArrayList<>(5);
        types.add("org.joda.time.DateTime");
        types.add("org.joda.time.LocalDateTime");
        types.add(Date.class.getName());
        types.add(Long.class.getName());
        types.add(Long.TYPE.getName());
        SUPPORTED_DATE_TYPES = Collections.unmodifiableList(types);
    }

    private AnnotationAuditingMetadata(Class<?> type) {
        Assert.notNull(type, "Given type must not be null!");
        this.createdByField = ReflectionUtils.findField(type, (ReflectionUtils.DescribedFieldFilter) CREATED_BY_FILTER);
        this.createdDateField = ReflectionUtils.findField(type, (ReflectionUtils.DescribedFieldFilter) CREATED_DATE_FILTER);
        this.lastModifiedByField = ReflectionUtils.findField(type, (ReflectionUtils.DescribedFieldFilter) LAST_MODIFIED_BY_FILTER);
        this.lastModifiedDateField = ReflectionUtils.findField(type, (ReflectionUtils.DescribedFieldFilter) LAST_MODIFIED_DATE_FILTER);
        assertValidDateFieldType(this.createdDateField);
        assertValidDateFieldType(this.lastModifiedDateField);
    }

    private void assertValidDateFieldType(Field field) {
        if (field == null || SUPPORTED_DATE_TYPES.contains(field.getType().getName())) {
            return;
        }
        Class<?> type = field.getType();
        if (Jsr310Converters.supports(type) || ThreeTenBackPortConverters.supports(type)) {
        } else {
            throw new IllegalStateException(String.format("Found created/modified date field with type %s but only %s as well as java.time types are supported!", type, SUPPORTED_DATE_TYPES));
        }
    }

    public static AnnotationAuditingMetadata getMetadata(Class<?> type) {
        if (METADATA_CACHE.containsKey(type)) {
            return METADATA_CACHE.get(type);
        }
        AnnotationAuditingMetadata metadata = new AnnotationAuditingMetadata(type);
        METADATA_CACHE.put(type, metadata);
        return metadata;
    }

    public boolean isAuditable() {
        if (this.createdByField == null && this.createdDateField == null && this.lastModifiedByField == null && this.lastModifiedDateField == null) {
            return false;
        }
        return true;
    }

    public Field getCreatedByField() {
        return this.createdByField;
    }

    public Field getCreatedDateField() {
        return this.createdDateField;
    }

    public Field getLastModifiedByField() {
        return this.lastModifiedByField;
    }

    public Field getLastModifiedDateField() {
        return this.lastModifiedDateField;
    }
}
