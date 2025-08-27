package org.hyperic.sigar.shell;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.itextpdf.kernel.xmp.PdfConst;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.pager.PageControl;
import org.hyperic.sigar.pager.PageFetchException;
import org.hyperic.sigar.pager.PageFetcher;
import org.hyperic.sigar.pager.PageList;
import org.hyperic.sigar.util.Getline;
import org.hyperic.sigar.util.GetlineCompleter;
import org.hyperic.sigar.util.IteratorIterator;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellBase.class */
public abstract class ShellBase implements ShellCommandMapper, GetlineCompleter, SIGINT {
    public static final String PROP_PAGE_SIZE = "page.size";
    private static final int DEFAULT_PAGE_SIZE = 20;
    private HashMap hiddenCommands;
    protected Getline gl;
    private boolean doHistoryAdd;
    private int pageSize;
    private boolean isRedirected;
    private GetlineCompleter completer;
    static Class class$org$hyperic$sigar$util$GetlineCompleter;
    private String name = null;
    private String prompt = null;
    private Map handlers = null;
    protected PrintStream out = System.out;
    protected PrintStream err = System.err;

    @Override // org.hyperic.sigar.shell.SIGINT
    public void handleSIGINT() {
        this.gl.reset();
    }

    public void initHistory() throws IOException {
        String historyFileName = new StringBuffer().append(".").append(this.name).append("_history").toString();
        initHistory(new File(System.getProperty("user.home"), historyFileName));
    }

    public void initHistory(File file) throws IOException {
        this.doHistoryAdd = true;
        this.gl.initHistoryFile(file);
    }

    public void registerSigIntHandler() {
        ShellIntHandler.register(this);
    }

    public void init(String applicationName, PrintStream out, PrintStream err) {
        this.name = applicationName;
        this.prompt = applicationName;
        this.gl = new Getline();
        this.out = out;
        this.err = err;
        this.doHistoryAdd = false;
        this.pageSize = Integer.getInteger(PROP_PAGE_SIZE, 20).intValue();
        if (this.pageSize != -1) {
            this.pageSize--;
            if (this.pageSize < 1) {
                this.pageSize = 1;
            }
        }
        this.isRedirected = false;
        this.handlers = new HashMap();
        this.hiddenCommands = new HashMap();
        try {
            ShellCommand_quit quitCommand = new ShellCommand_quit();
            ShellCommand_source sourceCommand = new ShellCommand_source();
            registerCommandHandler(".", sourceCommand);
            registerCommandHandler("alias", new ShellCommand_alias());
            registerCommandHandler("exit", quitCommand);
            registerCommandHandler(BeanUtil.PREFIX_GETTER_GET, new ShellCommand_get());
            registerCommandHandler("help", new ShellCommand_help());
            registerCommandHandler("q", quitCommand);
            registerCommandHandler("quit", quitCommand);
            registerCommandHandler("set", new ShellCommand_set());
            registerCommandHandler(PdfConst.Source, sourceCommand);
            registerCommandHandler("sleep", new ShellCommand_sleep());
        } catch (Exception e) {
            err.println(new StringBuffer().append("ERROR: could not register standard commands: ").append(e).toString());
            e.printStackTrace(err);
        }
        setHandlerHidden(".", true);
        setHandlerHidden("q", true);
        setHandlerHidden("exit", true);
        registerSigIntHandler();
        this.completer = new CollectionCompleter(this, this) { // from class: org.hyperic.sigar.shell.ShellBase.1
            private final ShellBase this$0;

            {
                this.this$0 = this;
            }

            @Override // org.hyperic.sigar.shell.CollectionCompleter
            public Iterator getIterator() {
                IteratorIterator it = new IteratorIterator();
                it.add(this.this$0.getCommandNameIterator());
                it.add(ShellCommand_alias.getAliases());
                return it;
            }
        };
    }

    public void readRCFile(File rcFile, boolean echoCommands) throws IOException {
        FileInputStream is = null;
        boolean oldHistAdd = this.doHistoryAdd;
        this.doHistoryAdd = false;
        try {
            is = new FileInputStream(rcFile);
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                String line2 = line.trim();
                if (!line2.startsWith("#") && line2.length() != 0) {
                    if (echoCommands) {
                        this.err.println(line2);
                    }
                    handleCommand(line2);
                }
            }
            if (is != null) {
                is.close();
            }
            this.doHistoryAdd = oldHistAdd;
        } catch (Throwable th) {
            if (is != null) {
                is.close();
            }
            this.doHistoryAdd = oldHistAdd;
            throw th;
        }
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void registerCommandHandler(String commandName, ShellCommandHandler handler) throws ShellCommandInitException {
        this.handlers.put(commandName, handler);
        handler.init(commandName, this);
    }

    public String getInput(String prompt) throws IOException {
        return this.gl.getLine(prompt);
    }

    public String getInput(String prompt, boolean addToHistory) throws IOException {
        return this.gl.getLine(prompt, addToHistory);
    }

