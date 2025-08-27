package org.aspectj.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivilegedActionException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.aspectj.util.FileUtil;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/LangUtil.class */
public class LangUtil {
    public static final String EOL;
    public static final String JRT_FS = "jrt-fs.jar";
    private static double vmVersion;

    static {
        StringWriter buf = new StringWriter();
        PrintWriter writer = new PrintWriter(buf);
        writer.println("");
        String eol = ScriptUtils.FALLBACK_STATEMENT_SEPARATOR;
        try {
            buf.close();
            StringBuffer sb = buf.getBuffer();
            if (sb != null) {
                eol = buf.toString();
            }
        } catch (Throwable th) {
        }
        EOL = eol;
        try {
            String vm = System.getProperty("java.version");
            if (vm == null) {
                vm = System.getProperty("java.runtime.version");
            }
            if (vm == null) {
                vm = System.getProperty("java.vm.version");
            }
            if (vm == null) {
                new RuntimeException("System properties appear damaged, cannot find: java.version/java.runtime.version/java.vm.version").printStackTrace(System.err);
                vmVersion = 1.5d;
            } else if (vm.startsWith("9")) {
                vmVersion = 1.9d;
            } else {
                try {
                    String versionString = vm.substring(0, 3);
                    Double temp = new Double(Double.parseDouble(versionString));
                    vmVersion = temp.doubleValue();
                } catch (Exception e) {
                    vmVersion = 1.5d;
                }
            }
        } catch (Throwable t) {
            new RuntimeException("System properties appear damaged, cannot find: java.version/java.runtime.version/java.vm.version", t).printStackTrace(System.err);
            vmVersion = 1.5d;
        }
    }

    public static boolean is13VMOrGreater() {
        return 1.3d <= vmVersion;
    }

    public static boolean is14VMOrGreater() {
        return 1.4d <= vmVersion;
    }

    public static boolean is15VMOrGreater() {
        return 1.5d <= vmVersion;
    }

    public static boolean is16VMOrGreater() {
        return 1.6d <= vmVersion;
    }

    public static boolean is17VMOrGreater() {
        return 1.7d <= vmVersion;
    }

    public static boolean is18VMOrGreater() {
        return 1.8d <= vmVersion;
    }

    public static boolean is19VMOrGreater() {
        return 1.9d <= vmVersion;
    }

    public static final void throwIaxIfNull(Object o, String name) {
        if (null == o) {
            String message = "null " + (null == name ? "input" : name);
            throw new IllegalArgumentException(message);
        }
    }

