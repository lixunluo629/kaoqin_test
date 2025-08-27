package org.aspectj.weaver;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/AnnotationNameValuePair.class */
public class AnnotationNameValuePair {
    private String name;
    private AnnotationValue val;

    public AnnotationNameValuePair(String name, AnnotationValue val) {
        this.name = name;
        this.val = val;
    }

    public String getName() {
        return this.name;
    }

    public AnnotationValue getValue() {
        return this.val;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.name + SymbolConstants.EQUAL_SYMBOL + this.val.toString());
        return sb.toString();
    }

    public String stringify() {
        StringBuffer sb = new StringBuffer();
        if (!this.name.equals("value")) {
            sb.append(this.name + SymbolConstants.EQUAL_SYMBOL);
        }
        sb.append(this.val.stringify());
        return sb.toString();
    }
}
