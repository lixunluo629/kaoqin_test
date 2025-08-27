package org.springframework.data.repository.init;

import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/init/UnmarshallingResourceReader.class */
public class UnmarshallingResourceReader implements ResourceReader {
    private final Unmarshaller unmarshaller;

    public UnmarshallingResourceReader(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Override // org.springframework.data.repository.init.ResourceReader
    public Object readFrom(Resource resource, ClassLoader classLoader) throws IOException {
        StreamSource source = new StreamSource(resource.getInputStream());
        return this.unmarshaller.unmarshal(source);
    }
}
