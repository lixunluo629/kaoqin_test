package org.aspectj.weaver.loadtime;

import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/DefaultMessageHandler.class */
public class DefaultMessageHandler implements IMessageHandler {
    boolean isVerbose = false;
    boolean isDebug = false;
    boolean showWeaveInfo = false;
    boolean showWarn = true;

    @Override // org.aspectj.bridge.IMessageHandler
    public boolean handleMessage(IMessage message) throws AbortException {
        if (isIgnoring(message.getKind())) {
            return false;
        }
        return SYSTEM_ERR.handleMessage(message);
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public boolean isIgnoring(IMessage.Kind kind) {
        return kind.equals(IMessage.WEAVEINFO) ? !this.showWeaveInfo : kind.isSameOrLessThan(IMessage.INFO) ? !this.isVerbose : kind.isSameOrLessThan(IMessage.DEBUG) ? !this.isDebug : !this.showWarn;
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public void dontIgnore(IMessage.Kind kind) {
        if (kind.equals(IMessage.WEAVEINFO)) {
            this.showWeaveInfo = true;
        } else if (kind.equals(IMessage.DEBUG)) {
            this.isVerbose = true;
        } else if (kind.equals(IMessage.WARNING)) {
            this.showWarn = false;
        }
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public void ignore(IMessage.Kind kind) {
        if (kind.equals(IMessage.WEAVEINFO)) {
            this.showWeaveInfo = false;
        } else if (kind.equals(IMessage.DEBUG)) {
            this.isVerbose = false;
        } else if (kind.equals(IMessage.WARNING)) {
            this.showWarn = true;
        }
    }
}
