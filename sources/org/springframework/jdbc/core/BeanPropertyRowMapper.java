package org.springframework.jdbc.core;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/BeanPropertyRowMapper.class */
public class BeanPropertyRowMapper<T> implements RowMapper<T> {
    protected final Log logger;
    private Class<T> mappedClass;
    private boolean checkFullyPopulated;
    private boolean primitivesDefaultedForNullValue;
    private ConversionService conversionService;
    private Map<String, PropertyDescriptor> mappedFields;
    private Set<String> mappedProperties;

    public BeanPropertyRowMapper() {
        this.logger = LogFactory.getLog(getClass());
        this.checkFullyPopulated = false;
        this.primitivesDefaultedForNullValue = false;
        this.conversionService = DefaultConversionService.getSharedInstance();
    }

    public BeanPropertyRowMapper(Class<T> mappedClass) throws BeansException {
        this.logger = LogFactory.getLog(getClass());
        this.checkFullyPopulated = false;
        this.primitivesDefaultedForNullValue = false;
        this.conversionService = DefaultConversionService.getSharedInstance();
        initialize(mappedClass);
    }

    public BeanPropertyRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) throws BeansException {
        this.logger = LogFactory.getLog(getClass());
        this.checkFullyPopulated = false;
        this.primitivesDefaultedForNullValue = false;
        this.conversionService = DefaultConversionService.getSharedInstance();
        initialize(mappedClass);
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public void setMappedClass(Class<T> mappedClass) throws BeansException {
        if (this.mappedClass == null) {
            initialize(mappedClass);
        } else if (this.mappedClass != mappedClass) {
            throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " + mappedClass + " since it is already providing mapping for " + this.mappedClass);
        }
    }

    public final Class<T> getMappedClass() {
        return this.mappedClass;
    }

    public void setCheckFullyPopulated(boolean checkFullyPopulated) {
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
    }

    public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
        this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
    }

    public boolean isPrimitivesDefaultedForNullValue() {
        return this.primitivesDefaultedForNullValue;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public ConversionService getConversionService() {
        return this.conversionService;
    }

    protected void initialize(Class<T> mappedClass) throws BeansException {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap();
        this.mappedProperties = new HashSet();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null) {
                this.mappedFields.put(lowerCaseName(pd.getName()), pd);
                String underscoredName = underscoreName(pd.getName());
                if (!lowerCaseName(pd.getName()).equals(underscoredName)) {
                    this.mappedFields.put(underscoredName, pd);
                }
                this.mappedProperties.add(pd.getName());
            }
        }
    }

    protected String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(lowerCaseName(name.substring(0, 1)));
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = lowerCaseName(s);
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

    protected String lowerCaseName(String name) {
        return name.toLowerCase(Locale.US);
    }

    @Override // org.springframework.jdbc.core.RowMapper
    public T mapRow(ResultSet resultSet, int i) throws SQLException {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        T t = (T) BeanUtils.instantiateClass(this.mappedClass);
        BeanWrapper beanWrapperForBeanPropertyAccess = PropertyAccessorFactory.forBeanPropertyAccess(t);
        initBeanWrapper(beanWrapperForBeanPropertyAccess);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        Set hashSet = isCheckFullyPopulated() ? new HashSet() : null;
        for (int i2 = 1; i2 <= columnCount; i2++) {
            String strLookupColumnName = JdbcUtils.lookupColumnName(metaData, i2);
            String strLowerCaseName = lowerCaseName(strLookupColumnName.replaceAll(SymbolConstants.SPACE_SYMBOL, ""));
            PropertyDescriptor propertyDescriptor = this.mappedFields.get(strLowerCaseName);
            if (propertyDescriptor != null) {
                try {
                    Object columnValue = getColumnValue(resultSet, i2, propertyDescriptor);
                    if (i == 0 && this.logger.isDebugEnabled()) {
                        this.logger.debug("Mapping column '" + strLookupColumnName + "' to property '" + propertyDescriptor.getName() + "' of type '" + ClassUtils.getQualifiedName(propertyDescriptor.getPropertyType()) + "'");
                    }
                    try {
                        beanWrapperForBeanPropertyAccess.setPropertyValue(propertyDescriptor.getName(), columnValue);
                    } catch (TypeMismatchException e) {
                        if (columnValue == null && this.primitivesDefaultedForNullValue) {
                            if (this.logger.isDebugEnabled()) {
                                this.logger.debug("Intercepted TypeMismatchException for row " + i + " and column '" + strLookupColumnName + "' with null value when setting property '" + propertyDescriptor.getName() + "' of type '" + ClassUtils.getQualifiedName(propertyDescriptor.getPropertyType()) + "' on object: " + t, e);
                            }
                        } else {
                            throw e;
                        }
                    }
                    if (hashSet != null) {
                        hashSet.add(propertyDescriptor.getName());
                    }
                } catch (NotWritablePropertyException e2) {
                    throw new DataRetrievalFailureException("Unable to map column '" + strLookupColumnName + "' to property '" + propertyDescriptor.getName() + "'", e2);
                }
            } else if (i == 0 && this.logger.isDebugEnabled()) {
                this.logger.debug("No property found for column '" + strLookupColumnName + "' mapped to field '" + strLowerCaseName + "'");
            }
        }
        if (hashSet != null && !hashSet.equals(this.mappedProperties)) {
            throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields necessary to populate object of class [" + this.mappedClass.getName() + "]: " + this.mappedProperties);
        }
        return t;
    }

    protected void initBeanWrapper(BeanWrapper bw) {
        ConversionService cs = getConversionService();
        if (cs != null) {
            bw.setConversionService(cs);
        }
    }

    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

    public static <T> BeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
        return new BeanPropertyRowMapper<>(mappedClass);
    }
}
