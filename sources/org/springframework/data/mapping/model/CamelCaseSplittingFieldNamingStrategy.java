package org.springframework.data.mapping.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.ParsingUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/CamelCaseSplittingFieldNamingStrategy.class */
public class CamelCaseSplittingFieldNamingStrategy implements FieldNamingStrategy {
    private final String delimiter;

    public CamelCaseSplittingFieldNamingStrategy(String delimiter) {
        Assert.notNull(delimiter, "Delimiter must not be null!");
        this.delimiter = delimiter;
    }

    @Override // org.springframework.data.mapping.model.FieldNamingStrategy
    public String getFieldName(PersistentProperty<?> property) {
        List<String> parts = ParsingUtils.splitCamelCaseToLower(property.getName());
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            String candidate = preparePart(part);
            if (StringUtils.hasText(candidate)) {
                result.add(candidate);
            }
        }
        return StringUtils.collectionToDelimitedString(result, this.delimiter);
    }

    protected String preparePart(String part) {
        return part;
    }
}
