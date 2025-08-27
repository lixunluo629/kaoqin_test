package org.aspectj.weaver;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHolder;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.aspectj.weaver.tools.Traceable;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Dump.class */
public class Dump {
    public static final String DUMP_CONDITION_PROPERTY = "org.aspectj.weaver.Dump.condition";
    public static final String DUMP_DIRECTORY_PROPERTY = "org.aspectj.dump.directory";
    private static final String FILENAME_PREFIX = "ajcore";
    private static final String FILENAME_SUFFIX = "txt";
    public static final String DUMP_EXCLUDED = "Excluded";
    public static final String NULL_OR_EMPTY = "Empty";
    private static Class<?> exceptionClass;
    private String reason;
    private String fileName;
    private PrintStream print;
    private static String[] savedCommandLine;
    private static List<String> savedFullClasspath;
    private static IMessageHolder savedMessageHolder;
    private static IMessage.Kind conditionKind = IMessage.ABORT;
    private static File directory = new File(".");
    public static final String UNKNOWN_FILENAME = "Unknown";
    private static String lastDumpFileName = UNKNOWN_FILENAME;
    private static boolean preserveOnNextReset = false;
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(Dump.class);

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Dump$INode.class */
    public interface INode {
        void accept(IVisitor iVisitor);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Dump$IVisitor.class */
    public interface IVisitor {
        void visitObject(Object obj);

        void visitList(List list);
    }

    static {
        String exceptionName = System.getProperty("org.aspectj.weaver.Dump.exception", "true");
        if (!exceptionName.equals("false")) {
            setDumpOnException(true);
        }
        String conditionName = System.getProperty(DUMP_CONDITION_PROPERTY);
        if (conditionName != null) {
            setDumpOnExit(conditionName);
        }
        String directoryName = System.getProperty(DUMP_DIRECTORY_PROPERTY);
        if (directoryName != null) {
            setDumpDirectory(directoryName);
        }
    }

    public static void preserveOnNextReset() {
        preserveOnNextReset = true;
    }

    public static void reset() {
        if (preserveOnNextReset) {
            preserveOnNextReset = false;
        } else {
            savedMessageHolder = null;
        }
    }

    public static String dump(String reason) {
        Dump dump = null;
        try {
            dump = new Dump(reason);
            String fileName = dump.getFileName();
            dump.dumpDefault();
            if (dump != null) {
                dump.close();
            }
            return fileName;
        } catch (Throwable th) {
            if (dump != null) {
                dump.close();
            }
            throw th;
        }
    }

    public static String dumpWithException(Throwable th) {
        return dumpWithException(savedMessageHolder, th);
    }

    public static String dumpWithException(IMessageHolder messageHolder, Throwable th) {
        if (!getDumpOnException()) {
            return null;
        }
        if (trace.isTraceEnabled()) {
            trace.enter("dumpWithException", (Object) null, new Object[]{messageHolder, th});
        }
        Dump dump = null;
        try {
            dump = new Dump(th.getClass().getName());
            String fileName = dump.getFileName();
            dump.dumpException(messageHolder, th);
            if (dump != null) {
                dump.close();
            }
            if (trace.isTraceEnabled()) {
                trace.exit("dumpWithException", fileName);
            }
            return fileName;
        } catch (Throwable th2) {
            if (dump != null) {
                dump.close();
            }
            throw th2;
        }
    }

    public static String dumpOnExit() {
        return dumpOnExit(savedMessageHolder, false);
    }

    public static String dumpOnExit(IMessageHolder messageHolder, boolean reset) throws UnsupportedOperationException {
        String fileName;
        if (!getDumpOnException()) {
            return null;
        }
        if (trace.isTraceEnabled()) {
            trace.enter("dumpOnExit", (Object) null, messageHolder);
        }
        if (!shouldDumpOnExit(messageHolder)) {
            fileName = DUMP_EXCLUDED;
        } else {
            Dump dump = null;
            try {
                dump = new Dump(conditionKind.toString());
                fileName = dump.getFileName();
                dump.dumpDefault(messageHolder);
                if (dump != null) {
                    dump.close();
                }
            } catch (Throwable th) {
                if (dump != null) {
                    dump.close();
                }
                throw th;
            }
        }
        if (reset) {
            messageHolder.clearMessages();
        }
        if (trace.isTraceEnabled()) {
            trace.exit("dumpOnExit", fileName);
        }
        return fileName;
    }

    private static boolean shouldDumpOnExit(IMessageHolder messageHolder) {
        if (trace.isTraceEnabled()) {
            trace.enter("shouldDumpOnExit", (Object) null, messageHolder);
        }
        if (trace.isTraceEnabled()) {
            trace.event("shouldDumpOnExit", (Object) null, conditionKind);
        }
        boolean result = messageHolder == null || messageHolder.hasAnyMessage(conditionKind, true);
        if (trace.isTraceEnabled()) {
            trace.exit("shouldDumpOnExit", result);
        }
        return result;
    }

    public static void setDumpOnException(boolean b) {
        if (b) {
            exceptionClass = Throwable.class;
        } else {
            exceptionClass = null;
        }
    }

    public static boolean setDumpDirectory(String directoryName) {
        if (trace.isTraceEnabled()) {
            trace.enter("setDumpDirectory", (Object) null, directoryName);
        }
        boolean success = false;
        File newDirectory = new File(directoryName);
        if (newDirectory.exists()) {
            directory = newDirectory;
            success = true;
        }
        if (trace.isTraceEnabled()) {
            trace.exit("setDumpDirectory", success);
        }
        return success;
    }

    public static boolean getDumpOnException() {
        return exceptionClass != null;
    }

