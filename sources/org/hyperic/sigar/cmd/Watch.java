package org.hyperic.sigar.cmd;

import ch.qos.logback.classic.net.SyslogAppender;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Date;
import org.hyperic.sigar.DirStat;
import org.hyperic.sigar.FileInfo;
import org.hyperic.sigar.FileWatcher;
import org.hyperic.sigar.FileWatcherThread;
import org.hyperic.sigar.ProcFileMirror;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Watch.class */
public class Watch {
    private static void printHeader(Sigar sigar, FileInfo info) throws SigarException {
        String file = info.getName();
        FileInfo link = sigar.getLinkInfo(file);
        if (link.getType() == 6) {
            try {
                System.out.println(new StringBuffer().append(file).append(" -> ").append(new File(file).getCanonicalPath()).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(new StringBuffer().append(link.getTypeChar()).append(info.getPermissionsString()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(info.getUid()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(info.getGid()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(info.getSize()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(new Date(info.getMtime())).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(file).toString());
        if (info.getType() == 2) {
            info.enableDirStat(true);
            DirStat stats = sigar.getDirStat(file);
            System.out.println(new StringBuffer().append("   Files.......").append(stats.getFiles()).toString());
            System.out.println(new StringBuffer().append("   Subdirs.....").append(stats.getSubdirs()).toString());
            System.out.println(new StringBuffer().append("   Symlinks....").append(stats.getSymlinks()).toString());
            System.out.println(new StringBuffer().append("   Chrdevs.....").append(stats.getChrdevs()).toString());
            System.out.println(new StringBuffer().append("   Blkdevs.....").append(stats.getBlkdevs()).toString());
            System.out.println(new StringBuffer().append("   Sockets.....").append(stats.getSockets()).toString());
            System.out.println(new StringBuffer().append("   Total.......").append(stats.getTotal()).toString());
            System.out.println(new StringBuffer().append("   Disk Usage..").append(stats.getDiskUsage()).toString());
        }
    }

    private static void add(Sigar sigar, FileWatcher watcher, String file, boolean recurse) throws SigarException {
        FileInfo info = watcher.add(file);
        printHeader(sigar, info);
        if (recurse && info.getType() == 2) {
            File[] dirs = new File(info.getName()).listFiles(new FileFilter() { // from class: org.hyperic.sigar.cmd.Watch.1
                @Override // java.io.FileFilter
                public boolean accept(File file2) {
                    return file2.isDirectory() && file2.canRead();
                }
            });
            for (File file2 : dirs) {
                add(sigar, watcher, file2.getAbsolutePath(), recurse);
            }
        }
    }

    public static void main(String[] args) throws SigarException, IOException {
        boolean recurse = false;
        Sigar sigar = new Sigar();
        FileWatcherThread watcherThread = FileWatcherThread.getInstance();
        watcherThread.setInterval(1000L);
        FileWatcher watcher = new FileWatcher(sigar) { // from class: org.hyperic.sigar.cmd.Watch.2
            @Override // org.hyperic.sigar.FileWatcher
            public void onChange(FileInfo info) {
                System.out.println(new StringBuffer().append(info.getName()).append(" Changed:\n").append(info.diff()).toString());
            }

            @Override // org.hyperic.sigar.FileWatcher
            public void onNotFound(FileInfo info) {
                System.out.println(new StringBuffer().append(info.getName()).append(" no longer exists").toString());
                remove(info.getName());
            }

            @Override // org.hyperic.sigar.FileWatcher
            public void onException(FileInfo info, SigarException e) {
                System.out.println(new StringBuffer().append("Error checking ").append(info.getName()).append(":").toString());
                e.printStackTrace();
            }
        };
        ProcFileMirror mirror = new ProcFileMirror(sigar, "./proc");
        watcher.setInterval(watcherThread.getInterval());
        mirror.setInterval(watcherThread.getInterval());
        mirror.setExpire(60L);
        for (String arg : args) {
            if (arg.startsWith("/proc/")) {
                mirror.add(arg);
                add(sigar, watcher, mirror.getProcFile(arg), false);
            } else if (arg.equals("-r")) {
                recurse = true;
            } else {
                add(sigar, watcher, arg, recurse);
            }
        }
        watcherThread.add(mirror);
        watcherThread.add(watcher);
        watcherThread.doStart();
        System.out.println("Press any key to stop");
        try {
            System.in.read();
        } catch (IOException e) {
        }
        watcherThread.doStop();
    }
}
