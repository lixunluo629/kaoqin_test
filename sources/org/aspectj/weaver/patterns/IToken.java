package org.aspectj.weaver.patterns;

import org.aspectj.weaver.IHasPosition;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/IToken.class */
public interface IToken extends IHasPosition {
    public static final IToken EOF = BasicToken.makeOperator("<eof>", 0, 0);

    String getString();

    boolean isIdentifier();

    String getLiteralKind();

    Pointcut maybeGetParsedPointcut();
}
