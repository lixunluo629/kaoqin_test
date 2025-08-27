package org.springframework.data.querydsl;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathType;
import lombok.Generated;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/QueryDslUtils.class */
public final class QueryDslUtils {
    public static final boolean QUERY_DSL_PRESENT = ClassUtils.isPresent("com.querydsl.core.types.Predicate", QueryDslUtils.class.getClassLoader());

    @Generated
    private QueryDslUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String toDotPath(Path<?> path) {
        return toDotPath(path, "");
    }

    private static String toDotPath(Path<?> path, String tail) {
        if (path == null) {
            return tail;
        }
        PathMetadata metadata = path.getMetadata();
        Path<?> parent = metadata.getParent();
        if (parent == null) {
            return tail;
        }
        if (metadata.getPathType().equals(PathType.DELEGATE)) {
            return toDotPath(parent, tail);
        }
        Object element = metadata.getElement();
        if (element == null || !StringUtils.hasText(element.toString())) {
            return toDotPath(parent, tail);
        }
        return toDotPath(parent, StringUtils.hasText(tail) ? String.format("%s.%s", element, tail) : element.toString());
    }
}
