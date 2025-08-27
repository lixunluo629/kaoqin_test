package org.aspectj.weaver.tools;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/JoinPointMatch.class */
public interface JoinPointMatch {
    boolean matches();

    PointcutParameter[] getParameterBindings();
}
