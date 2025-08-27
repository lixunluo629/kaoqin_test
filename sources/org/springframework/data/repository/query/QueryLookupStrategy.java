package org.springframework.data.repository.query;

import java.lang.reflect.Method;
import java.util.Locale;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/QueryLookupStrategy.class */
public interface QueryLookupStrategy {
    RepositoryQuery resolveQuery(Method method, RepositoryMetadata repositoryMetadata, ProjectionFactory projectionFactory, NamedQueries namedQueries);

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/QueryLookupStrategy$Key.class */
    public enum Key {
        CREATE,
        USE_DECLARED_QUERY,
        CREATE_IF_NOT_FOUND;

        public static Key create(String xml) {
            if (!StringUtils.hasText(xml)) {
                return null;
            }
            return valueOf(xml.toUpperCase(Locale.US).replace("-", "_"));
        }
    }
}
