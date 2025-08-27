package org.springframework.data.mapping.model;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/SnakeCaseFieldNamingStrategy.class */
public class SnakeCaseFieldNamingStrategy extends CamelCaseSplittingFieldNamingStrategy {
    public SnakeCaseFieldNamingStrategy() {
        super("_");
    }
}
