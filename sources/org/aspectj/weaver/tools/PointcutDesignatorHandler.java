package org.aspectj.weaver.tools;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/PointcutDesignatorHandler.class */
public interface PointcutDesignatorHandler {
    String getDesignatorName();

    ContextBasedMatcher parse(String str);
}
