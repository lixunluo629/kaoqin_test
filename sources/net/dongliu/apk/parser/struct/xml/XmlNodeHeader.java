package net.dongliu.apk.parser.struct.xml;

import net.dongliu.apk.parser.struct.ChunkHeader;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/xml/XmlNodeHeader.class */
public class XmlNodeHeader extends ChunkHeader {
    private int lineNum;
    private int commentRef;

    public XmlNodeHeader(int chunkType, int headerSize, long chunkSize) {
        super(chunkType, headerSize, chunkSize);
    }

    public int getLineNum() {
        return this.lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getCommentRef() {
        return this.commentRef;
    }

    public void setCommentRef(int commentRef) {
        this.commentRef = commentRef;
    }
}
