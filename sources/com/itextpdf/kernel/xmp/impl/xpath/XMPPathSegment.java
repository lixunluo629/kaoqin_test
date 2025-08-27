package com.itextpdf.kernel.xmp.impl.xpath;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/xpath/XMPPathSegment.class */
public class XMPPathSegment {
    private String name;
    private int kind;
    private boolean alias;
    private int aliasForm;

    public XMPPathSegment(String name) {
        this.name = name;
    }

    public XMPPathSegment(String name, int kind) {
        this.name = name;
        this.kind = kind;
    }

    public int getKind() {
        return this.kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlias(boolean alias) {
        this.alias = alias;
    }

    public boolean isAlias() {
        return this.alias;
    }

    public int getAliasForm() {
        return this.aliasForm;
    }

    public void setAliasForm(int aliasForm) {
        this.aliasForm = aliasForm;
    }

    public String toString() {
        switch (this.kind) {
        }
        return this.name;
    }
}
