package org.aspectj.bridge;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/ICommand.class */
public interface ICommand {
    boolean runCommand(String[] strArr, IMessageHandler iMessageHandler);

    boolean repeatCommand(IMessageHandler iMessageHandler);
}
