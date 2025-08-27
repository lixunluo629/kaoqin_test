package org.hyperic.sigar.cmd;

import java.io.IOException;
import java.util.ArrayList;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemMap;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.NfsFileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.shell.FileCompleter;
import org.hyperic.sigar.util.GetlineCompleter;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Df.class */
public class Df extends SigarCommandBase {
    private static final String OUTPUT_FORMAT = "%-15s %4s %4s %5s %4s %-15s %s";
    private static final String[] HEADER = {"Filesystem", "Size", "Used", "Avail", "Use%", "Mounted on", "Type"};
    private static final String[] IHEADER = {"Filesystem", "Inodes", "IUsed", "IFree", "IUse%", "Mounted on", "Type"};
    private GetlineCompleter completer;
    private boolean opt_i;

    public Df(Shell shell) {
        super(shell);
        setOutputFormat(OUTPUT_FORMAT);
        this.completer = new FileCompleter(shell);
    }

    public Df() {
        setOutputFormat(OUTPUT_FORMAT);
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public GetlineCompleter getCompleter() {
        return this.completer;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "[filesystem]";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Report filesystem disk space usage";
    }

    public void printHeader() {
        printf(this.opt_i ? IHEADER : HEADER);
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException, IOException {
        this.opt_i = false;
        ArrayList sys = new ArrayList();
        if (args.length > 0) {
            FileSystemMap mounts = this.proxy.getFileSystemMap();
            for (String arg : args) {
                if (arg.equals("-i")) {
                    this.opt_i = true;
                } else {
                    String name = FileCompleter.expand(arg);
                    FileSystem fs = mounts.getMountPoint(name);
                    if (fs == null) {
                        throw new SigarException(new StringBuffer().append(arg).append(" No such file or directory").toString());
                    }
                    sys.add(fs);
                }
            }
        }
        if (sys.size() == 0) {
            FileSystem[] fslist = this.proxy.getFileSystemList();
            for (FileSystem fileSystem : fslist) {
                sys.add(fileSystem);
            }
        }
        printHeader();
        for (int i = 0; i < sys.size(); i++) {
            output((FileSystem) sys.get(i));
        }
    }

    public void output(FileSystem fs) throws SigarException {
        long pct;
        long total;
        long avail;
        long used;
        String usePct;
        try {
            if (fs instanceof NfsFileSystem) {
                NfsFileSystem nfs = (NfsFileSystem) fs;
                if (!nfs.ping()) {
                    println(nfs.getUnreachableMessage());
                    return;
                }
            }
            FileSystemUsage usage = this.sigar.getFileSystemUsage(fs.getDirName());
            if (this.opt_i) {
                used = usage.getFiles() - usage.getFreeFiles();
                avail = usage.getFreeFiles();
                total = usage.getFiles();
                if (total == 0) {
                    pct = 0;
                } else {
                    long u100 = used * 100;
                    pct = (u100 / total) + (u100 % total != 0 ? 1 : 0);
                }
            } else {
                used = usage.getTotal() - usage.getFree();
                avail = usage.getAvail();
                total = usage.getTotal();
                pct = (long) (usage.getUsePercent() * 100.0d);
            }
        } catch (SigarException e) {
            pct = 0;
            total = 0;
            avail = 0;
            used = 0;
        }
        if (pct == 0) {
            usePct = "-";
        } else {
            usePct = new StringBuffer().append(pct).append(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL).toString();
        }
        ArrayList items = new ArrayList();
        items.add(fs.getDevName());
        items.add(formatSize(total));
        items.add(formatSize(used));
        items.add(formatSize(avail));
        items.add(usePct);
        items.add(fs.getDirName());
        items.add(new StringBuffer().append(fs.getSysTypeName()).append("/").append(fs.getTypeName()).toString());
        printf(items);
    }

    private String formatSize(long size) {
        return this.opt_i ? String.valueOf(size) : Sigar.formatSize(size * 1024);
    }

    public static void main(String[] args) throws Exception {
        new Df().processCommand(args);
    }
}
