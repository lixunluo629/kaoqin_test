package com.moredian.onpremise.core.utils;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.model.info.UploadAndUnpackInfo;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/MyFileUtils.class */
public class MyFileUtils {
    private static final int MAX_SIZE = 262144;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MyFileUtils.class);

    public static String upload(MultipartFile file, String filePath) {
        String fileName;
        try {
            if (file.isEmpty()) {
                return null;
            }
            synchronized (MyFileUtils.class) {
                fileName = DateFormatUtils.format(new Date(), "yyyyMMdd") + System.currentTimeMillis() + random();
            }
            String originalFilename = file.getOriginalFilename();
            logger.info("originalFilename :{} , fileName :{}", originalFilename, fileName);
            String path = filePath + fileName + "-" + originalFilename;
            File dest = new File(new File(path).getAbsolutePath());
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return fileName + "-" + originalFilename;
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static String upload(MultipartFile file, String filePath, String fileName) {
        try {
            if (file.isEmpty()) {
                return null;
            }
            String path = filePath + fileName;
            File dest = new File(new File(path).getAbsolutePath());
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return path;
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static String upload(String base64, String filePath) throws IOException {
        String fileName;
        try {
            synchronized (MyFileUtils.class) {
                fileName = DateFormatUtils.format(new Date(), "yyyyMMdd") + System.currentTimeMillis() + random();
            }
            String fileName__mirror = fileName + "_mirror";
            byte[] bytes = str2Image(base64);
            File dest = new File(filePath, fileName + ".jpg");
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            FileOutputStream imgOut = new FileOutputStream(dest);
            imgOut.write(bytes);
            imgOut.close();
            String path = filePath + fileName + ".jpg";
            String path_mirror = filePath + fileName__mirror + ".jpg";
            ImageMirrorUtils.rotateImg(path, path_mirror);
            deleteFile(path);
            return fileName__mirror + ".jpg";
        } catch (IOException e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static String uploadImageForReturnPath(String base64, String filePath) throws IOException {
        String fileName;
        try {
            synchronized (MyFileUtils.class) {
                fileName = DateFormatUtils.format(new Date(), "yyyyMMdd") + System.currentTimeMillis() + random();
            }
            byte[] bytes = str2Image(base64);
            File dest = new File(filePath, fileName + ".jpg");
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            FileOutputStream imgOut = new FileOutputStream(dest);
            imgOut.write(bytes);
            imgOut.close();
            return filePath + fileName + ".jpg";
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static String uploadFileForReturnPath(MultipartFile file, String filePath) {
        String fileName;
        try {
            if (file.isEmpty()) {
                return null;
            }
            synchronized (MyFileUtils.class) {
                fileName = DateFormatUtils.format(new Date(), "yyyyMMdd") + System.currentTimeMillis() + random();
            }
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.indexOf(".") + 1, originalFilename.length());
            logger.info("originalFilename :{} , fileName :{}", originalFilename, fileName);
            String path = filePath + fileName + "." + fileExtension;
            File dest = new File(new File(path).getAbsolutePath());
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return path;
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static String uploadAndPack(MultipartFile file, String filePath, String orgName) throws IOException, RuntimeException {
        String fileName;
        if (orgName == null || orgName.trim().length() == 0) {
            fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + System.currentTimeMillis();
        } else {
            fileName = orgName + "_" + DateFormatUtils.format(new Date(), "yyyyMMdd");
        }
        String path = upload(file, filePath, fileName + ".sql");
        fileToZip(path, filePath + fileName + ".zip");
        return null;
    }

    public static UploadAndUnpackInfo uploadAndUnpack(MultipartFile file, String filePath, String orgName) throws IOException {
        String fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + System.currentTimeMillis();
        String path = upload(file, filePath, fileName + ".zip");
        UploadAndUnpackInfo result = null;
        if (path != null) {
            logger.info("==zipPath :{},filePath:{}", path, filePath);
            result = zipToFile(path, filePath + fileName + ".sql");
            result.setRecordName(fileName + ".zip");
        }
        return result;
    }

    public static UploadAndUnpackInfo zipToFile(String zipPath, String filePath) throws IOException {
        int index;
        long startTime = System.currentTimeMillis();
        UploadAndUnpackInfo result = new UploadAndUnpackInfo();
        try {
            ZipFile zf = new ZipFile(zipPath);
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath));
            BufferedInputStream bin = new BufferedInputStream(zin);
            while (true) {
                ZipEntry entry = zin.getNextEntry();
                if (entry == null || entry.isDirectory()) {
                    break;
                }
                File file = new File(filePath);
                logger.info("file name :{},file path :{}", file.getName(), file.getPath());
                if (!file.exists()) {
                    new File(file.getParent()).mkdirs();
                }
                FileOutputStream out = new FileOutputStream(file);
                BufferedOutputStream bout = new BufferedOutputStream(out);
                while (true) {
                    int b = bin.read();
                    if (b != -1) {
                        bout.write(b);
                    }
                }
                bout.close();
                out.close();
                logger.info(file.getName() + "解压成功");
            }
            String comment = zf.getComment();
            String[] comments = comment.split("\r\n");
            for (String line : comments) {
                if (line.trim().length() != 0 && !line.startsWith("#") && (index = line.indexOf(SymbolConstants.EQUAL_SYMBOL)) != -1) {
                    Field[] fields = result.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if (field.getName().equals(line.substring(0, index))) {
                            setValue(result, field.getName(), field.getType(), line.substring(index + 1));
                        }
                    }
                }
            }
            bin.close();
            zin.close();
        } catch (Exception e) {
            result = null;
        }
        logger.info("耗费时间： " + (System.currentTimeMillis() - startTime) + " ms");
        return result;
    }

    public static void fileToZip(String filePath, String zipPath) throws IOException, RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            try {
                File srcFile = new File(filePath);
                File targetFile = new File(zipPath);
                zos = new ZipOutputStream(new FileOutputStream(targetFile));
                byte[] buf = new byte[4096];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                zos.setComment(setComment(1));
                FileInputStream in = new FileInputStream(srcFile);
                while (true) {
                    int len = in.read(buf);
                    if (len == -1) {
                        break;
                    } else {
                        zos.write(buf, 0, len);
                    }
                }
                zos.closeEntry();
                in.close();
                long end = System.currentTimeMillis();
                logger.info("压缩完成，耗时：" + (end - start) + " ms");
                if (zos != null) {
                    try {
                        zos.close();
                    } catch (IOException e) {
                        logger.error("error:{}", (Throwable) e);
                    }
                }
            } catch (Exception e2) {
                throw new RuntimeException("zip error from ZipUtils", e2);
            }
        } catch (Throwable th) {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e3) {
                    logger.error("error:{}", (Throwable) e3);
                }
            }
            throw th;
        }
    }

    public static String fileListToZip(List<String> filePathList, String zipPath, Integer level) throws IOException, RuntimeException {
        String zipName;
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        synchronized (MyFileUtils.class) {
            zipName = DateFormatUtils.format(new Date(), "yyyyMMdd") + System.currentTimeMillis() + random() + ".zip";
        }
        try {
            try {
                File targetFile = new File(zipPath + zipName);
                zos = new ZipOutputStream(new FileOutputStream(targetFile));
                for (String filePath : filePathList) {
                    File srcFile = new File(filePath);
                    byte[] buf = new byte[4096];
                    zos.putNextEntry(new ZipEntry(srcFile.getName()));
                    zos.setComment(setComment(1));
                    zos.setLevel(level.intValue());
                    FileInputStream in = new FileInputStream(srcFile);
                    while (true) {
                        int len = in.read(buf);
                        if (len != -1) {
                            zos.write(buf, 0, len);
                        }
                    }
                    in.close();
                }
                zos.closeEntry();
                long end = System.currentTimeMillis();
                logger.info("压缩完成，耗时：" + (end - start) + " ms");
                String str = zipPath + zipName;
                if (zos != null) {
                    try {
                        zos.close();
                    } catch (IOException e) {
                        logger.error("error:{}", (Throwable) e);
                    }
                }
                return str;
            } catch (Exception e2) {
                throw new RuntimeException("zip error from ZipUtils", e2);
            }
        } catch (Throwable th) {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e3) {
                    logger.error("error:{}", (Throwable) e3);
                }
            }
            throw th;
        }
    }

    public static File[] getCompareFileFilterByName(File file, final File fileName) {
        return file.listFiles(new FileFilter() { // from class: com.moredian.onpremise.core.utils.MyFileUtils.1
            @Override // java.io.FileFilter
            public boolean accept(File pathname) {
                String name1 = pathname.getName().substring(0, pathname.getName().lastIndexOf("."));
                String name2 = fileName.getName().substring(0, fileName.getName().lastIndexOf("."));
                boolean flag = pathname.isFile() && name1.equalsIgnoreCase(name2);
                if (pathname.isDirectory() || flag) {
                    return true;
                }
                return false;
            }
        });
    }

    private static String setComment(Integer type) {
        StringBuffer result = new StringBuffer();
        result.append("backupsTimeStamp");
        result.append(SymbolConstants.EQUAL_SYMBOL);
        result.append(System.currentTimeMillis());
        result.append("\r\n");
        result.append("backupsType");
        result.append(SymbolConstants.EQUAL_SYMBOL);
        result.append(type);
        return result.toString();
    }

    public static String getLastUpgradePackage(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                File result = files[0];
                for (int i = 0; i < files.length; i++) {
                    if (result.lastModified() < files[i].lastModified()) {
                        result = files[i];
                    }
                }
                return result.getPath();
            }
            return null;
        }
        return null;
    }

    public static byte[] str2Image(String fileStr) {
        if (fileStr == null) {
            return new byte[0];
        }
        if (fileStr.startsWith("data:image")) {
            int length = fileStr.indexOf(",");
            fileStr = fileStr.substring(length + 1);
        }
        byte[] b = Base64.decodeBase64(fileStr);
        return b;
    }

    public static byte[] getFileBytes(String filePath) throws IOException {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            while (true) {
                int n = fis.read(b);
                if (n == -1) {
                    break;
                }
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            logger.error("error:{}", (Throwable) e);
        } catch (IOException e2) {
            logger.error("error:{}", (Throwable) e2);
        }
        return buffer;
    }

    public static byte[] getMatrixBGR(BufferedImage image) {
        byte[] matrixBGR;
        if (null == image) {
            throw new NullPointerException();
        }
        if (isBGR3Byte(image)) {
            matrixBGR = (byte[]) image.getData().getDataElements(0, 0, image.getWidth(), image.getHeight(), (Object) null);
        } else {
            int[] intrgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), (int[]) null, 0, image.getWidth());
            matrixBGR = new byte[image.getWidth() * image.getHeight() * 3];
            int i = 0;
            int j = 0;
            while (i < intrgb.length) {
                matrixBGR[j] = (byte) (intrgb[i] & 255);
                matrixBGR[j + 1] = (byte) ((intrgb[i] >> 8) & 255);
                matrixBGR[j + 2] = (byte) ((intrgb[i] >> 16) & 255);
                i++;
                j += 3;
            }
        }
        return matrixBGR;
    }

    public static boolean isBGR3Byte(BufferedImage image) {
        return equalBandOffsetWith3Byte(image, new int[]{0, 1, 2});
    }

    private static boolean equalBandOffsetWith3Byte(BufferedImage image, int[] bandOffset) {
        if (image.getType() == 5 && (image.getData().getSampleModel() instanceof ComponentSampleModel)) {
            ComponentSampleModel sampleModel = image.getData().getSampleModel();
            if (Arrays.equals(sampleModel.getBandOffsets(), bandOffset)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static void setValue(Object obj, String filedName, Class<?> typeClass, Object value) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        String filedName2 = removeLine(filedName);
        String methodName = "set" + filedName2.substring(0, 1).toUpperCase() + filedName2.substring(1);
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName, typeClass);
            method.invoke(obj, getClassTypeValue(typeClass, value));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Object getClassTypeValue(Class<?> typeClass, Object value) {
        if (typeClass == Integer.TYPE || (value instanceof Integer)) {
            if (null == value) {
                return 0;
            }
            return value;
        }
        if (typeClass == Short.TYPE) {
            if (null == value) {
                return 0;
            }
            return value;
        }
        if (typeClass == Byte.TYPE) {
            if (null == value) {
                return 0;
            }
            return value;
        }
        if (typeClass == Double.TYPE) {
            if (null == value) {
                return 0;
            }
            return value;
        }
        if (typeClass == Long.TYPE) {
            if (null == value) {
                return 0;
            }
            return value;
        }
        if (typeClass == String.class) {
            if (null == value) {
                return "";
            }
            return value;
        }
        if (typeClass == Boolean.TYPE) {
            if (null == value) {
                return true;
            }
            return value;
        }
        if (typeClass == BigDecimal.class) {
            if (null == value) {
                return new BigDecimal(0);
            }
            return new BigDecimal(value + "");
        }
        return typeClass.cast(value);
    }

    public static String removeLine(String str) {
        if (null != str && str.contains("_")) {
            int i = str.indexOf("_");
            char ch2 = str.charAt(i + 1);
            char newCh = (ch2 + "").substring(0, 1).toUpperCase().toCharArray()[0];
            String newStr = str.replace(str.charAt(i + 1), newCh);
            String newStr2 = newStr.replace("_", "");
            return newStr2;
        }
        return str;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0058, code lost:
    
        parseForReturn(r10, r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.Map<java.lang.String, java.lang.String> parseTargz(java.lang.String r6) throws java.io.IOException {
        /*
            r0 = 0
            r7 = r0
            r0 = 0
            r8 = r0
            r0 = 0
            r9 = r0
            r0 = 0
            r10 = r0
            java.util.HashMap r0 = new java.util.HashMap
            r1 = r0
            r2 = 6
            r1.<init>(r2)
            r11 = r0
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r1 = r0
            java.io.File r2 = new java.io.File     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r3 = r2
            r4 = r6
            r3.<init>(r4)     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r1.<init>(r2)     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r7 = r0
            java.io.BufferedInputStream r0 = new java.io.BufferedInputStream     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r1 = r0
            r2 = r7
            r1.<init>(r2)     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r8 = r0
            java.util.zip.GZIPInputStream r0 = new java.util.zip.GZIPInputStream     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r1 = r0
            r2 = r8
            r1.<init>(r2)     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r9 = r0
            org.apache.commons.compress.archivers.tar.TarArchiveInputStream r0 = new org.apache.commons.compress.archivers.tar.TarArchiveInputStream     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r1 = r0
            r2 = r9
            r1.<init>(r2)     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r10 = r0
        L40:
            r0 = r10
            org.apache.commons.compress.archivers.tar.TarArchiveEntry r0 = r0.getNextTarEntry()     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            r1 = r0
            r12 = r1
            if (r0 == 0) goto L62
            r0 = r12
            java.lang.String r0 = r0.getName()     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            java.lang.String r1 = "ini"
            boolean r0 = r0.endsWith(r1)     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            if (r0 == 0) goto L40
            r0 = r10
            r1 = r11
            parseForReturn(r0, r1)     // Catch: java.lang.Exception -> L76 java.lang.Throwable -> L8c
            goto L62
        L62:
            r0 = r10
            closeStream(r0)
            r0 = r9
            closeStream(r0)
            r0 = r8
            closeStream(r0)
            r0 = r7
            closeStream(r0)
            goto La2
        L76:
            r12 = move-exception
            r0 = r10
            closeStream(r0)
            r0 = r9
            closeStream(r0)
            r0 = r8
            closeStream(r0)
            r0 = r7
            closeStream(r0)
            goto La2
        L8c:
            r13 = move-exception
            r0 = r10
            closeStream(r0)
            r0 = r9
            closeStream(r0)
            r0 = r8
            closeStream(r0)
            r0 = r7
            closeStream(r0)
            r0 = r13
            throw r0
        La2:
            r0 = r11
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.moredian.onpremise.core.utils.MyFileUtils.parseTargz(java.lang.String):java.util.Map");
    }

    public static void closeStream(Closeable stream) throws IOException {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
            }
        }
    }

    public static void parseForReturn(InputStream is, Map<String, String> result) throws IOException {
        int index;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while (true) {
            String line = br.readLine();
            if (line != null) {
                if (line.trim().length() != 0 && !line.startsWith("#") && (index = line.indexOf(SymbolConstants.EQUAL_SYMBOL)) != -1) {
                    result.put(line.substring(0, index), line.substring(index + 1));
                }
            } else {
                br.close();
                return;
            }
        }
    }

    public static boolean deleteFile(String path) {
        logger.info("=====>deleteFile：{}", path);
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    private static Integer random() {
        return Integer.valueOf(new Random().nextInt(8999) + 1000);
    }

    public static void copyMirrorImg(String source, String dest) {
        File fileDest = new File(dest);
        if (fileDest.exists() && fileDest.isFile()) {
            return;
        }
        ImageMirrorUtils.rotateImg(source, dest);
    }

    public static String copyFileUsingFileChannels(String sourcePath, String destPath) {
        String fileName;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                synchronized (MyFileUtils.class) {
                    fileName = DateFormatUtils.format(new Date(), "yyyyMMdd") + System.currentTimeMillis() + random();
                }
                File source = new File(sourcePath);
                String name = source.getName();
                String originalFilename = name.substring(name.lastIndexOf(".") + 1);
                logger.info("originalFilename :{} , fileName :{}", originalFilename, fileName);
                File dest = new File(destPath + fileName + "." + originalFilename);
                FileChannel inputChannel2 = new FileInputStream(source).getChannel();
                FileChannel outputChannel2 = new FileOutputStream(dest).getChannel();
                outputChannel2.transferFrom(inputChannel2, 0L, inputChannel2.size());
                String str = fileName + "." + originalFilename;
                try {
                    inputChannel2.close();
                    outputChannel2.close();
                } catch (IOException e) {
                    logger.error("error:{}", (Throwable) e);
                }
                return str;
            } catch (IOException e2) {
                logger.error("error:{}", (Throwable) e2);
                try {
                    inputChannel.close();
                    outputChannel.close();
                    return null;
                } catch (IOException e3) {
                    logger.error("error:{}", (Throwable) e3);
                    return null;
                }
            }
        } catch (Throwable th) {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e4) {
                logger.error("error:{}", (Throwable) e4);
            }
            throw th;
        }
    }

    public static boolean exists(String path) {
        return path != null && new File(path).exists();
    }

    public static List<String> zipToFile(String zipPath) throws IOException {
        List<String> childFiles = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try {
            new ZipFile(zipPath);
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath));
            BufferedInputStream bin = new BufferedInputStream(zin);
            String decompressPath = zipPath.substring(0, zipPath.indexOf("."));
            while (true) {
                ZipEntry entry = zin.getNextEntry();
                if (entry == null || entry.isDirectory()) {
                    break;
                }
                childFiles.add(decompressPath + "/" + entry.getName());
                File file = new File(decompressPath + "/" + entry.getName());
                logger.info("file name :{},file path :{}", file.getName(), file.getPath());
                if (!file.exists()) {
                    new File(file.getParent()).mkdirs();
                }
                FileOutputStream out = new FileOutputStream(file);
                BufferedOutputStream bout = new BufferedOutputStream(out);
                while (true) {
                    int b = bin.read();
                    if (b != -1) {
                        bout.write(b);
                    }
                }
                bout.close();
                out.close();
                logger.info(file.getName() + "解压成功");
            }
            bin.close();
            zin.close();
            logger.info("耗费时间： " + (System.currentTimeMillis() - startTime) + " ms");
            return childFiles;
        } catch (Exception e) {
            throw new BizException();
        }
    }
}
