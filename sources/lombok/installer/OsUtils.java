package lombok.installer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.core.Version;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: lombok-1.16.22.jar:lombok/installer/OsUtils.SCL.lombok */
public final class OsUtils {
    private static final AtomicBoolean windowsDriveInfoLibLoaded = new AtomicBoolean(false);

    private OsUtils() {
    }

    private static void loadWindowsDriveInfoLib() throws IOException {
        if (windowsDriveInfoLibLoaded.compareAndSet(false, true)) {
            String prefix = "lombok-" + Version.getVersion() + "-";
            File temp = File.createTempFile("lombok", ".mark");
            File dll1 = new File(temp.getParentFile(), String.valueOf(prefix) + "WindowsDriveInfo-i386.dll");
            File dll2 = new File(temp.getParentFile(), String.valueOf(prefix) + "WindowsDriveInfo-x86_64.dll");
            temp.delete();
            dll1.deleteOnExit();
            dll2.deleteOnExit();
            try {
                if (unpackDLL("WindowsDriveInfo-i386.dll", dll1)) {
                    System.load(dll1.getAbsolutePath());
                    return;
                }
            } catch (Throwable unused) {
            }
            try {
                if (unpackDLL("WindowsDriveInfo-x86_64.dll", dll2)) {
                    System.load(dll2.getAbsolutePath());
                }
            } catch (Throwable unused2) {
            }
        }
    }

    private static boolean unpackDLL(String dllName, File target) throws IOException {
        InputStream in = OsUtils.class.getResourceAsStream(dllName);
        try {
            try {
                FileOutputStream out = new FileOutputStream(target);
                try {
                    byte[] b = new byte[32000];
                    while (true) {
                        int r = in.read(b);
                        if (r != -1) {
                            out.write(b, 0, r);
                        } else {
                            out.close();
                            in.close();
                            return true;
                        }
                    }
                } catch (Throwable th) {
                    out.close();
                    throw th;
                }
            } catch (IOException unused) {
                boolean z = target.exists() && target.canRead();
                in.close();
                return z;
            }
        } catch (Throwable th2) {
            in.close();
            throw th2;
        }
    }

    public static List<String> getDrivesOnWindows() throws Throwable {
        loadWindowsDriveInfoLib();
        List<String> drives = new ArrayList<>();
        WindowsDriveInfo info = new WindowsDriveInfo();
        for (String drive : info.getLogicalDrives()) {
            if (info.isFixedDisk(drive)) {
                drives.add(drive);
            }
        }
        return drives;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/installer/OsUtils$OS.SCL.lombok */
    public enum OS {
        MAC_OS_X(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR),
        WINDOWS("\r\n"),
        UNIX(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);

        private final String lineEnding;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static OS[] valuesCustom() {
            OS[] osArrValuesCustom = values();
            int length = osArrValuesCustom.length;
            OS[] osArr = new OS[length];
            System.arraycopy(osArrValuesCustom, 0, osArr, 0, length);
            return osArr;
        }

        OS(String lineEnding) {
            this.lineEnding = lineEnding;
        }

        public String getLineEnding() {
            return this.lineEnding;
        }
    }

    public static OS getOS() {
        String prop = System.getProperty("os.name", "").toLowerCase();
        if (!prop.matches("^.*\\bmac\\b.*$") && !prop.matches("^.*\\bdarwin\\b.*$")) {
            return prop.matches("^.*\\bwin(dows|32|64)?\\b.*$") ? OS.WINDOWS : OS.UNIX;
        }
        return OS.MAC_OS_X;
    }
}
