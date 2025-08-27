package tk.mybatis.mapper.util;

import tk.mybatis.mapper.entity.Example;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/util/OGNL.class */
public abstract class OGNL {
    public static boolean hasSelectColumns(Object parameter) {
        if (parameter != null && (parameter instanceof Example)) {
            Example example = (Example) parameter;
            if (example.getSelectColumns() != null && example.getSelectColumns().size() > 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean hasNoSelectColumns(Object parameter) {
        return !hasSelectColumns(parameter);
    }
}
