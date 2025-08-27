package org.apache.poi.hpsf;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.util.Removal;

@Removal(version = "3.18")
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/SpecialPropertySet.class */
public class SpecialPropertySet extends MutablePropertySet {
    public SpecialPropertySet() {
    }

    public SpecialPropertySet(PropertySet ps) throws UnexpectedPropertySetTypeException {
        super(ps);
    }

    SpecialPropertySet(InputStream stream) throws MarkUnsupportedException, NoPropertySetStreamException, IOException {
        super(stream);
    }
}
