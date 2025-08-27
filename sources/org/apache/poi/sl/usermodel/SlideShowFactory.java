package org.apache.poi.sl.usermodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.poi.EmptyFileException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.OldFileFormatException;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.util.IOUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/SlideShowFactory.class */
public class SlideShowFactory {
    public static SlideShow<?, ?> create(NPOIFSFileSystem fs) throws IOException {
        return create(fs, (String) null);
    }

    public static SlideShow<?, ?> create(NPOIFSFileSystem fs, String password) throws IOException {
        boolean passwordSet;
        DirectoryNode root = fs.getRoot();
        if (root.hasEntry(Decryptor.DEFAULT_POIFS_ENTRY)) {
            passwordSet = null;
            try {
                passwordSet = DocumentFactoryHelper.getDecryptedStream(fs, password);
                SlideShow<?, ?> slideShowCreateXSLFSlideShow = createXSLFSlideShow(passwordSet);
                IOUtils.closeQuietly(passwordSet);
                return slideShowCreateXSLFSlideShow;
            } finally {
                IOUtils.closeQuietly(passwordSet);
            }
        }
        boolean passwordSet2 = false;
        if (password != null) {
            Biff8EncryptionKey.setCurrentUserPassword(password);
            passwordSet2 = true;
        }
        try {
            SlideShow<?, ?> slideShowCreateHSLFSlideShow = createHSLFSlideShow(fs);
            if (passwordSet) {
                Biff8EncryptionKey.setCurrentUserPassword(null);
            }
            return slideShowCreateHSLFSlideShow;
        } catch (Throwable th) {
            if (passwordSet) {
                Biff8EncryptionKey.setCurrentUserPassword(null);
            }
            throw th;
        }
    }

    public static SlideShow<?, ?> create(InputStream inp) throws EncryptedDocumentException, IOException {
        return create(inp, (String) null);
    }

    public static SlideShow<?, ?> create(InputStream inp, String password) throws EncryptedDocumentException, EmptyFileException, IOException, CloneNotSupportedException {
        InputStream is = FileMagic.prepareToCheckMagic(inp);
        FileMagic fm = FileMagic.valueOf(is);
        switch (fm) {
            case OLE2:
                NPOIFSFileSystem fs = new NPOIFSFileSystem(is);
                return create(fs, password);
            case OOXML:
                return createXSLFSlideShow(is);
            default:
                throw new IllegalArgumentException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
        }
    }

    public static SlideShow<?, ?> create(File file) throws EncryptedDocumentException, IOException {
        return create(file, (String) null);
    }

    public static SlideShow<?, ?> create(File file, String password) throws EncryptedDocumentException, IOException {
        return create(file, password, false);
    }

    public static SlideShow<?, ?> create(File file, String password, boolean readOnly) throws EncryptedDocumentException, IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.toString());
        }
        NPOIFSFileSystem fs = null;
        try {
            fs = new NPOIFSFileSystem(file, readOnly);
            return create(fs, password);
        } catch (OfficeXmlFileException e) {
            IOUtils.closeQuietly(fs);
            return createXSLFSlideShow(file, Boolean.valueOf(readOnly));
        } catch (RuntimeException e2) {
            IOUtils.closeQuietly(fs);
            throw e2;
        }
    }

    protected static SlideShow<?, ?> createHSLFSlideShow(Object... args) throws EncryptedDocumentException, IOException {
        return createSlideShow("org.apache.poi.hslf.usermodel.HSLFSlideShowFactory", args);
    }

    protected static SlideShow<?, ?> createXSLFSlideShow(Object... args) throws EncryptedDocumentException, IOException {
        return createSlideShow("org.apache.poi.xslf.usermodel.XSLFSlideShowFactory", args);
    }

    protected static SlideShow<?, ?> createSlideShow(String factoryClass, Object[] args) throws NoSuchMethodException, EncryptedDocumentException, ClassNotFoundException, SecurityException, IOException {
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(factoryClass);
            Class<?>[] argsClz = new Class[args.length];
            int i = 0;
            for (Object o : args) {
                Class<?> c = o.getClass();
                if (Boolean.class.isAssignableFrom(c)) {
                    c = Boolean.TYPE;
                } else if (InputStream.class.isAssignableFrom(c)) {
                    c = InputStream.class;
                }
                int i2 = i;
                i++;
                argsClz[i2] = c;
            }
            Method m = clazz.getMethod("createSlideShow", argsClz);
            return (SlideShow) m.invoke(null, args);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof IOException) {
                throw ((IOException) t);
            }
            if (t instanceof EncryptedDocumentException) {
                throw ((EncryptedDocumentException) t);
            }
            if (t instanceof OldFileFormatException) {
                throw ((OldFileFormatException) t);
            }
            throw new IOException(t);
        } catch (Exception e2) {
            throw new IOException(e2);
        }
    }
}
