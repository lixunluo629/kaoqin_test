package lombok.installer;

/* loaded from: lombok-1.16.22.jar:lombok/installer/UninstallException.SCL.lombok */
public class UninstallException extends Exception {
    private boolean warning;

    public UninstallException(String message, Throwable cause) {
        super(message, cause);
    }

    public UninstallException(boolean warning, String message, Throwable cause) {
        super(message, cause);
        this.warning = warning;
    }

    public boolean isWarning() {
        return this.warning;
    }
}
