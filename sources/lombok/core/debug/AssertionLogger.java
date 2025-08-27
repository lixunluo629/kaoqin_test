package lombok.core.debug;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.core.Version;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: lombok-1.16.22.jar:lombok/core/debug/AssertionLogger.SCL.lombok */
public class AssertionLogger {
    private static final String LOG_PATH;
    private static final AtomicBoolean loggedIntro;
    private static final String PROCESS_ID;
    private static final String ID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    static {
        String log;
        String log2 = System.getProperty("lombok.assertion.log", null);
        if (log2 != null) {
            LOG_PATH = log2.isEmpty() ? null : log2;
        } else {
            try {
                log = System.getenv("LOMBOK_ASSERTION_LOG");
            } catch (Exception unused) {
                log = null;
            }
            LOG_PATH = (log == null || log.isEmpty()) ? null : log;
        }
        loggedIntro = new AtomicBoolean(false);
        PROCESS_ID = generateProcessId();
    }

    private static String generateProcessId() {
        char[] ID = new char[4];
        Random r = new Random();
        for (int i = 0; i < ID.length; i++) {
            ID[i] = ID_CHARS.charAt(r.nextInt(ID_CHARS.length()));
        }
        return new String(ID);
    }

    private static synchronized void logToFile(String msg) throws IOException {
        if (msg == null) {
            return;
        }
        try {
            OutputStream out = new FileOutputStream(LOG_PATH, true);
            out.write(msg.getBytes("UTF-8"));
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("assertion logging can't write to log file", e);
        }
    }

    private static void logIntro() throws IOException {
        String version;
        if (loggedIntro.getAndSet(true)) {
            return;
        }
        try {
            version = Version.getFullVersion();
        } catch (Exception unused) {
            version = Version.getVersion();
        }
        logToFile(String.format("{%s} [%s -- START %s]\n", PROCESS_ID, new Date(), version));
    }

    public static <T extends Throwable> T assertLog(String message, T throwable) throws IOException {
        if (LOG_PATH == null) {
            return throwable;
        }
        logIntro();
        if (message == null) {
            message = "(No message)";
        }
        String stackMsg = "";
        if (throwable != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            pw.close();
            stackMsg = "\n  " + sw.toString().replace("\r", "").replace(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "\n  ").trim();
        }
        logToFile(String.format("{%s} [%ty%<tm%<tdT%<tH%<tM%<tS.%<tL] %s%s\n", PROCESS_ID, new Date(), message, stackMsg));
        return throwable;
    }

    public static void assertLog(String message) throws IOException {
        if (LOG_PATH == null) {
            return;
        }
        assertLog(message, null);
    }
}
