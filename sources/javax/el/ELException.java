package javax.el;

/* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/ELException.class */
public class ELException extends RuntimeException {
    private static final long serialVersionUID = -6228042809457459161L;

    public ELException() {
    }

    public ELException(String message) {
        super(message);
    }

    public ELException(Throwable cause) {
        super(cause);
    }

    public ELException(String message, Throwable cause) {
        super(message, cause);
    }
}
