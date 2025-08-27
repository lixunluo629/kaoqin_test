package org.hyperic.sigar.cmd;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.jmx.SigarInvokerJMX;
import org.hyperic.sigar.ptql.ProcessFinder;
import org.hyperic.sigar.shell.ShellBase;
import org.hyperic.sigar.shell.ShellCommandExecException;
import org.hyperic.sigar.shell.ShellCommandHandler;
import org.hyperic.sigar.shell.ShellCommandInitException;
import org.hyperic.sigar.shell.ShellCommandUsageException;
import org.hyperic.sigar.util.Getline;
import org.springframework.util.ClassUtils;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Shell.class */
public class Shell extends ShellBase {
    public static final String RCFILE_NAME = ".sigar_shellrc";
    private static final String CLEAR_SCREEN = "\u001b[2J";
    private Sigar sigar = new Sigar();
    private SigarProxy proxy = SigarProxyCache.newInstance(this.sigar);
    private long[] foundPids = new long[0];
    private boolean isInteractive = false;

    public static void clearScreen() {
        System.out.print(CLEAR_SCREEN);
    }

    public SigarProxy getSigarProxy() {
        return this.proxy;
    }

    public Sigar getSigar() {
        return this.sigar;
    }

    public boolean isInteractive() {
        return this.isInteractive;
    }

    public void setInteractive(boolean value) {
        this.isInteractive = value;
    }

    public void registerCommands() throws ShellCommandInitException {
        registerCommandHandler("df", new Df(this));
        registerCommandHandler("du", new Du(this));
        registerCommandHandler("ls", new Ls(this));
        registerCommandHandler("iostat", new Iostat(this));
        registerCommandHandler("free", new Free(this));
        registerCommandHandler("pargs", new ShowArgs(this));
        registerCommandHandler("penv", new ShowEnv(this));
        registerCommandHandler("pfile", new ProcFileInfo(this));
        registerCommandHandler("pmodules", new ProcModuleInfo(this));
        registerCommandHandler("pinfo", new ProcInfo(this));
        registerCommandHandler("cpuinfo", new CpuInfo(this));
        registerCommandHandler("ifconfig", new Ifconfig(this));
        registerCommandHandler("uptime", new Uptime(this));
        registerCommandHandler("ps", new Ps(this));
        registerCommandHandler("pidof", new Pidof(this));
        registerCommandHandler("kill", new Kill(this));
        registerCommandHandler("netstat", new Netstat(this));
        registerCommandHandler("netinfo", new NetInfo(this));
        registerCommandHandler("nfsstat", new Nfsstat(this));
        registerCommandHandler("route", new Route(this));
        registerCommandHandler("version", new Version(this));
        registerCommandHandler("mps", new MultiPs(this));
        registerCommandHandler("sysinfo", new SysInfo(this));
        registerCommandHandler(RtspHeaders.Values.TIME, new Time(this));
        registerCommandHandler("ulimit", new Ulimit(this));
        registerCommandHandler("who", new Who(this));
        if (SigarLoader.IS_WIN32) {
            registerCommandHandler("service", new Win32Service(this));
            registerCommandHandler("fversion", new FileVersionInfo(this));
        }
        try {
            registerCommandHandler("test", "org.hyperic.sigar.test.SigarTestRunner");
        } catch (Exception e) {
        } catch (NoClassDefFoundError e2) {
        }
    }

    private void registerCommandHandler(String name, String className) throws Exception {
        Class cls = Class.forName(className);
        Constructor con = cls.getConstructor(getClass());
        registerCommandHandler(name, (ShellCommandHandler) con.newInstance(this));
    }

    @Override // org.hyperic.sigar.shell.ShellBase
    public void processCommand(ShellCommandHandler handler, String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        try {
            super.processCommand(handler, args);
            if (handler instanceof SigarCommandBase) {
                ((SigarCommandBase) handler).flush();
            }
        } finally {
            SigarProxyCache.clear(this.proxy);
        }
    }

    public static long[] getPids(SigarProxy sigar, String[] args) throws SigarException {
        long[] pids;
        switch (args.length) {
            case 0:
                pids = new long[]{sigar.getPid()};
                break;
            case 1:
                if (args[0].indexOf(SymbolConstants.EQUAL_SYMBOL) > 0) {
                    pids = ProcessFinder.find(sigar, args[0]);
                    break;
                } else if (args[0].equals(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
                    pids = new long[]{sigar.getPid()};
                    break;
                } else {
                    pids = new long[]{Long.parseLong(args[0])};
                    break;
                }
            default:
                pids = new long[args.length];
                for (int i = 0; i < args.length; i++) {
                    pids[i] = Long.parseLong(args[i]);
                }
                break;
        }
        return pids;
    }

    public long[] findPids(String[] args) throws SigarException {
        if (args.length == 1 && args[0].equals("-")) {
            return this.foundPids;
        }
        this.foundPids = getPids(this.proxy, args);
        return this.foundPids;
    }

    public long[] findPids(String query) throws SigarException {
        return findPids(new String[]{query});
    }

    public void readCommandFile(String dir) {
        try {
            File rc = new File(dir, RCFILE_NAME);
            readRCFile(rc, false);
            if (this.isInteractive && Getline.isTTY()) {
                this.out.println(new StringBuffer().append("Loaded rc file: ").append(rc).toString());
            }
        } catch (IOException e) {
        }
    }

    public String getUserDeniedMessage(long pid) {
        return SigarPermissionDeniedException.getUserDeniedMessage(this.proxy, pid);
    }

    @Override // org.hyperic.sigar.shell.ShellBase
    public void shutdown() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.sigar.close();
        try {
            Class.forName("org.hyperic.sigar.test.SigarTestCase").getMethod("closeSigar", new Class[0]).invoke(null, new Object[0]);
        } catch (ClassNotFoundException e) {
        } catch (Exception e2) {
            e2.printStackTrace();
        } catch (NoClassDefFoundError e3) {
        }
        super.shutdown();
    }

    public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Shell shell = new Shell();
        try {
            try {
                if (args.length == 0) {
                    shell.isInteractive = true;
                }
                shell.init(SigarInvokerJMX.DOMAIN_NAME, System.out, System.err);
                shell.registerCommands();
                shell.readCommandFile(System.getProperty("user.home"));
                shell.readCommandFile(".");
                shell.readCommandFile(SigarLoader.getLocation());
                if (shell.isInteractive) {
                    shell.initHistory();
                    Getline.setCompleter(shell);
                    shell.run();
                } else {
                    shell.handleCommand(null, args);
                }
                shell.shutdown();
            } catch (Exception e) {
                System.err.println(new StringBuffer().append("Unexpected exception: ").append(e).toString());
                shell.shutdown();
            }
        } catch (Throwable th) {
            shell.shutdown();
            throw th;
        }
    }
}
