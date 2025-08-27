package org.apache.tomcat.util.http.fileupload;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/fileupload/FileUploadException.class */
public class FileUploadException extends Exception {
    private static final long serialVersionUID = -4222909057964038517L;

    public FileUploadException() {
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(Throwable cause) {
        super(cause);
    }
}
