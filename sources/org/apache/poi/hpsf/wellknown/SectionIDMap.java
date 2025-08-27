package org.apache.poi.hpsf.wellknown;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.hpsf.ClassID;
import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/wellknown/SectionIDMap.class */
public class SectionIDMap {
    private static ThreadLocal<Map<ClassID, PropertyIDMap>> defaultMap = new ThreadLocal<>();
    public static final ClassID SUMMARY_INFORMATION_ID = new ClassID("{F29F85E0-4FF9-1068-AB91-08002B27B3D9}");
    private static final ClassID DOC_SUMMARY_INFORMATION = new ClassID("{D5CDD502-2E9C-101B-9397-08002B2CF9AE}");
    private static final ClassID USER_DEFINED_PROPERTIES = new ClassID(POIXMLProperties.CustomProperties.FORMAT_ID);
    public static final ClassID[] DOCUMENT_SUMMARY_INFORMATION_ID = {DOC_SUMMARY_INFORMATION, USER_DEFINED_PROPERTIES};
    public static final String UNDEFINED = "[undefined]";

    public static SectionIDMap getInstance() {
        if (defaultMap.get() == null) {
            Map<ClassID, PropertyIDMap> m = new HashMap<>();
            m.put(SUMMARY_INFORMATION_ID, PropertyIDMap.getSummaryInformationProperties());
            m.put(DOCUMENT_SUMMARY_INFORMATION_ID[0], PropertyIDMap.getDocumentSummaryInformationProperties());
            defaultMap.set(m);
        }
        return new SectionIDMap();
    }

    public static String getPIDString(ClassID sectionFormatID, long pid) {
        String s;
        PropertyIDMap m = getInstance().get(sectionFormatID);
        if (m == null || (s = m.get((Object) Long.valueOf(pid))) == null) {
            return UNDEFINED;
        }
        return s;
    }

    public PropertyIDMap get(ClassID sectionFormatID) {
        return getInstance().get(sectionFormatID);
    }

    public PropertyIDMap put(ClassID sectionFormatID, PropertyIDMap propertyIDMap) {
        return getInstance().put(sectionFormatID, propertyIDMap);
    }

    protected PropertyIDMap put(String key, PropertyIDMap value) {
        return put(new ClassID(key), value);
    }
}
