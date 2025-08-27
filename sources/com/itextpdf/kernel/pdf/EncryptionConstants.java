package com.itextpdf.kernel.pdf;

import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/EncryptionConstants.class */
public class EncryptionConstants implements Serializable {
    private static final long serialVersionUID = 6234590207803219761L;
    public static final int STANDARD_ENCRYPTION_40 = 0;
    public static final int STANDARD_ENCRYPTION_128 = 1;
    public static final int ENCRYPTION_AES_128 = 2;
    public static final int ENCRYPTION_AES_256 = 3;
    public static final int DO_NOT_ENCRYPT_METADATA = 8;
    public static final int EMBEDDED_FILES_ONLY = 24;
    public static final int ALLOW_PRINTING = 2052;
    public static final int ALLOW_MODIFY_CONTENTS = 8;
    public static final int ALLOW_COPY = 16;
    public static final int ALLOW_MODIFY_ANNOTATIONS = 32;
    public static final int ALLOW_FILL_IN = 256;
    public static final int ALLOW_SCREENREADERS = 512;
    public static final int ALLOW_ASSEMBLY = 1024;
    public static final int ALLOW_DEGRADED_PRINTING = 4;
    static final int ENCRYPTION_MASK = 7;
}
