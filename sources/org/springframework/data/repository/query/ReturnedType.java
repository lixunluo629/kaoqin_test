package org.springframework.data.repository.query;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import lombok.NonNull;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.data.mapping.model.PreferredConstructorDiscoverer;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.ProjectionInformation;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ReturnedType.class */
public abstract class ReturnedType {

    @NonNull
    private final Class<?> domainType;

    public abstract boolean isProjecting();

    public abstract Class<?> getReturnedType();

    public abstract boolean needsCustomConstruction();

    public abstract Class<?> getTypeToRead();

    public abstract List<String> getInputProperties();

    @Generated
    private ReturnedType(@NonNull Class<?> domainType) {
        if (domainType == null) {
            throw new IllegalArgumentException("domainType is marked @NonNull but is null");
        }
        this.domainType = domainType;
    }

    static ReturnedType of(Class<?> returnedType, Class<?> domainType, ProjectionFactory factory) {
        Assert.notNull(returnedType, "Returned type must not be null!");
        Assert.notNull(domainType, "Domain type must not be null!");
        Assert.notNull(factory, "ProjectionFactory must not be null!");
        return returnedType.isInterface() ? new ReturnedInterface(factory.getProjectionInformation(returnedType), domainType) : new ReturnedClass(returnedType, domainType);
    }

    public final Class<?> getDomainType() {
        return this.domainType;
    }

    public final boolean isInstance(Object source) {
        return getReturnedType().isInstance(source);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ReturnedType$ReturnedInterface.class */
    private static final class ReturnedInterface extends ReturnedType {
        private final ProjectionInformation information;
        private final Class<?> domainType;

        public ReturnedInterface(ProjectionInformation information, Class<?> domainType) {
            super(domainType);
            Assert.notNull(information, "Projection information must not be null!");
            this.information = information;
            this.domainType = domainType;
        }

        @Override // org.springframework.data.repository.query.ReturnedType
        public Class<?> getReturnedType() {
            return this.information.getType();
        }

        @Override // org.springframework.data.repository.query.ReturnedType
        public boolean needsCustomConstruction() {
            return isProjecting() && this.information.isClosed();
        }

        @Override // org.springframework.data.repository.query.ReturnedType
        public boolean isProjecting() {
            return !this.information.getType().isAssignableFrom(this.domainType);
        }

        @Override // org.springframework.data.repository.query.ReturnedType
        public Class<?> getTypeToRead() {
            if (isProjecting() && this.information.isClosed()) {
                return null;
            }
            return this.domainType;
        }

        @Override // org.springframework.data.repository.query.ReturnedType
        public List<String> getInputProperties() {
            List<String> properties = new ArrayList<>();
            for (PropertyDescriptor descriptor : this.information.getInputProperties()) {
                if (!properties.contains(descriptor.getName())) {
                    properties.add(descriptor.getName());
                }
            }
            return properties;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ReturnedType$ReturnedClass.class */
    private static final class ReturnedClass extends ReturnedType {
        private static final Set<Class<?>> VOID_TYPES = new HashSet(Arrays.asList(Void.class, Void.TYPE));
        private final Class<?> type;
        private final List<String> inputProperties;

        public ReturnedClass(Class<?> returnedType, Class<?> domainType) {
            super(domainType);
            Assert.notNull(returnedType, "Returned type must not be null!");
            Assert.notNull(domainType, "Domain type must not be null!");
            Assert.isTrue(!returnedType.isInterface(), "Returned type must not be an interface!");
            this.type = returnedType;
            this.inputProperties = detectConstructorParameterNames(returnedType);
        }

        @Override // org.springframework.data.repository.query.ReturnedType
        public Class<?> getReturnedType() {
            return this.type;
        }

        @Override // org.springframework.data.repository.query.ReturnedType
        public Class<?> getTypeToRead() {
            return this.type;
        }

        @Override // org.springframework.data.repository.query.ReturnedType
        public boolean isProjecting() {
            return isDto();
        }

        @Override // org.springframework.data.repository.query.ReturnedType
        public boolean needsCustomConstruction() {
            return isDto() && !this.inputProperties.isEmpty();
        }

        @Override // org.springframework.data.repository.query.ReturnedType
        public List<String> getInputProperties() {
            return this.inputProperties;
        }

        private List<String> detectConstructorParameterNames(Class<?> type) {
            if (!isDto()) {
                return Collections.emptyList();
            }
            PreferredConstructorDiscoverer<?, ?> discoverer = new PreferredConstructorDiscoverer<>(type);
            PreferredConstructor constructor = discoverer.getConstructor();
            if (constructor == null) {
                return Collections.emptyList();
            }
            List<String> properties = new ArrayList<>();
            for (PreferredConstructor.Parameter<Object, ?> parameter : constructor.getParameters()) {
                properties.add(parameter.getName());
            }
            return properties;
        }

        private boolean isDto() {
            return (Object.class.equals(this.type) || this.type.isEnum() || isDomainSubtype() || isPrimitiveOrWrapper() || Number.class.isAssignableFrom(this.type) || VOID_TYPES.contains(this.type) || this.type.getPackage().getName().startsWith("java.")) ? false : true;
        }

        private boolean isDomainSubtype() {
            return getDomainType().equals(this.type) && getDomainType().isAssignableFrom(this.type);
        }

        private boolean isPrimitiveOrWrapper() {
            return ClassUtils.isPrimitiveOrWrapper(this.type);
        }
    }
}
