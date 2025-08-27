package org.apache.commons.compress.archivers.tar;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.ArchiveUtils;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/tar/TarArchiveEntry.class */
public class TarArchiveEntry implements ArchiveEntry, TarConstants {
    private static final TarArchiveEntry[] EMPTY_TAR_ARCHIVE_ENTRIES = new TarArchiveEntry[0];
    public static final long UNKNOWN = -1;
    private String name;
    private final boolean preserveAbsolutePath;
    private int mode;
    private long userId;
    private long groupId;
    private long size;
    private long modTime;
    private boolean checkSumOK;
    private byte linkFlag;
    private String linkName;
    private String magic;
    private String version;
    private String userName;
    private String groupName;
    private int devMajor;
    private int devMinor;
    private boolean isExtended;
    private long realSize;
    private boolean paxGNUSparse;
    private boolean starSparse;
    private final File file;
    private final Map<String, String> extraPaxHeaders;
    public static final int MAX_NAMELEN = 31;
    public static final int DEFAULT_DIR_MODE = 16877;
    public static final int DEFAULT_FILE_MODE = 33188;
    public static final int MILLIS_PER_SECOND = 1000;

    private TarArchiveEntry(boolean preserveAbsolutePath) {
        this.name = "";
        this.userId = 0L;
        this.groupId = 0L;
        this.size = 0L;
        this.linkName = "";
        this.magic = "ustar��";
        this.version = TarConstants.VERSION_POSIX;
        this.groupName = "";
        this.devMajor = 0;
        this.devMinor = 0;
        this.extraPaxHeaders = new HashMap();
        String user = System.getProperty("user.name", "");
        this.userName = user.length() > 31 ? user.substring(0, 31) : user;
        this.file = null;
        this.preserveAbsolutePath = preserveAbsolutePath;
    }

    public TarArchiveEntry(String name) {
        this(name, false);
    }

    public TarArchiveEntry(String name, boolean preserveAbsolutePath) {
        this(preserveAbsolutePath);
        String name2 = normalizeFileName(name, preserveAbsolutePath);
        boolean isDir = name2.endsWith("/");
        this.name = name2;
        this.mode = isDir ? DEFAULT_DIR_MODE : DEFAULT_FILE_MODE;
        this.linkFlag = isDir ? (byte) 53 : (byte) 48;
        this.modTime = new Date().getTime() / 1000;
        this.userName = "";
    }

    public TarArchiveEntry(String name, byte linkFlag) {
        this(name, linkFlag, false);
    }

    public TarArchiveEntry(String name, byte linkFlag, boolean preserveAbsolutePath) {
        this(name, preserveAbsolutePath);
        this.linkFlag = linkFlag;
        if (linkFlag == 76) {
            this.magic = TarConstants.MAGIC_GNU;
            this.version = TarConstants.VERSION_GNU_SPACE;
        }
    }

    public TarArchiveEntry(File file) {
        this(file, file.getPath());
    }

    public TarArchiveEntry(File file, String fileName) {
        this.name = "";
        this.userId = 0L;
        this.groupId = 0L;
        this.size = 0L;
        this.linkName = "";
        this.magic = "ustar��";
        this.version = TarConstants.VERSION_POSIX;
        this.groupName = "";
        this.devMajor = 0;
        this.devMinor = 0;
        this.extraPaxHeaders = new HashMap();
        String normalizedName = normalizeFileName(fileName, false);
        this.file = file;
        if (file.isDirectory()) {
            this.mode = DEFAULT_DIR_MODE;
            this.linkFlag = (byte) 53;
            int nameLength = normalizedName.length();
            if (nameLength == 0 || normalizedName.charAt(nameLength - 1) != '/') {
                this.name = normalizedName + "/";
            } else {
                this.name = normalizedName;
            }
        } else {
            this.mode = DEFAULT_FILE_MODE;
            this.linkFlag = (byte) 48;
            this.size = file.length();
            this.name = normalizedName;
        }
        this.modTime = file.lastModified() / 1000;
        this.userName = "";
        this.preserveAbsolutePath = false;
    }

