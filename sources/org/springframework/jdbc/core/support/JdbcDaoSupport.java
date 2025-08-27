package org.springframework.jdbc.core.support;

import java.sql.Connection;
import javax.sql.DataSource;
import org.springframework.dao.support.DaoSupport;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.SQLExceptionTranslator;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/support/JdbcDaoSupport.class */
public abstract class JdbcDaoSupport extends DaoSupport {
    private JdbcTemplate jdbcTemplate;

    public final void setDataSource(DataSource dataSource) {
        if (this.jdbcTemplate == null || dataSource != this.jdbcTemplate.getDataSource()) {
            this.jdbcTemplate = createJdbcTemplate(dataSource);
            initTemplateConfig();
        }
    }

    protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public final DataSource getDataSource() {
        if (this.jdbcTemplate != null) {
            return this.jdbcTemplate.getDataSource();
        }
        return null;
    }

    public final void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        initTemplateConfig();
    }

    public final JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    protected void initTemplateConfig() {
    }

    @Override // org.springframework.dao.support.DaoSupport
    protected void checkDaoConfig() {
        if (this.jdbcTemplate == null) {
            throw new IllegalArgumentException("'dataSource' or 'jdbcTemplate' is required");
        }
    }

    protected final SQLExceptionTranslator getExceptionTranslator() {
        return getJdbcTemplate().getExceptionTranslator();
    }

    protected final Connection getConnection() throws CannotGetJdbcConnectionException {
        return DataSourceUtils.getConnection(getDataSource());
    }

    protected final void releaseConnection(Connection con) {
        DataSourceUtils.releaseConnection(con, getDataSource());
    }
}
