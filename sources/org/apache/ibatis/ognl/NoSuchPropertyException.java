package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/NoSuchPropertyException.class */
public class NoSuchPropertyException extends OgnlException {
    private Object target;
    private Object name;

    public NoSuchPropertyException(Object target, Object name) {
        super(getReason(target, name));
    }

    public NoSuchPropertyException(Object target, Object name, Throwable reason) {
        super(getReason(target, name), reason);
        this.target = target;
        this.name = name;
    }

    static String getReason(Object target, Object name) {
        String ret;
        if (target == null) {
            ret = "null";
        } else if (target instanceof Class) {
            ret = ((Class) target).getName();
        } else {
            ret = target.getClass().getName();
        }
        return ret + "." + name;
    }

    public Object getTarget() {
        return this.target;
    }

    public Object getName() {
        return this.name;
    }
}
