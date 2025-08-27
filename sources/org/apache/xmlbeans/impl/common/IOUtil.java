package org.apache.xmlbeans.impl.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.channels.FileChannel;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/IOUtil.class */
public class IOUtil {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !IOUtil.class.desiredAssertionStatus();
    }

    public static void copyCompletely(InputStream input, OutputStream output) throws IOException {
        if ((output instanceof FileOutputStream) && (input instanceof FileInputStream)) {
            try {
                FileChannel target = ((FileOutputStream) output).getChannel();
                FileChannel source = ((FileInputStream) input).getChannel();
                source.transferTo(0L, 2147483647L, target);
                source.close();
                target.close();
                return;
            } catch (Exception e) {
            }
        }
        byte[] buf = new byte[8192];
        while (true) {
            int length = input.read(buf);
            if (length < 0) {
                try {
                    break;
                } catch (IOException e2) {
                }
            } else {
                output.write(buf, 0, length);
            }
        }
        input.close();
        try {
            output.close();
        } catch (IOException e3) {
        }
    }

    public static void copyCompletely(Reader input, Writer output) throws IOException {
        char[] buf = new char[8192];
        while (true) {
            int length = input.read(buf);
            if (length < 0) {
                try {
                    break;
                } catch (IOException e) {
                }
            } else {
                output.write(buf, 0, length);
            }
        }
        input.close();
        try {
            output.close();
        } catch (IOException e2) {
        }
    }

    public static void copyCompletely(URI input, URI output) throws IOException {
        InputStream in = null;
        try {
            try {
                File f = new File(input);
                if (f.exists()) {
                    in = new FileInputStream(f);
                }
            } catch (IllegalArgumentException e) {
                throw new IOException("Cannot copy to " + output);
            }
        } catch (Exception e2) {
        }
        File out = new File(output);
        File dir = out.getParentFile();
        dir.mkdirs();
        if (in == null) {
            in = input.toURL().openStream();
        }
        copyCompletely(in, new FileOutputStream(out));
    }

    public static File createDir(File rootdir, String subdir) {
        File newdir = subdir == null ? rootdir : new File(rootdir, subdir);
        boolean created = (newdir.exists() && newdir.isDirectory()) || newdir.mkdirs();
        if ($assertionsDisabled || created) {
            return newdir;
        }
        throw new AssertionError("Could not create " + newdir.getAbsolutePath());
    }
}
