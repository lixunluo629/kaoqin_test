package net.dongliu.apk.parser.bean;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/UseFeature.class */
public class UseFeature {
    private String name;
    private boolean required = true;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String toString() {
        return this.name;
    }
}
