package org.aspectj.bridge;

import java.io.OutputStream;
import java.io.PrintWriter;
import org.aspectj.bridge.IMessage;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/IMessageHandler.class */
public interface IMessageHandler {
    public static final IMessageHandler SYSTEM_ERR = new MessageWriter(new PrintWriter((OutputStream) System.err, true), true);
    public static final IMessageHandler SYSTEM_OUT = new MessageWriter(new PrintWriter((OutputStream) System.out, true), false);
    public static final IMessageHandler THROW = new IMessageHandler() { // from class: org.aspectj.bridge.IMessageHandler.1
        @Override // org.aspectj.bridge.IMessageHandler
        public boolean handleMessage(IMessage message) {
            if (message.getKind().compareTo(IMessage.ERROR) >= 0) {
                throw new AbortException(message);
            }
            return SYSTEM_OUT.handleMessage(message);
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
    };

    boolean handleMessage(IMessage iMessage) throws AbortException;

    boolean isIgnoring(IMessage.Kind kind);

    void dontIgnore(IMessage.Kind kind);

    void ignore(IMessage.Kind kind);
}
