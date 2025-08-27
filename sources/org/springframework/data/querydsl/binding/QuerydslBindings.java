package org.springframework.data.querydsl.binding;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import lombok.NonNull;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.querydsl.QueryDslUtils;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslBindings.class */
public class QuerydslBindings {
    private final Map<String, PathAndBinding<?, ?>> pathSpecs = new LinkedHashMap();
    private final Map<Class<?>, PathAndBinding<?, ?>> typeSpecs = new LinkedHashMap();
    private final Set<String> whiteList = new HashSet();
    private final Set<String> blackList = new HashSet();
    private final Set<String> aliases = new HashSet();
    private boolean excludeUnlistedProperties;

    public final <T extends Path<S>, S> AliasingPathBinder<T, S> bind(T path) {
        return new AliasingPathBinder<>(this, path);
    }

    public final <T extends Path<S>, S> PathBinder<T, S> bind(T... paths) {
        return new PathBinder<>(paths);
    }

    public final <T> TypeBinder<T> bind(Class<T> type) {
        return new TypeBinder<>(type);
    }

    public final void excluding(Path<?>... paths) {
        Assert.notEmpty(paths, "At least one path has to be provided!");
        for (Path<?> path : paths) {
            this.blackList.add(QueryDslUtils.toDotPath(path));
        }
    }

    public final void including(Path<?>... paths) {
        Assert.notEmpty(paths, "At least one path has to be provided!");
        for (Path<?> path : paths) {
            this.whiteList.add(QueryDslUtils.toDotPath(path));
        }
    }

    public final QuerydslBindings excludeUnlistedProperties(boolean excludeUnlistedProperties) {
        this.excludeUnlistedProperties = excludeUnlistedProperties;
        return this;
    }

    boolean isPathAvailable(String path, Class<?> type) {
        Assert.notNull(path, "Path must not be null!");
        Assert.notNull(type, "Type must not be null!");
        return isPathAvailable(path, ClassTypeInformation.from(type));
    }

    boolean isPathAvailable(String path, TypeInformation<?> type) {
        Assert.notNull(path, "Path must not be null!");
        Assert.notNull(type, "Type must not be null!");
        return getPropertyPath(path, type) != null;
    }

    public <S extends Path<? extends T>, T> MultiValueBinding<S, T> getBindingForPath(PathInformation pathInformation) {
        Assert.notNull(pathInformation, "PropertyPath must not be null!");
        PathAndBinding<?, ?> pathAndBinding = this.pathSpecs.get(pathInformation.toDotPath());
        if (pathAndBinding != null && pathAndBinding.getBinding() != null) {
            return pathAndBinding.getBinding();
        }
        PathAndBinding<?, ?> pathAndBinding2 = this.typeSpecs.get(pathInformation.getLeafType());
        if (pathAndBinding2 == null) {
            return null;
        }
        return pathAndBinding2.getBinding();
    }

    Path<?> getExistingPath(PathInformation path) {
        Assert.notNull(path, "PropertyPath must not be null!");
        PathAndBinding<?, ?> pathAndBuilder = this.pathSpecs.get(path.toDotPath());
        if (pathAndBuilder == null) {
            return null;
        }
        return pathAndBuilder.getPath();
    }

    PathInformation getPropertyPath(String path, TypeInformation<?> type) {
        Assert.notNull(path, "Path must not be null!");
        Assert.notNull(type, "Type information must not be null!");
        if (!isPathVisible(path)) {
            return null;
        }
        if (this.pathSpecs.containsKey(path)) {
            return QuerydslPathInformation.of(this.pathSpecs.get(path).getPath());
        }
        try {
            PathInformation propertyPath = PropertyPathInformation.of(path, type);
            if (isPathVisible(propertyPath)) {
                return propertyPath;
            }
            return null;
        } catch (PropertyReferenceException e) {
            return null;
        }
    }

    private boolean isPathVisible(PathInformation path) {
        List<String> segments = Arrays.asList(path.toDotPath().split("\\."));
        for (int i = 1; i <= segments.size(); i++) {
            if (!isPathVisible(StringUtils.collectionToDelimitedString(segments.subList(0, i), "."))) {
                if (!this.whiteList.isEmpty()) {
                    return this.whiteList.contains(path.toDotPath());
                }
                return false;
            }
        }
        return true;
    }