    public static final void throwIaxIfNotAssignable(Object[] ra, Class<?> c, String name) {
        throwIaxIfNull(ra, name);
        String label = null == name ? "input" : name;
        for (int i = 0; i < ra.length; i++) {
            if (null == ra[i]) {
                String m = " null " + label + PropertyAccessor.PROPERTY_KEY_PREFIX + i + "]";
                throw new IllegalArgumentException(m);
            }
            if (null != c) {
                Class<?> actualClass = ra[i].getClass();
                if (!c.isAssignableFrom(actualClass)) {
                    String message = label + " not assignable to " + c.getName();
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    public static final void throwIaxIfNotAssignable(Object o, Class<?> c, String name) {
        throwIaxIfNull(o, name);
        if (null != c) {
            Class<?> actualClass = o.getClass();
            if (!c.isAssignableFrom(actualClass)) {
                String message = name + " not assignable to " + c.getName();
                throw new IllegalArgumentException(message);
            }
        }
    }

    public static final void throwIaxIfFalse(boolean test, String message) {
        if (!test) {
            throw new IllegalArgumentException(message);
        }
    }

    public static boolean isEmpty(String s) {
        return null == s || 0 == s.length();
    }

    public static boolean isEmpty(Object[] ra) {
        return null == ra || 0 == ra.length;
    }

    public static boolean isEmpty(byte[] ra) {
        return null == ra || 0 == ra.length;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || 0 == collection.size();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || 0 == map.size();
    }

    public static String[] split(String text) {
        return (String[]) strings(text).toArray(new String[0]);
    }

    public static List<String> commaSplit(String input) {
        return anySplit(input, ",");
    }

    public static String[] splitClasspath(String classpath) {
        if (isEmpty(classpath)) {
            return new String[0];
        }
        StringTokenizer st = new StringTokenizer(classpath, File.pathSeparator);
        ArrayList<String> result = new ArrayList<>(st.countTokens());
        while (st.hasMoreTokens()) {
            String entry = st.nextToken();
            if (!isEmpty(entry)) {
                result.add(entry);
            }
        }
        return (String[]) result.toArray(new String[0]);
    }

    public static boolean getBoolean(String propertyName, boolean defaultValue) {
        if (null != propertyName) {
            try {
                String value = System.getProperty(propertyName);
                if (null != value) {
                    return Boolean.valueOf(value).booleanValue();
                }
            } catch (Throwable th) {
            }
        }
        return defaultValue;
    }

    public static List<String> anySplit(String input, String delim) {
        if (null == input) {
            return Collections.emptyList();
        }
        ArrayList<String> result = new ArrayList<>();
        if (isEmpty(delim) || -1 == input.indexOf(delim)) {
            result.add(input.trim());
        } else {
            StringTokenizer st = new StringTokenizer(input, delim);
            while (st.hasMoreTokens()) {
                result.add(st.nextToken().trim());
            }
        }
        return result;
    }

    public static List<String> strings(String text) {
        if (isEmpty(text)) {
            return Collections.emptyList();
        }
        List<String> strings = new ArrayList<>();
        StringTokenizer tok = new StringTokenizer(text);
        while (tok.hasMoreTokens()) {
            strings.add(tok.nextToken());
        }
        return strings;
    }

    public static <T> List<T> safeList(List<T> list) {
        return null == list ? Collections.emptyList() : Collections.unmodifiableList(list);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.String[], java.lang.String[][]] */
    public static String[][] copyStrings(String[][] in) {
        ?? r0 = new String[in.length];
        for (int i = 0; i < r0.length; i++) {
            r0[i] = new String[in[i].length];
            System.arraycopy(in[i], 0, r0[i], 0, r0[i].length);
        }
        return r0;
    }

    public static String[] extractOptions(String[] args, String[][] options) {
        if (isEmpty(args) || isEmpty(options)) {
            return args;
        }
        BitSet foundSet = new BitSet();
        String[] result = new String[args.length];
        int resultIndex = 0;
        int j = 0;
        while (j < args.length) {
            boolean found = false;
            for (int i = 0; !found && i < options.length; i++) {
                String[] option = options[i];
                throwIaxIfFalse(!isEmpty(option), "options");
                String sought = option[0];
                found = sought.equals(args[j]);
                if (found) {
                    foundSet.set(i);
                    int doMore = option.length - 1;
                    if (0 < doMore) {
                        int MAX = j + doMore;
                        if (MAX >= args.length) {
                            String s = "expecting " + doMore + " args after ";
                            throw new IllegalArgumentException(s + args[j]);
                        }
                        for (int k = 1; k < option.length; k++) {
                            j++;
                            option[k] = args[j];
                        }
                    } else {
                        continue;
                    }
                }
            }
            if (!found) {
                int i2 = resultIndex;
                resultIndex++;
                result[i2] = args[j];
            }
            j++;
        }
        for (int i3 = 0; i3 < options.length; i3++) {
            if (!foundSet.get(i3)) {
                options[i3][0] = null;
            }
        }
        if (resultIndex < args.length) {
            String[] temp = new String[resultIndex];
            System.arraycopy(result, 0, temp, 0, resultIndex);
            args = temp;
        }
        return args;
    }

    public static Object[] safeCopy(Object[] source, Object[] sink) {
        int resultSize;
        Class<?> sinkType = null == sink ? Object.class : sink.getClass().getComponentType();
        int sourceLength = null == source ? 0 : source.length;
        int sinkLength = null == sink ? 0 : sink.length;
        ArrayList<Object> result = null;
        if (0 == sourceLength) {
            resultSize = 0;
        } else {
            result = new ArrayList<>(sourceLength);
            for (int i = 0; i < sourceLength; i++) {
                if (null != source[i] && sinkType.isAssignableFrom(source[i].getClass())) {
                    result.add(source[i]);
                }
            }
            resultSize = result.size();
        }
        if (resultSize != sinkLength) {
            sink = (Object[]) Array.newInstance(sinkType, result.size());
        }
        if (0 < resultSize) {
            sink = result.toArray(sink);
        }
        return sink;
    }

    public static String unqualifiedClassName(Class<?> c) {
        if (null == c) {
            return "null";
        }
        String name = c.getName();
        int loc = name.lastIndexOf(".");
        if (-1 != loc) {
            name = name.substring(1 + loc);
        }
        return name;
    }

    public static String unqualifiedClassName(Object o) {
        return unqualifiedClassName(null == o ? null : o.getClass());
    }

    public static String replace(String in, String sought, String replace) {
        if (isEmpty(in) || isEmpty(sought)) {
            return in;
        }
        StringBuffer result = new StringBuffer();
        int len = sought.length();
        int i = 0;
        while (true) {
            int start = i;
            int loc = in.indexOf(sought, start);
            if (-1 != loc) {
                result.append(in.substring(start, loc));
                if (!isEmpty(replace)) {
                    result.append(replace);
                }
                i = loc + len;
            } else {
                result.append(in.substring(start));
                return result.toString();
            }
        }
    }

    public static String toSizedString(long i, int width) {
        String result = "" + i;
        int size = result.length();
        if (width > size) {
            int padLength = "                                              ".length();
            if (width > padLength) {
                width = padLength;
            }
            int topad = width - size;
            result = "                                              ".substring(0, topad) + result;
        }
        return result;
    }

    public static String renderExceptionShort(Throwable e) {
        if (null == e) {
            return "(Throwable) null";
        }
        return "(" + unqualifiedClassName(e) + ") " + e.getMessage();
    }

    public static String renderException(Throwable t) {
        return renderException(t, true);
    }

    public static String renderException(Throwable t, boolean elide) throws IOException {
        if (null == t) {
            return "null throwable";
        }
        StringBuffer stack = stackToString(unwrapException(t), false);
        if (elide) {
            elideEndingLines(StringChecker.TEST_PACKAGES, stack, 100);
        }
        return stack.toString();
    }

    static void elideEndingLines(StringChecker checker, StringBuffer stack, int maxLines) {
        if (null == checker || null == stack || 0 == stack.length()) {
            return;
        }
        LinkedList<String> lines = new LinkedList<>();
        StringTokenizer st = new StringTokenizer(stack.toString(), "\n\r");
        while (st.hasMoreTokens()) {
            maxLines--;
            if (0 >= maxLines) {
                break;
            } else {
                lines.add(st.nextToken());
            }
        }
        int elided = 0;
        while (!lines.isEmpty()) {
            String line = lines.getLast();
            if (!checker.acceptString(line)) {
                break;
            }
            elided++;
            lines.removeLast();
        }
        if (elided > 0 || maxLines < 1) {
            int EOL_LEN = EOL.length();
            int totalLength = 0;
            while (!lines.isEmpty()) {
                totalLength += EOL_LEN + lines.getFirst().length();
                lines.removeFirst();
            }
            if (stack.length() > totalLength) {
                stack.setLength(totalLength);
                if (elided > 0) {
                    stack.append("    (... " + elided + " lines...)");
                }
            }
        }
    }

    public static StringBuffer stackToString(Throwable throwable, boolean skipMessage) throws IOException {
        if (null == throwable) {
            return new StringBuffer();
        }
        StringWriter buf = new StringWriter();
        PrintWriter writer = new PrintWriter(buf);
        if (!skipMessage) {
            writer.println(throwable.getMessage());
        }
        throwable.printStackTrace(writer);
        try {
            buf.close();
        } catch (IOException e) {
        }
        return buf.getBuffer();
    }

    public static Throwable unwrapException(Throwable t) {
        Throwable current;
        Throwable th = t;
        while (true) {
            current = th;
            Throwable next = null;
            if (current == null) {
                break;
            }
            if (current instanceof InvocationTargetException) {
                next = ((InvocationTargetException) current).getTargetException();
            } else if (current instanceof ClassNotFoundException) {
                next = ((ClassNotFoundException) current).getException();
            } else if (current instanceof ExceptionInInitializerError) {
                next = ((ExceptionInInitializerError) current).getException();
            } else if (current instanceof PrivilegedActionException) {
                next = ((PrivilegedActionException) current).getException();
            } else if (current instanceof SQLException) {
                next = ((SQLException) current).getNextException();
            }
            if (null == next) {
                break;
            }
            th = next;
        }
        return current;
    }

    public static <T> List<T> arrayAsList(T[] array) {
        if (null == array || 1 > array.length) {
            return Collections.emptyList();
        }
        ArrayList<T> list = new ArrayList<>();
        list.addAll(Arrays.asList(array));
        return list;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/LangUtil$StringChecker.class */
    public static class StringChecker {
        static StringChecker TEST_PACKAGES = new StringChecker(new String[]{"org.aspectj.testing", "org.eclipse.jdt.internal.junit", "junit.framework.", "org.apache.tools.ant.taskdefs.optional.junit.JUnitTestRunner"});
        String[] infixes;

        StringChecker(String[] infixes) {
            this.infixes = infixes;
        }

        public boolean acceptString(String input) {
            boolean result = false;
            if (!LangUtil.isEmpty(input)) {
                for (int i = 0; !result && i < this.infixes.length; i++) {
                    result = -1 != input.indexOf(this.infixes[i]);
                }
            }
            return result;
        }
    }

    public static String makeClasspath(String bootclasspath, String classpath, String classesDir, String outputJar) {
        StringBuffer sb = new StringBuffer();
        addIfNotEmpty(bootclasspath, sb, File.pathSeparator);
        addIfNotEmpty(classpath, sb, File.pathSeparator);
        if (!addIfNotEmpty(classesDir, sb, File.pathSeparator)) {
            addIfNotEmpty(outputJar, sb, File.pathSeparator);
        }
        return sb.toString();
    }

    private static boolean addIfNotEmpty(String input, StringBuffer sink, String delimiter) {
        if (isEmpty(input) || null == sink) {
            return false;
        }
        sink.append(input);
        if (!isEmpty(delimiter)) {
            sink.append(delimiter);
            return true;
        }
        return true;
    }

    public static ProcessController makeProcess(ProcessController controller, String classpath, String mainClass, String[] args) {
        File java = getJavaExecutable();
        ArrayList<String> cmd = new ArrayList<>();
        cmd.add(java.getAbsolutePath());
        cmd.add("-classpath");
        cmd.add(classpath);
        cmd.add(mainClass);
        if (!isEmpty(args)) {
            cmd.addAll(Arrays.asList(args));
        }
        String[] command = (String[]) cmd.toArray(new String[0]);
        if (null == controller) {
            controller = new ProcessController();
        }
        controller.init(command, mainClass);
        return controller;
    }

    public static File getJavaExecutable() {
        String javaHome = null;
        File result = null;
        try {
            javaHome = System.getProperty("java.home");
        } catch (Throwable th) {
        }
        if (null != javaHome) {
            File binDir = new File(javaHome, "bin");
            if (binDir.isDirectory() && binDir.canRead()) {
                String[] execs = {"java", "java.exe"};
                for (String str : execs) {
                    result = new File(binDir, str);
                    if (result.canRead()) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static boolean sleepUntil(long time) throws InterruptedException {
        if (time == 0) {
            return true;
        }
        if (time < 0) {
            throw new IllegalArgumentException("negative: " + time);
        }
        long curTime = System.currentTimeMillis();
        for (int i = 0; i < 100 && curTime < time; i++) {
            try {
                Thread.sleep(time - curTime);
            } catch (InterruptedException e) {
            }
            curTime = System.currentTimeMillis();
        }
        return curTime >= time;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/LangUtil$ProcessController.class */
    public static class ProcessController {
        private String[] command;
        private String[] envp;
        private String label;
        private boolean init;
        private boolean started;
        private boolean completed;
        private boolean userStopped;
        private Process process;
        private FileUtil.Pipe errStream;
        private FileUtil.Pipe outStream;
        private FileUtil.Pipe inStream;
        private ByteArrayOutputStream errSnoop;
        private ByteArrayOutputStream outSnoop;
        private int result;
        private Thrown thrown;

        public final void reinit() {
            if (!this.init) {
                throw new IllegalStateException("must init(..) before reinit()");
            }
            if (this.started && !this.completed) {
                throw new IllegalStateException("not completed - do stop()");
            }
            this.started = false;
            this.completed = false;
            this.result = Integer.MIN_VALUE;
            this.thrown = null;
            this.process = null;
            this.errStream = null;
            this.outStream = null;
            this.inStream = null;
        }

        public final void init(String classpath, String mainClass, String[] args) {
            init(LangUtil.getJavaExecutable(), classpath, mainClass, args);
        }

        public final void init(File java, String classpath, String mainClass, String[] args) {
            LangUtil.throwIaxIfNull(java, "java");
            LangUtil.throwIaxIfNull(mainClass, "mainClass");
            LangUtil.throwIaxIfNull(args, "args");
            ArrayList<String> cmd = new ArrayList<>();
            cmd.add(java.getAbsolutePath());
            cmd.add("-classpath");
            cmd.add(classpath);
            cmd.add(mainClass);
            if (!LangUtil.isEmpty(args)) {
                cmd.addAll(Arrays.asList(args));
            }
            init((String[]) cmd.toArray(new String[0]), mainClass);
        }

        public final void init(String[] command, String label) {
            this.command = (String[]) LangUtil.safeCopy(command, new String[0]);
            if (1 > this.command.length) {
                throw new IllegalArgumentException("empty command");
            }
            this.label = LangUtil.isEmpty(label) ? command[0] : label;
            this.init = true;
            reinit();
        }

        public final void setEnvp(String[] envp) {
            this.envp = (String[]) LangUtil.safeCopy(envp, new String[0]);
            if (1 > this.envp.length) {
                throw new IllegalArgumentException("empty envp");
            }
        }

        public final void setErrSnoop(ByteArrayOutputStream snoop) {
            this.errSnoop = snoop;
            if (null != this.errStream) {
                this.errStream.setSnoop(this.errSnoop);
            }
        }

        public final void setOutSnoop(ByteArrayOutputStream snoop) {
            this.outSnoop = snoop;
            if (null != this.outStream) {
                this.outStream.setSnoop(this.outSnoop);
            }
        }

        public final Thread start() throws InterruptedException {
            if (!this.init) {
                throw new IllegalStateException("not initialized");
            }
            synchronized (this) {
                if (this.started) {
                    throw new IllegalStateException("already started");
                }
                this.started = true;
            }
            try {
                this.process = Runtime.getRuntime().exec(this.command);
                this.errStream = new FileUtil.Pipe(this.process.getErrorStream(), System.err);
                if (null != this.errSnoop) {
                    this.errStream.setSnoop(this.errSnoop);
                }
                this.outStream = new FileUtil.Pipe(this.process.getInputStream(), System.out);
                if (null != this.outSnoop) {
                    this.outStream.setSnoop(this.outSnoop);
                }
                this.inStream = new FileUtil.Pipe(System.in, this.process.getOutputStream());
                Runnable processRunner = new Runnable() { // from class: org.aspectj.util.LangUtil.ProcessController.1
                    @Override // java.lang.Runnable
                    public void run() throws InterruptedException {
                        int result = Integer.MIN_VALUE;
                        try {
                            new Thread(ProcessController.this.errStream).start();
                            new Thread(ProcessController.this.outStream).start();
                            new Thread(ProcessController.this.inStream).start();
                            ProcessController.this.process.waitFor();
                            result = ProcessController.this.process.exitValue();
                            ProcessController.this.stop(null, result);
                        } catch (Throwable e) {
                            ProcessController.this.stop(e, result);
                        }
                    }
                };
                Thread result = new Thread(processRunner, this.label);
                result.start();
                return result;
            } catch (IOException e) {
                stop(e, Integer.MIN_VALUE);
                return null;
            }
        }

        public final synchronized void stop() throws InterruptedException {
            if (this.completed) {
                return;
            }
            this.userStopped = true;
            stop(null, Integer.MIN_VALUE);
        }

        public final String[] getCommand() {
            String[] toCopy = this.command;
            if (LangUtil.isEmpty(toCopy)) {
                return new String[0];
            }
            String[] result = new String[toCopy.length];
            System.arraycopy(toCopy, 0, result, 0, result.length);
            return result;
        }

        public final boolean completed() {
            return this.completed;
        }

        public final boolean started() {
            return this.started;
        }

        public final boolean userStopped() {
            return this.userStopped;
        }

        public final Thrown getThrown() {
            return makeThrown(null);
        }

        public final int getResult() {
            return this.result;
        }

        protected void doCompleting(Thrown thrown, int result) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final synchronized void stop(Throwable thrown, int result) throws InterruptedException {
            if (this.completed) {
                throw new IllegalStateException("already completed");
            }
            if (null != this.thrown) {
                throw new IllegalStateException("already set thrown: " + thrown);
            }
            this.thrown = makeThrown(thrown);
            if (null != this.process) {
                this.process.destroy();
            }
            if (null != this.inStream) {
                this.inStream.halt(false, true);
                this.inStream = null;
            }
            if (null != this.outStream) {
                this.outStream.halt(true, true);
                this.outStream = null;
            }
            if (null != this.errStream) {
                this.errStream.halt(true, true);
                this.errStream = null;
            }
            if (Integer.MIN_VALUE != result) {
                this.result = result;
            }
            this.completed = true;
            doCompleting(this.thrown, result);
        }

        private final synchronized Thrown makeThrown(Throwable processThrown) {
            if (null != this.thrown) {
                return this.thrown;
            }
            return new Thrown(processThrown, null == this.outStream ? null : this.outStream.getThrown(), null == this.errStream ? null : this.errStream.getThrown(), null == this.inStream ? null : this.inStream.getThrown());
        }

        /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/LangUtil$ProcessController$Thrown.class */
        public static class Thrown {
            public final Throwable fromProcess;
            public final Throwable fromErrPipe;
            public final Throwable fromOutPipe;
            public final Throwable fromInPipe;
            public final boolean thrown;

            private Thrown(Throwable fromProcess, Throwable fromOutPipe, Throwable fromErrPipe, Throwable fromInPipe) {
                this.fromProcess = fromProcess;
                this.fromErrPipe = fromErrPipe;
                this.fromOutPipe = fromOutPipe;
                this.fromInPipe = fromInPipe;
                this.thrown = (null == fromProcess && null == fromInPipe && null == fromOutPipe && null == fromErrPipe) ? false : true;
            }

            public String toString() {
                StringBuffer sb = new StringBuffer();
                append(sb, this.fromProcess, "process");
                append(sb, this.fromOutPipe, " stdout");
                append(sb, this.fromErrPipe, " stderr");
                append(sb, this.fromInPipe, "  stdin");
                if (0 == sb.length()) {
                    return "Thrown (none)";
                }
                return sb.toString();
            }

            private void append(StringBuffer sb, Throwable thrown, String label) {
                if (null != thrown) {
                    sb.append("from " + label + ": ");
                    sb.append(LangUtil.renderExceptionShort(thrown));
                    sb.append(LangUtil.EOL);
                }
            }
        }
    }

    public static String getJrtFsFilePath() {
        return getJavaHome() + File.separator + "lib" + File.separator + JRT_FS;
    }

    public static String getJavaHome() {
        return System.getProperty("java.home");
    }
}
