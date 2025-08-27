package net.dongliu.apk.parser.utils.xml;

import java.io.IOException;
import java.io.Writer;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/xml/AggregateTranslator.class */
class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator... translators) {
        this.translators = translators;
    }

    @Override // net.dongliu.apk.parser.utils.xml.CharSequenceTranslator
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        for (CharSequenceTranslator translator : this.translators) {
            int consumed = translator.translate(input, index, out);
            if (consumed != 0) {
                return consumed;
            }
        }
        return 0;
    }
}
