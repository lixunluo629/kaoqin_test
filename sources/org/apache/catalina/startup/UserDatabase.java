package org.apache.catalina.startup;

import java.util.Enumeration;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/startup/UserDatabase.class */
public interface UserDatabase {
    UserConfig getUserConfig();

    void setUserConfig(UserConfig userConfig);

    String getHome(String str);

    Enumeration<String> getUsers();
}
