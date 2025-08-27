package org.apache.ibatis.scripting.xmltags;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.ognl.OgnlException;
import org.apache.ibatis.ognl.OgnlRuntime;
import org.apache.ibatis.ognl.PropertyAccessor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/DynamicContext.class */
public class DynamicContext {
    public static final String PARAMETER_OBJECT_KEY = "_parameter";
    public static final String DATABASE_ID_KEY = "_databaseId";
    private final ContextMap bindings;
    private final StringBuilder sqlBuilder = new StringBuilder();
    private int uniqueNumber = 0;

    static {
        OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
    }

    public DynamicContext(Configuration configuration, Object parameterObject) {
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            this.bindings = new ContextMap(metaObject);
        } else {
            this.bindings = new ContextMap(null);
        }
        this.bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
        this.bindings.put(DATABASE_ID_KEY, configuration.getDatabaseId());
    }

    public Map<String, Object> getBindings() {
        return this.bindings;
    }

    public void bind(String name, Object value) {
        this.bindings.put(name, value);
    }

    public void appendSql(String sql) {
        this.sqlBuilder.append(sql);
        this.sqlBuilder.append(SymbolConstants.SPACE_SYMBOL);
    }

    public String getSql() {
        return this.sqlBuilder.toString().trim();
    }

    public int getUniqueNumber() {
        int i = this.uniqueNumber;
        this.uniqueNumber = i + 1;
        return i;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/DynamicContext$ContextMap.class */
    static class ContextMap extends HashMap<String, Object> {
        private static final long serialVersionUID = 2977601501966151582L;
        private MetaObject parameterMetaObject;

        public ContextMap(MetaObject parameterMetaObject) {
            this.parameterMetaObject = parameterMetaObject;
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public Object get(Object key) {
            String strKey = (String) key;
            if (super.containsKey(strKey)) {
                return super.get(strKey);
            }
            if (this.parameterMetaObject != null) {
                return this.parameterMetaObject.getValue(strKey);
            }
            return null;
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/DynamicContext$ContextAccessor.class */
    static class ContextAccessor implements PropertyAccessor {
        ContextAccessor() {
        }

        @Override // org.apache.ibatis.ognl.PropertyAccessor
        public Object getProperty(Map context, Object target, Object name) throws OgnlException {
            Map map = (Map) target;
            Object result = map.get(name);
            if (map.containsKey(name) || result != null) {
                return result;
            }
            Object parameterObject = map.get(DynamicContext.PARAMETER_OBJECT_KEY);
            if (parameterObject instanceof Map) {
                return ((Map) parameterObject).get(name);
            }
            return null;
        }

        @Override // org.apache.ibatis.ognl.PropertyAccessor
        public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
            Map<Object, Object> map = (Map) target;
            map.put(name, value);
        }

        @Override // org.apache.ibatis.ognl.PropertyAccessor
        public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }

        @Override // org.apache.ibatis.ognl.PropertyAccessor
        public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }
    }
}
