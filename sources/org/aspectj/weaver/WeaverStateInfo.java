package org.aspectj.weaver;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipInputStream;
import org.aspectj.bridge.IMessage;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/WeaverStateInfo.class */
public class WeaverStateInfo {
    private List<Entry> typeMungers;
    private boolean oldStyle;
    private boolean reweavable;
    private boolean reweavableCompressedMode;
    private boolean reweavableDiffMode;
    private Set<String> aspectsAffectingType;
    private byte[] unwovenClassFile;
    private static boolean reweavableDefault = true;
    private static boolean reweavableCompressedModeDefault = false;
    private static boolean reweavableDiffModeDefault = true;
    private static byte[] key = {-51, 34, 105, 56, -34, 65, 45, 78, -26, 125, 114, 97, 98, 1, -1, -42};
    private boolean unwovenClassFileIsADiff;
    int compressionEnabled;
    private static final int UNTOUCHED = 0;
    private static final int WOVEN = 2;
    private static final int EXTENDED = 3;
    private static final byte REWEAVABLE_BIT = 16;
    private static final byte REWEAVABLE_COMPRESSION_BIT = 32;
    private static final byte REWEAVABLE_DIFF_BIT = 64;

    private void checkCompressionEnabled() {
        if (this.compressionEnabled == 0) {
            this.compressionEnabled = 1;
            try {
                String value = System.getProperty("aspectj.compression.weaverstateinfo", "false");
                if (value.equalsIgnoreCase("true")) {
                    System.out.println("ASPECTJ: aspectj.compression.weaverstateinfo=true: compressing weaverstateinfo");
                    this.compressionEnabled = 2;
                }
            } catch (Throwable th) {
            }
        }
    }

    private WeaverStateInfo() {
        this.unwovenClassFileIsADiff = false;
        this.compressionEnabled = 0;
    }

    public WeaverStateInfo(boolean reweavable) {
        this(new ArrayList(), false, reweavable, reweavableCompressedModeDefault, reweavableDiffModeDefault);
    }

    private WeaverStateInfo(List<Entry> typeMungers, boolean oldStyle, boolean reweavableMode, boolean reweavableCompressedMode, boolean reweavableDiffMode) {
        this.unwovenClassFileIsADiff = false;
        this.compressionEnabled = 0;
        this.typeMungers = typeMungers;
        this.oldStyle = oldStyle;
        this.reweavable = reweavableMode;
        this.reweavableCompressedMode = reweavableCompressedMode;
        this.reweavableDiffMode = reweavableMode ? reweavableDiffMode : false;
        this.aspectsAffectingType = new HashSet();
        this.unwovenClassFile = null;
    }

    public static void setReweavableModeDefaults(boolean mode, boolean compress, boolean diff) {
        reweavableDefault = mode;
        reweavableCompressedModeDefault = compress;
        reweavableDiffModeDefault = diff;
    }

    public static final WeaverStateInfo read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        UnresolvedType unresolvedTypeForSignature;
        byte b = s.readByte();
        boolean isReweavable = (b & 16) != 0;
        if (isReweavable) {
            b = (byte) (b - 16);
        }
        boolean isReweavableCompressed = (b & 32) != 0;
        if (isReweavableCompressed) {
            b = (byte) (b - 32);
        }
        boolean isReweavableDiff = (b & 64) != 0;
        if (isReweavableDiff) {
            b = (byte) (b - 64);
        }
        switch (b) {
            case 0:
                throw new RuntimeException("unexpected UNWOVEN");
            case 1:
            default:
                throw new RuntimeException("bad WeaverState.Kind: " + ((int) b) + ".  File was :" + (context == null ? "unknown" : context.makeSourceLocation(0, 0).toString()));
            case 2:
                return new WeaverStateInfo(Collections.emptyList(), true, isReweavable, isReweavableCompressed, isReweavableDiff);
            case 3:
                boolean isCompressed = false;
                if (s.isAtLeast169()) {
                    isCompressed = s.readBoolean();
                }
                int n = s.readShort();
                List<Entry> l = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    if (isCompressed) {
                        int cpIndex = s.readShort();
                        String signature = s.readUtf8(cpIndex);
                        if (signature.charAt(0) == '@') {
                            unresolvedTypeForSignature = ResolvedType.MISSING;
                        } else {
                            unresolvedTypeForSignature = UnresolvedType.forSignature(signature);
                        }
                    } else {
                        unresolvedTypeForSignature = UnresolvedType.read(s);
                    }
                    UnresolvedType aspectType = unresolvedTypeForSignature;
                    ResolvedTypeMunger typeMunger = ResolvedTypeMunger.read(s, context);
                    l.add(new Entry(aspectType, typeMunger));
                }
                WeaverStateInfo wsi = new WeaverStateInfo(l, false, isReweavable, isReweavableCompressed, isReweavableDiff);
                readAnyReweavableData(wsi, s, isCompressed);
                return wsi;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/WeaverStateInfo$Entry.class */
    private static class Entry {
        public UnresolvedType aspectType;
        public ResolvedTypeMunger typeMunger;

