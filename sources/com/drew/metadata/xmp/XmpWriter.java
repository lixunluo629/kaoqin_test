package com.drew.metadata.xmp;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.options.SerializeOptions;
import com.drew.metadata.Metadata;
import java.io.OutputStream;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/xmp/XmpWriter.class */
public class XmpWriter {
    public static boolean write(OutputStream os, Metadata data) {
        XmpDirectory dir = (XmpDirectory) data.getFirstDirectoryOfType(XmpDirectory.class);
        if (dir == null) {
            return false;
        }
        XMPMeta meta = dir.getXMPMeta();
        try {
            SerializeOptions so = new SerializeOptions().setOmitPacketWrapper(true);
            XMPMetaFactory.serialize(meta, os, so);
            return true;
        } catch (XMPException e) {
            e.printStackTrace();
            return false;
        }
    }
}
