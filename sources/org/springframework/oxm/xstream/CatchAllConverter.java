package org.springframework.oxm.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/xstream/CatchAllConverter.class */
public class CatchAllConverter implements Converter {
    public boolean canConvert(Class type) {
        return true;
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        throw new UnsupportedOperationException("Marshalling not supported");
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        throw new UnsupportedOperationException("Unmarshalling not supported");
    }
}
