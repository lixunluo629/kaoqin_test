package lombok.eclipse.agent;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/agent/PatchDiagnostics.SCL.lombok */
public class PatchDiagnostics {
    public static boolean setSourceRangeCheck(Object astNode, int startPosition, int length) {
        if (startPosition >= 0 && length < 0) {
            throw new IllegalArgumentException("startPos = " + startPosition + " and length is " + length + ".\nThis breaks the rule that lengths are not allowed to be negative. Affected Node:\n" + astNode);
        }
        if (startPosition < 0 && length != 0) {
            throw new IllegalArgumentException("startPos = " + startPosition + " and length is " + length + ".\nThis breaks the rule that length must be 0 if startPosition is negative. Affected Node:\n" + astNode);
        }
        return false;
    }
}
