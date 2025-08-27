package lombok.installer.eclipse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import lombok.installer.CorruptedIdeLocationException;
import lombok.installer.IdeLocation;
import lombok.installer.IdeLocationProvider;
import lombok.installer.OsUtils;
import org.apache.commons.httpclient.cookie.Cookie2;

/* loaded from: lombok-1.16.22.jar:lombok/installer/eclipse/EclipseProductLocationProvider.SCL.lombok */
public class EclipseProductLocationProvider implements IdeLocationProvider {
    private final EclipseProductDescriptor descriptor;
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

    EclipseProductLocationProvider(EclipseProductDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override // lombok.installer.IdeLocationProvider
    public final IdeLocation create(String path) throws CorruptedIdeLocationException {
        return create0(path);
    }

    private IdeLocation create0(String path) throws CorruptedIdeLocationException {
        if (path == null) {
            throw new NullPointerException(Cookie2.PATH);
        }
        String iniName = this.descriptor.getIniFileName();
        File p = new File(path);
        if (!p.exists()) {
            return null;
        }
        if (p.isDirectory()) {
            for (String possibleExeName : this.descriptor.getExecutableNames()) {
                File f = new File(p, possibleExeName);
                if (f.exists()) {
                    return findEclipseIniFromExe(f, 0);
                }
            }
            File f2 = new File(p, iniName);
            if (f2.exists()) {
                return makeLocation(IdeLocation.canonical(p), f2);
            }
        }
        if (p.isFile() && p.getName().equalsIgnoreCase(iniName)) {
            return makeLocation(IdeLocation.canonical(p.getParentFile()), p);
        }
        if (this.descriptor.getExecutableNames().contains(p.getName().toLowerCase())) {
            return findEclipseIniFromExe(p, 0);
        }
        return null;
    }

    private IdeLocation findEclipseIniFromExe(File exePath, int loopCounter) throws IOException, CorruptedIdeLocationException {
        String iniName = this.descriptor.getIniFileName();
        File ini = new File(exePath.getParentFile(), iniName);
        if (ini.isFile()) {
            return makeLocation(IdeLocation.canonical(exePath), ini);
        }
        String macAppName = this.descriptor.getMacAppName();
        File ini2 = new File(exePath.getParentFile(), String.valueOf(macAppName) + "/Contents/MacOS/" + iniName);
        if (ini2.isFile()) {
            return makeLocation(IdeLocation.canonical(exePath), ini2);
        }
        File ini3 = new File(exePath.getParentFile(), String.valueOf(macAppName) + "/Contents/Eclipse/" + iniName);
        if (ini3.isFile()) {
            return makeLocation(IdeLocation.canonical(exePath), ini3);
        }
        if (loopCounter < 50) {
            try {
                String oPath = exePath.getAbsolutePath();
                String nPath = exePath.getCanonicalPath();
                if (!oPath.equals(nPath)) {
                    try {
                        IdeLocation loc = findEclipseIniFromExe(new File(nPath), loopCounter + 1);
                        if (loc != null) {
                            return loc;
                        }
                    } catch (CorruptedIdeLocationException unused) {
                    }
                }
            } catch (IOException unused2) {
            }
        }
        String path = exePath.getAbsolutePath();
        try {
            path = exePath.getCanonicalPath();
        } catch (IOException unused3) {
        }
        String unixAppName = this.descriptor.getUnixAppName();
        if (path.equals("/usr/bin/" + unixAppName) || path.equals("/bin/" + unixAppName) || path.equals("/usr/local/bin/" + unixAppName)) {
            File ini4 = new File("/usr/lib/" + unixAppName + "/" + iniName);
            if (ini4.isFile()) {
                return makeLocation(path, ini4);
            }
            File ini5 = new File("/usr/local/lib/" + unixAppName + "/" + iniName);
            if (ini5.isFile()) {
                return makeLocation(path, ini5);
            }
            File ini6 = new File("/usr/local/etc/" + unixAppName + "/" + iniName);
            if (ini6.isFile()) {
                return makeLocation(path, ini6);
            }
            File ini7 = new File("/etc/" + iniName);
            if (ini7.isFile()) {
                return makeLocation(path, ini7);
            }
            return null;
        }
        return null;
    }

    private IdeLocation makeLocation(String name, File ini) throws CorruptedIdeLocationException {
        return new EclipseProductLocation(this.descriptor, name, ini);
    }

    @Override // lombok.installer.IdeLocationProvider
    public Pattern getLocationSelectors() {
        return this.descriptor.getLocationSelectors();
    }

    @Override // lombok.installer.IdeLocationProvider
    public void findIdes(List<IdeLocation> locations, List<CorruptedIdeLocationException> problems) {
        switch ($SWITCH_TABLE$lombok$installer$OsUtils$OS()[OsUtils.getOS().ordinal()]) {
            case 1:
                new MacFinder().findEclipse(locations, problems);
                break;
            case 2:
                new WindowsFinder().findEclipse(locations, problems);
                break;
            case 3:
            default:
                new UnixFinder().findEclipse(locations, problems);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<File> transformToFiles(List<String> fileNames) {
        List<File> files = new ArrayList<>();
        for (String fileName : fileNames) {
            files.add(new File(fileName));
        }
        return files;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<File> getFlatSourceLocationsOnUnix() {
        List<File> dirs = new ArrayList<>();
        dirs.add(new File("/usr/bin/"));
        dirs.add(new File("/usr/local/bin/"));
        dirs.add(new File(System.getProperty("user.home", "."), "bin/"));
        return dirs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<File> getNestedSourceLocationOnUnix() {
        List<File> dirs = new ArrayList<>();
        dirs.add(new File("/usr/local/share"));
        dirs.add(new File("/usr/local"));
        dirs.add(new File("/usr/share"));
        return dirs;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/installer/eclipse/EclipseProductLocationProvider$UnixFinder.SCL.lombok */
    private class UnixFinder extends DirectoryFinder {
        UnixFinder() {
            super(EclipseProductLocationProvider.this.getNestedSourceLocationOnUnix(), EclipseProductLocationProvider.this.getFlatSourceLocationsOnUnix());
        }

        @Override // lombok.installer.eclipse.EclipseProductLocationProvider.DirectoryFinder
        protected String findEclipseOnPlatform(File dir) {
            File possible = new File(dir, EclipseProductLocationProvider.this.descriptor.getUnixAppName());
            if (possible.exists()) {
                return possible.getAbsolutePath();
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<String> getSourceDirsOnWindowsWithDriveLetters() {
        List<String> driveLetters = Arrays.asList("C");
        try {
            driveLetters = OsUtils.getDrivesOnWindows();
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }
        List<String> sourceDirs = new ArrayList<>();
        for (String letter : driveLetters) {
            for (String possibleSource : this.descriptor.getSourceDirsOnWindows()) {
                if (!isDriveSpecificOnWindows(possibleSource)) {
                    sourceDirs.add(String.valueOf(letter) + ":" + possibleSource);
                }
            }
        }
        for (String possibleSource2 : this.descriptor.getSourceDirsOnWindows()) {
            if (isDriveSpecificOnWindows(possibleSource2)) {
                sourceDirs.add(possibleSource2);
            }
        }
        return sourceDirs;
    }

    private boolean isDriveSpecificOnWindows(String path) {
        return path.length() > 1 && path.charAt(1) == ':';
    }

    /* loaded from: lombok-1.16.22.jar:lombok/installer/eclipse/EclipseProductLocationProvider$WindowsFinder.SCL.lombok */
    private class WindowsFinder extends DirectoryFinder {
        WindowsFinder() {
            super(EclipseProductLocationProvider.this.transformToFiles(EclipseProductLocationProvider.this.getSourceDirsOnWindowsWithDriveLetters()), Collections.emptyList());
        }

        @Override // lombok.installer.eclipse.EclipseProductLocationProvider.DirectoryFinder
        protected String findEclipseOnPlatform(File dir) {
            File possible = new File(dir, EclipseProductLocationProvider.this.descriptor.getWindowsExecutableName());
            if (possible.isFile()) {
                return dir.getAbsolutePath();
            }
            return null;
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/installer/eclipse/EclipseProductLocationProvider$MacFinder.SCL.lombok */
    private class MacFinder extends DirectoryFinder {
        MacFinder() {
            super(EclipseProductLocationProvider.this.transformToFiles(EclipseProductLocationProvider.this.descriptor.getSourceDirsOnMac()), Collections.emptyList());
        }

        @Override // lombok.installer.eclipse.EclipseProductLocationProvider.DirectoryFinder
        protected String findEclipseOnPlatform(File dir) {
            if (dir.getName().toLowerCase().equals(EclipseProductLocationProvider.this.descriptor.getMacAppName().toLowerCase())) {
                return dir.getParent();
            }
            if (dir.getName().toLowerCase().contains(EclipseProductLocationProvider.this.descriptor.getDirectoryName()) && new File(dir, EclipseProductLocationProvider.this.descriptor.getMacAppName()).exists()) {
                return dir.toString();
            }
            return null;
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/installer/eclipse/EclipseProductLocationProvider$DirectoryFinder.SCL.lombok */
    private abstract class DirectoryFinder {
        private final List<File> flatSourceDirs;
        private final List<File> nestedSourceDirs;

        abstract String findEclipseOnPlatform(File file);

        DirectoryFinder(List<File> nestedSourceDirs, List<File> flatSourceDirs) {
            this.nestedSourceDirs = nestedSourceDirs;
            this.flatSourceDirs = flatSourceDirs;
        }

        void findEclipse(List<IdeLocation> locations, List<CorruptedIdeLocationException> problems) {
            for (File dir : this.nestedSourceDirs) {
                recurseDirectory(locations, problems, dir);
            }
            for (File dir2 : this.flatSourceDirs) {
                findEclipse(locations, problems, dir2);
            }
        }

        void recurseDirectory(List<IdeLocation> locations, List<CorruptedIdeLocationException> problems, File dir) {
            recurseDirectory0(locations, problems, dir, 0, false);
        }

        /* JADX WARN: Removed duplicated region for block: B:13:0x004b A[Catch: Exception -> 0x006a, TryCatch #0 {Exception -> 0x006a, blocks: (B:11:0x0031, B:13:0x004b, B:15:0x005a), top: B:21:0x0031 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void recurseDirectory0(java.util.List<lombok.installer.IdeLocation> r8, java.util.List<lombok.installer.CorruptedIdeLocationException> r9, java.io.File r10, int r11, boolean r12) {
            /*
                r7 = this;
                r0 = r10
                java.io.File[] r0 = r0.listFiles()
                r13 = r0
                r0 = r13
                if (r0 != 0) goto Lc
                return
            Lc:
                r0 = r13
                r1 = r0
                r17 = r1
                int r0 = r0.length
                r16 = r0
                r0 = 0
                r15 = r0
                goto L6e
            L1a:
                r0 = r17
                r1 = r15
                r0 = r0[r1]
                r14 = r0
                r0 = r14
                boolean r0 = r0.isDirectory()
                if (r0 != 0) goto L2c
                goto L6b
            L2c:
                r0 = r12
                if (r0 != 0) goto L4b
                r0 = r14
                java.lang.String r0 = r0.getName()     // Catch: java.lang.Exception -> L6a
                java.lang.String r0 = r0.toLowerCase()     // Catch: java.lang.Exception -> L6a
                r1 = r7
                lombok.installer.eclipse.EclipseProductLocationProvider r1 = lombok.installer.eclipse.EclipseProductLocationProvider.this     // Catch: java.lang.Exception -> L6a
                lombok.installer.eclipse.EclipseProductDescriptor r1 = lombok.installer.eclipse.EclipseProductLocationProvider.access$2(r1)     // Catch: java.lang.Exception -> L6a
                java.lang.String r1 = r1.getDirectoryName()     // Catch: java.lang.Exception -> L6a
                boolean r0 = r0.contains(r1)     // Catch: java.lang.Exception -> L6a
                if (r0 == 0) goto L6b
            L4b:
                r0 = r7
                r1 = r8
                r2 = r9
                r3 = r14
                r0.findEclipse(r1, r2, r3)     // Catch: java.lang.Exception -> L6a
                r0 = r11
                r1 = 50
                if (r0 >= r1) goto L6b
                r0 = r7
                r1 = r8
                r2 = r9
                r3 = r14
                r4 = r11
                r5 = 1
                int r4 = r4 + r5
                r5 = 1
                r0.recurseDirectory0(r1, r2, r3, r4, r5)     // Catch: java.lang.Exception -> L6a
                goto L6b
            L6a:
            L6b:
                int r15 = r15 + 1
            L6e:
                r0 = r15
                r1 = r16
                if (r0 < r1) goto L1a
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: lombok.installer.eclipse.EclipseProductLocationProvider.DirectoryFinder.recurseDirectory0(java.util.List, java.util.List, java.io.File, int, boolean):void");
        }

        private void findEclipse(List<IdeLocation> locations, List<CorruptedIdeLocationException> problems, File dir) {
            String eclipseLocation = findEclipseOnPlatform(dir);
            if (eclipseLocation != null) {
                try {
                    IdeLocation newLocation = EclipseProductLocationProvider.this.create(eclipseLocation);
                    if (newLocation != null) {
                        locations.add(newLocation);
                    }
                } catch (CorruptedIdeLocationException e) {
                    problems.add(e);
                }
            }
        }
    }
}
