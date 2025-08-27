package org.springframework.data.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/PreferredConstructor.class */
public class PreferredConstructor<T, P extends PersistentProperty<P>> {
    private final Constructor<T> constructor;
    private final List<Parameter<Object, P>> parameters;
    private final Map<PersistentProperty<?>, Boolean> isPropertyParameterCache = new HashMap();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock read = this.lock.readLock();
    private final Lock write = this.lock.writeLock();

    public PreferredConstructor(Constructor<T> constructor, Parameter<Object, P>... parameters) {
        Assert.notNull(constructor, "Constructor must not be null!");
        Assert.notNull(parameters, "Parameters must not be null!");
        ReflectionUtils.makeAccessible((Constructor<?>) constructor);
        this.constructor = constructor;
        this.parameters = Arrays.asList(parameters);
    }

    public Constructor<T> getConstructor() {
        return this.constructor;
    }

    public Iterable<Parameter<Object, P>> getParameters() {
        return this.parameters;
    }

    public boolean hasParameters() {
        return !this.parameters.isEmpty();
    }

    public boolean isNoArgConstructor() {
        return this.parameters.isEmpty();
    }

    public boolean isExplicitlyAnnotated() {
        return this.constructor.isAnnotationPresent(PersistenceConstructor.class);
    }

    public boolean isConstructorParameter(PersistentProperty<?> property) {
        Assert.notNull(property, "Property must not be null!");
        try {
            this.read.lock();
            Boolean cached = this.isPropertyParameterCache.get(property);
            if (cached != null) {
                boolean zBooleanValue = cached.booleanValue();
                this.read.unlock();
                return zBooleanValue;
            }
            this.read.unlock();
            try {
                this.write.lock();
                for (Parameter<?, P> parameter : this.parameters) {
                    if (parameter.maps(property)) {
                        this.isPropertyParameterCache.put(property, true);
                        this.write.unlock();
                        return true;
                    }
                }
                this.isPropertyParameterCache.put(property, false);
                this.write.unlock();
                return false;
            } catch (Throwable th) {
                this.write.unlock();
                throw th;
            }
        } catch (Throwable th2) {
            this.read.unlock();
            throw th2;
        }
    }

    public boolean isEnclosingClassParameter(Parameter<?, P> parameter) {
        Assert.notNull(parameter, "Parameter must not be null!");
        if (this.parameters.isEmpty() || !parameter.isEnclosingClassParameter()) {
            return false;
        }
        return this.parameters.get(0).equals(parameter);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/PreferredConstructor$Parameter.class */
    public static class Parameter<T, P extends PersistentProperty<P>> {
        private final String name;
        private final TypeInformation<T> type;
        private final String key;
        private final PersistentEntity<T, P> entity;
        private Boolean enclosingClassCache;
        private Boolean hasSpelExpression;

        public Parameter(String name, TypeInformation<T> type, Annotation[] annotations, PersistentEntity<T, P> entity) {
            Assert.notNull(type, "Type must not be null!");
            Assert.notNull(annotations, "Annotations must not be null!");
            this.name = name;
            this.type = type;
            this.key = getValue(annotations);
            this.entity = entity;
        }

        private String getValue(Annotation[] annotations) {
            for (Annotation anno : annotations) {
                if (anno.annotationType() == Value.class) {
                    return ((Value) anno).value();
                }
            }
            return null;
        }

        public String getName() {
            return this.name;
        }

        public TypeInformation<T> getType() {
            return this.type;
        }

        public Class<T> getRawType() {
            return this.type.getType();
        }

        public String getSpelExpression() {
            return this.key;
        }

        public boolean hasSpelExpression() {
            if (this.hasSpelExpression == null) {
                this.hasSpelExpression = Boolean.valueOf(StringUtils.hasText(getSpelExpression()));
            }
            return this.hasSpelExpression.booleanValue();
        }

        boolean maps(PersistentProperty<?> property) {
            PersistentProperty persistentProperty = this.entity == null ? null : this.entity.getPersistentProperty(this.name);
            if (property == null) {
                return false;
            }
            return property.equals(persistentProperty);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEnclosingClassParameter() {
            if (this.enclosingClassCache == null) {
                Class<T> owningType = this.entity.getType();
                this.enclosingClassCache = Boolean.valueOf(owningType.isMemberClass() && this.type.getType().equals(owningType.getEnclosingClass()));
            }
            return this.enclosingClassCache.booleanValue();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Parameter)) {
                return false;
            }
            Parameter<?, ?> that = (Parameter) obj;
            boolean nameEquals = this.name == null ? that.name == null : this.name.equals(that.name);
            boolean keyEquals = this.key == null ? that.key == null : this.key.equals(that.key);
            boolean entityEquals = this.entity == null ? that.entity == null : this.entity.equals(that.entity);
            return nameEquals && keyEquals && entityEquals && this.type.equals(that.type);
        }

        public int hashCode() {
            int result = 17 + (31 * ObjectUtils.nullSafeHashCode(this.name));
            return result + (31 * ObjectUtils.nullSafeHashCode(this.key)) + (31 * ObjectUtils.nullSafeHashCode(this.entity)) + (31 * this.type.hashCode());
        }
    }
}
