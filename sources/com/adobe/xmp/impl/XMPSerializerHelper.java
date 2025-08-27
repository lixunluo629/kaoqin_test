package com.adobe.xmp.impl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.options.SerializeOptions;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/XMPSerializerHelper.class */
public class XMPSerializerHelper {
    public static void serialize(XMPMetaImpl xMPMetaImpl, OutputStream outputStream, SerializeOptions serializeOptions) throws XMPException {
        SerializeOptions serializeOptions2 = serializeOptions != null ? serializeOptions : new SerializeOptions();
        if (serializeOptions2.getSort()) {
            xMPMetaImpl.sort();
        }
        new XMPSerializerRDF().serialize(xMPMetaImpl, outputStream, serializeOptions2);
    }

    public static String serializeToString(XMPMetaImpl xMPMetaImpl, SerializeOptions serializeOptions) throws XMPException {
        SerializeOptions serializeOptions2 = serializeOptions != null ? serializeOptions : new SerializeOptions();
        serializeOptions2.setEncodeUTF16BE(true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2048);
        serialize(xMPMetaImpl, byteArrayOutputStream, serializeOptions2);
        try {
            return byteArrayOutputStream.toString(serializeOptions2.getEncoding());
        } catch (UnsupportedEncodingException e) {
            return byteArrayOutputStream.toString();
        }
    }

    public static byte[] serializeToBuffer(XMPMetaImpl xMPMetaImpl, SerializeOptions serializeOptions) throws XMPException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2048);
        serialize(xMPMetaImpl, byteArrayOutputStream, serializeOptions);
        return byteArrayOutputStream.toByteArray();
    }
}