    private boolean isPathVisible(String path) {
        if (this.aliases.contains(path) && !this.blackList.contains(path)) {
            return true;
        }
        if (this.whiteList.isEmpty()) {
            return (this.excludeUnlistedProperties || this.blackList.contains(path)) ? false : true;
        }
        return this.whiteList.contains(path);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslBindings$PathBinder.class */
    public class PathBinder<P extends Path<? extends T>, T> {
        private final List<P> paths;

        PathBinder(P... paths) {
            Assert.notEmpty(paths, "At least one path has to be provided!");
            this.paths = Arrays.asList(paths);
        }

        public void first(SingleValueBinding<P, T> binding) {
            Assert.notNull(binding, "Binding must not be null!");
            all(new MultiValueBindingAdapter(binding));
        }

        public void all(MultiValueBinding<P, T> binding) {
            Assert.notNull(binding, "Binding must not be null!");
            for (P path : this.paths) {
                registerBinding(new PathAndBinding<>(path, binding));
            }
        }

        protected void registerBinding(PathAndBinding<P, T> binding) {
            QuerydslBindings.this.pathSpecs.put(QueryDslUtils.toDotPath(binding.getPath()), binding);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslBindings$AliasingPathBinder.class */
    public class AliasingPathBinder<P extends Path<? extends T>, T> extends PathBinder<P, T> {
        private final String alias;
        private final P path;

        AliasingPathBinder(QuerydslBindings this$0, P path) {
            this(null, path);
        }

        private AliasingPathBinder(String alias, P path) {
            super(path);
            Assert.notNull(path, "Path must not be null!");
            this.alias = alias;
            this.path = path;
        }

        public AliasingPathBinder<P, T> as(String alias) {
            Assert.hasText(alias, "Alias must not be null or empty!");
            return QuerydslBindings.this.new AliasingPathBinder<>(alias, this.path);
        }

        public void withDefaultBinding() {
            registerBinding(new PathAndBinding<>(this.path, null));
        }

        @Override // org.springframework.data.querydsl.binding.QuerydslBindings.PathBinder
        protected void registerBinding(PathAndBinding<P, T> binding) {
            super.registerBinding(binding);
            if (this.alias != null) {
                QuerydslBindings.this.pathSpecs.put(this.alias, binding);
                QuerydslBindings.this.aliases.add(this.alias);
                QuerydslBindings.this.blackList.add(QueryDslUtils.toDotPath(binding.getPath()));
            }
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslBindings$TypeBinder.class */
    public final class TypeBinder<T> {

        @NonNull
        private final Class<T> type;

        @Generated
        public TypeBinder(@NonNull Class<T> type) {
            if (type == null) {
                throw new IllegalArgumentException("type is marked @NonNull but is null");
            }
            this.type = type;
        }

        public <P extends Path<T>> void first(SingleValueBinding<P, T> binding) {
            Assert.notNull(binding, "Binding must not be null!");
            all(new MultiValueBindingAdapter(binding));
        }

        public <P extends Path<T>> void all(MultiValueBinding<P, T> binding) {
            Assert.notNull(binding, "Binding must not be null!");
            QuerydslBindings.this.typeSpecs.put(this.type, new PathAndBinding(null, binding));
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslBindings$PathAndBinding.class */
    private static final class PathAndBinding<S extends Path<? extends T>, T> {
        private final Path<?> path;
        private final MultiValueBinding<S, T> binding;

        @Generated
        public PathAndBinding(Path<?> path, MultiValueBinding<S, T> binding) {
            this.path = path;
            this.binding = binding;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof PathAndBinding)) {
                return false;
            }
            PathAndBinding<?, ?> other = (PathAndBinding) o;
            Object this$path = getPath();
            Object other$path = other.getPath();
            if (this$path == null) {
                if (other$path != null) {
                    return false;
                }
            } else if (!this$path.equals(other$path)) {
                return false;
            }
            Object this$binding = getBinding();
            Object other$binding = other.getBinding();
            return this$binding == null ? other$binding == null : this$binding.equals(other$binding);
        }

        @Generated
        public int hashCode() {
            Object $path = getPath();
            int result = (1 * 59) + ($path == null ? 43 : $path.hashCode());
            Object $binding = getBinding();
            return (result * 59) + ($binding == null ? 43 : $binding.hashCode());
        }

        @Generated
        public String toString() {
            return "QuerydslBindings.PathAndBinding(path=" + getPath() + ", binding=" + getBinding() + ")";
        }

        @Generated
        public Path<?> getPath() {
            return this.path;
        }

        @Generated
        public MultiValueBinding<S, T> getBinding() {
            return this.binding;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslBindings$MultiValueBindingAdapter.class */
    static class MultiValueBindingAdapter<P extends Path<? extends T>, T> implements MultiValueBinding<P, T> {

        @NonNull
        private final SingleValueBinding<P, T> delegate;

        @Generated
        public MultiValueBindingAdapter(@NonNull SingleValueBinding<P, T> delegate) {
            if (delegate == null) {
                throw new IllegalArgumentException("delegate is marked @NonNull but is null");
            }
            this.delegate = delegate;
        }

        @Override // org.springframework.data.querydsl.binding.MultiValueBinding
        public Predicate bind(P path, Collection<? extends T> value) {
            Iterator<? extends T> iterator = value.iterator();
            return this.delegate.bind(path, iterator.hasNext() ? iterator.next() : null);
        }
    }
}
