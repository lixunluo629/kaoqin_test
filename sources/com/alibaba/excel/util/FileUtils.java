package com.alibaba.excel.util;

import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelCommonException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import org.apache.poi.util.DefaultTempFileCreationStrategy;
import org.apache.poi.util.TempFile;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/FileUtils.class */
public class FileUtils {
    private static final int WRITE_BUFF_SIZE = 8192;
    private static String tempFilePrefix = System.getProperty(TempFile.JAVA_IO_TMPDIR) + File.separator + UUID.randomUUID().toString() + File.separator;
    public static final String POI_FILES = "poifiles";
    private static String poiFilesPath = tempFilePrefix + POI_FILES + File.separator;
    public static final String EX_CACHE = "excache";
    private static String cachePath = tempFilePrefix + EX_CACHE + File.separator;

    static {
        File tempFile = new File(tempFilePrefix);
        createDirectory(tempFile);
        tempFile.deleteOnExit();
        File cacheFile = new File(cachePath);
        createDirectory(cacheFile);
        cacheFile.deleteOnExit();
    }

    private FileUtils() {
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        InputStream in = openInputStream(file);
        try {
            long fileLength = file.length();
            return fileLength > 0 ? IoUtils.toByteArray(in, (int) fileLength) : IoUtils.toByteArray(in);
        } finally {
            in.close();
        }
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
            return new FileInputStream(file);
        }
        throw new FileNotFoundException("File '" + file + "' does not exist");
    }

    public static void writeToFile(File file, InputStream inputStream) throws IOException {
        OutputStream outputStream = null;
        try {
            try {
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[8192];
                while (true) {
                    int bytesRead = inputStream.read(buffer, 0, 8192);
                    if (bytesRead == -1) {
                        break;
                    } else {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        throw new ExcelAnalysisException("Can not close 'outputStream'!", e);
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                        throw new ExcelAnalysisException("Can not close 'inputStream'", e2);
                    }
                }
            } catch (Throwable th) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e3) {
                        throw new ExcelAnalysisException("Can not close 'outputStream'!", e3);
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e4) {
                        throw new ExcelAnalysisException("Can not close 'inputStream'", e4);
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            throw new ExcelAnalysisException("Can not create temporary file!", e5);
        }
    }

    public static void createPoiFilesDirectory() {
        File poiFilesPathFile = new File(poiFilesPath);
        createDirectory(poiFilesPathFile);
        TempFile.setTempFileCreationStrategy(new DefaultTempFileCreationStrategy(poiFilesPathFile));
        poiFilesPathFile.deleteOnExit();
    }

    public static File createCacheTmpFile() {
        return createDirectory(new File(cachePath + UUID.randomUUID().toString()));
    }

    public static File createTmpFile(String fileName) {
        File directory = createDirectory(new File(tempFilePrefix));
        return new File(directory, fileName);
    }

    private static File createDirectory(File directory) {
        if (!directory.exists() && !directory.mkdirs()) {
            throw new ExcelCommonException("Cannot create directory:" + directory.getAbsolutePath());
        }
        return directory;
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (File file2 : childFiles) {
                delete(file2);
            }
            file.delete();
        }
    }

    public static String getTempFilePrefix() {
        return tempFilePrefix;
    }

    public static void setTempFilePrefix(String tempFilePrefix2) {
        tempFilePrefix = tempFilePrefix2;
    }

    public static String getPoiFilesPath() {
        return poiFilesPath;
    }

    public static void setPoiFilesPath(String poiFilesPath2) {
        poiFilesPath = poiFilesPath2;
    }

    public static String getCachePath() {
        return cachePath;
    }

    public static void setCachePath(String cachePath2) {
        cachePath = cachePath2;
    }
}
