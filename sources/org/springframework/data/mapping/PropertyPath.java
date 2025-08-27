package org.springframework.data.mapping;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/PropertyPath.class */
public class PropertyPath implements Iterable<PropertyPath> {
    private static final String PARSE_DEPTH_EXCEEDED = "Trying to parse a path with depth greater than 1000! This has been disabled for security reasons to prevent parsing overflows.";
    private static final String ALL_UPPERCASE = "[A-Z0-9._$]+";
    private final TypeInformation<?> owningType;
    private final String name;
    private final TypeInformation<?> type;
    private final boolean isCollection;
    private PropertyPath next;
    private static final String DELIMITERS = "_\\.";
    private static final Pattern SPLITTER = Pattern.compile("(?:[%s]?([%s]*?[^%s]+))".replaceAll("%s", DELIMITERS));

    PropertyPath(String name, Class<?> owningType) {
        this(name, ClassTypeInformation.from(owningType), Collections.emptyList());
    }

    PropertyPath(String name, TypeInformation<?> owningType, List<PropertyPath> base) {
        Assert.hasText(name, "Name must not be null or empty!");
        Assert.notNull(owningType, "Owning type must not be null!");
        Assert.notNull(base, "Perviously found properties must not be null!");
        String propertyName = Introspector.decapitalize(name);
        TypeInformation<?> propertyType = owningType.getProperty(propertyName);
        if (propertyType == null) {
            throw new PropertyReferenceException(propertyName, owningType, base);
        }
        this.owningType = owningType;
        this.isCollection = propertyType.isCollectionLike();
        this.type = propertyType.getActualType();
        this.name = propertyName;
    }

    public TypeInformation<?> getOwningType() {
        return this.owningType;
    }

    public String getSegment() {
        return this.name;
    }

    public PropertyPath getLeafProperty() {
        PropertyPath next = this;
        while (true) {
            PropertyPath result = next;
            if (result.hasNext()) {
                next = result.next();
            } else {
                return result;
            }
        }
    }

    public Class<?> getType() {
        return this.type.getType();
    }

    public PropertyPath next() {
        return this.next;
    }

    public boolean hasNext() {
        return this.next != null;
    }

    public String toDotPath() {
        if (hasNext()) {
            return getSegment() + "." + next().toDotPath();
        }
        return getSegment();
    }

    public boolean isCollection() {
        return this.isCollection;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        PropertyPath that = (PropertyPath) obj;
        return this.name.equals(that.name) && this.type.equals(that.type) && ObjectUtils.nullSafeEquals(this.next, that.next);
    }

    public int hashCode() {
        int result = 17 + (31 * this.name.hashCode());
        return result + (31 * this.type.hashCode()) + (31 * (this.next == null ? 0 : this.next.hashCode()));
    }

    @Override // java.lang.Iterable
    public Iterator<PropertyPath> iterator() {
        return new Iterator<PropertyPath>() { // from class: org.springframework.data.mapping.PropertyPath.1
            private PropertyPath current;

            {
                this.current = PropertyPath.this;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.current != null;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public PropertyPath next() {
                PropertyPath result = this.current;
                this.current = this.current.next();
                return result;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static PropertyPath from(String source, Class<?> type) {
        return from(source, ClassTypeInformation.from(type));
    }

    public static PropertyPath from(String source, TypeInformation<?> type) {
        Assert.hasText(source, "Source must not be null or empty!");
        Assert.notNull(type, "TypeInformation must not be null or empty!");
        List<String> iteratorSource = new ArrayList<>();
        Matcher matcher = SPLITTER.matcher("_" + source);
        while (matcher.find()) {
            iteratorSource.add(matcher.group(1));
        }
        Iterator<String> parts = iteratorSource.iterator();
        PropertyPath result = null;
        Stack<PropertyPath> current = new Stack<>();
        while (parts.hasNext()) {
            if (result == null) {
                result = create(parts.next(), type, current);
                current.push(result);
            } else {
                current.push(create(parts.next(), current));
            }
        }
        return result;
    }

    private static PropertyPath create(String source, Stack<PropertyPath> base) {
        PropertyPath previous = base.peek();
        PropertyPath propertyPath = create(source, previous.type, base);
        previous.next = propertyPath;
        return propertyPath;
    }

    private static PropertyPath create(String source, TypeInformation<?> type, List<PropertyPath> base) {
        return create(source, type, "", base);
    }

    private static PropertyPath create(String source, TypeInformation<?> type, String addTail, List<PropertyPath> base) {
        if (base.size() > 1000) {
            throw new IllegalArgumentException(PARSE_DEPTH_EXCEEDED);
        }
        PropertyPath current = null;
        try {
            current = new PropertyPath(source, type, base);
            if (!base.isEmpty()) {
                base.get(base.size() - 1).next = current;
            }
            List<PropertyPath> newBase = new ArrayList<>(base);
            newBase.add(current);
            if (StringUtils.hasText(addTail)) {
                current.next = create(addTail, current.type, newBase);
            }
            return current;
        } catch (PropertyReferenceException e) {
            if (current != null) {
                throw e;
            }
            Pattern pattern = Pattern.compile("\\p{Lu}\\p{Ll}*$");
            Matcher matcher = pattern.matcher(source);
            if (matcher.find() && matcher.start() != 0) {
                int position = matcher.start();
                String head = source.substring(0, position);
                String tail = source.substring(position);
                try {
                    return create(head, type, tail + addTail, base);
                } catch (PropertyReferenceException e2) {
                    if (e2.hasDeeperResolutionDepthThan(e)) {
                        throw e2;
                    }
                    throw e;
                }
            }
            throw e;
        }
    }

    public String toString() {
        return String.format("%s.%s", this.owningType.getType().getSimpleName(), toDotPath());
    }
}
