package org.apache.xmlbeans.impl.tool;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/Diff.class */
public class Diff {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Diff.class.desiredAssertionStatus();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: diff <jarname1> <jarname2> to compare two jars");
            System.out.println("  or   diff <dirname1> <dirname2> to compare two dirs");
            return;
        }
        File file1 = new File(args[0]);
        if (!file1.exists()) {
            System.out.println("File \"" + args[0] + "\" not found.");
            return;
        }
        File file2 = new File(args[1]);
        if (!file2.exists()) {
            System.out.println("File \"" + args[1] + "\" not found.");
            return;
        }
        List result = new ArrayList();
        if (file1.isDirectory()) {
            if (!file2.isDirectory()) {
                System.out.println("Both parameters have to be directories if the first parameter is a directory.");
                return;
            }
            dirsAsTypeSystems(file1, file2, result);
        } else {
            if (file2.isDirectory()) {
                System.out.println("Both parameters have to be jar files if the first parameter is a jar file.");
                return;
            }
            try {
                JarFile jar1 = new JarFile(file1);
                JarFile jar2 = new JarFile(file2);
                jarsAsTypeSystems(jar1, jar2, result);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        if (result.size() < 1) {
            System.out.println("No differences encountered.");
            return;
        }
        System.out.println("Differences:");
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i).toString());
        }
    }

    public static void jarsAsTypeSystems(JarFile jar1, JarFile jar2, List diffs) throws IOException {
        Enumeration entries1 = jar1.entries();
        Enumeration entries2 = jar2.entries();
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        while (entries1.hasMoreElements()) {
            JarEntry jarEntryNextElement = entries1.nextElement();
            String name = jarEntryNextElement.getName();
            if (name.startsWith("schema" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN + "/system/s") && name.endsWith(".xsb")) {
                list1.add(jarEntryNextElement);
            }
        }
        while (entries2.hasMoreElements()) {
            JarEntry jarEntryNextElement2 = entries2.nextElement();
            String name2 = jarEntryNextElement2.getName();
            if (name2.startsWith("schema" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN + "/system/s") && name2.endsWith(".xsb")) {
                list2.add(jarEntryNextElement2);
            }
        }
        ZipEntry[] files1 = (ZipEntry[]) list1.toArray(new ZipEntry[list1.size()]);
        ZipEntry[] files2 = (ZipEntry[]) list2.toArray(new ZipEntry[list2.size()]);
        ZipEntryNameComparator comparator = new ZipEntryNameComparator();
        Arrays.sort(files1, comparator);
        Arrays.sort(files2, comparator);
        int i1 = 0;
        int i2 = 0;
        while (i1 < files1.length && i2 < files2.length) {
            String name1 = files1[i1].getName();
            String name22 = files2[i2].getName();
            int dif = name1.compareTo(name22);
            if (dif == 0) {
                zipEntriesAsXsb(files1[i1], jar1, files2[i2], jar2, diffs);
                i1++;
                i2++;
            } else if (dif < 0) {
                diffs.add("Jar \"" + jar1.getName() + "\" contains an extra file: \"" + name1 + SymbolConstants.QUOTES_SYMBOL);
                i1++;
            } else if (dif > 0) {
                diffs.add("Jar \"" + jar2.getName() + "\" contains an extra file: \"" + name22 + SymbolConstants.QUOTES_SYMBOL);
                i2++;
            }
        }
        while (i1 < files1.length) {
            diffs.add("Jar \"" + jar1.getName() + "\" contains an extra file: \"" + files1[i1].getName() + SymbolConstants.QUOTES_SYMBOL);
            i1++;
        }
        while (i2 < files2.length) {
            diffs.add("Jar \"" + jar2.getName() + "\" contains an extra file: \"" + files2[i2].getName() + SymbolConstants.QUOTES_SYMBOL);
            i2++;
        }
    }

    public static void dirsAsTypeSystems(File dir1, File dir2, List diffs) {
        if (!$assertionsDisabled && !dir1.isDirectory()) {
            throw new AssertionError("Parameters must be directories");
        }
        if (!$assertionsDisabled && !dir2.isDirectory()) {
            throw new AssertionError("Parameters must be directories");
        }
        File temp1 = new File(dir1, "schema" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN + "/system");
        File temp2 = new File(dir2, "schema" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN + "/system");
        if (temp1.exists() && temp2.exists()) {
            File[] files1 = temp1.listFiles();
            File[] files2 = temp2.listFiles();
            if (files1.length == 1 && files2.length == 1) {
                temp1 = files1[0];
                temp2 = files2[0];
            } else {
                if (files1.length == 0) {
                    temp1 = null;
                }
                if (files2.length == 0) {
                    temp2 = null;
                }
                if (files1.length > 1) {
                    diffs.add("More than one typesystem found in dir \"" + dir1.getName() + SymbolConstants.QUOTES_SYMBOL);
                    return;
                } else if (files2.length > 1) {
                    diffs.add("More than one typesystem found in dir \"" + dir2.getName() + SymbolConstants.QUOTES_SYMBOL);
                    return;
                }
            }
        } else {
            if (!temp1.exists()) {
                temp1 = null;
            }
            if (!temp2.exists()) {
                temp2 = null;
            }
        }
        if (temp1 == null && temp2 == null) {
            return;
        }
        if (temp1 == null || temp2 == null) {
            if (temp1 == null) {
                diffs.add("No typesystems found in dir \"" + dir1 + SymbolConstants.QUOTES_SYMBOL);
            }
            if (temp2 == null) {
                diffs.add("No typesystems found in dir \"" + dir2 + SymbolConstants.QUOTES_SYMBOL);
                return;
            }
            return;
        }
        File dir12 = temp1;
        File dir22 = temp2;
        boolean diffIndex = isDiffIndex();
        XsbFilenameFilter xsbName = new XsbFilenameFilter();
        File[] files12 = dir12.listFiles(xsbName);
        File[] files22 = dir22.listFiles(xsbName);
        FileNameComparator comparator = new FileNameComparator();
        Arrays.sort(files12, comparator);
        Arrays.sort(files22, comparator);
        int i1 = 0;
        int i2 = 0;
        while (i1 < files12.length && i2 < files22.length) {
            String name1 = files12[i1].getName();
            String name2 = files22[i2].getName();
            int dif = name1.compareTo(name2);
            if (dif == 0) {
                if (diffIndex || !files12[i1].getName().equals("index.xsb")) {
                    filesAsXsb(files12[i1], files22[i2], diffs);
                }
                i1++;
                i2++;
            } else if (dif < 0) {
                diffs.add("Dir \"" + dir12.getName() + "\" contains an extra file: \"" + name1 + SymbolConstants.QUOTES_SYMBOL);
                i1++;
            } else if (dif > 0) {
                diffs.add("Dir \"" + dir22.getName() + "\" contains an extra file: \"" + name2 + SymbolConstants.QUOTES_SYMBOL);
                i2++;
            }
        }
        while (i1 < files12.length) {
            diffs.add("Dir \"" + dir12.getName() + "\" contains an extra file: \"" + files12[i1].getName() + SymbolConstants.QUOTES_SYMBOL);
            i1++;
        }
        while (i2 < files22.length) {
            diffs.add("Dir \"" + dir22.getName() + "\" contains an extra file: \"" + files22[i2].getName() + SymbolConstants.QUOTES_SYMBOL);
            i2++;
        }
    }

    private static boolean isDiffIndex() {
        String prop = SystemProperties.getProperty("xmlbeans.diff.diffIndex");
        if (prop == null) {
            return true;
        }
        if ("0".equals(prop) || "false".equalsIgnoreCase(prop)) {
            return false;
        }
        return true;
    }

    public static void filesAsXsb(File file1, File file2, List diffs) {
        if (!$assertionsDisabled && !file1.exists()) {
            throw new AssertionError("File \"" + file1.getAbsolutePath() + "\" does not exist.");
        }
        if (!$assertionsDisabled && !file2.exists()) {
            throw new AssertionError("File \"" + file2.getAbsolutePath() + "\" does not exist.");
        }
        try {
            FileInputStream stream1 = new FileInputStream(file1);
            FileInputStream stream2 = new FileInputStream(file2);
            streamsAsXsb(stream1, file1.getName(), stream2, file2.getName(), diffs);
        } catch (FileNotFoundException e) {
        } catch (IOException e2) {
        }
    }

    public static void zipEntriesAsXsb(ZipEntry file1, JarFile jar1, ZipEntry file2, JarFile jar2, List diffs) throws IOException {
        try {
            InputStream stream1 = jar1.getInputStream(file1);
            InputStream stream2 = jar2.getInputStream(file2);
            streamsAsXsb(stream1, file1.getName(), stream2, file2.getName(), diffs);
        } catch (IOException e) {
        }
    }

    public static void streamsAsXsb(InputStream stream1, String name1, InputStream stream2, String name2, List diffs) throws IOException {
        ByteArrayOutputStream buf1 = new ByteArrayOutputStream();
        ByteArrayOutputStream buf2 = new ByteArrayOutputStream();
        XsbDumper.dump(stream1, "", new PrintStream(buf1));
        XsbDumper.dump(stream2, "", new PrintStream(buf2));
        stream1.close();
        stream2.close();
        readersAsText(new StringReader(buf1.toString()), name1, new StringReader(buf2.toString()), name2, diffs);
    }

    public static void readersAsText(Reader r1, String name1, Reader r2, String name2, List diffs) throws IOException {
        org.apache.xmlbeans.impl.util.Diff.readersAsText(r1, name1, r2, name2, diffs);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/Diff$XsbFilenameFilter.class */
    private static class XsbFilenameFilter implements FilenameFilter {
        private XsbFilenameFilter() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File dir, String name) {
            return name.endsWith(".xsb");
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/Diff$ZipEntryNameComparator.class */
    private static class ZipEntryNameComparator implements Comparator {
        static final /* synthetic */ boolean $assertionsDisabled;

        private ZipEntryNameComparator() {
        }

        static {
            $assertionsDisabled = !Diff.class.desiredAssertionStatus();
        }

        @Override // java.util.Comparator
        public boolean equals(Object object) {
            return this == object;
        }

        @Override // java.util.Comparator
        public int compare(Object object1, Object object2) {
            if (!$assertionsDisabled && !(object1 instanceof ZipEntry)) {
                throw new AssertionError("Must pass in a java.util.zip.ZipEntry as argument");
            }
            if (!$assertionsDisabled && !(object2 instanceof ZipEntry)) {
                throw new AssertionError("Must pass in a java.util.zip.ZipEntry as argument");
            }
            String name1 = ((ZipEntry) object1).getName();
            String name2 = ((ZipEntry) object2).getName();
            return name1.compareTo(name2);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/Diff$FileNameComparator.class */
    private static class FileNameComparator implements Comparator {
        static final /* synthetic */ boolean $assertionsDisabled;

        private FileNameComparator() {
        }

        static {
            $assertionsDisabled = !Diff.class.desiredAssertionStatus();
        }

        @Override // java.util.Comparator
        public boolean equals(Object object) {
            return this == object;
        }

        @Override // java.util.Comparator
        public int compare(Object object1, Object object2) {
            if (!$assertionsDisabled && !(object1 instanceof File)) {
                throw new AssertionError("Must pass in a java.io.File as argument");
            }
            if (!$assertionsDisabled && !(object2 instanceof File)) {
                throw new AssertionError("Must pass in a java.io.File as argument");
            }
            String name1 = ((File) object1).getName();
            String name2 = ((File) object2).getName();
            return name1.compareTo(name2);
        }
    }
}
