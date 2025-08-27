package lombok.installer.eclipse;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import lombok.installer.OsUtils;
import org.hyperic.sigar.win32.Win32;

/* loaded from: lombok-1.16.22.jar:lombok/installer/eclipse/StandardProductDescriptor.SCL.lombok */
public class StandardProductDescriptor implements EclipseProductDescriptor {
    private static final String USER_HOME = System.getProperty("user.home", ".");
    private static final String[] WINDOWS_ROOTS = {"\\", "\\Program Files", "\\Program Files (x86)", USER_HOME};
    private static final String[] MAC_ROOTS = {"/Applications", USER_HOME};
    private static final String[] UNIX_ROOTS = {USER_HOME};
    private final String productName;
    private final String windowsName;
    private final String unixName;
    private final String macAppName;
    private final List<String> executableNames;
    private final List<String> sourceDirsOnWindows;
    private final List<String> sourceDirsOnMac;
    private final List<String> sourceDirsOnUnix;
    private final String iniFileName;
    private final Pattern locationSelectors;
    private final String directoryName;
    private final URL ideIcon;
    private static /* synthetic */ int[] $SWITCH_TABLE$lombok$installer$OsUtils$OS;

    static /* synthetic */ int[] $SWITCH_TABLE$lombok$installer$OsUtils$OS() {
        int[] iArr = $SWITCH_TABLE$lombok$installer$OsUtils$OS;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[OsUtils.OS.valuesCustom().length];
        try {
            iArr2[OsUtils.OS.MAC_OS_X.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[OsUtils.OS.UNIX.ordinal()] = 3;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[OsUtils.OS.WINDOWS.ordinal()] = 2;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$lombok$installer$OsUtils$OS = iArr2;
        return iArr2;
    }

    public StandardProductDescriptor(String productName, String baseName, String directoryName, URL ideIcon, Collection<String> alternativeDirectoryNames) {
        this.productName = productName;
        this.windowsName = String.valueOf(baseName) + Win32.EXE_EXT;
        this.unixName = baseName;
        this.macAppName = String.valueOf(baseName) + ".app";
        this.executableNames = executableNames(baseName);
        this.sourceDirsOnWindows = generateAlternatives(WINDOWS_ROOTS, "\\", alternativeDirectoryNames);
        this.sourceDirsOnMac = generateAlternatives(MAC_ROOTS, "/", alternativeDirectoryNames);
        this.sourceDirsOnUnix = generateAlternatives(UNIX_ROOTS, "/", alternativeDirectoryNames);
        this.iniFileName = String.valueOf(baseName) + ".ini";
        this.locationSelectors = getLocationSelectors(baseName);
        this.directoryName = directoryName.toLowerCase();
        this.ideIcon = ideIcon;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public String getProductName() {
        return this.productName;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public String getWindowsExecutableName() {
        return this.windowsName;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public String getUnixAppName() {
        return this.unixName;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public String getMacAppName() {
        return this.macAppName;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public String getDirectoryName() {
        return this.directoryName;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public List<String> getExecutableNames() {
        return this.executableNames;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public List<String> getSourceDirsOnWindows() {
        return this.sourceDirsOnWindows;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public List<String> getSourceDirsOnMac() {
        return this.sourceDirsOnMac;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public List<String> getSourceDirsOnUnix() {
        return this.sourceDirsOnUnix;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public String getIniFileName() {
        return this.iniFileName;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public Pattern getLocationSelectors() {
        return this.locationSelectors;
    }

    @Override // lombok.installer.eclipse.EclipseProductDescriptor
    public URL getIdeIcon() {
        return this.ideIcon;
    }

    private static Pattern getLocationSelectors(String baseName) {
        return Pattern.compile(String.format(platformPattern(), baseName.toLowerCase()), 2);
    }

    private static String platformPattern() {
        switch ($SWITCH_TABLE$lombok$installer$OsUtils$OS()[OsUtils.getOS().ordinal()]) {
            case 1:
                return "^(%s|%<s\\.ini|%<s\\.app)$";
            case 2:
                return "^(%sc?\\.exe|%<s\\.ini)$";
            case 3:
            default:
                return "^(%s|%<s\\.ini)$";
        }
    }

    private static List<String> executableNames(String baseName) {
        String base = baseName.toLowerCase();
        return Collections.unmodifiableList(Arrays.asList(base, String.valueOf(base) + ".app", String.valueOf(base) + Win32.EXE_EXT, String.valueOf(base) + "c.exe"));
    }

    private static List<String> generateAlternatives(String[] roots, String pathSeparator, Collection<String> alternatives) {
        List<String> result = new ArrayList<>();
        for (String root : roots) {
            result.add(concat(root, pathSeparator, ""));
            for (String alternative : alternatives) {
                result.add(concat(root, pathSeparator, alternative));
            }
        }
        return Collections.unmodifiableList(result);
    }

    private static String concat(String base, String pathSeparator, String alternative) {
        if (alternative.isEmpty()) {
            return base;
        }
        if (base.endsWith(pathSeparator)) {
            return String.valueOf(base) + alternative.replaceAll("[\\/]", "\\" + pathSeparator);
        }
        return String.valueOf(base) + pathSeparator + alternative.replaceAll("[\\/]", "\\" + pathSeparator);
    }
}
