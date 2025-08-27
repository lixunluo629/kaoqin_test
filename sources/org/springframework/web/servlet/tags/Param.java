package org.springframework.web.servlet.tags;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/Param.class */
public class Param {
    private String name;
    private String value;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return "JSP Tag Param: name '" + this.name + "', value '" + this.value + "'";
    }
}
