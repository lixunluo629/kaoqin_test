package net.dongliu.apk.parser.parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import net.dongliu.apk.parser.bean.AttributeValues;
import net.dongliu.apk.parser.bean.Locales;
import net.dongliu.apk.parser.exception.ParserException;
import net.dongliu.apk.parser.struct.ChunkHeader;
import net.dongliu.apk.parser.struct.ResourceValue;
import net.dongliu.apk.parser.struct.StringPool;
import net.dongliu.apk.parser.struct.StringPoolHeader;
import net.dongliu.apk.parser.struct.resource.ResourceTable;
import net.dongliu.apk.parser.struct.xml.Attribute;
import net.dongliu.apk.parser.struct.xml.Attributes;
import net.dongliu.apk.parser.struct.xml.NullHeader;
import net.dongliu.apk.parser.struct.xml.XmlCData;
import net.dongliu.apk.parser.struct.xml.XmlHeader;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceStartTag;
import net.dongliu.apk.parser.struct.xml.XmlNodeEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNodeHeader;
import net.dongliu.apk.parser.struct.xml.XmlNodeStartTag;
import net.dongliu.apk.parser.struct.xml.XmlResourceMapHeader;
import net.dongliu.apk.parser.utils.Buffers;
import net.dongliu.apk.parser.utils.ParseUtils;
import net.dongliu.apk.parser.utils.Strings;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/BinaryXmlParser.class */
public class BinaryXmlParser {
    private StringPool stringPool;
    private String[] resourceMap;
    private ByteBuffer buffer;
    private XmlStreamer xmlStreamer;
    private final ResourceTable resourceTable;
    private static final Set<String> intAttributes = new HashSet(Arrays.asList("screenOrientation", "configChanges", "windowSoftInputMode", "launchMode", "installLocation", "protectionLevel"));
    private ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    private Locale locale = Locales.any;

    public BinaryXmlParser(ByteBuffer buffer, ResourceTable resourceTable) {
        this.buffer = buffer.duplicate();
        this.buffer.order(this.byteOrder);
        this.resourceTable = resourceTable;
    }

    public void parse() {
        ChunkHeader chunkHeader;
        ChunkHeader chunkHeader2 = readChunkHeader();
        if (chunkHeader2 == null) {
            return;
        }
        if ((chunkHeader2.getChunkType() != 3 && chunkHeader2.getChunkType() != 0) || (chunkHeader = readChunkHeader()) == null) {
            return;
        }
        ParseUtils.checkChunkType(1, chunkHeader.getChunkType());
        this.stringPool = ParseUtils.readStringPool(this.buffer, (StringPoolHeader) chunkHeader);
        ChunkHeader chunkHeader3 = readChunkHeader();
        if (chunkHeader3 == null) {
            return;
        }
        if (chunkHeader3.getChunkType() == 384) {
            long[] resourceIds = readXmlResourceMap((XmlResourceMapHeader) chunkHeader3);
            this.resourceMap = new String[resourceIds.length];
            for (int i = 0; i < resourceIds.length; i++) {
                this.resourceMap[i] = Attribute.AttrIds.getString(resourceIds[i]);
            }
            chunkHeader3 = readChunkHeader();
        }
        while (chunkHeader3 != null) {
            long beginPos = this.buffer.position();
            switch (chunkHeader3.getChunkType()) {
                case 256:
                    XmlNamespaceStartTag namespaceStartTag = readXmlNamespaceStartTag();
                    this.xmlStreamer.onNamespaceStart(namespaceStartTag);
                    break;
                case 257:
                    XmlNamespaceEndTag xmlNamespaceEndTag = readXmlNamespaceEndTag();
                    this.xmlStreamer.onNamespaceEnd(xmlNamespaceEndTag);
                    break;
                case 258:
                    readXmlNodeStartTag();
                    break;
                case 259:
                    readXmlNodeEndTag();
                    break;
                case 260:
                    readXmlCData();
                    break;
                default:
                    if (chunkHeader3.getChunkType() >= 256 && chunkHeader3.getChunkType() <= 383) {
                        Buffers.skip(this.buffer, chunkHeader3.getBodySize());
                        break;
                    } else {
                        throw new ParserException("Unexpected chunk type:" + chunkHeader3.getChunkType());
                    }
                    break;
            }
            Buffers.position(this.buffer, beginPos + chunkHeader3.getBodySize());
            chunkHeader3 = readChunkHeader();
        }
    }