    public static boolean setDumpOnExit(IMessage.Kind condition) {
        if (trace.isTraceEnabled()) {
            trace.event("setDumpOnExit", (Object) null, condition);
        }
        conditionKind = condition;
        return true;
    }

    public static boolean setDumpOnExit(String condition) {
        for (IMessage.Kind kind : IMessage.KINDS) {
            if (kind.toString().equals(condition)) {
                return setDumpOnExit(kind);
            }
        }
        return false;
    }

    public static IMessage.Kind getDumpOnExit() {
        return conditionKind;
    }

    public static String getLastDumpFileName() {
        return lastDumpFileName;
    }

    public static void saveCommandLine(String[] args) {
        savedCommandLine = new String[args.length];
        System.arraycopy(args, 0, savedCommandLine, 0, args.length);
    }

    public static void saveFullClasspath(List<String> list) {
        savedFullClasspath = list;
    }

    public static void saveMessageHolder(IMessageHolder holder) {
        savedMessageHolder = holder;
    }

    private Dump(String reason) {
        if (trace.isTraceEnabled()) {
            trace.enter("<init>", this, reason);
        }
        this.reason = reason;
        openDump();
        dumpAspectJProperties();
        dumpDumpConfiguration();
        if (trace.isTraceEnabled()) {
            trace.exit("<init>", this);
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    private void dumpDefault() {
        dumpDefault(savedMessageHolder);
    }

    private void dumpDefault(IMessageHolder holder) {
        dumpSytemProperties();
        dumpCommandLine();
        dumpFullClasspath();
        dumpCompilerMessages(holder);
    }

    private void dumpException(IMessageHolder messageHolder, Throwable th) {
        println("---- Exception Information ---");
        println(th);
        dumpDefault(messageHolder);
    }

    private void dumpAspectJProperties() {
        println("---- AspectJ Properties ---");
        println("AspectJ Compiler 1.8.14 built on Wednesday Mar 6, 2019 at 20:45:28 GMT");
    }

    private void dumpDumpConfiguration() {
        println("---- Dump Properties ---");
        println("Dump file: " + this.fileName);
        println("Dump reason: " + this.reason);
        println("Dump on exception: " + (exceptionClass != null));
        println("Dump at exit condition: " + conditionKind);
    }

    private void dumpFullClasspath() {
        println("---- Full Classpath ---");
        if (savedFullClasspath != null && savedFullClasspath.size() > 0) {
            for (String fileName : savedFullClasspath) {
                File file = new File(fileName);
                println(file);
            }
            return;
        }
        println(NULL_OR_EMPTY);
    }

    private void dumpSytemProperties() {
        println("---- System Properties ---");
        Properties props = System.getProperties();
        println(props);
    }

    private void dumpCommandLine() {
        println("---- Command Line ---");
        println((Object[]) savedCommandLine);
    }

    private void dumpCompilerMessages(IMessageHolder messageHolder) {
        println("---- Compiler Messages ---");
        if (messageHolder != null) {
            for (IMessage message : messageHolder.getUnmodifiableListView()) {
                println(message.toString());
            }
            return;
        }
        println(NULL_OR_EMPTY);
    }

    private void openDump() {
        if (this.print != null) {
            return;
        }
        Date now = new Date();
        this.fileName = "ajcore." + new SimpleDateFormat("yyyyMMdd").format(now) + "." + new SimpleDateFormat("HHmmss.SSS").format(now) + "." + FILENAME_SUFFIX;
        try {
            File file = new File(directory, this.fileName);
            this.print = new PrintStream((OutputStream) new FileOutputStream(file), true);
            trace.info("Dumping to " + file.getAbsolutePath());
        } catch (Exception e) {
            this.print = System.err;
            trace.info("Dumping to stderr");
            this.fileName = UNKNOWN_FILENAME;
        }
        lastDumpFileName = this.fileName;
    }

    public void close() {
        this.print.close();
    }

    private void println(Object obj) {
        this.print.println(obj);
    }

    private void println(Object[] array) {
        if (array == null) {
            println(NULL_OR_EMPTY);
            return;
        }
        for (Object obj : array) {
            this.print.println(obj);
        }
    }

    private void println(Properties props) {
        for (String key : props.keySet()) {
            String value = props.getProperty(key);
            this.print.println(key + SymbolConstants.EQUAL_SYMBOL + value);
        }
    }

    private void println(Throwable th) {
        th.printStackTrace(this.print);
    }

    private void println(File file) {
        this.print.print(file.getAbsolutePath());
        if (!file.exists()) {
            println("(missing)");
        } else if (file.isDirectory()) {
            int count = file.listFiles().length;
            println("(" + count + " entries)");
        } else {
            println("(" + file.length() + " bytes)");
        }
    }

    private void println(List list) {
        if (list == null || list.isEmpty()) {
            println(NULL_OR_EMPTY);
            return;
        }
        for (Object o : list) {
            if (o instanceof Exception) {
                println((Throwable) o);
            } else {
                println(o.toString());
            }
        }
    }

    private static Object formatObj(Object obj) {
        if (obj == null || (obj instanceof String) || (obj instanceof Number) || (obj instanceof Boolean) || (obj instanceof Exception) || (obj instanceof Character) || (obj instanceof Class) || (obj instanceof File) || (obj instanceof StringBuffer) || (obj instanceof URL)) {
            return obj;
        }
        try {
            if (obj instanceof Traceable) {
                Traceable t = (Traceable) obj;
                return t.toTraceString();
            }
            return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
        } catch (Exception e) {
            return obj.getClass().getName() + "@FFFFFFFF";
        }
    }
}
