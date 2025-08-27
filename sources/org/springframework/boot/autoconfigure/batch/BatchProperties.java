package org.springframework.boot.autoconfigure.batch;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.batch")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/batch/BatchProperties.class */
public class BatchProperties {
    private static final String DEFAULT_SCHEMA_LOCATION = "classpath:org/springframework/batch/core/schema-@@platform@@.sql";
    private String tablePrefix;
    private String schema = DEFAULT_SCHEMA_LOCATION;
    private final Initializer initializer = new Initializer();
    private final Job job = new Job();

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTablePrefix() {
        return this.tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public Initializer getInitializer() {
        return this.initializer;
    }

    public Job getJob() {
        return this.job;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/batch/BatchProperties$Initializer.class */
    public class Initializer {
        private Boolean enabled;

        public Initializer() {
        }

        public boolean isEnabled() {
            if (this.enabled != null) {
                return this.enabled.booleanValue();
            }
            boolean defaultTablePrefix = BatchProperties.this.getTablePrefix() == null;
            boolean customSchema = !BatchProperties.DEFAULT_SCHEMA_LOCATION.equals(BatchProperties.this.getSchema());
            return defaultTablePrefix || customSchema;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = Boolean.valueOf(enabled);
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/batch/BatchProperties$Job.class */
    public static class Job {
        private String names = "";

        public String getNames() {
            return this.names;
        }

        public void setNames(String names) {
            this.names = names;
        }
    }
}
