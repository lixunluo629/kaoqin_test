package org.springframework.jdbc.core.namedparam;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/NamedParameterJdbcDaoSupport.class */
public class NamedParameterJdbcDaoSupport extends JdbcDaoSupport {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override // org.springframework.jdbc.core.support.JdbcDaoSupport
    protected void initTemplateConfig() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return this.namedParameterJdbcTemplate;
    }
}
