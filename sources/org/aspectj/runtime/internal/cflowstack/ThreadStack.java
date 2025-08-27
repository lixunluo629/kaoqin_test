package org.aspectj.runtime.internal.cflowstack;

import java.util.Stack;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStack.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStack.class */
public interface ThreadStack {
    Stack getThreadStack();

    void removeThreadStack();
}
