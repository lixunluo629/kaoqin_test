package org.apache.poi.hpsf;

import java.io.UnsupportedEncodingException;
import org.apache.poi.util.Removal;

@Removal(version = "3.18")
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/MutableSection.class */
public class MutableSection extends Section {
    public MutableSection() {
    }

    public MutableSection(Section s) {
        super(s);
    }

    public MutableSection(byte[] src, int offset) throws UnsupportedEncodingException {
        super(src, offset);
    }
}
