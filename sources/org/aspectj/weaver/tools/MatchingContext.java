package org.aspectj.weaver.tools;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/MatchingContext.class */
public interface MatchingContext {
    boolean hasContextBinding(String str);

    Object getBinding(String str);
}
