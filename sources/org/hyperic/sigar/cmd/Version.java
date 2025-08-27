package org.hyperic.sigar.cmd;

import java.io.File;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.win32.LocaleInfo;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Version.class */
public class Version extends SigarCommandBase {
    public Version(Shell shell) {
        super(shell);
    }

    public Version() {
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display sigar and system version info";
    }

    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    private static void printNativeInfo(PrintStream os) throws Throwable {
        String fqdn;
        String version = new StringBuffer().append("java=1.6.4.129, native=").append(Sigar.NATIVE_VERSION_STRING).toString();
        String build = new StringBuffer().append("java=04/28/2010 04:26 PM, native=").append(Sigar.NATIVE_BUILD_DATE).toString();
        String scm = new StringBuffer().append("java=4b67f57, native=").append(Sigar.NATIVE_SCM_REVISION).toString();
        String archlib = SigarLoader.getNativeLibraryName();
        os.println(new StringBuffer().append("Sigar version.......").append(version).toString());
        os.println(new StringBuffer().append("Build date..........").append(build).toString());
        os.println(new StringBuffer().append("SCM rev.............").append(scm).toString());
        String host = getHostName();
        Sigar sigar = new Sigar();
        try {
            File lib = sigar.getNativeLibrary();
            if (lib != null) {
                archlib = lib.getName();
            }
            fqdn = sigar.getFQDN();
            sigar.close();
        } catch (SigarException e) {
            fqdn = "unknown";
            sigar.close();
        } catch (Throwable th) {
            sigar.close();
            throw th;
        }
        os.println(new StringBuffer().append("Archlib.............").append(archlib).toString());
        os.println(new StringBuffer().append("Current fqdn........").append(fqdn).toString());
        if (!fqdn.equals(host)) {
            os.println(new StringBuffer().append("Hostname............").append(host).toString());
        }
        if (SigarLoader.IS_WIN32) {
            LocaleInfo info = new LocaleInfo();
            os.println(new StringBuffer().append("Language............").append(info).toString());
            os.println(new StringBuffer().append("Perflib lang id.....").append(info.getPerflibLangId()).toString());
        }
    }

    public static void printInfo(PrintStream os) throws Throwable {
        try {
            printNativeInfo(os);
        } catch (UnsatisfiedLinkError e) {
            os.println(new StringBuffer().append("*******ERROR******* ").append(e).toString());
        }
        os.println(new StringBuffer().append("Current user........").append(System.getProperty("user.name")).toString());
        os.println("");
        OperatingSystem sys = OperatingSystem.getInstance();
        os.println(new StringBuffer().append("OS description......").append(sys.getDescription()).toString());
        os.println(new StringBuffer().append("OS name.............").append(sys.getName()).toString());
        os.println(new StringBuffer().append("OS arch.............").append(sys.getArch()).toString());
        os.println(new StringBuffer().append("OS machine..........").append(sys.getMachine()).toString());
        os.println(new StringBuffer().append("OS version..........").append(sys.getVersion()).toString());
        os.println(new StringBuffer().append("OS patch level......").append(sys.getPatchLevel()).toString());
        os.println(new StringBuffer().append("OS vendor...........").append(sys.getVendor()).toString());
        os.println(new StringBuffer().append("OS vendor version...").append(sys.getVendorVersion()).toString());
        if (sys.getVendorCodeName() != null) {
            os.println(new StringBuffer().append("OS code name........").append(sys.getVendorCodeName()).toString());
        }
        os.println(new StringBuffer().append("OS data model.......").append(sys.getDataModel()).toString());
        os.println(new StringBuffer().append("OS cpu endian.......").append(sys.getCpuEndian()).toString());
        os.println(new StringBuffer().append("Java vm version.....").append(System.getProperty("java.vm.version")).toString());
        os.println(new StringBuffer().append("Java vm vendor......").append(System.getProperty("java.vm.vendor")).toString());
        os.println(new StringBuffer().append("Java home...........").append(System.getProperty("java.home")).toString());
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws Throwable {
        printInfo(this.out);
    }

    public static void main(String[] args) throws Exception {
        new Version().processCommand(args);
    }
}