    private XmlCData readXmlCData() {
        XmlCData xmlCData = new XmlCData();
        int dataRef = this.buffer.getInt();
        if (dataRef > 0) {
            xmlCData.setData(this.stringPool.get(dataRef));
        }
        xmlCData.setTypedData(ParseUtils.readResValue(this.buffer, this.stringPool));
        if (this.xmlStreamer != null) {
        }
        return xmlCData;
    }

    private XmlNodeEndTag readXmlNodeEndTag() {
        XmlNodeEndTag xmlNodeEndTag = new XmlNodeEndTag();
        int nsRef = this.buffer.getInt();
        int nameRef = this.buffer.getInt();
        if (nsRef > 0) {
            xmlNodeEndTag.setNamespace(this.stringPool.get(nsRef));
        }
        xmlNodeEndTag.setName(this.stringPool.get(nameRef));
        if (this.xmlStreamer != null) {
            this.xmlStreamer.onEndTag(xmlNodeEndTag);
        }
        return xmlNodeEndTag;
    }

    private XmlNodeStartTag readXmlNodeStartTag() {
        int nsRef = this.buffer.getInt();
        int nameRef = this.buffer.getInt();
        XmlNodeStartTag xmlNodeStartTag = new XmlNodeStartTag();
        if (nsRef > 0) {
            xmlNodeStartTag.setNamespace(this.stringPool.get(nsRef));
        }
        xmlNodeStartTag.setName(this.stringPool.get(nameRef));
        Buffers.readUShort(this.buffer);
        Buffers.readUShort(this.buffer);
        int attributeCount = Buffers.readUShort(this.buffer);
        Buffers.readUShort(this.buffer);
        Buffers.readUShort(this.buffer);
        Buffers.readUShort(this.buffer);
        Attributes attributes = new Attributes(attributeCount);
        for (int count = 0; count < attributeCount; count++) {
            Attribute attribute = readAttribute();
            if (this.xmlStreamer != null) {
                String value = attribute.toStringValue(this.resourceTable, this.locale);
                if (intAttributes.contains(attribute.getName()) && Strings.isNumeric(value)) {
                    try {
                        value = getFinalValueAsString(attribute.getName(), value);
                    } catch (Exception e) {
                    }
                }
                attribute.setValue(value);
                attributes.set(count, attribute);
            }
        }
        xmlNodeStartTag.setAttributes(attributes);
        if (this.xmlStreamer != null) {
            this.xmlStreamer.onStartTag(xmlNodeStartTag);
        }
        return xmlNodeStartTag;
    }

    private String getFinalValueAsString(String attributeName, String str) throws NumberFormatException {
        int value;
        value = Integer.parseInt(str);
        switch (attributeName) {
            case "screenOrientation":
                return AttributeValues.getScreenOrientation(value);
            case "configChanges":
                return AttributeValues.getConfigChanges(value);
            case "windowSoftInputMode":
                return AttributeValues.getWindowSoftInputMode(value);
            case "launchMode":
                return AttributeValues.getLaunchMode(value);
            case "installLocation":
                return AttributeValues.getInstallLocation(value);
            case "protectionLevel":
                return AttributeValues.getProtectionLevel(value);
            default:
                return str;
        }
    }

