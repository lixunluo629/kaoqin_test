package org.hyperic.sigar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.hyperic.jni.ArchLoaderException;
import org.hyperic.jni.ArchNotSupportedException;
import org.hyperic.sigar.ptql.ProcessFinder;
import org.springframework.util.ClassUtils;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/Sigar.class */
public class Sigar implements SigarProxy {
    private static String loadError;
    public static final long FIELD_NOTIMPL = -1;
    public static final String VERSION_STRING = "1.6.4.129";
    public static final String NATIVE_VERSION_STRING;
    public static final String SCM_REVISION = "4b67f57";
    public static final String NATIVE_SCM_REVISION;
    public static final String BUILD_DATE = "04/28/2010 04:26 PM";
    public static final String NATIVE_BUILD_DATE;
    private static boolean enableLogging = "true".equals(System.getProperty("sigar.nativeLogging"));
    private static SigarLoader loader;
    private boolean open;
    private Cpu lastCpu;
    private Cpu[] lastCpuList;
    static Class class$org$hyperic$sigar$Sigar;
    private FileSystemMap mounts = null;
    int sigarWrapper = 0;
    long longSigarWrapper = 0;
    private ProcessFinder processFinder = null;

    public static native String formatSize(long j);

    private static native String getNativeVersion();

    private static native String getNativeBuildDate();

    private static native String getNativeScmRevision();

    private native void open() throws SigarException;

    private native int nativeClose();

    @Override // org.hyperic.sigar.SigarProxy
    public native long getPid();

    @Override // org.hyperic.sigar.SigarProxy
    public native long getServicePid(String str) throws SigarException;

    public native void kill(long j, int i) throws SigarException;

    public static native int getSigNum(String str);

    @Override // org.hyperic.sigar.SigarProxy
    public native double[] getLoadAverage() throws SigarException;

    @Override // org.hyperic.sigar.SigarProxy
    public native long[] getProcList() throws SigarException;

    @Override // org.hyperic.sigar.SigarProxy
    public native String[] getProcArgs(long j) throws SigarException;

    private native List getProcModulesNative(long j) throws SigarException;

    @Override // org.hyperic.sigar.SigarProxy
    public native long getProcPort(int i, long j) throws SigarException;

    private native FileSystem[] getFileSystemListNative() throws SigarException;

    @Override // org.hyperic.sigar.SigarProxy
    public native CpuInfo[] getCpuInfoList() throws SigarException;

    private native Cpu[] getCpuListNative() throws SigarException;

    @Override // org.hyperic.sigar.SigarProxy
    public native NetRoute[] getNetRouteList() throws SigarException;

    @Override // org.hyperic.sigar.SigarProxy
    public native NetConnection[] getNetConnectionList(int i) throws SigarException;

    @Override // org.hyperic.sigar.SigarProxy
    public native String getNetListenAddress(long j) throws SigarException;

    @Override // org.hyperic.sigar.SigarProxy
    public native String getNetServicesName(int i, long j);

    @Override // org.hyperic.sigar.SigarProxy
    public native Who[] getWhoList() throws SigarException;

    @Override // org.hyperic.sigar.SigarProxy
    public native String[] getNetInterfaceList() throws SigarException;

    static native String getPasswordNative(String str) throws SigarNotImplementedException, IOException;

    @Override // org.hyperic.sigar.SigarProxy
    public native String getFQDN() throws SigarException;

