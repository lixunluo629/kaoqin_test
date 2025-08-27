package org.springframework.data.repository.query;

import java.lang.reflect.Method;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.EntityMetadata;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.util.ClassUtils;
import org.springframework.data.repository.util.QueryExecutionConverters;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/QueryMethod.class */
public class QueryMethod {
    private final RepositoryMetadata metadata;
    private final Method method;
    private final Class<?> unwrappedReturnType;
    private final Parameters<?, ?> parameters;
    private final ResultProcessor resultProcessor;
    private Class<?> domainClass;

    public QueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
        Assert.notNull(method, "Method must not be null!");
        Assert.notNull(metadata, "Repository metadata must not be null!");
        Assert.notNull(factory, "ProjectionFactory must not be null!");
        for (Class<?> type : Parameters.TYPES) {
            if (ClassUtils.getNumberOfOccurences(method, type) > 1) {
                throw new IllegalStateException(String.format("Method must only one argument of type %s! Offending method: %s", type.getSimpleName(), method.toString()));
            }
        }
        this.method = method;
        this.unwrappedReturnType = potentiallyUnwrapReturnTypeFor(method);
        this.parameters = createParameters(method);
        this.metadata = metadata;
        if (ClassUtils.hasParameterOfType(method, Pageable.class)) {
            if (!isStreamQuery()) {
                assertReturnTypeAssignable(method, QueryExecutionConverters.getAllowedPageableTypes());
            }
            if (ClassUtils.hasParameterOfType(method, Sort.class)) {
                throw new IllegalStateException(String.format("Method must not have Pageable *and* Sort parameter. Use sorting capabilities on Pageable instead! Offending method: %s", method.toString()));
            }
        }
        Assert.notNull(this.parameters, String.format("Parameters extracted from method '%s' must not be null!", method.getName()));
        if (isPageQuery()) {
            Assert.isTrue(this.parameters.hasPageableParameter(), String.format("Paging query needs to have a Pageable parameter! Offending method %s", method.toString()));
        }
        this.resultProcessor = new ResultProcessor(this, factory);
    }

    protected Parameters<?, ?> createParameters(Method method) {
        return new DefaultParameters(method);
    }

    public String getName() {
        return this.method.getName();
    }

    public EntityMetadata<?> getEntityInformation() {
        return new EntityMetadata() { // from class: org.springframework.data.repository.query.QueryMethod.1
            @Override // org.springframework.data.repository.core.EntityMetadata
            public Class<?> getJavaType() {
                return QueryMethod.this.getDomainClass();
            }
        };
    }

    public String getNamedQueryName() {
        return String.format("%s.%s", getDomainClass().getSimpleName(), this.method.getName());
    }

    protected Class<?> getDomainClass() {
        if (this.domainClass == null) {
            Class<?> repositoryDomainClass = this.metadata.getDomainType();
            Class<?> methodDomainClass = this.metadata.getReturnedDomainClass(this.method);
            this.domainClass = (repositoryDomainClass == null || repositoryDomainClass.isAssignableFrom(methodDomainClass)) ? methodDomainClass : repositoryDomainClass;
        }
        return this.domainClass;
    }

    public Class<?> getReturnedObjectType() {
        return this.metadata.getReturnedDomainClass(this.method);
    }

    public boolean isCollectionQuery() {
        if (isPageQuery() || isSliceQuery()) {
            return false;
        }
        Class<?> returnType = this.method.getReturnType();
        if (QueryExecutionConverters.supports(returnType) && !QueryExecutionConverters.isSingleValue(returnType)) {
            return true;
        }
        if (QueryExecutionConverters.supports(this.unwrappedReturnType)) {
            return !QueryExecutionConverters.isSingleValue(this.unwrappedReturnType);
        }
        return ClassTypeInformation.from(this.unwrappedReturnType).isCollectionLike();
    }

    public boolean isSliceQuery() {
        return !isPageQuery() && org.springframework.util.ClassUtils.isAssignable(Slice.class, this.unwrappedReturnType);
    }

    public final boolean isPageQuery() {
        return org.springframework.util.ClassUtils.isAssignable(Page.class, this.unwrappedReturnType);
    }

    public boolean isModifyingQuery() {
        return false;
    }

    public boolean isQueryForEntity() {
        return getDomainClass().isAssignableFrom(getReturnedObjectType());
    }

    public boolean isStreamQuery() {
        return ReflectionUtils.isJava8StreamType(this.unwrappedReturnType);
    }

    public Parameters<?, ?> getParameters() {
        return this.parameters;
    }

    public ResultProcessor getResultProcessor() {
        return this.resultProcessor;
    }

    public String toString() {
        return this.method.toString();
    }

    private static Class<? extends Object> potentiallyUnwrapReturnTypeFor(Method method) {
        if (QueryExecutionConverters.supports(method.getReturnType())) {
            return ClassTypeInformation.fromReturnTypeOf(method).getComponentType().getType();
        }
        return method.getReturnType();
    }

    private static void assertReturnTypeAssignable(Method method, Set<Class<?>> types) {
        Assert.notNull(method, "Method must not be null!");
        Assert.notEmpty(types, "Types must not be null or empty!");
        TypeInformation<?> returnType = ClassTypeInformation.fromReturnTypeOf(method);
        TypeInformation<?> returnType2 = QueryExecutionConverters.isSingleValue(returnType.getType()) ? returnType.getComponentType() : returnType;
        for (Class<?> type : types) {
            if (type.isAssignableFrom(returnType2.getType())) {
                return;
            }
        }
        throw new IllegalStateException("Method has to have one of the following return types! " + types.toString());
    }
}