    public TarArchiveEntry(byte[] headerBuf) {
        this(false);
        parseTarHeader(headerBuf);
    }

    public TarArchiveEntry(byte[] headerBuf, ZipEncoding encoding) throws IOException {
        this(headerBuf, encoding, false);
    }

    public TarArchiveEntry(byte[] headerBuf, ZipEncoding encoding, boolean lenient) throws IOException {
        this(false);
        parseTarHeader(headerBuf, encoding, false, lenient);
    }

    public boolean equals(TarArchiveEntry it) {
        return it != null && getName().equals(it.getName());
    }

    public boolean equals(Object it) {
        if (it == null || getClass() != it.getClass()) {
            return false;
        }
        return equals((TarArchiveEntry) it);
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean isDescendent(TarArchiveEntry desc) {
        return desc.getName().startsWith(getName());
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = normalizeFileName(name, this.preserveAbsolutePath);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getLinkName() {
        return this.linkName;
    }

    public void setLinkName(String link) {
        this.linkName = link;
    }

    @Deprecated
    public int getUserId() {
        return (int) (this.userId & (-1));
    }

    public void setUserId(int userId) {
        setUserId(userId);
    }

    public long getLongUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Deprecated
    public int getGroupId() {
        return (int) (this.groupId & (-1));
    }

    public void setGroupId(int groupId) {
        setGroupId(groupId);
    }

    public long getLongGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setIds(int userId, int groupId) {
        setUserId(userId);
        setGroupId(groupId);
    }

    public void setNames(String userName, String groupName) {
        setUserName(userName);
        setGroupName(groupName);
    }

    public void setModTime(long time) {
        this.modTime = time / 1000;
    }

    public void setModTime(Date time) {
        this.modTime = time.getTime() / 1000;
    }

    public Date getModTime() {
        return new Date(this.modTime * 1000);
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public Date getLastModifiedDate() {
        return getModTime();
    }

    public boolean isCheckSumOK() {
        return this.checkSumOK;
    }

    public File getFile() {
        return this.file;
    }

    public int getMode() {
        return this.mode;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size is out of range: " + size);
        }
        this.size = size;
    }

    public int getDevMajor() {
        return this.devMajor;
    }

    public void setDevMajor(int devNo) {
        if (devNo < 0) {
            throw new IllegalArgumentException("Major device number is out of range: " + devNo);
        }
        this.devMajor = devNo;
    }

    public int getDevMinor() {
        return this.devMinor;
    }

    public void setDevMinor(int devNo) {
        if (devNo < 0) {
            throw new IllegalArgumentException("Minor device number is out of range: " + devNo);
        }
        this.devMinor = devNo;
    }

    public boolean isExtended() {
        return this.isExtended;
    }

    public long getRealSize() {
        return this.realSize;
    }

    public boolean isGNUSparse() {
        return isOldGNUSparse() || isPaxGNUSparse();
    }

    public boolean isOldGNUSparse() {
        return this.linkFlag == 83;
    }

    public boolean isPaxGNUSparse() {
        return this.paxGNUSparse;
    }

    public boolean isStarSparse() {
        return this.starSparse;
    }

    public boolean isGNULongLinkEntry() {
        return this.linkFlag == 75;
    }

    public boolean isGNULongNameEntry() {
        return this.linkFlag == 76;
    }

    public boolean isPaxHeader() {
        return this.linkFlag == 120 || this.linkFlag == 88;
    }

    public boolean isGlobalPaxHeader() {
        return this.linkFlag == 103;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public boolean isDirectory() {
        if (this.file != null) {
            return this.file.isDirectory();
        }
        if (this.linkFlag == 53) {
            return true;
        }
        return (isPaxHeader() || isGlobalPaxHeader() || !getName().endsWith("/")) ? false : true;
    }

    public boolean isFile() {
        if (this.file != null) {
            return this.file.isFile();
        }
        return this.linkFlag == 0 || this.linkFlag == 48 || !getName().endsWith("/");
    }

    public boolean isSymbolicLink() {
        return this.linkFlag == 50;
    }

    public boolean isLink() {
        return this.linkFlag == 49;
    }

    public boolean isCharacterDevice() {
        return this.linkFlag == 51;
    }

    public boolean isBlockDevice() {
        return this.linkFlag == 52;
    }

    public boolean isFIFO() {
        return this.linkFlag == 54;
    }

    public boolean isSparse() {
        return isGNUSparse() || isStarSparse();
    }

    public Map<String, String> getExtraPaxHeaders() {
        return Collections.unmodifiableMap(this.extraPaxHeaders);
    }

    public void clearExtraPaxHeaders() {
        this.extraPaxHeaders.clear();
    }

    public void addPaxHeader(String name, String value) {
        processPaxHeader(name, value);
    }

    public String getExtraPaxHeader(String name) {
        return this.extraPaxHeaders.get(name);
    }

    void updateEntryFromPaxHeaders(Map<String, String> headers) {
        for (Map.Entry<String, String> ent : headers.entrySet()) {
            String key = ent.getKey();
            String val = ent.getValue();
            processPaxHeader(key, val, headers);
        }
    }

    private void processPaxHeader(String key, String val) {
        processPaxHeader(key, val, this.extraPaxHeaders);
    }

    private void processPaxHeader(String key, String val, Map<String, String> headers) {
        switch (key) {
            case "path":
                setName(val);
                break;
            case "linkpath":
                setLinkName(val);
                break;
            case "gid":
                setGroupId(Long.parseLong(val));
                break;
            case "gname":
                setGroupName(val);
                break;
            case "uid":
                setUserId(Long.parseLong(val));
                break;
            case "uname":
                setUserName(val);
                break;
            case "size":
                setSize(Long.parseLong(val));
                break;
            case "mtime":
                setModTime((long) (Double.parseDouble(val) * 1000.0d));
                break;
            case "SCHILY.devminor":
                setDevMinor(Integer.parseInt(val));
                break;
            case "SCHILY.devmajor":
                setDevMajor(Integer.parseInt(val));
                break;
            case "GNU.sparse.size":
                fillGNUSparse0xData(headers);
                break;
            case "GNU.sparse.realsize":
                fillGNUSparse1xData(headers);
                break;
            case "SCHILY.filetype":
                if ("sparse".equals(val)) {
                    fillStarSparseData(headers);
                    break;
                }
                break;
            default:
                this.extraPaxHeaders.put(key, val);
                break;
        }
    }

    public TarArchiveEntry[] getDirectoryEntries() {
        if (this.file == null || !this.file.isDirectory()) {
            return EMPTY_TAR_ARCHIVE_ENTRIES;
        }
        String[] list = this.file.list();
        if (list == null) {
            return EMPTY_TAR_ARCHIVE_ENTRIES;
        }
        TarArchiveEntry[] result = new TarArchiveEntry[list.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new TarArchiveEntry(new File(this.file, list[i]));
        }
        return result;
    }

    public void writeEntryHeader(byte[] outbuf) {
        try {
            writeEntryHeader(outbuf, TarUtils.DEFAULT_ENCODING, false);
        } catch (IOException e) {
            try {
                writeEntryHeader(outbuf, TarUtils.FALLBACK_ENCODING, false);
            } catch (IOException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    public void writeEntryHeader(byte[] outbuf, ZipEncoding encoding, boolean starMode) throws IOException {
        int offset = writeEntryHeaderField(this.modTime, outbuf, writeEntryHeaderField(this.size, outbuf, writeEntryHeaderField(this.groupId, outbuf, writeEntryHeaderField(this.userId, outbuf, writeEntryHeaderField(this.mode, outbuf, TarUtils.formatNameBytes(this.name, outbuf, 0, 100, encoding), 8, starMode), 8, starMode), 8, starMode), 12, starMode), 12, starMode);
        for (int c = 0; c < 8; c++) {
            int i = offset;
            offset++;
            outbuf[i] = 32;
        }
        outbuf[offset] = this.linkFlag;
        int offset2 = writeEntryHeaderField(this.devMinor, outbuf, writeEntryHeaderField(this.devMajor, outbuf, TarUtils.formatNameBytes(this.groupName, outbuf, TarUtils.formatNameBytes(this.userName, outbuf, TarUtils.formatNameBytes(this.version, outbuf, TarUtils.formatNameBytes(this.magic, outbuf, TarUtils.formatNameBytes(this.linkName, outbuf, offset + 1, 100, encoding), 6), 2), 32, encoding), 32, encoding), 8, starMode), 8, starMode);
        while (offset2 < outbuf.length) {
            int i2 = offset2;
            offset2++;
            outbuf[i2] = 0;
        }
        long chk = TarUtils.computeCheckSum(outbuf);
        TarUtils.formatCheckSumOctalBytes(chk, outbuf, offset, 8);
    }

    private int writeEntryHeaderField(long value, byte[] outbuf, int offset, int length, boolean starMode) {
        if (!starMode && (value < 0 || value >= (1 << (3 * (length - 1))))) {
            return TarUtils.formatLongOctalBytes(0L, outbuf, offset, length);
        }
        return TarUtils.formatLongOctalOrBinaryBytes(value, outbuf, offset, length);
    }

    public void parseTarHeader(byte[] header) {
        try {
            parseTarHeader(header, TarUtils.DEFAULT_ENCODING);
        } catch (IOException e) {
            try {
                parseTarHeader(header, TarUtils.DEFAULT_ENCODING, true, false);
            } catch (IOException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    public void parseTarHeader(byte[] header, ZipEncoding encoding) throws IOException {
        parseTarHeader(header, encoding, false, false);
    }

    private void parseTarHeader(byte[] header, ZipEncoding encoding, boolean oldStyle, boolean lenient) throws IOException {
        String name;
        int offset;
        String name2;
        String name3;
        if (oldStyle) {
            name = TarUtils.parseName(header, 0, 100);
        } else {
            name = TarUtils.parseName(header, 0, 100, encoding);
        }
        this.name = name;
        int offset2 = 0 + 100;
        this.mode = (int) parseOctalOrBinary(header, offset2, 8, lenient);
        this.userId = (int) parseOctalOrBinary(header, r12, 8, lenient);
        this.groupId = (int) parseOctalOrBinary(header, r12, 8, lenient);
        int offset3 = offset2 + 8 + 8 + 8;
        this.size = TarUtils.parseOctalOrBinary(header, offset3, 12);
        int offset4 = offset3 + 12;
        this.modTime = parseOctalOrBinary(header, offset4, 12, lenient);
        this.checkSumOK = TarUtils.verifyCheckSum(header);
        int offset5 = offset4 + 12 + 8;
        int offset6 = offset5 + 1;
        this.linkFlag = header[offset5];
        this.linkName = oldStyle ? TarUtils.parseName(header, offset6, 100) : TarUtils.parseName(header, offset6, 100, encoding);
        int offset7 = offset6 + 100;
        this.magic = TarUtils.parseName(header, offset7, 6);
        int offset8 = offset7 + 6;
        this.version = TarUtils.parseName(header, offset8, 2);
        int offset9 = offset8 + 2;
        this.userName = oldStyle ? TarUtils.parseName(header, offset9, 32) : TarUtils.parseName(header, offset9, 32, encoding);
        int offset10 = offset9 + 32;
        this.groupName = oldStyle ? TarUtils.parseName(header, offset10, 32) : TarUtils.parseName(header, offset10, 32, encoding);
        int offset11 = offset10 + 32;
        if (this.linkFlag == 51 || this.linkFlag == 52) {
            this.devMajor = (int) parseOctalOrBinary(header, offset11, 8, lenient);
            int offset12 = offset11 + 8;
            this.devMinor = (int) parseOctalOrBinary(header, offset12, 8, lenient);
            offset = offset12 + 8;
        } else {
            offset = offset11 + 16;
        }
        int type = evaluateType(header);
        switch (type) {
            case 2:
                int offset13 = offset + 12 + 12 + 12 + 4 + 1 + 96;
                this.isExtended = TarUtils.parseBoolean(header, offset13);
                int offset14 = offset13 + 1;
                this.realSize = TarUtils.parseOctal(header, offset14, 12);
                int i = offset14 + 12;
                break;
            case 3:
            default:
                if (oldStyle) {
                    name2 = TarUtils.parseName(header, offset, 155);
                } else {
                    name2 = TarUtils.parseName(header, offset, 155, encoding);
                }
                String prefix = name2;
                if (isDirectory() && !this.name.endsWith("/")) {
                    this.name += "/";
                }
                if (prefix.length() > 0) {
                    this.name = prefix + "/" + this.name;
                    break;
                }
                break;
            case 4:
                if (oldStyle) {
                    name3 = TarUtils.parseName(header, offset, 131);
                } else {
                    name3 = TarUtils.parseName(header, offset, 131, encoding);
                }
                String xstarPrefix = name3;
                if (xstarPrefix.length() > 0) {
                    this.name = xstarPrefix + "/" + this.name;
                    break;
                }
                break;
        }
    }

    private long parseOctalOrBinary(byte[] header, int offset, int length, boolean lenient) {
        if (lenient) {
            try {
                return TarUtils.parseOctalOrBinary(header, offset, length);
            } catch (IllegalArgumentException e) {
                return -1L;
            }
        }
        return TarUtils.parseOctalOrBinary(header, offset, length);
    }

    private static String normalizeFileName(String fileName, boolean preserveAbsolutePath) {
        String fileName2;
        String osname;
        int colon;
        if (!preserveAbsolutePath && (osname = System.getProperty("os.name").toLowerCase(Locale.ENGLISH)) != null) {
            if (osname.startsWith("windows")) {
                if (fileName.length() > 2) {
                    char ch1 = fileName.charAt(0);
                    char ch2 = fileName.charAt(1);
                    if (ch2 == ':' && ((ch1 >= 'a' && ch1 <= 'z') || (ch1 >= 'A' && ch1 <= 'Z'))) {
                        fileName = fileName.substring(2);
                    }
                }
            } else if (osname.contains("netware") && (colon = fileName.indexOf(58)) != -1) {
                fileName = fileName.substring(colon + 1);
            }
        }
        String strReplace = fileName.replace(File.separatorChar, '/');
        while (true) {
            fileName2 = strReplace;
            if (preserveAbsolutePath || !fileName2.startsWith("/")) {
                break;
            }
            strReplace = fileName2.substring(1);
        }
        return fileName2;
    }

    private int evaluateType(byte[] header) {
        if (ArchiveUtils.matchAsciiBuffer(TarConstants.MAGIC_GNU, header, 257, 6)) {
            return 2;
        }
        if (ArchiveUtils.matchAsciiBuffer("ustar��", header, 257, 6)) {
            if (ArchiveUtils.matchAsciiBuffer(TarConstants.MAGIC_XSTAR, header, 508, 4)) {
                return 4;
            }
            return 3;
        }
        return 0;
    }

    void fillGNUSparse0xData(Map<String, String> headers) {
        this.paxGNUSparse = true;
        this.realSize = Integer.parseInt(headers.get("GNU.sparse.size"));
        if (headers.containsKey("GNU.sparse.name")) {
            this.name = headers.get("GNU.sparse.name");
        }
    }

    void fillGNUSparse1xData(Map<String, String> headers) {
        this.paxGNUSparse = true;
        this.realSize = Integer.parseInt(headers.get("GNU.sparse.realsize"));
        this.name = headers.get("GNU.sparse.name");
    }

    void fillStarSparseData(Map<String, String> headers) {
        this.starSparse = true;
        if (headers.containsKey("SCHILY.realsize")) {
            this.realSize = Long.parseLong(headers.get("SCHILY.realsize"));
        }
    }
}
