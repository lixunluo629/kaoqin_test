package lombok.installer;

/* loaded from: lombok-1.16.22.jar:lombok/installer/InstallException.SCL.lombok */
public class InstallException extends Exception {
    private boolean warning;

    public InstallException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstallException(boolean warning, String message, Throwable cause) {
        super(message, cause);
        this.warning = warning;
    }

    public boolean isWarning() {
        return this.warning;
    }
}
