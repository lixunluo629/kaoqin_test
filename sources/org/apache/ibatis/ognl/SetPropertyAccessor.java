package org.apache.ibatis.ognl;

import java.util.Map;
import java.util.Set;
import org.springframework.web.servlet.tags.form.InputTag;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/SetPropertyAccessor.class */
public class SetPropertyAccessor extends ObjectPropertyAccessor implements PropertyAccessor {
    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
        Object result;
        Set set = (Set) target;
        if (name instanceof String) {
            if (name.equals(InputTag.SIZE_ATTRIBUTE)) {
                result = new Integer(set.size());
            } else if (name.equals("iterator")) {
                result = set.iterator();
            } else if (name.equals("isEmpty")) {
                result = set.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
            } else {
                result = super.getProperty(context, target, name);
            }
            return result;
        }
        throw new NoSuchPropertyException(target, name);
    }
}
