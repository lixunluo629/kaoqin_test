package net.dongliu.apk.parser.utils.xml;

import java.io.IOException;
import java.io.Writer;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/xml/CodePointTranslator.class */
abstract class CodePointTranslator extends CharSequenceTranslator {
    public abstract boolean translate(int i, Writer writer) throws IOException;

    CodePointTranslator() {
    }

    @Override // net.dongliu.apk.parser.utils.xml.CharSequenceTranslator
    public final int translate(CharSequence input, int index, Writer out) throws IOException {
        int codepoint = Character.codePointAt(input, index);
        boolean consumed = translate(codepoint, out);
        return consumed ? 1 : 0;
    }
}
