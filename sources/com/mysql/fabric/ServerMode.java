package com.mysql.fabric;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/ServerMode.class */
public enum ServerMode {
    OFFLINE,
    READ_ONLY,
    WRITE_ONLY,
    READ_WRITE;

    public static ServerMode getFromConstant(Integer constant) {
        return values()[constant.intValue()];
    }
}
