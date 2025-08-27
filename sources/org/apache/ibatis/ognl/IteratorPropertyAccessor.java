package org.apache.ibatis.ognl;

import java.util.Iterator;
import java.util.Map;
import org.springframework.hateoas.Link;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/IteratorPropertyAccessor.class */
public class IteratorPropertyAccessor extends ObjectPropertyAccessor implements PropertyAccessor {
    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
        Object result;
        Iterator iterator = (Iterator) target;
        if (name instanceof String) {
            if (name.equals(Link.REL_NEXT)) {
                result = iterator.next();
            } else if (name.equals("hasNext")) {
                result = iterator.hasNext() ? Boolean.TRUE : Boolean.FALSE;
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
        throw new IllegalArgumentException("can't set property " + name + " on Iterator");
    }
}
