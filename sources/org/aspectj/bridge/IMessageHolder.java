package org.aspectj.bridge;

import java.util.List;
import org.aspectj.bridge.IMessage;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/IMessageHolder.class */
public interface IMessageHolder extends IMessageHandler {
    public static final boolean ORGREATER = true;
    public static final boolean EQUAL = false;

    boolean hasAnyMessage(IMessage.Kind kind, boolean z);

    int numMessages(IMessage.Kind kind, boolean z);

    IMessage[] getMessages(IMessage.Kind kind, boolean z);

    List<IMessage> getUnmodifiableListView();

    void clearMessages() throws UnsupportedOperationException;
}
