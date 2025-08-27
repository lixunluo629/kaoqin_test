package org.aspectj.weaver.tools;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/ContextBasedMatcher.class */
public interface ContextBasedMatcher {
    boolean couldMatchJoinPointsInType(Class cls);

    boolean couldMatchJoinPointsInType(Class cls, MatchingContext matchingContext);

    boolean mayNeedDynamicTest();

    FuzzyBoolean matchesStatically(MatchingContext matchingContext);

    boolean matchesDynamically(MatchingContext matchingContext);
}
