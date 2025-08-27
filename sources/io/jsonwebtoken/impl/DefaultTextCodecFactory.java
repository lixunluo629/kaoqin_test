package io.jsonwebtoken.impl;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/DefaultTextCodecFactory.class */
public class DefaultTextCodecFactory implements TextCodecFactory {
    protected String getSystemProperty(String key) {
        return System.getProperty(key);
    }

    protected boolean isAndroid() {
        String name = getSystemProperty("java.vm.name");
        if (name != null) {
            String lcase = name.toLowerCase();
            return lcase.contains("dalvik");
        }
        String name2 = getSystemProperty("java.vm.vendor");
        if (name2 != null) {
            String lcase2 = name2.toLowerCase();
            return lcase2.contains("android");
        }
        return false;
    }

    @Override // io.jsonwebtoken.impl.TextCodecFactory
    public TextCodec getTextCodec() {
        if (isAndroid()) {
            return new AndroidBase64Codec();
        }
        return new Base64Codec();
    }
}
