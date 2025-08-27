package org.apache.ibatis.session;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.beans.PropertyAccessor;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/AutoMappingUnknownColumnBehavior.class */
public enum AutoMappingUnknownColumnBehavior {
    NONE { // from class: org.apache.ibatis.session.AutoMappingUnknownColumnBehavior.1
        @Override // org.apache.ibatis.session.AutoMappingUnknownColumnBehavior
        public void doAction(MappedStatement mappedStatement, String columnName, String property, Class<?> propertyType) {
        }
    },
    WARNING { // from class: org.apache.ibatis.session.AutoMappingUnknownColumnBehavior.2
        @Override // org.apache.ibatis.session.AutoMappingUnknownColumnBehavior
        public void doAction(MappedStatement mappedStatement, String columnName, String property, Class<?> propertyType) {
            AutoMappingUnknownColumnBehavior.log.warn(AutoMappingUnknownColumnBehavior.buildMessage(mappedStatement, columnName, property, propertyType));
        }
    },
    FAILING { // from class: org.apache.ibatis.session.AutoMappingUnknownColumnBehavior.3
        @Override // org.apache.ibatis.session.AutoMappingUnknownColumnBehavior
        public void doAction(MappedStatement mappedStatement, String columnName, String property, Class<?> propertyType) {
            throw new SqlSessionException(AutoMappingUnknownColumnBehavior.buildMessage(mappedStatement, columnName, property, propertyType));
        }
    };

    private static final Log log = LogFactory.getLog((Class<?>) AutoMappingUnknownColumnBehavior.class);

    public abstract void doAction(MappedStatement mappedStatement, String str, String str2, Class<?> cls);

    /* JADX INFO: Access modifiers changed from: private */
    public static String buildMessage(MappedStatement mappedStatement, String columnName, String property, Class<?> propertyType) {
        return "Unknown column is detected on '" + mappedStatement.getId() + "' auto-mapping. Mapping parameters are " + PropertyAccessor.PROPERTY_KEY_PREFIX + "columnName=" + columnName + ",propertyName=" + property + ",propertyType=" + (propertyType != null ? propertyType.getName() : null) + "]";
    }
}
