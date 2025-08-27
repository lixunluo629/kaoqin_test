package com.itextpdf.io.image;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.util.StreamUtil;
import com.itextpdf.io.util.UrlUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/GifImageData.class */
public class GifImageData {
    private float logicalHeight;
    private float logicalWidth;
    private List<ImageData> frames = new ArrayList();
    private byte[] data;
    private URL url;

    protected GifImageData(URL url) {
        this.url = url;
    }

    protected GifImageData(byte[] data) {
        this.data = data;
    }

    public float getLogicalHeight() {
        return this.logicalHeight;
    }

    public void setLogicalHeight(float logicalHeight) {
        this.logicalHeight = logicalHeight;
    }

    public float getLogicalWidth() {
        return this.logicalWidth;
    }

    public void setLogicalWidth(float logicalWidth) {
        this.logicalWidth = logicalWidth;
    }

    public List<ImageData> getFrames() {
        return this.frames;
    }

    protected byte[] getData() {
        return this.data;
    }

    protected URL getUrl() {
        return this.url;
    }

    protected void addFrame(ImageData frame) {
        this.frames.add(frame);
    }

    void loadData() throws IOException {
        InputStream input = null;
        try {
            input = UrlUtil.openStream(this.url);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            StreamUtil.transferBytes(UrlUtil.openStream(this.url), stream);
            this.data = stream.toByteArray();
            if (input != null) {
                input.close();
            }
        } catch (Throwable th) {
            if (input != null) {
                input.close();
            }
            throw th;
        }
    }
}
