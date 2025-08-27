package org.aspectj.weaver.tools;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/ShadowMatch.class */
public interface ShadowMatch {
    boolean alwaysMatches();

    boolean maybeMatches();

    boolean neverMatches();

    JoinPointMatch matchesJoinPoint(Object obj, Object obj2, Object[] objArr);

    void setMatchingContext(MatchingContext matchingContext);
}
