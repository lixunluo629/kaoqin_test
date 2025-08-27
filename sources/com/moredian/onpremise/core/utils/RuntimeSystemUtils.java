package com.moredian.onpremise.core.utils;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/RuntimeSystemUtils.class */
public class RuntimeSystemUtils {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) RuntimeSystemUtils.class);
    private static Sigar sigar;

    public static String getMotherboardSN() throws IOException {
        String result;
        String result2 = "";
        if ("linux".equalsIgnoreCase(os().getName())) {
            result = getSerialNumber("dmidecode |grep 'Serial Number'", "Serial Number", ":");
        } else {
            try {
                File file = File.createTempFile("realhowto", ".vbs");
                file.deleteOnExit();
                FileWriter fw = new FileWriter(file);
                fw.write("Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\nSet colItems = objWMIService.ExecQuery _ \n   (\"Select * from Win32_BaseBoard\") \nFor Each objItem in colItems \n    Wscript.Echo objItem.SerialNumber \n    exit for  ' do the first cpu only! \nNext \n");
                fw.close();
                Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while (true) {
                    String line = input.readLine();
                    if (line == null) {
                        break;
                    }
                    result2 = result2 + line;
                }
                input.close();
            } catch (Exception e) {
                logger.error("error:{}", (Throwable) e);
            }
            result = result2.trim();
        }
        return result;
    }

    public static String getCPUSerial() throws IOException {
        String result;
        String result2 = "";
        if ("linux".equalsIgnoreCase(os().getName())) {
            result = getSerialNumber("dmidecode -t processor | grep 'ID'", "ID", ":");
        } else {
            try {
                File file = File.createTempFile("tmp", ".vbs");
                file.deleteOnExit();
                FileWriter fw = new FileWriter(file);
                fw.write("Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\nSet colItems = objWMIService.ExecQuery _ \n   (\"Select * from Win32_Processor\") \nFor Each objItem in colItems \n    Wscript.Echo objItem.ProcessorId \n    exit for  ' do the first cpu only! \nNext \n");
                fw.close();
                Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while (true) {
                    String line = input.readLine();
                    if (line == null) {
                        break;
                    }
                    result2 = result2 + line;
                }
                input.close();
                file.delete();
            } catch (Exception e) {
                e.fillInStackTrace();
            }
            if (result2.trim().length() < 1 || result2 == null) {
                result2 = "无CPU_ID被读取";
            }
            result = result2.trim();
        }
        return result;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x00ab, code lost:
    
        r5 = r0.substring(r0.indexOf("-") - 2);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getMac() throws java.io.IOException {
        /*
            java.lang.String r0 = ""
            r5 = r0
            java.lang.String r0 = "linux"
            org.hyperic.sigar.OperatingSystem r1 = os()     // Catch: java.lang.Exception -> Lc4
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Exception -> Lc4
            boolean r0 = r0.equalsIgnoreCase(r1)     // Catch: java.lang.Exception -> Lc4
            if (r0 == 0) goto L79
            org.hyperic.sigar.Sigar r0 = new org.hyperic.sigar.Sigar     // Catch: java.lang.Exception -> Lc4
            r1 = r0
            r1.<init>()     // Catch: java.lang.Exception -> Lc4
            r6 = r0
            r0 = r6
            java.lang.String[] r0 = r0.getNetInterfaceList()     // Catch: java.lang.Exception -> Lc4
            r7 = r0
            r0 = r7
            r8 = r0
            r0 = r8
            int r0 = r0.length     // Catch: java.lang.Exception -> Lc4
            r9 = r0
            r0 = 0
            r10 = r0
        L27:
            r0 = r10
            r1 = r9
            if (r0 >= r1) goto L76
            r0 = r8
            r1 = r10
            r0 = r0[r1]     // Catch: java.lang.Exception -> Lc4
            r11 = r0
            r0 = r6
            r1 = r11
            org.hyperic.sigar.NetInterfaceConfig r0 = r0.getNetInterfaceConfig(r1)     // Catch: java.lang.Exception -> Lc4
            r12 = r0
            java.lang.String r0 = "127.0.0.1"
            r1 = r12
            java.lang.String r1 = r1.getAddress()     // Catch: java.lang.Exception -> Lc4
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Exception -> Lc4
            if (r0 != 0) goto L70
            r0 = r12
            long r0 = r0.getFlags()     // Catch: java.lang.Exception -> Lc4
            r1 = 8
            long r0 = r0 & r1
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L70
            java.lang.String r0 = "00:00:00:00:00:00"
            r1 = r12
            java.lang.String r1 = r1.getHwaddr()     // Catch: java.lang.Exception -> Lc4
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Exception -> Lc4
            if (r0 == 0) goto L67
            goto L70
        L67:
            r0 = r12
            java.lang.String r0 = r0.getHwaddr()     // Catch: java.lang.Exception -> Lc4
            r5 = r0
            goto L76
        L70:
            int r10 = r10 + 1
            goto L27
        L76:
            goto Lc1
        L79:
            java.lang.Runtime r0 = java.lang.Runtime.getRuntime()     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            java.lang.String r1 = "ipconfig /all"
            java.lang.Process r0 = r0.exec(r1)     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            r6 = r0
            java.io.InputStreamReader r0 = new java.io.InputStreamReader     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            r1 = r0
            r2 = r6
            java.io.InputStream r2 = r2.getInputStream()     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            r1.<init>(r2)     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            r7 = r0
            java.io.LineNumberReader r0 = new java.io.LineNumberReader     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            r1 = r0
            r2 = r7
            r1.<init>(r2)     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            r8 = r0
        L97:
            r0 = r8
            java.lang.String r0 = r0.readLine()     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            r1 = r0
            r9 = r1
            if (r0 == 0) goto Lbd
            r0 = r9
            java.lang.String r1 = "Physical Address"
            int r0 = r0.indexOf(r1)     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            if (r0 <= 0) goto L97
            r0 = r9
            r1 = r9
            java.lang.String r2 = "-"
            int r1 = r1.indexOf(r2)     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            r2 = 2
            int r1 = r1 - r2
            java.lang.String r0 = r0.substring(r1)     // Catch: java.io.IOException -> Lc0 java.lang.Exception -> Lc4
            r5 = r0
            goto Lbd
        Lbd:
            goto Lc1
        Lc0:
            r6 = move-exception
        Lc1:
            goto Lc5
        Lc4:
            r6 = move-exception
        Lc5:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.moredian.onpremise.core.utils.RuntimeSystemUtils.getMac():java.lang.String");
    }

    public static String getCloudUuid() throws IOException {
        String result = "";
        if ("linux".equalsIgnoreCase(os().getName())) {
            result = getSerialNumber("dmidecode |grep 'UUID'", "UUID", ":");
        }
        return result;
    }

    public static FileSystemUsage memory() {
        Sigar sigar2 = new Sigar();
        try {
            return sigar2.getFileSystemUsage("/home");
        } catch (SigarException e) {
            logger.error("error:{}", (Throwable) e);
            return new FileSystemUsage();
        }
    }

    public static OperatingSystem os() {
        OperatingSystem os = OperatingSystem.getInstance();
        return os;
    }

    public static String getSerialNumber(String cmd, String record, String symbol) throws IOException {
        String execResult = executeLinuxCmd(cmd);
        String[] infos = execResult.split(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        for (String str : infos) {
            String info = str.trim();
            if (info.indexOf(record) != -1) {
                info.replace(SymbolConstants.SPACE_SYMBOL, "");
                String[] sn = info.split(symbol);
                if (sn.length != 2) {
                    return null;
                }
                return sn[1];
            }
        }
        return null;
    }

    public static String executeLinuxCmd(String cmd) throws IOException {
        try {
            Runtime run = Runtime.getRuntime();
            Process process = run.exec(cmd);
            InputStream in = process.getInputStream();
            new BufferedReader(new InputStreamReader(in));
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[8192];
            while (true) {
                int n = in.read(b);
                if (n != -1) {
                    out.append(new String(b, 0, n));
                } else {
                    in.close();
                    process.destroy();
                    return out.toString();
                }
            }
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static void getFileInfos() {
        try {
            FileSystem[] fslist = getInstance().getFileSystemList();
            for (FileSystem fs : fslist) {
                if (!"/home".equals(fs.getDirName())) {
                }
            }
        } catch (SigarException e) {
        }
    }

    public static Sigar getInstance() {
        if (null == sigar) {
            sigar = new Sigar();
        }
        return sigar;
    }
}
