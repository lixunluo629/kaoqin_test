package org.aspectj.weaver;

import org.aspectj.bridge.ISourceLocation;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/IHasSourceLocation.class */
public interface IHasSourceLocation extends IHasPosition {
    ISourceContext getSourceContext();

    ISourceLocation getSourceLocation();
}
