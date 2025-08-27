package com.mysql.fabric;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/ServerRole.class */
public enum ServerRole {
    FAULTY,
    SPARE,
    SECONDARY,
    PRIMARY,
    CONFIGURING;

    public static ServerRole getFromConstant(Integer constant) {
        return values()[constant.intValue()];
    }
}
