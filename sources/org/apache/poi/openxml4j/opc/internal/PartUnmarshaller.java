package org.apache.poi.openxml4j.opc.internal;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.internal.unmarshallers.UnmarshallContext;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/opc/internal/PartUnmarshaller.class */
public interface PartUnmarshaller {
    PackagePart unmarshall(UnmarshallContext unmarshallContext, InputStream inputStream) throws InvalidFormatException, IOException;
}
