package io.netty.channel.kqueue;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/kqueue/KQueueStaticallyReferencedJniMethods.class */
final class KQueueStaticallyReferencedJniMethods {
    static native short evAdd();

    static native short evEnable();

    static native short evDisable();

    static native short evDelete();

    static native short evClear();

    static native short evEOF();

    static native short evError();

    static native short noteReadClosed();

    static native short noteConnReset();

    static native short noteDisconnected();

    static native short evfiltRead();

    static native short evfiltWrite();

    static native short evfiltUser();

    static native short evfiltSock();

    private KQueueStaticallyReferencedJniMethods() {
    }
}
