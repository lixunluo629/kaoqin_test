package org.aspectj.weaver;

import org.aspectj.bridge.ISourceLocation;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ISourceContext.class */
public interface ISourceContext {
    ISourceLocation makeSourceLocation(IHasPosition iHasPosition);

    ISourceLocation makeSourceLocation(int i, int i2);

    int getOffset();

    void tidy();
}
