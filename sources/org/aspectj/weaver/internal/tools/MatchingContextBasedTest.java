package org.aspectj.weaver.internal.tools;

import org.aspectj.weaver.ast.ITestVisitor;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.tools.ContextBasedMatcher;
import org.aspectj.weaver.tools.MatchingContext;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/internal/tools/MatchingContextBasedTest.class */
public class MatchingContextBasedTest extends Test {
    private final ContextBasedMatcher matcher;

    public MatchingContextBasedTest(ContextBasedMatcher pc) {
        this.matcher = pc;
    }

    @Override // org.aspectj.weaver.ast.Test
    public void accept(ITestVisitor v) {
        v.visit(this);
    }

    public boolean matches(MatchingContext context) {
        return this.matcher.matchesDynamically(context);
    }
}
