package org.hyperic.sigar;

import java.util.List;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/SigarProxy.class */
public interface SigarProxy {
    long getPid();

    long getServicePid(String str) throws SigarException;

    Mem getMem() throws SigarException;

    Swap getSwap() throws SigarException;

    Cpu getCpu() throws SigarException;

    CpuPerc getCpuPerc() throws SigarException;

    Uptime getUptime() throws SigarException;

    ResourceLimit getResourceLimit() throws SigarException;

    double[] getLoadAverage() throws SigarException;

    long[] getProcList() throws SigarException;

    ProcStat getProcStat() throws SigarException;

    ProcMem getProcMem(long j) throws SigarException;

    ProcMem getProcMem(String str) throws SigarException;

    ProcMem getMultiProcMem(String str) throws SigarException;

    ProcState getProcState(long j) throws SigarException;

    ProcState getProcState(String str) throws SigarException;

    ProcTime getProcTime(long j) throws SigarException;

    ProcTime getProcTime(String str) throws SigarException;

    ProcCpu getProcCpu(long j) throws SigarException;

    ProcCpu getProcCpu(String str) throws SigarException;

    MultiProcCpu getMultiProcCpu(String str) throws SigarException;

    ProcCred getProcCred(long j) throws SigarException;

    ProcCred getProcCred(String str) throws SigarException;

    ProcCredName getProcCredName(long j) throws SigarException;

    ProcCredName getProcCredName(String str) throws SigarException;

    ProcFd getProcFd(long j) throws SigarException;

    ProcFd getProcFd(String str) throws SigarException;

    ProcExe getProcExe(long j) throws SigarException;

    ProcExe getProcExe(String str) throws SigarException;

    String[] getProcArgs(long j) throws SigarException;

    String[] getProcArgs(String str) throws SigarException;

    Map getProcEnv(long j) throws SigarException;

    Map getProcEnv(String str) throws SigarException;

    String getProcEnv(long j, String str) throws SigarException;

    String getProcEnv(String str, String str2) throws SigarException;

    List getProcModules(long j) throws SigarException;

    List getProcModules(String str) throws SigarException;

    long getProcPort(int i, long j) throws SigarException;

    long getProcPort(String str, String str2) throws SigarException;

    FileSystem[] getFileSystemList() throws SigarException;

    FileSystemMap getFileSystemMap() throws SigarException;

    FileSystemUsage getMountedFileSystemUsage(String str) throws SigarException;

    FileSystemUsage getFileSystemUsage(String str) throws SigarException;

    DiskUsage getDiskUsage(String str) throws SigarException;

    FileInfo getFileInfo(String str) throws SigarException;

    FileInfo getLinkInfo(String str) throws SigarException;

    DirStat getDirStat(String str) throws SigarException;

    DirUsage getDirUsage(String str) throws SigarException;

    CpuInfo[] getCpuInfoList() throws SigarException;

    Cpu[] getCpuList() throws SigarException;

    CpuPerc[] getCpuPercList() throws SigarException;

    NetRoute[] getNetRouteList() throws SigarException;

    NetInterfaceConfig getNetInterfaceConfig(String str) throws SigarException;

    NetInterfaceConfig getNetInterfaceConfig() throws SigarException;

    NetInterfaceStat getNetInterfaceStat(String str) throws SigarException;

    String[] getNetInterfaceList() throws SigarException;

    NetConnection[] getNetConnectionList(int i) throws SigarException;

    String getNetListenAddress(long j) throws SigarException;

    String getNetListenAddress(String str) throws SigarException;

    NetStat getNetStat() throws SigarException;

    String getNetServicesName(int i, long j);

    Who[] getWhoList() throws SigarException;

    Tcp getTcp() throws SigarException;

    NfsClientV2 getNfsClientV2() throws SigarException;

    NfsServerV2 getNfsServerV2() throws SigarException;

    NfsClientV3 getNfsClientV3() throws SigarException;

    NfsServerV3 getNfsServerV3() throws SigarException;

    NetInfo getNetInfo() throws SigarException;

    String getFQDN() throws SigarException;
}
