package net.sf.jsqlparser.expression;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/Alias.class */
public class Alias {
    private String name;
    private boolean useAs;

    public Alias(String name) {
        this.useAs = true;
        this.name = name;
    }

    public Alias(String name, boolean useAs) {
        this.useAs = true;
        this.name = name;
        this.useAs = useAs;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUseAs() {
        return this.useAs;
    }

    public void setUseAs(boolean useAs) {
        this.useAs = useAs;
    }

    public String toString() {
        return (this.useAs ? " AS " : SymbolConstants.SPACE_SYMBOL) + this.name;
    }
}