        public Entry(UnresolvedType aspectType, ResolvedTypeMunger typeMunger) {
            this.aspectType = aspectType;
            this.typeMunger = typeMunger;
        }

        public String toString() {
            return "<" + this.aspectType + ", " + this.typeMunger + ">";
        }
    }

    public void write(CompressingDataOutputStream s) throws IOException {
        checkCompressionEnabled();
        if (this.oldStyle || this.reweavableCompressedMode) {
            throw new RuntimeException("shouldn't be writing this");
        }
        byte weaverStateInfoKind = 3;
        if (this.reweavable) {
            weaverStateInfoKind = (byte) (3 | 16);
        }
        if (this.reweavableDiffMode) {
            s.write(key);
            weaverStateInfoKind = (byte) (weaverStateInfoKind | 64);
        }
        s.writeByte(weaverStateInfoKind);
        try {
            s.compressionEnabled = this.compressionEnabled == 2;
            s.writeBoolean(s.canCompress());
            int n = this.typeMungers.size();
            s.writeShort(n);
            for (Entry e : this.typeMungers) {
                if (s.canCompress()) {
                    s.writeCompressedSignature(e.aspectType.getSignature());
                } else {
                    e.aspectType.write(s);
                }
                e.typeMunger.write(s);
            }
            writeAnyReweavableData(this, s, s.canCompress());
            s.compressionEnabled = true;
        } catch (Throwable th) {
            s.compressionEnabled = true;
            throw th;
        }
    }

    public void addConcreteMunger(ConcreteTypeMunger munger) {
        this.typeMungers.add(new Entry(munger.getAspectType(), munger.getMunger()));
    }

    public String toString() {
        return "WeaverStateInfo(aspectsAffectingType=" + this.aspectsAffectingType + "," + this.typeMungers + ", " + this.oldStyle + ")";
    }