    public String getHiddenInput(String prompt) throws IOException {
        return Sigar.getPassword(prompt);
    }

    public void sendToOutStream(String s) {
        this.out.println(s);
    }

    public void sendToErrStream(String s) {
        this.err.println(s);
    }

    public void run() {
        ShellIntHandler.push(this);
        while (true) {
            try {
                String input = this.gl.getLine(new StringBuffer().append(this.prompt).append("> ").toString(), false);
                if (input == null || input.trim().length() == 0) {
                    if (!Getline.isTTY()) {
                        break;
                    }
                } else {
                    try {
                        handleCommand(input);
                    } catch (NormalQuitCommandException e) {
                    }
                }
            } catch (EOFException e2) {
            } catch (Exception e3) {
                this.err.println(new StringBuffer().append("Fatal error reading input line: ").append(e3).toString());
                e3.printStackTrace(this.err);
                return;
            }
        }
        if (Getline.isTTY()) {
            this.out.println("Goodbye.");
        }
    }

    public void handleCommand(String line) {
        try {
            String[] args = explodeQuoted(line);
            if (args.length != 0) {
                handleCommand(line, args);
            }
        } catch (IllegalArgumentException e) {
            this.out.println("Syntax error: Unbalanced quotes");
        }
    }

    public void handleCommand(String line, String[] args) {
        PrintStream oldSysOut = null;
        PrintStream oldOut = null;
        String command = args[0];
        if (args.length == 0) {
            return;
        }
        ShellCommandHandler handler = getHandler(command);
        if (handler == null) {
            String[] aliasArgs = ShellCommand_alias.getAlias(command);
            if (aliasArgs == null) {
                this.err.println(new StringBuffer().append("unknown command: ").append(command).toString());
                return;
            } else {
                handleCommand(line, aliasArgs);
                return;
            }
        }
        int useArgs = args.length;
        if (args.length > 2 && args[args.length - 2].equals(">")) {
            oldSysOut = System.out;
            oldOut = this.out;
            try {
                FileOutputStream fOut = new FileOutputStream(args[args.length - 1]);
                PrintStream newOut = new PrintStream(fOut);
                this.isRedirected = true;
                this.out = newOut;
                System.setOut(newOut);
                useArgs -= 2;
            } catch (IOException exc) {
                this.err.println(new StringBuffer().append("Failed to redirect to output file: ").append(exc).toString());
                return;
            }
        }
        String[] subArgs = new String[useArgs - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        try {
            try {
                try {
                    try {
                        processCommand(handler, subArgs);
                        if (this.doHistoryAdd) {
                            this.gl.addToHistory(line);
                        }
                        if (oldSysOut != null) {
                            this.isRedirected = false;
                            System.setOut(oldSysOut);
                            this.out = oldOut;
                        }
                    } catch (NormalQuitCommandException e) {
                        throw e;
                    } catch (ShellCommandUsageException e2) {
                        String msg = e2.getMessage();
                        if (msg == null || msg.trim().length() == 0) {
                            msg = "an unknown error occurred";
                        }
                        this.err.println(new StringBuffer().append(command).append(": ").append(msg).toString());
                        if (this.doHistoryAdd) {
                            this.gl.addToHistory(line);
                        }
                        if (oldSysOut != null) {
                            this.isRedirected = false;
                            System.setOut(oldSysOut);
                            this.out = oldOut;
                        }
                    }
                } catch (Exception e3) {
                    this.err.println(new StringBuffer().append("Unexpected exception processing command '").append(command).append("': ").append(e3).toString());
                    e3.printStackTrace(this.err);
                    if (this.doHistoryAdd) {
                        this.gl.addToHistory(line);
                    }
                    if (oldSysOut != null) {
                        this.isRedirected = false;
                        System.setOut(oldSysOut);
                        this.out = oldOut;
                    }
                }
            } catch (ShellCommandExecException e4) {
                this.err.println(e4.getMessage());
                if (this.doHistoryAdd) {
                    this.gl.addToHistory(line);
                }
                if (oldSysOut != null) {
                    this.isRedirected = false;
                    System.setOut(oldSysOut);
                    this.out = oldOut;
                }
            }
        } catch (Throwable th) {
            if (this.doHistoryAdd) {
                this.gl.addToHistory(line);
            }
            if (oldSysOut != null) {
                this.isRedirected = false;
                System.setOut(oldSysOut);
                this.out = oldOut;
            }
            throw th;
        }
    }

    public void processCommand(ShellCommandHandler handler, String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        handler.processCommand(args);
    }

    public PrintStream getOutStream() {
        return this.out;
    }

    public PrintStream getErrStream() {
        return this.err;
    }

    public Getline getGetline() {
        return this.gl;
    }

    public boolean hasCompleter(ShellCommandHandler handler) throws Throwable {
        Class clsClass$;
        if (class$org$hyperic$sigar$util$GetlineCompleter == null) {
            clsClass$ = class$("org.hyperic.sigar.util.GetlineCompleter");
            class$org$hyperic$sigar$util$GetlineCompleter = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$util$GetlineCompleter;
        }
        return clsClass$.isAssignableFrom(handler.getClass());
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public String complete(ShellCommandHandler handler, String line) {
        if (hasCompleter(handler)) {
            return ((GetlineCompleter) handler).complete(line);
        }
        return line;
    }

    @Override // org.hyperic.sigar.util.GetlineCompleter
    public String complete(String line) {
        if (line == null) {
            return null;
        }
        int ix = line.indexOf(SymbolConstants.SPACE_SYMBOL);
        if (ix != -1) {
            String cmd = line.substring(0, ix);
            String sub = line.substring(ix + 1, line.length());
            ShellCommandHandler handler = getHandler(cmd);
            if (handler != null) {
                String hline = complete(handler, sub);
                return new StringBuffer().append(cmd).append(SymbolConstants.SPACE_SYMBOL).append(hline).toString();
            }
            return line;
        }
        String line2 = this.completer.complete(line);
        if (getHandler(line2) != null) {
            return new StringBuffer().append(line2).append(SymbolConstants.SPACE_SYMBOL).toString();
        }
        return line2;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandMapper
    public ShellCommandHandler getHandler(String command) {
        if (command == null) {
            return null;
        }
        return (ShellCommandHandler) this.handlers.get(command.toLowerCase());
    }

    public void setHandlerHidden(String handlerName, boolean isHidden) {
        if (getHandler(handlerName) == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Unknown handler: ").append(handlerName).toString());
        }
        this.hiddenCommands.put(handlerName, isHidden ? Boolean.TRUE : Boolean.FALSE);
    }

    public boolean handlerIsHidden(String handlerName) {
        return this.hiddenCommands.get(handlerName) != null;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandMapper
    public Iterator getCommandNameIterator() {
        ArrayList keyArray = new ArrayList();
        for (String keyName : this.handlers.keySet()) {
            if (!handlerIsHidden(keyName)) {
                keyArray.add(keyName);
            }
        }
        String[] keys = (String[]) keyArray.toArray(new String[0]);
        Arrays.sort(keys);
        return Arrays.asList(keys).iterator();
    }

    public void shutdown() {
    }

    public boolean isRedirected() {
        return this.isRedirected;
    }

    public void setPageSize(int size) {
        if (size == 0 || size < -1) {
            throw new IllegalArgumentException("Page size must be > 0 or -1");
        }
        this.pageSize = size;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    private int getNumPages(PageControl control, PageList list) {
        int pageSize = control.getPagesize();
        int totalElems = list.getTotalSize();
        if (pageSize == -1) {
            return 1;
        }
        if (pageSize == 0) {
            return 0;
        }
        if (totalElems % pageSize == 0) {
            return totalElems / pageSize;
        }
        return (totalElems / pageSize) + 1;
    }

    private void printPage(PrintStream out, PageList data, int lineNo, boolean printLineNumbers) {
        Iterator i = data.iterator();
        while (i.hasNext()) {
            if (printLineNumbers) {
                int i2 = lineNo;
                lineNo++;
                out.print(new StringBuffer().append(i2).append(": ").toString());
            }
            out.println((String) i.next());
        }
    }

    public PageControl getDefaultPageControl() {
        PageControl res = new PageControl(0, getPageSize() == -1 ? -1 : getPageSize());
        return res;
    }

    public void performPaging(PageFetcher fetcher) throws PageFetchException, NumberFormatException {
        performPaging(fetcher, getDefaultPageControl());
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x01a3 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x004d A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void performPaging(org.hyperic.sigar.pager.PageFetcher r7, org.hyperic.sigar.pager.PageControl r8) throws org.hyperic.sigar.pager.PageFetchException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 466
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.hyperic.sigar.shell.ShellBase.performPaging(org.hyperic.sigar.pager.PageFetcher, org.hyperic.sigar.pager.PageControl):void");
    }

    private static String[] explodeQuoted(String arg) {
        ArrayList res = new ArrayList();
        boolean inQuote = false;
        StringTokenizer quoteTok = new StringTokenizer(arg.trim(), SymbolConstants.QUOTES_SYMBOL, true);
        while (quoteTok.hasMoreTokens()) {
            String elem = (String) quoteTok.nextElement();
            if (elem.equals(SymbolConstants.QUOTES_SYMBOL)) {
                inQuote = !inQuote;
            } else if (inQuote) {
                res.add(elem);
            } else {
                StringTokenizer spaceTok = new StringTokenizer(elem.trim());
                while (spaceTok.hasMoreTokens()) {
                    res.add(spaceTok.nextToken());
                }
            }
        }
        if (inQuote) {
            throw new IllegalArgumentException("Unbalanced quotation marks");
        }
        return (String[]) res.toArray(new String[0]);
    }
}
