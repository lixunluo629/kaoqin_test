package org.aspectj.bridge;

import java.io.PrintWriter;
import org.aspectj.bridge.IMessage;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/MessageWriter.class */
public class MessageWriter implements IMessageHandler {
    protected PrintWriter writer;
    protected boolean abortOnFailure;

    public MessageWriter(PrintWriter writer, boolean abortOnFailure) {
        this.writer = null != writer ? writer : new PrintWriter(System.out);
        this.abortOnFailure = abortOnFailure;
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public boolean handleMessage(IMessage message) throws AbortException {
        String result;
        if (null != message && !isIgnoring(message.getKind()) && null != (result = render(message))) {
            this.writer.println(result);
            this.writer.flush();
            if (this.abortOnFailure) {
                if (message.isFailed() || message.isAbort()) {
                    throw new AbortException(message);
                }
                return true;
            }
            return true;
        }
        return true;
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public boolean isIgnoring(IMessage.Kind kind) {
        return false;
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public void dontIgnore(IMessage.Kind kind) {
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public void ignore(IMessage.Kind kind) {
    }

    protected String render(IMessage message) {
        return message.toString();
    }
}
