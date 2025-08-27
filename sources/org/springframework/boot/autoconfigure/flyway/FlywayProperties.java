package org.springframework.boot.autoconfigure.flyway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "flyway", ignoreUnknownFields = true)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayProperties.class */
public class FlywayProperties {
    private String user;
    private String password;
    private String url;
    private List<String> locations = new ArrayList(Collections.singletonList("db/migration"));
    private boolean checkLocation = false;
    private boolean enabled = true;
    private List<String> initSqls = new ArrayList();

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getLocations() {
        return this.locations;
    }

    public void setCheckLocation(boolean checkLocation) {
        this.checkLocation = checkLocation;
    }

    public boolean isCheckLocation() {
        return this.checkLocation;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password != null ? this.password : "";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getInitSqls() {
        return this.initSqls;
    }

    public void setInitSqls(List<String> initSqls) {
        this.initSqls = initSqls;
    }

    public boolean isCreateDataSource() {
        return (this.url == null || this.user == null) ? false : true;
    }
}
