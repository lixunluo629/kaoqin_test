package org.apache.poi.hssf.record.crypto;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/crypto/Biff8EncryptionKey.class */
public final class Biff8EncryptionKey {
    private static final ThreadLocal<String> _userPasswordTLS = new ThreadLocal<>();

    public static void setCurrentUserPassword(String password) {
        if (password == null) {
            _userPasswordTLS.remove();
        } else {
            _userPasswordTLS.set(password);
        }
    }

    public static String getCurrentUserPassword() {
        return _userPasswordTLS.get();
    }
}
