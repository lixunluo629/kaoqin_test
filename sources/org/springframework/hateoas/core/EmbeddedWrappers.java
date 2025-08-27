package org.springframework.hateoas.core;

import java.util.Collection;
import java.util.Collections;
import org.springframework.aop.support.AopUtils;
import org.springframework.hateoas.Resource;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/EmbeddedWrappers.class */
public class EmbeddedWrappers {
    private final boolean preferCollections;

    public EmbeddedWrappers(boolean preferCollections) {
        this.preferCollections = preferCollections;
    }

    public EmbeddedWrapper wrap(Object source) {
        return wrap(source, "___norel___");
    }

    public EmbeddedWrapper emptyCollectionOf(Class<?> type) {
        return new EmptyCollectionEmbeddedWrapper(type);
    }

    public EmbeddedWrapper wrap(Object source, String rel) {
        if (source == null) {
            return null;
        }
        if (source instanceof EmbeddedWrapper) {
            return (EmbeddedWrapper) source;
        }
        if (source instanceof Collection) {
            return new EmbeddedCollection((Collection) source, rel);
        }
        if (this.preferCollections) {
            return new EmbeddedCollection(Collections.singleton(source), rel);
        }
        return new EmbeddedElement(source, rel);
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/EmbeddedWrappers$AbstractEmbeddedWrapper.class */
    private static abstract class AbstractEmbeddedWrapper implements EmbeddedWrapper {
        private static final String NO_REL = "___norel___";
        private final String rel;

        protected abstract Object peek();

        public AbstractEmbeddedWrapper(String rel) {
            Assert.hasText(rel, "Rel must not be null or empty!");
            this.rel = rel;
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public String getRel() {
            if (NO_REL.equals(this.rel)) {
                return null;
            }
            return this.rel;
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public boolean hasRel(String rel) {
            return this.rel.equals(rel);
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public Class<?> getRelTargetType() {
            Object peek = peek();
            if (peek == null) {
                return null;
            }
            return AopUtils.getTargetClass(peek instanceof Resource ? ((Resource) peek).getContent() : peek);
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/EmbeddedWrappers$EmbeddedElement.class */
    private static class EmbeddedElement extends AbstractEmbeddedWrapper {
        private final Object value;

        public EmbeddedElement(Object value, String rel) {
            super(rel);
            Assert.notNull(value, "Value must not be null!");
            this.value = value;
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public Object getValue() {
            return this.value;
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrappers.AbstractEmbeddedWrapper
        protected Object peek() {
            return getValue();
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public boolean isCollectionValue() {
            return false;
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/EmbeddedWrappers$EmbeddedCollection.class */
    private static class EmbeddedCollection extends AbstractEmbeddedWrapper {
        private final Collection<Object> value;

        public EmbeddedCollection(Collection<Object> value, String rel) {
            super(rel);
            Assert.notNull(value, "Collection must not be null!");
            if ("___norel___".equals(rel) && value.isEmpty()) {
                throw new IllegalArgumentException("Cannot wrap an empty collection with no rel given!");
            }
            this.value = value;
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public Collection<Object> getValue() {
            return this.value;
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrappers.AbstractEmbeddedWrapper
        protected Object peek() {
            if (this.value.isEmpty()) {
                return null;
            }
            return this.value.iterator().next();
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public boolean isCollectionValue() {
            return true;
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/EmbeddedWrappers$EmptyCollectionEmbeddedWrapper.class */
    private static class EmptyCollectionEmbeddedWrapper implements EmbeddedWrapper {
        private final Class<?> type;

        public EmptyCollectionEmbeddedWrapper(Class<?> type) {
            Assert.notNull(type, "Element type must not be null!");
            this.type = type;
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public String getRel() {
            return null;
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public Object getValue() {
            return Collections.emptySet();
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public Class<?> getRelTargetType() {
            return this.type;
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public boolean isCollectionValue() {
            return true;
        }

        @Override // org.springframework.hateoas.core.EmbeddedWrapper
        public boolean hasRel(String rel) {
            return false;
        }
    }
}
