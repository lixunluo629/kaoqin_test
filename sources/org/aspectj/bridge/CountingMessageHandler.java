package org.aspectj.bridge;

import java.util.Enumeration;
import java.util.Hashtable;
import org.aspectj.bridge.IMessage;
import org.aspectj.util.LangUtil;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/CountingMessageHandler.class */
public class CountingMessageHandler implements IMessageHandler {
    public final IMessageHandler delegate;
    public final CountingMessageHandler proxy;
    private final Hashtable<IMessage.Kind, IntHolder> counters;

    public static CountingMessageHandler makeCountingMessageHandler(IMessageHandler handler) {
        if (handler instanceof CountingMessageHandler) {
            return (CountingMessageHandler) handler;
        }
        return new CountingMessageHandler(handler);
    }

    public CountingMessageHandler(IMessageHandler delegate) {
        LangUtil.throwIaxIfNull(delegate, "delegate");
        this.delegate = delegate;
        this.counters = new Hashtable<>();
        this.proxy = delegate instanceof CountingMessageHandler ? (CountingMessageHandler) delegate : null;
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public boolean handleMessage(IMessage message) throws AbortException {
        if (null != this.proxy) {
            return this.proxy.handleMessage(message);
        }
        if (null != message) {
            IMessage.Kind kind = message.getKind();
            if (!isIgnoring(kind)) {
                increment(kind);
            }
        }
        return this.delegate.handleMessage(message);
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public boolean isIgnoring(IMessage.Kind kind) {
        return this.delegate.isIgnoring(kind);
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public void dontIgnore(IMessage.Kind kind) {
        this.delegate.dontIgnore(kind);
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public void ignore(IMessage.Kind kind) {
        this.delegate.ignore(kind);
    }

    public String toString() {
        return this.delegate.toString();
    }

    public int numMessages(IMessage.Kind kind, boolean orGreater) {
        if (null != this.proxy) {
            return this.proxy.numMessages(kind, orGreater);
        }
        int result = 0;
        if (null == kind) {
            Enumeration<IntHolder> enu = this.counters.elements();
            while (enu.hasMoreElements()) {
                result += enu.nextElement().count;
            }
        } else if (!orGreater) {
            result = numMessages(kind);
        } else {
            for (IMessage.Kind k : IMessage.KINDS) {
                if (kind.isSameOrLessThan(k)) {
                    result += numMessages(k);
                }
            }
        }
        return result;
    }

    public boolean hasErrors() {
        return 0 < numMessages(IMessage.ERROR, true);
    }

    private int numMessages(IMessage.Kind kind) {
        if (null != this.proxy) {
            return this.proxy.numMessages(kind);
        }
        IntHolder counter = this.counters.get(kind);
        if (null == counter) {
            return 0;
        }
        return counter.count;
    }

    private void increment(IMessage.Kind kind) {
        if (null != this.proxy) {
            throw new IllegalStateException("not called when proxying");
        }
        IntHolder counter = this.counters.get(kind);
        if (null == counter) {
            counter = new IntHolder();
            this.counters.put(kind, counter);
        }
        counter.count++;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/CountingMessageHandler$IntHolder.class */
    private static class IntHolder {
        int count;

        private IntHolder() {
        }
    }

    public void reset() {
        if (this.proxy != null) {
            this.proxy.reset();
        }
        this.counters.clear();
    }
}
