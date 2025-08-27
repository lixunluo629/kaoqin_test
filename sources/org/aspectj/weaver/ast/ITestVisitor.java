package org.aspectj.weaver.ast;

import org.aspectj.weaver.internal.tools.MatchingContextBasedTest;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/ITestVisitor.class */
public interface ITestVisitor {
    void visit(And and);

    void visit(Instanceof r1);

    void visit(Not not);

    void visit(Or or);

    void visit(Literal literal);

    void visit(Call call);

    void visit(FieldGetCall fieldGetCall);

    void visit(HasAnnotation hasAnnotation);

    void visit(MatchingContextBasedTest matchingContextBasedTest);
}
