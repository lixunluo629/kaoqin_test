package org.hyperic.sigar.cmd;

import java.io.IOException;
import java.util.ArrayList;
import org.hyperic.sigar.DiskUsage;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemMap;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.shell.FileCompleter;
import org.hyperic.sigar.util.GetlineCompleter;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Iostat.class */
public class Iostat extends SigarCommandBase {
    private static final String OUTPUT_FORMAT = "%-15s %-15s %10s %10s %7s %7s %5s %5s";
    private static final String[] HEADER = {"Filesystem", "Mounted on", "Reads", "Writes", "R-bytes", "W-bytes", "Queue", "Svctm"};
    private GetlineCompleter completer;

    public Iostat(Shell shell) {
        super(shell);
        setOutputFormat(OUTPUT_FORMAT);
        this.completer = new FileCompleter(shell);
    }

    public Iostat() {
        setOutputFormat(OUTPUT_FORMAT);
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public GetlineCompleter getCompleter() {
        return this.completer;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return args.length <= 1;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "[filesystem]";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Report filesystem disk i/o";
    }

    public void printHeader() {
        printf(HEADER);
    }

    private String svctm(double val) {
        return sprintf("%3.2f", new Object[]{new Double(val)});
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException, IOException {
        if (args.length == 1) {
            String arg = args[0];
            if (arg.indexOf(47) != -1 || arg.indexOf(92) != -1) {
                outputFileSystem(arg);
                return;
            } else {
                outputDisk(arg);
                return;
            }
        }
        FileSystem[] fslist = this.proxy.getFileSystemList();
        printHeader();
        for (int i = 0; i < fslist.length; i++) {
            if (fslist[i].getType() == 2) {
                output(fslist[i]);
            }
        }
    }

    public void outputFileSystem(String arg) throws SigarException, IOException {
        FileSystemMap mounts = this.proxy.getFileSystemMap();
        String name = FileCompleter.expand(arg);
        FileSystem fs = mounts.getMountPoint(name);
        if (fs != null) {
            printHeader();
            output(fs);
            return;
        }
        throw new SigarException(new StringBuffer().append(arg).append(" No such file or directory").toString());
    }

    public void outputDisk(String name) throws SigarException {
        DiskUsage disk = this.sigar.getDiskUsage(name);
        ArrayList items = new ArrayList();
        printHeader();
        items.add(name);
        items.add("-");
        items.add(String.valueOf(disk.getReads()));
        items.add(String.valueOf(disk.getWrites()));
        if (disk.getReadBytes() == -1) {
            items.add("-");
            items.add("-");
        } else {
            items.add(Sigar.formatSize(disk.getReadBytes()));
            items.add(Sigar.formatSize(disk.getWriteBytes()));
        }
        if (disk.getQueue() == -1.0d) {
            items.add("-");
        } else {
            items.add(svctm(disk.getQueue()));
        }
        if (disk.getServiceTime() == -1.0d) {
            items.add("-");
        } else {
            items.add(svctm(disk.getServiceTime()));
        }
        printf(items);
    }

    public void output(FileSystem fs) throws SigarException {
        FileSystemUsage usage = this.sigar.getFileSystemUsage(fs.getDirName());
        ArrayList items = new ArrayList();
        items.add(fs.getDevName());
        items.add(fs.getDirName());
        items.add(String.valueOf(usage.getDiskReads()));
        items.add(String.valueOf(usage.getDiskWrites()));
        if (usage.getDiskReadBytes() == -1) {
            items.add("-");
            items.add("-");
        } else {
            items.add(Sigar.formatSize(usage.getDiskReadBytes()));
            items.add(Sigar.formatSize(usage.getDiskWriteBytes()));
        }
        if (usage.getDiskQueue() == -1.0d) {
            items.add("-");
        } else {
            items.add(svctm(usage.getDiskQueue()));
        }
        if (usage.getDiskServiceTime() == -1.0d) {
            items.add("-");
        } else {
            items.add(svctm(usage.getDiskServiceTime()));
        }
        printf(items);
    }

    public static void main(String[] args) throws Exception {
        new Iostat().processCommand(args);
    }
}
