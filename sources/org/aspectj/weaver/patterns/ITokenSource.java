package org.aspectj.weaver.patterns;

import org.aspectj.weaver.ISourceContext;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ITokenSource.class */
public interface ITokenSource {
    IToken next();

    IToken peek();

    IToken peek(int i);

    int getIndex();

    void setIndex(int i);

    ISourceContext getSourceContext();
}
