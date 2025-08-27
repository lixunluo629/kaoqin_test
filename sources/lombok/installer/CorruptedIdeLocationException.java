package lombok.installer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/* loaded from: lombok-1.16.22.jar:lombok/installer/CorruptedIdeLocationException.SCL.lombok */
public class CorruptedIdeLocationException extends Exception {
    private final String ideType;

    public CorruptedIdeLocationException(String message, String ideType, Throwable cause) {
        super(message, cause);
        this.ideType = ideType;
    }

    public String getIdeType() {
        return this.ideType;
    }

    void showDialog(JFrame appWindow) {
        JOptionPane.showMessageDialog(appWindow, getMessage(), "Cannot configure " + this.ideType + " installation", 2);
    }
}