    static {
        Class clsClass$;
        loadError = null;
        if (class$org$hyperic$sigar$Sigar == null) {
            clsClass$ = class$("org.hyperic.sigar.Sigar");
            class$org$hyperic$sigar$Sigar = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$Sigar;
        }
        loader = new SigarLoader(clsClass$);
        String nativeVersion = "unknown";
        String nativeBuildDate = "unknown";
        String nativeScmRevision = "unknown";
        try {
            loadLibrary();
            nativeVersion = getNativeVersion();
            nativeBuildDate = getNativeBuildDate();
            nativeScmRevision = getNativeScmRevision();
            checkVersion(nativeVersion);
        } catch (SigarException e) {
            loadError = e.getMessage();
            try {
                SigarLog.debug(loadError, e);
            } catch (NoClassDefFoundError e2) {
                System.err.println(loadError);
                e.printStackTrace();
            }
        }
        NATIVE_VERSION_STRING = nativeVersion;
        NATIVE_BUILD_DATE = nativeBuildDate;
        NATIVE_SCM_REVISION = nativeScmRevision;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private static void checkVersion(String nativeVersionString) throws SigarException {
        StringTokenizer javaVersion = new StringTokenizer(VERSION_STRING, ".");
        StringTokenizer nativeVersion = new StringTokenizer(nativeVersionString, ".");
        String[] desc = {"major", "minor"};
        for (String str : desc) {
            String jv = javaVersion.hasMoreTokens() ? javaVersion.nextToken() : "0";
            String nv = nativeVersion.hasMoreTokens() ? nativeVersion.nextToken() : "0";
            if (!jv.equals(nv)) {
                String msg = new StringBuffer().append(str).append(" version mismatch: (").append(jv).append("!=").append(nv).append(") ").append("java=").append(VERSION_STRING).append(", native=").append(nativeVersionString).toString();
                throw new SigarException(msg);
            }
        }
    }

    public static void load() throws SigarException {
        if (loadError != null) {
            throw new SigarException(loadError);
        }
    }

    private static void loadLibrary() throws SigarException {
        try {
            if (SigarLoader.IS_WIN32 && System.getProperty("os.version").equals("4.0")) {
                String lib = new StringBuffer().append(loader.findJarPath("pdh.dll")).append(File.separator).append("pdh.dll").toString();
                loader.systemLoad(lib);
            }
            loader.load();
        } catch (UnsatisfiedLinkError e) {
            throw new SigarException(e.getMessage());
        } catch (ArchLoaderException e2) {
            throw new SigarException(e2.getMessage());
        } catch (ArchNotSupportedException e3) {
            throw new SigarException(e3.getMessage());
        }
    }

    public File getNativeLibrary() {
        return loader.getNativeLibrary();
    }

    public Sigar() {
        this.open = false;
        try {
            open();
            this.open = true;
        } catch (UnsatisfiedLinkError e) {
            if (enableLogging) {
                e.printStackTrace();
            }
        } catch (SigarException e2) {
            if (enableLogging) {
                e2.printStackTrace();
            }
        }
        if (enableLogging) {
            enableLogging(true);
        }
    }

    protected void finalize() {
        close();
    }

    public synchronized void close() {
        if (this.open) {
            nativeClose();
            this.open = false;
        }
    }

    public void kill(long pid, String signame) throws SigarException, NumberFormatException {
        int signum;
        if (Character.isDigit(signame.charAt(0))) {
            try {
                signum = Integer.parseInt(signame);
            } catch (NumberFormatException e) {
                signum = -1;
            }
        } else {
            signum = getSigNum(signame);
        }
        if (signum < 0) {
            throw new SigarException(new StringBuffer().append(signame).append(": invalid signal specification").toString());
        }
        kill(pid, signum);
    }

    public void kill(String pid, int signum) throws SigarException {
        kill(convertPid(pid), signum);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public Mem getMem() throws SigarException {
        return Mem.fetch(this);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public Swap getSwap() throws SigarException {
        return Swap.fetch(this);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public Cpu getCpu() throws SigarException {
        return Cpu.fetch(this);
    }

    static void pause(int millis) throws InterruptedException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    static void pause() throws InterruptedException {
        pause(500);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public CpuPerc getCpuPerc() throws SigarException, InterruptedException {
        Cpu oldCpu;
        if (this.lastCpu == null) {
            oldCpu = getCpu();
            pause();
        } else {
            oldCpu = this.lastCpu;
        }
        this.lastCpu = getCpu();
        return CpuPerc.fetch(this, oldCpu, this.lastCpu);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public CpuPerc[] getCpuPercList() throws SigarException, InterruptedException {
        Cpu[] oldCpuList;
        if (this.lastCpuList == null) {
            oldCpuList = getCpuList();
            pause();
        } else {
            oldCpuList = this.lastCpuList;
        }
        this.lastCpuList = getCpuList();
        int curLen = this.lastCpuList.length;
        int oldLen = oldCpuList.length;
        CpuPerc[] perc = new CpuPerc[curLen < oldLen ? curLen : oldLen];
        for (int i = 0; i < curLen; i++) {
            perc[i] = CpuPerc.fetch(this, oldCpuList[i], this.lastCpuList[i]);
        }
        return perc;
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ResourceLimit getResourceLimit() throws SigarException {
        return ResourceLimit.fetch(this);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public Uptime getUptime() throws SigarException {
        return Uptime.fetch(this);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcStat getProcStat() throws SigarException {
        return ProcStat.fetch(this);
    }

    private long convertPid(String pid) throws SigarException {
        if (pid.equals(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
            return getPid();
        }
        if (Character.isDigit(pid.charAt(0))) {
            return Long.parseLong(pid);
        }
        if (this.processFinder == null) {
            this.processFinder = new ProcessFinder(this);
        }
        return this.processFinder.findSingleProcess(pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcMem getProcMem(long pid) throws SigarException {
        return ProcMem.fetch(this, pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcMem getProcMem(String pid) throws SigarException {
        return getProcMem(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcMem getMultiProcMem(String query) throws SigarException {
        return MultiProcMem.get(this, query);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcState getProcState(long pid) throws SigarException {
        return ProcState.fetch(this, pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcState getProcState(String pid) throws SigarException {
        return getProcState(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcTime getProcTime(long pid) throws SigarException {
        return ProcTime.fetch(this, pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcTime getProcTime(String pid) throws SigarException {
        return getProcTime(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcCpu getProcCpu(long pid) throws SigarException {
        return ProcCpu.fetch(this, pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcCpu getProcCpu(String pid) throws SigarException {
        return getProcCpu(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public MultiProcCpu getMultiProcCpu(String query) throws SigarException {
        return MultiProcCpu.get(this, query);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcCred getProcCred(long pid) throws SigarException {
        return ProcCred.fetch(this, pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcCred getProcCred(String pid) throws SigarException {
        return getProcCred(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcCredName getProcCredName(long pid) throws SigarException {
        return ProcCredName.fetch(this, pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcCredName getProcCredName(String pid) throws SigarException {
        return getProcCredName(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcFd getProcFd(long pid) throws SigarException {
        return ProcFd.fetch(this, pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcFd getProcFd(String pid) throws SigarException {
        return getProcFd(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcExe getProcExe(long pid) throws SigarException {
        return ProcExe.fetch(this, pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public ProcExe getProcExe(String pid) throws SigarException {
        return getProcExe(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public String[] getProcArgs(String pid) throws SigarException {
        return getProcArgs(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public Map getProcEnv(long pid) throws SigarException {
        return ProcEnv.getAll(this, pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public Map getProcEnv(String pid) throws SigarException {
        return getProcEnv(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public String getProcEnv(long pid, String key) throws SigarException {
        return ProcEnv.getValue(this, pid, key);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public String getProcEnv(String pid, String key) throws SigarException {
        return getProcEnv(convertPid(pid), key);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public List getProcModules(long pid) throws SigarException {
        return getProcModulesNative(pid);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public List getProcModules(String pid) throws SigarException {
        return getProcModules(convertPid(pid));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public long getProcPort(String protocol, String port) throws SigarException {
        return getProcPort(NetFlags.getConnectionProtocol(protocol), Integer.parseInt(port));
    }

    public ThreadCpu getThreadCpu() throws SigarException {
        return ThreadCpu.fetch(this, 0L);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public FileSystem[] getFileSystemList() throws SigarException {
        FileSystem[] fslist = getFileSystemListNative();
        if (this.mounts != null) {
            this.mounts.init(fslist);
        }
        return fslist;
    }

    @Override // org.hyperic.sigar.SigarProxy
    public FileSystemUsage getFileSystemUsage(String name) throws SigarException {
        if (name == null) {
            throw new SigarException("name cannot be null");
        }
        return FileSystemUsage.fetch(this, name);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public DiskUsage getDiskUsage(String name) throws SigarException {
        if (name == null) {
            throw new SigarException("name cannot be null");
        }
        return DiskUsage.fetch(this, name);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public FileSystemUsage getMountedFileSystemUsage(String name) throws SigarException {
        FileSystem fs = getFileSystemMap().getFileSystem(name);
        if (fs == null) {
            throw new SigarException(new StringBuffer().append(name).append(" is not a mounted filesystem").toString());
        }
        if (fs instanceof NfsFileSystem) {
            NfsFileSystem nfs = (NfsFileSystem) fs;
            if (!nfs.ping()) {
                throw nfs.getUnreachableException();
            }
        }
        return FileSystemUsage.fetch(this, name);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public FileSystemMap getFileSystemMap() throws SigarException {
        if (this.mounts == null) {
            this.mounts = new FileSystemMap();
        }
        getFileSystemList();
        return this.mounts;
    }

    @Override // org.hyperic.sigar.SigarProxy
    public FileInfo getFileInfo(String name) throws SigarException {
        return FileInfo.fetchFileInfo(this, name);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public FileInfo getLinkInfo(String name) throws SigarException {
        return FileInfo.fetchLinkInfo(this, name);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public DirStat getDirStat(String name) throws SigarException {
        return DirStat.fetch(this, name);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public DirUsage getDirUsage(String name) throws SigarException {
        return DirUsage.fetch(this, name);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public Cpu[] getCpuList() throws SigarException {
        return getCpuListNative();
    }

    @Override // org.hyperic.sigar.SigarProxy
    public String getNetListenAddress(String port) throws SigarException {
        return getNetListenAddress(Long.parseLong(port));
    }

    @Override // org.hyperic.sigar.SigarProxy
    public NetStat getNetStat() throws SigarException {
        NetStat netstat = new NetStat();
        netstat.stat(this);
        return netstat;
    }

    public NetStat getNetStat(byte[] address, long port) throws SigarException {
        NetStat netstat = new NetStat();
        netstat.stat(this, address, port);
        return netstat;
    }

    @Override // org.hyperic.sigar.SigarProxy
    public Tcp getTcp() throws SigarException {
        return Tcp.fetch(this);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public NfsClientV2 getNfsClientV2() throws SigarException {
        return NfsClientV2.fetch(this);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public NfsServerV2 getNfsServerV2() throws SigarException {
        return NfsServerV2.fetch(this);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public NfsClientV3 getNfsClientV3() throws SigarException {
        return NfsClientV3.fetch(this);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public NfsServerV3 getNfsServerV3() throws SigarException {
        return NfsServerV3.fetch(this);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public NetInfo getNetInfo() throws SigarException {
        return NetInfo.fetch(this);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public NetInterfaceConfig getNetInterfaceConfig(String name) throws SigarException {
        return NetInterfaceConfig.fetch(this, name);
    }

    @Override // org.hyperic.sigar.SigarProxy
    public NetInterfaceConfig getNetInterfaceConfig() throws SigarException {
        NetInterfaceConfig ifconfig;
        long flags;
        String[] interfaces = getNetInterfaceList();
        for (String name : interfaces) {
            try {
                ifconfig = getNetInterfaceConfig(name);
                flags = ifconfig.getFlags();
            } catch (SigarException e) {
            }
            if ((flags & 1) > 0 && (flags & 16) <= 0 && (flags & 8) <= 0) {
                return ifconfig;
            }
        }
        throw new SigarException("No ethernet interface available");
    }

    @Override // org.hyperic.sigar.SigarProxy
    public NetInterfaceStat getNetInterfaceStat(String name) throws SigarException {
        return NetInterfaceStat.fetch(this, name);
    }

    public static String getPassword(String prompt) throws IOException {
        try {
            return getPasswordNative(prompt);
        } catch (IOException e) {
            throw e;
        } catch (SigarNotImplementedException e2) {
            System.out.print(prompt);
            return new BufferedReader(new InputStreamReader(System.in)).readLine();
        }
    }

    public void enableLogging(boolean value) {
        if (value) {
            SigarLog.enable(this);
        } else {
            SigarLog.disable(this);
        }
    }
}
