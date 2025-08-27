package lombok.patcher;

/* loaded from: lombok-1.16.22.jar:lombok/patcher/Version.SCL.lombok */
public class Version {
    private static final String VERSION = "0.26";

    private Version() {
    }

    public static void main(String[] args) {
        System.out.println(VERSION);
    }

    public static String getVersion() {
        return VERSION;
    }
}
