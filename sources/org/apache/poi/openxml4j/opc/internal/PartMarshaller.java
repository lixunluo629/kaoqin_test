package org.apache.poi.openxml4j.opc.internal;

import java.io.OutputStream;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.PackagePart;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/opc/internal/PartMarshaller.class */
public interface PartMarshaller {
    boolean marshall(PackagePart packagePart, OutputStream outputStream) throws OpenXML4JException;
}
