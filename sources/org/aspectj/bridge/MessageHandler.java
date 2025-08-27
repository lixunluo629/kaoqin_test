package org.aspectj.bridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.aspectj.bridge.IMessage;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/MessageHandler.class */
public class MessageHandler implements IMessageHolder {
    protected final ArrayList<IMessage> messages;
    protected final ArrayList<IMessage.Kind> ignoring;
    protected boolean handleMessageResult;
    protected IMessageHandler interceptor;

    public MessageHandler() {
        this(false);
    }

    public MessageHandler(boolean accumulateOnly) {
        this.messages = new ArrayList<>();
        this.ignoring = new ArrayList<>();
        init(accumulateOnly);
        ignore(IMessage.WEAVEINFO);
    }

    public void init() {
        init(false);
    }

    public void init(boolean accumulateOnly) {
        this.handleMessageResult = accumulateOnly;
        if (0 < this.messages.size()) {
            this.messages.clear();
        }
        if (0 < this.ignoring.size()) {
            boolean ignoringWeaveMessages = isIgnoring(IMessage.WEAVEINFO);
            this.ignoring.clear();
            if (ignoringWeaveMessages) {
                ignore(IMessage.WEAVEINFO);
            }
        }
        if (null != this.interceptor) {
            this.interceptor = null;
        }
    }

    @Override // org.aspectj.bridge.IMessageHolder
    public void clearMessages() {
        if (0 < this.messages.size()) {
            this.messages.clear();
        }
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public boolean handleMessage(IMessage message) {
        if (null != this.interceptor && this.interceptor.handleMessage(message)) {
            return true;
        }
        if (null == message) {
            throw new IllegalArgumentException("null message");
        }
        if (!this.ignoring.contains(message.getKind())) {
            this.messages.add(message);
        }
        return this.handleMessageResult;
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public boolean isIgnoring(IMessage.Kind kind) {
        return null != kind && this.ignoring.contains(kind);
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public void ignore(IMessage.Kind kind) {
        if (null != kind && !this.ignoring.contains(kind)) {
            this.ignoring.add(kind);
        }
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public void dontIgnore(IMessage.Kind kind) {
        if (null != kind) {
            this.ignoring.remove(kind);
        }
    }

    @Override // org.aspectj.bridge.IMessageHolder
    public boolean hasAnyMessage(IMessage.Kind kind, boolean orGreater) {
        if (null == kind) {
            return 0 < this.messages.size();
        }
        if (!orGreater) {
            Iterator<IMessage> it = this.messages.iterator();
            while (it.hasNext()) {
                IMessage m = it.next();
                if (kind == m.getKind()) {
                    return true;
                }
            }
            return false;
        }
        Iterator<IMessage> it2 = this.messages.iterator();
        while (it2.hasNext()) {
            IMessage m2 = it2.next();
            if (kind.isSameOrLessThan(m2.getKind())) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.bridge.IMessageHolder
    public int numMessages(IMessage.Kind kind, boolean orGreater) {
        if (null == kind) {
            return this.messages.size();
        }
        int result = 0;
        if (!orGreater) {
            Iterator<IMessage> it = this.messages.iterator();
            while (it.hasNext()) {
                IMessage m = it.next();
                if (kind == m.getKind()) {
                    result++;
                }
            }
        } else {
            Iterator<IMessage> it2 = this.messages.iterator();
            while (it2.hasNext()) {
                IMessage m2 = it2.next();
                if (kind.isSameOrLessThan(m2.getKind())) {
                    result++;
                }
            }
        }
        return result;
    }

    @Override // org.aspectj.bridge.IMessageHolder
    public List<IMessage> getUnmodifiableListView() {
        return Collections.unmodifiableList(this.messages);
    }

    @Override // org.aspectj.bridge.IMessageHolder
    public IMessage[] getMessages(IMessage.Kind kind, boolean orGreater) {
        if (null == kind) {
            return (IMessage[]) this.messages.toArray(IMessage.RA_IMessage);
        }
        ArrayList<IMessage> result = new ArrayList<>();
        if (!orGreater) {
            Iterator<IMessage> it = this.messages.iterator();
            while (it.hasNext()) {
                IMessage m = it.next();
                if (kind == m.getKind()) {
                    result.add(m);
                }
            }
        } else {
            Iterator<IMessage> it2 = this.messages.iterator();
            while (it2.hasNext()) {
                IMessage m2 = it2.next();
                if (kind.isSameOrLessThan(m2.getKind())) {
                    result.add(m2);
                }
            }
        }
        if (0 == result.size()) {
            return IMessage.RA_IMessage;
        }
        return (IMessage[]) result.toArray(IMessage.RA_IMessage);
    }

    public IMessage[] getErrors() {
        return getMessages(IMessage.ERROR, false);
    }

    public IMessage[] getWarnings() {
        return getMessages(IMessage.WARNING, false);
    }

    public void setInterceptor(IMessageHandler interceptor) {
        this.interceptor = interceptor;
    }

    public String toString() {
        if (0 == this.messages.size()) {
            return "MessageHandler: no messages";
        }
        return "MessageHandler: " + this.messages;
    }
}
