package io.netty.channel.unix;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/unix/ErrorsStaticallyReferencedJniMethods.class */
final class ErrorsStaticallyReferencedJniMethods {
    static native int errnoENOENT();

    static native int errnoEBADF();

    static native int errnoEPIPE();

    static native int errnoECONNRESET();

    static native int errnoENOTCONN();

    static native int errnoEAGAIN();

    static native int errnoEWOULDBLOCK();

    static native int errnoEINPROGRESS();

    static native int errorECONNREFUSED();

    static native int errorEISCONN();

    static native int errorEALREADY();

    static native int errorENETUNREACH();

    static native String strError(int i);

    private ErrorsStaticallyReferencedJniMethods() {
    }
}