    public List<ConcreteTypeMunger> getTypeMungers(ResolvedType onType) {
        World world = onType.getWorld();
        List<ConcreteTypeMunger> ret = new ArrayList<>();
        for (Entry entry : this.typeMungers) {
            ResolvedType aspectType = world.resolve(entry.aspectType, true);
            if (aspectType.isMissing()) {
                world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.ASPECT_NEEDED, entry.aspectType, onType), onType.getSourceLocation(), null);
            } else {
                ret.add(new TemporaryTypeMunger(entry.typeMunger, aspectType));
            }
        }
        return ret;
    }

    public boolean isOldStyle() {
        return this.oldStyle;
    }

    public byte[] getUnwovenClassFileData(byte[] wovenClassFile) {
        if (this.unwovenClassFileIsADiff) {
            this.unwovenClassFile = applyDiff(wovenClassFile, this.unwovenClassFile);
            this.unwovenClassFileIsADiff = false;
        }
        return this.unwovenClassFile;
    }

    public void setUnwovenClassFileData(byte[] data) {
        this.unwovenClassFile = data;
    }

    public boolean isReweavable() {
        return this.reweavable;
    }

    public void setReweavable(boolean rw) {
        this.reweavable = rw;
    }

    public void addAspectsAffectingType(Collection<String> aspects) {
        this.aspectsAffectingType.addAll(aspects);
    }

    public void addAspectAffectingType(String aspectSignature) {
        this.aspectsAffectingType.add(aspectSignature);
    }

    public Set<String> getAspectsAffectingType() {
        return this.aspectsAffectingType;
    }

    private static void readAnyReweavableData(WeaverStateInfo wsi, VersionedDataInputStream s, boolean compressed) throws IOException {
        byte[] classData;
        int bytesToGo;
        String str;
        if (wsi.isReweavable()) {
            int numberAspectsAffectingType = s.readShort();
            for (int i = 0; i < numberAspectsAffectingType; i++) {
                if (compressed) {
                    str = s.readSignature();
                } else {
                    str = s.readUTF();
                    if (s.getMajorVersion() < 7) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(StandardRoles.L).append(str.replace('.', '/')).append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                        str = sb.toString();
                    }
                }
                wsi.addAspectAffectingType(str);
            }
            int unwovenClassFileSize = s.readInt();
            if (wsi.reweavableCompressedMode) {
                classData = new byte[unwovenClassFileSize];
                ZipInputStream zis = new ZipInputStream(s);
                zis.getNextEntry();
                int current = 0;
                int i2 = unwovenClassFileSize;
                while (true) {
                    bytesToGo = i2;
                    if (bytesToGo <= 0) {
                        break;
                    }
                    int amount = zis.read(classData, current, bytesToGo);
                    current += amount;
                    i2 = bytesToGo - amount;
                }
                zis.closeEntry();
                if (bytesToGo != 0) {
                    throw new IOException("ERROR whilst reading compressed reweavable data, expected " + unwovenClassFileSize + " bytes, only found " + current);
                }
            } else {
                classData = new byte[unwovenClassFileSize];
                int bytesread = s.read(classData);
                if (bytesread != unwovenClassFileSize) {
                    throw new IOException("ERROR whilst reading reweavable data, expected " + unwovenClassFileSize + " bytes, only found " + bytesread);
                }
            }
            wsi.unwovenClassFileIsADiff = wsi.reweavableDiffMode;
            wsi.setUnwovenClassFileData(classData);
        }
    }

    public byte[] replaceKeyWithDiff(byte[] wovenClassFile) throws IOException {
        if (this.reweavableDiffMode) {
            ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
            DataOutputStream s = new DataOutputStream(arrayStream);
            int endOfKey = findEndOfKey(wovenClassFile);
            int startOfKey = endOfKey - key.length;
            int oldLengthLocation = startOfKey - 4;
            int oldLength = readInt(wovenClassFile, oldLengthLocation);
            byte[] wovenClassFile2 = deleteInArray(wovenClassFile, startOfKey, endOfKey);
            byte[] wovenClassFileUpToWSI = new byte[oldLengthLocation];
            System.arraycopy(wovenClassFile2, 0, wovenClassFileUpToWSI, 0, oldLengthLocation);
            byte[] diff = generateDiff(wovenClassFileUpToWSI, this.unwovenClassFile);
            try {
                s.writeInt(diff.length);
                s.write(diff);
            } catch (IOException e) {
            }
            byte[] diff2 = arrayStream.toByteArray();
            int newLength = (oldLength - key.length) + diff2.length;
            byte[] newLengthBytes = serializeInt(newLength);
            wovenClassFile2[oldLengthLocation] = newLengthBytes[0];
            wovenClassFile2[oldLengthLocation + 1] = newLengthBytes[1];
            wovenClassFile2[oldLengthLocation + 2] = newLengthBytes[2];
            wovenClassFile2[oldLengthLocation + 3] = newLengthBytes[3];
            wovenClassFile = insertArray(diff2, wovenClassFile2, ((oldLengthLocation + 4) + oldLength) - key.length);
        }
        return wovenClassFile;
    }

    private static final int findEndOfKey(byte[] wovenClassFile) {
        for (int i = wovenClassFile.length - 1; i > 0; i--) {
            if (endOfKeyHere(wovenClassFile, i)) {
                return i + 1;
            }
        }
        throw new RuntimeException("key not found in wovenClassFile");
    }

    private static final boolean endOfKeyHere(byte[] lookIn, int i) {
        for (int j = 0; j < key.length; j++) {
            if (key[(key.length - 1) - j] != lookIn[i - j]) {
                return false;
            }
        }
        return true;
    }

    private static final byte[] insertArray(byte[] toInsert, byte[] original, int offset) {
        byte[] result = new byte[original.length + toInsert.length];
        System.arraycopy(original, 0, result, 0, offset);
        System.arraycopy(toInsert, 0, result, offset, toInsert.length);
        System.arraycopy(original, offset, result, offset + toInsert.length, original.length - offset);
        return result;
    }

    private static final int readInt(byte[] a, int offset) throws IOException {
        ByteArrayInputStream b = new ByteArrayInputStream(a, offset, 4);
        DataInputStream d = new DataInputStream(b);
        try {
            int length = d.readInt();
            return length;
        } catch (IOException e) {
            throw new RuntimeException("readInt called with a bad array or offset");
        }
    }

    private static final byte[] deleteInArray(byte[] a, int start, int end) {
        int lengthToDelete = end - start;
        byte[] result = new byte[a.length - lengthToDelete];
        System.arraycopy(a, 0, result, 0, start);
        System.arraycopy(a, end, result, start, a.length - end);
        return result;
    }

    byte[] generateDiff(byte[] wovenClassFile, byte[] unWovenClassFile) throws IOException {
        int lookingAt = 10;
        int shorterLength = wovenClassFile.length < unWovenClassFile.length ? wovenClassFile.length : unWovenClassFile.length;
        while (lookingAt < shorterLength && wovenClassFile[lookingAt] == unWovenClassFile[lookingAt]) {
            lookingAt++;
        }
        int lengthInCommon = lookingAt - 10;
        byte[] diff = new byte[(unWovenClassFile.length - 4) - lengthInCommon];
        diff[0] = unWovenClassFile[8];
        diff[1] = unWovenClassFile[9];
        byte[] lengthInCommonBytes = serializeInt(lengthInCommon);
        diff[2] = lengthInCommonBytes[0];
        diff[3] = lengthInCommonBytes[1];
        diff[4] = lengthInCommonBytes[2];
        diff[5] = lengthInCommonBytes[3];
        System.arraycopy(unWovenClassFile, 10 + lengthInCommon, diff, 6, diff.length - 6);
        return diff;
    }

    byte[] applyDiff(byte[] wovenClassFile, byte[] diff) throws IOException {
        int lengthInCommon = readInt(diff, 2);
        byte[] unWovenClassFile = new byte[4 + diff.length + lengthInCommon];
        System.arraycopy(wovenClassFile, 0, unWovenClassFile, 0, 8);
        unWovenClassFile[8] = diff[0];
        unWovenClassFile[9] = diff[1];
        System.arraycopy(wovenClassFile, 10, unWovenClassFile, 10, lengthInCommon);
        System.arraycopy(diff, 6, unWovenClassFile, 10 + lengthInCommon, diff.length - 6);
        return unWovenClassFile;
    }

    private byte[] serializeInt(int i) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(i);
        } catch (IOException e) {
        }
        return bos.toByteArray();
    }

    private static void writeAnyReweavableData(WeaverStateInfo wsi, CompressingDataOutputStream s, boolean compress) throws IOException {
        if (wsi.isReweavable()) {
            s.writeShort(wsi.aspectsAffectingType.size());
            for (String type : wsi.aspectsAffectingType) {
                if (compress) {
                    s.writeCompressedSignature(type);
                } else {
                    s.writeUTF(type);
                }
            }
            byte[] data = wsi.unwovenClassFile;
            if (!wsi.reweavableDiffMode) {
                s.writeInt(data.length);
                s.write(wsi.unwovenClassFile);
            }
        }
    }

    public boolean isAspectAlreadyApplied(ResolvedType someAspect) {
        String someAspectSignature = someAspect.getSignature();
        for (String aspectSignature : this.aspectsAffectingType) {
            if (aspectSignature.equals(someAspectSignature)) {
                return true;
            }
        }
        return false;
    }
}
