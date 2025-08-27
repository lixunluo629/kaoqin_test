package com.adobe.xmp.impl.xpath;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/xpath/XMPPathSegment.class */
public class XMPPathSegment {
    private String name;
    private int kind;
    private boolean alias;
    private int aliasForm;

    public XMPPathSegment(String str) {
        this.name = str;
    }

    public XMPPathSegment(String str, int i) {
        this.name = str;
        this.kind = i;
    }

    public int getKind() {
        return this.kind;
    }

    public void setKind(int i) {
        this.kind = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setAlias(boolean z) {
        this.alias = z;
    }

    public boolean isAlias() {
        return this.alias;
    }

    public int getAliasForm() {
        return this.aliasForm;
    }

    public void setAliasForm(int i) {
        this.aliasForm = i;
    }

    public String toString() {
        switch (this.kind) {
        }
        return this.name;
    }
}
