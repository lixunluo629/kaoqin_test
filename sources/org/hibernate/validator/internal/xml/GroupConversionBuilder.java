package org.hibernate.validator.internal.xml;

import java.util.List;
import java.util.Map;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/GroupConversionBuilder.class */
class GroupConversionBuilder {
    private final ClassLoadingHelper classLoadingHelper;

    GroupConversionBuilder(ClassLoadingHelper classLoadingHelper) {
        this.classLoadingHelper = classLoadingHelper;
    }

    Map<Class<?>, Class<?>> buildGroupConversionMap(List<GroupConversionType> groupConversionTypes, String defaultPackage) {
        Map<Class<?>, Class<?>> groupConversionMap = CollectionHelper.newHashMap();
        for (GroupConversionType groupConversionType : groupConversionTypes) {
            Class<?> fromClass = this.classLoadingHelper.loadClass(groupConversionType.getFrom(), defaultPackage);
            Class<?> toClass = this.classLoadingHelper.loadClass(groupConversionType.getTo(), defaultPackage);
            groupConversionMap.put(fromClass, toClass);
        }
        return groupConversionMap;
    }
}
