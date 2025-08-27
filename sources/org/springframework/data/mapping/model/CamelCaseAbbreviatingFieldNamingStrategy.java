package org.springframework.data.mapping.model;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/CamelCaseAbbreviatingFieldNamingStrategy.class */
public class CamelCaseAbbreviatingFieldNamingStrategy extends CamelCaseSplittingFieldNamingStrategy {
    public CamelCaseAbbreviatingFieldNamingStrategy() {
        super("");
    }

    @Override // org.springframework.data.mapping.model.CamelCaseSplittingFieldNamingStrategy
    protected String preparePart(String part) {
        return part.substring(0, 1);
    }
}
