package org.hyperic.sigar.cmd;

import com.alibaba.excel.constant.ExcelXmlConstants;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.pager.PageFetchException;
import org.hyperic.sigar.pager.StaticPageFetcher;
import org.hyperic.sigar.shell.CollectionCompleter;
import org.hyperic.sigar.shell.ProcessQueryCompleter;
import org.hyperic.sigar.shell.ShellCommandBase;
import org.hyperic.sigar.shell.ShellCommandExecException;
import org.hyperic.sigar.shell.ShellCommandUsageException;
import org.hyperic.sigar.util.GetlineCompleter;
import org.hyperic.sigar.util.PrintfFormat;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/SigarCommandBase.class */
public abstract class SigarCommandBase extends ShellCommandBase implements GetlineCompleter {
    protected Shell shell;
    protected PrintStream out;
    protected PrintStream err;
    protected Sigar sigar;
    protected SigarProxy proxy;
    protected List output;
    private CollectionCompleter completer;
    private GetlineCompleter ptqlCompleter;
    private Collection completions;
    private PrintfFormat formatter;
    private ArrayList printfItems;

    public abstract void output(String[] strArr) throws SigarException;

    public SigarCommandBase(Shell shell) {
        this.out = System.out;
        this.err = System.err;
        this.output = new ArrayList();
        this.completions = new ArrayList();
        this.printfItems = new ArrayList();
        this.shell = shell;
        this.out = shell.getOutStream();
        this.err = shell.getErrStream();
        this.sigar = shell.getSigar();
        this.proxy = shell.getSigarProxy();
        this.completer = new CollectionCompleter(shell);
        if (isPidCompleter()) {
            this.ptqlCompleter = new ProcessQueryCompleter(shell);
        }
    }

    public SigarCommandBase() {
        this(new Shell());
        this.shell.setPageSize(-1);
    }

    public void setOutputFormat(String format) {
        this.formatter = new PrintfFormat(format);
    }

    public PrintfFormat getFormatter() {
        return this.formatter;
    }

    public String sprintf(String format, Object[] items) {
        return new PrintfFormat(format).sprintf(items);
    }

    public void printf(String format, Object[] items) {
        println(sprintf(format, items));
    }

    public void printf(Object[] items) {
        PrintfFormat formatter = getFormatter();
        if (formatter == null) {
            this.printfItems.add(items);
        } else {
            println(formatter.sprintf(items));
        }
    }

    public void printf(List items) {
        printf(items.toArray(new Object[0]));
    }

    public void println(String line) {
        if (this.shell.isInteractive()) {
            this.output.add(line);
        } else {
            this.out.println(line);
        }
    }

    private void flushPrintfItems() {
        if (this.printfItems.size() == 0) {
            return;
        }
        int[] max = null;
        Iterator it = this.printfItems.iterator();
        while (it.hasNext()) {
            Object[] items = (Object[]) it.next();
            if (max == null) {
                max = new int[items.length];
                Arrays.fill(max, 0);
            }
            for (int i = 0; i < items.length; i++) {
                int len = items[i].toString().length();
                if (len > max[i]) {
                    max[i] = len;
                }
            }
        }
        StringBuffer format = new StringBuffer();
        for (int i2 = 0; i2 < max.length; i2++) {
            format.append(new StringBuffer().append("%-").append(max[i2]).append(ExcelXmlConstants.CELL_DATA_FORMAT_TAG).toString());
            if (i2 < max.length - 1) {
                format.append("    ");
            }
        }
        Iterator it2 = this.printfItems.iterator();
        while (it2.hasNext()) {
            printf(format.toString(), (Object[]) it2.next());
        }
        this.printfItems.clear();
    }

    public void flush() {
        flushPrintfItems();
        try {
            try {
                this.shell.performPaging(new StaticPageFetcher(this.output));
                this.output.clear();
            } catch (PageFetchException e) {
                this.err.println(new StringBuffer().append("Error paging: ").append(e.getMessage()).toString());
                this.output.clear();
            }
        } catch (Throwable th) {
            this.output.clear();
            throw th;
        }
    }

    protected boolean validateArgs(String[] args) {
        return args.length == 0;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public void processCommand(String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        if (!validateArgs(args)) {
            throw new ShellCommandUsageException(getSyntax());
        }
        try {
            output(args);
        } catch (SigarException e) {
            throw new ShellCommandExecException(e.getMessage());
        }
    }

    public Collection getCompletions() {
        return this.completions;
    }

    public GetlineCompleter getCompleter() {
        return null;
    }

    public boolean isPidCompleter() {
        return false;
    }

    public String completePid(String line) {
        if (line.length() >= 1 && Character.isDigit(line.charAt(0))) {
            return line;
        }
        return this.ptqlCompleter.complete(line);
    }

    @Override // org.hyperic.sigar.util.GetlineCompleter
    public String complete(String line) {
        if (isPidCompleter()) {
            return completePid(line);
        }
        GetlineCompleter c = getCompleter();
        if (c != null) {
            return c.complete(line);
        }
        this.completer.setCollection(getCompletions());
        return this.completer.complete(line);
    }
}
