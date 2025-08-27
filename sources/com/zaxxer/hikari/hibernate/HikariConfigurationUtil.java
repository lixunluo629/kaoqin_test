package com.zaxxer.hikari.hibernate;

import com.mysql.jdbc.NonRegisteringDriver;
import com.zaxxer.hikari.HikariConfig;
import java.util.Map;
import java.util.Properties;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/hibernate/HikariConfigurationUtil.class */
public class HikariConfigurationUtil {
    public static final String CONFIG_PREFIX = "hibernate.hikari.";
    public static final String CONFIG_PREFIX_DATASOURCE = "hibernate.hikari.dataSource.";

    public static HikariConfig loadConfiguration(Map props) {
        Properties hikariProps = new Properties();
        copyProperty("hibernate.connection.isolation", props, "transactionIsolation", hikariProps);
        copyProperty("hibernate.connection.autocommit", props, "autoCommit", hikariProps);
        copyProperty("hibernate.connection.driver_class", props, "driverClassName", hikariProps);
        copyProperty("hibernate.connection.url", props, "jdbcUrl", hikariProps);
        copyProperty("hibernate.connection.username", props, "username", hikariProps);
        copyProperty("hibernate.connection.password", props, NonRegisteringDriver.PASSWORD_PROPERTY_KEY, hikariProps);
        for (Object keyo : props.keySet()) {
            String key = (String) keyo;
            if (key.startsWith(CONFIG_PREFIX)) {
                hikariProps.setProperty(key.substring(CONFIG_PREFIX.length()), (String) props.get(key));
            }
        }
        return new HikariConfig(hikariProps);
    }

    private static void copyProperty(String srcKey, Map src, String dstKey, Properties dst) {
        if (src.containsKey(srcKey)) {
            dst.setProperty(dstKey, (String) src.get(srcKey));
        }
    }
}
