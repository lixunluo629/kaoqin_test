package org.apache.tomcat.jni;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/jni/Error.class */
public class Error extends Exception {
    private static final long serialVersionUID = 1;
    private final int error;
    private final String description;

    public static native int osError();

    public static native int netosError();

    public static native String strerror(int i);

    private Error(int error, String description) {
        super(error + ": " + description);
        this.error = error;
        this.description = description;
    }

    public int getError() {
        return this.error;
    }

    public String getDescription() {
        return this.description;
    }
}
