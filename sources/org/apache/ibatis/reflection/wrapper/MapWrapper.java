package org.apache.ibatis.reflection.wrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/wrapper/MapWrapper.class */
public class MapWrapper extends BaseWrapper {
    private final Map<String, Object> map;

    public MapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject);
        this.map = map;
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public Object get(PropertyTokenizer prop) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, this.map);
            return getCollectionValue(prop, collection);
        }
        return this.map.get(prop.getName());
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public void set(PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, this.map);
            setCollectionValue(prop, collection, value);
        } else {
            this.map.put(prop.getName(), value);
        }
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return name;
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public String[] getGetterNames() {
        return (String[]) this.map.keySet().toArray(new String[this.map.keySet().size()]);
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public String[] getSetterNames() {
        return (String[]) this.map.keySet().toArray(new String[this.map.keySet().size()]);
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                return Object.class;
            }
            return metaValue.getSetterType(prop.getChildren());
        }
        if (this.map.get(name) != null) {
            return this.map.get(name).getClass();
        }
        return Object.class;
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                return Object.class;
            }
            return metaValue.getGetterType(prop.getChildren());
        }
        if (this.map.get(name) != null) {
            return this.map.get(name).getClass();
        }
        return Object.class;
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public boolean hasSetter(String name) {
        return true;
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.map.containsKey(prop.getIndexedName())) {
                MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
                if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                    return true;
                }
                return metaValue.hasGetter(prop.getChildren());
            }
            return false;
        }
        return this.map.containsKey(prop.getName());
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        HashMap<String, Object> map = new HashMap<>();
        set(prop, map);
        return MetaObject.forObject(map, this.metaObject.getObjectFactory(), this.metaObject.getObjectWrapperFactory(), this.metaObject.getReflectorFactory());
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public boolean isCollection() {
        return false;
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public void add(Object element) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.ibatis.reflection.wrapper.ObjectWrapper
    public <E> void addAll(List<E> element) {
        throw new UnsupportedOperationException();
    }
}
