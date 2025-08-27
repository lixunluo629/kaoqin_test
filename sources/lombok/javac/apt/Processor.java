package lombok.javac.apt;

import com.itextpdf.layout.element.List;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.sun.tools.javac.processing.JavacFiler;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Options;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@Deprecated
@SupportedAnnotationTypes({"*"})
/* loaded from: lombok-1.16.22.jar:lombok/javac/apt/Processor.class */
public class Processor extends AbstractProcessor {
    public void init(ProcessingEnvironment procEnv) {
        super.init(procEnv);
        if (System.getProperty("lombok.disable") != null) {
            return;
        }
        procEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Wrong usage of 'lombok.javac.apt.Processor'. " + report(procEnv));
    }

    private String report(ProcessingEnvironment procEnv) throws NoSuchFieldException, IOException {
        String data = collectData(procEnv);
        try {
            return writeFile(data);
        } catch (Exception e) {
            return "Report:\n\n" + data;
        }
    }

    private String writeFile(String data) throws IOException {
        File file = File.createTempFile("lombok-processor-report-", ".txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write(data);
        writer.close();
        return "Report written to '" + file.getCanonicalPath() + "'\n";
    }

    private String collectData(ProcessingEnvironment procEnv) throws NoSuchFieldException, IOException {
        StringBuilder message = new StringBuilder();
        message.append("Problem report for usages of 'lombok.javac.apt.Processor'\n\n");
        listOptions(message, procEnv);
        findServices(message, procEnv.getFiler());
        addStacktrace(message);
        listProperties(message);
        return message.toString();
    }

    private void listOptions(StringBuilder message, ProcessingEnvironment procEnv) throws NoSuchFieldException {
        try {
            JavacProcessingEnvironment environment = (JavacProcessingEnvironment) procEnv;
            Options instance = Options.instance(environment.getContext());
            Field field = Options.class.getDeclaredField("values");
            field.setAccessible(true);
            Map<String, String> values = (Map) field.get(instance);
            if (values.isEmpty()) {
                message.append("Options: empty\n\n");
                return;
            }
            message.append("Compiler Options:\n");
            for (Map.Entry<String, String> value : values.entrySet()) {
                message.append(List.DEFAULT_LIST_SYMBOL);
                string(message, value.getKey());
                message.append(" = ");
                string(message, value.getValue());
                message.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
            message.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        } catch (Exception e) {
            message.append("No options available\n\n");
        }
    }

    private void findServices(StringBuilder message, Filer filer) throws NoSuchFieldException, IOException {
        Enumeration<URL> resources;
        try {
            Field filerFileManagerField = JavacFiler.class.getDeclaredField("fileManager");
            filerFileManagerField.setAccessible(true);
            JavaFileManager jfm = (JavaFileManager) filerFileManagerField.get(filer);
            ClassLoader processorClassLoader = jfm.hasLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH) ? jfm.getClassLoader(StandardLocation.ANNOTATION_PROCESSOR_PATH) : jfm.getClassLoader(StandardLocation.CLASS_PATH);
            resources = processorClassLoader.getResources("META-INF/services/javax.annotation.processing.Processor");
        } catch (Exception e) {
            message.append("Filer information unavailable\n");
        }
        if (!resources.hasMoreElements()) {
            message.append("No processors discovered\n\n");
            return;
        }
        message.append("Discovered processors:\n");
        while (resources.hasMoreElements()) {
            URL processorUrl = resources.nextElement();
            message.append("- '").append(processorUrl).append("'");
            InputStream content = (InputStream) processorUrl.getContent();
            if (content != null) {
                try {
                    InputStreamReader reader = new InputStreamReader(content, "UTF-8");
                    StringWriter sw = new StringWriter();
                    char[] buffer = new char[8192];
                    while (true) {
                        int read = reader.read(buffer);
                        if (read == -1) {
                            break;
                        } else {
                            sw.write(buffer, 0, read);
                        }
                    }
                    String wholeFile = sw.toString();
                    if (wholeFile.contains("lombok.javac.apt.Processor")) {
                        message.append(" <= problem\n");
                    } else {
                        message.append(" (ok)\n");
                    }
                    message.append("    ").append(wholeFile.replace(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "\n    ")).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                    content.close();
                } catch (Throwable th) {
                    content.close();
                    throw th;
                }
            }
        }
        message.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
    }

    private void addStacktrace(StringBuilder message) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements != null) {
            message.append("Called from\n");
            for (int i = 1; i < stackTraceElements.length; i++) {
                StackTraceElement element = stackTraceElements[i];
                if (!element.getClassName().equals("lombok.javac.apt.Processor")) {
                    message.append(List.DEFAULT_LIST_SYMBOL).append(element).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                }
            }
        } else {
            message.append("No stacktrace available\n");
        }
        message.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
    }

    private void listProperties(StringBuilder message) {
        Properties properties = System.getProperties();
        ArrayList<String> propertyNames = new ArrayList<>(properties.stringPropertyNames());
        Collections.sort(propertyNames);
        message.append("Properties: \n");
        Iterator<String> it = propertyNames.iterator();
        while (it.hasNext()) {
            String propertyName = it.next();
            if (!propertyName.startsWith("user.")) {
                message.append(List.DEFAULT_LIST_SYMBOL).append(propertyName).append(" = ");
                string(message, System.getProperty(propertyName));
                message.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
        }
        message.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
    }

    private static void string(StringBuilder sb, String s) {
        if (s == null) {
            sb.append("null");
            return;
        }
        sb.append(SymbolConstants.QUOTES_SYMBOL);
        for (int i = 0; i < s.length(); i++) {
            sb.append(escape(s.charAt(i)));
        }
        sb.append(SymbolConstants.QUOTES_SYMBOL);
    }

    private static String escape(char ch2) {
        switch (ch2) {
            case '\b':
                return "\\b";
            case '\t':
                return "\\t";
            case '\n':
                return "\\n";
            case '\f':
                return "\\f";
            case '\r':
                return "\\r";
            case '\"':
                return "\\\"";
            case '\'':
                return "\\'";
            case '\\':
                return "\\\\";
            default:
                return ch2 < ' ' ? String.format("\\%03o", Integer.valueOf(ch2)) : String.valueOf(ch2);
        }
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.values()[SourceVersion.values().length - 1];
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}
