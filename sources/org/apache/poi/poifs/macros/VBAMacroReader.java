package org.apache.poi.poifs.macros;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.EmptyFileException;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.RLEDecompressingInputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/macros/VBAMacroReader.class */
public class VBAMacroReader implements Closeable {
    protected static final String VBA_PROJECT_OOXML = "vbaProject.bin";
    protected static final String VBA_PROJECT_POIFS = "VBA";
    private static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    private NPOIFSFileSystem fs;
    private static final int EOF = -1;
    private static final int VERSION_INDEPENDENT_TERMINATOR = 16;
    private static final int VERSION_DEPENDENT_TERMINATOR = 43;
    private static final int PROJECTVERSION = 9;
    private static final int PROJECTCODEPAGE = 3;
    private static final int STREAMNAME = 26;
    private static final int MODULEOFFSET = 49;
    private static final int MODULETYPE_PROCEDURAL = 33;
    private static final int MODULETYPE_DOCUMENT_CLASS_OR_DESIGNER = 34;
    private static final int PROJECTLCID = 2;
    private static final int MODULE_NAME = 25;
    private static final int MODULE_NAME_UNICODE = 71;
    private static final int MODULE_DOC_STRING = 28;
    private static final int STREAMNAME_RESERVED = 50;

    public VBAMacroReader(InputStream rstream) throws EmptyFileException, IOException, CloneNotSupportedException {
        InputStream is = FileMagic.prepareToCheckMagic(rstream);
        FileMagic fm = FileMagic.valueOf(is);
        if (fm == FileMagic.OLE2) {
            this.fs = new NPOIFSFileSystem(is);
        } else {
            openOOXML(is);
        }
    }

    public VBAMacroReader(File file) throws IOException {
        try {
            this.fs = new NPOIFSFileSystem(file);
        } catch (OfficeXmlFileException e) {
            openOOXML(new FileInputStream(file));
        }
    }

    public VBAMacroReader(NPOIFSFileSystem fs) {
        this.fs = fs;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002d, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0033, code lost:
    
        throw r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x002b, code lost:
    
        r9 = move-exception;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void openOOXML(java.io.InputStream r6) throws java.io.IOException {
        /*
            r5 = this;
            java.util.zip.ZipInputStream r0 = new java.util.zip.ZipInputStream
            r1 = r0
            r2 = r6
            r1.<init>(r2)
            r7 = r0
        L9:
            r0 = r7
            java.util.zip.ZipEntry r0 = r0.getNextEntry()
            r1 = r0
            r8 = r1
            if (r0 == 0) goto L34
            r0 = r8
            java.lang.String r0 = r0.getName()
            java.lang.String r1 = "vbaProject.bin"
            boolean r0 = org.apache.poi.util.StringUtil.endsWithIgnoreCase(r0, r1)
            if (r0 == 0) goto L9
            r0 = r5
            org.apache.poi.poifs.filesystem.NPOIFSFileSystem r1 = new org.apache.poi.poifs.filesystem.NPOIFSFileSystem     // Catch: java.io.IOException -> L2b
            r2 = r1
            r3 = r7
            r2.<init>(r3)     // Catch: java.io.IOException -> L2b
            r0.fs = r1     // Catch: java.io.IOException -> L2b
            return
        L2b:
            r9 = move-exception
            r0 = r7
            r0.close()
            r0 = r9
            throw r0
        L34:
            r0 = r7
            r0.close()
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.String r2 = "No VBA project found"
            r1.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.poifs.macros.VBAMacroReader.openOOXML(java.io.InputStream):void");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.fs.close();
        this.fs = null;
    }

    public Map<String, String> readMacros() throws IOException {
        ModuleMap modules = new ModuleMap();
        findMacros(this.fs.getRoot(), modules);
        Map<String, String> moduleSources = new HashMap<>();
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            Module module = entry.getValue();
            if (module.buf != null && module.buf.length > 0) {
                moduleSources.put(entry.getKey(), new String(module.buf, modules.charset));
            }
        }
        return moduleSources;
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/poifs/macros/VBAMacroReader$Module.class */
    protected static class Module {
        Integer offset;
        byte[] buf;

        protected Module() {
        }

        void read(InputStream in) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            out.close();
            this.buf = out.toByteArray();
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/poifs/macros/VBAMacroReader$ModuleMap.class */
    protected static class ModuleMap extends HashMap<String, Module> {
        Charset charset = Charset.forName("Cp1252");

        protected ModuleMap() {
        }
    }

    protected void findMacros(DirectoryNode dir, ModuleMap modules) throws IOException {
        if (VBA_PROJECT_POIFS.equalsIgnoreCase(dir.getName())) {
            readMacros(dir, modules);
            return;
        }
        Iterator i$ = dir.iterator();
        while (i$.hasNext()) {
            Entry child = i$.next();
            if (child instanceof DirectoryNode) {
                findMacros((DirectoryNode) child, modules);
            }
        }
    }

    private static String readString(InputStream stream, int length, Charset charset) throws IOException {
        byte[] buffer = new byte[length];
        int count = stream.read(buffer);
        return new String(buffer, 0, count, charset);
    }

    private static void readModule(RLEDecompressingInputStream in, String streamName, ModuleMap modules) throws IOException {
        int moduleOffset = in.readInt();
        Module module = modules.get(streamName);
        if (module == null) {
            Module module2 = new Module();
            module2.offset = Integer.valueOf(moduleOffset);
            modules.put(streamName, module2);
        } else {
            InputStream stream = new RLEDecompressingInputStream(new ByteArrayInputStream(module.buf, moduleOffset, module.buf.length - moduleOffset));
            module.read(stream);
            stream.close();
        }
    }

    private static void readModule(DocumentInputStream dis, String name, ModuleMap modules) throws IOException {
        Module module = modules.get(name);
        if (module == null) {
            Module module2 = new Module();
            modules.put(name, module2);
            module2.read(dis);
        } else if (module.buf == null) {
            if (module.offset == null) {
                throw new IOException("Module offset for '" + name + "' was never read.");
            }
            long skippedBytes = dis.skip(module.offset.intValue());
            if (skippedBytes != module.offset.intValue()) {
                throw new IOException("tried to skip " + module.offset + " bytes, but actually skipped " + skippedBytes + " bytes");
            }
            InputStream stream = new RLEDecompressingInputStream(dis);
            module.read(stream);
            stream.close();
        }
    }

    private static void trySkip(InputStream in, long n) throws IOException {
        long skippedBytes = in.skip(n);
        if (skippedBytes != n) {
            if (skippedBytes < 0) {
                throw new IOException("Tried skipping " + n + " bytes, but no bytes were skipped. The end of the stream has been reached or the stream is closed.");
            }
            throw new IOException("Tried skipping " + n + " bytes, but only " + skippedBytes + " bytes were skipped. This should never happen.");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x012b, code lost:
    
        r0.close();
     */
    /* JADX WARN: Finally extract failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void readMacros(org.apache.poi.poifs.filesystem.DirectoryNode r6, org.apache.poi.poifs.macros.VBAMacroReader.ModuleMap r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 418
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.poifs.macros.VBAMacroReader.readMacros(org.apache.poi.poifs.filesystem.DirectoryNode, org.apache.poi.poifs.macros.VBAMacroReader$ModuleMap):void");
    }

    private String readUnicodeString(RLEDecompressingInputStream in, int unicodeNameRecordLength) throws IOException {
        byte[] buffer = new byte[unicodeNameRecordLength];
        IOUtils.readFully(in, buffer);
        return new String(buffer, UTF_16LE);
    }
}
