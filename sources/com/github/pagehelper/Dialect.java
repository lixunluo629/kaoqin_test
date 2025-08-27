package com.github.pagehelper;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/Dialect.class */
public enum Dialect {
    mysql,
    mariadb,
    sqlite,
    oracle,
    hsqldb,
    postgresql,
    sqlserver,
    db2,
    informix;

    public static Dialect of(String dialect) {
        String string;
        try {
            return valueOf(dialect);
        } catch (IllegalArgumentException e) {
            String dialects = null;
            Dialect[] arr$ = values();
            for (Dialect d : arr$) {
                if (dialects == null) {
                    string = d.toString();
                } else {
                    string = dialects + "," + d;
                }
                dialects = string;
            }
            throw new IllegalArgumentException("Mybatis分页插件dialect参数值错误，可选值为[" + dialects + "]");
        }
    }
}
