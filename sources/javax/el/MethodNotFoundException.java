package javax.el;

/* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/MethodNotFoundException.class */
public class MethodNotFoundException extends ELException {
    private static final long serialVersionUID = -3631968116081480328L;

    public MethodNotFoundException() {
    }

    public MethodNotFoundException(String message) {
        super(message);
    }

    public MethodNotFoundException(Throwable cause) {
        super(cause);
    }

    public MethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
