package lombok.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: lombok-1.16.22.jar:lombok/core/PostCompiler.SCL.lombok */
public final class PostCompiler {
    private static List<PostCompilerTransformation> transformations;

    private PostCompiler() {
    }

    public static byte[] applyTransformations(byte[] original, String fileName, DiagnosticsReceiver diagnostics) {
        if (System.getProperty("lombok.disablePostCompiler", null) != null) {
            return original;
        }
        init(diagnostics);
        byte[] previous = original;
        for (PostCompilerTransformation transformation : transformations) {
            try {
                byte[] next = transformation.applyTransformations(previous, fileName, diagnostics);
                if (next != null) {
                    previous = next;
                }
            } catch (Exception e) {
                diagnostics.addWarning(String.format("Error during the transformation of '%s'; post-compiler '%s' caused an exception: %s", fileName, transformation.getClass().getName(), e));
            }
        }
        return previous;
    }

    private static synchronized void init(DiagnosticsReceiver diagnostics) {
        if (transformations != null) {
            return;
        }
        try {
            transformations = SpiLoadUtil.readAllFromIterator(SpiLoadUtil.findServices(PostCompilerTransformation.class, PostCompilerTransformation.class.getClassLoader()));
        } catch (IOException e) {
            transformations = Collections.emptyList();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter((Writer) sw, true));
            diagnostics.addWarning("Could not load post-compile transformers: " + e.getMessage() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + sw.toString());
        }
    }

    public static OutputStream wrapOutputStream(final OutputStream originalStream, final String fileName, final DiagnosticsReceiver diagnostics) throws IOException {
        return System.getProperty("lombok.disablePostCompiler", null) != null ? originalStream : new ByteArrayOutputStream() { // from class: lombok.core.PostCompiler.1
            @Override // java.io.ByteArrayOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                byte[] original = toByteArray();
                byte[] copy = null;
                try {
                    copy = PostCompiler.applyTransformations(original, fileName, diagnostics);
                } catch (Exception unused) {
                    diagnostics.addWarning(String.format("Error during the transformation of '%s'; no post-compilation has been applied", fileName));
                }
                if (copy == null) {
                    copy = original;
                }
                originalStream.write(copy);
                originalStream.close();
            }
        };
    }
}
