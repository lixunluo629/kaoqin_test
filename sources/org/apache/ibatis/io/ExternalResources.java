package org.apache.ibatis.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Properties;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

@Deprecated
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/ExternalResources.class */
public class ExternalResources {
    private static final Log log = LogFactory.getLog((Class<?>) ExternalResources.class);

    private ExternalResources() {
    }

    public static void copyExternalResource(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0L, source.size());
            closeQuietly(source);
            closeQuietly(destination);
        } catch (Throwable th) {
            closeQuietly(source);
            closeQuietly(destination);
            throw th;
        }
    }

    private static void closeQuietly(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static String getConfiguredTemplate(String templatePath, String templateProperty) throws IOException {
        String templateName = "";
        Properties migrationProperties = new Properties();
        try {
            migrationProperties.load(new FileInputStream(templatePath));
            templateName = migrationProperties.getProperty(templateProperty);
        } catch (FileNotFoundException e) {
            throw e;
        } catch (Exception e2) {
            log.error("", e2);
        }
        return templateName;
    }
}
