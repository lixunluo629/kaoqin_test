package org.springframework.jdbc.datasource.embedded;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseBuilder.class */
public class EmbeddedDatabaseBuilder {
    private final EmbeddedDatabaseFactory databaseFactory;
    private final ResourceDatabasePopulator databasePopulator;
    private final ResourceLoader resourceLoader;

    public EmbeddedDatabaseBuilder() {
        this(new DefaultResourceLoader());
    }

    public EmbeddedDatabaseBuilder(ResourceLoader resourceLoader) {
        this.databaseFactory = new EmbeddedDatabaseFactory();
        this.databasePopulator = new ResourceDatabasePopulator();
        this.databaseFactory.setDatabasePopulator(this.databasePopulator);
        this.resourceLoader = resourceLoader;
    }

    public EmbeddedDatabaseBuilder generateUniqueName(boolean flag) {
        this.databaseFactory.setGenerateUniqueDatabaseName(flag);
        return this;
    }

    public EmbeddedDatabaseBuilder setName(String databaseName) {
        this.databaseFactory.setDatabaseName(databaseName);
        return this;
    }

    public EmbeddedDatabaseBuilder setType(EmbeddedDatabaseType databaseType) {
        this.databaseFactory.setDatabaseType(databaseType);
        return this;
    }

    public EmbeddedDatabaseBuilder setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        Assert.notNull(dataSourceFactory, "DataSourceFactory is required");
        this.databaseFactory.setDataSourceFactory(dataSourceFactory);
        return this;
    }

    public EmbeddedDatabaseBuilder addDefaultScripts() {
        return addScripts("schema.sql", "data.sql");
    }

    public EmbeddedDatabaseBuilder addScript(String script) {
        this.databasePopulator.addScript(this.resourceLoader.getResource(script));
        return this;
    }

    public EmbeddedDatabaseBuilder addScripts(String... scripts) {
        for (String script : scripts) {
            addScript(script);
        }
        return this;
    }

    public EmbeddedDatabaseBuilder setScriptEncoding(String scriptEncoding) {
        this.databasePopulator.setSqlScriptEncoding(scriptEncoding);
        return this;
    }

    public EmbeddedDatabaseBuilder setSeparator(String separator) {
        this.databasePopulator.setSeparator(separator);
        return this;
    }

    public EmbeddedDatabaseBuilder setCommentPrefix(String commentPrefix) {
        this.databasePopulator.setCommentPrefix(commentPrefix);
        return this;
    }

    public EmbeddedDatabaseBuilder setBlockCommentStartDelimiter(String blockCommentStartDelimiter) {
        this.databasePopulator.setBlockCommentStartDelimiter(blockCommentStartDelimiter);
        return this;
    }

    public EmbeddedDatabaseBuilder setBlockCommentEndDelimiter(String blockCommentEndDelimiter) {
        this.databasePopulator.setBlockCommentEndDelimiter(blockCommentEndDelimiter);
        return this;
    }

    public EmbeddedDatabaseBuilder continueOnError(boolean flag) {
        this.databasePopulator.setContinueOnError(flag);
        return this;
    }

    public EmbeddedDatabaseBuilder ignoreFailedDrops(boolean flag) {
        this.databasePopulator.setIgnoreFailedDrops(flag);
        return this;
    }

    public EmbeddedDatabase build() {
        return this.databaseFactory.getDatabase();
    }
}
