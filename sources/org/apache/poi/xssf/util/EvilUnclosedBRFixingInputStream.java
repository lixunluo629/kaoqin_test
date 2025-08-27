package org.apache.poi.xssf.util;

import java.io.InputStream;
import org.apache.poi.util.Internal;
import org.apache.poi.util.Removal;
import org.apache.poi.util.ReplacingInputStream;

@Removal(version = "3.18")
@Internal
@Deprecated
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/util/EvilUnclosedBRFixingInputStream.class */
public class EvilUnclosedBRFixingInputStream extends ReplacingInputStream {
    public EvilUnclosedBRFixingInputStream(InputStream source) {
        super(source, "<br>", "<br/>");
    }
}