    private Attribute readAttribute() {
        int nsRef = this.buffer.getInt();
        int nameRef = this.buffer.getInt();
        Attribute attribute = new Attribute();
        if (nsRef > 0) {
            attribute.setNamespace(this.stringPool.get(nsRef));
        }
        attribute.setName(this.stringPool.get(nameRef));
        if (attribute.getName().isEmpty() && this.resourceMap != null && nameRef < this.resourceMap.length) {
            attribute.setName(this.resourceMap[nameRef]);
        }
        int rawValueRef = this.buffer.getInt();
        if (rawValueRef > 0) {
            attribute.setRawValue(this.stringPool.get(rawValueRef));
        }
        ResourceValue resValue = ParseUtils.readResValue(this.buffer, this.stringPool);
        attribute.setTypedValue(resValue);
        return attribute;
    }

    private XmlNamespaceStartTag readXmlNamespaceStartTag() {
        int prefixRef = this.buffer.getInt();
        int uriRef = this.buffer.getInt();
        XmlNamespaceStartTag nameSpace = new XmlNamespaceStartTag();
        if (prefixRef > 0) {
            nameSpace.setPrefix(this.stringPool.get(prefixRef));
        }
        if (uriRef > 0) {
            nameSpace.setUri(this.stringPool.get(uriRef));
        }
        return nameSpace;
    }

    private XmlNamespaceEndTag readXmlNamespaceEndTag() {
        int prefixRef = this.buffer.getInt();
        int uriRef = this.buffer.getInt();
        XmlNamespaceEndTag nameSpace = new XmlNamespaceEndTag();
        if (prefixRef > 0) {
            nameSpace.setPrefix(this.stringPool.get(prefixRef));
        }
        if (uriRef > 0) {
            nameSpace.setUri(this.stringPool.get(uriRef));
        }
        return nameSpace;
    }

    private long[] readXmlResourceMap(XmlResourceMapHeader chunkHeader) {
        int count = chunkHeader.getBodySize() / 4;
        long[] resourceIds = new long[count];
        for (int i = 0; i < count; i++) {
            resourceIds[i] = Buffers.readUInt(this.buffer);
        }
        return resourceIds;
    }

    private ChunkHeader readChunkHeader() {
        if (!this.buffer.hasRemaining()) {
            return null;
        }
        long begin = this.buffer.position();
        int chunkType = Buffers.readUShort(this.buffer);
        int headerSize = Buffers.readUShort(this.buffer);
        long chunkSize = Buffers.readUInt(this.buffer);
        switch (chunkType) {
            case 0:
                return new NullHeader(chunkType, headerSize, chunkSize);
            case 1:
                StringPoolHeader stringPoolHeader = new StringPoolHeader(headerSize, chunkSize);
                stringPoolHeader.setStringCount(Buffers.readUInt(this.buffer));
                stringPoolHeader.setStyleCount(Buffers.readUInt(this.buffer));
                stringPoolHeader.setFlags(Buffers.readUInt(this.buffer));
                stringPoolHeader.setStringsStart(Buffers.readUInt(this.buffer));
                stringPoolHeader.setStylesStart(Buffers.readUInt(this.buffer));
                Buffers.position(this.buffer, begin + headerSize);
                return stringPoolHeader;
            case 3:
                return new XmlHeader(chunkType, headerSize, chunkSize);
            case 256:
            case 257:
            case 258:
            case 259:
            case 260:
                XmlNodeHeader header = new XmlNodeHeader(chunkType, headerSize, chunkSize);
                header.setLineNum((int) Buffers.readUInt(this.buffer));
                header.setCommentRef((int) Buffers.readUInt(this.buffer));
                Buffers.position(this.buffer, begin + headerSize);
                return header;
            case 384:
                Buffers.position(this.buffer, begin + headerSize);
                return new XmlResourceMapHeader(chunkType, headerSize, chunkSize);
            default:
                throw new ParserException("Unexpected chunk type:" + chunkType);
        }
    }

    public void setLocale(Locale locale) {
        if (locale != null) {
            this.locale = locale;
        }
    }

    public Locale getLocale() {
        return this.locale;
    }

    public XmlStreamer getXmlStreamer() {
        return this.xmlStreamer;
    }

    public void setXmlStreamer(XmlStreamer xmlStreamer) {
        this.xmlStreamer = xmlStreamer;
    }
}
