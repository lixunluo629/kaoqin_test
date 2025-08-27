package net.dongliu.apk.parser;

import java.io.File;
import java.io.IOException;

@Deprecated
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/ApkParser.class */
public class ApkParser extends ApkFile {
    public ApkParser(File apkFile) throws IOException {
        super(apkFile);
    }

    public ApkParser(String filePath) throws IOException {
        super(filePath);
    }
}
