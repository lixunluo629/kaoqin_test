package org.springframework.data.mapping.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/DefaultPersistentPropertyPath.class */
class DefaultPersistentPropertyPath<T extends PersistentProperty<T>> implements PersistentPropertyPath<T> {
    private final List<T> properties;

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/DefaultPersistentPropertyPath$PropertyNameConverter.class */
    private enum PropertyNameConverter implements Converter<PersistentProperty<?>, String> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public String convert(PersistentProperty<?> source) {
            return source.getName();
        }
    }

    public DefaultPersistentPropertyPath(List<T> properties) {
        Assert.notNull(properties, "Properties must not be null!");
        this.properties = properties;
    }

    public static <T extends PersistentProperty<T>> DefaultPersistentPropertyPath<T> empty() {
        return new DefaultPersistentPropertyPath<>(Collections.emptyList());
    }

    public DefaultPersistentPropertyPath<T> append(T property) {
        Assert.notNull(property, "Property must not be null!");
        if (isEmpty()) {
            return new DefaultPersistentPropertyPath<>(Arrays.asList(property));
        }
        Class<?> leafPropertyType = getLeafProperty().getActualType();
        Assert.isTrue(property.getOwner().getType().equals(leafPropertyType), String.format("Cannot append property %s to type %s!", property.getName(), leafPropertyType.getName()));
        List<T> properties = new ArrayList<>(this.properties);
        properties.add(property);
        return new DefaultPersistentPropertyPath<>(properties);
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public String toDotPath() {
        return toPath(null, null);
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public String toDotPath(Converter<? super T, String> converter) {
        return toPath(null, converter);
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public String toPath(String delimiter) {
        return toPath(delimiter, null);
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public String toPath(String str, Converter<? super T, String> converter) {
        Converter<? super T, String> converter2 = converter == null ? PropertyNameConverter.INSTANCE : converter;
        String str2 = str == null ? "." : str;
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = this.properties.iterator();
        while (it.hasNext()) {
            String strConvert = converter2.convert(it.next());
            if (StringUtils.hasText(strConvert)) {
                arrayList.add(strConvert);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return StringUtils.collectionToDelimitedString(arrayList, str2);
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public T getLeafProperty() {
        if (this.properties.isEmpty()) {
            return null;
        }
        return this.properties.get(this.properties.size() - 1);
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public T getBaseProperty() {
        if (this.properties.isEmpty()) {
            return null;
        }
        return this.properties.get(0);
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public boolean isBasePathOf(PersistentPropertyPath<T> path) {
        if (path == null) {
            return false;
        }
        Iterator<T> iterator = path.iterator();
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T property = it.next();
            if (!iterator.hasNext()) {
                return false;
            }
            T reference = iterator.next();
            if (!property.equals(reference)) {
                return false;
            }
        }
        return true;
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public PersistentPropertyPath<T> getExtensionForBaseOf(PersistentPropertyPath<T> base) {
        if (!base.isBasePathOf(this)) {
            return this;
        }
        List<T> result = new ArrayList<>();
        Iterator<T> iterator = iterator();
        for (int i = 0; i < base.getLength(); i++) {
            iterator.next();
        }
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return new DefaultPersistentPropertyPath(result);
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public PersistentPropertyPath<T> getParentPath() {
        int size = this.properties.size();
        if (size <= 1) {
            return this;
        }
        return new DefaultPersistentPropertyPath(this.properties.subList(0, size - 1));
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public int getLength() {
        return this.properties.size();
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return this.properties.iterator();
    }

    @Override // org.springframework.data.mapping.context.PersistentPropertyPath
    public boolean isEmpty() {
        return this.properties.isEmpty();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        DefaultPersistentPropertyPath<?> that = (DefaultPersistentPropertyPath) obj;
        return this.properties.equals(that.properties);
    }

    public int hashCode() {
        return this.properties.hashCode();
    }

    public String toString() {
        return toDotPath();
    }
}
