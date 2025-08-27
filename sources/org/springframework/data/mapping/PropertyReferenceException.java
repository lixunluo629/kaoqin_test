package org.springframework.data.mapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.PropertyMatches;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/PropertyReferenceException.class */
public class PropertyReferenceException extends RuntimeException {
    private static final long serialVersionUID = -5254424051438976570L;
    private static final String ERROR_TEMPLATE = "No property %s found for type %s!";
    private static final String HINTS_TEMPLATE = " Did you mean %s?";
    private final String propertyName;
    private final TypeInformation<?> type;
    private final List<PropertyPath> alreadyResolvedPath;
    private final Set<String> propertyMatches;

    public PropertyReferenceException(String propertyName, TypeInformation<?> type, List<PropertyPath> alreadyResolvedPah) {
        Assert.hasText(propertyName, "Property name must not be null!");
        Assert.notNull(type, "Type must not be null!");
        Assert.notNull(alreadyResolvedPah, "Already resolved paths must not be null!");
        this.propertyName = propertyName;
        this.type = type;
        this.alreadyResolvedPath = alreadyResolvedPah;
        this.propertyMatches = detectPotentialMatches(propertyName, type.getType());
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public TypeInformation<?> getType() {
        return this.type;
    }

    Collection<String> getPropertyMatches() {
        return this.propertyMatches;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuilder builder = new StringBuilder(String.format(ERROR_TEMPLATE, this.propertyName, this.type.getType().getSimpleName()));
        if (!this.propertyMatches.isEmpty()) {
            String matches = StringUtils.collectionToDelimitedString(this.propertyMatches, ",", "'", "'");
            builder.append(String.format(HINTS_TEMPLATE, matches));
        }
        if (!this.alreadyResolvedPath.isEmpty()) {
            builder.append(" Traversed path: ");
            builder.append(this.alreadyResolvedPath.get(0).toString());
            builder.append(".");
        }
        return builder.toString();
    }

    public PropertyPath getBaseProperty() {
        if (this.alreadyResolvedPath.isEmpty()) {
            return null;
        }
        return this.alreadyResolvedPath.get(this.alreadyResolvedPath.size() - 1);
    }

    public boolean hasDeeperResolutionDepthThan(PropertyReferenceException exception) {
        return this.alreadyResolvedPath.size() > exception.alreadyResolvedPath.size();
    }

    private static Set<String> detectPotentialMatches(String propertyName, Class<?> type) {
        Set<String> result = new HashSet<>();
        result.addAll(Arrays.asList(PropertyMatches.forField(propertyName, type).getPossibleMatches()));
        result.addAll(Arrays.asList(PropertyMatches.forProperty(propertyName, type).getPossibleMatches()));
        return result;
    }
}
