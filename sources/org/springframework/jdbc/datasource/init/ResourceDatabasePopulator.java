package org.springframework.jdbc.datasource.init;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/init/ResourceDatabasePopulator.class */
public class ResourceDatabasePopulator implements DatabasePopulator {
    List<Resource> scripts;
    private String sqlScriptEncoding;
    private String separator;
    private String commentPrefix;
    private String blockCommentStartDelimiter;
    private String blockCommentEndDelimiter;
    private boolean continueOnError;
    private boolean ignoreFailedDrops;

    public ResourceDatabasePopulator() {
        this.scripts = new ArrayList();
        this.separator = ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;
        this.commentPrefix = ScriptUtils.DEFAULT_COMMENT_PREFIX;
        this.blockCommentStartDelimiter = ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER;
        this.blockCommentEndDelimiter = "*/";
        this.continueOnError = false;
        this.ignoreFailedDrops = false;
    }

    public ResourceDatabasePopulator(Resource... scripts) {
        this();
        setScripts(scripts);
    }

    public ResourceDatabasePopulator(boolean continueOnError, boolean ignoreFailedDrops, String sqlScriptEncoding, Resource... scripts) {
        this(scripts);
        this.continueOnError = continueOnError;
        this.ignoreFailedDrops = ignoreFailedDrops;
        setSqlScriptEncoding(sqlScriptEncoding);
    }

    public void addScript(Resource script) {
        Assert.notNull(script, "Script must not be null");
        this.scripts.add(script);
    }

    public void addScripts(Resource... scripts) {
        assertContentsOfScriptArray(scripts);
        this.scripts.addAll(Arrays.asList(scripts));
    }

    public void setScripts(Resource... scripts) {
        assertContentsOfScriptArray(scripts);
        this.scripts = new ArrayList(Arrays.asList(scripts));
    }

    private void assertContentsOfScriptArray(Resource... scripts) {
        Assert.notNull(scripts, "Scripts array must not be null");
        Assert.noNullElements(scripts, "Scripts array must not contain null elements");
    }

    public void setSqlScriptEncoding(String sqlScriptEncoding) {
        this.sqlScriptEncoding = StringUtils.hasText(sqlScriptEncoding) ? sqlScriptEncoding : null;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setCommentPrefix(String commentPrefix) {
        this.commentPrefix = commentPrefix;
    }

    public void setBlockCommentStartDelimiter(String blockCommentStartDelimiter) {
        Assert.hasText(blockCommentStartDelimiter, "BlockCommentStartDelimiter must not be null or empty");
        this.blockCommentStartDelimiter = blockCommentStartDelimiter;
    }

    public void setBlockCommentEndDelimiter(String blockCommentEndDelimiter) {
        Assert.hasText(blockCommentEndDelimiter, "BlockCommentEndDelimiter must not be null or empty");
        this.blockCommentEndDelimiter = blockCommentEndDelimiter;
    }

    public void setContinueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
    }

    public void setIgnoreFailedDrops(boolean ignoreFailedDrops) {
        this.ignoreFailedDrops = ignoreFailedDrops;
    }

    @Override // org.springframework.jdbc.datasource.init.DatabasePopulator
    public void populate(Connection connection) throws ScriptException {
        Assert.notNull(connection, "Connection must not be null");
        for (Resource script : this.scripts) {
            EncodedResource encodedScript = new EncodedResource(script, this.sqlScriptEncoding);
            ScriptUtils.executeSqlScript(connection, encodedScript, this.continueOnError, this.ignoreFailedDrops, this.commentPrefix, this.separator, this.blockCommentStartDelimiter, this.blockCommentEndDelimiter);
        }
    }

    public void execute(DataSource dataSource) throws DataAccessException {
        DatabasePopulatorUtils.execute(this, dataSource);
    }
}
