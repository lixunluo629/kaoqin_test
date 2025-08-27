package net.dongliu.apk.parser.utils.xml;

import java.io.IOException;
import java.io.Writer;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/xml/UnicodeUnpairedSurrogateRemover.class */
class UnicodeUnpairedSurrogateRemover extends CodePointTranslator {
    UnicodeUnpairedSurrogateRemover() {
    }

    @Override // net.dongliu.apk.parser.utils.xml.CodePointTranslator
    public boolean translate(int codepoint, Writer out) throws IOException {
        if (codepoint >= 55296 && codepoint <= 57343) {
            return true;
        }
        return false;
    }
}
