package org.apache.ibatis.reflection;

import org.apache.ibatis.io.Resources;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/Jdk.class */
public class Jdk {
    public static final boolean parameterExists;
    public static final boolean dateAndTimeApiExists;

    static {
        boolean available = false;
        try {
            Resources.classForName("java.lang.reflect.Parameter");
            available = true;
        } catch (ClassNotFoundException e) {
        }
        parameterExists = available;
        boolean available2 = false;
        try {
            Resources.classForName("java.time.Clock");
            available2 = true;
        } catch (ClassNotFoundException e2) {
        }
        dateAndTimeApiExists = available2;
    }

    private Jdk() {
    }
}
