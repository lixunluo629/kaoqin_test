package org.hyperic.sigar.cmd;

import java.io.File;
import java.util.Map;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.win32.FileVersion;
import org.hyperic.sigar.win32.Win32;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/FileVersionInfo.class */
public class FileVersionInfo extends SigarCommandBase {
    public FileVersionInfo(Shell shell) {
        super(shell);
    }

    public FileVersionInfo() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return args.length >= 1;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display file version info";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        for (String exe : args) {
            if (new File(exe).exists()) {
                output(exe);
            } else {
                long[] pids = this.shell.findPids(exe);
                for (long j : pids) {
                    try {
                        output(this.sigar.getProcExe(j).getName());
                    } catch (SigarException e) {
                        println(new StringBuffer().append(exe).append(": ").append(e.getMessage()).toString());
                    }
                }
            }
        }
    }

    private void output(String key, String val) {
        int len = 20 - key.length();
        StringBuffer sb = new StringBuffer();
        sb.append("  ").append(key);
        while (true) {
            int i = len;
            len = i - 1;
            if (i > 0) {
                sb.append('.');
            } else {
                sb.append(val);
                println(sb.toString());
                return;
            }
        }
    }

    public void output(String exe) throws SigarException {
        FileVersion info = Win32.getFileVersion(exe);
        if (info == null) {
            return;
        }
        println(new StringBuffer().append("Version info for file '").append(exe).append("':").toString());
        output("FileVersion", info.getFileVersion());
        output("ProductVersion", info.getProductVersion());
        for (Map.Entry entry : info.getInfo().entrySet()) {
            output((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public static void main(String[] args) throws Exception {
        new FileVersionInfo().processCommand(args);
    }
}
