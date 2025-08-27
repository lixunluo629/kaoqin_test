package net.dongliu.apk.parser.bean;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/Permission.class */
public class Permission {
    private String name;
    private String label;
    private String icon;
    private String description;
    private String group;
    private String protectionLevel;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getProtectionLevel() {
        return this.protectionLevel;
    }

    public void setProtectionLevel(String protectionLevel) {
        this.protectionLevel = protectionLevel;
    }
}
