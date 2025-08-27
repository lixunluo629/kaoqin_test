package org.springframework.jdbc.object;

import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/GenericSqlQuery.class */
public class GenericSqlQuery<T> extends SqlQuery<T> {
    private RowMapper<T> rowMapper;
    private Class<? extends RowMapper> rowMapperClass;

    public void setRowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public void setRowMapperClass(Class<? extends RowMapper> rowMapperClass) {
        this.rowMapperClass = rowMapperClass;
    }

    @Override // org.springframework.jdbc.object.RdbmsOperation, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.isTrue((this.rowMapper == null && this.rowMapperClass == null) ? false : true, "'rowMapper' or 'rowMapperClass' is required");
    }

    @Override // org.springframework.jdbc.object.SqlQuery
    protected RowMapper<T> newRowMapper(Object[] parameters, Map<?, ?> context) {
        return this.rowMapper != null ? this.rowMapper : (RowMapper) BeanUtils.instantiateClass(this.rowMapperClass);
    }
}
