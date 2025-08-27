package org.apache.xmlbeans.impl.tool;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.namespace.QName;
import org.apache.commons.codec.language.bm.Rule;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.util.HexBin;
import org.apache.xmlbeans.soap.SOAPArrayType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/XsbDumper.class */
public class XsbDumper {
    private String _indent;
    private PrintStream _out;
    public static final int DATA_BABE = -629491010;
    public static final int MAJOR_VERSION = 2;
    public static final int MINOR_VERSION = 24;
    public static final int FILETYPE_SCHEMAINDEX = 1;
    public static final int FILETYPE_SCHEMATYPE = 2;
    public static final int FILETYPE_SCHEMAELEMENT = 3;
    public static final int FILETYPE_SCHEMAATTRIBUTE = 4;
    public static final int FILETYPE_SCHEMAPOINTER = 5;
    public static final int FILETYPE_SCHEMAMODELGROUP = 6;
    public static final int FILETYPE_SCHEMAATTRIBUTEGROUP = 7;
    public static final int FLAG_PART_SKIPPABLE = 1;
    public static final int FLAG_PART_FIXED = 4;
    public static final int FLAG_PART_NILLABLE = 8;
    public static final int FLAG_PART_BLOCKEXT = 16;
    public static final int FLAG_PART_BLOCKREST = 32;
    public static final int FLAG_PART_BLOCKSUBST = 64;
    public static final int FLAG_PART_ABSTRACT = 128;
    public static final int FLAG_PART_FINALEXT = 256;
    public static final int FLAG_PART_FINALREST = 512;
    public static final int FLAG_PROP_ISATTR = 1;
    public static final int FLAG_PROP_JAVASINGLETON = 2;
    public static final int FLAG_PROP_JAVAOPTIONAL = 4;
    public static final int FLAG_PROP_JAVAARRAY = 8;
    public static final int FIELD_NONE = 0;
    public static final int FIELD_GLOBAL = 1;
    public static final int FIELD_LOCALATTR = 2;
    public static final int FIELD_LOCALELT = 3;
    static final int FLAG_SIMPLE_TYPE = 1;
    static final int FLAG_DOCUMENT_TYPE = 2;
    static final int FLAG_ORDERED = 4;
    static final int FLAG_BOUNDED = 8;
    static final int FLAG_FINITE = 16;
    static final int FLAG_NUMERIC = 32;
    static final int FLAG_STRINGENUM = 64;
    static final int FLAG_UNION_OF_LISTS = 128;
    static final int FLAG_HAS_PATTERN = 256;
    static final int FLAG_ORDER_SENSITIVE = 512;
    static final int FLAG_TOTAL_ORDER = 1024;
    static final int FLAG_COMPILED = 2048;
    static final int FLAG_BLOCK_EXT = 4096;
    static final int FLAG_BLOCK_REST = 8192;
    static final int FLAG_FINAL_EXT = 16384;
    static final int FLAG_FINAL_REST = 32768;
    static final int FLAG_FINAL_UNION = 65536;
    static final int FLAG_FINAL_LIST = 131072;
    static final int FLAG_ABSTRACT = 262144;
    static final int FLAG_ATTRIBUTE_TYPE = 524288;
    DataInputStream _input;
    StringPool _stringPool;
    private static final XmlOptions prettyOptions;
    static final byte[] SINGLE_ZERO_BYTE;
    private int _majorver;
    private int _minorver;
    private int _releaseno;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XsbDumper.class.desiredAssertionStatus();
        prettyOptions = new XmlOptions().setSavePrettyPrint();
        SINGLE_ZERO_BYTE = new byte[]{0};
    }

    public static void printUsage() {
        System.out.println("Prints the contents of an XSB file in human-readable form.");
        System.out.println("An XSB file contains schema meta information needed to ");
        System.out.println("perform tasks such as binding and validation.");
        System.out.println("Usage: dumpxsb myfile.xsb");
        System.out.println("    myfile.xsb - Path to an XSB file.");
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            printUsage();
            System.exit(0);
        } else {
            for (String str : args) {
                dump(new File(str), true);
            }
        }
    }

    private static void dump(File file, boolean force) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles(new FileFilter() { // from class: org.apache.xmlbeans.impl.tool.XsbDumper.1
                @Override // java.io.FileFilter
                public boolean accept(File file2) {
                    return file2.isDirectory() || (file2.isFile() && file2.getName().endsWith(".xsb"));
                }
            });
            for (File file2 : files) {
                dump(file2, false);
            }
            return;
        }
        if (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")) {
            dumpZip(file);
            return;
        }
        if (force || file.getName().endsWith(".xsb")) {
            try {
                System.out.println(file.toString());
                dump(new FileInputStream(file), "  ");
                System.out.println();
            } catch (FileNotFoundException e) {
                System.out.println(e.toString());
            }
        }
    }

    public static void dumpZip(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                if (entry.getName().endsWith(".xsb")) {
                    System.out.println(entry.getName());
                    dump(zipFile.getInputStream(entry), "  ");
                    System.out.println();
                }
            }
        } catch (IOException e2) {
            System.out.println(e2.toString());
        }
    }

    public static void dump(InputStream input) throws IOException {
        dump(input, "", System.out);
    }

    public static void dump(InputStream input, String indent) throws IOException {
        dump(input, indent, System.out);
    }

    public static void dump(InputStream input, String indent, PrintStream output) throws IOException {
        XsbDumper dumper = new XsbDumper(input, indent, output);
        dumper.dumpAll();
    }

    private XsbDumper(InputStream stream, String indent, PrintStream ostream) {
        this._input = new DataInputStream(stream);
        this._indent = indent;
        this._out = ostream;
    }

    void flush() {
        this._out.flush();
    }

    void emit(String str) {
        this._out.println(this._indent + str);
        flush();
    }

    void emit() {
        this._out.println();
        flush();
    }

    void error(Exception e) {
        this._out.println(e.toString());
        flush();
        IllegalStateException e2 = new IllegalStateException(e.getMessage());
        e2.initCause(e);
        throw e2;
    }

    void error(String str) {
        this._out.println(str);
        flush();
        IllegalStateException e2 = new IllegalStateException(str);
        throw e2;
    }

    void indent() {
        this._indent += "  ";
    }

    void outdent() {
        this._indent = this._indent.substring(0, this._indent.length() - 2);
    }

    static String filetypeString(int code) {
        switch (code) {
            case 1:
                return "FILETYPE_SCHEMAINDEX";
            case 2:
                return "FILETYPE_SCHEMATYPE";
            case 3:
                return "FILETYPE_SCHEMAELEMENT";
            case 4:
                return "FILETYPE_SCHEMAATTRIBUTE";
            case 5:
                return "FILETYPE_SCHEMAPOINTER";
            case 6:
                return "FILETYPE_SCHEMAMODELGROUP";
            case 7:
                return "FILETYPE_SCHEMAATTRIBUTEGROUP";
            default:
                return "Unknown FILETYPE (" + code + ")";
        }
    }

    static String particleflagsString(int flags) {
        StringBuffer result = new StringBuffer();
        if ((flags & 1) != 0) {
            result.append("FLAG_PART_SKIPPABLE | ");
        }
        if ((flags & 4) != 0) {
            result.append("FLAG_PART_FIXED | ");
        }
        if ((flags & 8) != 0) {
            result.append("FLAG_PART_NILLABLE | ");
        }
        if ((flags & 16) != 0) {
            result.append("FLAG_PART_BLOCKEXT | ");
        }
        if ((flags & 32) != 0) {
            result.append("FLAG_PART_BLOCKREST | ");
        }
        if ((flags & 64) != 0) {
            result.append("FLAG_PART_BLOCKSUBST | ");
        }
        if ((flags & 128) != 0) {
            result.append("FLAG_PART_ABSTRACT | ");
        }
        if ((flags & 256) != 0) {
            result.append("FLAG_PART_FINALEXT | ");
        }
        if ((flags & 512) != 0) {
            result.append("FLAG_PART_FINALREST | ");
        }
        if (result.length() == 0) {
            result.append("0 | ");
        }
        return result.substring(0, result.length() - 3);
    }

    static String propertyflagsString(int flags) {
        StringBuffer result = new StringBuffer();
        if ((flags & 1) != 0) {
            result.append("FLAG_PROP_ISATTR | ");
        }
        if ((flags & 2) != 0) {
            result.append("FLAG_PROP_JAVASINGLETON | ");
        }
        if ((flags & 4) != 0) {
            result.append("FLAG_PROP_JAVAOPTIONAL | ");
        }
        if ((flags & 8) != 0) {
            result.append("FLAG_PROP_JAVAARRAY | ");
        }
        if (result.length() == 0) {
            result.append("0 | ");
        }
        return result.substring(0, result.length() - 3);
    }

    static String containerfieldTypeString(int code) {
        switch (code) {
            case 0:
                return "FIELD_NONE";
            case 1:
                return "FIELD_GLOBAL";
            case 2:
                return "FIELD_LOCALATTR";
            case 3:
                return "FIELD_LOCALELT";
            default:
                return "Unknown container field type (" + code + ")";
        }
    }

    static String typeflagsString(int flags) {
        StringBuffer result = new StringBuffer();
        if ((flags & 1) != 0) {
            result.append("FLAG_SIMPLE_TYPE | ");
        }
        if ((flags & 2) != 0) {
            result.append("FLAG_DOCUMENT_TYPE | ");
        }
        if ((flags & 524288) != 0) {
            result.append("FLAG_ATTRIBUTE_TYPE | ");
        }
        if ((flags & 4) != 0) {
            result.append("FLAG_ORDERED | ");
        }
        if ((flags & 8) != 0) {
            result.append("FLAG_BOUNDED | ");
        }
        if ((flags & 16) != 0) {
            result.append("FLAG_FINITE | ");
        }
        if ((flags & 32) != 0) {
            result.append("FLAG_NUMERIC | ");
        }
        if ((flags & 64) != 0) {
            result.append("FLAG_STRINGENUM | ");
        }
        if ((flags & 128) != 0) {
            result.append("FLAG_UNION_OF_LISTS | ");
        }
        if ((flags & 256) != 0) {
            result.append("FLAG_HAS_PATTERN | ");
        }
        if ((flags & 1024) != 0) {
            result.append("FLAG_TOTAL_ORDER | ");
        }
        if ((flags & 2048) != 0) {
            result.append("FLAG_COMPILED | ");
        }
        if ((flags & 4096) != 0) {
            result.append("FLAG_BLOCK_EXT | ");
        }
        if ((flags & 8192) != 0) {
            result.append("FLAG_BLOCK_REST | ");
        }
        if ((flags & 16384) != 0) {
            result.append("FLAG_FINAL_EXT | ");
        }
        if ((flags & 32768) != 0) {
            result.append("FLAG_FINAL_REST | ");
        }
        if ((flags & 65536) != 0) {
            result.append("FLAG_FINAL_UNION | ");
        }
        if ((flags & 131072) != 0) {
            result.append("FLAG_FINAL_LIST | ");
        }
        if ((flags & 262144) != 0) {
            result.append("FLAG_ABSTRACT | ");
        }
        if (result.length() == 0) {
            result.append("0 | ");
        }
        return result.substring(0, result.length() - 3);
    }

    void dumpAll() throws IOException {
        int filetype = dumpHeader();
        switch (filetype) {
            case 1:
                dumpIndexData();
                return;
            case 2:
                dumpTypeFileData();
                break;
            case 3:
                dumpParticleData(true);
                break;
            case 4:
                dumpAttributeData(true);
                break;
            case 5:
                dumpPointerData();
                break;
            case 6:
                dumpModelGroupData();
                break;
            case 7:
                dumpAttributeGroupData();
                break;
        }
        readEnd();
    }

    static String hex32String(int i) {
        return Integer.toHexString(i);
    }

    protected int dumpHeader() throws IOException {
        int magic = readInt();
        emit("Magic cookie: " + hex32String(magic));
        if (magic != -629491010) {
            emit("Wrong magic cookie.");
            return 0;
        }
        this._majorver = readShort();
        this._minorver = readShort();
        if (atLeast(2, 18, 0)) {
            this._releaseno = readShort();
        }
        emit("Major version: " + this._majorver);
        emit("Minor version: " + this._minorver);
        emit("Release number: " + this._releaseno);
        if (this._majorver != 2 || this._minorver > 24) {
            emit("Incompatible version.");
            return 0;
        }
        int actualfiletype = readShort();
        emit("Filetype: " + filetypeString(actualfiletype));
        this._stringPool = new StringPool();
        this._stringPool.readFrom(this._input);
        return actualfiletype;
    }

    void dumpPointerData() {
        emit("Type system: " + readString());
    }

    protected void dumpIndexData() {
        int size = readShort();
        emit("Handle pool (" + size + "):");
        indent();
        for (int i = 0; i < size; i++) {
            String handle = readString();
            int code = readShort();
            emit(handle + " (" + filetypeString(code) + ")");
        }
        outdent();
        dumpQNameMap("Global elements");
        dumpQNameMap("Global attributes");
        dumpQNameMap("Model groups");
        dumpQNameMap("Attribute groups");
        dumpQNameMap("Identity constraints");
        dumpQNameMap("Global types");
        dumpQNameMap("Document types");
        dumpQNameMap("Attribute types");
        dumpClassnameIndex("All types by classname");
        dumpStringArray("Defined namespaces");
        if (atLeast(2, 15, 0)) {
            dumpQNameMap("Redefined global types");
            dumpQNameMap("Redfined model groups");
            dumpQNameMap("Redfined attribute groups");
        }
        if (atLeast(2, 19, 0)) {
            dumpAnnotations();
        }
        readEnd();
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/XsbDumper$StringPool.class */
    class StringPool {
        private List intsToStrings = new ArrayList();
        private Map stringsToInts = new HashMap();

        StringPool() {
            this.intsToStrings.add(null);
        }

        String stringForCode(int code) {
            if (code == 0) {
                return null;
            }
            return (String) this.intsToStrings.get(code);
        }

        int codeForString(String str) {
            if (str == null) {
                return 0;
            }
            Integer result = (Integer) this.stringsToInts.get(str);
            if (result == null) {
                result = new Integer(this.intsToStrings.size());
                this.intsToStrings.add(str);
                this.stringsToInts.put(str, result);
            }
            return result.intValue();
        }

        void readFrom(DataInputStream input) throws IOException {
            if (this.intsToStrings.size() != 1 || this.stringsToInts.size() != 0) {
                throw new IllegalStateException();
            }
            try {
                int size = input.readShort();
                XsbDumper.this.emit("String pool (" + size + "):");
                XsbDumper.this.indent();
                for (int i = 1; i < size; i++) {
                    String str = input.readUTF();
                    int code = codeForString(str);
                    if (code != i) {
                        throw new IllegalStateException();
                    }
                    XsbDumper.this.emit(code + " = \"" + str + SymbolConstants.QUOTES_SYMBOL);
                }
                XsbDumper.this.outdent();
            } catch (IOException e) {
                XsbDumper.this.emit(e.toString());
            }
        }
    }

    int readShort() {
        try {
            return this._input.readUnsignedShort();
        } catch (IOException e) {
            error(e);
            return 0;
        }
    }

    int readInt() {
        try {
            return this._input.readInt();
        } catch (IOException e) {
            error(e);
            return 0;
        }
    }

    String readString() {
        return this._stringPool.stringForCode(readShort());
    }

    QName readQName() {
        String namespace = readString();
        String localname = readString();
        if (localname == null) {
            return null;
        }
        return new QName(namespace, localname);
    }

    String readHandle() {
        return readString();
    }

    String readType() {
        return readHandle();
    }

    static String qnameString(QName qname) {
        if (qname == null) {
            return "(null)";
        }
        if (qname.getNamespaceURI() != null) {
            return qname.getLocalPart() + "@" + qname.getNamespaceURI();
        }
        return qname.getLocalPart();
    }

    static String qnameSetString(QNameSet set) {
        return set.toString();
    }

    void dumpQNameMap(String fieldname) {
        int size = readShort();
        emit(fieldname + " (" + size + "):");
        indent();
        for (int i = 0; i < size; i++) {
            emit(qnameString(readQName()) + " = " + readHandle());
        }
        outdent();
    }

    void dumpTypeArray(String fieldname) {
        int size = readShort();
        emit(fieldname + " (" + size + "):");
        indent();
        for (int i = 0; i < size; i++) {
            emit(i + " = " + readType());
        }
        outdent();
    }

    void dumpClassnameIndex(String fieldname) {
        int size = readShort();
        emit(fieldname + " (" + size + "):");
        indent();
        for (int i = 0; i < size; i++) {
            emit(readString() + " = " + readType());
        }
        outdent();
    }

    void dumpStringArray(String fieldname) {
        int size = readShort();
        emit(fieldname + " (" + size + "):");
        indent();
        for (int i = 0; i < size; i++) {
            emit(readString());
        }
        outdent();
    }

    void readEnd() {
        try {
            this._input.close();
        } catch (IOException e) {
        }
        this._input = null;
        this._stringPool = null;
    }

    static String particleTypeString(int spt) {
        switch (spt) {
            case 1:
                return Rule.ALL;
            case 2:
                return "CHOICE";
            case 3:
                return "SEQUENCE";
            case 4:
                return "ELEMENT";
            case 5:
                return "WILDCARD";
            default:
                return "Unknown particle type (" + spt + ")";
        }
    }

    static String bigIntegerString(BigInteger bigint) {
        if (bigint == null) {
            return "(null)";
        }
        return bigint.toString();
    }

    static String wcprocessString(int code) {
        switch (code) {
            case 0:
                return "NOT_WILDCARD";
            case 1:
                return "STRICT";
            case 2:
                return "LAX";
            case 3:
                return "SKIP";
            default:
                return "Unknown process type (" + code + ")";
        }
    }

    void dumpAnnotation() {
        int n;
        if (!atLeast(2, 19, 0) || (n = readInt()) == -1) {
            return;
        }
        emit("Annotation");
        boolean empty = true;
        indent();
        if (n > 0) {
            emit("Attributes (" + n + "):");
            indent();
            for (int i = 0; i < n; i++) {
                if (atLeast(2, 24, 0)) {
                    emit("Name: " + qnameString(readQName()) + ", Value: " + readString() + ", ValueURI: " + readString());
                } else {
                    emit("Name: " + qnameString(readQName()) + ", Value: " + readString());
                }
            }
            outdent();
            empty = false;
        }
        int n2 = readInt();
        if (n2 > 0) {
            emit("Documentation elements (" + n2 + "):");
            indent();
            for (int i2 = 0; i2 < n2; i2++) {
                emit(readString());
            }
            outdent();
            empty = false;
        }
        int n3 = readInt();
        if (n3 > 0) {
            emit("Appinfo elements (" + n3 + "):");
            indent();
            for (int i3 = 0; i3 < n3; i3++) {
                emit(readString());
            }
            outdent();
            empty = false;
        }
        if (empty) {
            emit("<empty>");
        }
        outdent();
    }

    void dumpAnnotations() {
        int n = readInt();
        if (n > 0) {
            emit("Top-level annotations (" + n + "):");
            indent();
            for (int i = 0; i < n; i++) {
                dumpAnnotation();
            }
            outdent();
        }
    }

    void dumpParticleData(boolean global) {
        int particleType = readShort();
        emit(particleTypeString(particleType) + ":");
        indent();
        int particleFlags = readShort();
        emit("Flags: " + particleflagsString(particleFlags));
        emit("MinOccurs: " + bigIntegerString(readBigInteger()));
        emit("MaxOccurs: " + bigIntegerString(readBigInteger()));
        emit("Transition: " + qnameSetString(readQNameSet()));
        switch (particleType) {
            case 1:
            case 2:
            case 3:
                dumpParticleArray("Particle children");
                break;
            case 4:
                emit("Name: " + qnameString(readQName()));
                emit("Type: " + readType());
                emit("Default: " + readString());
                if (atLeast(2, 16, 0)) {
                    emit("Default value: " + readXmlValueObject());
                }
                emit("WsdlArrayType: " + SOAPArrayTypeString(readSOAPArrayType()));
                dumpAnnotation();
                if (global) {
                    if (atLeast(2, 17, 0)) {
                        emit("Substitution group ref: " + readHandle());
                    }
                    int substGroupCount = readShort();
                    emit("Substitution group members (" + substGroupCount + ")");
                    indent();
                    for (int i = 0; i < substGroupCount; i++) {
                        emit(qnameString(readQName()));
                    }
                    outdent();
                }
                int count = readShort();
                emit("Identity constraints (" + count + "):");
                indent();
                for (int i2 = 0; i2 < count; i2++) {
                    emit(readHandle());
                }
                outdent();
                if (global) {
                    emit("Filename: " + readString());
                    break;
                }
                break;
            case 5:
                emit("Wildcard set: " + qnameSetString(readQNameSet()));
                emit("Wildcard process: " + wcprocessString(readShort()));
                break;
            default:
                error("Unrecognized schema particle type");
                break;
        }
        outdent();
    }

    void dumpParticleArray(String fieldname) {
        int count = readShort();
        emit(fieldname + "(" + count + "):");
        indent();
        for (int i = 0; i < count; i++) {
            dumpParticleData(false);
        }
        outdent();
    }

    static String complexVarietyString(int code) {
        switch (code) {
            case 1:
                return "EMPTY_CONTENT";
            case 2:
                return "SIMPLE_CONTENT";
            case 3:
                return "ELEMENT_CONTENT";
            case 4:
                return "MIXED_CONTENT";
            default:
                return "Unknown complex variety (" + code + ")";
        }
    }

    static String simpleVarietyString(int code) {
        switch (code) {
            case 1:
                return "ATOMIC";
            case 2:
                return "UNION";
            case 3:
                return "LIST";
            default:
                return "Unknown simple variety (" + code + ")";
        }
    }

    String facetCodeString(int code) {
        switch (code) {
            case 0:
                return "FACET_LENGTH";
            case 1:
                return "FACET_MIN_LENGTH";
            case 2:
                return "FACET_MAX_LENGTH";
            case 3:
                return "FACET_MIN_EXCLUSIVE";
            case 4:
                return "FACET_MIN_INCLUSIVE";
            case 5:
                return "FACET_MAX_INCLUSIVE";
            case 6:
                return "FACET_MAX_EXCLUSIVE";
            case 7:
                return "FACET_TOTAL_DIGITS";
            case 8:
                return "FACET_FRACTION_DIGITS";
            default:
                return "Unknown facet code (" + code + ")";
        }
    }

    String whitespaceCodeString(int code) {
        switch (code) {
            case 0:
                return "WS_UNSPECIFIED";
            case 1:
                return "WS_PRESERVE";
            case 2:
                return "WS_REPLACE";
            case 3:
                return "WS_COLLAPSE";
            default:
                return "Unknown whitespace code (" + code + ")";
        }
    }

    String derivationTypeString(int code) {
        switch (code) {
            case 0:
                return "DT_NOT_DERIVED";
            case 1:
                return "DT_RESTRICTION";
            case 2:
                return "DT_EXTENSION";
            default:
                return "Unknown derivation code (" + code + ")";
        }
    }

    void dumpTypeFileData() {
        emit("Name: " + qnameString(readQName()));
        emit("Outer type: " + readType());
        emit("Depth: " + readShort());
        emit("Base type: " + readType());
        emit("Derivation type: " + derivationTypeString(readShort()));
        dumpAnnotation();
        emit("Container field:");
        indent();
        int containerfieldtype = readShort();
        emit("Reftype: " + containerfieldTypeString(containerfieldtype));
        switch (containerfieldtype) {
            case 1:
                emit("Handle: " + readHandle());
                break;
            case 2:
                emit("Index: " + readShort());
                break;
            case 3:
                emit("Index: " + readShort());
                break;
        }
        outdent();
        emit("Java class name: " + readString());
        emit("Java impl class name: " + readString());
        dumpTypeArray("Anonymous types");
        emit("Anonymous union member ordinal: " + readShort());
        int flags = readInt();
        emit("Flags: " + typeflagsString(flags));
        boolean isComplexType = (flags & 1) == 0;
        int complexVariety = 0;
        if (isComplexType) {
            complexVariety = readShort();
            emit("Complex variety: " + complexVarietyString(complexVariety));
            if (atLeast(2, 23, 0)) {
                emit("Content based on type: " + readType());
            }
            int attrCount = readShort();
            emit("Attribute model (" + attrCount + "):");
            indent();
            for (int i = 0; i < attrCount; i++) {
                dumpAttributeData(false);
            }
            emit("Wildcard set: " + qnameSetString(readQNameSet()));
            emit("Wildcard process: " + wcprocessString(readShort()));
            outdent();
            int attrPropCount = readShort();
            emit("Attribute properties (" + attrPropCount + "):");
            indent();
            for (int i2 = 0; i2 < attrPropCount; i2++) {
                dumpPropertyData();
            }
            outdent();
            if (complexVariety == 3 || complexVariety == 4) {
                emit("IsAll: " + readShort());
                dumpParticleArray("Content model");
                int elemPropCount = readShort();
                emit("Element properties (" + elemPropCount + "):");
                indent();
                for (int i3 = 0; i3 < elemPropCount; i3++) {
                    dumpPropertyData();
                }
                outdent();
            }
        }
        if (!isComplexType || complexVariety == 2) {
            int simpleVariety = readShort();
            emit("Simple type variety: " + simpleVarietyString(simpleVariety));
            boolean isStringEnum = (flags & 64) != 0;
            int facetCount = readShort();
            emit("Facets (" + facetCount + "):");
            indent();
            for (int i4 = 0; i4 < facetCount; i4++) {
                emit(facetCodeString(readShort()));
                emit("Value: " + readXmlValueObject());
                emit("Fixed: " + readShort());
            }
            outdent();
            emit("Whitespace rule: " + whitespaceCodeString(readShort()));
            int patternCount = readShort();
            emit("Patterns (" + patternCount + "):");
            indent();
            for (int i5 = 0; i5 < patternCount; i5++) {
                emit(readString());
            }
            outdent();
            int enumCount = readShort();
            emit("Enumeration values (" + enumCount + "):");
            indent();
            for (int i6 = 0; i6 < enumCount; i6++) {
                emit(readXmlValueObject());
            }
            outdent();
            emit("Base enum type: " + readType());
            if (isStringEnum) {
                int seCount = readShort();
                emit("String enum entries (" + seCount + "):");
                indent();
                for (int i7 = 0; i7 < seCount; i7++) {
                    emit(SymbolConstants.QUOTES_SYMBOL + readString() + "\" -> " + readShort() + " = " + readString());
                }
                outdent();
            }
            switch (simpleVariety) {
                case 1:
                    emit("Primitive type: " + readType());
                    emit("Decimal size: " + readInt());
                    break;
                case 2:
                    dumpTypeArray("Union members");
                    break;
                case 3:
                    emit("List item type: " + readType());
                    break;
                default:
                    error("Unknown simple type variety");
                    break;
            }
        }
        emit("Filename: " + readString());
    }

    static String attruseCodeString(int code) {
        switch (code) {
            case 1:
                return "PROHIBITED";
            case 2:
                return "OPTIONAL";
            case 3:
                return "REQUIRED";
            default:
                return "Unknown use code (" + code + ")";
        }
    }

    void dumpAttributeData(boolean global) {
        emit("Name: " + qnameString(readQName()));
        emit("Type: " + readType());
        emit("Use: " + attruseCodeString(readShort()));
        emit("Default: " + readString());
        if (atLeast(2, 16, 0)) {
            emit("Default value: " + readXmlValueObject());
        }
        emit("Fixed: " + readShort());
        emit("WsdlArrayType: " + SOAPArrayTypeString(readSOAPArrayType()));
        dumpAnnotation();
        if (global) {
            emit("Filename: " + readString());
        }
    }

    void dumpXml() {
        String xml = readString();
        try {
            emit(XmlObject.Factory.parse(xml).xmlText(prettyOptions));
        } catch (XmlException e) {
            emit("!!!!!! BAD XML !!!!!");
            emit(xml);
        }
    }

    void dumpModelGroupData() {
        emit("Name: " + qnameString(readQName()));
        emit("Target namespace: " + readString());
        emit("Chameleon: " + readShort());
        if (atLeast(2, 22, 0)) {
            emit("Element form default: " + readString());
        }
        if (atLeast(2, 22, 0)) {
            emit("Attribute form default: " + readString());
        }
        if (atLeast(2, 15, 0)) {
            emit("Redefine: " + readShort());
        }
        emit("Model Group Xml: ");
        dumpXml();
        dumpAnnotation();
        if (atLeast(2, 21, 0)) {
            emit("Filename: " + readString());
        }
    }

    void dumpAttributeGroupData() {
        emit("Name: " + qnameString(readQName()));
        emit("Target namespace: " + readString());
        emit("Chameleon: " + readShort());
        if (atLeast(2, 22, 0)) {
            emit("Form default: " + readString());
        }
        if (atLeast(2, 15, 0)) {
            emit("Redefine: " + readShort());
        }
        emit("Attribute Group Xml: ");
        dumpXml();
        dumpAnnotation();
        if (atLeast(2, 21, 0)) {
            emit("Filename: " + readString());
        }
    }

    static String alwaysString(int code) {
        switch (code) {
            case 0:
                return "NEVER";
            case 1:
                return "VARIABLE";
            case 2:
                return "CONSISTENTLY";
            default:
                return "Unknown frequency code (" + code + ")";
        }
    }

    static String jtcString(int code) {
        switch (code) {
            case 0:
                return "XML_OBJECT";
            case 1:
                return "JAVA_BOOLEAN";
            case 2:
                return "JAVA_FLOAT";
            case 3:
                return "JAVA_DOUBLE";
            case 4:
                return "JAVA_BYTE";
            case 5:
                return "JAVA_SHORT";
            case 6:
                return "JAVA_INT";
            case 7:
                return "JAVA_LONG";
            case 8:
                return "JAVA_BIG_DECIMAL";
            case 9:
                return "JAVA_BIG_INTEGER";
            case 10:
                return "JAVA_STRING";
            case 11:
                return "JAVA_BYTE_ARRAY";
            case 12:
                return "JAVA_GDATE";
            case 13:
                return "JAVA_GDURATION";
            case 14:
                return "JAVA_DATE";
            case 15:
                return "JAVA_QNAME";
            case 16:
                return "JAVA_LIST";
            case 17:
                return "JAVA_CALENDAR";
            case 18:
                return "JAVA_ENUM";
            case 19:
                return "JAVA_OBJECT";
            default:
                return "Unknown java type code (" + code + ")";
        }
    }

    void dumpPropertyData() {
        emit("Property");
        indent();
        emit("Name: " + qnameString(readQName()));
        emit("Type: " + readType());
        int propflags = readShort();
        emit("Flags: " + propertyflagsString(propflags));
        emit("Container type: " + readType());
        emit("Min occurances: " + bigIntegerString(readBigInteger()));
        emit("Max occurances: " + bigIntegerString(readBigInteger()));
        emit("Nillable: " + alwaysString(readShort()));
        emit("Default: " + alwaysString(readShort()));
        emit("Fixed: " + alwaysString(readShort()));
        emit("Default text: " + readString());
        emit("Java prop name: " + readString());
        emit("Java type code: " + jtcString(readShort()));
        emit("Type for java signature: " + readType());
        if (atMost(2, 19, 0)) {
            emit("Java setter delimiter: " + qnameSetString(readQNameSet()));
        }
        if (atLeast(2, 16, 0)) {
            emit("Default value: " + readXmlValueObject());
        }
        if ((propflags & 1) == 0 && atLeast(2, 17, 0)) {
            int size = readShort();
            emit("Accepted substitutions (" + size + "):");
            for (int i = 0; i < size; i++) {
                emit("  Accepted name " + readQName());
            }
        }
        outdent();
    }

    String readXmlValueObject() {
        String value;
        String type = readType();
        if (type == null) {
            return "null";
        }
        int btc = readShort();
        switch (btc) {
            case 1:
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            case 0:
                value = "nil";
                return value + " (" + type + ": " + btc + ")";
            case 2:
            case 3:
            case 6:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                value = readString();
                return value + " (" + type + ": " + btc + ")";
            case 4:
            case 5:
                value = new String(HexBin.encode(readByteArray()));
                if (value.length() > 19) {
                    value = ((Object) value.subSequence(0, 16)) + "...";
                }
                return value + " (" + type + ": " + btc + ")";
            case 7:
            case 8:
                value = QNameHelper.pretty(readQName());
                return value + " (" + type + ": " + btc + ")";
            case 9:
            case 10:
                value = Double.toString(readDouble());
                return value + " (" + type + ": " + btc + ")";
        }
    }

    double readDouble() {
        try {
            return this._input.readDouble();
        } catch (IOException e) {
            error(e);
            return 0.0d;
        }
    }

    String SOAPArrayTypeString(SOAPArrayType t) {
        if (t == null) {
            return "null";
        }
        return QNameHelper.pretty(t.getQName()) + t.soap11DimensionString();
    }

    SOAPArrayType readSOAPArrayType() {
        QName qName = readQName();
        String dimensions = readString();
        if (qName == null) {
            return null;
        }
        return new SOAPArrayType(qName, dimensions);
    }

    QNameSet readQNameSet() {
        int flag = readShort();
        Set uriSet = new HashSet();
        int uriCount = readShort();
        for (int i = 0; i < uriCount; i++) {
            uriSet.add(readString());
        }
        Set qnameSet1 = new HashSet();
        int qncount1 = readShort();
        for (int i2 = 0; i2 < qncount1; i2++) {
            qnameSet1.add(readQName());
        }
        Set qnameSet2 = new HashSet();
        int qncount2 = readShort();
        for (int i3 = 0; i3 < qncount2; i3++) {
            qnameSet2.add(readQName());
        }
        if (flag == 1) {
            return QNameSet.forSets(uriSet, null, qnameSet1, qnameSet2);
        }
        return QNameSet.forSets(null, uriSet, qnameSet2, qnameSet1);
    }

    byte[] readByteArray() throws IOException {
        try {
            int len = this._input.readShort();
            byte[] result = new byte[len];
            this._input.readFully(result);
            return result;
        } catch (IOException e) {
            error(e);
            return null;
        }
    }

    BigInteger readBigInteger() throws IOException {
        byte[] result = readByteArray();
        if (result.length == 0) {
            return null;
        }
        if (result.length == 1 && result[0] == 0) {
            return BigInteger.ZERO;
        }
        if (result.length == 1 && result[0] == 1) {
            return BigInteger.ONE;
        }
        return new BigInteger(result);
    }

    protected boolean atLeast(int majorver, int minorver, int releaseno) {
        if (this._majorver > majorver) {
            return true;
        }
        if (this._majorver < majorver) {
            return false;
        }
        if (this._minorver > minorver) {
            return true;
        }
        return this._minorver >= minorver && this._releaseno >= releaseno;
    }

    protected boolean atMost(int majorver, int minorver, int releaseno) {
        if (this._majorver > majorver) {
            return false;
        }
        if (this._majorver < majorver) {
            return true;
        }
        if (this._minorver > minorver) {
            return false;
        }
        return this._minorver < minorver || this._releaseno <= releaseno;
    }
}
