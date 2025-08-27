package org.aspectj.weaver;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/StandardAnnotation.class */
public class StandardAnnotation extends AbstractAnnotationAJ {
    private final boolean isRuntimeVisible;
    private List<AnnotationNameValuePair> nvPairs;

    public StandardAnnotation(ResolvedType type, boolean isRuntimeVisible) {
        super(type);
        this.nvPairs = null;
        this.isRuntimeVisible = isRuntimeVisible;
    }

    @Override // org.aspectj.weaver.AbstractAnnotationAJ, org.aspectj.weaver.AnnotationAJ
    public boolean isRuntimeVisible() {
        return this.isRuntimeVisible;
    }

    @Override // org.aspectj.weaver.AbstractAnnotationAJ, org.aspectj.weaver.AnnotationAJ
    public String stringify() {
        StringBuffer sb = new StringBuffer();
        sb.append("@").append(this.type.getClassName());
        if (hasNameValuePairs()) {
            sb.append("(");
            for (AnnotationNameValuePair nvPair : this.nvPairs) {
                sb.append(nvPair.stringify());
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Anno[" + getTypeSignature() + SymbolConstants.SPACE_SYMBOL + (this.isRuntimeVisible ? "rVis" : "rInvis"));
        if (this.nvPairs != null) {
            sb.append(SymbolConstants.SPACE_SYMBOL);
            Iterator<AnnotationNameValuePair> iter = this.nvPairs.iterator();
            while (iter.hasNext()) {
                AnnotationNameValuePair element = iter.next();
                sb.append(element.toString());
                if (iter.hasNext()) {
                    sb.append(",");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override // org.aspectj.weaver.AbstractAnnotationAJ, org.aspectj.weaver.AnnotationAJ
    public boolean hasNamedValue(String n) {
        if (this.nvPairs == null) {
            return false;
        }
        for (int i = 0; i < this.nvPairs.size(); i++) {
            AnnotationNameValuePair pair = this.nvPairs.get(i);
            if (pair.getName().equals(n)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.AbstractAnnotationAJ, org.aspectj.weaver.AnnotationAJ
    public boolean hasNameValuePair(String n, String v) {
        if (this.nvPairs == null) {
            return false;
        }
        for (int i = 0; i < this.nvPairs.size(); i++) {
            AnnotationNameValuePair pair = this.nvPairs.get(i);
            if (pair.getName().equals(n) && pair.getValue().stringify().equals(v)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.AbstractAnnotationAJ, org.aspectj.weaver.AnnotationAJ
    public Set<String> getTargets() {
        if (!this.type.equals(UnresolvedType.AT_TARGET)) {
            return Collections.emptySet();
        }
        AnnotationNameValuePair nvp = this.nvPairs.get(0);
        ArrayAnnotationValue aav = (ArrayAnnotationValue) nvp.getValue();
        AnnotationValue[] avs = aav.getValues();
        Set<String> targets = new HashSet<>();
        for (AnnotationValue annotationValue : avs) {
            EnumAnnotationValue value = (EnumAnnotationValue) annotationValue;
            targets.add(value.getValue());
        }
        return targets;
    }

    public List<AnnotationNameValuePair> getNameValuePairs() {
        return this.nvPairs;
    }

    public boolean hasNameValuePairs() {
        return (this.nvPairs == null || this.nvPairs.size() == 0) ? false : true;
    }

    public void addNameValuePair(AnnotationNameValuePair pair) {
        if (this.nvPairs == null) {
            this.nvPairs = new ArrayList();
        }
        this.nvPairs.add(pair);
    }

    @Override // org.aspectj.weaver.AnnotationAJ
    public String getStringFormOfValue(String name) {
        if (hasNameValuePairs()) {
            for (AnnotationNameValuePair nvPair : this.nvPairs) {
                if (nvPair.getName().equals(name)) {
                    return nvPair.getValue().stringify();
                }
            }
            return null;
        }
        return null;
    }
}
