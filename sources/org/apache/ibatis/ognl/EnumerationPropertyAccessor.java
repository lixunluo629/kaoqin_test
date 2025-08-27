package org.apache.ibatis.ognl;

import java.util.Enumeration;
import java.util.Map;
import org.springframework.hateoas.Link;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/EnumerationPropertyAccessor.class */
public class EnumerationPropertyAccessor extends ObjectPropertyAccessor implements PropertyAccessor {
    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
        Object result;
        Enumeration e = (Enumeration) target;
        if (name instanceof String) {
            if (name.equals(Link.REL_NEXT) || name.equals("nextElement")) {
                result = e.nextElement();
            } else if (name.equals("hasNext") || name.equals("hasMoreElements")) {
                result = e.hasMoreElements() ? Boolean.TRUE : Boolean.FALSE;
            } else {
                result = super.getProperty(context, target, name);
            }
        } else {
            result = super.getProperty(context, target, name);
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
        throw new IllegalArgumentException("can't set property " + name + " on Enumeration");
    }
}
