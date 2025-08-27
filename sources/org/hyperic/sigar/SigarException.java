package org.hyperic.sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/SigarException.class */
public class SigarException extends Exception {
    private String message;

    public SigarException() {
    }

    public SigarException(String s) {
        super(s);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (this.message != null) {
            return this.message;
        }
        return super.getMessage();
    }

    void setMessage(String message) {
        this.message = message;
    }
}
