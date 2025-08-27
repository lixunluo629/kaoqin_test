package org.apache.ibatis.ognl;

import java.security.BasicPermission;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlInvokePermission.class */
public class OgnlInvokePermission extends BasicPermission {
    public OgnlInvokePermission(String name) {
        super(name);
    }

    public OgnlInvokePermission(String name, String actions) {
        super(name, actions);
    }
}
