package org.springframework.boot.autoconfigure.data.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.http.MediaType;

@ConfigurationProperties(prefix = "spring.data.rest")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/rest/RepositoryRestProperties.class */
public class RepositoryRestProperties {
    private String basePath;
    private Integer defaultPageSize;
    private Integer maxPageSize;
    private String pageParamName;
    private String limitParamName;
    private String sortParamName;
    private RepositoryDetectionStrategy.RepositoryDetectionStrategies detectionStrategy = RepositoryDetectionStrategy.RepositoryDetectionStrategies.DEFAULT;
    private MediaType defaultMediaType;
    private Boolean returnBodyOnCreate;
    private Boolean returnBodyOnUpdate;
    private Boolean enableEnumTranslation;

    public String getBasePath() {
        return this.basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Integer getDefaultPageSize() {
        return this.defaultPageSize;
    }

    public void setDefaultPageSize(Integer defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public Integer getMaxPageSize() {
        return this.maxPageSize;
    }

    public void setMaxPageSize(Integer maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public String getPageParamName() {
        return this.pageParamName;
    }

    public void setPageParamName(String pageParamName) {
        this.pageParamName = pageParamName;
    }

    public String getLimitParamName() {
        return this.limitParamName;
    }

    public void setLimitParamName(String limitParamName) {
        this.limitParamName = limitParamName;
    }

    public String getSortParamName() {
        return this.sortParamName;
    }

    public void setSortParamName(String sortParamName) {
        this.sortParamName = sortParamName;
    }

    public RepositoryDetectionStrategy.RepositoryDetectionStrategies getDetectionStrategy() {
        return this.detectionStrategy;
    }

    public void setDetectionStrategy(RepositoryDetectionStrategy.RepositoryDetectionStrategies detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    public MediaType getDefaultMediaType() {
        return this.defaultMediaType;
    }

    public void setDefaultMediaType(MediaType defaultMediaType) {
        this.defaultMediaType = defaultMediaType;
    }

    public Boolean getReturnBodyOnCreate() {
        return this.returnBodyOnCreate;
    }

    public void setReturnBodyOnCreate(Boolean returnBodyOnCreate) {
        this.returnBodyOnCreate = returnBodyOnCreate;
    }

    public Boolean getReturnBodyOnUpdate() {
        return this.returnBodyOnUpdate;
    }

    public void setReturnBodyOnUpdate(Boolean returnBodyOnUpdate) {
        this.returnBodyOnUpdate = returnBodyOnUpdate;
    }

    public Boolean getEnableEnumTranslation() {
        return this.enableEnumTranslation;
    }

    public void setEnableEnumTranslation(Boolean enableEnumTranslation) {
        this.enableEnumTranslation = enableEnumTranslation;
    }

    public void applyTo(RepositoryRestConfiguration configuration) {
        if (this.basePath != null) {
            configuration.setBasePath(this.basePath);
        }
        if (this.defaultPageSize != null) {
            configuration.setDefaultPageSize(this.defaultPageSize.intValue());
        }
        if (this.maxPageSize != null) {
            configuration.setMaxPageSize(this.maxPageSize.intValue());
        }
        if (this.pageParamName != null) {
            configuration.setPageParamName(this.pageParamName);
        }
        if (this.limitParamName != null) {
            configuration.setLimitParamName(this.limitParamName);
        }
        if (this.sortParamName != null) {
            configuration.setSortParamName(this.sortParamName);
        }
        if (this.detectionStrategy != null) {
            configuration.setRepositoryDetectionStrategy(this.detectionStrategy);
        }
        if (this.defaultMediaType != null) {
            configuration.setDefaultMediaType(this.defaultMediaType);
        }
        if (this.returnBodyOnCreate != null) {
            configuration.setReturnBodyOnCreate(this.returnBodyOnCreate);
        }
        if (this.returnBodyOnUpdate != null) {
            configuration.setReturnBodyOnUpdate(this.returnBodyOnUpdate);
        }
        if (this.enableEnumTranslation != null) {
            configuration.setEnableEnumTranslation(this.enableEnumTranslation.booleanValue());
        }
    }
}
