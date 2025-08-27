package net.dongliu.apk.parser.parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.dongliu.apk.parser.exception.ParserException;
import net.dongliu.apk.parser.struct.ChunkHeader;
import net.dongliu.apk.parser.struct.StringPool;
import net.dongliu.apk.parser.struct.StringPoolHeader;
import net.dongliu.apk.parser.struct.resource.LibraryHeader;
import net.dongliu.apk.parser.struct.resource.NullHeader;
import net.dongliu.apk.parser.struct.resource.PackageHeader;
import net.dongliu.apk.parser.struct.resource.ResTableConfig;
import net.dongliu.apk.parser.struct.resource.ResourcePackage;
import net.dongliu.apk.parser.struct.resource.ResourceTable;
import net.dongliu.apk.parser.struct.resource.ResourceTableHeader;
import net.dongliu.apk.parser.struct.resource.TypeHeader;
import net.dongliu.apk.parser.struct.resource.TypeSpecHeader;
import net.dongliu.apk.parser.utils.Buffers;
import net.dongliu.apk.parser.utils.Pair;
import net.dongliu.apk.parser.utils.ParseUtils;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/ResourceTableParser.class */
public class ResourceTableParser {
    private ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    private StringPool stringPool;
    private ByteBuffer buffer;
    private ResourceTable resourceTable;
    private Set<Locale> locales;

    public ResourceTableParser(ByteBuffer buffer) {
        this.buffer = buffer.duplicate();
        this.buffer.order(this.byteOrder);
        this.locales = new HashSet();
    }

    public void parse() {
        ResourceTableHeader resourceTableHeader = (ResourceTableHeader) readChunkHeader();
        this.stringPool = ParseUtils.readStringPool(this.buffer, (StringPoolHeader) readChunkHeader());
        this.resourceTable = new ResourceTable();
        this.resourceTable.setStringPool(this.stringPool);
        PackageHeader packageHeader = (PackageHeader) readChunkHeader();
        for (int i = 0; i < resourceTableHeader.getPackageCount(); i++) {
            Pair<ResourcePackage, PackageHeader> pair = readPackage(packageHeader);
            this.resourceTable.addPackage(pair.getLeft());
            packageHeader = pair.getRight();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0298, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private net.dongliu.apk.parser.utils.Pair<net.dongliu.apk.parser.struct.resource.ResourcePackage, net.dongliu.apk.parser.struct.resource.PackageHeader> readPackage(net.dongliu.apk.parser.struct.resource.PackageHeader r7) {
        /*
            Method dump skipped, instructions count: 665
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.dongliu.apk.parser.parser.ResourceTableParser.readPackage(net.dongliu.apk.parser.struct.resource.PackageHeader):net.dongliu.apk.parser.utils.Pair");
    }

    private ChunkHeader readChunkHeader() {
        long begin = this.buffer.position();
        int chunkType = Buffers.readUShort(this.buffer);
        int headerSize = Buffers.readUShort(this.buffer);
        int chunkSize = (int) Buffers.readUInt(this.buffer);
        switch (chunkType) {
            case 0:
                Buffers.position(this.buffer, begin + headerSize);
                return new NullHeader(headerSize, chunkSize);
            case 1:
                StringPoolHeader stringPoolHeader = new StringPoolHeader(headerSize, chunkSize);
                stringPoolHeader.setStringCount(Buffers.readUInt(this.buffer));
                stringPoolHeader.setStyleCount(Buffers.readUInt(this.buffer));
                stringPoolHeader.setFlags(Buffers.readUInt(this.buffer));
                stringPoolHeader.setStringsStart(Buffers.readUInt(this.buffer));
                stringPoolHeader.setStylesStart(Buffers.readUInt(this.buffer));
                Buffers.position(this.buffer, begin + headerSize);
                return stringPoolHeader;
            case 2:
                ResourceTableHeader resourceTableHeader = new ResourceTableHeader(headerSize, chunkSize);
                resourceTableHeader.setPackageCount(Buffers.readUInt(this.buffer));
                Buffers.position(this.buffer, begin + headerSize);
                return resourceTableHeader;
            case 512:
                PackageHeader packageHeader = new PackageHeader(headerSize, chunkSize);
                packageHeader.setId(Buffers.readUInt(this.buffer));
                packageHeader.setName(ParseUtils.readStringUTF16(this.buffer, 128));
                packageHeader.setTypeStrings(Buffers.readUInt(this.buffer));
                packageHeader.setLastPublicType(Buffers.readUInt(this.buffer));
                packageHeader.setKeyStrings(Buffers.readUInt(this.buffer));
                packageHeader.setLastPublicKey(Buffers.readUInt(this.buffer));
                Buffers.position(this.buffer, begin + headerSize);
                return packageHeader;
            case 513:
                TypeHeader typeHeader = new TypeHeader(headerSize, chunkSize);
                typeHeader.setId(Buffers.readUByte(this.buffer));
                typeHeader.setRes0(Buffers.readUByte(this.buffer));
                typeHeader.setRes1(Buffers.readUShort(this.buffer));
                typeHeader.setEntryCount(Buffers.readUInt(this.buffer));
                typeHeader.setEntriesStart(Buffers.readUInt(this.buffer));
                typeHeader.setConfig(readResTableConfig());
                Buffers.position(this.buffer, begin + headerSize);
                return typeHeader;
            case 514:
                TypeSpecHeader typeSpecHeader = new TypeSpecHeader(headerSize, chunkSize);
                typeSpecHeader.setId(Buffers.readUByte(this.buffer));
                typeSpecHeader.setRes0(Buffers.readUByte(this.buffer));
                typeSpecHeader.setRes1(Buffers.readUShort(this.buffer));
                typeSpecHeader.setEntryCount(Buffers.readUInt(this.buffer));
                Buffers.position(this.buffer, begin + headerSize);
                return typeSpecHeader;
            case 515:
                LibraryHeader libraryHeader = new LibraryHeader(headerSize, chunkSize);
                libraryHeader.setCount(Buffers.readUInt(this.buffer));
                Buffers.position(this.buffer, begin + headerSize);
                return libraryHeader;
            default:
                throw new ParserException("Unexpected chunk Type: 0x" + Integer.toHexString(chunkType));
        }
    }

    private ResTableConfig readResTableConfig() {
        long beginPos = this.buffer.position();
        ResTableConfig config = new ResTableConfig();
        long size = Buffers.readUInt(this.buffer);
        config.setMcc(this.buffer.getShort());
        config.setMnc(this.buffer.getShort());
        config.setLanguage(new String(Buffers.readBytes(this.buffer, 2)).replace(ThumbnailParameter.DETERMINE_FORMAT, ""));
        config.setCountry(new String(Buffers.readBytes(this.buffer, 2)).replace(ThumbnailParameter.DETERMINE_FORMAT, ""));
        config.setOrientation(Buffers.readUByte(this.buffer));
        config.setTouchscreen(Buffers.readUByte(this.buffer));
        config.setDensity(Buffers.readUShort(this.buffer));
        long endPos = this.buffer.position();
        Buffers.skip(this.buffer, (int) (size - (endPos - beginPos)));
        return config;
    }

    public ResourceTable getResourceTable() {
        return this.resourceTable;
    }

    public Set<Locale> getLocales() {
        return this.locales;
    }
}
