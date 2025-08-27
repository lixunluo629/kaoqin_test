package org.springframework.jdbc.core.namedparam;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/BeanPropertySqlParameterSource.class */
public class BeanPropertySqlParameterSource extends AbstractSqlParameterSource {
    private final BeanWrapper beanWrapper;
    private String[] propertyNames;

    public BeanPropertySqlParameterSource(Object object) {
        this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
    }

    @Override // org.springframework.jdbc.core.namedparam.SqlParameterSource
    public boolean hasValue(String paramName) {
        return this.beanWrapper.isReadableProperty(paramName);
    }

    @Override // org.springframework.jdbc.core.namedparam.SqlParameterSource
    public Object getValue(String paramName) throws IllegalArgumentException {
        try {
            return this.beanWrapper.getPropertyValue(paramName);
        } catch (NotReadablePropertyException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @Override // org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource, org.springframework.jdbc.core.namedparam.SqlParameterSource
    public int getSqlType(String paramName) {
        int sqlType = super.getSqlType(paramName);
        if (sqlType != Integer.MIN_VALUE) {
            return sqlType;
        }
        Class<?> propType = this.beanWrapper.getPropertyType(paramName);
        return StatementCreatorUtils.javaTypeToSqlParameterType(propType);
    }

    public String[] getReadablePropertyNames() {
        if (this.propertyNames == null) {
            List<String> names = new ArrayList<>();
            PropertyDescriptor[] props = this.beanWrapper.getPropertyDescriptors();
            for (PropertyDescriptor pd : props) {
                if (this.beanWrapper.isReadableProperty(pd.getName())) {
                    names.add(pd.getName());
                }
            }
            this.propertyNames = StringUtils.toStringArray(names);
        }
        return this.propertyNames;
    }
}
