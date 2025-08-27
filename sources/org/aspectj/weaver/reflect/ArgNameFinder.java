package org.aspectj.weaver.reflect;

import java.lang.reflect.Member;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/ArgNameFinder.class */
public interface ArgNameFinder {
    String[] getParameterNames(Member member);
}
