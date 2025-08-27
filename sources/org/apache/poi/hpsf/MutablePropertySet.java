package org.apache.poi.hpsf;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.util.Removal;

@Removal(version = "3.18")
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/MutablePropertySet.class */
public class MutablePropertySet extends PropertySet {
    public MutablePropertySet() {
    }

    public MutablePropertySet(PropertySet ps) {
        super(ps);
    }

    MutablePropertySet(InputStream stream) throws MarkUnsupportedException, NoPropertySetStreamException, IOException {
        super(stream);
    }
}
