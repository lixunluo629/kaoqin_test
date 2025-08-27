package org.apache.poi.ss.extractor;

import org.apache.poi.ss.usermodel.Shape;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/ss/extractor/EmbeddedData.class */
public class EmbeddedData {
    private String filename;
    private byte[] embeddedData;
    private Shape shape;
    private String contentType = "binary/octet-stream";

    public EmbeddedData(String filename, byte[] embeddedData, String contentType) {
        setFilename(filename);
        setEmbeddedData(embeddedData);
        setContentType(contentType);
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        if (filename == null) {
            this.filename = "unknown.bin";
        } else {
            this.filename = filename.replaceAll("[^/\\\\]*[/\\\\]", "").trim();
        }
    }

    public byte[] getEmbeddedData() {
        return this.embeddedData;
    }

    public void setEmbeddedData(byte[] embeddedData) {
        this.embeddedData = embeddedData == null ? null : (byte[]) embeddedData.clone();
    }

    public Shape getShape() {
        return this.shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
