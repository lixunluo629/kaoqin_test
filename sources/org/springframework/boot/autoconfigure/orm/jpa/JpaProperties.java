package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "spring.jpa")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/JpaProperties.class */
public class JpaProperties {
    private String databasePlatform;
    private Database database;
    private Map<String, String> properties = new HashMap();
    private boolean generateDdl = false;
    private boolean showSql = false;
    private Hibernate hibernate = new Hibernate();

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getDatabasePlatform() {
        return this.databasePlatform;
    }

    public void setDatabasePlatform(String databasePlatform) {
        this.databasePlatform = databasePlatform;
    }

    public Database getDatabase() {
        return this.database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public boolean isGenerateDdl() {
        return this.generateDdl;
    }

    public void setGenerateDdl(boolean generateDdl) {
        this.generateDdl = generateDdl;
    }

    public boolean isShowSql() {
        return this.showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public Hibernate getHibernate() {
        return this.hibernate;
    }

    public void setHibernate(Hibernate hibernate) {
        this.hibernate = hibernate;
    }

    public Map<String, String> getHibernateProperties(DataSource dataSource) {
        return this.hibernate.getAdditionalProperties(this.properties, dataSource);
    }

    public Database determineDatabase(DataSource dataSource) {
        if (this.database != null) {
            return this.database;
        }
        return DatabaseLookup.getDatabase(dataSource);
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/JpaProperties$Hibernate.class */
    public static class Hibernate {
        private static final String USE_NEW_ID_GENERATOR_MAPPINGS = "hibernate.id.new_generator_mappings";
        private String ddlAuto;
        private Boolean useNewIdGeneratorMappings;

        @NestedConfigurationProperty
        private final Naming naming = new Naming();

        public String getDdlAuto() {
            return this.ddlAuto;
        }

        public void setDdlAuto(String ddlAuto) {
            this.ddlAuto = ddlAuto;
        }

        public boolean isUseNewIdGeneratorMappings() {
            return this.useNewIdGeneratorMappings.booleanValue();
        }

        public void setUseNewIdGeneratorMappings(boolean useNewIdGeneratorMappings) {
            this.useNewIdGeneratorMappings = Boolean.valueOf(useNewIdGeneratorMappings);
        }

        public Naming getNaming() {
            return this.naming;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Map<String, String> getAdditionalProperties(Map<String, String> existing, DataSource dataSource) {
            Map<String, String> result = new HashMap<>(existing);
            applyNewIdGeneratorMappings(result);
            getNaming().applyNamingStrategy(result);
            String ddlAuto = getOrDeduceDdlAuto(existing, dataSource);
            if (StringUtils.hasText(ddlAuto) && !"none".equals(ddlAuto)) {
                result.put("hibernate.hbm2ddl.auto", ddlAuto);
            } else {
                result.remove("hibernate.hbm2ddl.auto");
            }
            return result;
        }

        private void applyNewIdGeneratorMappings(Map<String, String> result) {
            if (this.useNewIdGeneratorMappings != null) {
                result.put(USE_NEW_ID_GENERATOR_MAPPINGS, this.useNewIdGeneratorMappings.toString());
            } else if (HibernateVersion.getRunning() == HibernateVersion.V5 && !result.containsKey(USE_NEW_ID_GENERATOR_MAPPINGS)) {
                result.put(USE_NEW_ID_GENERATOR_MAPPINGS, "false");
            }
        }

        private String getOrDeduceDdlAuto(Map<String, String> existing, DataSource dataSource) {
            String ddlAuto = this.ddlAuto != null ? this.ddlAuto : getDefaultDdlAuto(dataSource);
            if (!existing.containsKey("hibernate.hbm2ddl.auto") && !"none".equals(ddlAuto)) {
                return ddlAuto;
            }
            if (existing.containsKey("hibernate.hbm2ddl.auto")) {
                return existing.get("hibernate.hbm2ddl.auto");
            }
            return "none";
        }

        private String getDefaultDdlAuto(DataSource dataSource) {
            if (EmbeddedDatabaseConnection.isEmbedded(dataSource)) {
                return "create-drop";
            }
            return "none";
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/JpaProperties$Naming.class */
    public static class Naming {
        private static final String DEFAULT_HIBERNATE4_STRATEGY = "org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy";
        private static final String DEFAULT_PHYSICAL_STRATEGY = "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy";
        private static final String DEFAULT_IMPLICIT_STRATEGY = "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy";
        private String implicitStrategy;
        private String physicalStrategy;
        private String strategy;

        public String getImplicitStrategy() {
            return this.implicitStrategy;
        }

        public void setImplicitStrategy(String implicitStrategy) {
            this.implicitStrategy = implicitStrategy;
        }

        public String getPhysicalStrategy() {
            return this.physicalStrategy;
        }

        public void setPhysicalStrategy(String physicalStrategy) {
            this.physicalStrategy = physicalStrategy;
        }

        public String getStrategy() {
            return this.strategy;
        }

        public void setStrategy(String strategy) {
            this.strategy = strategy;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void applyNamingStrategy(Map<String, String> properties) {
            switch (HibernateVersion.getRunning()) {
                case V4:
                    applyHibernate4NamingStrategy(properties);
                    break;
                case V5:
                    applyHibernate5NamingStrategy(properties);
                    break;
            }
        }

        private void applyHibernate5NamingStrategy(Map<String, String> properties) {
            applyHibernate5NamingStrategy(properties, "hibernate.implicit_naming_strategy", this.implicitStrategy, DEFAULT_IMPLICIT_STRATEGY);
            applyHibernate5NamingStrategy(properties, "hibernate.physical_naming_strategy", this.physicalStrategy, DEFAULT_PHYSICAL_STRATEGY);
        }

        private void applyHibernate5NamingStrategy(Map<String, String> properties, String key, String strategy, String defaultStrategy) {
            if (strategy != null) {
                properties.put(key, strategy);
            } else if (defaultStrategy != null && !properties.containsKey(key)) {
                properties.put(key, defaultStrategy);
            }
        }

        private void applyHibernate4NamingStrategy(Map<String, String> properties) {
            if (!properties.containsKey("hibernate.ejb.naming_strategy_delegator")) {
                properties.put("hibernate.ejb.naming_strategy", getHibernate4NamingStrategy(properties));
            }
        }

        private String getHibernate4NamingStrategy(Map<String, String> existing) {
            if (!existing.containsKey("hibernate.ejb.naming_strategy") && this.strategy != null) {
                return this.strategy;
            }
            return DEFAULT_HIBERNATE4_STRATEGY;
        }
    }
}
