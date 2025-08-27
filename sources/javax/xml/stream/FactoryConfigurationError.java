package javax.xml.stream;

/* loaded from: stax-api-1.0.1.jar:javax/xml/stream/FactoryConfigurationError.class */
public class FactoryConfigurationError extends Error {
    Exception nested;

    public FactoryConfigurationError() {
    }

    public FactoryConfigurationError(Exception e) {
        this.nested = e;
    }

    public FactoryConfigurationError(Exception e, String msg) {
        super(msg);
        this.nested = e;
    }

    public FactoryConfigurationError(String msg, Exception e) {
        super(msg);
        this.nested = e;
    }

    public FactoryConfigurationError(String msg) {
        super(msg);
    }

    public Exception getException() {
        return this.nested;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String msg = super.getMessage();
        if (msg != null) {
            return msg;
        }
        if (this.nested != null) {
            msg = this.nested.getMessage();
            if (msg == null) {
                msg = this.nested.getClass().toString();
            }
        }
        return msg;
    }
}
