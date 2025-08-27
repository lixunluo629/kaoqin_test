package org.hyperic.sigar.cmd;

import com.alibaba.excel.constant.ExcelXmlConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.hyperic.sigar.FileInfo;
import org.hyperic.sigar.FileTail;
import org.hyperic.sigar.FileWatcherThread;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Tail.class */
public class Tail {
    public boolean follow;
    public int number = 10;
    public List files = new ArrayList();

    public void parseArgs(String[] args) throws SigarException {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) != '-') {
                this.files.add(arg);
            } else {
                String arg2 = arg.substring(1);
                if (arg2.equals(ExcelXmlConstants.CELL_FORMULA_TAG)) {
                    this.follow = true;
                } else if (Character.isDigit(arg2.charAt(0))) {
                    this.number = Integer.parseInt(arg2);
                } else {
                    throw new SigarException(new StringBuffer().append("Unknown argument: ").append(args[i]).toString());
                }
            }
        }
    }

    public static void main(String[] args) throws SigarException, IOException {
        Sigar sigar = new Sigar();
        FileWatcherThread watcherThread = FileWatcherThread.getInstance();
        watcherThread.doStart();
        watcherThread.setInterval(1000L);
        FileTail watcher = new FileTail(sigar) { // from class: org.hyperic.sigar.cmd.Tail.1
            @Override // org.hyperic.sigar.FileTail
            public void tail(FileInfo info, Reader reader) throws IOException {
                BufferedReader buffer = new BufferedReader(reader);
                if (getFiles().size() > 1) {
                    System.out.println(new StringBuffer().append("==> ").append(info.getName()).append(" <==").toString());
                }
                while (true) {
                    try {
                        String line = buffer.readLine();
                        if (line != null) {
                            System.out.println(line);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        System.out.println(e);
                        return;
                    }
                }
            }
        };
        for (String str : args) {
            watcher.add(str);
        }
        watcherThread.add(watcher);
        try {
            System.in.read();
        } catch (IOException e) {
        }
        watcherThread.doStop();
    }
}
